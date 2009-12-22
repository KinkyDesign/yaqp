package org.opentox.Resources.Models;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.SimpleSelector;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.opentox.Resources.AbstractResource.Directories;
import org.opentox.Resources.Algorithms.Preprocessing;
import org.opentox.Resources.ErrorSource;
import org.opentox.ontology.Dataset;
import org.opentox.ontology.Model;
import org.opentox.ontology.Namespace;
import org.opentox.ontology.OT;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import weka.classifiers.pmml.consumer.PMMLClassifier;
import weka.core.Attribute;
import weka.core.Instances;
import weka.core.pmml.PMMLFactory;
import weka.core.pmml.PMMLModel;

/**
 * Applies a dataset on an MLR model to predict a feature.
 * @author OpenTox - http://www.opentox.org/
 * @author Sopasakis Pantelis
 * @author Sarimveis Harry
 * @version 1.3.3 (Last update: Dec 20, 2009)
 */
public class MlrPredictor implements Predictor {

    private Status internalStatus = Status.SUCCESS_ACCEPTED;

    @Override
    public Representation predict(Form form, String model_id) {
        Representation rep = null;

        try {
            URI d_set = new URI(form.getFirstValue("dataset_uri"));            
            Dataset wekaData = new Dataset(d_set);
            Instances testData = wekaData.getWekaDatasetForTraining(null, false);
            Preprocessing.removeStringAtts(testData);


            /**
             * Set the class attribute in the dataset as the dependent variable of the model:
             */
            FileInputStream in = new FileInputStream(Directories.modelRdfDir + "/" + model_id);
            OntModel jenaModel = Namespace.createModel();
            jenaModel.read(in, null);
            StmtIterator stmtIt = jenaModel.listStatements(new SimpleSelector(null, OT.dependentVariables, (Resource) null));
            if (stmtIt.hasNext()) {
                testData.setClass(testData.attribute(stmtIt.next().getObject().as(Resource.class).getURI()));
            }            


            /**
             * Check if all features of the model are contained in the features of the dataset.
             */
            if (wekaData.setOfFeatures().
                    containsAll((Collection<?>) new Model(
                      new FileInputStream(Directories.modelRdfDir + "/" + model_id)).setOfFeatures())) {

                try {
                    PMMLModel mlrModel = PMMLFactory.getPMMLModel(
                            new File(Directories.modelPmmlDir + "/" + model_id));
                    if (mlrModel instanceof PMMLClassifier) {
                        Attribute att = testData.attribute(
                                mlrModel.getMiningSchema().getFieldsAsInstances().classAttribute().name());
                        testData.setClass(att);
                        PMMLClassifier classifier = (PMMLClassifier) mlrModel;
                        // list of predicted values...
                        String predictions = "";
                        for (int i = 0; i < testData.numInstances(); i++) {
                            predictions = predictions + classifier.classifyInstance(testData.instance(i)) + "\n";
                        }
                        rep = new StringRepresentation(predictions, MediaType.TEXT_PLAIN);
                    }
                } catch (Exception ex) {
                    Logger.getLogger(ModelResource.class.getName()).log(Level.SEVERE, null, ex);
                }

            }

        } catch (IOException ex) {
            Logger.getLogger(ModelResource.class.getName()).log(Level.SEVERE, null, ex);
        } catch (URISyntaxException ex) {
            Logger.getLogger(ModelResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rep;
    }



    
}
