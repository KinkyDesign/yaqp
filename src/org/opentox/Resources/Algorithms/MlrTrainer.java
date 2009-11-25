package org.opentox.Resources.Algorithms;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.opentox.Applications.OpenToxApplication;
import org.opentox.Resources.AbstractResource;
import org.opentox.client.opentoxClient;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import weka.classifiers.functions.LinearRegression;
import weka.core.Instances;

/**
 *
 * @author OpenTox - http://www.opentox.org
 * @author Sopasakis Pantelis
 * @author Sarimveis Harry
 */
public class MlrTrainer extends AbstractTrainer {

    protected Instances data;

    public MlrTrainer(Form form) {
        super(form);
    }

    @Override
    public synchronized Representation train() {
        Representation representation = null;
        int model_id = 0;
        model_id = org.opentox.Applications.OpenToxApplication.dbcon.getModelsStack() + 1;


        Representation errorRep = checkParameters();
        if (errorRep != null) {
            representation = errorRep;
        }

        if (Status.SUCCESS_ACCEPTED.equals(internalStatus)) {
            
            data = opentoxClient.getInstances(dataseturi);

            try {
                data.setClass(data.attribute(targeturi.toString()));
                Preprocessing.removeStringAtts(data);
                LinearRegression linreg = new LinearRegression();
                String[] linRegOptions = {"-S", "1", "-C"};
                try {

                    linreg.setOptions(linRegOptions);
                    linreg.buildClassifier(data);
                    generatePMML(linreg.coefficients(), model_id);
                    setInternalStatus(Status.SUCCESS_OK);
                    representation = new StringRepresentation(AbstractResource.URIs.modelURI + "/" +
                            OpenToxApplication.dbcon.registerNewModel(
                            AbstractResource.URIs.mlrAlgorithmURI) + "\n");
                } catch (Exception ex) {
                    OpenToxApplication.opentoxLogger.severe("Severe Error while trying to build an MLR model.\n" +
                            "Details :" + ex.getMessage() + "\n");
                    representation = new StringRepresentation("Severe Error while trying to build an MLR model.\n" +
                            "Details :" + ex + "\n");
                    setInternalStatus(Status.SERVER_ERROR_INTERNAL);
                }
            } catch (NullPointerException ex) {
                OpenToxApplication.opentoxLogger.severe("Severe Error while trying to build an MLR model.\n" +
                        "Details :" + ex.getMessage() + "\n");
                representation = new StringRepresentation("Probably this exception is thrown" +
                        "because the dataset or target uri you provided is not valid or some other internal" +
                        "server error happened!\n"+
                        "Exception Details: "+ ex+"\n");
                setInternalStatus(Status.SERVER_ERROR_INTERNAL);
            }
        }
        return representation;
    }

    /**
     * Check wether the dataset and target values are valid URIs. The internal
     * status is 202 (Accepted) if the posted data are accepted, or 400
     * (Client Error, Bad Requested) otherwise.
     * @return Returns null if no error is found (the posted parameters are
     * acceptable), otherwise a representation of the error. The internal status
     * is defined accordingly.
     */
    @Override
    public Representation checkParameters() {
        Representation rep = null;
        MediaType errorMediaType = MediaType.TEXT_PLAIN;
        Status clientPostedWrongParametersStatus = Status.CLIENT_ERROR_BAD_REQUEST;
        String errorDetails = "";
        setInternalStatus(Status.SUCCESS_ACCEPTED);


        try {
            dataseturi = new URI(form.getFirstValue("dataset"));
        } catch (URISyntaxException ex) {
            setInternalStatus(clientPostedWrongParametersStatus);
            errorDetails = errorDetails + "[Wrong Posted Parameter ]: The client did" +
                    " not post a valid URI for the dataset";
        }
        try {
            targeturi = new URI(form.getFirstValue("target"));
        } catch (URISyntaxException ex) {
            setInternalStatus(clientPostedWrongParametersStatus);
            errorDetails = errorDetails + "[Wrong Posted Parameter ]: The client did" +
                    " not post a valid URI for the target feature";
        }
        if (!(errorDetails.equalsIgnoreCase(""))) {
            rep = new StringRepresentation(
                    "Error Code            : " + clientPostedWrongParametersStatus.toString() + "\n" +
                    "Error Code Desription : The request could not be understood by the server due to " +
                    "malformed syntax.\nThe client SHOULD NOT repeat the request without modifications.\n" +
                    "Error Explanation     :\n" + errorDetails + "\n", errorMediaType);
            return rep;
        } else {
            setInternalStatus(Status.SUCCESS_ACCEPTED);
            return null;
        }
    }

    /**
     * Generates the PMML representation of the model and stores in the hard
     * disk.
     * @param coefficients The vector of the coefficients of the MLR model.
     * @param model_id The id of the generated model.
     */
    private void generatePMML(double[] coefficients, int model_id) {
        StringBuilder pmml = new StringBuilder();
        pmml.append(AbstractResource.xmlIntro);
        pmml.append(AbstractResource.PMMLIntro);
        pmml.append("<Model ID=\"" + model_id + "\" Name=\"MLR Model\">\n");
        pmml.append("<link href=\"" + AbstractResource.URIs.modelURI + "/" + model_id + "\" />\n");
        pmml.append("<AlgorithmID href=\"" + AbstractResource.URIs.mlrAlgorithmURI + "\"/>\n");
        pmml.append("<DatasetID href=\"" + dataseturi.toString() + "\"/>\n");
        pmml.append("<AlgorithmParameters />\n");
        pmml.append("<FeatureDefinitions>\n");
        for (int k = 1; k <= data.numAttributes(); k++) {
            pmml.append("<link href=\"" + data.attribute(k - 1).name() + "\"/>\n");
        }
        pmml.append("<target index=\"" + data.attribute(targeturi.toString()).index() + "\" name=\"" +
                targeturi.toString() + "\"/>\n");
        pmml.append("</FeatureDefinitions>\n");
        pmml.append("<User>Guest</User>\n");
        pmml.append("<Timestamp>" + java.util.GregorianCalendar.getInstance().getTime() + "</Timestamp>\n");
        pmml.append("</Model>\n");

        pmml.append("<DataDictionary numberOfFields=\"" + data.numAttributes() + "\" >\n");
        for (int k = 0; k <=
                data.numAttributes() - 1; k++) {
            pmml.append("<DataField name=\"" + data.attribute(k).name() +
                    "\" optype=\"continuous\" dataType=\"double\" />\n");
        }
        pmml.append("</DataDictionary>\n");
        // RegressionModel
        pmml.append("<RegressionModel modelName=\"" + AbstractResource.URIs.modelURI + "/" + model_id + "\"" +
                " functionName=\"regression\"" +
                " modelType=\"linearRegression\"" +
                " algorithmName=\"linearRegression\"" +
                " targetFieldName=\"" + data.attribute(data.numAttributes() - 1).name() + "\"" +
                ">\n");
        // RegressionModel::MiningSchema
        pmml.append("<MiningSchema>\n");
        for (int k = 0; k <= data.numAttributes() - 1; k++) {
            if (k != data.classIndex()) {
                pmml.append("<MiningField name=\"" +
                        data.attribute(k).name() + "\" />\n");
            }
        }
        pmml.append("<MiningField name=\"" +
                data.attribute(data.classIndex()).name() + "\" " +
                "usageType=\"predicted\"/>\n");
        pmml.append("</MiningSchema>\n");
        // RegressionModel::RegressionTable
        pmml.append("<RegressionTable intercept=\"" + coefficients[coefficients.length - 1] + "\">\n");
        for (int k = 0; k <=
                data.numAttributes() - 2; k++) {
            pmml.append("<NumericPredictor name=\"" +
                    data.attribute(k).name() + "\" " +
                    " exponent=\"1\" " +
                    "coefficient=\"" + coefficients[k] + "\"/>\n");
        }
        pmml.append("</RegressionTable>\n");
        pmml.append("</RegressionModel>\n");
        pmml.append("</PMML>\n\n");
        try {
            FileWriter fwriter = new FileWriter(AbstractResource.Directories.modelXmlDir +
                    "/" + model_id);
            BufferedWriter writer = new BufferedWriter(fwriter);
            writer.write(pmml.toString());
            writer.flush();
            writer.close();
            System.out.println(pmml.toString());
        } catch (IOException ex) {
            Logger.getLogger(MlrTrainer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}