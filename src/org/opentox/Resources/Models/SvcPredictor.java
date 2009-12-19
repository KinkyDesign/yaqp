package org.opentox.Resources.Models;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.opentox.Resources.AbstractResource.Directories;
import org.opentox.Resources.Algorithms.Preprocessing;
import org.opentox.ontology.Dataset;
import org.opentox.ontology.Model;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import weka.classifiers.Classifier;
import weka.classifiers.functions.LibSVM;
import weka.classifiers.functions.SMO;
import weka.classifiers.trees.J48;
import weka.core.Instances;
import weka.core.SerializationHelper;

/**
 *
 * @author chung
 */
public class SvcPredictor implements Predictor{

    public Representation predict(Form form, String model_id) {
        Representation rep = null;
        try {
            URI d_set = new URI(form.getFirstValue("dataset_uri"));
            Dataset wekaData = new Dataset(d_set);
            Instances testData = wekaData.getWekaDataset(null, false);
            Preprocessing.removeStringAtts(testData);


            /****
             * TODO: Fix the following line:
             */
            testData.setClass(testData.attribute("http://sth.com/feature/1"));



            /**
             * Check if all features of the model are contained in the features of the dataset.
             */
            if (wekaData.setOfFeatures().
                    containsAll((Collection<?>) new Model(
                    new FileInputStream(Directories.modelRdfDir + "/" + model_id)).setOfFeatures())) {


                    try {
                        Classifier wekaModel = (SMO) SerializationHelper.read(Directories.modelWekaDir + "/" + model_id);
                        String predictions = "";
                        for (int k = 0; k < testData.numInstances(); k++) {
                            predictions = predictions + "Instance: " + testData.instance(k) + " ----> "
                                    + wekaModel.classifyInstance(testData.instance(k)) + "\n";
                        }
                        rep = new StringRepresentation(predictions, MediaType.TEXT_PLAIN);

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
