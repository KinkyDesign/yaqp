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
 * @version 1.1 (Last Update: Aug 26, 2009)
 */
public class ListClassificationAlgorithms extends AbstractResource {

    private static final long serialVersionUID = 10012190006004001L;
    

    public ListClassificationAlgorithms(Context context, Request request, Response response) {
        super(context, request, response);
        getVariants().add(new Variant(MediaType.TEXT_URI_LIST));
    }

    @Override
    public Representation represent(Variant variant) {


        ReferenceList list = new ReferenceList();
        String cls = baseURI+"/algorithm/learning/classification";

        Map<String, Set<String>> map = getAlgorithmIdsAsMap();
        Iterator<String> classificationAlgorithms = map.get("classification").iterator();


        while (classificationAlgorithms.hasNext()){
        list.add(cls+"/"+classificationAlgorithms.next());
        }

        Representation rep = list.getTextRepresentation();
        rep.setMediaType(MediaType.TEXT_URI_LIST);

        return rep;

    }
}
