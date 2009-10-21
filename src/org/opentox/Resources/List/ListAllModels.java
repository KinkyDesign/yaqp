package org.opentox.Resources.List;

import org.opentox.Resources.*;
import java.io.File;

import org.restlet.Context;
import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.data.ReferenceList;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
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
     * Initialize the resource.
     * @throws ResourceException
     */
    @Override
    public void doInit() throws ResourceException {
        super.doInit();
        getVariants().put(Method.GET, new Variant(MediaType.TEXT_URI_LIST));
        getVariants().put(Method.GET, new Variant(MediaType.TEXT_HTML));
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
        list = org.opentox.Applications.OpenToxApplication.dbcon.getModelsAsReferenceList();

        if (MediaType.TEXT_URI_LIST.equals(variant.getMediaType())) {
            rep = list.getTextRepresentation();
            rep.setMediaType(MediaType.TEXT_URI_LIST);
        } else if (MediaType.TEXT_HTML.equals(variant.getMediaType())) {
            rep = list.getWebRepresentation();
            rep.setMediaType(MediaType.TEXT_HTML);
        }
        return rep;
    }
}
