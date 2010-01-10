package org.opentox.ontology.namespaces;

import com.hp.hpl.jena.rdf.model.Resource;

public class OTClass extends AbsOntClass {

    public OTClass(Resource resource) {
        super(resource);
    }
    public static final Class Compound =
            new Class(m_model.createResource(String.format(_NS_OT, "Compound")));
    public static final Class Conformer =
            new Class(m_model.createResource(String.format(_NS_OT, "Conformer")));
    public static final Class Dataset =
            new Class(m_model.createResource(String.format(_NS_OT, "Dataset")));
    public static final Class DataEntry =
            new Class(m_model.createResource(String.format(_NS_OT, "DataEntry")));
    public static final Class Feature =
            new Class(m_model.createResource(String.format(_NS_OT, "Feature")));
    public static final Class FeatureValue =
            new Class(m_model.createResource(String.format(_NS_OT, "FeatureValue")));
    public static final Class Algorithm =
            new Class(m_model.createResource(String.format(_NS_OT, "Algorithm")));
    public static final Class Model =
            new Class(m_model.createResource(String.format(_NS_OT, "Model")));
    public static final Class Validation =
            new Class(m_model.createResource(String.format(_NS_OT, "Validation")));
    public static final Class ValidationInfo =
            new Class(m_model.createResource(String.format(_NS_OT, "ValidationInfo")));
    public static final Class Parameter =
            new Class(m_model.createResource(String.format(_NS_OT, "Parameter")));
}
