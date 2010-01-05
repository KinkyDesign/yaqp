package org.opentox.formatters;

import org.opentox.ontology.meta.AlgorithmMeta;
import org.opentox.error.ErrorSource;
import org.restlet.data.MediaType;
import org.restlet.representation.Representation;

/**
 *
 * @author OpenTox - http://www.opentox.org
 * @author Sopasakis Pantelis
 * @author Sarimveis Harry
 */
public abstract class AbstractAlgorithmFormatter extends ErrorSource implements Formatter{

    public AlgorithmMeta metainf;

    /**
     * Constructor of the Abstract Formater.
     */
    public AbstractAlgorithmFormatter() {
    }

    /**
     * Returns the Corresponding Representation.
     * @return representation in proper media type.
     */
    public abstract Representation getRepresentation(MediaType mediatype);
    
}
