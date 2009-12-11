package org.opentox.Resources;

import java.util.Collection;
import java.util.Iterator;
import org.opentox.util.RepresentationFactory;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
public class IndexResource extends AbstractResource {

    private static final long serialVersionUID = 10012190003302L;
    private static int views = 0;

    /**
     * Initializes the Resource
     * @throws ResourceException
     */
    @Override
    public void doInit() throws ResourceException {
        super.doInit();
        List<Variant> variants = new ArrayList<Variant>();
        variants.add(new Variant(MediaType.TEXT_PLAIN));
        variants.add(new Variant(MediaType.TEXT_HTML));
        getVariants().put(Method.GET, variants);
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
    public synchronized Representation get(Variant variant) throws ResourceException {

    if (MediaType.TEXT_PLAIN.equals(variant.getMediaType())){
        String textMessage = "OpenTox Restful Web Services\n" +
                "There is nothing to see here. Visit our home page at http://opentox.ntua.gr\n" +
                "Contact us: chvng [ a t ] mail (do t) ntua (d ot) gr" +
                "\n";
        return new StringRepresentation(textMessage, variant.getMediaType());
    }else if (MediaType.TEXT_HTML.equals(variant.getMediaType())){
        String htmlMessage = "<html><body>" +
                "<h2>OpenTox Restful Web Services</h2>" +
                "<p>There is nothing to see here. Please follow <a href=\"http://opentox.ntua.gr\">this link</a> to " +
                "go to our home page. </p>" +
                "<p>Contact us: chvng <b> a t </b> mail <b>do t</b> ntua <b>d o t</b> gr</p>" +
                "</body></html>";
        return new StringRepresentation(htmlMessage, variant.getMediaType());
    }else{
        return null;
    }
        



    }
}
