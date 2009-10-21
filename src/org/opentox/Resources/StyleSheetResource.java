package org.opentox.Resources;


import org.opentox.util.RepresentationFactory;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.restlet.Context;
import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.representation.Variant;
import org.restlet.resource.ResourceException;




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

    @Override
    public void doInit() throws ResourceException{
        super.doInit();
         getVariants().put(Method.GET, new Variant(MediaType.TEXT_HTML));
    }



    // GET METHOD...............................................................

    @Override
     public synchronized Representation get(Variant variant) throws ResourceException
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


 
}
