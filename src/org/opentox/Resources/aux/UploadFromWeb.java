package org.opentox.Resources.aux;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.opentox.Resources.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import org.opentox.util.arff2dsd;
import org.restlet.Context;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.resource.Representation;
import org.restlet.resource.StringRepresentation;
import org.restlet.resource.Variant;

/**
 * UNDER DEVELOPEMENT!!!
 * Resource that enables the clients to upload text files on the server that are located
 * somewhere on the web. The upload limit is set to 100kB which is adequate for even
 * large data sets for testing purposes. The user provides the URL for an arff file.
 * @author OpenTox - http://www.opentox.org
 * @author Sopasakis Pantelis
 * @author Sarimveis Harry
 * @version 1.4 (Last Update: Augg 23, 2009)
 */
public class UploadFromWeb extends AbstractResource{

    private static final long serialVersionUID = 104341464172580L;

    private String fileURL="";
    private static final int uploadLimit=100000;
    


    public UploadFromWeb(Context context, Request request, Response response) {
        super(context, request, response);
        getVariants().add(new Variant(MediaType.TEXT_HTML));
        getVariants().add(new Variant(MediaType.TEXT_PLAIN));
        
    }


    @Override
    public Representation represent(Variant variant){
        StringBuilder builder = new StringBuilder();

        builder.append(htmlHEAD);
        builder.append("<h2>Upload an Online ARFF file on the server</h2>");
        builder.append(
                "<h3>Arff URI to be Uploaded on the server</h3>" +
                "<br/>Enter the URI:<br/><br/>" +
                "<form enctype=\"application/x-www-form-urlencoded\" method=\"POST\" action=\"");
            // The action of the web form points to this resource.
            builder.append(getRequest().getResourceRef().getIdentifier());
        builder.append("\">");
        builder.append("<input type=\"text\" name=\"file\"> <br/><br/>" );
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
    public boolean allowPost(){
        return true;
    }

    @Override
    public boolean allowGet(){
        return true;
    }

    @Override
    public void acceptRepresentation(Representation entity){
        

            Form form = new Form(entity);
            fileURL=form.getFirstValue("file");
        {
            FileOutputStream out = null;
            {
                InputStream in = null;
                try {
                    URL url = new URL(fileURL);

                    File arffDirectory = new File(arffDir);
                    File[] arffFiles = arffDirectory.listFiles();
                    File file = new File(arffDir + "/"+dataSetPrefix+arffFiles.length);
                    out = new FileOutputStream(file);
                    in = url.openStream();

                    byte[] buf = new byte[4096];
                    int bytesRead;

                    while ((bytesRead = in.read(buf)) != -1) {
                        out.write(buf, 0, bytesRead);
                    }
                    

                }
                catch (IOException ex) {
                    Logger.getLogger(UploadFromWeb.class.getName()).log(Level.SEVERE, null, ex);
                }
                finally {
                    try {
                        out.close();
                    } catch (IOException ex) {
                        Logger.getLogger(UploadFromWeb.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    try {
                        in.close();
                    } catch (IOException ex) {
                        Logger.getLogger(UploadFromWeb.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
            
        
    }

 

    private void setFileName(String name){
    	this.fileURL = name;
    }



    private String getFileName(){
    	return this.fileURL;
    }


}
