/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.opentox.interfaces;

import java.io.OutputStream;
import java.util.Set;
import org.opentox.ontology.meta.ModelMeta;
import weka.core.Instances;

/**
 *
 * @author chung
 */
public interface IModel {

    /**
     * Check the assertion that a certain weka.core.Instances object is
     * compatible with this model in terms of having proper features. In fact
     * the set of attributes of testData must be a hyperset of the dependent
     * attributes of the model. In plain english, the testData set should provide
     * at least the information needed.
     * @param testData
     * @return Returns true if this Model object is compatible with the specified dataset.
     */
    boolean compatibleWith(Instances testData);

    /**
     * Creates the RDF representation for an OpenTox model given its name, the uri
     * of the dataset used to train it, its target feature, the Data and a List of
     * tuning parameters for the training algorithm. The RDF document is built according
     * to the specification of OpenTox API (v 1.1).
     * @param model_id The id of the model (Integer).
     * @param dataseturi The URI of the dataset used to train the model.
     * @param data The Instances object containing the training data.
     * @param algorithmParameters A List of the tuning parameters of the algorithm.
     * @param out The output stream used to store the model.
     */
    void createModel(ModelMeta meta, OutputStream out);

    /**
     * Returns the dependent features of the model.
     * @return dependent feature URI as a String.
     */
    String getDependentFeatureUri();

    /**
     * Get the URI of the predicted feature of the model.
     * @return URI of predicted feature.
     */
    String getPredictedFeatureUri();

    /**
     * The set of independent variables of the model.
     * @return set of URIs
     */
    Set<String> getSetOfIndependentFeatures();

}
