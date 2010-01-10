package org.opentox.interfaces;

import org.opentox.error.ErrorRepresentation;
import org.restlet.representation.Representation;

/**
 *
 * @author chung
 */
public interface IFeatureSelection extends IProne2Error {

    /**
     * ???
     * @return ?
     */
    public abstract Representation selectFeatures();

    /**
     * Check the consistency of the posted parameters.
     * @return A null Representation if the posted parameters are consistent,
     * or an instance of ErrorRepresentation otherwise, containing a list of
     * Trhowables and explanatory messages for each one.
     */
    public abstract ErrorRepresentation checkParameters();

}
