package org.opentox.algorithm.trainer;

import org.opentox.interfaces.ITrainer;
import java.net.URI;
import org.opentox.error.ErrorSource;
import org.restlet.data.Form;
import org.restlet.resource.ServerResource;


/**
 * Abstract Algorithm Trainer.
 * @author OpenTox - http://www.opentox.org/
 * @author Sopasakis Pantelis
 * @author Sarimveis Harry
 * @version 1.3.3 (Last update: Dec 20, 2009)
 */
public abstract class AbstractTrainer extends ErrorSource
        implements ITrainer{
    

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

       

    protected @interface Regression{ String name() default "";  };

    protected @interface Classification{ String name() default "";  };


    

}
