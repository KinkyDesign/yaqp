package org.opentox.formatters;

import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.representation.StringRepresentation;

/**
 *
 * @author chung
 */
public abstract class AbstractModelFormatter {

    protected int model_id;
    public Status internal_status = Status.SUCCESS_OK;

    public AbstractModelFormatter(int model_id){
        this.model_id = model_id;
    }

    public abstract StringRepresentation getStringRepresentation(MediaType mediatype);

   

}
