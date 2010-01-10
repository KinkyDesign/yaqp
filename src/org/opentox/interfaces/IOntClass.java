package org.opentox.interfaces;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;

/**
 *
 * @author Sopasakis Pantelis
 */
public interface IOntClass {

    /**
     * Creates a new Ontological class for an Ontological Model.
     * @param model The ontological model.
     * @return The generated ontological class.
     */
    OntClass createOntClass(final OntModel model);

    /**
     * Generates a property out of a given model.
     * @param model An ontological model.
     * @return The corresponding property.
     */
    Property createProperty(final OntModel model);

    /**
     * Returns the corresponding Ontological Class (i.e. an instance of
     * {@link com.hp.hpl.jena.ontology.OntClass } )
     * @param model The ontological model
     * @return the ontological class of the model
     */
    OntClass getOntClass(final OntModel model);

    /**
     * Returns the Resource of this class
     * ( {@link org.opentox.interfaces.IOntClass } ).
     * @return the corresponding jena resource.
     */
    Resource getResource();

    /**
     * Returns the URI of the class
     * @return class URI
     */
    String getURI();

}
