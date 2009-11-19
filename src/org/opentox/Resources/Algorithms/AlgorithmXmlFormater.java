package org.opentox.Resources.Algorithms;


import org.opentox.Resources.AbstractResource;

/**
 *
 * @author OpenTox - http://www.opentox.org
 * @author Sopasakis Pantelis
 * @author Sarimveis Harry
 */
public class AlgorithmXmlFormater extends AbstractAlgorithmFormater{

    public String getXml() {
        StringBuilder builder = new StringBuilder();
        builder.append(AbstractResource.xmlIntro);
        builder.append("<algorithm name=\"" + title + "\" id=\"" + identifier + "\">\n");
        builder.append("<algorithmType>" + algorithmType + "</algorithmType>\n");

        builder.append("<Parameters>\n");
        if (Parameters[0].length != 3) {
            System.err.println("ERROR!!! Invalid Parameters Element!");
        } else {
            for (int i = 0; i < Parameters.length; i++) {
                builder.append("<param type=\"" + Parameters[i][1] + "\" " +
                        "defaultvalue=\"" + Parameters[i][2] + "\">" +
                        Parameters[i][0] + "</param>\n");
            }
        }
        builder.append("</Parameters>\n");

        if (statisticsSupported.isEmpty()) {
            builder.append("<statisticsSupported/>\n");
        } else {
            builder.append("<statisticsSupported>\n");
            for (int i = 0; i < statisticsSupported.size(); i++) {
                builder.append("<statistic>" + statisticsSupported.get(i) + "</statistic>\n");
            }
            builder.append("</statisticsSupported>\n");
        }
        builder.append("</algorithm>\n");
        return builder.toString();
    }
}
