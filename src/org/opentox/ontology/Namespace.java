package org.opentox.ontology;

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
 *
 * @author OpenTox - http://www.opentox.org
 * @author Sopasakis Pantelis
 * @author Sarimveis Harry
 */
public abstract class Namespace {

    private static final long serialVersionUID = 569539073288446072L;

    protected static final String _NS = "http://www.opentox.org/api/1.1#%s";

    public static final String NS = String.format(_NS, "");

    /** <p>The RDF model that holds the vocabulary terms</p> */
    protected static Model m_model = ModelFactory.createDefaultModel();

    public static OntModel createModel() throws Exception {
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

    public static class Class {

        protected Resource resource;

        public Class() {
        }

        public Class(Resource resource) {
            this.resource = resource;
        }

        public String getURI() {
            return resource.getURI();
        }

        public OntClass getOntClass(final OntModel model) {
            return model.getOntClass(getURI());
        }

        public OntClass createOntClass(final OntModel model) {
            return model.createClass(getURI());
        }

        public Property createProperty(final OntModel model) {
            return model.createProperty(getURI());
        }

        public Resource getResource(){
            return this.resource;
        }
    }
}
