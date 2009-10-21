package org.opentox.Resources;


import java.util.Collection;
import java.util.Iterator;
import org.opentox.util.RepresentationFactory;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.restlet.Context;
import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.data.ServerInfo;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.representation.Variant;
import org.restlet.resource.ResourceException;




/**
 * REST Web Service
 *
 * @author OpenTox - http://www.opentox.org
 * @author Sopasakis Pantelis
 * @author Sarimveis Harry
 * @version 1.0 (Last Updated Aug 20, 2009)
 */
public class IndexResource extends AbstractResource
{

    private static final long serialVersionUID = 10012190003302L;

    private static int views=0;

    /**
     * Initializes the Resource
     * @throws ResourceException
     */
    @Override
    public void doInit() throws ResourceException{
        super.doInit();
        getVariants().put(Method.GET, new Variant(MediaType.TEXT_HTML));
    }

//    public IndexResource(Context context, Request request,
//            Response response){
//         super(context, request, response)  ;
//         //getVariants().add(new Variant(MediaType.TEXT_PLAIN));
//         getVariants().add(new Variant(MediaType.TEXT_HTML));
//         views++;
//    }



    // GET METHOD...............................................................

    @Override
     public synchronized Representation get(Variant variant) throws ResourceException
    {

        StringBuilder builder = new StringBuilder();

        RepresentationFactory factory = new RepresentationFactory(
                getBaseDirectory()+
                "/Static/index.html");

        try {
            builder = factory.getString();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(IndexResource.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(IndexResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        builder.append("<br/><br/><div id=\"pageviews\">You are the visitor No."+views+"</div>");

         return new StringRepresentation(builder.toString(), MediaType.TEXT_HTML);
     }




}
