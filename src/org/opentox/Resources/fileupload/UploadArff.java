package org.opentox.Resources.fileupload;

import org.opentox.Resources.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.opentox.Applications.OpenToxApplication;
import org.opentox.util.arff2dsd;
import org.opentox.util.svm_scale;
import org.restlet.Context;
import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.data.Status;
import org.restlet.ext.fileupload.RestletFileUpload;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.representation.Variant;
import org.restlet.resource.ResourceException;
import org.apache.commons.io.output.DeferredFileOutputStream;

/**
 *
 * Resource that enables the clients to upload text files on the server. The upload
 * limit is set to 100kB which is adequate for even large data sets for testing purposes.
 * @author OpenTox - http://www.opentox.org
 * @author Sopasakis Pantelis
 * @author Sarimveis Harry
 * @version 1.6 (Last Update: Augg 23, 2009)
 */
public class UploadArff extends AbstractResource{

    private static final long serialVersionUID = 882349871231246000L;

    private String fileName="";
    private static final int uploadLimit=10000000;
    private boolean itsok=true;


 @Override
    public void doInit() throws ResourceException{
        super.doInit();
//        Collection<Method> allowedMethods = new ArrayList<Method>();
//        allowedMethods.add(Method.GET);
//        allowedMethods.add(Method.POST);
//        getAllowedMethods().addAll(allowedMethods);
        List<Variant> variants = new ArrayList<Variant>();
        variants.add(new Variant(MediaType.TEXT_PLAIN));
        variants.add(new Variant(MediaType.TEXT_XML));
        /** Sometime we will support HTML representation for models **/
        variants.add(new Variant(MediaType.TEXT_HTML));
        getVariants().put(Method.GET, variants);

    }


    @Override
    public Representation get(Variant variant){
        StringBuilder builder = new StringBuilder();

        builder.append(htmlHEAD);
        builder.append("<h2>Upload your File</h2>");
        builder.append(
                "<h3>Arff Data File Upload</h3>" +
                "<br/>Pick a txt File:<br/><br/>" +
                "<form enctype=\"multipart/form-data\" method=\"POST\" action=\"");
            // The action of the web form points to this resource.
            builder.append(getRequest().getResourceRef().getIdentifier());
        builder.append("\">");
        builder.append("<input type=\"file\" name=\"file\"> <br/><br/>" );
        builder.append("<input type=\"submit\" value=\"Upload It!\">");
        builder.append("</form><br/>");



        builder.append("<p align=\"justify\" style=\"width:450px; font-size:small\">" +
                "Upload Limit: 100kB<br/>" +
                "Once you have uploaded your file successfully, a unique ID " +
                "will be assigned to your file that enables you to make use of it," +
                "read it, download it, delete it or encapsulate it in a computational " +
                "process.</p>");



        builder.append(htmlEND);

        return new StringRepresentation(builder.toString(), MediaType.TEXT_HTML);
    }



    @Override
    public Representation post(Representation entity) {
    Representation rep = null;
       if (entity != null) {
            if (MediaType.MULTIPART_FORM_DATA.equals(entity.getMediaType(), true)) {
                getResponse().setStatus(Status.SUCCESS_ACCEPTED);


            	String path = arffDir;
            	File folder = new File(path);
            	OpenToxApplication.opentoxLogger.config("Uploading file(s) to:" + path);
            	File[] listOfFiles = folder.listFiles();
                int ln = listOfFiles.length;
                

                /*
                 *  The Apache FileUpload project parses HTTP requests which
                 *  conform to RFC 1867, "Form-based File Upload in HTML". That
                 *  is, if an HTTP request is submitted using the POST method,
                 *  and with a content type of "multipart/form-data", then
                 *  FileUpload can parse that request, and get all uploaded files
                 *  as FileItem.
                 */

                /*
                 *  1. Create a factory for disk-based file items
                 */
                DiskFileItemFactory factory = new DiskFileItemFactory();
                factory.setSizeThreshold(uploadLimit);

                /*
                 *  2. Create a new file upload handler based on the Restlet
                 *  FileUpload extension that will parse Restlet requests and
                 * generates FileItems.
                 */
                RestletFileUpload upload = new RestletFileUpload(factory);
                List<FileItem> items;
                try {

                    /*
                     * 3/ Request is parsed by the handler which generates a
                     * list of FileItems
                     */
                    items = upload.parseRequest(getRequest());

                    /*
                     *  Process only the uploaded item called "file" and
                     *  save it on disk
                     */
                    boolean found = false;
                    boolean sizeFlag = false;
                    for (final Iterator<FileItem> it = items.iterator(); it.hasNext() && !found;) {
                        FileItem fi = it.next();
                        if (fi.getFieldName().equals("file")) {
                            found = true;
                            String name = dataSetPrefix+ln;

                            File file = new File(arffDir, name);

                            /*
                             *  restrict file size
                             */
                            if(fi.getSize() <= uploadLimit){
                            	
                            	fi.write(file);
                            	sizeFlag = true;
                            }
                            setFileName(name);
                        }
                    }

                    /*
                     * Once handled, the content of the uploaded file is sent
                     * back to the client.
                     */
                    
                    if (found && sizeFlag) {


                                /**
                                 * Preprocessing of the data file:
                                 * First the arff file is converted into an equivalent dsd representation
                                 * and a meta file is generated:
                                 */
                                arff2dsd arff2dsdConverter =
                                        new arff2dsd(arffDir+"/"+getFileName(),
                                        metaDir+"/"+getFileName(),
                                        dsdDir+"/"+getFileName()
                                        );
                                arff2dsdConverter.convert();


                                /***
                                 *
                                 * Check if the DSD file was created.
                                 * If not, delete the arff file!
                                 */
                                File arffFile = new File(arffDir+"/"+getFileName()),
                                        dsdFile = new File(dsdDir+"/"+getFileName()),
                                        metaFile = new File(metaDir+"/"+getFileName());
                                if (
                                        (!(dsdFile.exists()))||
                                        (!(metaFile.exists()))
                                   )
                                   {
                                    arffFile.delete();
                                    dsdFile.delete();
                                    metaFile.delete();
                                    itsok=false;
                                    
                                }



                                /**
                                 * If itsok=true proceed...
                                 * The arff file is converted into an equivalent
                                 * XRFF representation
                                 */
                                if (itsok){
                                    weka.core.converters.XRFFSaver xrffSaver = new weka.core.converters.XRFFSaver();
                                    String[] xrffOptions = {
                                    "-i",arffDir+"/"+getFileName(),
                                    "-o",xrffDir+"/"+getFileName()
                                    };
                                    xrffSaver.setOptions(xrffOptions);
                                    xrffSaver.writeBatch();
                                    File xrffFile = new File(xrffDir+"/"+getFileName()+".xrff");
                                    xrffFile.renameTo(new File(xrffDir+"/"+getFileName()));

                                    /***
                                     *
                                     * Check if the xrff File was really created...
                                     */
                                    File renamedXrffFile = new File(xrffDir+"/"+getFileName());
                                    if (!(renamedXrffFile.exists())){
                                        arffFile.delete();
                                        itsok=false;

                                    }
                                }




                                /**
                                 * If itsok=true proceed....
                                 * Scale the generated DSD file.
                                 * Two files are produced: The one is a dsd-formated
                                 * file containing the scaled data and the other one
                                 * it a file containing the range of each attribute.
                                 */

                                if (itsok){
                                    String[] ops=
                                    {"-l", "-1",
                                     "-u",  "1",
                                     "-s",
                                     rangeDir+"/"+getFileName(), dsdDir+"/"+getFileName()
                                    };

                                    svm_scale scaler = new svm_scale();
                                    scaler.scale(ops, scaledDir+"/"+getFileName());

                                    /***
                                     * Check if the scaled file and the DSD file were created...
                                     */
                                    File scaledFile = new File(scaledDir+"/"+getFileName());
                                    if (!(scaledFile.exists())){
                                        arffFile.delete();
                                        itsok=false;
                                    }

                                }


                         if (itsok){
                             StringBuilder builder = new StringBuilder();
                             builder.append(baseURI+"/dataset/"+getFileName()+"\n");
                             rep = new StringRepresentation(builder.toString(), MediaType.TEXT_PLAIN);
                         }else{
                             rep = new StringRepresentation("Error while trying to upload the file!", MediaType.TEXT_PLAIN);
                         }


                    }
                    else {
                        /**
                         * If the file was unacceptably large!
                         */
                    	if (found){
                            getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
                            StringBuilder builder = new StringBuilder();
                             builder.append("Error 400!\n" +
                                     "The file you tried to upload is very large!\n");

                             rep = new StringRepresentation(builder.toString(), MediaType.TEXT_PLAIN);

                    	}else{
                            /*
                             *  Some problem occurs, sent back a simple line of text.
                             */
                    		rep = new StringRepresentation("No file was uploaded!\n", MediaType.TEXT_PLAIN);
                                getResponse().setStatus(Status.SERVER_ERROR_INTERNAL);
                    	}
                    }
                    /*
                     *  Set the representation of the resource once the POST
                     *  request has been handled.
                     */

                        


                     //   getResponse().setEntity(representation);
                    //    Set the status of the response.
                    if (Status.SUCCESS_ACCEPTED.equals(getResponse().getStatus())){
                        getResponse().setEntity(rep);
                        getResponse().setStatus(Status.SUCCESS_OK);
                    }

                } catch (Exception e) {
                    /*
                     *  The message of all thrown exception is sent back to
                     *  client as simple plain text
                     */
                    getResponse().setEntity(new StringRepresentation("Error 400!\n" +
                            "Possibly the file you provided does not comply with the" +
                            " proposed standards or an internal server error occured.\n" +
                            "Details : "+
                            e.getMessage()+"\n", MediaType.TEXT_PLAIN));
                    /**
                     * In that case, leave no trace of the bad files...
                     */
                    File arffFile = new File(arffDir+"/"+getFileName()),
                            dsdFile = new File(dsdDir+"/"+getFileName()),
                            scaledFile = new File(scaledDir+"/"+getFileName()),
                            xrffFile = new File(xrffDir+"/"+getFileName()),
                            metaFile = new File(metaDir+"/"+getFileName());
                    arffFile.delete();
                    dsdFile.delete();
                    scaledFile.delete();
                    xrffFile.delete();
                    metaFile.delete();

                    getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
                    e.printStackTrace();
                }
            }
        } else {
            /*
             *  POST request with no entity.
             */
            getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
            /**
                     * In that case, leave no trace of the bad files...
                     */
                    File arffFile = new File(arffDir+"/"+getFileName()),
                            dsdFile = new File(dsdDir+"/"+getFileName()),
                            scaledFile = new File(scaledDir+"/"+getFileName()),
                            xrffFile = new File(xrffDir+"/"+getFileName()),
                            metaFile = new File(metaDir+"/"+getFileName());
                    arffFile.delete();
                    dsdFile.delete();
                    scaledFile.delete();
                    xrffFile.delete();
                    metaFile.delete();
        }
       return rep;
    }


    private void setFileName(String name){
    	this.fileName = name;
    }



    private String getFileName(){
    	return this.fileName;
    }



}
