package org.opentox.interfaces;

import org.restlet.representation.Representation;
import org.restlet.representation.Variant;
import org.restlet.resource.ResourceException;

/**
 *
 * @author chung
 */
public interface IProvidesHttpAccess {


    void doInit() throws ResourceException;


    Representation get(Variant variant) throws ResourceException;

}
