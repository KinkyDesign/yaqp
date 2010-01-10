package org.opentox.formatters;


import org.opentox.ontology.meta.AlgorithmMeta;
import org.opentox.resource.OTResource;
import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;

/**
 *
 * @author OpenTox - http://www.opentox.org
 * @author Sopasakis Pantelis
 * @author Sarimveis Harry
 */
public class AlgorithmXmlFormatter extends AbstractAlgorithmFormatter{

    private static final MediaType mime = MediaType.TEXT_XML;

    public AlgorithmXmlFormatter(AlgorithmMeta metainf){
        super.metainf = metainf;
    }

    public Representation getRepresentation(MediaType mediatype) {
        StringBuilder builder = new StringBuilder();
        builder.append(OTResource.xmlIntro);
        builder.append("<algorithm name=\"" + metainf.title + "\" id=\"" + metainf.identifier + "\">\n");
        builder.append("<algorithmType>" + metainf.algorithmType.getURI() + "</algorithmType>\n");

        builder.append("<Parameters>\n");
   
            for (int i = 0; i < metainf.Parameters.size(); i++) {
                builder.append("<param type=\"" + metainf.Parameters.get(i).dataType.toString() + "\" " +
                        "defaultvalue=\"" + metainf.Parameters.get(i).paramValue.toString() + "\">" +
                        metainf.Parameters.get(i).paramName + "</param>\n");
            }
   
        builder.append("</Parameters>\n");

        
        builder.append("</algorithm>\n");
        return new StringRepresentation(builder.toString(), mime);
    }
}
