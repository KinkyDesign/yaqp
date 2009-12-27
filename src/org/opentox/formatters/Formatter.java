package org.opentox.formatters;

import org.restlet.data.MediaType;
import org.restlet.representation.Representation;

/**
 *
 * @author chung
 */
public interface Formatter {

    public abstract Representation getRepresentation(MediaType mediatype);

}
