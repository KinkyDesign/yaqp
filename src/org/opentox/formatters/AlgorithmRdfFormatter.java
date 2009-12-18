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

            // Create an Individual for the algorithm resource:
            Individual algorithm = jenaModel.createIndividual(metainf.identifier,
                    OT.Class.Algorithm.getOntClass(jenaModel));

            // set the title and the identifier for the algorithm:
            algorithm.addLiteral(DC.title,
                    jenaModel.createTypedLiteral(metainf.title,XSDDatatype.XSDstring));
            algorithm.addLiteral(DC.creator,
                    jenaModel.createTypedLiteral(AbstractResource.URIs.baseURI,XSDDatatype.XSDanyURI));
            algorithm.addLiteral(DC.source,
                    jenaModel.createTypedLiteral(AbstractResource.URIs.baseURI,XSDDatatype.XSDanyURI));
            algorithm.addLiteral(DC.publisher,
                    jenaModel.createTypedLiteral(AbstractResource.URIs.baseURI,XSDDatatype.XSDanyURI));
            algorithm.addLiteral(DC.contributor,
                    jenaModel.createTypedLiteral(AbstractResource.URIs.OpentoxUri,XSDDatatype.XSDanyURI));
            algorithm.addLiteral(DC.relation,
                    jenaModel.createTypedLiteral(AbstractResource.URIs.OpentoxUri,XSDDatatype.XSDanyURI));
            algorithm.addLiteral(DC.rights,
                    jenaModel.createTypedLiteral(AbstractResource.URIs.licenceUri,XSDDatatype.XSDanyURI));
            algorithm.addLiteral(DC.date,
                    jenaModel.createTypedLiteral("2009/16/12",XSDDatatype.XSDdate));
           algorithm.addLiteral(DC.description,
                    jenaModel.createTypedLiteral(metainf.description,XSDDatatype.XSDanyURI));
            algorithm.addLiteral(DC.identifier,
                    jenaModel.createTypedLiteral(metainf.identifier,XSDDatatype.XSDanyURI));

            algorithm.addProperty(OT.isA, metainf.algorithmType.createProperty(jenaModel));
            algorithm.addProperty(OWL.sameAs, metainf.algorithmType.createProperty(jenaModel));

            Individual iparam;
                    for (int i=0;i<metainf.Parameters.size();i++){
                        iparam = jenaModel.createIndividual(OT.Class.Parameter.getOntClass(jenaModel));
                        iparam.addProperty(DC.title, metainf.Parameters.get(i).paramName);
                        iparam.addLiteral(OT.paramValue, jenaModel.createTypedLiteral(
                                metainf.Parameters.get(i).paramValue.toString(),
                                metainf.Parameters.get(i).dataType));
                        iparam.addLiteral(OT.paramScope, jenaModel.
                                createTypedLiteral(metainf.Parameters.get(i).paramScope,
                                XSDDatatype.XSDstring));
                        algorithm.addProperty(OT.parameters, iparam);
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
