package org.opentox.Resources.Algorithms;

import org.opentox.Resources.AbstractResource;
import org.restlet.data.MediaType;
import org.restlet.representation.StringRepresentation;

/**
 * Build an RDF representation for an Algorithm.
 * @author OpenTox - http://www.opentox.org
 * @author Sopasakis Pantelis
 * @author Sarimveis Harry
 */
public class AlgorithmRdfFormater extends AbstractAlgorithmFormater {

    private static final MediaType mime = MediaType.APPLICATION_RDF_XML;
    private static final long serialVersionUID = 52795861750765264L;
    

    public AlgorithmRdfFormater(AlgorithmMetaInf metainf) {
        super.metainf = metainf;
    }

    
    /**
     * Redefine the RDF namespace which by default is set to
     * http://www.w3.org/1999/02/22-rdf-syntax-ns#
     * @param rdfns A new namespace for RDF
     */
    public void setRdfNamespace(String rdfns) {
        this.rdfNamespace = rdfns;
    }

    /**
     * Update the namespace for OpenTox Models and Algorithms.
     * @param otns
     */
    public void setOpentoxNamespace(String otns) {
        this.opentoxNamespace = otns;
    }

    /**
     * Update the Dublin Core namespace.
     * @param dcns new DCNS
     */
    public void setDcNamespace(String dcns) {
        this.dcNamespace = dcns;
    }



    private String opentoxAlgoritmElement() {
        StringBuilder builder = new StringBuilder();
        builder.append("<ot:algorithm name=\"" + metainf.getTitle() + "\" ot:id=\"" + metainf.getIdentifier() + "\">\n");
        builder.append("<ot:algorithmType>" + metainf.getAlgorithmType() + "</ot:algorithmType>\n");

        builder.append("<ot:Parameters>\n");
        if (metainf.getParameters()[0].length != 3) {
            System.err.println("ERROR!!! Invalid Parameters Element!");
        } else {
            for (int i = 0; i < metainf.getParameters().length; i++) {
                builder.append("<ot:param ot:type=\"" + metainf.getParameters()[i][1] + "\" " +
                        "ot:defaultvalue=\"" + metainf.getParameters()[i][2] + "\">" +
                        metainf.getParameters()[i][0] + "</ot:param>\n");
            }
        }
        builder.append("</ot:Parameters>\n");

        if (metainf.getStatisticsSupported().isEmpty()) {
            builder.append("<ot:statisticsSupported/>\n");
        } else {
            builder.append("<ot:statisticsSupported>\n");
            for (int i = 0; i < metainf.getStatisticsSupported().size(); i++) {
                builder.append("<ot:statistic>" + metainf.getStatisticsSupported().get(i) + "</ot:statistic>\n");
            }
            builder.append("</ot:statisticsSupported>\n");
        }
        builder.append("</ot:algorithm>\n");
        return builder.toString();
    }


    @Override
    public StringRepresentation getStringRepresentation() {
        StringBuilder builder = new StringBuilder();
        builder.append(AbstractResource.xmlIntro);

        builder.append("<rdf:RDF " +
                "xmlns:rdf=\"" + rdfNamespace + "\" " +
                "xmlns:dc=\"" + dcNamespace + "\" " +
                "xmlns:ot=\"" + opentoxNamespace + "\">\n");

        builder.append("<rdf:Description rdf:about=\"" + metainf.getAbout() + "\">\n");
        builder.append("<dc:title>" + metainf.getTitle() + "</dc:title>\n");
        builder.append("<dc:subject>" + metainf.getSubject() + "</dc:subject>\n");
        builder.append("<dc:description>" + metainf.getDescription() + "</dc:description>\n");
        builder.append("<dc:type>" + metainf.getType() + "</dc:type>\n");
        builder.append("<dc:source>" + metainf.getSource() + "</dc:source>\n");
        builder.append("<dc:relation>" + metainf.getRelation() + "</dc:relation>\n");
        builder.append("<dc:rights>" + metainf.getRights() + "</dc:rights>\n");
        builder.append("<dc:creator>" + metainf.getCreator() + "</dc:creator>\n");
        builder.append("<dc:publisher>" + metainf.getPublisher() + "</dc:publisher>\n");
        builder.append("<dc:contributor>" + metainf.getContributor() + "</dc:contributor>\n");
        builder.append("<dc:date>" + metainf.getDate() + "</dc:date>\n");
        builder.append("<dc:format>" + metainf.getFormat() + "</dc:format>\n");
        builder.append("<dc:identifier>" + metainf.getIdentifier() + "</dc:identifier>\n");
        builder.append("<dc:audience>" + metainf.getAudience() + "</dc:audience>\n");
        builder.append("<dc:provenance>" + metainf.getProvenance() + "</dc:provenance>\n");
        builder.append("<dc:language>" + metainf.getLanguage() + "</dc:language>\n");
        builder.append(opentoxAlgoritmElement());
        builder.append("</rdf:Description>\n");
        builder.append("</rdf:RDF>\n\n");
        return new StringRepresentation(builder.toString(), mime);
    }

}
