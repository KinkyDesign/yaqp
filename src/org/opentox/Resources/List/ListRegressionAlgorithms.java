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
 * List of all regression algorithms
 * <p>
 * URI: /algorithm/learningalgorithm/regression<br/>
 * Allowed Methods: GET<br/>
 * Status Codes: 200, 503<br/>
 * Supported Mediatypes: TEXT_URI_LIST
 * </p>
 * @author OpenTox, http://www.opentox.org
 * @author Sopasakis Pantelis
 * @author Sarimveis Harry
 * @version 1.1 (Last Update: Aug 26, 2009)
 */
public class ListRegressionAlgorithms extends AbstractResource {

    private static final long serialVersionUID = 10012190006005501L;

    public ListRegressionAlgorithms(Context context, Request request, Response response) {
        super(context, request, response);
        getVariants().add(new Variant(MediaType.TEXT_URI_LIST));
    }

    @Override
    public Representation represent(Variant variant) {


        ReferenceList list = new ReferenceList();
        String reg=baseURI+"/algorithm/learning/regression";
        
        Map<String, Set<String>> map = getAlgorithmIdsAsMap();
    
        Iterator<String> regressionAlgorithms = map.get("regression").iterator();

        
        while (regressionAlgorithms.hasNext()){
        list.add(reg+"/"+regressionAlgorithms.next());
        }
        
        Representation rep = list.getTextRepresentation();
        rep.setMediaType(MediaType.TEXT_URI_LIST);

        return rep;

    }
}
