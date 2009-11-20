package org.opentox.Resources.Algorithms;

import org.restlet.data.MediaType;
import org.restlet.representation.StringRepresentation;

/**
 *
 * @author OpenTox - http://www.opentox.org
 * @author Sopasakis Pantelis
 * @author Sarimveis Harry
 */
public class AlgorithmJsonFormater extends AbstractAlgorithmFormater {

    private static final MediaType mime = MediaType.APPLICATION_JSON;


    public AlgorithmJsonFormater(AlgorithmMetaInf metainf) {
        super();
        super.metainf = metainf;
    }

    
    @Override
    public StringRepresentation getStringRepresentation() {
        StringBuilder builder = new StringBuilder();
        builder.append("\"Algorithm\": \n{\n");
        builder.append("\"name\" : \""+metainf.getTitle()+"\",\n");
        builder.append("\"id\" : \""+metainf.getIdentifier()+"\",\n");
        builder.append("\"AlgorithmType\" : \""+metainf.getAlgorithmType()+"\",\n");
        builder.append("\"Parameters\" : {\n");
        for (int i=0;i<metainf.getParameters().length;i++){
            builder.append("\""+metainf.getParameters()[i][0]+"\" : { \"type\" : \""+
                    metainf.getParameters()[i][1]+
                    "\" , \"defaultValue\"  :\""+metainf.getParameters()[i][2]+"\"\n");
        }
        builder.append("},\n");
        builder.append("\"statisticsSupported\" : {\n");
        for (int i=0;i<metainf.getStatisticsSupported().size();i++){
            builder.append("\"statistic\" : \""+metainf.getStatisticsSupported().get(i)+"\",\n");
        }        
        builder.append("}\n");

        return new StringRepresentation(builder.toString(), mime);
    }
}
