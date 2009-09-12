package org.opentox.Resources.List;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.opentox.Resources.*;
import org.restlet.Context;
import org.restlet.data.MediaType;
import org.restlet.data.ReferenceList;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.resource.Representation;
import org.restlet.resource.Variant;

/**
 * List of all classification algorithms.
 * <p>
 * URI: /algorithm/learningalgorithm/classification<br/>
 * Allowed Methods: GET<br/>
 * Status Codes: 200, 503<br/>
 * Supported Mediatypes: TEXT_URI_LIST
 * </p>
 * @author OpenTox, http://www.opentox.org
 * @author Sopasakis Pantelis
 * @author Sarimveis Harry
 * @version 1.0
 */
public class ListValidationRoutines extends AbstractResource {

    private static final long serialVersionUID = 10012190006004401L;


    public ListValidationRoutines(Context context, Request request, Response response) {
        super(context, request, response);
        getVariants().add(new Variant(MediaType.TEXT_URI_LIST));
    }

    @Override
    public Representation represent(Variant variant) {


        ReferenceList list = new ReferenceList();
        String vld = baseURI+"/validation";

        Map<String, Set<String>> map = getAlgorithmIdsAsMap();
        Iterator<String> validationRoutines = map.get("validation").iterator();


        while (validationRoutines.hasNext()){
            list.add(vld + "/"+validationRoutines.next());
        }
        
        

        Representation rep = list.getTextRepresentation();
        rep.setMediaType(MediaType.TEXT_URI_LIST);

        return rep;

    }
}
