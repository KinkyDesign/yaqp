package org.opentox.Resources.Algorithms;

import org.opentox.Resources.AbstractResource;
import org.restlet.data.MediaType;
import org.restlet.representation.StringRepresentation;


/**
 * Build an RDF representation for an Algorithm.
 * Validated at <a href="http://www.rdfabout.com/demo/validator/">
 * http://www.rdfabout.com/demo/validator/</a> !
 * @author OpenTox - http://www.opentox.org
 * @author Sopasakis Pantelis
 * @author Kolotouros Dimitris
 * @author Sarimveis Harry
 */
public class AlgorithmRdfFormater extends AbstractAlgorithmFormater {

    private static final MediaType mime = MediaType.APPLICATION_RDF_XML;

    private static final long serialVersionUID = 52795861750765264L;

    /**
     * Class Constructor.
     * @param metainf Algorithm Meta-information one has to provide to
     * construct an AlgorithmRdfFormater object.
     */
    public AlgorithmRdfFormater(AlgorithmMetaInf metainf) {
        super.metainf = metainf;
    }


    private static class Elements{

        static class RDF{
            static String description = "rdf:Description",
                    about = "rdf:about",
                    datatype="rdf:datatype",
                    RDF="rdf:RDF";
        }

        static class DC{
            static String title="dc:title",
                    subject="dc:subject",
                    type="dc:type",
                    source="dc:source",
                    relation="dc:relation",
                    rights="dc:rights",
                    creator="dc:creator",
                    publisher="dc:publisher",
                    contributor="dc:contributor",
                    description="dc:description",
                    date="dc:date",
                    format="dc:format",
                    identifier="dc:identifier",
                    audience="dc:audience",
                    provenance="dc:provenance",
                    language="dc:language";
        }

        static class OT{
            static String
                    alg="ot:Algorithmalgorithm",
                    algType="ot:AlgorithmalgorithmType",
                    algParameters="ot:AlgorithmParameters",
                    algParam="ot:AlgorithmParam",
                    algParamName="ot:AlgorithmParamName",
                    algParamDefaultValue="ot:AlgorithmParamDefaultValue",
                    algstatisticsSupported="ot:AlgorithmstatisticsSupported",
                    algstatistic="ot:Algorithmstatistic";
        }

    }


    private String opentoxAlgoritmElement() {
        StringBuilder builder = new StringBuilder();

        builder.append("<"+Elements.OT.alg+">");
        builder.append("<"+Elements.RDF.description+">");
        builder.append("<"+Elements.OT.algType+">" +
                metainf.getAlgorithmType() +
                "</"+Elements.OT.algType+">\n");
        builder.append("<"+Elements.OT.algParameters+">\n");
        builder.append("<"+Elements.RDF.description+">\n");
        if (metainf.getParameters()[0].length != 3) {
            System.err.println("ERROR!!! Invalid Parameters Element!");
        } else {
            for (int i = 0; i < metainf.getParameters().length; i++) {
                builder.append("<"+Elements.OT.algParam+">\n");
                builder.append("<"+Elements.RDF.description+">\n");
                builder.append("<"+Elements.OT.algParamName+">\n"+
                        metainf.getParameters()[i][0]+
                        "</"+Elements.OT.algParamName+">\n");
                builder.append("<"+Elements.OT.algParamDefaultValue+" "+
                        Elements.RDF.datatype+"=\""+
                        super.xsd+metainf.getParameters()[i][1]+
                        "\" >");
                builder.append(metainf.getParameters()[i][2]);
                builder.append("</"+Elements.OT.algParamDefaultValue+">\n");
                builder.append("</"+Elements.RDF.description+">\n");
                builder.append("</"+Elements.OT.algParam+">\n");
            }
        }
        builder.append("</"+Elements.RDF.description+">\n");
        builder.append("</"+Elements.OT.algParameters+">\n");

        if (metainf.getStatisticsSupported().isEmpty()) {
            builder.append("<"+Elements.OT.algstatisticsSupported+"/>\n");
        } else {
            builder.append("<"+Elements.OT.algstatisticsSupported+">\n");
            builder.append("<"+Elements.RDF.description+">\n");
            for (int i = 0; i < metainf.getStatisticsSupported().size(); i++) {
                builder.append("<"+Elements.OT.algstatistic+">\n" +
                        metainf.getStatisticsSupported().get(i) +
                        "</"+Elements.OT.algstatistic+">\n");
            }
            builder.append("</"+Elements.RDF.description+">\n");
            builder.append("</"+Elements.OT.algstatisticsSupported+">\n");
        }
        builder.append("</"+Elements.RDF.description+">\n");
        builder.append("</"+Elements.OT.alg+">\n");
        
        return builder.toString();
    }


    @Override
    public StringRepresentation getStringRepresentation() {
        StringBuilder builder = new StringBuilder();
        builder.append(AbstractResource.xmlIntro);

        builder.append("<"+Elements.RDF.RDF+" " +
                "xmlns:rdf=\"" + rdf + "\" " +
                "xmlns:dc=\"" + dc + "\" " +
                "xmlns:ot=\"" + ot + "\" " +
                "xmlns:xsd=\"" + xsd + "\""+
                ">\n");
        builder.append("<"+Elements.RDF.description+" "+
                Elements.RDF.about+"=\"" +
                metainf.getAbout() + "\">\n");
        builder.append("<"+Elements.DC.title+">" +
                metainf.getTitle() +
                "</"+Elements.DC.title+">\n");
        builder.append("<"+Elements.DC.subject+">" +
                metainf.getSubject() +
                "</"+Elements.DC.subject+">\n");
        builder.append("<"+Elements.DC.description+">" +
                metainf.getDescription() +
                "</"+Elements.DC.description+">\n");
        builder.append("<"+Elements.DC.type+">" +
                metainf.getType() +
                "</"+Elements.DC.type+">\n");
        builder.append("<"+Elements.DC.source+">" +
                metainf.getSource() +
                "</"+Elements.DC.source+">\n");
        builder.append("<"+Elements.DC.relation+">" +
                metainf.getRelation() +
                "</"+Elements.DC.relation+">\n");
        builder.append("<"+Elements.DC.rights+">" +
                metainf.getRights() +
                "</"+Elements.DC.rights+">\n");
        builder.append("<"+Elements.DC.creator+">" +
                metainf.getCreator() +
                "</"+Elements.DC.creator+">\n");
        builder.append("<"+Elements.DC.publisher+">" +
                metainf.getPublisher() +
                "</"+Elements.DC.publisher+">\n");
        builder.append("<"+Elements.DC.contributor +">" +
                metainf.getContributor() +
                "</"+Elements.DC.contributor+">\n");
        builder.append("<"+Elements.DC.date+">" +
                metainf.getDate() +
                "</"+Elements.DC.date+">\n");
        builder.append("<"+Elements.DC.format+">" +
                metainf.getFormat() +
                "</"+Elements.DC.format+">\n");
        builder.append("<"+Elements.DC.identifier+">" +
                metainf.getIdentifier() +
                "</"+Elements.DC.identifier+">\n");
        builder.append("<"+Elements.DC.audience+">" +
                metainf.getAudience() +
                "</"+Elements.DC.audience+">\n");
        builder.append("<"+Elements.DC.provenance+">" +
                metainf.getProvenance() + "</dc:provenance>\n");
        builder.append("<"+Elements.DC.language+">" +
                metainf.getLanguage() +
                "</"+Elements.DC.language+">\n");
        builder.append(opentoxAlgoritmElement());
        builder.append("</"+Elements.RDF.description+">\n");
        builder.append("</"+Elements.RDF.RDF+">\n\n");
        return new StringRepresentation(builder.toString(), mime);
    }
       

    
    /**
     * @param rdfns A new namespace for RDF
     */
    public void setRdfNamespace(String rdfns) {
        this.rdf = rdfns;
    }

    /**
     * Update the namespace for OpenTox Models and Algorithms.
     * @param otns
     */
    public void setOpentoxNamespace(String otns) {
        this.ot = otns;
    }

    /**
     * Update the Dublin Core namespace.
     * @param dcns new DCNS
     */
    public void setDcNamespace(String dcns) {
        this.dc = dcns;
    }

   
}
