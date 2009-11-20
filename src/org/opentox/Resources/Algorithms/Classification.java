package org.opentox.Resources.Algorithms;

import org.opentox.Resources.*;
import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import org.opentox.util.libSVM.svm_train;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.data.Reference;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.representation.Variant;
import org.restlet.resource.ResourceException;
import org.opentox.Applications.OpenToxApplication;

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
@Deprecated
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

    /**
     * Auxiliary integer variable.
     */
    private int i;

    /**
     * Auxiliary double variable.
     */
    private double d;

    /**
     * Unique Integer value corresponding to a model - either classification
     * or regression
     */
    private int model_id;

    /**
     * The status of the Resource. It is initialized with
     * success/created (201) according to RFC 2616.
     */
    private Status internalStatus = Status.SUCCESS_CREATED;

    /**
     * Constructor
     * @param context
     * @param request
     * @param response
     */

    @Override
    public void doInit() throws ResourceException{
        super.doInit();
        List<Variant> variants = new ArrayList<Variant>();
        variants.add(new Variant(MediaType.TEXT_PLAIN));
        variants.add(new Variant(MediaType.TEXT_XML));
        /** Sometime we will support HTML representation for models **/
        variants.add(new Variant(MediaType.TEXT_HTML));
        getVariants().put(Method.GET, variants);
        this.algorithmId=Reference.decode(getRequest().getAttributes().get("id").toString());
    }
    


    /**
     * Materialization of the GET method. XML Representations are generated for
     * each algorithm (svm, pls, j48 and kNN). What is more an XML schema is
     * available.
     * @param variant
     * @return StringRepresentation
     */
    @Override
    public Representation get(Variant variant) {

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
     * Set the private status variable
     * @param status the new status
     */
    private void setInternalStatus(Status status){
        this.internalStatus=status;
    }


    /**
     * Returns the error representation after checking if the parameters posted
     * to the svm classification algorithm are acceptable. It returns <b>null</b>
     * if the parameters are acceptable, otherwise a representation in Plain Text
     * format explaining the problem.
     * @param form the form containing the posted parameters
     * @return A representation that is not null only when some
     * posted parameters are unacceptable.
     */
    private Representation checkSvcParameters(Form form){
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


    /***
     * Returns the options String array which is given as input to the
     * training algorithm.
     * @return
     */
    private String[] getSvcOptions(){
        String[] options = {""};
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
        String scaledPath = scaledDir+"/"+dataSetPrefix+dataid;
                String modelPath = CLS_SVM_modelsDir + "/" + model_id;
                if (ker.equalsIgnoreCase("0")){
                    String[] ops={
                        "-s", "0",
                        "-t", "0",
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
                        "-c", cost,
                        "-g", gamma,
                        "-e", tolerance,
                        "-q",
                        scaledPath,
                        modelPath
                    };
                    options=ops;
                }
        return options;
    }




    /**
     * Processing of POST requests.
     * @param entity
     */
    @Override
    public Representation post(Representation entity)
    {

        Representation representation = null;

        /**
         * Once the representation is accepted, the status
         * is set to 202 (Success - Accepted) because the action cannot be
         * carried out immediately (RFC 2616, sec. 10).
         */
        setInternalStatus(Status.SUCCESS_ACCEPTED);


        model_id = org.opentox.Applications.OpenToxApplication.dbcon.getModelsStack()+1;
    
        /**
         * Implementation of the SVM classification algorithm.
         */
        if (algorithmId.equalsIgnoreCase("svc"))
        {
                       
                
                Form form = new Form(entity);
                Representation errorRep = checkSvcParameters(form);
                if (errorRep!=null){
                    representation = errorRep;
                }




                /**
                 * If all the posted parameters (kernel type, cost, gamma, etc)
                 * are acceptable the the status is 202.
                 */
            if (Status.SUCCESS_ACCEPTED.equals(internalStatus)){
                try {
                    
                    representation = new StringRepresentation(internalStatus.toString(),MediaType.TEXT_PLAIN);
                    svm_train.main(getSvcOptions());

                        /**
                         * Check if the model was created.
                         * If yes, set the status to 200,
                         * otherwise the status is set to 500
                         */
                    File modelFile = new File(CLS_SVM_modelsDir+"/"+model_id);
                    boolean modelCreated = modelFile.exists();
                    if (!(modelCreated)){
                        representation = new StringRepresentation(
                                "Error 400: Client Error, Bad Requset\n" +
                                "The server encountered an unexpected condition " +
                                "which prevented it from fulfilling the request."+
                                "Details: Unexpected Error while trying to train the model."+"\n\n",
                                MediaType.TEXT_PLAIN);
                        setInternalStatus(Status.SERVER_ERROR_INTERNAL);
                    }else{ /** if the model was successfully created... **/
                        org.opentox.Applications.OpenToxApplication.dbcon.registerNewModel(baseURI+"/algorithm/learning/classification/svc");
                        setInternalStatus(Status.SUCCESS_OK);
                    }
            

                    /**
                     * If the model was successfully created, that is the
                     * status is 200, return a report to the user in
                     * HTML form. This is for testing reasons only as according to
                     * the OpenTox API specification, the URI of the trained model
                     * should be returned
                     */
                    if (internalStatus.equals(Status.SUCCESS_OK)){
                    
                    representation = new StringRepresentation(ModelURI + "/" + model_id+"\n\n", MediaType.TEXT_PLAIN);
                    // TODO : create and store xml
                       StringBuilder xmlstr = new StringBuilder();
                       xmlstr.append(xmlIntro);
                       xmlstr.append("<ot:Model xmlns:ot=\"http://opentox.org/1.0/\" " +
                               "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" " +
                               "xsi:schemaLocation=\"http://opentox.org/1.0/Algorithm.xsd\" " +
                               "ID=\""+model_id+"\" Name=\"SVM Classification Model\">\n");
                           xmlstr.append("<ot:link href=\"" + URIs.modelURI + "/" + model_id +  "\" />\n");
                           xmlstr.append("<ot:AlgorithmID href=\"" + URIs.svcAlgorithmURI +  "\"/>\n");
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
                    OpenToxApplication.opentoxLogger.log(Level.SEVERE, null, ex);
                }
            }
        }// end of svm classification algorithm




        /**
         * Implementation of the kNN Algorithm
         */
        else if (algorithmId.equalsIgnoreCase("knn")){
            representation = new StringRepresentation("Error 501: Not Implemented\n" +
                    "The server does not support the functionality required to fulfill the request.\n" +
                    "Details: This algorithm is not implemented yet!"+"\n\n",MediaType.TEXT_PLAIN);
            setInternalStatus(Status.SERVER_ERROR_NOT_IMPLEMENTED);
        }
        /**
         * Implementation of the J48 Algorithm
         */
        else if (algorithmId.equalsIgnoreCase("j48")){
            representation = new StringRepresentation("Error 501: Not Implemented\n" +
                    "The server does not support the functionality required to fulfill the request.\n" +
                    "Details: This algorithm is not implemented yet!"+"\n\n",MediaType.TEXT_PLAIN);
            setInternalStatus(Status.SERVER_ERROR_NOT_IMPLEMENTED);
        }
        /**
         * Implementation of the PLS Algorithm
         */
        else if (algorithmId.equalsIgnoreCase("pls")){
            representation = new StringRepresentation("Error 501: Not Implemented\n" +
                    "The server does not support the functionality required to fulfill the request.\n" +
                    "Details: This algorithm is not implemented yet!"+"\n\n",MediaType.TEXT_PLAIN);
            setInternalStatus(Status.SERVER_ERROR_NOT_IMPLEMENTED);
        }
        /**
         * Unknown Algorithm
         */
        else{
            representation = new StringRepresentation("Error 404: Not Found\n" +
                    "The server has not found anything matching the Request-URI or the server \n" +
                    "does not wish to reveal exactly why the request has been refused, or no other\n" +
                    "response is applicable.\n" +
                    "Details: This classification model : "+algorithmId+" was not found on the server!"+"\n\n",MediaType.TEXT_PLAIN);
            setInternalStatus(Status.CLIENT_ERROR_NOT_FOUND);
        }// end of all algorithms

    getResponse().setStatus(internalStatus);
    return representation;
    }// end of post method




}// End of class ClassificationResource