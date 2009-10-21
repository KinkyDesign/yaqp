package org.opentox.Resources.Algorithms;

import org.opentox.Resources.*;
import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.opentox.util.svm_train;
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
import java.util.Date;
import weka.experiment.LearningRateResultProducer;


/**
 * Resource for classification algorithms.
 *
 * Implemented Algorithms:
 * <ul>
 *  <li>SVM</li>
 * </ul>
 * Algorithms Not Implemented Yet:
 * <ul>
 *  <li>kNN</li>
 *  <li>J48</li>
 *  <li>PLS</li>
 * </ul>
 * <p style="width:50%">
 * <b>URI:</b> /algorithm/learningalgorithm/classification/{id}<br/>
 * <b>Allowed Methods:</b> GET, POST<br/>
 * <b>Status Codes for POST:</b> 200(Success, OK),202(Success, Accepted but not Completed yet),
 * 400(Bad Request),404(Not Found) ,500(Internal Server Error) ,503(Service Unavailable) <br/>
 * <b>Supported Mediatypes for GET:</b> TEXT_XML
 * </p>
 *
 * @author OpenTox - http://www.opentox.org
 * @author Sopasakis Pantelis
 * @author Sarimveis Harry
 * @version 1.5 (Last Updated: Aug 20, 2009)
 * @since 1.0
 */
public class Classification extends AbstractResource{

    private static final long serialVersionUID = 7323625622428712L;

    /**
     * Algorithm id can be svm, pls, knn, j48.
     * For more information see the class ClassificationAlgorithmsList
     */
    private volatile String algorithmId="";
    /**
     * SVMParameters:
     */
    private String dataid, kernel, degree, cacheSize,
            cost, epsilon, gamma, coeff0, tolerance;
    private int i;
    private double d;

    private int model_id;

    /**
     * Constructor
     * @param context
     * @param request
     * @param response
     */
    public Classification(Context context, Request request, Response response) {
        super(context, request, response);
        getVariants().add(new Variant(MediaType.TEXT_XML));
        this.algorithmId=Reference.decode(request.getAttributes().get("id").toString());
    }


    /**
     * Materialization of the GET method. XML Representations are generated for
     * each algorithm (svm, pls, j48 and kNN). What is more an XML schema is
     * available.
     * @param variant
     * @return StringRepresentation
     */
    @Override
    public Representation represent(Variant variant) {

        StringBuilder builder = new StringBuilder();
        builder.append(xmlIntro);
       
       /**
        * Support Vector Machines XML representation
        */
        if(algorithmId.equalsIgnoreCase("svc")){
            builder.append("<algorithm name=\"Support Vector Machine\" id=\"svc\">\n");
            builder.append("<AlgorithmType>classification</AlgorithmType>\n");
            builder.append("<Parameters>\n");
                builder.append("<param type=\"String\" defaultvalue=\"RBF\">kernel</param>\n");
                builder.append("<param type=\"Double\" defaultvalue=\"10\">cost</param>\n");
                builder.append("<param type=\"Double\" defaultvalue=\"1\">gamma</param>\n");
                builder.append("<param type=\"Double\" defaultvalue=\"0\">coeff0</param>\n");
                builder.append("<param type=\"Integer\" defaultvalue=\"3\">degree</param>\n");
                builder.append("<param type=\"Double\" defaultvalue=\"1E-4\">tolerance</param>\n");
                builder.append("<param type=\"Integer\" defaultvalue=\"50\">cacheSize</param>\n");
            builder.append("</Parameters>\n");
            builder.append("<statisticsSupported>\n");
                builder.append("<statistic>Comfusion Matrix</statistic>\n");
                builder.append("<statistic>Per Class Success</statistic>\n");
                builder.append("<statistic>Correctly Classified Instances</statistic>\n");
                builder.append("<statistic>Overall success rate</statistic>\n");
                builder.append("<statistic>Per instance probabilities</statistic>\n");
            builder.append("</statisticsSupported>\n");
            builder.append("</algorithm>\n\n");
            
        /**
         * Partial Least Squares
         */
        }else if(algorithmId.equalsIgnoreCase("plsc")){
           builder.append("<algorithm name=\"Partial Least Squares\" id=\"plsc\">\n");
            builder.append("<AlgorithmType>classification</AlgorithmType>\n");
            builder.append("<Parameters>\n");
                builder.append("<param type=\"Integer\" defaultvalue=\"RBF\">nComp</param>\n");
            builder.append("</Parameters>\n");
            builder.append("<!-- Under Developement... -->");
            builder.append("<statisticsSupported>\n");
                builder.append("<statistic>?</statistic>\n");
            builder.append("</statisticsSupported>\n");
            builder.append("</algorithm>\n\n");
        }
        /**
         * k Nearest Neighbors
         */
        else if(algorithmId.equalsIgnoreCase("kNNc")){
            builder.append("<algorithm name=\"k Nearest Neighbors\" id=\"knnc\">\n");
            builder.append("<AlgorithmType>classification</AlgorithmType>\n");
            builder.append("<Parameters>\n");
                builder.append("<param type=\"Integer\" defaultvalue=\"10\">numNeighbors</param>\n");
                builder.append("<param type=\"String\" defaultvalue=\"Euclidian\">distanceFunction</param>\n");
            builder.append("</Parameters>\n");
            builder.append("<!-- Under Developement... -->");
            builder.append("<statisticsSupported>\n");
                builder.append("<statistic>?</statistic>\n");
            builder.append("</statisticsSupported>\n");
            builder.append("</algorithm>\n\n");
        }
        /**
         * J48 Classification Algorithm
         */
        else if (algorithmId.equalsIgnoreCase("j48c"))
        {
            builder.append("<algorithm name=\"J48 Classification Algorithm\" id=\"j48c\">\n");
            builder.append("<AlgorithmType>classification</AlgorithmType>\n");
            builder.append("<Parameters>\n");
                builder.append("<param type=\"Boolean\" defaultvalue=\"False\">binSplits</param>\n");
                builder.append("<param type=\"Boolean\" defaultvalue=\"False\">laplaceSmoothing</param>\n");
                builder.append("<param type=\"Boolean\" defaultvalue=\"False\">subTreeRaising</param>\n");
                builder.append("<param type=\"String\" defaultvalue=\"No\">pruning</param>\n");
                builder.append("<param type=\"Double\" defaultvalue=\"confidence\">degree</param>\n");
                builder.append("<param type=\"Integer\" defaultvalue=\"2\">minInstPerLeaf</param>\n");
            builder.append("</Parameters>\n");
            builder.append("<!-- Under Developement... -->");
            builder.append("<statisticsSupported>\n");
                builder.append("<statistic>Under Construction</statistic>\n");
            builder.append("</statisticsSupported>\n");
            builder.append("</algorithm>\n");

            /**
             * XML Schema for (classification) algorithm
             */
        }else if (algorithmId.equalsIgnoreCase("xsd")){
            builder.append(
                    "<schema xmlns:xs=\"http://www.w3.org/2001/XMLSchema\" " +
                      "targetNamespace=\"http://www.opentox.org/Algorithm/1.0\" "+
                      "xmlns:tns=\"http://www.opentox.org/Algorithm/1.0\" " +
                      "elementFormDefault=\"qualified\">\n" +
                    "<complexType name=\"Algorithm\">\n" +
                    "<xs:attribute name=\"ID\" type=\"xs:string\" " +
                    "use=\"required\">\n" +
                    "</xs:attribute>\n<xs:attribute name=\"Name\" " +
                    "type=\"xs:string\" use=\"required\">\n</xs:attribute>" +
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
                    "</xs:sequence>\n</xs:complexType></xs:element>\n" +
                    "<xs:element name=\"statisticsSupported\">\n<xs:complexType>\n" +
                    "<xs:sequence>\n<xs:element name=\"statistic\" type=\"xs:string\">\n" +
                    "</xs:element>\n</xs:sequence>\n</xs:complexType>\n</xs:element>\n</complexType>\n</schema>\n\n"
                    );
        }
        else
        {
            builder.append("<algorithm name=\"unknown\" id=\""+algorithmId+"\">\n");
            builder.append("<status code=\"404\">The server has not found anything matching the Request-URI or the server does not" +
                    " wish to reveal exactly why the request has been refused, or no other response is applicable.</status>\n");
            builder.append("</algorithm>\n\n");
            getResponse().setStatus(Status.CLIENT_ERROR_NOT_FOUND);
        }

        
        return new StringRepresentation(builder.toString(),MediaType.TEXT_XML);
    }

    /**
     * Post is allowed.
     * @return true
     */
    @Override
    public boolean allowPost(){
        return true;
    }

    /**
     * Processing of POST requests.
     * @param entity
     */
    @Override
    public void acceptRepresentation(Representation entity)
    {

        /**
         * Once the representation is accepted, the status
         * is set to 202 (Success - Accepted).
         */
        getResponse().setStatus(Status.SUCCESS_ACCEPTED);
        model_id = org.opentox.Applications.OpenToxApplication.dbcon.getModelsStack()+1;
    
        /**
         * Implementation of the SVM classification algorithm.
         */
        if (algorithmId.equalsIgnoreCase("svc"))
        {
            File svmModelFolder = new File(CLS_SVM_modelsDir);
            String[] listOfFiles = svmModelFolder.list();
            int NSVM = listOfFiles.length;
            
            
                /**
                 * We construct a Form object that handles incoming parameters
                 * If a parameter "x" has been POSTed, we can retrieve it using
                 * form.getFirstValue("x");
                 */
                Form form = new Form(entity);

                /**
                 * Retrieve the POSTed parameters.
                 * If a parameter was not posted set it to its default value...
                 */
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
                 * Check if the POSTed parameters are
                 * acceptable (Throws Error 400: Client Error - Bad Request)
                 */

                /**
                 * Check dataId
                 */
                try{
                    i=Integer.parseInt(dataid);
                    File dataFile = new File(dsdDir+"/"+dataSetPrefix+dataid);
                    boolean exists = dataFile.exists();
                    if (!exists){
                        //// File not found Exception!
                        getResponse().setEntity(
                                "Error 400: Client Error, Bad Requset\n" +
                                "The request could not be understood by the server due " +
                                "to malformed syntax.\n" +
                                "Details: Data File Not Found on the server!\n\n", MediaType.TEXT_PLAIN);
                        getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST);    
                    }
                }catch(NumberFormatException e){
                    getResponse().setEntity(
                                "Error 400: Client Error, Bad Requset\n" +
                                "The request could not be understood by the server due " +
                                "to malformed syntax.\n" +
                                "Details: The data file id that you provided is not valid" +
                                " : "+dataid+"\n\n", MediaType.TEXT_PLAIN);
                    getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST);    
                }

                // Kernel should be one of rbf, linear, sigmoid or polynomial
                if (getResponse().getStatus().equals(Status.SUCCESS_ACCEPTED)){
                if (
                        !(
                        (kernel.equalsIgnoreCase("rbf"))||
                        (kernel.equalsIgnoreCase("linear"))||
                        (kernel.equalsIgnoreCase("sigmoid"))||
                        (kernel.equalsIgnoreCase("polynomial"))
                        )
                    )
                {
                   getResponse().setEntity(
                                "Error 400: Client Error, Bad Requset\n" +
                                "The request could not be understood by the server due " +
                                "to malformed syntax.\n" +
                                "Details: The kernel you specified is not valid : "+
                                kernel+"\n\n", MediaType.TEXT_PLAIN);
                   getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST);    
                }
                }

                /**
                 * Cost should be convertible to Double and strictly
                 * positive.
                 */
                if (getResponse().getStatus().equals(Status.SUCCESS_ACCEPTED)){
                try{
                    d = Double.parseDouble(cost);
                    if (d<=0){
                        getResponse().setEntity(
                                "Error 400: Client Error, Bad Requset\n" +
                                "The request could not be understood by the server due " +
                                "to malformed syntax.\n" +
                                "Details: The cost should be strictly positive!"+"\n\n",
                                MediaType.TEXT_PLAIN);
                        getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
                    }
                }catch(NumberFormatException e){
                    getResponse().setEntity(
                                "Error 400: Client Error, Bad Requset\n" +
                                "The request could not be understood by the server due " +
                                "to malformed syntax.\n" +
                                "Details: The cost should be Double type, while you specified " +
                                "a non double value : "+cost+"\n\n",
                                MediaType.TEXT_PLAIN);
                    getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
                }
                }

                /**
                 * Epsilon should be convertible to Double and strictly
                 * positive.
                 */
                if (getResponse().getStatus().equals(Status.SUCCESS_ACCEPTED)){
                 try{
                     d=Double.parseDouble(epsilon);
                     if (d<=0){
                         getResponse().setEntity(
                                "Error 400: Client Error, Bad Requset\n" +
                                "The request could not be understood by the server due " +
                                "to malformed syntax.\n" +
                                "Details: Epsilon should be strictly positive!"+"\n\n",
                                MediaType.TEXT_PLAIN);
                         getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
                     }
                 }catch(NumberFormatException e){
                     getResponse().setEntity(
                                "Error 400: Client Error, Bad Requset\n" +
                                "The request could not be understood by the server due " +
                                "to malformed syntax.\n" +
                                "Details: Epsilon should be Double type, while you specified " +
                                "a non double value : "+epsilon+"\n\n",
                                MediaType.TEXT_PLAIN);
                     getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST);    
                 }
                }

                /**
                 * Degree should be a strictly positive integer
                 */
                if (getResponse().getStatus().equals(Status.SUCCESS_ACCEPTED)){
                try{
                    i=Integer.parseInt(degree);
                    if (i<=0){
                        getResponse().setEntity(
                                "Error 400: Client Error, Bad Requset\n" +
                                "The request could not be understood by the server due " +
                                "to malformed syntax.\n" +
                                "Details: The degree should be an Integer greater or equal to 1, while you specified " +
                                "an inacceptable value : "+degree+"\n\n",
                                MediaType.TEXT_PLAIN);
                        getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
                    }
                }catch(NumberFormatException e){
                    getResponse().setEntity(
                                "Error 400: Client Error, Bad Requset\n" +
                                "The request could not be understood by the server due " +
                                "to malformed syntax.\n" +
                                "Details: The degree should be an Integer, while you specified " +
                                "a non Integer value : "+degree+"\n\n",
                                MediaType.TEXT_PLAIN);
                    getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
                }
                }


                /**
                 * Gamma should be convertible to Double and strictly
                 * positive.
                 */
                if (getResponse().getStatus().equals(Status.SUCCESS_ACCEPTED)){
                 try{
                     d=Double.parseDouble(gamma);
                     if (d<=0){
                         getResponse().setEntity(
                                "Error 400: Client Error, Bad Requset\n" +
                                "The request could not be understood by the server due " +
                                "to malformed syntax.\n" +
                                "Details: Gamma should be strictly positive!"+"\n\n",
                                MediaType.TEXT_PLAIN);
                         getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
                     }
                 }catch(NumberFormatException e){
                     getResponse().setEntity(
                                "Error 400: Client Error, Bad Requset\n" +
                                "The request could not be understood by the server due " +
                                "to malformed syntax.\n" +
                                "Details: Gamma should be Double, while you specified " +
                                "a non double value : "+gamma+"\n\n",
                                MediaType.TEXT_PLAIN);
                     getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
                 }
                }

                /**
                 * coeff0 should be convertible to Double.
                 */
                if (getResponse().getStatus().equals(Status.SUCCESS_ACCEPTED)){
                  try{
                     d=Double.parseDouble(coeff0);
                  }catch(NumberFormatException e){
                      getResponse().setEntity(
                                "Error 400: Client Error, Bad Requset\n" +
                                "The request could not be understood by the server due " +
                                "to malformed syntax.\n" +
                                "Details: coeff0 should be Double, while you specified " +
                                "a non double value : "+coeff0+"\n\n",
                                MediaType.TEXT_PLAIN);
                     getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
                 }
                }

                /**
                 * Tolerance
                 */
                if (getResponse().getStatus().equals(Status.SUCCESS_ACCEPTED)){
                  try{
                     d=Double.parseDouble(tolerance);
                     if (d<=0){
                         getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
                     }
                 }catch(NumberFormatException e){
                     getResponse().setEntity(
                                "Error 400: Client Error, Bad Requset\n" +
                                "The request could not be understood by the server due " +
                                "to malformed syntax.\n" +
                                "Details: Tolerance should be (preferably small) Double, while you specified " +
                                "a non double value : "+tolerance+"\n\n",
                                MediaType.TEXT_PLAIN);
                     getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
                 }
                }


                /**
                 * cache size
                 */
                if (getResponse().getStatus().equals(Status.SUCCESS_ACCEPTED)){
                  try{
                     i=Integer.parseInt(cacheSize);
                     if (d<=0){
                         getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
                     }
                 }catch(NumberFormatException e){
                     getResponse().setEntity(
                                "Error 400: Client Error, Bad Requset\n" +
                                "The request could not be understood by the server due " +
                                "to malformed syntax.\n" +
                                "Details: cache size (in MB) should be an Integer, while you specified " +
                                "a non Integer value : "+cacheSize+"\n\n",
                                MediaType.TEXT_PLAIN);
                     getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
                 }
                }

                String ker="";
                if (kernel.equalsIgnoreCase("linear"))
                    ker="0";
                else if (kernel.equalsIgnoreCase("polynomial"))
                    ker="1";
                else if (kernel.equalsIgnoreCase("rbf"))
                    ker="2";
                else if (kernel.equalsIgnoreCase("sigmoid"))
                    ker="3";
                else
                    ker="3";

                String[] options = {""};
                String scaledPath = scaledDir+"/"+dataSetPrefix+dataid;
                String modelPath = CLS_SVM_modelsDir + "/" + model_id;
                if (ker.equalsIgnoreCase("0")){
                    String[] ops={
                        "-s", "0",
                        "-t", "0",
                        "-b","1",
                        "-c", cost,
                        "-e", tolerance,
                        "-q",
                        scaledPath,
                        modelPath

                    };
                    options=ops;
                }else if (ker.equalsIgnoreCase("1")){
                    String[] ops={
                        "-s", "0",// C-SVC (Classifier)
                        "-t", ker,
                        "-b","1",
                        "-c", cost,
                        "-g", gamma,
                        "-d", degree,
                        "-r", coeff0,
                        "-e", tolerance,
                        "-q",
                        scaledPath,
                        modelPath

                    };
                    options=ops;
                }else if (ker.equalsIgnoreCase("2")){
                    String[] ops={
                        "-s", "0",// C-SVC (Classifier)
                        "-t", ker,
                        "-b","1",
                        "-c", cost,
                        "-g", gamma,
                        "-e", tolerance,
                        "-q",
                        scaledPath,
                        modelPath

                    };
                    options=ops;
                }else if (ker.equalsIgnoreCase("3")){
                    String[] ops={
                        "-s", "0",// C-SVC (Classifier)
                        "-t", ker,
                        "-b","1",
                        "-c", cost,
                        "-g", gamma,
                        "-e", tolerance,
                        "-q",
                        scaledPath,
                        modelPath
                    };
                    options=ops;
                     for(int i=0; i<options.length; i++){
                        System.out.println(options[i]);
                    }
                }

                /**
                 * If all the posted parameters (kernel type, cost, gamma, etc)
                 * are acceptable the the status is 202.
                 */
            if (getResponse().getStatus().equals(Status.SUCCESS_ACCEPTED)){
                try {
                    
                    getResponse().setEntity(getResponse().getStatus().toString(),MediaType.TEXT_PLAIN);
                    long before=System.currentTimeMillis();
                    svm_train.main(options);
                    long timeSpent=System.currentTimeMillis()-before;

                        /**
                         * Check if the model was created.
                         * If yes, set the status to 200,
                         * otherwise the status is set to 500
                         */
                    File modelFile = new File(CLS_SVM_modelsDir+"/"+model_id);
                    boolean modelCreated = modelFile.exists();
                    if (!(modelCreated)){
                        getResponse().setEntity(
                                "Error 400: Client Error, Bad Requset\n" +
                                "The server encountered an unexpected condition " +
                                "which prevented it from fulfilling the request."+
                                "Details: Unexpected Error while trying to train the model."+"\n\n",
                                MediaType.TEXT_PLAIN);
                        getResponse().setStatus(Status.SERVER_ERROR_INTERNAL);
                    }else{
                        org.opentox.Applications.OpenToxApplication.dbcon.registerNewModel(baseURI+"/algorithm/learning/regression/mlr");
                        getResponse().setStatus(Status.SUCCESS_OK);
                    }
            

                    /**
                     * If the model was successfully created, that is the
                     * status is 200, return a report to the user in
                     * HTML form. This is for testing reasons only as according to
                     * the OpenTox API specification, the URI of the trained model
                     * should be returned
                     */
                    if (getResponse().getStatus().equals(Status.SUCCESS_OK)){
                    
                    getResponse().setEntity(ModelURI + "/" + model_id+"\n\n", MediaType.TEXT_PLAIN);
                    // TODO : create and store xml
                       StringBuilder xmlstr = new StringBuilder();
                       xmlstr.append(xmlIntro);
                       xmlstr.append("<ot:Model xmlns:ot=\"http://opentox.org/1.0/\" " +
                               "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" " +
                               "xsi:schemaLocation=\"http://opentox.org/1.0/Algorithm.xsd\" " +
                               "ID=\""+model_id+"\" Name=\"SVM Classification Model\">\n");
                           xmlstr.append("<ot:link href=\"" + SvcModelURI + "/" + model_id +  "\" />\n");
                           xmlstr.append("<ot:AlgorithmID href=\"" + SvcAlgorithmURI +  "\"/>\n");
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
                       try{

                           FileWriter fstream = new FileWriter(modelsXmlDir + "/" + model_id);
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
        }// end of svm classification algorithm
        /**
         * Implementation of the kNN Algorithm
         */
        else if (algorithmId.equalsIgnoreCase("knn")){
            getResponse().setEntity("Error 501: Not Implemented\n" +
                    "The server does not support the functionality required to fulfill the request.\n" +
                    "Details: This algorithm is not implemented yet!"+"\n\n",MediaType.TEXT_PLAIN);
            getResponse().setStatus(Status.SERVER_ERROR_NOT_IMPLEMENTED);
        }
        /**
         * Implementation of the J48 Algorithm
         */
        else if (algorithmId.equalsIgnoreCase("j48")){
            getResponse().setEntity("Error 501: Not Implemented\n" +
                    "The server does not support the functionality required to fulfill the request.\n" +
                    "Details: This algorithm is not implemented yet!"+"\n\n",MediaType.TEXT_PLAIN);
            getResponse().setStatus(Status.SERVER_ERROR_NOT_IMPLEMENTED);
        }
        /**
         * Implementation of the PLS Algorithm
         */
        else if (algorithmId.equalsIgnoreCase("pls")){
            getResponse().setEntity("Error 501: Not Implemented\n" +
                    "The server does not support the functionality required to fulfill the request.\n" +
                    "Details: This algorithm is not implemented yet!"+"\n\n",MediaType.TEXT_PLAIN);
            getResponse().setStatus(Status.SERVER_ERROR_NOT_IMPLEMENTED);
        }
        /**
         * Unknown Algorithm
         */
        else{
            getResponse().setEntity("Error 404: Not Found\n" +
                    "The server has not found anything matching the Request-URI or the server \n" +
                    "does not wish to reveal exactly why the request has been refused, or no other\n" +
                    "response is applicable.\n" +
                    "Details: This classification model : "+algorithmId+" was not found on the server!"+"\n\n",MediaType.TEXT_PLAIN);
            getResponse().setStatus(Status.CLIENT_ERROR_NOT_FOUND);
        }// end of all algorithms

    }// end of acceptRepresentation method




}// End of class ClassificationResource
