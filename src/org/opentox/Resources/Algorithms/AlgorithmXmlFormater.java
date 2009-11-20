package org.opentox.Resources.Algorithms;


import org.opentox.Resources.AbstractResource;
import org.restlet.data.MediaType;
import org.restlet.representation.StringRepresentation;

/**
 *
 * @author OpenTox - http://www.opentox.org
 * @author Sopasakis Pantelis
 * @author Sarimveis Harry
 */
public class AlgorithmXmlFormater extends AbstractAlgorithmFormater{

    private static final MediaType mime = MediaType.TEXT_XML;

    public AlgorithmXmlFormater(AlgorithmMetaInf metainf){
        super.metainf = metainf;
    }

    public StringRepresentation getStringRepresentation() {
        StringBuilder builder = new StringBuilder();
        builder.append(AbstractResource.xmlIntro);
        builder.append("<algorithm name=\"" + metainf.getTitle() + "\" id=\"" + metainf.getIdentifier() + "\">\n");
        builder.append("<algorithmType>" + metainf.getAlgorithmType() + "</algorithmType>\n");

        builder.append("<Parameters>\n");
        if (metainf.getParameters()[0].length != 3) {
            System.err.println("ERROR!!! Invalid Parameters Element!");
        } else {
            for (int i = 0; i < metainf.getParameters().length; i++) {
                builder.append("<param type=\"" + metainf.getParameters()[i][1] + "\" " +
                        "defaultvalue=\"" + metainf.getParameters()[i][2] + "\">" +
                        metainf.getParameters()[i][0] + "</param>\n");
            }
        }
        builder.append("</Parameters>\n");

        if (metainf.getStatisticsSupported().isEmpty()) {
            builder.append("<statisticsSupported/>\n");
        } else {
            builder.append("<statisticsSupported>\n");
            for (int i = 0; i < metainf.getStatisticsSupported().size(); i++) {
                builder.append("<statistic>" + metainf.getStatisticsSupported().get(i) + "</statistic>\n");
            }
            builder.append("</statisticsSupported>\n");
        }
        builder.append("</algorithm>\n");
        return new StringRepresentation(builder.toString(), mime);
    }
}
