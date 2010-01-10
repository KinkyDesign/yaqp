package org.opentox.interfaces;

import org.opentox.algorithm.AlgorithmEnum;
import org.restlet.data.MediaType;
import org.restlet.representation.Representation;

/**
 *
 * Interface for the production of representations for an algorithm resource
 * with respect to a desired MIME type.
 * @author OpenTox - http://www.opentox.org/
 * @author Sopasakis Pantelis
 * @author Sarimveis Harry
 * @author Kolotouros Dimitris
 * @version 1.3.3 (Last update: Jan 11, 2009)
 */
public interface IAlgorithmReporter extends IProne2Error{
   

    /**
     * Formatted representation of an algorithm given the algorithm and the
     * desired MIME type.
     * @param media Prefered mime.
     * @param algorithm The algorithm for which the representation is needed.
     * @return Representation of algorithm in prefered MIME type.
     */
    public abstract Representation formatedRepresntation(MediaType media,
            AlgorithmEnum algorithm);
}
