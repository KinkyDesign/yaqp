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

    protected Status internalStatus = Status.SUCCESS_ACCEPTED;

    protected URI targeturi;

    protected URI dataseturi;

    protected Form form;


    
    /**
     * Constructor.
     * @param form The set of posted parameters.
     */
    public AbstractTrainer(final Form form){
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
     * Suggest a status for the Resource calling the trainer. The status can be
     * updated only in the following cases:
     * <ul>
     * <li>The current status is less than 300, i.e. No errors occured up to the current state</li>
     * <li>The current status is 4XX and the new status is also
     * a 4XX or a 5XX status (client or server error).</li>
     * <li>If the current status is a 5XX (eg 500), it will not be updated</li>
     * </ul>
     * @param status The status
     */
    protected void setInternalStatus(Status status){
        if ( ((internalStatus.getCode()>=400)&&(internalStatus.getCode()<500)&&status.getCode()>=400)||
                ((internalStatus.getCode())<300)){
            this.internalStatus=status;
        }
    }


    /**
     * Returns the status that the trainer suggests.
     * @return The internal status of the Trainer.
     */
    public Status getInternalStatus() {
        return internalStatus;
    }


}
