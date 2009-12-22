package org.opentox.Resources.Algorithms;

import org.opentox.Resources.ErrorSource;
import org.restlet.data.MediaType;
import org.restlet.representation.StringRepresentation;

/**
 *
 * @author OpenTox - http://www.opentox.org/
 * @author Sopasakis Pantelis
 * @author Sarimveis Harry
 * @author Kolotouros Dimitris
 * @version 1.3.3 (Last update: Dec 20, 2009)
 */
public abstract class AbstractAlgorithmReporter {

    public abstract StringRepresentation FormatedRepresntation(MediaType media,
            AlgorithmEnum algorithm);
}
