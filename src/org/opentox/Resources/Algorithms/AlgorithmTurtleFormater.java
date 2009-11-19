package org.opentox.Resources.Algorithms;

import java.util.ArrayList;
import org.restlet.data.MediaType;
import org.restlet.representation.StringRepresentation;

/**
 *
 * @author OpenTox - http://www.opentox.org
 * @author Sopasakis Pantelis
 * @author Sarimveis Harry
 */
public class AlgorithmTurtleFormater extends AbstractAlgorithmFormater{

    private String 
            description,
            subject,
            type,
            source,
            relation,
            creator,
            publisher,
            contributor,
            rights,
            date,
            format,            
            language,
            audience,
            provenance,
            about;

    @Override
    public StringRepresentation getStringRepresentation() {
        StringBuilder builder = new StringBuilder();
        return new StringRepresentation(builder.toString(), MediaType.APPLICATION_RDF_TURTLE);
    }
    
}
