package org.opentox.formatters;

import org.opentox.ontology.meta.AlgorithmMeta;
import org.opentox.namespaces.OTProperties;
import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.vocabulary.DC;
import java.io.ByteArrayOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.opentox.media.OpenToxMediaType;
import org.opentox.namespaces.OTClass;
import org.opentox.resource.OTResource;
import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;

/**
 * Build an RDF representation for an Algorithm.
 * 
 * Validated at <a href="http://www.rdfabout.com/demo/validator/">
 * http://www.rdfabout.com/demo/validator/</a> !
 * @author OpenTox - http://www.opentox.org
 * @author Sopasakis Pantelis
 * @author Kolotouros Dimitris
 * @author Sarimveis Harry
 */
public class AlgorithmRdfFormatter extends AbstractAlgorithmFormatter {

    private static final long serialVersionUID = 4685714092437186923L;

    /**
     * Class Constructor.
     * @param metainf Algorithm Meta-information one has to provide to
     * construct an AlgorithmRdfFormater object.
     */
    public AlgorithmRdfFormatter(AlgorithmMeta metainf) {
        super.metainf = metainf;
    }

    @Override
    public Representation getRepresentation(MediaType mediatype) {
    ByteArrayOutputStream outStream = null;
        OntModel jenaModel;
        try {
            // define a jena model:
            jenaModel = OTProperties.createModel();

            // defines Algorithm and Parameter classes:

            OTClass.Parameter.createOntClass(jenaModel);
            OTClass.Algorithm.createOntClass(jenaModel);



            // Create an Individual for the algorithm resource:
            Individual algorithm = jenaModel.createIndividual(metainf.identifier,
                    jenaModel.createOntResource(OTClass.Algorithm.getURI()));

            // dc:title
            algorithm.addLiteral(jenaModel.createAnnotationProperty(DC.title.getURI()),
                    jenaModel.createTypedLiteral(metainf.title, XSDDatatype.XSDstring));
            // dc:creator
            algorithm.addLiteral(jenaModel.createAnnotationProperty(DC.creator.getURI()),
                    jenaModel.createTypedLiteral(OTResource.URIs.baseURI, XSDDatatype.XSDanyURI));
            // dc:source
            algorithm.addLiteral(jenaModel.createAnnotationProperty(DC.source.getURI()),
                    jenaModel.createTypedLiteral(OTResource.URIs.baseURI, XSDDatatype.XSDanyURI));
            // dc:publisher
            algorithm.addLiteral(jenaModel.createAnnotationProperty(DC.publisher.getURI()),
                    jenaModel.createTypedLiteral(OTResource.URIs.baseURI, XSDDatatype.XSDanyURI));
            // dc:contributor
            algorithm.addLiteral(jenaModel.createAnnotationProperty(DC.contributor.getURI()),
                    jenaModel.createTypedLiteral(OTResource.URIs.OpentoxUri, XSDDatatype.XSDanyURI));
            // dc:relation
            algorithm.addLiteral(jenaModel.createAnnotationProperty(DC.relation.getURI()),
                    jenaModel.createTypedLiteral(OTResource.URIs.OpentoxUri, XSDDatatype.XSDanyURI));
            // dc:rights
            algorithm.addLiteral(jenaModel.createAnnotationProperty(DC.rights.getURI()),
                    jenaModel.createTypedLiteral(OTResource.URIs.licenceUri, XSDDatatype.XSDanyURI));
            // dc:date
            algorithm.addLiteral(jenaModel.createAnnotationProperty(DC.date.getURI()),
                    jenaModel.createTypedLiteral("2009/16/12", XSDDatatype.XSDdate));
            // dc:description
            algorithm.addLiteral(jenaModel.createAnnotationProperty(DC.description.getURI()),
                    jenaModel.createTypedLiteral(metainf.description, XSDDatatype.XSDanyURI));
            // dc:identifier
            algorithm.addLiteral(jenaModel.createAnnotationProperty(DC.identifier.getURI()),
                    jenaModel.createTypedLiteral(metainf.identifier, XSDDatatype.XSDanyURI));
            // ot:isA
            algorithm.addProperty(jenaModel.createAnnotationProperty(OTProperties.isA.getURI()), jenaModel.createOntResource(metainf.algorithmType.getURI()));
            // owl:sameAs
            algorithm.setSameAs(jenaModel.createResource(metainf.algorithmType.getURI(), OTClass.Algorithm.getResource()));


            Individual iparam;
            for (int i = 0; i < metainf.Parameters.size(); i++) {
                iparam = jenaModel.createIndividual(OTClass.Parameter.getOntClass(jenaModel));
                iparam.addProperty(jenaModel.createAnnotationProperty(DC.title.getURI()),
                        metainf.Parameters.get(i).paramName);
                iparam.addLiteral(jenaModel.createAnnotationProperty(OTProperties.paramValue.getURI()),
                        jenaModel.createTypedLiteral(
                        metainf.Parameters.get(i).paramValue.toString(),
                        metainf.Parameters.get(i).dataType));
                iparam.addLiteral(jenaModel.createAnnotationProperty(OTProperties.paramScope.getURI()),
                        jenaModel.createTypedLiteral(metainf.Parameters.get(i).paramScope,
                        XSDDatatype.XSDstring));
                algorithm.addProperty(jenaModel.createAnnotationProperty(OTProperties.parameters.getURI()), iparam);
            }

            outStream = new ByteArrayOutputStream();
            String Lang = "RDF/XML";
            if (MediaType.APPLICATION_RDF_TURTLE.equals(mediatype)) {
                Lang = "TTL";
            } else if (OpenToxMediaType.TEXT_TRIPLE.equals(mediatype)) {
                Lang = "N-TRIPLE";
            } else if (OpenToxMediaType.TEXT_N3.equals(mediatype)) {
                Lang = "N3";
            }
            jenaModel.write(outStream, Lang);            

        } catch (Exception ex) {
            errorRep.append(ex, "Error while formatting and algorithm RDF representation.", Status.SERVER_ERROR_INTERNAL);
            Logger.getLogger(AlgorithmRdfFormatter.class.getName()).log(Level.SEVERE, null, ex);
        }

            return errorRep.getErrorLevel()==0 ? new StringRepresentation(outStream.toString(), mediatype) : errorRep;
    }
}
