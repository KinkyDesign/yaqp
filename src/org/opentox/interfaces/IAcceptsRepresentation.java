package org.opentox.interfaces;

import org.restlet.representation.Representation;
import org.restlet.resource.ResourceException;

/**
 *
 * @author chung
 */
public interface IAcceptsRepresentation {

    public Representation post(Representation entity)
            throws ResourceException;

}
