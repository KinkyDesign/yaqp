package org.opentox.rdf;

import org.opentox.namespaces.OT;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.vocabulary.DC;
import java.io.OutputStream;
import org.opentox.resource.AbstractResource;

/**
 * 
 * @author OpenTox - http://www.opentox.org/
 * @author Sopasakis Pantelis
 * @author Sarimveis Harry
 * @version 1.3.3 (Last update: Dec 23, 2009)
 */
public class Feature extends RDFHandler {

    public Feature() {
        super();
    }

    /**
     * Creates a new Feature and writes it to an OutputStream.
     * @param sameAs
     * @param output
     */
    public void createNewFeature(String sameAs, OutputStream output) {
        OntModel featureModel = OT.createModel();
        Individual feature = featureModel.createIndividual(featureModel.getOntClass(OT.Class.Dataset.getURI()));
        feature.addRDFType(OT.Class.Feature.createProperty(featureModel));
        feature.addProperty(featureModel.createAnnotationProperty(DC.creator.getURI()), featureModel.createTypedLiteral(AbstractResource.URIs.baseURI));
        feature.setSameAs(featureModel.createResource(sameAs, OT.Class.Feature.getResource()));
        OT.Class.Feature.createOntClass(featureModel);
        featureModel.write(output);
    }


}
