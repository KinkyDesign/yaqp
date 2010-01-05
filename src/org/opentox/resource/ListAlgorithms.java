package org.opentox.resource;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import org.opentox.algorithm.AlgorithmEnum;
import org.opentox.resource.*;
import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.data.ReferenceList;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.representation.Variant;
import org.restlet.resource.ResourceException;


/**
 * List of all algorithms.
 * <p>
 * URI: /algorithm/<br/>
 * Allowed Methods: GET<br/>
 * Status Codes: 200, 503<br/>
 * Supported Mediatypes: text/uri-list, text/html
 * </p>
 * @author OpenTox - http://www.opentox.org/
 * @author Sopasakis Pantelis
 * @author Sarimveis Harry
 * @version 1.3.3 (Last update: Dec 20, 2009)
 */
public class ListAlgorithms extends AbstractResource {

    private static final long serialVersionUID = 98723648273928734L;
    
    

    /**
     * Initialize the resource.
     * @throws ResourceException
     */
    @Override
    public void doInit() throws ResourceException{
        super.doInit();
        Collection<Variant> variants = new ArrayList<Variant>();
        variants.add(new Variant(MediaType.TEXT_URI_LIST));
        variants.add(new Variant(MediaType.TEXT_HTML));
        getVariants().put(Method.GET, variants);
    }
   
    @Override
    public Representation get(Variant variant) {

    ReferenceList list = new ReferenceList();    
    
    Set<String > algorithms = AlgorithmsSet();
    Iterator<String > algorithmIterator = algorithms.iterator();
    

    while (algorithmIterator.hasNext()){
        list.add(URIs.algorithmURI+"/"+algorithmIterator.next());
    }

    
    
    Representation rep = null;

    if (variant.getMediaType().equals(MediaType.TEXT_URI_LIST)){
        rep = list.getTextRepresentation();
        rep.setMediaType(MediaType.TEXT_URI_LIST);
    }else if (variant.getMediaType().equals(MediaType.TEXT_HTML)){
        rep = list.getWebRepresentation();
        rep.setMediaType(MediaType.TEXT_HTML);
    }
    

    getResponse().setStatus(Status.SUCCESS_OK);

    return rep;


    }
}
