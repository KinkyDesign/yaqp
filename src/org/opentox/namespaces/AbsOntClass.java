package org.opentox.namespaces;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;

/**
 *
 * @author chung
 */
public abstract class AbsOntClass extends Namespace {

    protected Resource resource;

    public AbsOntClass() {
    }

    public AbsOntClass(Resource resource) {
        this.resource = resource;
    }

    /**
     * Returns the URI of the class
     * @return class URI
     */
    public String getURI() {
        return resource.getURI();
    }

    /**
     * Returns the corresponding Ontological Class (i.e. an instance of
     * {@link com.hp.hpl.jena.ontology.OntClass } )
     * @param model The ontological model
     * @return the ontological class of the model
     */
    public OntClass getOntClass(final OntModel model) {
        return model.getOntClass(getURI());
    }

    /**
     * Creates a new Ontological class for an Ontological Model.
     * @param model The ontological model.
     * @return The generated ontological class.
     */
    public OntClass createOntClass(final OntModel model) {
        return model.createClass(getURI());
    }

    /**
     * Generates a property out of a given model.
     * @param model An ontological model.
     * @return The corresponding property.
     */
    public Property createProperty(final OntModel model) {
        return model.createProperty(getURI());
    }

    /**
     * Returns the Resource of this class
     * ( {@link org.opentox.namespaces.Namespace.Class } ).
     * @return the corresponding jena resource.
     */
    public Resource getResource() {
        return this.resource;
    }
}
