package org.opentox.ontology.namespaces;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.DC;
import com.hp.hpl.jena.vocabulary.OWL;
import java.util.HashMap;
import java.util.Map;
import org.opentox.interfaces.IOntClass;

/**
 * Superclass for all Namespaces introduced in opentox such as 
 * {@link org.opentox.ontology.namespaces.AlgorithmTypes },
 * {@link org.opentox.ontology.namespaces.OTProperties }
 * and {@link org.opentox.ontology.namespaces.OTClass }
 * @author OpenTox - http://www.opentox.org
 * @author Sopasakis Pantelis
 * @author Sarimveis Harry
 * @see org.opentox.interfaces.IOntClass
 */
public abstract class AbsOntClass implements IOntClass {

    private static final long serialVersionUID = 569539073288446072L;

    protected static final String _NS_OT = "http://www.opentox.org/api/1.1#%s";
    protected static final String _NS_AlgorithmTypes = "http://www.opentox.org/algorithmTypes.owl%s";

    public static final String NS_OT = String.format(_NS_OT, "");
    public static final String NS_AlgorithmTypes = String.format(_NS_AlgorithmTypes, "");

            
    protected static OntModel m_model = createModel();

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
     * ( {@link org.opentox.ontology.namespaces.AbsOntClass.Class } ).
     * @return the corresponding jena resource.
     */
    public Resource getResource() {
        return this.resource;
    }


    /**
     * Creates an OWL-DL Ontological Model which includes the definition
     * of some AbsOntClass prefices such as ot, dc and owl.
     * @return Ontological Model ( {@link OntModel } ) with namespace definitions.
     */
    public static OntModel createModel()  {
        OntModel jenaModel = ModelFactory.createOntologyModel(
                OntModelSpec.OWL_DL_MEM, null);
        Map<String, String> prefixesMap = new HashMap<String, String>();
        prefixesMap.put("ot", AbsOntClass.NS_OT);
        prefixesMap.put("owl", OWL.NS);
        prefixesMap.put("dc", DC.NS);
        prefixesMap.put("ot_algorithmTypes", AbsOntClass.NS_AlgorithmTypes);
        jenaModel.setNsPrefixes(prefixesMap);
        return jenaModel;
    }

    /** <p>The namespace of the vocabalary as a Resource.</p> */
    public static final Resource NAMESPACE =
            m_model.createResource(NS_OT);

    /**
     * Class Resources of the AbsOntClass.
     */
    public static class Class {

        protected Resource resource;

        public Class() {
        }

        public Class(Resource resource) {
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
         * ( {@link org.opentox.ontology.namespaces.AbsOntClass.Class } ).
         * @return the corresponding jena resource.
         */
        public Resource getResource(){
            return this.resource;
        }
    }

}
