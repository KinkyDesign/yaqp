package org.opentox.rdf;

import java.io.FileNotFoundException;
import org.opentox.namespaces.OT;
import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntResource;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.SimpleSelector;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;
import com.hp.hpl.jena.vocabulary.DC;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.opentox.resource.AbstractResource;
import org.opentox.resource.AbstractResource.URIs;
import org.opentox.algorithm.AlgorithmParameter;
import org.restlet.data.Response;
import org.restlet.data.Status;
import weka.core.Attribute;
import weka.core.Instances;

/**
 * This class is used to parse and generate RDF representations of Models.
 * @author OpenTox - http://www.opentox.org/
 * @author Sopasakis Pantelis
 * @author Sarimveis Harry
 * @version 1.3.3 (Last update: Dec 23, 2009)
 */
public class Model extends RDFHandler  implements Serializable{

    private static final long serialVersionUID = -4754250023818796913L;

    public Model() {
        super();
    }

    /**
     * Initialized a new instance of {@link Model } reading its content
     * from an input stream which can be a {@link FileInputStream } or an 
     * InputStream pointing to a web Resource.
     * @see HttpURLConnection#getInputStream() 
     * @param in InputStream for reading the content of the Model.
     */
    public Model(InputStream in) {
        super(in);
    }

    /**
     * Returns the set of all features in the Model (RDF representation) including
     * dependent, independent and predicted ones.
     * @return set of all features or the model.
     */
    public Set<String> setOfFeatures() {
        Set<String> set = new HashSet<String>();
        OntClass myClass = OT.Class.Feature.getOntClass(jenaModel);
        ExtendedIterator<? extends OntResource> featureIterator = myClass.listInstances();
        while (featureIterator.hasNext()) {
            set.add(featureIterator.next().getURI());
        }
        return set;
    }


    /**
     * The set of independent variables of the model.
     * @return set of URIs
     */
    public Set<String> getSetOfIndependentFeatures(){
        Set<String> set = new HashSet<String>();
        StmtIterator stmt_iter = jenaModel.listStatements(
                new SimpleSelector(null, OT.independentVariables, (Resource) null));
        while (stmt_iter.hasNext()){
            Statement stmt = stmt_iter.next();
            set.add(stmt.getObject().as(Resource.class).getURI());
        }
        return set;
    }

    /**
     * Returns the dependent features of the model.
     * @return dependent feature URI as a String.
     */
    public String getDependentFeatureUri(){
        String dependentFeature = null;
        StmtIterator stmt_iter = jenaModel.listStatements(
                new SimpleSelector(null, OT.dependentVariables, (Resource) null));
        if (stmt_iter.hasNext()){
            dependentFeature = stmt_iter.next().getObject().as(Resource.class).getURI();
        }
        return dependentFeature;
    }

    /**
     * Get the URI of the predicted feature of the model.
     * @return URI of predicted feature.
     */
    public String getPredictedFeatureUri() {
        String predictedFeature = null;
        StmtIterator stmt_iter = jenaModel.listStatements(
                new SimpleSelector(null, OT.predictedVariables, (Resource) null));
        if (stmt_iter.hasNext()){
            predictedFeature = stmt_iter.next().getObject().as(Resource.class).getURI();
        }
        return predictedFeature;
    }

    /**
     * Creates the RDF representation for an OpenTox model given its name, the uri
     * of the dataset used to train it, its target feature, the Data and a List of
     * tuning parameters for the training algorithm. The RDF document is built according
     * to the specification of OpenTox API (v 1.1).
     * @param model_id The id of the model (Integer).
     * @param dataseturi The URI of the dataset used to train the model.
     * @param targeturi The URI of the target feature, i.e. the dependent variable of
     * the model.
     * @param data The Instances object containing the training data.
     * @param algorithmParameters A List of the tuning parameters of the algorithm.
     * @param out The output stream used to store the model.
     */
    public void createModel(String model_id, String dataseturi, Instances data,
            List<AlgorithmParameter> algorithmParameters, String AlgorithmURI, OutputStream out) {
        try {
            jenaModel = org.opentox.namespaces.Namespace.createModel();

            OT.Class.Dataset.createOntClass(jenaModel);
            OT.Class.Feature.createOntClass(jenaModel);
            OT.Class.Algorithm.createOntClass(jenaModel);
            OT.Class.Parameter.createOntClass(jenaModel);
            OT.Class.Model.createOntClass(jenaModel);


            Individual ot_model = jenaModel.createIndividual(
                    URIs.modelURI + "/" + model_id, OT.Class.Model.getOntClass(jenaModel));
            ot_model.addProperty(jenaModel.createAnnotationProperty(DC.title.getURI()), "Model " + model_id);
            ot_model.addProperty(jenaModel.createAnnotationProperty(DC.identifier.getURI()), URIs.modelURI + "/" + model_id);
            ot_model.addProperty(jenaModel.createAnnotationProperty(DC.creator.getURI()), URIs.baseURI);
            ot_model.addProperty(jenaModel.createAnnotationProperty(DC.date.getURI()), java.util.GregorianCalendar.getInstance().getTime().toString());
            ot_model.addProperty(jenaModel.createAnnotationProperty(OT.isA.getURI()), OT.Class.Model.getResource());

            //the algorithm
            Individual algorithm = jenaModel.createIndividual(
                    AlgorithmURI, OT.Class.Algorithm.getOntClass(jenaModel));
            ot_model.addProperty(jenaModel.createAnnotationProperty(OT.algorithm.getURI()), algorithm);

            //assign training dataset (same as above)
            Individual dataset = jenaModel.createIndividual(dataseturi.toString(), jenaModel.createOntResource(OT.Class.Dataset.getURI()));
            ot_model.addProperty(jenaModel.createAnnotationProperty(OT.trainingDataset.getURI()), dataset);

            // Add all parameters:
            Individual iparam;
            String targeturi = null;
            for (int i = 0; i < algorithmParameters.size(); i++) {
                iparam = jenaModel.createIndividual(OT.Class.Parameter.getOntClass(jenaModel));
                iparam.addProperty(jenaModel.createAnnotationProperty(DC.title.getURI()), algorithmParameters.get(i).paramName);
                iparam.addLiteral(jenaModel.createAnnotationProperty(OT.paramValue.getURI()), jenaModel.createTypedLiteral(
                        algorithmParameters.get(i).paramValue.toString(),
                        algorithmParameters.get(i).dataType));
                iparam.addLiteral(jenaModel.createAnnotationProperty(OT.paramScope.getURI()), jenaModel.createTypedLiteral(algorithmParameters.get(i).paramScope,
                        XSDDatatype.XSDstring));
                ot_model.addProperty(jenaModel.createAnnotationProperty(OT.parameters.getURI()), iparam);
                if (algorithmParameters.get(i).paramName.equalsIgnoreCase("target")) {
                    targeturi = (String) algorithmParameters.get(i).paramValue;
                }
            }


            /**
             * Generate a new feature in AMBIT ( http://ambit.uni-plovdiv.bg:8080/ambit2/feature )....
             */
            Feature featurec = new Feature();
            Response response = featurec.createNewFeature("http://someserver.com/feature/101", new URI("http://ambit.uni-plovdiv.bg:8080/ambit2/feature"));

            System.out.println(response.getEntity());

            if (!(response.getStatus().getCode() == 200)) {
                System.out.println(targeturi);
                System.out.println(response.getStatus().getCode());
                Exception failure = new Exception("Feature Generation Failure!");
                errorRep.append(failure, "Could not generate a new feaure in AMBIT server",
                        Status.SERVER_ERROR_BAD_GATEWAY);
                throw failure;
            }



            Individual feature = null;
            for (int i = 0; i < data.numAttributes(); i++) {
                // for the target attribute...
                if (targeturi.toString().equals(data.attribute(i).name())) {
                    feature = jenaModel.createIndividual(targeturi.toString(),
                            OT.Class.Feature.getOntClass(jenaModel));
                    ot_model.addProperty(jenaModel.createAnnotationProperty(OT.dependentVariables.getURI()), feature);
                    //Add the predicted variable...
                    {
                        Individual predicted = jenaModel.createIndividual(response.getEntity().getText().replaceAll("\\s\\s+|\\n|\\r", ""),
                                OT.Class.Feature.getOntClass(jenaModel));
                        ot_model.addProperty(jenaModel.createAnnotationProperty(OT.predictedVariables.getURI()), predicted);
                    }
                } else {
                    feature = jenaModel.createIndividual(data.attribute(i).name(),
                            OT.Class.Feature.getOntClass(jenaModel));
                    ot_model.addProperty(jenaModel.createAnnotationProperty(OT.independentVariables.getURI()), feature);
                }
            }
            jenaModel.write(out);

        } catch (Exception ex) {
            errorRep.append(ex, "Severe Error while parsing a Model!", Status.SERVER_ERROR_INTERNAL);
        }

    }

    
    
    public static void main(String[] args) throws FileNotFoundException{
        Model mod = new Model(new FileInputStream(AbstractResource.Directories.modelRdfDir+"/233"));
        Set<String> ind_set = mod.getSetOfIndependentFeatures();
        Iterator<String> it = ind_set.iterator();
        while (it.hasNext()){
            System.out.println(it.next());
        }
    }

    /**
     * Check the assertion that a certain weka.core.Instances object is
     * compatible with this model in terms of having proper features. In fact
     * the set of attributes of testData must be a hyperset of the dependent
     * attributes of the model. In plain english, the testData set should provide
     * at least the information needed.
     * @param testData
     * @return
     */
    public boolean compatibleWith(Instances testData) {
        Set<String> modelIndependentFeatures = getSetOfIndependentFeatures();
        Set<String> testDataIndependentFeatures = new HashSet<String>();

        for (int j=0;j<testData.numAttributes();j++){
            Attribute current = testData.attribute(j);
            if (testData.classAttribute().equals(current)){
                testDataIndependentFeatures.add(current.name());
            }
        }
        return testDataIndependentFeatures.containsAll(modelIndependentFeatures);
    }


}
