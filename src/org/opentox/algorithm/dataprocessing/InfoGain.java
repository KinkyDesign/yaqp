package org.opentox.algorithm.dataprocessing;

import org.opentox.error.ErrorRepresentation;
import org.opentox.interfaces.IFeatureSelection;
import org.restlet.representation.Representation;

/**
 *
 * @author chung
 */
public class InfoGain implements IFeatureSelection{

    public ErrorRepresentation getErrorRep() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Representation selectFeatures() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public ErrorRepresentation checkParameters() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
