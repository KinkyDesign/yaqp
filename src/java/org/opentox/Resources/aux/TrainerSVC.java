package org.opentox.Resources.aux;

import org.opentox.Resources.*;
import org.restlet.Context;
import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.resource.Representation;
import org.restlet.resource.StringRepresentation;
import org.restlet.resource.Variant;

/**
 * HTML interface to provide parameters to the SVC training resource.
 * @author OpenTox - http://www.opentox.org
 * @author Sopasakis Pantelis
 * @author Sarimveis Harry
 * @version 1.0 (Last Update: Aug 20, 2009)
 */
public class TrainerSVC extends AbstractResource
{
    private static final long serialVersionUID = 10012190008004001L;


    /**
     * Public Constructor of the Class Poster
     * @param context
     * @param request
     * @param response
     */
    public TrainerSVC(Context context, Request request, Response response) {
        super(context, request, response);
        getVariants().add(new Variant(MediaType.TEXT_HTML));
    }


    @Override
    public Representation represent(Variant variant){

        StringBuilder builder = new StringBuilder();
        builder.append(htmlHEAD);
        
            // HTML Form used to post data to this resource.
            builder.append("<form enctype=\"application/x-www-form-urlencoded\" method=\"POST\" action=\""+
                    getRequest().getRootRef()+"/algorithm/learning/classification/svc");
            builder.append("\" >");
            builder.append("<p>Train an SVM model<br/>" +
                    "<span style=\"font-size:small\">Simple interface for testing purposes only!</span></p>");
            builder.append("Training File:<br/>dataSet-<input type=\"text\" size=\"10\" maxlength=\"10\" name=\"dataId\"><br/>");
            builder.append("kernel:<br/><input type=\"text\" size=\"10\" maxlength=\"10\" name=\"kernel\"><br/>");
            builder.append("gamma:<br/><input type=\"text\" size=\"10\" maxlength=\"10\" name=\"gamma\"><br/>");
            builder.append("cost:<br/><input type=\"text\" size=\"10\" maxlength=\"10\" name=\"cost\"><br/>");
            builder.append("epsilon:<br/><input type=\"text\" size=\"10\" maxlength=\"10\" name=\"epsilon\"><br/>");
            builder.append("coeff0:<br/><input type=\"text\" size=\"10\" maxlength=\"10\" name=\"coeff0\"><br/>");
            builder.append("degree:<br/><input type=\"text\" size=\"10\" maxlength=\"10\" name=\"degree\"><br/>");
            builder.append("tolerance:<br/><input type=\"text\" size=\"10\" maxlength=\"10\" name=\"tolerance\"><br/>");
            builder.append("cacheSize:<br/><input type=\"text\" size=\"10\" maxlength=\"10\" name=\"cacheSize\"><br/>");
            builder.append("<br/><br/><input type=\"submit\" value=\"Train\">");
            builder.append("</form>");

        builder.append(htmlEND);
        return new StringRepresentation(builder.toString(), MediaType.TEXT_HTML);
    }

    @Override
    public boolean allowPost(){
        return false;
    }


}
