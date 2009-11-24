package org.opentox.Resources.Algorithms;

import org.restlet.data.MediaType;
import org.restlet.representation.StringRepresentation;

/**
 *
 * @author OpenTox - http://www.opentox.org
 * @author Sopasakis Pantelis
 * @author Kolotouros Dimitris
 * @author Sarimveis Harry
 */
public abstract class AbstractAlgorithmReporter {

    public abstract StringRepresentation FormatedRepresntation(MediaType media,
            AlgorithmEnum algorithm);
}
