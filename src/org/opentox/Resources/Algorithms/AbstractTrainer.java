package org.opentox.Resources.Algorithms;

import java.net.URI;
import org.restlet.data.Form;
import org.restlet.data.Status;
import org.restlet.representation.Representation;


/**
 *
 * @author OpenTox - http://www.opentox.org
 * @author Sopasakis Pantelis
 * @author Sarimveis Harry
 */
public abstract class AbstractTrainer {

    protected Status internalStatus;

    protected URI targeturi;

    protected URI dataseturi;

    protected Form form;


    public AbstractTrainer(){
        
    }

    /**
     * Constructor.
     * @param form The set of posted parameters.
     */
    public AbstractTrainer(Form form){
        this.form=form;
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
    public abstract Representation checkParameters();


    /**
     * Suggest a status for the Resource calling the trainer.
     * @param status The status
     */
    protected void setInternalStatus(Status status){
        this.internalStatus = status;
    }


    /**
     * Returns the status that the trainer suggests.
     * @return The internal status of the Trainer.
     */
    public Status getInternalStatus() {
        return internalStatus;
    }


}
