package org.opentox.namespaces;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.DC;
import com.hp.hpl.jena.vocabulary.OWL;
import java.util.HashMap;
import java.util.Map;

/**
 * Superclass for all Namespaces introduced in opentox such as {@link org.opentox.namespaces.AlgorithmTypes }
 * and {@link org.opentox.namespaces.OT }
 * @author OpenTox - http://www.opentox.org
 * @author Sopasakis Pantelis
 * @author Sarimveis Harry
 */
public abstract class Namespace {

    private static final long serialVersionUID = 569539073288446072L;

    protected static final String _NS = "http://www.opentox.org/api/1.1#%s";

    public static final String NS = String.format(_NS, "");

        
    
    protected static OntModel m_model = createModel();


    /**
     * Creates an OWL-DL Ontological Model which includes the definition
     * of some Namespace prefices such as ot, dc and owl.
     * @return Ontological Model ( {@link OntModel } ) with namespace definitions.
     */
    public static OntModel createModel()  {
        OntModel jenaModel = ModelFactory.createOntologyModel(
                OntModelSpec.OWL_DL_MEM, null);
        Map<String, String> prefixesMap = new HashMap<String, String>();
        prefixesMap.put("ot", Namespace.NS);
        prefixesMap.put("owl", OWL.NS);
        prefixesMap.put("dc", DC.NS);
        jenaModel.setNsPrefixes(prefixesMap);
        return jenaModel;
    }

    /** <p>The namespace of the vocabalary as a Resource.</p> */
    public static final Resource NAMESPACE =
            m_model.createResource(NS);

    /**
     * Class Resources of the Namespace.
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
         * ( {@link org.opentox.namespaces.Namespace.Class } ).
         * @return the corresponding jena resource.
         */
        public Resource getResource(){
            return this.resource;
        }
    }
}
