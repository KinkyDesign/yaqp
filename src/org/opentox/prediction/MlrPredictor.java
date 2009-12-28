package org.opentox.prediction;

import java.io.FileNotFoundException;
import java.io.File;
import java.io.FileInputStream;
import java.net.URI;
import java.net.URISyntaxException;
import org.opentox.resource.AbstractResource.Directories;
import org.opentox.algorithm.dataprocessing.DataCleanUp;
import org.opentox.error.ErrorSource;
import org.opentox.rdf.Dataset;
import org.opentox.rdf.Model;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import weka.classifiers.pmml.consumer.PMMLClassifier;
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
public class MlrPredictor extends ErrorSource implements Predictor {


    /**
     * Constructor.
     */
    public MlrPredictor(){

    }


    /**
     * An implementation of the corresponding method in {@link Predictor } for
     * MLR models.
     * @param form The posted parameters as an instance of {@link org.restlet.data.Form }
     * @param model_id The id of the model to be used for the prediction.
     * @return Representation of the prediction result which is the URI of the generated dataset
     * that contains the predicted variables or some error representation instead.
     */
    @Override
    public Representation predict(Form form, String model_id) {

        final Status clientBadRequest = Status.CLIENT_ERROR_BAD_REQUEST,
                notFound = Status.CLIENT_ERROR_NOT_FOUND;

        // the resulting representaiton.
        Representation rep = null;
        Model model = new Model();
        Dataset dataset = new Dataset();

        try {


            // the model as an instance of org.opentox.rdf.Model (model)
            FileInputStream in = new FileInputStream(Directories.modelRdfDir + "/" + model_id);
            model = new Model(in);

            // The dataset as an instance of org.opentox.rdf.Dataset (dataset)
            URI d_set = new URI(form.getFirstValue("dataset_uri"));
            dataset = new Dataset(d_set);

            // The dataset content as an instance of weka.core.Instances (testData):
            Instances testData = dataset.getWekaDatasetForTraining(null, false);
          

            // check the compatibility of the model with the testData:
            assert (model.compatibleWith(testData));

            // The target variable of the model:
            String target = model.getDependentFeatureUri();

            // set the target:
            testData.setClass(testData.attribute(target));


            // Create an instance of PMMLModel
            PMMLModel mlrModel = PMMLFactory.getPMMLModel(
                    new File(Directories.modelPmmlDir + "/" + model_id));


            if (mlrModel instanceof PMMLClassifier) {

                PMMLClassifier classifier = (PMMLClassifier) mlrModel;


                /**
                 * TODO: Do not return the list of predictions to the client! Instead
                 * create a new dataset and post it to a dataset service.
                 */
                String predictions = "";

                for (int i = 0; i < testData.numInstances(); i++) {
                    predictions = predictions + classifier.classifyInstance(testData.instance(i)) + "\n";
                }

                rep = new StringRepresentation(predictions, MediaType.TEXT_PLAIN);
            }

        } catch (AssertionError ass) {
            errorRep.append(ass, "This model is incompatible with the specified dataset!", clientBadRequest);
        } catch (URISyntaxException exc) {
            errorRep.append(exc, "Malformed URI for dataset_uri parameter!", clientBadRequest);
        } catch (FileNotFoundException exc) {
            errorRep.append(exc, "No such model!", notFound);
        } catch (Exception exc) { // thrown by dataset.getWekaDatasetForTraining(null, false);
            errorRep.append(exc, "This dataset cannot be successfully parsed due to syntax errors!",
                    clientBadRequest);
        } finally{
            errorRep.append(model.errorRep);
            errorRep.append(dataset.errorRep);
        }
        
    return errorRep.getErrorLevel()==0 ? rep : errorRep;
}

}
