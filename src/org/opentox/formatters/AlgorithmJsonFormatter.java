package org.opentox.formatters;

import org.opentox.Resources.Algorithms.*;
import org.restlet.data.MediaType;
import org.restlet.representation.StringRepresentation;

/**
 *
 * @author OpenTox - http://www.opentox.org
 * @author Sopasakis Pantelis
 * @author Sarimveis Harry
 */
public class AlgorithmJsonFormatter extends AbstractAlgorithmFormatter {

    private static final MediaType mime = MediaType.APPLICATION_JSON;


    public AlgorithmJsonFormatter(AlgorithmMetaInf metainf) {
        super();
        super.metainf = metainf;
    }

    
    @Override
    public StringRepresentation getStringRepresentation() {
        StringBuilder builder = new StringBuilder();
        builder.append("\"Algorithm\": \n{\n");
        builder.append("\"name\" : \""+metainf.title+"\",\n");
        builder.append("\"id\" : \""+metainf.identifier+"\",\n");
        builder.append("\"AlgorithmType\" : \""+metainf.algorithmType+"\",\n");
        builder.append("\"Parameters\" : {\n");
        for (int i=0;i<metainf.Parameters.size();i++){
            builder.append("\""+metainf.Parameters.get(i).paramName+"\" : { \"type\" : \""+
                    metainf.Parameters.get(i).dataType.toString()+
                    "\" , \"defaultValue\"  :\""+metainf.Parameters.get(i).paramValue.toString()+"\"\n");
        }
        builder.append("},\n");
        builder.append("\"statisticsSupported\" : {\n");
        for (int i=0;i<metainf.statisticsSupported.size();i++){
            builder.append("\"statistic\" : \""+metainf.statisticsSupported.get(i)+"\",\n");
        }        
        builder.append("}\n");
        return new StringRepresentation(builder.toString(), mime);
    }
}
