package org.opentox.Resources.Algorithms;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.opentox.Applications.OpenToxApplication;
import org.opentox.Resources.*;

import org.opentox.util.svm_train;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Reference;
import org.restlet.data.Status;


import org.restlet.data.Method;
import org.restlet.data.ReferenceList;
import org.restlet.representation.Representation;
import org.restlet.representation.StreamRepresentation;
import org.restlet.representation.StringRepresentation;
import org.restlet.representation.Variant;
import org.restlet.resource.ResourceException;
import weka.classifiers.functions.LinearRegression;
import weka.core.Instances;

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
 * @version 1.7 (Last Update: Oct 21, 2009)
 */
public class Regression extends AbstractResource {

    private static final long serialVersionUID = -5538766798434922154L;
    private volatile String algorithmId;
    private int i;
    private double d;
    private String dataid, kernel, degree, cacheSize,
            cost, epsilon, gamma, coeff0, tolerance;
    private int model_id;
    /**
     * The status of the Resource. It is initialized with
     * success/created (201) according to RFC 2616.
     */
    private Status internalStatus = Status.SUCCESS_CREATED;

    @Override
    public void doInit() throws ResourceException {
        super.doInit();
        List<Variant> variants = new ArrayList<Variant>();
        variants.add(new Variant(MediaType.TEXT_PLAIN));
        variants.add(new Variant(MediaType.TEXT_XML));
        variants.add(new Variant(MediaType.TEXT_HTML));
        getVariants().put(Method.GET, variants);
        this.algorithmId = Reference.decode(getRequest().getAttributes().get("id").toString());
    }

    /**
     * Set the private status variable
     * @param status the new status
     */
    private void setInternalStatus(Status status) {
        this.internalStatus = status;
    }


    private String getSvmXml(){
        StringBuilder builder = new StringBuilder();
        builder.append(xmlIntro);
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
                return builder.toString();
    }

    private String getSvmHtml(){
        StringBuilder builder = new StringBuilder();
        builder.append(htmlHEAD);
        builder.append("<h1>Support Vector Machine Regression Algorithm</h1>");
        
        builder.append("<table><tbody>");
        builder.append("<tr >");
        builder.append("<td style=\"width:200\" ><b>Algorithm Name</b></td><td>Support Vector Machine</td>");
        builder.append("</tr>");
        builder.append("<tr>");
        builder.append("<td><b>Algorithm Type</b></td><td>Regression</td>");
        builder.append("</tr>");
        builder.append("<tr>");
        builder.append("<td><b>Algorithm Parameters</b></td><td>&nbsp;</td>");
        builder.append("</tr>");
        builder.append("<tr>");
        builder.append("<td>kernel</td><td>{rbf, linear, sigmoid, polynomial}</td>");
        builder.append("</tr>");
        builder.append("<tr>");
        builder.append("<td>cost</td><td>double, strictly positive</td>");
        builder.append("</tr>");
        builder.append("<tr>");
        builder.append("<td>epsilon</td><td>double, strictly positive</td>");
        builder.append("</tr>");
        builder.append("<tr>");
        builder.append("<td>gamma</td><td>double, strictly positive</td>");
        builder.append("</tr>");
        builder.append("<tr>");
        builder.append("<td>coeff0</td><td>double</td>");
        builder.append("</tr>");
        builder.append("<tr>");
        builder.append("<td>degree</td><td>integer, strictly positive</td>");
        builder.append("</tr>");
        builder.append("<tr>");
        builder.append("<td>tolerance</td><td>integer, strictly positive</td>");
        builder.append("</tr>");
        builder.append("<tr>");
        builder.append("<td>cacheSize</td><td>integer, strictly positive</td>");
        builder.append("</tr>");
        builder.append("</tbody></table>");
        
        builder.append(htmlEND);
        return builder.toString();
    }

    private String getPlsrXml(){
        StringBuilder builder = new StringBuilder();
        builder.append(xmlIntro);
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
                builder.append("</algorithm>\n\n");
                return builder.toString();
    }

    private String getMlrXml(){
        StringBuilder builder = new StringBuilder();
        builder.append(xmlIntro);
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
                return builder.toString();
    }

    /**
     * Implementation of the GET method.
     * Returns XML representations for the supported regression algorithms
     * @param variant
     * @return XML representation of algorithm
     */
    @Override
    public Representation get(Variant variant) {

        


        if ( (MediaType.TEXT_XML.equals(variant.getMediaType())) ||
                (MediaType.TEXT_HTML.equals(variant.getMediaType())) ){
            if (algorithmId.equalsIgnoreCase("svm")) {                
                return new StringRepresentation(getSvmXml(), MediaType.TEXT_XML);

            } else if (algorithmId.equalsIgnoreCase("plsr")) {               
                return new StringRepresentation(getPlsrXml(), MediaType.TEXT_XML);
            } else if (algorithmId.equalsIgnoreCase("mlr")) {
                
                return new StringRepresentation(getMlrXml(), MediaType.TEXT_XML);
            } else //Not Found!
            {
                getResponse().setStatus(Status.CLIENT_ERROR_NOT_FOUND);
                return new StringRepresentation("Not Found", MediaType.TEXT_PLAIN);
            }
        } else if (variant.getMediaType().equals(MediaType.TEXT_URI_LIST)) {
            ReferenceList list = new ReferenceList();
            list.add(getOriginalRef());
            return list.getTextRepresentation();
        }else {
            getResponse().setStatus(Status.CLIENT_ERROR_NOT_ACCEPTABLE);
            return new StringRepresentation(variant.getMediaType()+" is Not supported media type!", MediaType.TEXT_PLAIN);
        }
    }

    /**
     *
     * @param form
     * @return
     */
    private Representation checkMlrParameters(Form form) {
        Representation rep = null;
        MediaType errorMediaType = MediaType.TEXT_PLAIN;
        Status clientPostedWrongParametersStatus = Status.CLIENT_ERROR_BAD_REQUEST;
        String errorDetails = "";

        setInternalStatus(Status.SUCCESS_ACCEPTED);

        dataid =
                form.getFirstValue("dataId");

        try {
//                i = Integer.parseInt(dataid);
            File dataFile = new File(REG_MLR_FilePath(dataid));
            boolean exists = dataFile.exists();
            if (!exists) {
                //// File not found Exception!
                errorDetails = errorDetails +
                        "* [Inacceptable Parameter Value] The dataset id you specified " +
                        "does not correspond to an existing resource";
                setInternalStatus(clientPostedWrongParametersStatus);
            }

        } catch (NumberFormatException e) {
            errorDetails = errorDetails +
                    "* [Inacceptable Parameter Value] The dataset id you specified is not valid";
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


    private Representation checkSvmParameters(Form form){
        Representation rep = null;
        MediaType errorMediaType = MediaType.TEXT_PLAIN;
        Status clientPostedWrongParametersStatus = Status.CLIENT_ERROR_BAD_REQUEST;
        String errorDetails = "";


        setInternalStatus(Status.SUCCESS_ACCEPTED);

                kernel=form.getFirstValue("kernel");
                    if(kernel==null)
                        kernel="RBF";
                cost=form.getFirstValue("cost");
                    if (cost==null)
                        cost="100";
                gamma = form.getFirstValue("gamma");
                    if (gamma==null)
                        gamma="1.5";
                epsilon=form.getFirstValue("epsilon");
                    if (epsilon==null)
                        epsilon="0.1";
                coeff0=form.getFirstValue("coeff0");
                    if (coeff0==null)
                        coeff0="0";
                degree=form.getFirstValue("degree");
                    if (degree==null)
                        degree="3";
                tolerance=form.getFirstValue("tolerance");
                    if (tolerance==null)
                        tolerance="0.0001";
                cacheSize=form.getFirstValue("cacheSize");
                    if (cacheSize==null)
                        cacheSize="50";
                dataid=form.getFirstValue("dataId");
                    if (dataid==null)
                        dataid="0";

                /**
                 * Check dataId
                 */
                try{
                    i=Integer.parseInt(dataid);
                    File dataFile = new File(dsdDir+"/"+dataSetPrefix+dataid);
                    boolean exists = dataFile.exists();
                    if (!exists){
                        //// File not found Exception!
                        errorDetails = errorDetails + "* [Inacceptable Parameter Value] The Requested Data File was Not Found on the server.\n";
                        setInternalStatus(clientPostedWrongParametersStatus);
                    }
                }catch(NumberFormatException e){

                    errorDetails = errorDetails + "* [Inacceptable Parameter Value] The data file id that you provided is not valid.\n";
                    setInternalStatus(clientPostedWrongParametersStatus);
                }


                if (
                        !(
                        (kernel.equalsIgnoreCase("rbf"))||
                        (kernel.equalsIgnoreCase("linear"))||
                        (kernel.equalsIgnoreCase("sigmoid"))||
                        (kernel.equalsIgnoreCase("polynomial"))
                        )
                    )
                {
                   errorDetails = errorDetails + "* [Inacceptable Parameter Value] Invalid Kernel Type!\n";
                   setInternalStatus(clientPostedWrongParametersStatus);
                }


                /**
                 * Cost should be convertible to Double and strictly
                 * positive.
                 */

                try{
                    d = Double.parseDouble(cost);
                    if (d<=0){
                        errorDetails = errorDetails + "* [Inacceptable Parameter Value] The cost should be strictly positive\n";
                        setInternalStatus(clientPostedWrongParametersStatus);
                    }
                }catch(NumberFormatException e){
                    errorDetails = errorDetails +
                            "* [Inacceptable Parameter Value]  The cost should be Double type, while you specified " +
                                "a non double value : "+cost+"\n";
                    setInternalStatus(clientPostedWrongParametersStatus);
                }


                /**
                 * Epsilon should be convertible to Double and strictly
                 * positive.
                 */

                 try{
                     d=Double.parseDouble(epsilon);
                     if (d<=0){
                         errorDetails = errorDetails + "* [Inacceptable Parameter Value] Epsinlon must be strictly positive!\n";
                         setInternalStatus(clientPostedWrongParametersStatus);
                     }
                 }catch(NumberFormatException e){
                     errorDetails = errorDetails + "* [Inacceptable Parameter Value] Epsilon must be a striclty positive number!\n";
                     setInternalStatus(clientPostedWrongParametersStatus);
                 }


                /**
                 * Degree should be a strictly positive integer
                 */

                try{
                    i=Integer.parseInt(degree);
                    if (i<=0){
                        errorDetails = errorDetails + "* [Inacceptable Parameter Value] The degree must be a strictly positive integer!\n";
                        setInternalStatus(clientPostedWrongParametersStatus);
                    }
                }catch(NumberFormatException e){
                    errorDetails = errorDetails + "* [Inacceptable Parameter Value] The degree must be a strictly positive integer!\n";
                    setInternalStatus(clientPostedWrongParametersStatus);
                }



                /**
                 * Gamma should be convertible to Double and strictly
                 * positive.
                 */

                 try{
                     d=Double.parseDouble(gamma);
                     if (d<=0){
                         errorDetails = errorDetails + "* [Inacceptable Parameter Value] gamma must be a strictly positive double!\n";
                         setInternalStatus(clientPostedWrongParametersStatus);
                     }
                 }catch(NumberFormatException e){
                     errorDetails = errorDetails + "* [Inacceptable Parameter Value] gamma must be a strictly positive double!\n";
                     setInternalStatus(clientPostedWrongParametersStatus);
                 }


                /**
                 * coeff0 should be convertible to Double.
                 */

                  try{
                     d=Double.parseDouble(coeff0);
                  }catch(NumberFormatException e){
                      errorDetails = errorDetails + "* [Inacceptable Parameter Value] coeff must be a number!\n";
                     setInternalStatus(clientPostedWrongParametersStatus);
                 }


                /**
                 * Tolerance
                 */
                  try{
                     d=Double.parseDouble(tolerance);
                     if (d<=0){
                         errorDetails = errorDetails +
                         "* [Inacceptable Parameter Value] Tolerance must be a strictly positive double (preferably small)!\n";
                         setInternalStatus(clientPostedWrongParametersStatus);
                     }
                 }catch(NumberFormatException e){
                     errorDetails = errorDetails +
                             "* [Inacceptable Parameter Value] Tolerance must be a strictly positive double (preferably small)!\n";
                     setInternalStatus(clientPostedWrongParametersStatus);
                 }



                /**
                 * cache size
                 */

                  try{
                     i=Integer.parseInt(cacheSize);
                     if (d<=0){
                         setInternalStatus(clientPostedWrongParametersStatus);
                     }
                 }catch(NumberFormatException e){
                     rep = new StringRepresentation(
                                "Error 400: Client Error, Bad Requset\n" +
                                "The request could not be understood by the server due " +
                                "to malformed syntax.\n" +
                                "Details: cache size (in MB) should be an Integer, while you specified " +
                                "a non Integer value : "+cacheSize+"\n\n",
                                errorMediaType);
                     setInternalStatus(clientPostedWrongParametersStatus);
                 }

                 if (!(errorDetails.equalsIgnoreCase(""))){
                     rep = new StringRepresentation("Error Code : "+clientPostedWrongParametersStatus.toString()+"\n"+
                             "Error Code Desription : The request could not be understood by the server due to " +
                             "malformed syntax.\nThe client SHOULD NOT repeat the request without modifications.\n"+
                             "Error Explanation :\n"+errorDetails+"\n",errorMediaType);
                    return rep;
                 }else{
                    return null;
                 }

    }

    /**
     * The following two commands are necessary before providing an Instances
     * object to this method:<br/>
     * <pre>data.deleteAttributeAt(0);</pre>
     * <pre>data.setClassIndex(data.numAttributes() - 1);</pre>
     * @param data the weka.core.Instances object containing the data.
     * @return xml representation for the trained mlr model
     */
    private String MlrTrain(Instances data){
        StringBuilder builder = new StringBuilder();


        LinearRegression linreg = new LinearRegression();
                    /*
                     * Options:
                     * No attribute selection algorithm.
                     * Do not try to eliminate colinear attributes.
                     */
                    String[] linRegOptions = {"-S", "1", "-C"};


    try {
            linreg.setOptions(linRegOptions);
            linreg.buildClassifier(data);
        } catch (Exception ex) {
            Logger.getLogger(Regression.class.getName()).log(Level.SEVERE, null, ex);
        }
                    // Build the classifier


                    double[] coeffs = linreg.coefficients();

                    builder.append(xmlIntro);
                    builder.append("<ot:Model xmlns:ot=\"http://opentox.org/1.0/\" " +
                            "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" " +
                            "xsi:schemaLocation=\"http://opentox.org/1.0/Algorithm.xsd\" " +
                            "ID=\"" + model_id + "\" Name=\"MLR Model\">\n");
                    builder.append("<ot:link href=\"" + ModelURI + "/" + model_id + "\" />\n");
                    builder.append("<ot:AlgorithmID href=\"" + MlrAlgorithmURI + "\"/>\n");
                    builder.append("<DatasetID href=\"\"/>\n");
                    builder.append("<AlgorithmParameters />\n");
                    builder.append("<FeatureDefinitions>\n");
                    builder.append("</FeatureDefinitions>\n");
                    builder.append("<User>Guest</User>\n");
                    builder.append("<Timestamp>" + java.util.GregorianCalendar.getInstance().getTime() + "</Timestamp>\n");


                    //beginning of PMML element
                    builder.append(PMMLIntro);


                    builder.append("<DataDictionary numberOfFields=\"" + data.numAttributes() + "\" >\n");
                    for (int k = 0; k <=
                            data.numAttributes() - 1; k++) {
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
                    for (int k = 0; k <=
                            data.numAttributes() - 2; k++) {
                        builder.append("<MiningField name=\"" +
                                data.attribute(k).name() + "\" />\n");
                    }

                    builder.append("<MiningField name=\"" +
                            data.attribute(data.numAttributes() - 1).name() + "\" " +
                            "usageType=\"predicted\"/>\n");
                    builder.append("</MiningSchema>\n");

                    // RegressionModel::RegressionTable
                    builder.append("<RegressionTable intercept=\"" + coeffs[coeffs.length - 1] + "\">\n");
                    for (int k = 0; k <=
                            data.numAttributes() - 2; k++) {
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
        return builder.toString();
    }


    /**
     *
     * @return
     */
    private String[] getSvmOptions(){
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
                ker = "3";
            }

            String scaledPath = scaledDir + "/" + dataSetPrefix + dataid;
            String modelPath = REG_SVM_modelsDir + "/" + model_id;

            if (ker.equalsIgnoreCase("0")) {
                String[] ops = {
                    "-s", "0",
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
                    "-s", "0",// C-SVC (Classifier)
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
                    "-s", "0",// C-SVC (Classifier)
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
                    "-s", "0",// C-SVC (Classifier)
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

    /**
     * POST Method
     *
     */
    @Override
    public Representation post(Representation entity) {
        
        setInternalStatus(Status.SUCCESS_ACCEPTED);

        Representation representation = null;

        model_id = org.opentox.Applications.OpenToxApplication.dbcon.getModelsStack() + 1;
        

        /**
         * Implementation of the MLR algorithm
         */
        if (algorithmId.equalsIgnoreCase("mlr")) {
            Form form = new Form(entity);
            Representation errorRep = checkMlrParameters(form);
                if (errorRep!=null){
                    representation = errorRep;
                }


            if (Status.SUCCESS_ACCEPTED.equals(internalStatus)) {
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

                    String mlrXml = MlrTrain(data);

                    // Now, construct the XML:
                    /***********
                     * TODO:
                     * This is not correct!
                     * First get the new index from the database and then
                     * , if everything goes ok, register the model!
                     */
                    model_id = org.opentox.Applications.OpenToxApplication.dbcon.registerNewModel(
                            baseURI + "/algorithm/learning/regression/mlr");
                    
                    //!! Now store the PMML representation in a file:
                    FileWriter fileStream = new FileWriter(modelsXmlDir + "/" + model_id);
                    BufferedWriter output = new BufferedWriter(fileStream);
                    output.write(mlrXml);
                    output.flush();
                    output.close();

                    //getResponse().setEntity(baseURI + "/model/" + modelPrefix + dataid+"\n", MediaType.TEXT_PLAIN);
                    representation = new StringRepresentation(baseURI + "/model/" + model_id + "\n", MediaType.TEXT_PLAIN);

                    if (Status.SUCCESS_ACCEPTED.equals(internalStatus)) {
                        setInternalStatus(Status.SUCCESS_OK);
                    }

                } catch (Exception ex) {
                    representation = new StringRepresentation(
                            "Error 500: Server Error, Internal\n" +
                            "Description: The server encountered an unexpected condition which prevented it \n" +
                            "from fulfilling the request.\n" +
                            "Details:\n"+
                            "Error Message           : "+ex.getMessage()+"\n"+
                            "Localized Error Message : "+ex.getLocalizedMessage() +"\n",
                            MediaType.TEXT_PLAIN);
                    setInternalStatus(Status.SERVER_ERROR_INTERNAL);
                    Logger.getLogger(Regression.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            return representation;

        }// end of MLR algorithm
        
       
        
        /**
         * Implementation of the SVM algorithm...
         */
        else if (algorithmId.equalsIgnoreCase("svm")) {

            
            File svmModelFolder = new File(REG_SVM_modelsDir);
            String[] listOfFiles = svmModelFolder.list();
            int NSVM = listOfFiles.length;

            Form form = new Form(entity);



            /**
             * Retrieve the POSTed parameters.
             * If a parameter was not posted set it to its default value...
             */            
            representation = checkSvmParameters(form);
            String[] options = getSvmOptions();

            /**
             * If all the posted parameters (kernel type, cost, gamma, etc)
             * are acceptable the the status is 202.
             */
            if (Status.SUCCESS_ACCEPTED.equals(internalStatus)) {
                try {
                    representation = new StringRepresentation(getResponse().getStatus().toString(), MediaType.TEXT_PLAIN);                                        
                    svm_train.main(options);
                    

                    /**
                     * Check if the model was created.
                     * If yes, set the status to 200,
                     * otherwise the status is set to 500
                     */
                    File modelFile = new File(REG_SVM_modelsDir + "/" + model_id);
                    boolean modelCreated = modelFile.exists();
                    if (!(modelCreated)) {
                        representation = new StringRepresentation(
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
                        //String model_id = modelPrefix + dataid + "-" + NSVM;
                        model_id = org.opentox.Applications.OpenToxApplication.dbcon.registerNewModel(baseURI + "/algorithm/learning/regression/svm");

                        representation = new StringRepresentation(ModelURI + "/" + model_id + "\n", MediaType.TEXT_PLAIN);

                        StringBuilder xmlstr = new StringBuilder();
                        xmlstr.append(xmlIntro);
                        xmlstr.append("<ot:Model xmlns:ot=\"http://opentox.org/1.0/\" " +
                                "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" " +
                                "xsi:schemaLocation=\"http://opentox.org/1.0/Algorithm.xsd\" " +
                                "ID=\"" + model_id + "\" Name=\"SVM Regression Model\">\n");
                        xmlstr.append("<ot:link href=\"" + SvmModelURI + "/" + model_id + "\" />\n");
                        xmlstr.append("<ot:AlgorithmID href=\"" + SvmAlgorithmURI + "\"/>\n");
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
                        try {

                            FileWriter fstream = new FileWriter(modelsXmlDir + "/" + model_id);
                            BufferedWriter out = new BufferedWriter(fstream);
                            out.write(xmlstr.toString());
                            out.flush();
                            out.close();
                        } catch (Exception e) {
                            OpenToxApplication.opentoxLogger.severe(e.getMessage());
                        }


                    }

                } catch (IOException ex) {
                    OpenToxApplication.opentoxLogger.log(Level.SEVERE, null, ex);
                }                
            }
            return representation;
        }else{
            return new StringRepresentation("--",MediaType.TEXT_PLAIN);
        }

        

    }// end of acceptRepresentation

    private String REG_MLR_FilePath(String dataid) {
        return arffDir + "/" + dataSetPrefix + dataid;
    }
}
