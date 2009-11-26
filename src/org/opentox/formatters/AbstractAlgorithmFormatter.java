package org.opentox.formatters;

import org.opentox.Resources.Algorithms.*;
import org.restlet.representation.StringRepresentation;

/**
 *
 * @author OpenTox - http://www.opentox.org
 * @author Sopasakis Pantelis
 * @author Sarimveis Harry
 */
public abstract class AbstractAlgorithmFormatter {

    public AlgorithmMetaInf metainf;

    /**
     * Constructor of the Abstract Formater.
     */
    public AbstractAlgorithmFormatter() {
    }

    /**
     * Returns the Corresponding Representation.
     * @return
     */
    public abstract StringRepresentation getStringRepresentation();
}
