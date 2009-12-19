package org.opentox.formatters;

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.vocabulary.DC;
import com.hp.hpl.jena.vocabulary.OWL;
import java.io.ByteArrayOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.opentox.MediaTypes.OpenToxMediaType;
import org.opentox.Resources.AbstractResource;
import org.opentox.Resources.Algorithms.*;
import org.restlet.data.MediaType;
import org.restlet.representation.StringRepresentation;
import org.opentox.ontology.*;

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

    private static final long serialVersionUID = 5082315122564986995L;

    /**
     * Class Constructor.
     * @param metainf Algorithm Meta-information one has to provide to
     * construct an AlgorithmRdfFormater object.
     */
    public AlgorithmRdfFormatter(AlgorithmMetaInf metainf) {
        super.metainf = metainf;
    }

    @Override
    public StringRepresentation getStringRepresentation(MediaType mediatype) {

        OntModel jenaModel;
        try {
            // define a jena model:
            jenaModel = OT.createModel();

            // defines Algorithm and Parameter classes:
            
            OT.Class.Parameter.createOntClass(jenaModel);
            OT.Class.Algorithm.createOntClass(jenaModel);



            // Create an Individual for the algorithm resource:
            Individual algorithm = jenaModel.createIndividual(metainf.identifier,
                    jenaModel.createOntResource(OT.Class.Algorithm.getURI()));

            // set the title and the identifier for the algorithm:
            algorithm.addLiteral(jenaModel.createAnnotationProperty(DC.title.getURI()),
                    jenaModel.createTypedLiteral(metainf.title,XSDDatatype.XSDstring));
            algorithm.addLiteral(jenaModel.createAnnotationProperty(DC.creator.getURI()),
                    jenaModel.createTypedLiteral(AbstractResource.URIs.baseURI,XSDDatatype.XSDanyURI));
            algorithm.addLiteral(jenaModel.createAnnotationProperty(DC.source.getURI()),
                    jenaModel.createTypedLiteral(AbstractResource.URIs.baseURI,XSDDatatype.XSDanyURI));
            algorithm.addLiteral(jenaModel.createAnnotationProperty(DC.publisher.getURI()),
                    jenaModel.createTypedLiteral(AbstractResource.URIs.baseURI,XSDDatatype.XSDanyURI));
            algorithm.addLiteral(jenaModel.createAnnotationProperty(DC.contributor.getURI()),
                    jenaModel.createTypedLiteral(AbstractResource.URIs.OpentoxUri,XSDDatatype.XSDanyURI));
            algorithm.addLiteral(jenaModel.createAnnotationProperty(DC.relation.getURI()),
                    jenaModel.createTypedLiteral(AbstractResource.URIs.OpentoxUri,XSDDatatype.XSDanyURI));
            algorithm.addLiteral(jenaModel.createAnnotationProperty(DC.rights.getURI()),
                    jenaModel.createTypedLiteral(AbstractResource.URIs.licenceUri,XSDDatatype.XSDanyURI));
            algorithm.addLiteral(jenaModel.createAnnotationProperty(DC.date.getURI()),
                    jenaModel.createTypedLiteral("2009/16/12",XSDDatatype.XSDdate));
           algorithm.addLiteral(jenaModel.createAnnotationProperty(DC.description.getURI()),
                    jenaModel.createTypedLiteral(metainf.description,XSDDatatype.XSDanyURI));
            algorithm.addLiteral(jenaModel.createAnnotationProperty(DC.identifier.getURI()),
                    jenaModel.createTypedLiteral(metainf.identifier,XSDDatatype.XSDanyURI));

//            algorithm.addProperty(jenaModel.createAnnotationProperty(OT.isA.getURI()),
//                    jenaModel.createOntResource(metainf.algorithmType.getURI()) );

            algorithm.setSameAs(jenaModel.createResource(metainf.algorithmType.getURI(), OT.Class.Algorithm.getResource() ));
            

            Individual iparam;
                    for (int i=0;i<metainf.Parameters.size();i++){
                        iparam = jenaModel.createIndividual(OT.Class.Parameter.getOntClass(jenaModel));
                        iparam.addProperty(jenaModel.createAnnotationProperty(DC.title.getURI()),
                                metainf.Parameters.get(i).paramName);
                        iparam.addLiteral(jenaModel.createAnnotationProperty(OT.paramValue.getURI()),
                                jenaModel.createTypedLiteral(
                                metainf.Parameters.get(i).paramValue.toString(),
                                metainf.Parameters.get(i).dataType));
                        iparam.addLiteral(jenaModel.createAnnotationProperty(OT.paramScope.getURI()),
                                jenaModel.createTypedLiteral(metainf.Parameters.get(i).paramScope,
                                XSDDatatype.XSDstring));
                        algorithm.addProperty(jenaModel.createAnnotationProperty(OT.parameters.getURI()), iparam);
                    }
                    ByteArrayOutputStream outStream = new ByteArrayOutputStream();
                    String Lang="RDF/XML";
                    if (MediaType.APPLICATION_RDF_TURTLE.equals(mediatype)){
                        Lang="TTL";
                    }else if (OpenToxMediaType.TEXT_TRIPLE.equals(mediatype)){
                        Lang="N-TRIPLE";
                    }else if (OpenToxMediaType.TEXT_N3.equals(mediatype)){
                        Lang="N3";
                    }
                    jenaModel.write(outStream, Lang);
                    return new StringRepresentation(outStream.toString(), mediatype);

        } catch (Exception ex) {
            Logger.getLogger(AlgorithmRdfFormatter.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

    
    }
}
