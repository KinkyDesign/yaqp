package org.opentox.ontology;

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntResource;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;
import com.hp.hpl.jena.vocabulary.DC;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.opentox.Resources.AbstractResource;
import org.opentox.Resources.AbstractResource.URIs;
import org.opentox.Resources.Algorithms.AlgorithmParameter;
import org.restlet.data.Status;
import weka.core.Instances;

/**
 *
 * @author Sopasakis Pantelis
 */
public class Model extends RDFParser{

    private static final long serialVersionUID = -4754250023818796913L;
     

    public Model(){
        super();
    }


    public Model(InputStream in){
        super(in);
    }



    /**
     * Returns the set of features in the Model (RDF representation).
     * @return
     */
    public Set<String> setOfFeatures(){
        Set<String> set = new HashSet<String>();
        OntClass myClass = OT.Class.Feature.getOntClass(jenaModel);
        ExtendedIterator<? extends OntResource> featureIterator = myClass.listInstances() ;
        while (featureIterator.hasNext()){
            set.add(featureIterator.next().getURI());
        }
        return set;
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
    public void createModel(String model_id, String dataseturi, String targeturi, Instances data,
            List<AlgorithmParameter> algorithmParameters, String AlgorithmURI, OutputStream out){
            try {
            jenaModel = org.opentox.ontology.Namespace.createModel();

            OT.Class.Dataset.createOntClass(jenaModel);
            OT.Class.Feature.createOntClass(jenaModel);
            OT.Class.Algorithm.createOntClass(jenaModel);
            OT.Class.Parameter.createOntClass(jenaModel);
            OT.Class.Model.createOntClass(jenaModel);


            Individual ot_model = jenaModel.createIndividual(
            URIs.modelURI + "/" + model_id, OT.Class.Model.getOntClass(jenaModel));
            ot_model.addProperty(jenaModel.createAnnotationProperty(DC.title.getURI()), "Model " + model_id);
            ot_model.addProperty(jenaModel.createAnnotationProperty(DC.identifier.getURI()), URIs.modelURI + "/" + model_id);
            ot_model.addProperty(jenaModel.createAnnotationProperty(DC.creator.getURI()), AbstractResource.baseURI);
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
                    for (int i=0;i<algorithmParameters.size();i++){
                        iparam = jenaModel.createIndividual(OT.Class.Parameter.getOntClass(jenaModel));
                        iparam.addProperty(jenaModel.createAnnotationProperty(DC.title.getURI()), algorithmParameters.get(i).paramName);
                        iparam.addLiteral(jenaModel.createAnnotationProperty(OT.paramValue.getURI()), jenaModel.createTypedLiteral(
                                algorithmParameters.get(i).paramValue.toString(),
                                algorithmParameters.get(i).dataType));
                        iparam.addLiteral(jenaModel.createAnnotationProperty(OT.paramScope.getURI()), jenaModel.
                                createTypedLiteral(algorithmParameters.get(i).paramScope,
                                XSDDatatype.XSDstring));
                        ot_model.addProperty(jenaModel.createAnnotationProperty(OT.parameters.getURI()), iparam);
                    }

            Individual feature = null;

            for (int i = 0; i < data.numAttributes(); i++) {
                // for the target attribute...
                if (targeturi.toString().equals(data.attribute(i).name())) {
                    feature = jenaModel.createIndividual(targeturi.toString(),
                            OT.Class.Feature.getOntClass(jenaModel));
                    ot_model.addProperty(jenaModel.createAnnotationProperty(OT.dependentVariables.getURI()), feature);
                } else {
                    feature = jenaModel.createIndividual(data.attribute(i).name(),
                            OT.Class.Feature.getOntClass(jenaModel));
                    ot_model.addProperty(jenaModel.createAnnotationProperty(OT.independentVariables.getURI()), feature);
                }
            }
            jenaModel.write(out);

        } catch (Exception ex) {
            internalStatus = Status.SERVER_ERROR_INTERNAL;
        }

    }

    

}
