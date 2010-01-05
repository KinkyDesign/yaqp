package org.opentox.ontology.meta;

import java.util.List;
import org.opentox.algorithm.AlgorithmParameter;
import weka.core.Instances;

/**
 * Meta information about a Model.
 * @author Sopasakis Pantelis
 */
public class ModelMeta {

    public ModelMeta(String model_id, String dataseturi, Instances data, List<AlgorithmParameter> algorithmParameters, String AlgorithmURI){
        this.model_id = model_id;
        this.dataseturi = dataseturi;
        this.algorithmParameters = algorithmParameters;
        this.data = data;
        this.AlgorithmURI = AlgorithmURI;
    }

    /**
     * Identification number of the model.
     */
    public String model_id;
    /**
     * URI of the dataset used to train the model.
     */
    public String dataseturi;
    /**
     * Instances object corresponding to the URI of the dataset.
     */
    public Instances data;
    /**
     * List of parameters of the algorithm used to train the model.
     */
    public List<AlgorithmParameter> algorithmParameters;
    /**
     * URI of the algorithm used to train the model.
     */
    public String AlgorithmURI;

}
