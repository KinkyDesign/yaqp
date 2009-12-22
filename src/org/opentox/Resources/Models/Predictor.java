package org.opentox.Resources.Models;

import org.restlet.data.Form;
import org.restlet.representation.Representation;

/**
 * This interface is used by all predictors such as MlrPredictor.
 * @author OpenTox - http://www.opentox.org/
 * @author Sopasakis Pantelis
 * @author Sarimveis Harry
 * @version 1.3.3 (Last update: Dec 20, 2009)
 */
public interface Predictor {

    /**
     * Given a dataset uri and a model id, calculate the set of predicted values for
     * the corresponding feature.
     * @param form
     * @param model_id
     * @return
     */
    public abstract Representation predict(Form form, String model_id);

}
