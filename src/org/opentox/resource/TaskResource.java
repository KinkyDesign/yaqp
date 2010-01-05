package org.opentox.resource;

import java.util.ArrayList;
import java.util.List;
import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.representation.Representation;
import org.restlet.representation.Variant;
import org.restlet.resource.ResourceException;

/**
 *
 * @author chung
 */
public class TaskResource extends AbstractResource {

    @Override
    public void doInit() throws ResourceException {
        List<Variant> variants = new ArrayList<Variant>();
        variants.add(new Variant(MediaType.TEXT_PLAIN));
        getVariants().put(Method.GET, variants);

    }

    /**
     * TODO: Return a representation of the task - use org.opentox.rdf.Task.
     * @param variant
     * @return
     * @throws ResourceException
     */
    @Override
    protected Representation get(Variant variant) throws ResourceException {
        throw new UnsupportedOperationException("Not supported yet!");
    }
}
