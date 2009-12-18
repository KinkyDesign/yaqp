package org.opentox.Resources.Models;

import org.restlet.data.Form;
import org.restlet.representation.Representation;

/**
 *
 * @author chung
 */
public interface Predictor {

    public abstract Representation predict(Form form, String model_id);

}
