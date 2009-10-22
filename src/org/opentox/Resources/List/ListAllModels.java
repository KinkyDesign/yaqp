package org.opentox.Resources.List;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.opentox.Resources.*;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.data.ReferenceList;
import org.restlet.representation.Representation;
import org.restlet.representation.Variant;
import org.restlet.resource.ResourceException;

/**
 * Lists all classificantion and regression models - returns an xml representation
 * for that list.
 * @author OpenTox Team, http://www.opentox.org/
 * @author Sopasakis Pantelis
 * @author Sarimveis Harry
 * @version 1.1 (Last update: Aug 27, 2009)
 */
public class ListAllModels extends AbstractResource {

    private static final long serialVersionUID = 482012197707002001L;
    /**
     * searchAlgorithm can be any keyword related to algorithms such as
     * classification, regression, learning, svm, svc, mlr, knn. This keyword
     * is not case sensitive, so svm will give the same results with SVM ro Svm.
     */
    private static final String ALGORITHM_TYPE_QUERY = "searchAlgorithm";
    private String searchAlgorithm = "";

    @Override
    public void doInit() throws ResourceException {
        org.opentox.Applications.OpenToxApplication.opentoxLogger.info("ALL MODELS!");
        super.doInit();
        List<Variant> variants = new ArrayList<Variant>();
        variants.add(new Variant(MediaType.TEXT_URI_LIST));
        variants.add(new Variant(MediaType.TEXT_HTML));
        getVariants().put(Method.GET, variants);
        Form queryForm = getReference().getQueryAsForm();
        searchAlgorithm = queryForm.getFirstValue(ALGORITHM_TYPE_QUERY);
    }

    /**
     *
     * @param variant
     * @return XML file containings the ids of all classification and regression models.
     */
    @Override
    public Representation get(Variant variant) {
 
        Representation rep = null;
        ReferenceList list = new ReferenceList();
        System.out.println(searchAlgorithm);
        if (!(searchAlgorithm == null)) {            
                list = org.opentox.Applications.OpenToxApplication.dbcon.getReferenceListFromAlgId(searchAlgorithm);            
        } else {
            list = org.opentox.Applications.OpenToxApplication.dbcon.getModelsAsReferenceList();
        }


        if ((MediaType.TEXT_URI_LIST).equals(variant.getMediaType())) {
            rep = list.getTextRepresentation();
            rep.setMediaType(MediaType.TEXT_URI_LIST);
        } else if ((MediaType.TEXT_HTML).equals(variant.getMediaType())) {
            rep = list.getWebRepresentation();
            rep.setMediaType(MediaType.TEXT_HTML);
        }
        return rep;
    }


}
