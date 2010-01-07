package org.opentox.interfaces;

import org.restlet.data.MediaType;
import org.restlet.representation.Representation;

/**
 *
 * @author chung
 */
public interface IFormatter {

    public abstract Representation getRepresentation(MediaType mediatype);

}
