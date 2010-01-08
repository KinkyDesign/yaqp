package org.opentox.interfaces;

import org.opentox.algorithm.AlgorithmEnum;
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
public interface IAlgorithmReporter extends IProne2Error{
   

    public abstract Representation formatedRepresntation(MediaType media,
            AlgorithmEnum algorithm);
}
