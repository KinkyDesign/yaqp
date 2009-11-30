package org.opentox.formatters;


import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.vocabulary.DC;
import com.hp.hpl.jena.vocabulary.RDF;
import java.io.ByteArrayOutputStream;
import org.opentox.formatters.NameSpaces.*;
import org.opentox.Resources.Algorithms.*;
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
public class AlgorithmRdfFormatter extends AbstractAlgorithmFormatter {

    private static final MediaType mime = MediaType.APPLICATION_RDF_XML;

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
    public StringRepresentation getStringRepresentation() {        
        com.hp.hpl.jena.rdf.model.Model model =
                com.hp.hpl.jena.rdf.model.ModelFactory.createDefaultModel();

        model.setNsPrefix("ot", OT.getURI());
        model.setNsPrefix("dc", DC.getURI());

        com.hp.hpl.jena.rdf.model.Resource algorithmResource =
                model.createResource(
                metainf.identifier);

        algorithmResource.
                addProperty(DC.identifier, metainf.identifier).
                addProperty(DC.type, OT.algorithm).
                addProperty(DC.rights, metainf.rights).
                addProperty(DC.publisher, AbstractResource.URIs.baseURI);

        /**
         * Add Parameters....
         */
        for (int i=0;i<metainf.Parameters.size();i++){
            algorithmResource.
                addProperty(OT.parameters,
                model.createResource().
                    addProperty(DC.title,metainf.Parameters.get(i).paramName,
                      XSDDatatype.XSDstring).

                    addProperty(OT.paramScope,
                      metainf.Parameters.get(i).paramScope,
                        XSDDatatype.XSDstring).
                    
                    addProperty(OT.paramValue,
                      metainf.Parameters.get(i).paramValue.toString(),
                        metainf.Parameters.get(i).dataType).

                    addProperty(DC.type, OT.parameter)
                 );
        }

        /**
         * Add supported statistics...
         */
        for (int i=0;i<metainf.statisticsSupported.size();i++){
            algorithmResource.addProperty(OT.statisticsSupported,
                model.createResource(). addProperty(RDF.type, OT.statistic).
                addProperty(DC.title, metainf.statisticsSupported.get(i), XSDDatatype.XSDstring)
                );
        }


        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        model.write(outStream);

        return new StringRepresentation(outStream.toString(), mime);
    }
       

    
       
}
