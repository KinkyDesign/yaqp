package org.opentox.resource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.opentox.resource.*;
import org.opentox.database.ModelsTable;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.data.ReferenceList;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.representation.Variant;
import org.restlet.resource.ResourceException;

/**
 * Lists all classificantion and regression models - returns a text/uri-list
 * representation for that list.
 * @author OpenTox - http://www.opentox.org/
 * @author Sopasakis Pantelis
 * @author Sarimveis Harry
 * @version 1.3.3 (Last update: Dec 20, 2009)
 */
public class ListModels extends AbstractResource {

    private static final long serialVersionUID = 203859831723987321L;
    /**
     * searchAlgorithm can be any keyword related to algorithms such as
     * classification, regression, learning, svm, svc, mlr, knn. This keyword
     * is not case sensitive, so svm will give the same results with SVM ro Svm.
     */
    private static final String ALGORITHM_TYPE_QUERY = "searchAlgorithm";
    /**
     * Database Query
     */
    private String searchAlgorithm = "";

    /**
     * Initializes the resource.
     * @throws ResourceException
     */
    @Override
    public void doInit() throws ResourceException {
        super.doInit();
        Collection<Variant> variants = new ArrayList<Variant>();
        variants.add(new Variant(MediaType.TEXT_URI_LIST));
        variants.add(new Variant(MediaType.TEXT_HTML));
        getVariants().put(Method.GET, variants);
        Form queryForm = getReference().getQueryAsForm();
        searchAlgorithm = queryForm.getFirstValue(ALGORITHM_TYPE_QUERY);
    }

    /**
     * Return a text/uri-list or text/html representation of the list of
     * available models.
     * @param variant
     * @return Representation of list of models.
     */
    @Override
    public Representation get(Variant variant) {

        Representation rep = null;
        ReferenceList list = new ReferenceList();
        if (!(searchAlgorithm == null)) {
            list = ModelsTable.INSTANCE.getReferenceListFromAlgId(searchAlgorithm);
        } else {
            list = ModelsTable.INSTANCE.getModelsAsReferenceList();
        }


        if ((MediaType.TEXT_URI_LIST).equals(variant.getMediaType())) {

            if (list.size() > 0) {
                getResponse().setStatus(Status.SUCCESS_OK);
                rep = list.getTextRepresentation();
                rep.setMediaType(MediaType.TEXT_URI_LIST);
            } else {
                getResponse().setStatus(Status.SUCCESS_NO_CONTENT);
            }

        } else if ((MediaType.TEXT_HTML).equals(variant.getMediaType())) {
            if (list.size() > 0) {
                rep = list.getWebRepresentation();
                getResponse().setStatus(Status.SUCCESS_OK);
                rep.setMediaType(MediaType.TEXT_HTML);
            } else {
                getResponse().setStatus(Status.SUCCESS_NO_CONTENT);
            }
        }        
        return rep;
    }
}
