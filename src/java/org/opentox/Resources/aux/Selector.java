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
 *
 * @author OpenTox, http://www.opentox.org/
 * @author Sopasakis Pantelis
 * @author Sarimveis Harry
 */
public class Selector extends AbstractResource
{

    private static final long serialVersionUID = 10012190008002001L;


    /**
     * Public Constructor of the Class Poster
     * @param context
     * @param request
     * @param response
     */
    public Selector(Context context, Request request, Response response) {
        super(context, request, response);
        getVariants().add(new Variant(MediaType.TEXT_HTML));
    }


    @Override
    public Representation represent(Variant variant){

        StringBuilder builder = new StringBuilder();
        builder.append(htmlHEAD);

            // HTML Form used to post data to this resource.
            builder.append("<form enctype=\"application/x-www-form-urlencoded\" method=\"POST\" action=\""+
                    getRequest().getRootRef()+"/algorithm/preprocessing/featureselection/infoGainAttributeEvaluation");
            builder.append("\" >");
            builder.append("<p>Attribute Selection<br/>" +
                    "<span style=\"font-size:small\">Simple interface for testing purposes only!</span></p>");
            builder.append("Data File:<br/>OpenTox-<input type=\"text\" size=\"10\" maxlength=\"10\" name=\"dataId\"><br/>");
            builder.append("Threshold:<br/><input type=\"text\" size=\"10\" maxlength=\"10\" name=\"threshold\"><br/>");
            builder.append("NTS:<br/><input type=\"text\" size=\"10\" maxlength=\"10\" name=\"numToSel\"><br/>");
            builder.append("<br/><br/><input type=\"submit\" value=\"Select\">");
            builder.append("</form>");

        builder.append(htmlEND);
        return new StringRepresentation(builder.toString(), MediaType.TEXT_HTML);
    }

    @Override
    public boolean allowPost(){
        return false;
    }


}
