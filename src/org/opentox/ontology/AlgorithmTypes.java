package org.opentox.ontology;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Property;

/**
 *
 * @author OpenTox - http://www.opentox.org
 * @author Sopasakis Pantelis
 * @author Sarimveis Harry
 */
public class AlgorithmTypes {

    protected static final String _NS = "http://www.opentox.org/algorithmTypes.owl#%s";
    public static final String NS = String.format(_NS, "");

    public enum Class {

        ClassificationEagerMultipleTargets,
        RegressionEagerMultipleTargets,
        RegressionEagerSingleTarget,
        ClassificationEagerSingleTarget,
        EagerLearning,
        Regression,
        Supervised,
        Learning,
        SingleTarget,
        MultipleTargets,
        MSDMTox;

        public String getNS() {
            return String.format(_NS,
                    toString());
        }

        public OntClass getOntClass(OntModel model) {
            return model.getOntClass(getNS());
        }

        public OntClass createOntClass(OntModel model) {
            return model.createClass(getNS());
        }

        public Property createProperty(OntModel model) {
            return model.createProperty(getNS());
        }
    };
}

