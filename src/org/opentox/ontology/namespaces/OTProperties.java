package org.opentox.ontology.namespaces;


import com.hp.hpl.jena.rdf.model.*;

/**
 * Namespaces used in this project.
 * @author OpenTox - http://www.opentox.org
 * @author Sopasakis Pantelis
 * @author Sarimveis Harry
 */
public class OTProperties extends AbsOntClass {

    private static final long serialVersionUID = 8498366532297574L;

    
    public static final Property dataEntry =
            m_model.createProperty(String.format(_NS_OT, "dataEntry"));
    public static final Property compound =
            m_model.createProperty(String.format(_NS_OT, "compound"));
    public static final Property feature =
            m_model.createProperty(String.format(_NS_OT, "feature"));
    public static final Property values =
            m_model.createProperty(String.format(_NS_OT, "values"));
    public static final Property hasSource =
            m_model.createProperty(String.format(_NS_OT, "hasSource"));
    public static final Property conformer =
            m_model.createProperty(String.format(_NS_OT, "conformer"));
    @Deprecated
    public static final Property isA =
            m_model.createProperty(String.format(_NS_OT, "isA"));
    public static final Property model =
            m_model.createProperty(String.format(_NS_OT, "model"));
    public static final Property report =
            m_model.createProperty(String.format(_NS_OT, "report"));
    public static final Property algorithm =
            m_model.createProperty(String.format(_NS_OT, "algorithm"));
    public static final Property dependentVariables =
            m_model.createProperty(String.format(_NS_OT, "dependentVariables"));
    public static final Property independentVariables =
            m_model.createProperty(String.format(_NS_OT, "independentVariables"));
    public static final Property predictedVariables =
            m_model.createProperty(String.format(_NS_OT, "predictedVariables"));
    public static final Property trainingDataset =
            m_model.createProperty(String.format(_NS_OT, "trainingDataset"));
    public static final Property validationReport =
            m_model.createProperty(String.format(_NS_OT, "validationReport"));
    public static final Property validation =
            m_model.createProperty(String.format(_NS_OT, "validation"));
    public static final Property hasValidationInfo =
            m_model.createProperty(String.format(_NS_OT, "hasValidationInfo"));
    public static final Property validationModel =
            m_model.createProperty(String.format(_NS_OT, "validationModel"));
    public static final Property validationPredictionDataset =
            m_model.createProperty(String.format(_NS_OT, "validationPredictionDataset"));
    public static final Property validationTestDataset =
            m_model.createProperty(String.format(_NS_OT, "validationTestDataset"));
    public static final Property value =
            m_model.createProperty(String.format(_NS_OT, "value"));
    public static final Property units =
            m_model.createProperty(String.format(_NS_OT, "units"));
    public static final Property has3Dstructure =
            m_model.createProperty(String.format(_NS_OT, "has3Dstructure"));
    public static final Property hasStatus =
            m_model.createProperty(String.format(_NS_OT, "hasStatus"));
    public static final Property paramScope =
            m_model.createProperty(String.format(_NS_OT, "paramScope"));
    public static final Property paramValue =
            m_model.createProperty(String.format(_NS_OT, "paramValue"));
    public static final Property statisticsSupported =
            m_model.createProperty(String.format(_NS_OT, "statisticsSupported"));
    public static final Property parameters =
            m_model.createProperty(String.format(_NS_OT, "parameters"));

    
}
