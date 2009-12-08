package org.opentox.formatters;

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.vocabulary.DC;
import com.hp.hpl.jena.vocabulary.RDF;
import java.io.ByteArrayOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.opentox.Resources.Algorithms.*;
import org.opentox.Resources.AbstractResource;
import org.restlet.data.MediaType;
import org.restlet.representation.StringRepresentation;
import org.opentox.ontology.*;

/**
 * Build an RDF representation for an Algorithm.
 * Validated at <a href="http://www.rdfabout.com/demo/validator/">
 * http://www.rdfabout.com/demo/validator/</a> !
 * @author OpenTox - http://www.opentox.org
 * @author Sopasakis Pantelis
 * @author Kolotouros Dimitris
 * @author Sarimveis Harry
 */
public class AlgorithmRdfFormatter extends AbstractAlgorithmFormatter {

    private static final long serialVersionUID = 52795861750765264L;

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
            OT.Class.Algorithm.createOntClass(jenaModel);
            OT.Class.Parameter.createOntClass(jenaModel);

            // Create an Individual for the algorithm resource:
            Individual algorithm = jenaModel.createIndividual(metainf.identifier,
                    OT.Class.Algorithm.getOntClass(jenaModel));

            // set the title and the identifier for the algorithm:
            algorithm.addLiteral(DC.title,
                    jenaModel.createTypedLiteral(metainf.title,XSDDatatype.XSDstring));
            algorithm.addLiteral(DC.identifier,
                    jenaModel.createTypedLiteral(metainf.identifier,XSDDatatype.XSDanyURI));

            algorithm.addProperty(OT.isA, metainf.algorithmType.createProperty(jenaModel));

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
                    }else if (MediaType.APPLICATION_RDF_TRIX.equals(mediatype)){
                        Lang="N-TRIPLE";
                    }
                    jenaModel.write(outStream, Lang);
                    return new StringRepresentation(outStream.toString(), mediatype);

        } catch (Exception ex) {
            Logger.getLogger(AlgorithmRdfFormatter.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

    
    }
}
