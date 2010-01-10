package org.opentox.interfaces;

import org.opentox.error.ErrorRepresentation;
import org.restlet.representation.Representation;

/**
 *
 * @author chung
 */
public interface ITrainer extends IProne2Error {

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

}
