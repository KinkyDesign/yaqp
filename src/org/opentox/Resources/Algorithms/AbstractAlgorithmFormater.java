package org.opentox.Resources.Algorithms;

import java.util.ArrayList;
import org.restlet.representation.StringRepresentation;

/**
 *
 * @author OpenTox - http://www.opentox.org
 * @author Sopasakis Pantelis
 * @author Sarimveis Harry
 */
public abstract class AbstractAlgorithmFormater{

    
    public AlgorithmMetaInf metainf;

    protected String
            rdfNamespace = "http://www.w3.org/1999/02/22-rdf-syntax-ns#",
            dcNamespace="http://purl.org/dc/elements/1.1/",
            opentoxNamespace="http://opentox.org/api-1.1/Algorithm";

    /**
     * Constructor of the Abstract Formater.
     */
    public AbstractAlgorithmFormater() {}

    public abstract StringRepresentation getStringRepresentation();
    
}
