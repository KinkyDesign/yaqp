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
import java.util.logging.Logger;
import org.opentox.Applications.OpenToxApplication;
import org.opentox.MediaTypes.OpenToxMediaType;
import org.opentox.Resources.*;


import org.opentox.Resources.Algorithms.AlgorithmReporter.*;
import org.opentox.client.opentoxClient;
import org.opentox.util.libSVM.svm_scale;
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

    private static final long  serialVersionUID = -9058627046190364530L;
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
        variants.add(new Variant(MediaType.APPLICATION_RDF_XML));  //-- (application/rdf+xml)
        variants.add(new Variant(MediaType.TEXT_PLAIN ));
        variants.add(new Variant(MediaType.TEXT_URI_LIST ));
        variants.add(new Variant(MediaType.TEXT_XML));
        variants.add(new Variant(MediaType.TEXT_HTML));
        variants.add(new Variant(OpenToxMediaType.TEXT_YAML));
        variants.add(new Variant(MediaType.APPLICATION_JSON));        
        variants.add(new Variant(MediaType.APPLICATION_RDF_TURTLE));  //-- (application/x-turtle)
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
    private  String MlrTrain(Instances data) {
        StringBuilder pmml = new StringBuilder();


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

        pmml.append(xmlIntro);



        //beginning of PMML element
        pmml.append(PMMLIntro);
        pmml.append("<Model ID=\"" + model_id + "\" Name=\"MLR Model\">\n");
        pmml.append("<link href=\"" + AbstractResource.URIs.modelURI + "/" + model_id + "\" />\n");
        pmml.append("<AlgorithmID href=\"" + URIs.mlrAlgorithmURI + "\"/>\n");
        pmml.append("<DatasetID href=\"" + datasetURI.toString() + "\"/>\n");
        pmml.append("<AlgorithmParameters />\n");
        pmml.append("<FeatureDefinitions>\n");
        for (int k = 1; k <= data.numAttributes(); k++) {
            pmml.append("<link href=\"" + data.attribute(k - 1).name() + "\"/>\n");
        }
        pmml.append("<target index=\"" + data.attribute(targetAttribute).index() + "\" name=\"" +
                targetAttribute + "\"/>\n");
        pmml.append("</FeatureDefinitions>\n");
        pmml.append("<User>Guest</User>\n");
        pmml.append("<Timestamp>" + java.util.GregorianCalendar.getInstance().getTime() + "</Timestamp>\n");
        pmml.append("</Model>\n");

        pmml.append("<DataDictionary numberOfFields=\"" + data.numAttributes() + "\" >\n");
        for (int k = 0; k <=
                data.numAttributes() - 1; k++) {
            pmml.append("<DataField name=\"" + data.attribute(k).name() + "\" optype=\"continuous\" dataType=\"double\" />\n");
        }

        pmml.append("</DataDictionary>\n");
        // RegressionModel
        pmml.append("<RegressionModel modelName=\"" + URIs.modelURI+"/"+model_id + "\"" +
                " functionName=\"regression\"" +
                " modelType=\"linearRegression\"" +
                " algorithmName=\"linearRegression\"" +
                " targetFieldName=\"" + data.attribute(data.numAttributes() - 1).name() + "\"" +
                ">\n");
        // RegressionModel::MiningSchema
        pmml.append("<MiningSchema>\n");
        for (int k = 0; k <= data.numAttributes() - 1; k++) {
            if (k!=dataInstances.classIndex())
                pmml.append("<MiningField name=\"" +
                    data.attribute(k).name() + "\" />\n");
            
        }
                 pmml.append("<MiningField name=\"" +
                data.attribute(dataInstances.classIndex()).name() + "\" " +
                "usageType=\"predicted\"/>\n");



        pmml.append("</MiningSchema>\n");

        // RegressionModel::RegressionTable
        pmml.append("<RegressionTable intercept=\"" + coeffs[coeffs.length - 1] + "\">\n");
        for (int k = 0; k <=
                data.numAttributes() - 2; k++) {
            pmml.append("<NumericPredictor name=\"" +
                    data.attribute(k).name() + "\" " +
                    " exponent=\"1\" " +
                    "coefficient=\"" + coeffs[k] + "\"/>\n");
        }

        pmml.append("</RegressionTable>\n");

        pmml.append("</RegressionModel>\n");
        pmml.append("</PMML>\n\n");


        return pmml.toString();
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


    
    private Representation SvmTrain(Form form) {
        Representation representation = checkSvmParameters(form);
        /**
         * Preprocess the data (The instances object has already been created
         * - see checkSvmParameters). Now remove all string attributes and
         * scale the data. Then Save the instances as a libSvm file in
         * /temp/scaled
         */
        Preprocessing.removeStringAtts(dataInstances);

        //Save data in a temp file (dsd format)
        weka.core.converters.LibSVMSaver saver = new weka.core.converters.LibSVMSaver();

        saver.setInstances(new Instances(dataInstances));

        String key = CreateARandomFilename();
        File tempDsdFile = new File(Directories.dataDSDDir + "/" + key);
        while(tempDsdFile.exists()){
            key = CreateARandomFilename();
            tempDsdFile = new File(Directories.dataDSDDir + "/" + key);
        }

        try {
            saver.setFile(tempDsdFile);
            saver.writeBatch();

            //Scaling data and saving a temp scaled data file (dsd format)
            svm_scale scaler = new svm_scale();
            String[] scalingOptions = {"-l", "-1", "-u", "1", "-s",
              Directories.dataRangesDir + "/" + model_id, tempDsdFile.getPath()};
            key = CreateARandomFilename();
            File tempScaledFile = new File(Directories.dataScaledDir + "/" + key);
            while(tempScaledFile.exists()){
                key = CreateARandomFilename();
                tempScaledFile = new File(Directories.dataScaledDir + "/" + key);
            }
            try {
                scaler.scale(scalingOptions, tempScaledFile.toString());
            } catch (IOException ex) {
                Logger.getLogger(Regression.class.getName()).log(Level.SEVERE, null, ex);
            }

            String[] options = getSvmOptions(tempDsdFile.toString(), Directories.modelRawDir + "/" + model_id);
            if (Status.SUCCESS_ACCEPTED.equals(internalStatus)) {

                representation = new StringRepresentation(getResponse().getStatus().toString(), MediaType.TEXT_PLAIN);
                svm_train.main(options);
                representation = new StringRepresentation(AbstractResource.URIs.modelURI + "/" + model_id + "\n\n", MediaType.TEXT_PLAIN);

                /*Creating the xml*/
                StringBuilder xmlstr = new StringBuilder();
                xmlstr.append(xmlIntro);
                xmlstr.append("<ot:Model xmlns:ot=\"http://opentox.org/1.0/\" " +
                        "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" " +
                        "xsi:schemaLocation=\"http://opentox.org/1.0/Algorithm.xsd\" " +
                        "ID=\"" + model_id + "\" Name=\"SVM Classification Model\">\n");
                xmlstr.append("<ot:link href=\"" + URIs.modelURI + "/" + model_id + "\" />\n");
                xmlstr.append("<ot:AlgorithmID href=\"" + URIs.svmAlgorithmURI + "\"/>\n");
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

                /*Writing the xml to file*/
                try {

                    FileWriter fstream = new FileWriter(Directories.modelRdfDir + "/" + model_id);
                    BufferedWriter out = new BufferedWriter(fstream);
                    out.write(xmlstr.toString());
                    out.flush();
                    out.close();
                } catch (Exception e) {
                    System.err.println("Error: " + e.getMessage());
                }

                //check if model was succesfully created
                File modelFile = new File(Directories.modelRdfDir + "/" + model_id);
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
                    org.opentox.Applications.OpenToxApplication.dbcon.registerNewModel(baseURI + "/algorithm/learning/classification/svc");
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
                }

            }


        } catch (IOException ex) {
            OpenToxApplication.opentoxLogger.log(Level.SEVERE,
                    "Error while tryning to save the dataset as LibSVM file : ", ex);
        }
        return representation;
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
                    FileWriter fileStream = new FileWriter(Directories.modelRdfDir + "/" + model_id);
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
            representation = SvmTrain(form);
            return representation;
        }
        /**
         * In case the user asks for other algorithms...
         */
        else {
            getResponse().setStatus(Status.CLIENT_ERROR_NOT_FOUND);
            return new StringRepresentation("Sorry, this algorithm is not supported!", MediaType.TEXT_PLAIN);
        }



    }// end of acceptRepresentation

    
}
