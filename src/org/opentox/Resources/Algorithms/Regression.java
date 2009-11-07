package org.opentox.Resources.Algorithms;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import org.opentox.Applications.OpenToxApplication;
import org.opentox.MediaTypes.OpenToxMediaType;
import org.opentox.Resources.*;

import org.opentox.client.opentoxClient;
import org.opentox.util.libSVM.svm_train;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Reference;
import org.restlet.data.Status;


import org.restlet.data.Method;
import org.restlet.data.ReferenceList;
import org.restlet.representation.Representation;
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
 * <b>Status Codes for POST:</b>
 * <ul>
 * <li>200(Success, OK)</li>
 * <li>202(Success, Accepted but not Completed yet)</li>
 * <li>400(Bad Request)</li>
 * <li>404(Not Found) </li>
 * <li>500(Internal Server Error)</li>
 * <li>503(Service Unavailable)</li>
 * </ul>
 * <b>Supported Mediatypes for GET:</b>
 * <ul>
 * <li>text/xml</li>
 * <li>text/html</li>
 * <li>text/x-yaml</li>
 * <li>application/json</li>
 * </p>
 *
 * @author OpenTox - http://www.opentox.org
 * @author Sopasakis Pantelis
 * @author Sarimveis Harry
 * @version 1.7 (Last Update: Nov 6, 2009)
 */
public class Regression extends AbstractResource {

    private static final long  serialVersionUID = 1042369829901523417L;
    /**
     * The id of the regression algorithm.
     * This can be either mlr or svm.
     */
    private volatile String algorithmId;
    private int i;
    private double d;
    /**
     * The name of the target attribute which normally is the
     * URI of a feature definition.
     */
    private String targetAttribute;
    /**
     * The URI of the dataset which is used to build the regression model.
     */
    private URI datasetURI;
    /**
     * The name of the dataset.
     */
//    private String dataset;
    /**
     * The kernel used in the SVM model.
     * This can be rbf, linear, sigmoid or polynomial.
     */
    private String kernel;
    /**
     * The degree of the polynomial kernel (when used).
     */
    private String degree;
    /**
     * The cahed memory used in model training.
     */
    private String cacheSize;
    /**
     * The Cost coefficient.
     */
    private String cost;
    /**
     * The parameter epsilon used in SVM models.
     */
    private String epsilon;
    /**
     * The kernel parameter gamma used in various kernel functions.
     */
    private String gamma;
    /**
     * The bias of the support vector model.
     */
    private String coeff0;
    /**
     * The tolerance used in model training.
     */
    private String tolerance;
    /**
     * The id of the generated model.
     */
    private int model_id;
    /**
     * An Instances object used to store the data.
     */
    private Instances dataInstances;
    /**
     * The status of the Resource. It is initialized with
     * success/created (201) according to RFC 2616.
     */
    private Status internalStatus = Status.SUCCESS_CREATED;

    /**
     * Initialize the resource. Supported Variants are:
     * <ul>
     * <li>text/plain</li>
     * <li>text/xml</li>
     * <li>text/html</li>
     * <li>text/x-yaml</li>
     * <li>application/json</li>
     * </ul>
     * Allowed Methods are:
     * <ul>
     * <li>GET</li>
     * <li>POST</li>
     * </ul>
     * @throws ResourceException
     */
    @Override
    public void doInit() throws ResourceException {
        super.doInit();
        Collection<Method> allowedMethods = new ArrayList<Method>();
        allowedMethods.add(Method.GET);
        allowedMethods.add(Method.POST);
        getAllowedMethods().addAll(allowedMethods);
        List<Variant> variants = new ArrayList<Variant>();
        variants.add(new Variant(MediaType.TEXT_PLAIN));
        variants.add(new Variant(MediaType.TEXT_XML));
        variants.add(new Variant(MediaType.TEXT_HTML));
        variants.add(new Variant(OpenToxMediaType.TEXT_YAML));
        variants.add(new Variant(MediaType.APPLICATION_JSON));
        getVariants().put(Method.GET, variants);
        /** The algorithm id can be one of {svm, mlr} **/
        this.algorithmId = Reference.decode(getRequest().getAttributes().get("id").toString());
    }

    /**
     * Set the private status variable
     * @param status the new status
     */
    private void setInternalStatus(Status status) {
        this.internalStatus = status;
    }

    private String getSvmXml() {
        StringBuilder builder = new StringBuilder();
        builder.append(xmlIntro);
        builder.append("<algorithm name=\"Support Vector Machine\" id=\"svm\">\n");
        builder.append("<AlgorithmType>regression</AlgorithmType>\n");
        builder.append("<Parameters>\n");
        builder.append("<!-- \n" +
                "The id of the dataset used for the training\n" +
                "of the model\n" +
                "-->\n");
        builder.append("<param type=\"String\" defaultvalue=\"0\">dataset</param>\n");
        builder.append("<param type=\"String\" defaultvalue=\"0\">target</param>\n");
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

    private String getSvmHtml() {
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

    private String getPlsrXml() {
        StringBuilder builder = new StringBuilder();
        builder.append(xmlIntro);
        builder.append("<algorithm name=\"Partial Least Squares\" id=\"pls\">\n");
        builder.append("<AlgorithmType>regression</AlgorithmType>\n");
        builder.append("<Parameters>\n");
        builder.append("<!-- \n" +
                "The id of the dataset used for the training\n" +
                "of the model\n" +
                "-->\n");
        builder.append("<param type=\"String\" defaultvalue=\"0\">dataset</param>\n");
        builder.append("<param type=\"String\" defaultvalue=\"0\">target</param>\n");
        builder.append("<param type=\"Integer\" defaultvalue=\"1\">nComp</param>\n");
        builder.append("</Parameters>\n");
        builder.append("<statisticsSupported>\n");
        builder.append("<statistic>x</statistic>\n");
        builder.append("</statisticsSupported>\n");
        builder.append("</algorithm>\n\n");
        return builder.toString();
    }

    private String getMlrXml() {
        StringBuilder builder = new StringBuilder();
        builder.append(xmlIntro);
        builder.append("<algorithm name=\"Multiple Linear Regression\" id=\"mlr\">\n");
        builder.append("<AlgorithmType>regression</AlgorithmType>\n");
        builder.append("<Parameters>\n");
        builder.append("<!-- \n" +
                "The id of the dataset used for the training\n" +
                "of the model\n" +
                "-->\n");
        builder.append("<param type=\"String\" defaultvalue=\"0\">dataset</param>\n");
        builder.append("<param type=\"String\" defaultvalue=\"0\">target</param>\n");
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


    private String getMlrJson(){
        StringBuilder builder = new StringBuilder();
        builder.append("\"Algorithm\": \n{\n");
        builder.append("\"name\" : \"Multiple Linear Regression\",\n");
        builder.append("\"id\" : \"mlr\",\n");
        builder.append("\"AlgorithmType\" : \"regression\",\n");
        builder.append("\"Parameters\" : {\n" +
                "\"dataset\" : {\"type\" : \"URI\", \"defaultValue\"  : \"\"},\n"+
                "\"target\"  : {\"type\" : \"URI\",  \"defaultValue\" : \"\"}\n" +
                "},\n");
        builder.append("\"statisticsSupported\" : {\n");
        builder.append("\"statistic\" : \"RootMeanSquaredError\",\n");
        builder.append("\"statistic\" : \"RelativeAbsoluteError\",\n");
        builder.append("\"statistic\" : \"RootRelativeSquaredError\",\n");
        builder.append("\"statistic\" : \"MeanAbsolutError\"\n");
        builder.append("}\n");
        return  builder.toString();
    }



    private String getSvmJson(){
        StringBuilder builder = new StringBuilder();
        builder.append("\"Algorithm\": \n{\n");
        builder.append("\"name\" : \"Support Vector Machine\",\n");
        builder.append("\"id\" : \"svm\",\n");
        builder.append("\"AlgorithmType\" : \"regression\",\n");
        builder.append("\"Parameters\" : {\n" +
                "\"dataset\" : {\"type\" : \"URI\", \"defaultValue\"  : \"\"},\n"+
                "\"target\"  : {\"type\" : \"URI\",  \"defaultValue\" : \"\"},\n" +
                "\"kernel\"  : {\"type\" : \"String\",  \"defaultValue\" : \"rbf\"},\n" +
                "\"cost\"    : {\"type\" : \"Double\",  \"defaultValue\" : \"10\"},\n" +
                "\"epsilon\" : {\"type\" : \"Double\",  \"defaultValue\" : \"0.1\"},\n" +
                "\"gamma\"   : {\"type\" : \"Double\",  \"defaultValue\" : \"1\"},\n" +
                "\"coeff0\"  : {\"type\" : \"Double\",  \"defaultValue\" : \"0\"},\n" +
                "\"degree\"  : {\"type\" : \"Integer\",  \"defaultValue\" : \"3\"},\n" +
                "\"tolerance\"  : {\"type\" : \"Double\",  \"defaultValue\" : \"1E-4\"},\n" +
                "\"cacheSize\"  : {\"type\" : \"Integer\",  \"defaultValue\" : \"50\"}\n" +
                "},\n");
        builder.append("\"statisticsSupported\" : {\n");
        builder.append("\"statistic\" : \"RootMeanSquaredError\",\n");
        builder.append("\"statistic\" : \"RelativeAbsoluteError\",\n");
        builder.append("\"statistic\" : \"RootRelativeSquaredError\",\n");
        builder.append("\"statistic\" : \"MeanAbsolutError\"\n");
        builder.append("}\n");
        return  builder.toString();
    }


    private String getMlrYaml(){
        StringBuilder builder = new StringBuilder();
        builder.append("---\nAlgorithm:\n");
        builder.append("    name : Multiple Linear Regression\n");
        builder.append("    id : mlr\n");
        builder.append("    AlgorithmType : regression\n");
        builder.append("    Parameters:\n");
        builder.append("        -dataset:\n");
        builder.append("            type:URI\n");
        builder.append("            defaultValue:none\n");
        builder.append("        -target:\n");
        builder.append("            type:URI\n");
        builder.append("            defaultValue:none\n");
        builder.append("    statisticsSupported:\n");
        builder.append("            -RootMeanSquaredError\n");
        builder.append("            -RelativeAbsoluteError\n");
        builder.append("            -RootRelativeSquaredError\n");
        builder.append("            -MeanAbsolutError\n");
        return  builder.toString();
    }



    private String getSvmYaml(){
        StringBuilder builder = new StringBuilder();
        builder.append("---\nAlgorithm:\n");
        builder.append("    name : Support Vector Machine\n");
        builder.append("    id : mlr\n");
        builder.append("    AlgorithmType : regression\n");
        builder.append("    Parameters:\n");
        builder.append("        -dataset:\n");
        builder.append("            type:URI\n");
        builder.append("            defaultValue:none\n");
        builder.append("        -target:\n");
        builder.append("            type:URI\n");
        builder.append("            defaultValue:none\n");
        builder.append("        -kernel:\n");
        builder.append("            type:String\n");
        builder.append("            defaultValue:rbf\n");
        builder.append("        -cost:\n");
        builder.append("            type:Double\n");
        builder.append("            defaultValue:10\n");
        builder.append("        -epsilon:\n");
        builder.append("            type:Double\n");
        builder.append("            defaultValue:0.1\n");
        builder.append("        -gamma:\n");
        builder.append("            type:Double\n");
        builder.append("            defaultValue:1\n");
        builder.append("        -coeff0:\n");
        builder.append("            type:Double\n");
        builder.append("            defaultValue:0\n");
        builder.append("        -degree:\n");
        builder.append("            type:Integer\n");
        builder.append("            defaultValue:3\n");
        builder.append("        -tolerance:\n");
        builder.append("            type:Double\n");
        builder.append("            defaultValue:0.1\n");
        builder.append("        -cacheSize:\n");
        builder.append("            type:Integer\n");
        builder.append("            defaultValue:50\n");
        builder.append("    statisticsSupported:\n");
        builder.append("            -RootMeanSquaredError\n");
        builder.append("            -RelativeAbsoluteError\n");
        builder.append("            -RootRelativeSquaredError\n");
        builder.append("            -MeanAbsolutError\n");
        return  builder.toString();
    }


    /**
     * Implementation of the GET method.
     * Returns XML representations for the supported regression algorithms
     * @param variant
     * @return XML representation of algorithm
     */
    @Override
    public Representation get(Variant variant) {

        if ((MediaType.TEXT_XML.equals(variant.getMediaType())) ||
                (MediaType.TEXT_HTML.equals(variant.getMediaType()))) {
            if (algorithmId.equalsIgnoreCase("svm")) {
                return new StringRepresentation(getSvmXml(), MediaType.TEXT_XML);

            } else if (algorithmId.equalsIgnoreCase("plsr")) {
                return new StringRepresentation(getPlsrXml(), MediaType.TEXT_XML);
            } else if (algorithmId.equalsIgnoreCase("mlr")) {

                return new StringRepresentation(getMlrXml(), MediaType.TEXT_XML);
            } else //Not Found!
            {
                getResponse().setStatus(Status.CLIENT_ERROR_NOT_FOUND);
                return new StringRepresentation("Algorithm Not Found!\n", MediaType.TEXT_PLAIN);
            }
        } else if (MediaType.TEXT_URI_LIST.equals(variant.getMediaType())) {
            ReferenceList list = new ReferenceList();
            list.add(getOriginalRef());
            return list.getTextRepresentation();
        }else if (MediaType.APPLICATION_JSON.equals(variant.getMediaType())){
            if (algorithmId.equalsIgnoreCase("mlr")) {
                return new StringRepresentation(getMlrJson(), MediaType.TEXT_XML);
            }else if (algorithmId.equalsIgnoreCase("svm")){
                return new StringRepresentation(getSvmJson(), MediaType.TEXT_XML);
            }else{
                getResponse().setStatus(Status.CLIENT_ERROR_NOT_FOUND);
                return new StringRepresentation("Algorithm Not Found!\n");
            }
        }else if (OpenToxMediaType.TEXT_YAML.equals(variant.getMediaType())){
            if (algorithmId.equalsIgnoreCase("mlr")) {
                return new StringRepresentation(getMlrYaml(), MediaType.TEXT_XML);
            }else if (algorithmId.equalsIgnoreCase("svm")){
                return new StringRepresentation(getSvmYaml(), MediaType.TEXT_XML);
            }else{
                getResponse().setStatus(Status.CLIENT_ERROR_NOT_FOUND);
                return new StringRepresentation("Algorithm Not Found!\n");
            }
        }
        else {
            getResponse().setStatus(Status.CLIENT_ERROR_NOT_ACCEPTABLE);
            return new StringRepresentation(variant.getMediaType() + " is Not a supported media type!", MediaType.TEXT_PLAIN);
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


        try {
            datasetURI = new URI(form.getFirstValue("dataset"));
        } catch (URISyntaxException ex) {
            setInternalStatus(clientPostedWrongParametersStatus);
            errorDetails = errorDetails + "[Wrong Posted Parameter ]: The client did" +
                    " not post a valid URI for the dataset";
        }


        targetAttribute = form.getFirstValue("target");



        if (!(errorDetails.equalsIgnoreCase(""))) {
            rep = new StringRepresentation(
                    "Error Code            : " + clientPostedWrongParametersStatus.toString() + "\n" +
                    "Error Code Desription : The request could not be understood by the server due to " +
                    "malformed syntax.\nThe client SHOULD NOT repeat the request without modifications.\n" +
                    "Error Explanation     :\n" + errorDetails + "\n", errorMediaType);
            return rep;
        } else {
            return null;
        }
    }

    private Representation checkSvmParameters(Form form) {
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
            datasetURI = new URI(form.getFirstValue("dataset"));
            dataInstances = opentoxClient.getInstances(datasetURI);
        } catch (URISyntaxException ex) {
            setInternalStatus(clientPostedWrongParametersStatus);
            errorDetails = errorDetails + "[Wrong Posted Parameter ]: The client did" +
                    " not post a valid URI for the dataset";
        }


        targetAttribute = form.getFirstValue("target");

        dataInstances.setClass(dataInstances.attribute(targetAttribute));

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
                errorDetails = errorDetails + "* [Inacceptable Parameter Value] The cost should be strictly positive\n";
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
                errorDetails = errorDetails + "* [Inacceptable Parameter Value] Epsinlon must be strictly positive!\n";
                setInternalStatus(clientPostedWrongParametersStatus);
            }
        } catch (NumberFormatException e) {
            errorDetails = errorDetails + "* [Inacceptable Parameter Value] Epsilon must be a striclty positive number!\n";
            setInternalStatus(clientPostedWrongParametersStatus);
        }


        /**
         * Degree should be a strictly positive integer
         */
        try {
            i = Integer.parseInt(degree);
            if (i <= 0) {
                errorDetails = errorDetails + "* [Inacceptable Parameter Value] The degree must be a strictly positive integer!\n";
                setInternalStatus(clientPostedWrongParametersStatus);
            }
        } catch (NumberFormatException e) {
            errorDetails = errorDetails + "* [Inacceptable Parameter Value] The degree must be a strictly positive integer!\n";
            setInternalStatus(clientPostedWrongParametersStatus);
        }



        /**
         * Gamma should be convertible to Double and strictly
         * positive.
         */
        try {
            d = Double.parseDouble(gamma);
            if (d <= 0) {
                errorDetails = errorDetails + "* [Inacceptable Parameter Value] gamma must be a strictly positive double!\n";
                setInternalStatus(clientPostedWrongParametersStatus);
            }
        } catch (NumberFormatException e) {
            errorDetails = errorDetails + "* [Inacceptable Parameter Value] gamma must be a strictly positive double!\n";
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

    /**
     * The following two commands are necessary before providing an Instances
     * object to this method:<br/>
     * <pre>data.deleteAttributeAt(0);</pre>
     * <pre>data.setClassIndex(data.numAttributes() - 1);</pre>
     * @param data the weka.core.Instances object containing the data.
     * @return xml representation for the trained mlr model
     */
    private String MlrTrain(Instances data) {
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
            OpenToxApplication.opentoxLogger.severe("Severe Error while trying to build an MLR model.\n" +
                    "Details :" + ex.getMessage() + "\n");
        }
        // Build the classifier


        double[] coeffs = linreg.coefficients();

        builder.append(xmlIntro);



        //beginning of PMML element
        builder.append(PMMLIntro);
        builder.append("<Model ID=\"" + model_id + "\" Name=\"MLR Model\">\n");
        builder.append("<link href=\"" + ModelURI + "/" + model_id + "\" />\n");
        builder.append("<AlgorithmID href=\"" + URIs.mlrAlgorithmURI + "\"/>\n");
        builder.append("<DatasetID href=\"" + datasetURI.toString() + "\"/>\n");
        builder.append("<AlgorithmParameters />\n");
        builder.append("<FeatureDefinitions>\n");
        for (int k = 1; k <= data.numAttributes(); k++) {
            builder.append("<link href=\"" + data.attribute(k - 1).name() + "\"/>\n");
        }
        builder.append("<target index=\"" + data.attribute(targetAttribute).index() + "\" name=\"" +
                targetAttribute + "\"/>\n");
        builder.append("</FeatureDefinitions>\n");
        builder.append("<User>Guest</User>\n");
        builder.append("<Timestamp>" + java.util.GregorianCalendar.getInstance().getTime() + "</Timestamp>\n");
        builder.append("</Model>\n");

        builder.append("<DataDictionary numberOfFields=\"" + data.numAttributes() + "\" >\n");
        for (int k = 0; k <=
                data.numAttributes() - 1; k++) {
            builder.append("<DataField name=\"" + data.attribute(k).name() + "\" optype=\"continuous\" dataType=\"double\" />\n");
        }

        builder.append("</DataDictionary>\n");
        // RegressionModel
        builder.append("<RegressionModel modelName=\"" + URIs.modelURI+"/"+model_id + "\"" +
                " functionName=\"regression\"" +
                " modelType=\"linearRegression\"" +
                " algorithmName=\"linearRegression\"" +
                " targetFieldName=\"" + data.attribute(data.numAttributes() - 1).name() + "\"" +
                ">\n");
        // RegressionModel::MiningSchema
        builder.append("<MiningSchema>\n");
        for (int k = 0; k <= data.numAttributes() - 1; k++) {
            if (k!=dataInstances.classIndex())
                builder.append("<MiningField name=\"" +
                    data.attribute(k).name() + "\" />\n");
            
        }
                 builder.append("<MiningField name=\"" +
                data.attribute(dataInstances.classIndex()).name() + "\" " +
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


        return builder.toString();
    }

    /**
     *
     * @return
     */
    private String[] getSvmOptions(String scaledPath, String modelPath) {
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

    /**
     *
     * @param dsdFile path to DSD model file.
     * @return pmml representation of the DSD file
     */
    private String PmmlFromDsD(File dsdFile){
        StringBuilder pmml = new StringBuilder();
        pmml.append(xmlIntro);
        pmml.append(PMMLIntro);
        
        /** ##Data Dictionary **/
        pmml.append("<DataDictionary numberOfFields=\""+(dataInstances.numAttributes())+"\">\n");
        for (int k=0;k<dataInstances.numAttributes();k++){
            pmml.append("<DataField name=\""+dataInstances.attribute(k).name()+"\" optype=\"continuous\" dataType=\"double\"/>\n");
        }
        pmml.append("</DataDictionary>\n");

        /** ##SupportVectorMachineModel **/
        pmml.append("<SupportVectorMachineModel modelName=\""+URIs.modelURI+"/"+model_id+"\" "+
                "algorithmName=\"supportVectorMachine\" "+
                "functionName=\"regression\" "+
                "svmRepresentation=\"SupportVectors\" "+
                "alternateBinaryTargetCategory=\"no\" >\n");
        /** #Mining Schema **/
        pmml.append("<MiningSchema>\n");
        for (int k=0;k<dataInstances.numAttributes();k++){
            if (k!=dataInstances.classIndex()){
                pmml.append("<MiningField name=\""+dataInstances.attribute(k).name()+"\" />\n");
            }else{
                pmml.append("<MiningField name=\""+dataInstances.attribute(k).name()+"\" usageType=\"predicted\" />\n");
            }
        }
        pmml.append("</MiningSchema>\n");

        /** #RadialBasisKernelType **/
        pmml.append("<RadialBasisKernelType gamma=\""+gamma+"\" description=\"Radial basis kernel type\" />\n" );

        /** #VectorDictionary **/
        pmml.append("<VectorDictionary numberOfVectors=\""+dataInstances.numAttributes()+"\" >\n");
        pmml.append("<VectorFields numberOfFields=\""+(dataInstances.numAttributes()-1)+"\">\n");
            for (int k=0;k<dataInstances.numAttributes();k++){
                if (k!=dataInstances.classIndex()){
                    pmml.append("<FieldRef field=\""+dataInstances.attribute(k).name()+"\" />\n");
                }
            }
        
        pmml.append("</VectorFields>\n");
        for (int k=0;k<dataInstances.numInstances();k++){
            pmml.append("<VectorInstance id=\"node"+k+"\">\n");
            pmml.append("<REAL-SparseArray n=\""+(dataInstances.numAttributes()-1)+"\">\n");
            pmml.append("<Indices>");
            for (int j=1;j<=dataInstances.numAttributes()-1;j++){
                pmml.append(j+" ");
            }
            pmml.append("</Indices>\n");
            pmml.append("<REAL-Entries>");
            for (int j=1;j<=dataInstances.numAttributes()-1;j++){
                pmml.append(dataInstances.instance(k).value(j-1)+" ");
            }
            pmml.append("</REAL-Entries>\n");
            pmml.append("</REAL-SparseArray>\n");
            pmml.append("</VectorInstance>\n");
        }
        pmml.append("</VectorDictionary>\n");

        pmml.append("<SupportVectorMachine>");
        pmml.append("<SupportVectors numberOfAttributes=\""+(dataInstances.numAttributes()-1)+"\" " +
                "numberOfSupportVectors=\""+dataInstances.numInstances()+"\" >");
        
        pmml.append("</SupportVectors>");
        pmml.append("</SupportVectorMachine>");

        pmml.append("</SupportVectorMachineModel>\n");
        pmml.append("</PMML>");
        return pmml.toString();
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
            if (errorRep != null) {
                representation = errorRep;
            }


            if (Status.SUCCESS_ACCEPTED.equals(internalStatus)) {
                try {
                    // Load the data in an Instances-type object:

                    dataInstances = opentoxClient.getInstances(datasetURI);
                    dataInstances.setClass(dataInstances.attribute(targetAttribute));
                    System.out.println(
                            dataInstances.classAttribute().name()
                            );


                    /* Removes all string attributes!
                    TODO: Parse values one by one... */
                    for (int j = 0; j < dataInstances.numAttributes(); j++) {
                        if (dataInstances.attribute(j).isString()) {
                            dataInstances.deleteAttributeAt(j);
                            j--;
                        }
                    }

                    String mlrXml = MlrTrain(dataInstances);

                    // Now, construct the XML:
                    model_id = org.opentox.Applications.OpenToxApplication.dbcon.registerNewModel(
                            baseURI + "/algorithm/learning/regression/mlr");

                    //!! Now store the PMML representation in a file:
                    FileWriter fileStream = new FileWriter(Directories.modelXmlDir + "/" + model_id);
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
                    String errorMessage = "Error 500: Server Error, Internal\n" +
                            "Description: The server encountered an unexpected condition which prevented it \n" +
                            "from fulfilling the request.\n" +
                            "Details.....\n" +
                            "Error Message           : " + ex.getMessage() + "\n" +
                            "Localized Error Message : " + ex.getLocalizedMessage() + "\n" +
                            "Exception               : " + ex.toString() + "\n";
                    representation = new StringRepresentation(
                            errorMessage,
                            MediaType.TEXT_PLAIN);
                    setInternalStatus(Status.SERVER_ERROR_INTERNAL);
                    OpenToxApplication.opentoxLogger.info(errorMessage);
                }
            }

            return representation;

        }// end of MLR algorithm
        /**
         * Implementation of the SVM algorithm...
         */
        else if (algorithmId.equalsIgnoreCase("svm")) {            

            Form form = new Form(entity);
            representation = checkSvmParameters(form);
            /**
             * Preprocess the data (The instances object has already been created
             * - see checkSvmParameters). Now remove all string attributes and
             * scale the data. Then Save the instances as a libSvm file in
             * /temp/scaled
             */
            Preprocessing.removeStringAtts(dataInstances);
            dataInstances = Preprocessing.scale(dataInstances);
            
            
            weka.core.converters.LibSVMSaver saver = new weka.core.converters.LibSVMSaver();
            
            saver.setInstances(new Instances(dataInstances));

            /**
             * Generate a temporary file name consisting of about 25 random
             * characters (random length). Then create a java.io.File object
             * pointing to the file containing the scaled data.
             */
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
            File tempScaledFile = new File(Directories.tempScaledDir + "/" + key);
            try {
                saver.setFile(tempScaledFile);
                saver.writeBatch();
                /**
                 * Now the scaled data are availabel in DSD (libsvm) format.
                 * Now get the options for svmtrain.
                 */
                String[] options = getSvmOptions(tempScaledFile.toString(), Directories.svmModel + "/" + key);
                if (Status.SUCCESS_ACCEPTED.equals(internalStatus)) {
                    representation = new StringRepresentation(getResponse().getStatus().toString(), MediaType.TEXT_PLAIN);
                    svm_train.main(options);

                    /**
                     * TODO: check if the model was really created!!!
                     * If not, return a status code 500.
                     */
                    String pmml = PmmlFromDsD(new File(Directories.svmModel + "/" + key));
                    representation = new StringRepresentation(pmml,MediaType.TEXT_XML);
                }

            } catch (IOException ex) {
                OpenToxApplication.opentoxLogger.log(Level.SEVERE,
                        "Error while tryning to save the dataset as LibSVM file : ", ex);
            }
            

            /**
             * If all the posted parameters (kernel type, cost, gamma, etc)
             * are acceptable the the status is 202.
             */
            return representation;
        } /** end of svm implementation **/
        /**
         * In case the user asks for other algorithms...
         */
        else {
            getResponse().setStatus(Status.CLIENT_ERROR_NOT_FOUND);
            return new StringRepresentation("Sorry, this algorithm is not supported!", MediaType.TEXT_PLAIN);
        }



    }// end of acceptRepresentation

    
}
