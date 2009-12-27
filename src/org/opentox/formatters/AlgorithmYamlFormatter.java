
package org.opentox.formatters;

import org.opentox.algorithm.AlgorithmMetaInf;
import org.opentox.media.OpenToxMediaType;
import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;

/**
 *
 * @author OpenTox - http://www.opentox.org
 * @author Sopasakis Pantelis
 * @author Sarimveis Harry
 */
public class AlgorithmYamlFormatter extends AbstractAlgorithmFormatter{

    private static final MediaType mime = OpenToxMediaType.APPLICATION_YAML;


   public AlgorithmYamlFormatter(AlgorithmMetaInf metainf){
       super();
       super.metainf = metainf;
   }


    public Representation getRepresentation(MediaType mediatype) {
        StringBuilder builder = new StringBuilder();
        builder.append("---\nAlgorithm:\n");
        builder.append("    name : " + metainf.title + "\n");
        builder.append("    id : " + metainf.identifier + "\n");
        builder.append("    AlgorithmType : " + metainf.algorithmType + "\n");
        builder.append("    Parameters:\n");
        
            for (int i = 0; i < metainf.Parameters.size(); i++) {
                builder.append("        -" + metainf.Parameters.get(i).paramName + ":\n");
                builder.append("            type:" + metainf.Parameters.get(i).dataType.toString()+"\n");
                builder.append("            defaultValue:" + metainf.Parameters.get(i).paramValue.toString()+"\n");
            }
        
        builder.append("    statisticsSupported:\n");
        if (!metainf.statisticsSupported.isEmpty()) {
            for (int i = 0; i < metainf.statisticsSupported.size(); i++) {
                builder.append("            -" + metainf.statisticsSupported.get(i) + "\n");
            }
        }
        return new StringRepresentation(builder.toString(), mime);
    }

}
