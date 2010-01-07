package org.opentox.resource;

import org.opentox.interfaces.IProvidesHttpAccess;
import org.restlet.representation.Representation;
import org.restlet.representation.Variant;
import org.restlet.resource.ResourceException;

/**
 *
 * @author chung
 */
public class ListTasks extends OTResource
        implements IProvidesHttpAccess {

    @Override
    public void doInit() throws ResourceException{
        super.doInit();
    }

    
    @Override
    public  Representation get(Variant variant) throws ResourceException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
