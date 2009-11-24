/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.opentox.formatters;

import org.opentox.Resources.Algorithms.*;
import org.opentox.formatters.AbstractAlgorithmFormatter;
import java.util.ArrayList;
import org.opentox.MediaTypes.OpenToxMediaType;
import org.restlet.data.MediaType;
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


    public StringRepresentation getStringRepresentation() {
        StringBuilder builder = new StringBuilder();
        builder.append("---\nAlgorithm:\n");
        builder.append("    name : " + metainf.title + "\n");
        builder.append("    id : " + metainf.identifier + "\n");
        builder.append("    AlgorithmType : " + metainf.algorithmType + "\n");
        builder.append("    Parameters:\n");
        if (metainf.Parameters[0].length != 3) {
            System.err.println("ERROR!!! Invalid Parameters Element!");
        } else {
            for (int i = 0; i < metainf.Parameters.length; i++) {
                builder.append("        -" + metainf.Parameters[i][0] + ":\n");
                builder.append("            type:" + metainf.Parameters[i][1]+"\n");
                builder.append("            defaultValue:" + metainf.Parameters[i][2]+"\n");
            }
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
