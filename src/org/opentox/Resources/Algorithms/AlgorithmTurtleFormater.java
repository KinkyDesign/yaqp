package org.opentox.Resources.Algorithms;


import org.restlet.data.MediaType;
import org.restlet.representation.StringRepresentation;

/**
 *
 * @author OpenTox - http://www.opentox.org
 * @author Sopasakis Pantelis
 * @author Sarimveis Harry
 */
public class AlgorithmTurtleFormater extends AbstractAlgorithmFormater{

    public AlgorithmTurtleFormater(AlgorithmMetaInf metainf){
        super.metainf = metainf;
    }
    
    @Override
    public StringRepresentation getStringRepresentation() {
        StringBuilder builder = new StringBuilder();
        return new StringRepresentation(builder.toString(), MediaType.APPLICATION_RDF_TURTLE);
    }
    
}
