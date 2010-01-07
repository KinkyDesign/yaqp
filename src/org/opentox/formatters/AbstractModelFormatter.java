package org.opentox.formatters;

import org.opentox.interfaces.IFormatter;
import org.opentox.error.ErrorSource;
import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.representation.Representation;


/**
 *
 * @author chung
 */
public abstract class AbstractModelFormatter extends ErrorSource implements IFormatter{

    protected int model_id;
    public Status internal_status = Status.SUCCESS_OK;

    public AbstractModelFormatter(int model_id){
        this.model_id = model_id;
    }

    public abstract Representation getRepresentation(MediaType mediatype);

   

}
