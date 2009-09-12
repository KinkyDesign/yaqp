package org.opentox.Resources;


import org.opentox.util.RepresentationFactory;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.restlet.Context;
import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.resource.Representation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.StringRepresentation;
import org.restlet.resource.Variant;



/**
 * REST Web Service
 *
 * @author OpenTox - http://www.opentox.org/
 * @author Sopasakis Pantelis
 * @author Sarimveis Harry
 * @version 1.0 (Last Update: Aug 20, 2009)
 */
public class StyleSheetResource extends AbstractResource
{

    private static final long serialVersionUID = 10012190008701L;

    public StyleSheetResource(Context context, Request request,
            Response response){
         super(context, request, response)  ;
         getVariants().add(new Variant(MediaType.TEXT_CSS));
             }



    // GET METHOD...............................................................

    @Override
     public synchronized Representation represent(Variant variant) throws ResourceException
    {

        StringBuilder builder = new StringBuilder();

        RepresentationFactory factory = new RepresentationFactory(
                getBaseDirectory()+
                "/Static/a.css");

        try {
            builder = factory.getString();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(IndexResource.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(IndexResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        


         return new StringRepresentation(builder.toString(), MediaType.TEXT_CSS);
     }


    @Override
    public boolean allowPost()
    {
        return false;
    }


    @Override
    public boolean allowPut()
    {
        return false;
    }


    @Override
    public boolean allowDelete()
    {
        return false;
    }


    @Override
    public boolean isModifiable()
    {
        return false;
    }





}
