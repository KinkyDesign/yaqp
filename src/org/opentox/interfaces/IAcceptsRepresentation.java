package org.opentox.interfaces;

import org.restlet.representation.Representation;
import org.restlet.resource.ResourceException;

/**
 * A Resource that accepts a representation through the POST HTTP method.
 * @author Sopasakis Pantelis
 */
public interface IAcceptsRepresentation {


    /**
     * Implementation of the POST method for the specific resource.
     * @param entity The posted entity.
     * @return The optinal represnetation of the resource following the POST.
     * @throws ResourceException
     */
    public Representation post(Representation entity)
            throws ResourceException;

}
