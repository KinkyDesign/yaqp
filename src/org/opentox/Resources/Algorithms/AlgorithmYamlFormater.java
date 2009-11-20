/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.opentox.Resources.Algorithms;

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
public class AlgorithmYamlFormater extends AbstractAlgorithmFormater{

    private static final MediaType mime = OpenToxMediaType.APPLICATION_YAML;


   public AlgorithmYamlFormater(AlgorithmMetaInf metainf){
       super();
       super.metainf = metainf;
   }


    public StringRepresentation getStringRepresentation() {
        StringBuilder builder = new StringBuilder();
        builder.append("---\nAlgorithm:\n");
        builder.append("    name : " + metainf.getTitle() + "\n");
        builder.append("    id : " + metainf.getIdentifier() + "\n");
        builder.append("    AlgorithmType : " + metainf.getAlgorithmType() + "\n");
        builder.append("    Parameters:\n");
        if (metainf.getParameters()[0].length != 3) {
            System.err.println("ERROR!!! Invalid Parameters Element!");
        } else {
            for (int i = 0; i < metainf.getParameters().length; i++) {
                builder.append("        -" + metainf.getParameters()[i][0] + ":\n");
                builder.append("            type:" + metainf.getParameters()[i][1]+"\n");
                builder.append("            defaultValue:" + metainf.getParameters()[i][2]+"\n");
            }
        }
        builder.append("    statisticsSupported:\n");
        if (!metainf.getStatisticsSupported().isEmpty()) {
            for (int i = 0; i < metainf.getStatisticsSupported().size(); i++) {
                builder.append("            -" + metainf.getStatisticsSupported().get(i) + "\n");
            }
        }
        return new StringRepresentation(builder.toString(), mime);
    }

}
