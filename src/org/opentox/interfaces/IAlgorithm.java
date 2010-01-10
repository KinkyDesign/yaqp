package org.opentox.interfaces;

import org.opentox.ontology.meta.AlgorithmMeta;

/**
 *
 * @author chung
 */
public interface IAlgorithm {

    /**
     * Metainformation about the algorithm include its parameters,
     * title, identifier and other data.
     * @return
     */
    AlgorithmMeta getMeta();
    

}
