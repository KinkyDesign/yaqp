package org.opentox.namespaces;


import com.hp.hpl.jena.rdf.model.*;

/**
 * Namespaces used in this project.
 * @author OpenTox - http://www.opentox.org
 * @author Sopasakis Pantelis
 * @author Sarimveis Harry
 */
public abstract class OTProperties extends Namespace {

    private static final long serialVersionUID = 8498366532297574L;

    
    public static final Property dataEntry =
            m_model.createProperty(String.format(_NS, "dataEntry"));
    public static final Property compound =
            m_model.createProperty(String.format(_NS, "compound"));
    public static final Property feature =
            m_model.createProperty(String.format(_NS, "feature"));
    public static final Property values =
            m_model.createProperty(String.format(_NS, "values"));
    public static final Property hasSource =
            m_model.createProperty(String.format(_NS, "hasSource"));
    public static final Property conformer =
            m_model.createProperty(String.format(_NS, "conformer"));
    public static final Property isA =
            m_model.createProperty(String.format(_NS, "isA"));
    public static final Property model =
            m_model.createProperty(String.format(_NS, "model"));
    public static final Property report =
            m_model.createProperty(String.format(_NS, "report"));
    public static final Property algorithm =
            m_model.createProperty(String.format(_NS, "algorithm"));
    public static final Property dependentVariables =
            m_model.createProperty(String.format(_NS, "dependentVariables"));
    public static final Property independentVariables =
            m_model.createProperty(String.format(_NS, "independentVariables"));
    public static final Property predictedVariables =
            m_model.createProperty(String.format(_NS, "predictedVariables"));
    public static final Property trainingDataset =
            m_model.createProperty(String.format(_NS, "trainingDataset"));
    public static final Property validationReport =
            m_model.createProperty(String.format(_NS, "validationReport"));
    public static final Property validation =
            m_model.createProperty(String.format(_NS, "validation"));
    public static final Property hasValidationInfo =
            m_model.createProperty(String.format(_NS, "hasValidationInfo"));
    public static final Property validationModel =
            m_model.createProperty(String.format(_NS, "validationModel"));
    public static final Property validationPredictionDataset =
            m_model.createProperty(String.format(_NS, "validationPredictionDataset"));
    public static final Property validationTestDataset =
            m_model.createProperty(String.format(_NS, "validationTestDataset"));
    public static final Property value =
            m_model.createProperty(String.format(_NS, "value"));
    public static final Property units =
            m_model.createProperty(String.format(_NS, "units"));
    public static final Property has3Dstructure =
            m_model.createProperty(String.format(_NS, "has3Dstructure"));
    public static final Property hasStatus =
            m_model.createProperty(String.format(_NS, "hasStatus"));
    public static final Property paramScope =
            m_model.createProperty(String.format(_NS, "paramScope"));
    public static final Property paramValue =
            m_model.createProperty(String.format(_NS, "paramValue"));
    public static final Property statisticsSupported =
            m_model.createProperty(String.format(_NS, "statisticsSupported"));
    public static final Property parameters =
            m_model.createProperty(String.format(_NS, "parameters"));

    
}
