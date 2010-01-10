package org.opentox.ontology.namespaces;


/**
 *
 * @author OpenTox - http://www.opentox.org
 * @author Sopasakis Pantelis
 * @author Sarimveis Harry
 */
public class AlgorithmTypes extends AbsOntClass{

        public static final Class ClassificationEagerMultipleTargets =
                new Class(m_model.createResource(String.format(_NS_AlgorithmTypes, "ClassificationEagerMultipleTargets")));
        public static final Class RegressionEagerMultipleTargets =
                new Class(m_model.createResource(String.format(_NS_AlgorithmTypes, "RegressionEagerMultipleTargets")));
        public static final Class RegressionEagerSingleTarget =
                new Class(m_model.createResource(String.format(_NS_AlgorithmTypes, "RegressionEagerSingleTarget")));
        public static final Class ClassificationEagerSingleTarget =
                new Class(m_model.createResource(String.format(_NS_AlgorithmTypes, "ClassificationEagerSingleTarget")));
        public static final Class EagerLearning =
                new Class(m_model.createResource(String.format(_NS_AlgorithmTypes, "EagerLearning")));
        public static final Class Regression =
                new Class(m_model.createResource(String.format(_NS_AlgorithmTypes, "Regression")));
        public static final Class Supervised =
                new Class(m_model.createResource(String.format(_NS_AlgorithmTypes, "Supervised")));
        public static final Class Learning =
                new Class(m_model.createResource(String.format(_NS_AlgorithmTypes, "Learning")));
        public static final Class SingleTarget =
                new Class(m_model.createResource(String.format(_NS_AlgorithmTypes, "SingleTarget")));
        public static final Class MultipleTargets =
                new Class(m_model.createResource(String.format(_NS_AlgorithmTypes, "MultipleTargets")));
        public static final Class MSDMTox =
                new Class(m_model.createResource(String.format(_NS_AlgorithmTypes, "MSDMTox")));
        public static final Class Preprocessing =
                new Class(m_model.createResource(String.format(_NS_AlgorithmTypes, "Preprocessing")));
        public static final Class DataCleanup =
                new Class(m_model.createResource(String.format(_NS_AlgorithmTypes, "DataCleanup")));
        public static final Class DescriptorCalculation =
                new Class(m_model.createResource(String.format(_NS_AlgorithmTypes, "DescriptorCalculation")));
        public static final Class Normalization =
                new Class(m_model.createResource(String.format(_NS_AlgorithmTypes, "Normalization")));
        public static final Class Unsupervised =
                new Class(m_model.createResource(String.format(_NS_AlgorithmTypes, "Unsupervised")));
        public static final Class FeatureSelectionSupervised =
                new Class(m_model.createResource(String.format(_NS_AlgorithmTypes, "FeatureSelectionSupervised")));
        public static final Class FeatureSelectionUnsupervised =
                new Class(m_model.createResource(String.format(_NS_AlgorithmTypes, "FeatureSelectionUnsupervised")));
        public static final Class AlgorithmType =
                new Class(m_model.createResource(String.format(_NS_AlgorithmTypes, "AlgorithmType")));






    
}

