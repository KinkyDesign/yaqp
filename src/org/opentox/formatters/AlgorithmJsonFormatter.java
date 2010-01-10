package org.opentox.formatters;

import org.opentox.ontology.meta.AlgorithmMeta;
import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;

/**
 *
 * @author OpenTox - http://www.opentox.org
 * @author Sopasakis Pantelis
 * @author Sarimveis Harry
 */
public class AlgorithmJsonFormatter extends AbstractAlgorithmFormatter {

    private static final MediaType mime = MediaType.APPLICATION_JSON;


    public AlgorithmJsonFormatter(AlgorithmMeta metainf) {
        super();
        super.metainf = metainf;
    }

    
    @Override
    public Representation getRepresentation(MediaType mediatype) {
        StringBuilder builder = new StringBuilder();
        builder.append("\"Algorithm\": \n{\n");
        builder.append("\"name\" : \""+metainf.title+"\",\n");
        builder.append("\"id\" : \""+metainf.identifier+"\",\n");
        builder.append("\"AlgorithmType\" : \""+metainf.algorithmType.getURI()+"\",\n");
        builder.append("\"Parameters\" : {\n");
        for (int i=0;i<metainf.Parameters.size();i++){
            builder.append("\""+metainf.Parameters.get(i).paramName+"\" : { \"type\" : \""+
                    metainf.Parameters.get(i).dataType.getURI()+
                    "\" , \"defaultValue\"  :\""+metainf.Parameters.get(i).paramValue.toString()+" \" } \n");
        }
        builder.append("}\n");
        
        builder.append("}\n");
        return new StringRepresentation(builder.toString(), mime);
    }
}
