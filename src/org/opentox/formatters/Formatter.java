package org.opentox.formatters;

import org.restlet.data.MediaType;
import org.restlet.representation.StringRepresentation;

/**
 *
 * @author chung
 */
public interface Formatter {

    public abstract StringRepresentation getStringRepresentation(MediaType mediatype);

}
