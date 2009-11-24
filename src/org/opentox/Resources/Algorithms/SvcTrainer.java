package org.opentox.Resources.Algorithms;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.opentox.Applications.OpenToxApplication;
import org.opentox.Resources.*;
import org.opentox.Resources.AbstractResource.Directories;
import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.opentox.client.opentoxClient;
import org.opentox.util.libSVM.svm_train;
import org.opentox.util.libSVM.svm_scale;
import org.restlet.data.Form;
import weka.core.Instances;

/**
 *
 * @author OpenTox - http://www.opentox.org
 * @author Sopasakis Pantelis
 * @author Sarimveis Harry
 */
public class SvcTrainer extends AbstractTrainer{

    private String kernel;
    private String cost;
    private String gamma;
    private String epsilon;
    private String coeff0;
    private String degree;
    private String tolerance;
    private String cacheSize;
    private int model_id;
    private double d;
    private int i;
    private Instances dataInstances;

     public SvcTrainer(Form form){
        super(form);
        super.form = form;
    }

    @Override
    public Representation train() {
        
        model_id = org.opentox.Applications.OpenToxApplication.dbcon.getModelsStack() + 1;

        Representation representation = checkParameters();
        dataInstances = opentoxClient.getInstances(dataseturi);
        Preprocessing.removeStringAtts(dataInstances);
        weka.core.converters.LibSVMSaver saver = new weka.core.converters.LibSVMSaver();
        saver.setInstances(new Instances(dataInstances));

        File tempDsdFile = new File(Directories.tempDSDDir + "/" + CreateARandomFilename());
        while (tempDsdFile.exists()) {
            tempDsdFile = new File(Directories.tempDSDDir + "/" + CreateARandomFilename());
        }
        try {
            saver.setFile(tempDsdFile);
            saver.writeBatch();

            //Scaling data and saving a temp scaled data file (dsd format)
            svm_scale scaler = new svm_scale();
            String[] scalingOptions = {"-l", "-1", "-u", "1", "-s",
             Directories.rangesDir + "/" + model_id, tempDsdFile.getPath()};

            File tempScaledFile = new File(Directories.tempScaledDir + "/" + CreateARandomFilename());
            while (tempScaledFile.exists()) {
                tempScaledFile = new File(Directories.tempScaledDir + "/" + CreateARandomFilename());
            }
            try {
                scaler.scale(scalingOptions, tempScaledFile.toString());
            } catch (IOException ex) {
                Logger.getLogger(Regression.class.getName()).log(Level.SEVERE, null, ex);
            }


            String[] options = getSvcOptions(tempDsdFile.toString(), Directories.svcModel + "/" + model_id);

            /**
             * If all the posted parameters (kernel type, cost, gamma, etc)
             * are acceptable the the status is 202.
             */
            if (Status.SUCCESS_ACCEPTED.equals(internalStatus)) {
                try {

                    representation = new StringRepresentation(internalStatus.toString(),
                            MediaType.TEXT_PLAIN);
                    svm_train.main(options);
                    representation = new StringRepresentation(
                            AbstractResource.URIs.modelURI + "/" + model_id +
                            "\n\n", MediaType.TEXT_PLAIN);

                    /**
                     * Check if the model was created.
                     * If yes, set the status to 200,
                     * otherwise the status is set to 500
                     */
                    File modelFile = new File(Directories.svcModel + "/" + model_id);
                    boolean modelCreated = modelFile.exists();
                    if (!(modelCreated)) {
                        representation = new StringRepresentation(
                                "Error 400: Client Error, Bad Requset\n" +
                                "The server encountered an unexpected condition " +
                                "which prevented it from fulfilling the request." +
                                "Details: Unexpected Error while trying to train the model." + "\n\n",
                                MediaType.TEXT_PLAIN);
                        setInternalStatus(Status.SERVER_ERROR_INTERNAL);
                    } else {
                        /** if the model was successfully created... **/
                        org.opentox.Applications.OpenToxApplication.dbcon.
                                registerNewModel(AbstractResource.URIs.svcAlgorithmURI);
                        setInternalStatus(Status.SUCCESS_OK);
                    }

                    /**
                     * If the model was successfully created, that is the
                     * status is 200, return a report to the user in
                     * HTML form. This is for testing reasons only as according to
                     * the OpenTox API specification, the URI of the trained model
                     * should be returned
                     */
                    if (internalStatus.equals(Status.SUCCESS_OK)) {

                        representation = new StringRepresentation(
                                AbstractResource.URIs.mlrAlgorithmURI + "/" +
                                model_id + "\n\n", MediaType.TEXT_PLAIN);
                        String xmlstr = xmlString();
                        try {
                            FileWriter fstream = new FileWriter(Directories.modelXmlDir + "/" + model_id);
                            BufferedWriter out = new BufferedWriter(fstream);
                            out.write(xmlstr);
                            out.flush();
                            out.close();
                        } catch (Exception e) {
                            System.err.println("Error: " + e.getMessage());
                        }
                    }
                } catch (IOException ex) {
                    OpenToxApplication.opentoxLogger.log(Level.SEVERE, null, ex);
                }
            }

        } catch (IOException ex) {
            Logger.getLogger(SvcTrainer.class.getName()).log(Level.SEVERE, null, ex);
        }



        return representation;
    }

    private String xmlString() {
        StringBuilder xmlstr = new StringBuilder();
        xmlstr.append(AbstractResource.xmlIntro);
        xmlstr.append("<ot:Model xmlns:ot=\"http://opentox.org/1.0/\" " +
                "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" " +
                "xsi:schemaLocation=\"http://opentox.org/1.0/Algorithm.xsd\" " +
                "ID=\"" + model_id + "\" Name=\"SVM Classification Model\">\n");
        xmlstr.append("<ot:link href=\"" + AbstractResource.URIs.modelURI + "/" + model_id + "\" />\n");
        xmlstr.append("<ot:AlgorithmID href=\"" + AbstractResource.URIs.svcAlgorithmURI + "\"/>\n");
        xmlstr.append("<DatasetID href=\"\"/>\n");
        xmlstr.append("<AlgorithmParameters>\n");
        xmlstr.append("<param name=\"kernel\"  type=\"string\">" + kernel + "</param>\n");
        xmlstr.append("<param name=\"cost\"  type=\"double\">" + cost + "</param>\n");
        xmlstr.append("<param name=\"gamma\"  type=\"double\">" + gamma + "</param>\n");
        xmlstr.append("<param name=\"coeff0\"  type=\"double\">" + coeff0 + "</param>\n");
        xmlstr.append("<param name=\"degree\"  type=\"int\">" + degree + "</param>\n");
        xmlstr.append("<param name=\"tolerance\"  type=\"double\">" + tolerance + "</param>\n");
        xmlstr.append("<param name=\"cacheSize\"  type=\"double\">" + cacheSize + "</param>\n");
        xmlstr.append("</AlgorithmParameters>\n");
        xmlstr.append("<FeatureDefinitions>\n");
        xmlstr.append("</FeatureDefinitions>\n");
        xmlstr.append("<User>Guest</User>\n");
        xmlstr.append("<Timestamp>" + java.util.GregorianCalendar.getInstance().getTime() + "</Timestamp>\n");
        xmlstr.append("</ot:Model>\n");
        return xmlstr.toString();
    }

    private String CreateARandomFilename(){
        String key = new String();
        int flag = (int) (Math.random() * 10) + 20;

        int rand = 0;
        for (int x = 0; x < flag; x++) {
            rand = ((int) (Math.random() * 10)) % 3;
            if ((rand == 0)) {
                key += (int) (Math.random() * 10);
            } else if ((rand == 1)) {
                key += (char) (Math.random() * 26 + 65);
            } else {
                key += (char) (Math.random() * 26 + 97);
            }
        }
        return key;
    }
    private String[] getSvcOptions(String scaledPath, String modelPath) {
        String[] options = {""};


        String ker = "";
        if (kernel.equalsIgnoreCase("linear")) {
            ker = "0";
        } else if (kernel.equalsIgnoreCase("polynomial")) {
            ker = "1";
        } else if (kernel.equalsIgnoreCase("rbf")) {
            ker = "2";
        } else if (kernel.equalsIgnoreCase("sigmoid")) {
            ker = "3";
        } else {
            ker = "2";
        }


        if (ker.equalsIgnoreCase("0")) {
            String[] ops = {
                "-s", "3",// epsilon-SVr
                "-t", "0",
                "-c", cost,
                "-e", tolerance,
                "-q",
                scaledPath,
                modelPath
            };
            options =
                    ops;
        } else if (ker.equalsIgnoreCase("1")) {
            String[] ops = {
                "-s", "3",// epsilon-SVr
                "-t", ker,
                "-c", cost,
                "-g", gamma,
                "-d", degree,
                "-r", coeff0,
                "-e", tolerance,
                "-q",
                scaledPath,
                modelPath
            };
            options =
                    ops;
        } else if (ker.equalsIgnoreCase("2")) {
            String[] ops = {
                "-s", "3",// epsilon-SVr
                "-t", ker,
                "-c", cost,
                "-g", gamma,
                "-e", tolerance,
                "-q",
                scaledPath,
                modelPath
            };
            options =
                    ops;
        } else if (ker.equalsIgnoreCase("3")) {
            String[] ops = {
                "-s", "3",// epsilon-SVr
                "-t", ker,
                "-c", cost,
                "-g", gamma,
                "-e", tolerance,
                "-q",
                scaledPath,
                modelPath
            };
            options =
                    ops;
        }
        return options;
    }

    @Override
    public Representation checkParameters() {
        Representation rep = null;
        MediaType errorMediaType = MediaType.TEXT_PLAIN;
        Status clientPostedWrongParametersStatus = Status.CLIENT_ERROR_BAD_REQUEST;
        String errorDetails = "";


        setInternalStatus(Status.SUCCESS_ACCEPTED);

        kernel = form.getFirstValue("kernel");
        if (kernel == null) {
            kernel = "RBF";
        }
        cost = form.getFirstValue("cost");
        if (cost == null) {
            cost = "100";
        }
        gamma = form.getFirstValue("gamma");
        if (gamma == null) {
            gamma = "1.5";
        }
        epsilon = form.getFirstValue("epsilon");
        if (epsilon == null) {
            epsilon = "0.1";
        }
        coeff0 = form.getFirstValue("coeff0");
        if (coeff0 == null) {
            coeff0 = "0";
        }
        degree = form.getFirstValue("degree");
        if (degree == null) {
            degree = "3";
        }
        tolerance = form.getFirstValue("tolerance");
        if (tolerance == null) {
            tolerance = "0.0001";
        }
        cacheSize = form.getFirstValue("cacheSize");
        if (cacheSize == null) {
            cacheSize = "50";
        }

        /**
         * Get and Check the posted dataset.
         * Check whether the posted dataset id parameter is indeed
         * a URI.
         */
        try {
            dataseturi = new URI(form.getFirstValue("dataset"));
            dataInstances = opentoxClient.getInstances(dataseturi);
        } catch (URISyntaxException ex) {
            setInternalStatus(clientPostedWrongParametersStatus);
            errorDetails = errorDetails + "[Wrong Posted Parameter ]: The client did" +
                    " not post a valid URI for the dataset";
        }



        if (!((kernel.equalsIgnoreCase("rbf")) ||
                (kernel.equalsIgnoreCase("linear")) ||
                (kernel.equalsIgnoreCase("sigmoid")) ||
                (kernel.equalsIgnoreCase("polynomial")))) {
            errorDetails = errorDetails + "* [Inacceptable Parameter Value] Invalid Kernel Type!\n";
            setInternalStatus(clientPostedWrongParametersStatus);
        }


        /**
         * Cost should be convertible to Double and strictly
         * positive.
         */
        try {
            d = Double.parseDouble(cost);
            if (d <= 0) {
                errorDetails = errorDetails +
                        "* [Inacceptable Parameter Value] The cost should be strictly positive\n";
                setInternalStatus(clientPostedWrongParametersStatus);
            }
        } catch (NumberFormatException e) {
            errorDetails = errorDetails +
                    "* [Inacceptable Parameter Value]  The cost should be Double type, while you specified " +
                    "a non double value : " + cost + "\n";
            setInternalStatus(clientPostedWrongParametersStatus);
        }


        /**
         * Epsilon should be convertible to Double and strictly
         * positive.
         */
        try {
            d = Double.parseDouble(epsilon);
            if (d <= 0) {
                errorDetails = errorDetails +
                        "* [Inacceptable Parameter Value] Epsinlon must be strictly positive!\n";
                setInternalStatus(clientPostedWrongParametersStatus);
            }
        } catch (NumberFormatException e) {
            errorDetails = errorDetails +
                    "* [Inacceptable Parameter Value] Epsilon must be a striclty positive number!\n";
            setInternalStatus(clientPostedWrongParametersStatus);
        }


        /**
         * Degree should be a strictly positive integer
         */
        try {
            i = Integer.parseInt(degree);
            if (i <= 0) {
                errorDetails = errorDetails +
                        "* [Inacceptable Parameter Value] The degree must be a strictly positive integer!\n";
                setInternalStatus(clientPostedWrongParametersStatus);
            }
        } catch (NumberFormatException e) {
            errorDetails = errorDetails +
                    "* [Inacceptable Parameter Value] The degree must be a strictly positive integer!\n";
            setInternalStatus(clientPostedWrongParametersStatus);
        }



        /**
         * Gamma should be convertible to Double and strictly
         * positive.
         */
        try {
            d = Double.parseDouble(gamma);
            if (d <= 0) {
                errorDetails = errorDetails +
                        "* [Inacceptable Parameter Value] gamma must be a strictly positive double!\n";
                setInternalStatus(clientPostedWrongParametersStatus);
            }
        } catch (NumberFormatException e) {
            errorDetails = errorDetails +
                    "* [Inacceptable Parameter Value] gamma must be a strictly positive double!\n";
            setInternalStatus(clientPostedWrongParametersStatus);
        }


        /**
         * coeff0 should be convertible to Double.
         */
        try {
            d = Double.parseDouble(coeff0);
        } catch (NumberFormatException e) {
            errorDetails = errorDetails + "* [Inacceptable Parameter Value] coeff must be a number!\n";
            setInternalStatus(clientPostedWrongParametersStatus);
        }


        /**
         * Tolerance
         */
        try {
            d = Double.parseDouble(tolerance);
            if (d <= 0) {
                errorDetails = errorDetails +
                        "* [Inacceptable Parameter Value] Tolerance must be a strictly positive double (preferably small)!\n";
                setInternalStatus(clientPostedWrongParametersStatus);
            }
        } catch (NumberFormatException e) {
            errorDetails = errorDetails +
                    "* [Inacceptable Parameter Value] Tolerance must be a strictly positive double (preferably small)!\n";
            setInternalStatus(clientPostedWrongParametersStatus);
        }



        /**
         * cache size
         */
        try {
            i = Integer.parseInt(cacheSize);
            if (d <= 0) {
                setInternalStatus(clientPostedWrongParametersStatus);
            }
        } catch (NumberFormatException e) {
            rep = new StringRepresentation(
                    "Error 400: Client Error, Bad Requset\n" +
                    "The request could not be understood by the server due " +
                    "to malformed syntax.\n" +
                    "Details: cache size (in MB) should be an Integer, while you specified " +
                    "a non Integer value : " + cacheSize + "\n\n",
                    errorMediaType);
            setInternalStatus(clientPostedWrongParametersStatus);
        }

        if (!(errorDetails.equalsIgnoreCase(""))) {
            rep = new StringRepresentation("Error Code : " + clientPostedWrongParametersStatus.toString() + "\n" +
                    "Error Code Desription : The request could not be understood by the server due to " +
                    "malformed syntax.\nThe client SHOULD NOT repeat the request without modifications.\n" +
                    "Error Explanation :\n" + errorDetails + "\n", errorMediaType);
            return rep;
        } else {
            return null;
        }
    }
}
