package org.opentox.Resources.Algorithms;



import weka.core.Instances;
import weka.attributeSelection.*;
import weka.filters.*;
import weka.core.converters.*;
import org.restlet.Context;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Reference;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.data.Status;
import org.restlet.resource.Representation;
import org.restlet.resource.Variant;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.File;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.opentox.Resources.AbstractResource;
import org.opentox.util.arff2dsd;
import org.opentox.util.svm_scale;
import org.restlet.resource.StringRepresentation;
import weka.core.FastVector;
import weka.core.ProtectedProperties;




/**
 * Atribute Selection using the algorithm Ranker (infoGainAttributeEvaluation).
 * <p style="width:50%">
 * The user provides two parameters - namely numToSel and threshold - and the dataset
 * id. A new data set in ARFF format is generated and the URL of the new dataset is then
 * returned to the user.
 * </p>
 * @author OpenTox, http://www.opentox.org
 * @author Sopasakis Pantelis
 * @author Sarimveis Harry
 * @version 1.4
 */
public class AttributeSelection extends AbstractResource {

    /**
     * Class id
     */
    private static final long serialVersionUID = 10012190006001009L;

    /**
     * Folder containing the unfiltered arff data files
     */
    private static final String preAttribFolder = arffDir;

    /**
     * Folder containing the filtered data files
     */
    private static final String postAttribFolder = arffDir;

    /**
     * Threshold
     */
    private String threshold = "";
    /**
     * Number of attributes to be removed from the data set
     */
    private String numToSel="";
    /**
     * Data id
     */
    private String dataid;
    /**
     * Algorithm id. e.g. infoGainAttributeEvaluation
     */
    private String algorithm_id;


    
    /**
     * Constructor.
     * @param con
     * @param req
     * @param resp
     */
    public AttributeSelection(Context context, Request request, Response response){
        super(context,request,response);
        algorithm_id = Reference.decode(request.getAttributes().get("algorithm_id").toString());
        getVariants().add(new Variant(MediaType.TEXT_XML));
    }

    /**
     * Provide an XML representation of the algorithm
     * @param variant
     * @return
     */
    @Override
    public Representation represent(Variant variant) {
        StringBuilder builder = new StringBuilder();
        if (algorithm_id.equalsIgnoreCase("infoGainAttributeEvaluation")){
            builder.append(xmlIntro);
            builder.append("<Algorithm id=\"infoGainAttributeEvaluation\" name=\"Ranker-infoGainAttributeEvaluation\">\n");
            builder.append("<AlgorithmType>Feature Selection Algorithm</AlgorithmType>\n");
            builder.append("<Parameters>\n");
            builder.append("<!-- Number of attributes to be selected. If -1 this parameter will be ignored.  -->\n");
            builder.append("<param type=\"negativeInteger\" defaultvalue=\"-1\" >numToSel</param>\n");
            builder.append("<param type=\"double\" defaultvalue=\"-1.7976931348623157E308\">threshold</param>\n");
            builder.append("</Parameters>\n");
            builder.append("<statisticsSupported>\n");
            builder.append("</statisticsSupported>\n");
            builder.append("</Algorithm>\n\n");
        }else{
            builder.append(xmlIntro);
            builder.append("<Algorithm id=\"unknownAlgorithm\" name=\"unknown algorithm\">\n");
            builder.append("<AlgorithmType>Feature Selection Algorithm</AlgorithmType>\n");
            builder.append("<info>The requested algorithm was not found on the server!</info>\n");
            builder.append("</Algorithm>\n\n");
            getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
        }
        

        return new StringRepresentation(builder.toString(), MediaType.TEXT_XML);
    }




    /**
     * Allows data to be posted.
     * @return true
     */
    @Override
    public boolean allowPost(){
        return true;
    }



    /**
     * Implementation of the POST method
     * @param entity
     */
    @Override
    public void acceptRepresentation(Representation entity){

        getResponse().setStatus(Status.SUCCESS_ACCEPTED);


        /**
         * Retrieve posted parameters
         */
        Form form = new Form(entity);
        dataid = form.getFirstValue("dataId");
        threshold=form.getFirstValue("threshold");
        numToSel=form.getFirstValue("numToSel");

        try{
                File dataFile = new File(preAttribFolder+'/'+dataSetPrefix+dataid);
                boolean exists = dataFile.exists();
                if (!exists){    //// File not found Exception!
                getResponse().setEntity("Error 400: Client Error, Bad Requset\n"
                       +"The request could not be understood by the server due "
                       +"to malformed syntax.\n"
                       +"Details: Data File Not Found on the server!\n",MediaType.TEXT_PLAIN);
                logger.info("Attribute Selection: Data File Not Found on the server!");
               getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
                }
        }
        catch(Exception fileException){
                getResponse().setEntity("Error 400: Client Error, Bad Requset\n" +
                "The request could not be understood by the server due " +
                "to malformed syntax.\n"
                +"Details: The data file id that you provided is not valid"
                +" : "+dataid+"\n", MediaType.TEXT_PLAIN );
                    getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST);

        }




        if (getResponse().getStatus().equals(Status.SUCCESS_ACCEPTED)){
            try{
                Instances preAttribInst    = new Instances(
                        new BufferedReader(new FileReader(preAttribFolder+'/'+dataSetPrefix+dataid)));
                
                preAttribInst.setClassIndex(preAttribInst.numAttributes() - 1);

                // remove from the initial data set the first attribute:
                // @attribute id String
                preAttribInst.deleteAttributeAt(0);

                weka.filters.supervised.attribute.AttributeSelection toxfilter = new
                        weka.filters.supervised.attribute.AttributeSelection();
                InfoGainAttributeEval toxevaluator  = new InfoGainAttributeEval();
                Ranker toxRanker = new Ranker();

                toxRanker.setGenerateRanking(true);
                toxRanker.setThreshold(Double.parseDouble(threshold));
                toxRanker.setNumToSelect(Integer.parseInt(numToSel));
                

                toxfilter.setEvaluator(toxevaluator);
                toxfilter.setSearch(toxRanker);
                toxfilter.setInputFormat(preAttribInst);
                Instances m_postAttribInst = Filter.useFilter(preAttribInst, toxfilter);

                File arffFolder = new File(postAttribFolder);
                int newid = arffFolder.list().length;


                FastVector vec = null;
                ProtectedProperties props = new ProtectedProperties(new Properties());
                weka.core.Attribute att = new weka.core.Attribute("id", vec, props);
                m_postAttribInst.insertAttributeAt(att, 0);


                Instances init   = new Instances(
                        new BufferedReader(new FileReader(preAttribFolder+'/'+dataSetPrefix+dataid)));
                for (int k=0;k<m_postAttribInst.numInstances();k++){
                    m_postAttribInst.instance(k).setValue(0,init.instance(k).stringValue(0));

                }

                
                String postAttribFile = dataSetPrefix+newid;
                String postFileName = postAttribFolder+"/"+postAttribFile;

                /**
                 * Write the filtered data in an arff file...
                 */
                ArffSaver post_saver = new ArffSaver();
                post_saver.setInstances(m_postAttribInst);
                post_saver.setFile(new File(postFileName));
                post_saver.setDestination(new File(postFileName));
                post_saver.writeBatch();

                /**
                 * Convert the generated arff file to the equivalent dsd
                 * format:
                 */
                arff2dsd arff2dsdConverter = new arff2dsd(postFileName,
                        metaDir+"/"+postAttribFile,
                        dsdDir+"/"+postAttribFile);
                arff2dsdConverter.convert();




                /**
                 * Scale the generated DSD file:
                 */
                String[] ops=
                {
                    "-l", "-1",
                    "-u",  "1",
                    "-s",
                    rangeDir+"/"+postAttribFile,
                    dsdDir+"/"+postAttribFile
                };

                svm_scale scaler = new svm_scale();
                scaler.scale(ops, scaledDir+"/"+postAttribFile);


                /**
                 * Convert the arff representation into
                 * an XRFF equivalent one!
                 */
                weka.core.converters.XRFFSaver xrffSaver = new weka.core.converters.XRFFSaver();
                                    String[] xrffOptions = {
                                    "-i",arffDir+"/"+postAttribFile,
                                    "-o",xrffDir+"/"+postAttribFile
                                    };
                                    xrffSaver.setOptions(xrffOptions);
                                    xrffSaver.writeBatch();
                                    File xrffFile = new File(xrffDir+"/"+postAttribFile+".xrff");
                                    xrffFile.renameTo(new File(xrffDir+"/"+postAttribFile));


                getResponse().setEntity(baseURI+"/dataset/"+postAttribFile+"\n"
                        ,MediaType.TEXT_PLAIN);
                getResponse().setStatus(Status.SUCCESS_OK);

            }
            catch (Exception ex) {
                getResponse().setEntity("Error 500: Server Error, Internal\n" +
                "Description: The server encountered an unexpected condition which prevented it \n" +
                "from fulfilling the request.\n" +"Details: "+ex.getMessage()+"\n\n" ,
                 MediaType.TEXT_PLAIN);
                getResponse().setStatus(Status.SERVER_ERROR_INTERNAL);
                Logger.getLogger(AttributeSelection.class.getName()).log(Level.SEVERE,null, ex);
            }
        }
 
}

    

    }
    
    
    


    
