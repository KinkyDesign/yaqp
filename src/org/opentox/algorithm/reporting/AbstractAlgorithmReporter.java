package org.opentox.algorithm.reporting;

import org.opentox.algorithm.AlgorithmEnum;
import org.opentox.error.ErrorSource;
import org.restlet.data.MediaType;
import org.restlet.representation.Representation;

/**
 *
 * @author OpenTox - http://www.opentox.org/
 * @author Sopasakis Pantelis
 * @author Sarimveis Harry
 * @author Kolotouros Dimitris
 * @version 1.3.3 (Last update: Dec 20, 2009)
 */
public abstract class AbstractAlgorithmReporter extends ErrorSource{

    public AbstractAlgorithmReporter(){
        
    }

    public abstract Representation FormatedRepresntation(MediaType media,
            AlgorithmEnum algorithm);
}
