package org.opentox.algorithm.trainer;

import java.net.URI;
import org.opentox.error.ErrorRepresentation;
import org.opentox.error.ErrorSource;
import org.restlet.data.Form;
import org.restlet.representation.Representation;
import org.restlet.resource.ServerResource;


/**
 * Abstract Algorithm Trainer.
 * @author OpenTox - http://www.opentox.org/
 * @author Sopasakis Pantelis
 * @author Sarimveis Harry
 * @version 1.3.3 (Last update: Dec 20, 2009)
 */
public abstract class AbstractTrainer extends ErrorSource{
    

    protected URI targeturi;

    protected URI dataseturi;

    protected Form form;

    protected ServerResource resource = null;


    
    /**
     * Constructor.
     * @param form The set of posted parameters.
     * @param resource The resource that calls the trainer.
     */
    public AbstractTrainer(final Form form, final ServerResource resource){
        this.form = form;
        this.resource = resource;
    }

    /**
     * Applies a Learning Algorithm to train a new Model. The general process
     * consists of the following steps:
     * <ul>
     * <li>A new model id is selected using the database {@link org.opentox.database.InHouseDB}</li>
     * <li>The posted parameters are checked for consistency.</li>
     * <li>The algorithm is applied to the data and a new model is generated</li>
     * <li>The model is stored as a file on the server</li>
     * <li>The database is now updated and the URI of the created model is returned to the client
     * in text/plain format</li>
     * </ul>
     * @return Representation of the model URI.
     */
    public abstract Representation train();


    /**
     * Check the consistency of the posted parameters.
     * @return A null Representation if the posted parameters are consistent,
     * or an Error Message otherwise.
     */
    public abstract ErrorRepresentation checkParameters();

    protected @interface Regression{ String name() default "";  };

    protected @interface Classification{ String name() default "";  };


    

}