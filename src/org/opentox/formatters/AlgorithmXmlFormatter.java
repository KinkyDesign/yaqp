package org.opentox.formatters;


import org.opentox.Resources.Algorithms.*;
import org.opentox.formatters.AbstractAlgorithmFormatter;
import org.opentox.Resources.AbstractResource;
import org.restlet.data.MediaType;
import org.restlet.representation.StringRepresentation;

/**
 *
 * @author OpenTox - http://www.opentox.org
 * @author Sopasakis Pantelis
 * @author Sarimveis Harry
 */
public class AlgorithmXmlFormatter extends AbstractAlgorithmFormatter{

    private static final MediaType mime = MediaType.TEXT_XML;

    public AlgorithmXmlFormatter(AlgorithmMetaInf metainf){
        super.metainf = metainf;
    }

    public StringRepresentation getStringRepresentation() {
        StringBuilder builder = new StringBuilder();
        builder.append(AbstractResource.xmlIntro);
        builder.append("<algorithm name=\"" + metainf.title + "\" id=\"" + metainf.identifier + "\">\n");
        builder.append("<algorithmType>" + metainf.algorithmType + "</algorithmType>\n");

        builder.append("<Parameters>\n");
        if (metainf.Parameters[0].length != 4) {
            System.err.println("ERROR!!! Invalid Parameters Element!");
        } else {
            for (int i = 0; i < metainf.Parameters.length; i++) {
                builder.append("<param type=\"" + metainf.Parameters[i][1] + "\" " +
                        "defaultvalue=\"" + metainf.Parameters[i][2] + "\">" +
                        metainf.Parameters[i][0] + "</param>\n");
            }
        }
        builder.append("</Parameters>\n");

        if (metainf.statisticsSupported.isEmpty()) {
            builder.append("<statisticsSupported/>\n");
        } else {
            builder.append("<statisticsSupported>\n");
            for (int i = 0; i < metainf.statisticsSupported.size(); i++) {
                builder.append("<statistic>" + metainf.statisticsSupported.get(i) + "</statistic>\n");
            }
            builder.append("</statisticsSupported>\n");
        }
        builder.append("</algorithm>\n");
        return new StringRepresentation(builder.toString(), mime);
    }
}
