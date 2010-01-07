package org.opentox.namespaces;

import com.hp.hpl.jena.rdf.model.Resource;

public class OTClass extends AbsOntClass {

    public OTClass(Resource resource) {
        super(resource);
    }
    public static final Class Compound =
            new Class(m_model.createResource(String.format(_NS, "Compound")));
    public static final Class Conformer =
            new Class(m_model.createResource(String.format(_NS, "Conformer")));
    public static final Class Dataset =
            new Class(m_model.createResource(String.format(_NS, "Dataset")));
    public static final Class DataEntry =
            new Class(m_model.createResource(String.format(_NS, "DataEntry")));
    public static final Class Feature =
            new Class(m_model.createResource(String.format(_NS, "Feature")));
    public static final Class FeatureValue =
            new Class(m_model.createResource(String.format(_NS, "FeatureValue")));
    public static final Class Algorithm =
            new Class(m_model.createResource(String.format(_NS, "Algorithm")));
    public static final Class Model =
            new Class(m_model.createResource(String.format(_NS, "Model")));
    public static final Class Validation =
            new Class(m_model.createResource(String.format(_NS, "Validation")));
    public static final Class ValidationInfo =
            new Class(m_model.createResource(String.format(_NS, "ValidationInfo")));
    public static final Class Parameter =
            new Class(m_model.createResource(String.format(_NS, "Parameter")));
}
