package org.opentox.Resources.Algorithms;

import org.opentox.Resources.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.restlet.Context;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Reference;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.data.Status;
import org.restlet.resource.Representation;
import org.restlet.resource.StringRepresentation;
import org.restlet.resource.Variant;

import weka.classifiers.functions.LinearRegression;
import weka.core.Instances;

import org.opentox.util.svm_train;

/**
 * Resource for regression algorithms.
 * <p style="width:50%">
 * Implemented Algorithms:
 * <ul>
 *  <li>MLR</li>
 * </ul>
 * Algorithms Not implemented yet:
 * <ul>
 *  <li>SVM (Under Developement)</li>
 *  <li>Neural Network</li>
 *  <li>PLS</li>
 * </ul>
 * </p>
 * <p style="width:50%">
 * <b>URI:</b> /algorithm/learningalgorithm/regression/{id}<br/>
 * <b>Allowed Methods:</b> GET, POST<br/>
 * <b>Status Codes for POST:</b> 200(Success, OK),202(Success, Accepted but not Completed yet),
 * 400(Bad Request),404(Not Found) ,500(Internal Server Error) ,503(Service Unavailable) <br/>
 * <b>Supported Mediatypes for GET:</b> TEXT_XML
 * </p>
 *
 * @author OpenTox - http://www.opentox.org
 * @author Sopasakis Pantelis
 * @author Sarimveis Harry
 * @version 1.7 (Last Update: Aug 20, 2009)
 */
public class Regression extends AbstractResource {

    private static final long serialVersionUID = 10012190006007017L;
    private volatile String algorithmId;
    private int i;
    private double d;
    private String dataid, kernel, degree, cacheSize,
            cost, epsilon, gamma, coeff0, tolerance;

    public Regression(Context context, Request request, Response response) {
        super(context, request, response);
        getVariants().add(new Variant(MediaType.TEXT_HTML));

        this.algorithmId = Reference.decode(request.getAttributes().get("id").toString());
    }

    /**
     * Implementation of the GET method.
     * Returns XML representations for the supported regression algorithms
     * @param variant
     * @return XML representation of algorithm
     */
    @Override
    public Representation represent(Variant variant) {

        StringBuilder builder = new StringBuilder();
        builder.append(xmlIntro);

        MediaType Media = null;

        if (algorithmId.equalsIgnoreCase("svm")) {
            builder.append("<algorithm name=\"Support Vector Machine\" id=\"svm\">\n");
            builder.append("<AlgorithmType>regression</AlgorithmType>\n");
            builder.append("<Parameters>\n");
            builder.append("<!-- \n" +
                    "The id of the dataset used for the training\n" +
                    "of the model\n" +
                    "-->\n");
            builder.append("<param type=\"String\" defaultvalue=\"0\">dataId</param>\n");
            builder.append("<param type=\"String\" defaultvalue=\"RBF\">kernel</param>\n");
            builder.append("<param type=\"Double\" defaultvalue=\"10\">cost</param>\n");
            builder.append("<param type=\"Double\" defaultvalue=\"0.1\">epsilon</param>\n");
            builder.append("<param type=\"Double\" defaultvalue=\"1\">gamma</param>\n");
            builder.append("<param type=\"Double\" defaultvalue=\"0\">coeff0</param>\n");
            builder.append("<param type=\"Integer\" defaultvalue=\"3\">degree</param>\n");
            builder.append("<param type=\"Double\" defaultvalue=\"1E-4\">tolerance</param>\n");
            builder.append("<param type=\"Integer\" defaultvalue=\"50\">cacheSize</param>\n");
            builder.append("</Parameters>\n");
            builder.append("<statisticsSupported>\n");
            builder.append("<statistic>RMSE</statistic>\n");
            builder.append("<statistic>MSE</statistic>\n");
            builder.append("</statisticsSupported>\n");
            builder.append("</algorithm>\n\n");
            Media = MediaType.TEXT_XML;

        } else if (algorithmId.equalsIgnoreCase("plsr")) {
            builder.append("<algorithm name=\"Partial Least Squares\" id=\"pls\">\n");
            builder.append("<AlgorithmType>regression</AlgorithmType>\n");
            builder.append("<Parameters>\n");
            builder.append("<!-- \n" +
                    "The id of the dataset used for the training\n" +
                    "of the model\n" +
                    "-->\n");
            builder.append("<param type=\"String\" defaultvalue=\"0\">dataId</param>\n");
            builder.append("<param type=\"Integer\" defaultvalue=\"1\">nComp</param>\n");
            builder.append("</Parameters>\n");
            builder.append("<statisticsSupported>\n");
            builder.append("<statistic>x</statistic>\n");
            builder.append("</statisticsSupported>\n");
            Media = MediaType.TEXT_XML;
            builder.append("</algorithm>\n\n");
        } else if (algorithmId.equalsIgnoreCase("mlr")) {
            builder.append("<algorithm name=\"Multiple Linear Regression\" id=\"mlr\">\n");
            builder.append("<AlgorithmType>regression</AlgorithmType>\n");
            builder.append("<Parameters>\n");
            builder.append("<!-- \n" +
                    "The id of the dataset used for the training\n" +
                    "of the model\n" +
                    "-->\n");
            builder.append("<param type=\"String\" defaultvalue=\"0\">dataId</param>\n");
            builder.append("</Parameters>\n");
            builder.append("<statisticsSupported>\n");
            builder.append("<statistic>RootMeanSquaredError</statistic>\n");
            builder.append("<statistic>RelativeAbsoluteError</statistic>\n");
            builder.append("<statistic>RootRelativeSquaredError</statistic>\n");
            builder.append("<statistic>MeanAbsolutError</statistic>\n");
            builder.append("</statisticsSupported>\n");
            builder.append("</algorithm>\n\n");
            Media = MediaType.TEXT_XML;
        } else if (algorithmId.equalsIgnoreCase("xsd")) {
            builder.append(
                    "<schema xmlns:xs=\"http://www.w3.org/2001/XMLSchema\" " +
                    "targetNamespace=\"http://www.opentox.org/Algorithm/1.0\" " +
                    "xmlns:tns=\"http://www.opentox.org/Algorithm/1.0\" " +
                    "elementFormDefault=\"qualified\">\n" +
                    "<complexType name=\"Algorithm\">\n" +
                    "<xs:attribute name=\"ID\" type=\"xs:string\" " +
                    "use=\"required\">\n" +
                    "</xs:attribute><xs:attribute name=\"Name\" " +
                    "type=\"xs:string\" use=\"required\"></xs:attribute>\n" +
                    "<xs:element name=\"AlgorithmType\" type=\"xs:string\">\n" +
                    "</xs:element>\n" +
                    "<xs:element name=\"Parameters\">\n" +
                    "<xs:complexType>\n" +
                    "<xs:sequence>\n" +
                    "<xs:element name=\"param\" type=\"xs:string\">\n" +
                    "<xs:attribute name=\"type\" use=\"required\">\n" +
                    "</xs:attribute>\n" +
                    "<xs:attribute name=\"defaultvalue\" use=\"required\">\n" +
                    "</xs:attribute>\n" +
                    "</xs:element>\n" +
                    "</xs:sequence></xs:complexType></xs:element>\n" +
                    "<xs:element name=\"statisticsSupported\"><xs:complexType>\n" +
                    "<xs:sequence>\n<xs:element name=\"statistic\" type=\"xs:string\">\n" +
                    "</xs:element>\n</xs:sequence>\n</xs:complexType>\n</xs:element>\n</complexType>\n</schema>\n\n");
            Media = MediaType.APPLICATION_W3C_SCHEMA_XML;
        } else //Not Found!
        {
            builder.append("<algorithm name=\"unknown\" id=\"" + algorithmId + "\">\n");

            builder.append("<status code=\"404\">The server has not found anything matching the Request-URI or the server does not" +
                    " wish to reveal exactly why the request has been refused, or no other response is applicable.</status>\n\n");
            getResponse().setStatus(Status.CLIENT_ERROR_NOT_FOUND);
            Media = MediaType.TEXT_XML;
            builder.append("</algorithm>\n\n");
        }
        return new StringRepresentation(builder.toString(), Media);
    }

    /**
     * Allow POST
     */
    @Override
    public boolean allowPost() {
        return true;
    }

    /**
     * POST Method
     *
     */
    @Override
    public void acceptRepresentation(Representation entity) {
        /**
         * Once the representation is accepted, the status
         * is set to 202
         */
        getResponse().setStatus(Status.SUCCESS_ACCEPTED);

        /**
         * Implementation of the MLR algorithm
         */
        if (algorithmId.equalsIgnoreCase("mlr")) {
            Form form = new Form(entity);
            dataid = form.getFirstValue("dataId");

            try {
                i = Integer.parseInt(dataid);
                File dataFile = new File(REG_MLR_FilePath(dataid));
                boolean exists = dataFile.exists();
                if (!exists) {
                    //// File not found Exception!
                    getResponse().setEntity(
                            "Error 400: Client Error, Bad Requset\n" +
                            "The request could not be understood by the server due " +
                            "to malformed syntax.\n" +
                            "Details: Data File Not Found on the server!\n\n", MediaType.TEXT_PLAIN);
                    getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
                }
            } catch (NumberFormatException e) {
                getResponse().setEntity(
                        "Error 400: Client Error, Bad Requset\n" +
                        "The request could not be understood by the server due " +
                        "to malformed syntax.\n" +
                        "Details: The data file id that you provided is not valid" +
                        " : " + dataid+"\n\n", MediaType.TEXT_PLAIN);
                getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
            }

            if (getResponse().getStatus().equals(Status.SUCCESS_ACCEPTED)) {
                try {
                    // Load the data in an Instances-type object:
                    Instances data = new Instances(
                            new BufferedReader(
                            new FileReader(REG_MLR_FilePath(dataid))));
                    /* Remove the first attribute which corresponds to the
                    structure's id */
                    data.deleteAttributeAt(0);
                    // Set the target attribute (the last one)
                    data.setClassIndex(data.numAttributes() - 1);

                    LinearRegression linreg = new LinearRegression();
                    /*
                     * Options:
                     * No attribute selection algorithm.
                     * Do not try to eliminate colinear attributes.
                     */
                    String[] linRegOptions = {"-S", "1", "-C"};
                    linreg.setOptions(linRegOptions);
                    // Build the classifier
                    linreg.buildClassifier(data);

                    double[] coeffs = linreg.coefficients();


                    // Now, construct the PMML:
                    StringBuilder builder = new StringBuilder();
                    File mlrModelFolder = new File(REG_MLR_modelsDir);
                    String[] listOfFiles = mlrModelFolder.list();
                    int NMLR = listOfFiles.length;
                    String model_id = new String(modelPrefix + dataid + "-" + NMLR);
                    builder.append(xmlIntro);
                    builder.append("<ot:Model xmlns:ot=\"http://opentox.org/1.0/\" " +
                               "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" " +
                               "xsi:schemaLocation=\"http://opentox.org/1.0/Algorithm.xsd\" " +
                               "ID=\"" + model_id + "\" Name=\"MLR Model\">\n");
                        builder.append("<ot:link href=\"" + ModelURI + "/" + model_id +  "\" />\n");
                        builder.append("<ot:AlgorithmID href=\"" + MlrAlgorithmURI +  "\"/>\n");
                        builder.append("<DatasetID href=\"\"/>\n");
                        builder.append("<AlgorithmParameters />\n");
                        builder.append("<FeatureDefinitions>\n");
                        builder.append("</FeatureDefinitions>\n");
                        builder.append("<User>Guest</User>\n");
                        builder.append("<Timestamp>" + java.util.GregorianCalendar.getInstance().getTime() + "</Timestamp>\n");


                        //beginning of PMML element
                        builder.append(PMMLIntro);


                        builder.append("<DataDictionary numberOfFields=\"" + data.numAttributes() + "\" >\n");
                        for (int k = 0; k <= data.numAttributes() - 1; k++) {
                            builder.append("<DataField name=\"" + data.attribute(k).name() + "\" optype=\"continuous\" dataType=\"double\" />\n");
                        }
                        builder.append("</DataDictionary>\n");
                        // RegressionModel
                        builder.append("<RegressionModel modelName=\"" + dataid + "\"" +
                            " functionName=\"regression\"" +
                            " modelType=\"linearRegression\"" +
                            " algorithmName=\"linearRegression\"" +
                            " targetFieldName=\"" + data.attribute(data.numAttributes() - 1).name() + "\"" +
                            ">\n");
                        // RegressionModel::MiningSchema
                        builder.append("<MiningSchema>\n");
                        for (int k = 0; k <= data.numAttributes() - 2; k++) {
                            builder.append("<MiningField name=\"" +
                                data.attribute(k).name() + "\" />\n");
                        }
                        builder.append("<MiningField name=\"" +
                            data.attribute(data.numAttributes() - 1).name() + "\" " +
                            "usageType=\"predicted\"/>\n");
                        builder.append("</MiningSchema>\n");

                        // RegressionModel::RegressionTable
                        builder.append("<RegressionTable intercept=\"" + coeffs[coeffs.length - 1] + "\">\n");
                        for (int k = 0; k <= data.numAttributes() - 2; k++) {
                            builder.append("<NumericPredictor name=\"" +
                                data.attribute(k).name() + "\" " +
                                " exponent=\"1\" " +
                                "coefficient=\"" + coeffs[k] + "\"/>\n");
                        }
                            builder.append("</RegressionTable>\n");

                            builder.append("</RegressionModel>\n");
                        builder.append("</PMML>\n\n");
                        //end of PMML element
                    builder.append("</ot:Model>\n");
                    //!! Now store the PMML representation in a file:
                    FileWriter fileStream = new FileWriter(modelsXmlDir + "/" + model_id + ".xml");
                    BufferedWriter output = new BufferedWriter(fileStream);
                    output.write(builder.toString());
                    output.flush();
                    output.close();

                    getResponse().setEntity(baseURI + "/model/" + modelPrefix + dataid+"\n", MediaType.TEXT_PLAIN);
                    if (getResponse().getStatus().equals(Status.SUCCESS_ACCEPTED)) {
                        getResponse().setStatus(Status.SUCCESS_OK);
                    }


                } catch (Exception ex) {
                    getResponse().setEntity(
                            "Error 500: Server Error, Internal\n" +
                            "Description: The server encountered an unexpected condition which prevented it \n" +
                            "from fulfilling the request.\n" +
                            "Details: Unexplicable Error!\n\n",
                            MediaType.TEXT_PLAIN);
                    getResponse().setStatus(Status.SERVER_ERROR_INTERNAL);
                    Logger.getLogger(Regression.class.getName()).log(Level.SEVERE, null, ex);
                }
            }


        }// end of MLR algorithm


        /**
         * Implementation of the SVM algorithm...
         */
        else if (algorithmId.equalsIgnoreCase("svm")) {
        
                getResponse().setStatus(Status.SUCCESS_ACCEPTED);
            File svmModelFolder = new File(REG_SVM_modelsDir);
            String[] listOfFiles = svmModelFolder.list();
            int NSVM = listOfFiles.length;

            Form form = new Form(entity);

            /**
             * Retrieve the POSTed parameters.
             * If a parameter was not posted set it to its default value...
             */
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
            dataid = form.getFirstValue("dataId");
            if (dataid == null) {
                dataid = "0";
            }

            /**
             * Check if the POSTed parameters are
             * acceptable (Throws Error 400: Client Error - Bad Request)
             */
            /**
             * Check dataId
             */
            try {
                i = Integer.parseInt(dataid);
                File dataFile = new File(dsdDir + "/" + dataSetPrefix + dataid);
                boolean exists = dataFile.exists();
                if (!exists) {
                    //// File not found Exception!
                    getResponse().setEntity(
                            "Error 400: Client Error, Bad Requset\n" +
                            "The request could not be understood by the server due " +
                            "to malformed syntax.\n" +
                            "Details: Data File Not Found on the server!\n", MediaType.TEXT_PLAIN);
                    getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
                }
            } catch (NumberFormatException e) {
                getResponse().setEntity(
                        "Error 400: Client Error, Bad Requset\n" +
                        "The request could not be understood by the server due " +
                        "to malformed syntax.\n" +
                        "Details: The data file id that you provided is not valid" +
                        " : " + dataid+"\n", MediaType.TEXT_PLAIN);
                getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
            }

            // Kernel should be one of rbf, linear, sigmoid or polynomial
            if (getResponse().getStatus().equals(Status.SUCCESS_ACCEPTED)) {
                if (!((kernel.equalsIgnoreCase("rbf")) ||
                        (kernel.equalsIgnoreCase("linear")) ||
                        (kernel.equalsIgnoreCase("sigmoid")) ||
                        (kernel.equalsIgnoreCase("polynomial")))) {
                    getResponse().setEntity(
                            "Error 400: Client Error, Bad Requset\n" +
                            "The request could not be understood by the server due " +
                            "to malformed syntax.\n" +
                            "Details: The kernel you specified is not valid : " +
                            kernel+"\n", MediaType.TEXT_PLAIN);
                    getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
                }
            }

            /**
             * Cost should be convertible to Double and strictly
             * positive.
             */
            if (getResponse().getStatus().equals(Status.SUCCESS_ACCEPTED)) {
                try {
                    d = Double.parseDouble(cost);
                    if (d <= 0) {
                        getResponse().setEntity(
                                "Error 400: Client Error, Bad Requset\n" +
                                "The request could not be understood by the server due " +
                                "to malformed syntax.\n" +
                                "Details: The cost should be strictly positive!\n",
                                MediaType.TEXT_PLAIN);
                        getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
                    }
                } catch (NumberFormatException e) {
                    getResponse().setEntity(
                            "Error 400: Client Error, Bad Requset\n" +
                            "The request could not be understood by the server due " +
                            "to malformed syntax.\n" +
                            "Details: The cost should be Double type, while you specified " +
                            "a non double value : " + cost+ "\n",
                            MediaType.TEXT_PLAIN);
                    getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
                }
            }

            /**
             * Epsilon should be convertible to Double and strictly
             * positive.
             */
            if (getResponse().getStatus().equals(Status.SUCCESS_ACCEPTED)) {
                try {
                    d = Double.parseDouble(epsilon);
                    if (d <= 0) {
                        getResponse().setEntity(
                                "Error 400: Client Error, Bad Requset\n" +
                                "The request could not be understood by the server due " +
                                "to malformed syntax.\n" +
                                "Details: Epsilon should be strictly positive!\n",
                                MediaType.TEXT_PLAIN);
                        getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
                    }
                } catch (NumberFormatException e) {
                    getResponse().setEntity(
                            "Error 400: Client Error, Bad Requset\n" +
                            "The request could not be understood by the server due " +
                            "to malformed syntax.\n" +
                            "Details: Epsilon should be Double type, while you specified " +
                            "a non double value : " + epsilon+"\n",
                            MediaType.TEXT_PLAIN);
                    getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
                }
            }

            /**
             * Degree should be a strictly positive integer
             */
            if (getResponse().getStatus().equals(Status.SUCCESS_ACCEPTED)) {
                try {
                    i = Integer.parseInt(degree);
                    if (i <= 0) {
                        getResponse().setEntity(
                                "Error 400: Client Error, Bad Requset\n" +
                                "The request could not be understood by the server due " +
                                "to malformed syntax.\n" +
                                "Details: The degree should be an Integer greater or equal to 1, while you specified " +
                                "an inacceptable value : " + degree+"\n",
                                MediaType.TEXT_PLAIN);
                        getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
                    }
                } catch (NumberFormatException e) {
                    getResponse().setEntity(
                            "Error 400: Client Error, Bad Requset\n" +
                            "The request could not be understood by the server due " +
                            "to malformed syntax.\n" +
                            "Details: The degree should be an Integer, while you specified " +
                            "a non Integer value : " + degree+"\n",
                            MediaType.TEXT_PLAIN);
                    getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
                }
            }


            /**
             * Gamma should be convertible to Double and strictly
             * positive.
             */
            if (getResponse().getStatus().equals(Status.SUCCESS_ACCEPTED)) {
                try {
                    d = Double.parseDouble(gamma);
                    if (d <= 0) {
                        getResponse().setEntity(
                                "Error 400: Client Error, Bad Requset\n" +
                                "The request could not be understood by the server due " +
                                "to malformed syntax.\n" +
                                "Details: Gamma should be strictly positive!\n\n",
                                MediaType.TEXT_PLAIN);
                        getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
                    }
                } catch (NumberFormatException e) {
                    getResponse().setEntity(
                            "Error 400: Client Error, Bad Requset\n" +
                            "The request could not be understood by the server due " +
                            "to malformed syntax.\n" +
                            "Details: Gamma should be Double, while you specified " +
                            "a non double value : " + gamma+"\n\n",
                            MediaType.TEXT_PLAIN);
                    getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
                }
            }

            /**
             * coeff0 should be convertible to Double.
             */
            if (getResponse().getStatus().equals(Status.SUCCESS_ACCEPTED)) {
                try {
                    d = Double.parseDouble(coeff0);
                } catch (NumberFormatException e) {
                    getResponse().setEntity(
                            "Error 400: Client Error, Bad Requset\n" +
                            "The request could not be understood by the server due " +
                            "to malformed syntax.\n" +
                            "Details: coeff0 should be Double, while you specified " +
                            "a non double value : " + coeff0+"\n\n",
                            MediaType.TEXT_PLAIN);
                    getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
                }
            }

            /**
             * Tolerance
             */
            if (getResponse().getStatus().equals(Status.SUCCESS_ACCEPTED)) {
                try {
                    d = Double.parseDouble(tolerance);
                    if (d <= 0) {
                        getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
                    }
                } catch (NumberFormatException e) {
                    getResponse().setEntity(
                            "Error 400: Client Error, Bad Requset\n" +
                            "The request could not be understood by the server due " +
                            "to malformed syntax.\n" +
                            "Details: Tolerance should be (preferably small) Double, while you specified " +
                            "a non double value : " + tolerance+"\n\n",
                            MediaType.TEXT_PLAIN);
                    getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
                }
            }


            /**
             * cache size
             */
            if (getResponse().getStatus().equals(Status.SUCCESS_ACCEPTED)) {
                try {
                    i = Integer.parseInt(cacheSize);
                    if (d <= 0) {
                        getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
                    }
                } catch (NumberFormatException e) {
                    getResponse().setEntity(
                            "Error 400: Client Error, Bad Requset\n" +
                            "The request could not be understood by the server due " +
                            "to malformed syntax.\n" +
                            "Details: cache size (in MB) should be an Integer, while you specified " +
                            "a non Integer value : " + cacheSize+"\n\n",
                            MediaType.TEXT_PLAIN);
                    getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
                }
            }

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
                ker = "3";
            }

            String[] options = {""};

            if (ker.equalsIgnoreCase("0")) {
                String[] ops = {
                    "-s", "0",
                    "-t", "0",
                    "-c", cost,
                    "-e", tolerance,
                    scaledDir + "/" + dataSetPrefix + dataid,
                    REG_SVM_modelsDir + "/" + modelPrefix + dataid + "-" + NSVM
                };
                options = ops;
            } else if (ker.equalsIgnoreCase("1")) {
                String[] ops = {
                    "-s", "0",// C-SVC (Classifier)
                    "-t", ker,
                    "-c", cost,
                    "-g", gamma,
                    "-d", degree,
                    "-r", coeff0,
                    "-e", tolerance,
                    scaledDir + "/" + dataSetPrefix + dataid,
                    REG_SVM_modelsDir + "/" + modelPrefix + dataid + "-" + NSVM
                };
                options = ops;
            } else if (ker.equalsIgnoreCase("2")) {
                String[] ops = {
                    "-s", "0",// C-SVC (Classifier)
                    "-t", ker,
                    "-c", cost,
                    "-g", gamma,
                    "-e", tolerance,
                    scaledDir + "/" + dataSetPrefix + dataid,
                    REG_SVM_modelsDir + "/" + modelPrefix + dataid + "-" + NSVM
                };
                options = ops;
            } else if (ker.equalsIgnoreCase("3")) {
                String[] ops = {
                    "-s", "0",// C-SVC (Classifier)
                    "-t", ker,
                    "-c", cost,
                    "-g", gamma,
                    "-e", tolerance,
                    scaledDir + "/" + dataSetPrefix + dataid,
                    REG_SVM_modelsDir + "/" + modelPrefix + dataid + "-" + NSVM
                };
                options = ops;
            }

            /**
             * If all the posted parameters (kernel type, cost, gamma, etc)
             * are acceptable the the status is 202.
             */
            if (getResponse().getStatus().equals(Status.SUCCESS_ACCEPTED)) {
                try {
                    getResponse().setEntity(getResponse().getStatus().toString(), MediaType.TEXT_PLAIN);
                    long before = System.currentTimeMillis();
                    logger.info("aaa");
                    svm_train.main(options);
                    long timeSpent = System.currentTimeMillis() - before;

                    /**
                     * Check if the model was created.
                     * If yes, set the status to 200,
                     * otherwise the status is set to 500
                     */
                    File modelFile = new File(REG_SVM_modelsDir + "/" + modelPrefix + dataid + "-" + NSVM);
                    boolean modelCreated = modelFile.exists();
                    if (!(modelCreated)) {
                        getResponse().setEntity(
                                "Error 400: Client Error, Bad Requset\n" +
                                "The server encountered an unexpected condition " +
                                "which prevented it from fulfilling the request." +
                                "Details: Unexpected Error while trying to train the model.\n\n",
                                MediaType.TEXT_PLAIN);
                        getResponse().setStatus(Status.SERVER_ERROR_INTERNAL);
                    } else {
                        getResponse().setStatus(Status.SUCCESS_OK);
                    }


                    /**
                     * If the model was successfully created, that is the
                     * status is 200, return a report to the user in
                     * HTML form. This is for testing reasons only as according to
                     * the OpenTox API specification, the URI of the trained model
                     * should be returned
                     */
                    if (getResponse().getStatus().equals(Status.SUCCESS_OK)) {
                        String model_id = modelPrefix + dataid + "-" + NSVM;
                        getResponse().setEntity(ModelURI + "/"+ model_id+"\n", MediaType.TEXT_PLAIN);

                       StringBuilder xmlstr = new StringBuilder();
                       xmlstr.append(xmlIntro);
                       xmlstr.append("<ot:Model xmlns:ot=\"http://opentox.org/1.0/\" " +
                               "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" " +
                               "xsi:schemaLocation=\"http://opentox.org/1.0/Algorithm.xsd\" " +
                               "ID=\""+model_id+"\" Name=\"SVM Regression Model\">\n");
                           xmlstr.append("<ot:link href=\"" + SvmModelURI + "/" + model_id +  "\" />\n");
                           xmlstr.append("<ot:AlgorithmID href=\"" + SvmAlgorithmURI +  "\"/>\n");
                           xmlstr.append("<DatasetID href=\"\"/>\n");
                           xmlstr.append("<AlgorithmParameters>\n");
                               xmlstr.append("<param name=\"kernel\"  type=\"string\">" + kernel + "</param>\n");
                               xmlstr.append("<param name=\"cost\"  type=\"double\">" + cost + "</param>\n");
                               xmlstr.append("<param name=\"epsilon\"  type=\"double\">" + epsilon + "</param>\n");
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
                       try{

                           FileWriter fstream = new FileWriter(modelsXmlDir + "/" + model_id + ".xml" );
                           BufferedWriter out = new BufferedWriter(fstream);
                           out.write(xmlstr.toString());
                           out.flush();
                           out.close();
                       } catch (Exception e) {
                           System.err.println("Error: " + e.getMessage());
                       }
                    }
                } catch (IOException ex) {
                    Logger.getLogger(Classification.class.getName()).log(Level.SEVERE, null, ex);
                }
            }


        }

    }// end of acceptRepresentation

    private String REG_MLR_FilePath(String dataid) {
        return arffDir + "/" + dataSetPrefix + dataid;
    }
}
