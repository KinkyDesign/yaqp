package org.opentox.algorithm.trainer;

import org.opentox.algorithm.ConstantParameters;
import org.opentox.algorithm.AlgorithmParameter;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.opentox.OpenToxApplication;
import org.opentox.resource.AbstractResource;
import org.opentox.resource.AbstractResource.Directories;
import org.opentox.resource.AbstractResource.URIs;
import org.opentox.algorithm.dataprocessing.DataCleanUp;
import org.opentox.algorithm.trainer.AbstractTrainer.Regression;
import org.opentox.error.ErrorRepresentation;
import org.opentox.client.opentoxClient;
import org.opentox.database.ModelsTable;
import org.opentox.ontology.meta.ModelMeta;
import org.opentox.ontology.rdf.Dataset;
import org.opentox.ontology.rdf.Model;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.ServerResource;
import weka.classifiers.functions.LinearRegression;
import weka.core.Instances;

/**
 * Trainer for MLR models.
 * @author OpenTox - http://www.opentox.org/
 * @author Sopasakis Pantelis
 * @author Sarimveis Harry
 * @version 1.3.3 (Last update: Dec 20, 2009)
 */
@Regression(name="MLR") public class MlrTrainer extends AbstractTrainer{

    protected Instances data;

    public MlrTrainer(Form form, ServerResource resource) {
        super(form, resource);
    }

    /**
     * Returns an ErrorRepresentation if something goes wrong or a StringRepresentation
     * with the URI of the created model, if the model is successfully created.
     * @return representation of the training result.
     */
    @Override
    public synchronized Representation train() {
        Representation representation = null;
        int model_id = 0;
        model_id = ModelsTable.INSTANCE.getModelsStack() + 1;


        errorRep = (ErrorRepresentation) checkParameters();

        if (errorRep.getErrorLevel() > 0) {
            representation = errorRep;
        } else {

            /**
             * Retrive the Dataset (RDF), parse it and generate the corresponding
             * weka.core.Instances object.
             */
            Dataset dataset = new Dataset(dataseturi);
            Model model = new Model();



            try {
                data = dataset.getWekaDatasetForTraining(null, false);
                data.setClass(data.attribute(targeturi.toString()));
                DataCleanUp.removeStringAtts(data);
                LinearRegression linreg = new LinearRegression();
                String[] linRegOptions = {"-S", "1", "-C"};



                linreg.setOptions(linRegOptions);
                linreg.buildClassifier(data);
                generatePMML(linreg.coefficients(), model_id);

                List<AlgorithmParameter> paramList = new ArrayList<AlgorithmParameter>();
                paramList.add(ConstantParameters.TARGET(targeturi.toString()));

                /**
                 * Store the model as RDF...
                 */
                model.createModel(new ModelMeta(Integer.toString(model_id),
                        dataseturi.toString(),
                        data,
                        paramList,
                        URIs.mlrAlgorithmURI),
                        new FileOutputStream(Directories.modelRdfDir + "/" + model_id));


                // return the URI of generated model:
                if (model.errorRep.getErrorLevel() == 0) {

                    representation = new StringRepresentation(AbstractResource.URIs.modelURI + "/"
                            + ModelsTable.INSTANCE.registerNewModel(
                            AbstractResource.URIs.mlrAlgorithmURI) + "\n");

                }

            } catch (NullPointerException ex) {
                OpenToxApplication.opentoxLogger.severe(ex.toString());
                errorRep.append(ex, "Probably this exception is thrown "
                        + "because the dataset or target uri you provided is not valid or some other internal "
                        + "server error happened! Please verify that the target uri you specified is an "
                        + "attribute of the dataset and is not of type 'string'!", Status.CLIENT_ERROR_BAD_REQUEST);
            } catch (Exception ex) {
                OpenToxApplication.opentoxLogger.severe(ex.toString());
                errorRep.append(ex, "Severe Error while trying to build an MLR model.", Status.SERVER_ERROR_INTERNAL);
            } catch (Throwable thr) {
                OpenToxApplication.opentoxLogger.severe(thr.toString());
                errorRep.append(thr, "An unexpected exception or error was thrown while "
                        + "training an MLR model. Please contact the system admnistrator for further information!",
                        Status.SERVER_ERROR_INTERNAL);
            } finally {
                errorRep.append(model.errorRep);
                errorRep.append(dataset.errorRep);
            }
        }

        return errorRep.getErrorLevel() == 0 ? representation : errorRep;
    }

    /**
     * Check wether the dataset and target values are valid URIs. 
     * @return Returns an empty ErrorRepresentation if no error is found (the posted parameters are
     * acceptable), otherwise a representation of the error. The internal status
     * is defined accordingly.
     */
    @Override
    public synchronized ErrorRepresentation checkParameters() {
        final Status clientPostedWrongParametersStatus = Status.CLIENT_ERROR_BAD_REQUEST;


        /**
         * Check the dataset_uri parameter.........
         */
        Thread check1 = new Thread() {

            @Override
            public void run() {
                String errorDetails = "";
                try {
                    dataseturi = new URI(form.getFirstValue("dataset_uri"));
                    dataseturi.toURL();
                    if (!(opentoxClient.IsMimeAvailable(dataseturi, MediaType.APPLICATION_RDF_XML, false))) {
                        errorRep.append(new Exception(), "The dataset uri that client provided "
                                + "does not seem to support the MIME: application/rdf+xml", clientPostedWrongParametersStatus);
                    }
                } catch (MalformedURLException ex) {
                    errorDetails = "The client did not post a valid URI for the dataset";
                    errorRep.append(ex, errorDetails, clientPostedWrongParametersStatus);
                } catch (URISyntaxException ex) {
                    errorDetails = "The client did not post a valid URI for the dataset";
                    errorRep.append(ex, errorDetails, clientPostedWrongParametersStatus);
                } catch (IllegalArgumentException ex) {
                    errorDetails = "The client did not post a valid URI for the dataset";
                    errorRep.append(ex, errorDetails, clientPostedWrongParametersStatus);
                } catch (NullPointerException ex) {
                    errorDetails = "It seems you forgot to post the dataset_uri parameter!";
                    errorRep.append(ex, errorDetails, clientPostedWrongParametersStatus);
                }

            }
        };

        /**
         * Check the target parameter.........
         */
        Thread check2 = new Thread() {

            @Override
            public void run() {
                String errorDetails;
                try {
                    targeturi = new URI(form.getFirstValue("target"));
                    targeturi.toURL();
                } catch (MalformedURLException ex) {
                    errorDetails = "[Wrong Posted Parameter ]: The client did"
                            + " not post a valid URI for the target feature";
                    errorRep.append(ex, errorDetails, clientPostedWrongParametersStatus);
                } catch (URISyntaxException ex) {
                    errorDetails = "[Wrong Posted Parameter ]: The client did"
                            + " not post a valid URI for the target feature";
                    errorRep.append(ex, errorDetails, clientPostedWrongParametersStatus);
                } catch (IllegalArgumentException ex) {
                    errorDetails = "The client did not post a valid URI for the target";
                    errorRep.append(ex, errorDetails, clientPostedWrongParametersStatus);
                } catch (NullPointerException ex) {
                    errorDetails = "It seems you forgot to post the target parameter!";
                    errorRep.append(ex, errorDetails, clientPostedWrongParametersStatus);
                }

            }
        };


        ExecutorService checker = Executors.newFixedThreadPool(2);
        checker.execute(check1);
        checker.execute(check2);
        checker.shutdown();

        /**
         * Wait until all checks are terminated!
         */
        while (!checker.isTerminated()) {
            try {
                wait(5);
            } catch (InterruptedException ex) {
            }
        }

        notifyAll();
        return errorRep;
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
        pmml.append("<target index=\"" + data.attribute(targeturi.toString()).index() + "\" name=\""
                + targeturi.toString() + "\"/>\n");
        pmml.append("</FeatureDefinitions>\n");
        pmml.append("<User>Guest</User>\n");
        pmml.append("<Timestamp>" + java.util.GregorianCalendar.getInstance().getTime() + "</Timestamp>\n");
        pmml.append("</Model>\n");

        pmml.append("<DataDictionary numberOfFields=\"" + data.numAttributes() + "\" >\n");
        for (int k = 0; k
                <= data.numAttributes() - 1; k++) {
            pmml.append("<DataField name=\"" + data.attribute(k).name()
                    + "\" optype=\"continuous\" dataType=\"double\" />\n");
        }
        pmml.append("</DataDictionary>\n");
        // RegressionModel
        pmml.append("<RegressionModel modelName=\"" + AbstractResource.URIs.modelURI + "/" + model_id + "\""
                + " functionName=\"regression\""
                + " modelType=\"linearRegression\""
                + " algorithmName=\"linearRegression\""
                + " targetFieldName=\"" + data.attribute(data.numAttributes() - 1).name() + "\""
                + ">\n");
        // RegressionModel::MiningSchema
        pmml.append("<MiningSchema>\n");
        for (int k = 0; k <= data.numAttributes() - 1; k++) {
            if (k != data.classIndex()) {
                pmml.append("<MiningField name=\""
                        + data.attribute(k).name() + "\" />\n");
            }
        }
        pmml.append("<MiningField name=\""
                + data.attribute(data.classIndex()).name() + "\" "
                + "usageType=\"predicted\"/>\n");
        pmml.append("</MiningSchema>\n");
        // RegressionModel::RegressionTable
        pmml.append("<RegressionTable intercept=\"" + coefficients[coefficients.length - 1] + "\">\n");

        for (int k = 0; k
                <= data.numAttributes() - 1; k++) {

            if (!(targeturi.toString().equals(data.attribute(k).name()))) {
                pmml.append("<NumericPredictor name=\""
                        + data.attribute(k).name() + "\" "
                        + " exponent=\"1\" "
                        + "coefficient=\"" + coefficients[k] + "\"/>\n");
            }
        }

        pmml.append("</RegressionTable>\n");
        pmml.append("</RegressionModel>\n");
        pmml.append("</PMML>\n\n");
        try {
            FileWriter fwriter = new FileWriter(AbstractResource.Directories.modelPmmlDir
                    + "/" + model_id);
            BufferedWriter writer = new BufferedWriter(fwriter);
            writer.write(pmml.toString());
            writer.flush();
            writer.close();
        } catch (IOException ex) {
            errorRep.append(ex, "Input/Output Exception while generating a PMML "
                    + "representation for an MLR model. Probably the destination does not exist.",
                    Status.SERVER_ERROR_INTERNAL);
            Logger.getLogger(MlrTrainer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
