package org.opentox.ontology.namespaces;

import com.hp.hpl.jena.rdf.model.Resource;

/**
 *
 * @author OpenTox - http://www.opentox.org
 * @author Sopasakis Pantelis
 * @author Sarimveis Harry
 */
public class AlgorithmTypes extends AbsOntClass{

    private static final long serialVersionUID = -6959757110626828334L;

    public static class Class extends AbsOntClass.Class{

        public Class(Resource resource){
            super(resource);
        }

        public static final Class ClassificationEagerMultipleTargets =
                new Class(m_model.createResource(String.format(_NS, "ClassificationEagerMultipleTargets")));
        public static final Class RegressionEagerMultipleTargets =
                new Class(m_model.createResource(String.format(_NS, "RegressionEagerMultipleTargets")));
        public static final Class RegressionEagerSingleTarget =
                new Class(m_model.createResource(String.format(_NS, "RegressionEagerSingleTarget")));
        public static final Class ClassificationEagerSingleTarget =
                new Class(m_model.createResource(String.format(_NS, "ClassificationEagerSingleTarget")));
        public static final Class EagerLearning =
                new Class(m_model.createResource(String.format(_NS, "EagerLearning")));
        public static final Class Regression =
                new Class(m_model.createResource(String.format(_NS, "Regression")));
        public static final Class Supervised =
                new Class(m_model.createResource(String.format(_NS, "Supervised")));
        public static final Class Learning =
                new Class(m_model.createResource(String.format(_NS, "Learning")));
        public static final Class SingleTarget =
                new Class(m_model.createResource(String.format(_NS, "SingleTarget")));
        public static final Class MultipleTargets =
                new Class(m_model.createResource(String.format(_NS, "MultipleTargets")));
        public static final Class MSDMTox =
                new Class(m_model.createResource(String.format(_NS, "MSDMTox")));
    };
}

