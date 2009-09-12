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
 * @author OpenTox - http://www.opentox.org
 * @author Sopasakis Pantelis
 * @author Sarimveis Harry
 * @version 1.0 (Last Update: Aug 24, 2009)
 */
public class ValidatorSVC extends AbstractResource{

    private static final long serialVersionUID = 10012190008007001L;

    public ValidatorSVC(Context context, Request request, Response response) {
        super(context, request, response);
        getVariants().add(new Variant(MediaType.TEXT_HTML));
    }



    @Override
    public Representation represent(Variant variant){

        StringBuilder builder = new StringBuilder();
        builder.append(htmlHEAD);

            // HTML Form used to post data to this resource.
            builder.append("<form enctype=\"application/x-www-form-urlencoded\" method=\"POST\" action=\""+
                    getRequest().getRootRef()+"/validation/test_set_validation/svc");
            builder.append("\" >");
            builder.append("<h2>Validate an SVM-Classification model</h2>" +
                    "<span style=\"font-size:small\">Simple interface for testing purposes only!</span></p>");
            builder.append("<br/><br/><br/>");
            
            builder.append("Test DataSet Id: <br/><tt>dataSet-</tt><input type=\"text\" size=\"5\" maxlength=\"10\" name=\"dataid\">");
            
            builder.append("<br/><br/>Model Id: <br/><tt>model-</tt><input type=\"text\" size=\"5\" maxlength=\"10\" name=\"modelid\"></td>");
            
            
            builder.append("<br/><br/><input type=\"submit\" value=\"Validate\">");
            
            
            builder.append("</form>");

        builder.append(htmlEND);
        return new StringRepresentation(builder.toString(), MediaType.TEXT_HTML);
    }

    @Override
    public boolean allowPost(){
        return false;
    }

}
