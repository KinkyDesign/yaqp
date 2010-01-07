package org.opentox.prediction;

import org.opentox.interfaces.IPredictor;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.File;
import java.io.FileInputStream;
import java.net.URI;
import java.net.URISyntaxException;
import org.opentox.resource.OTResource.Directories;
import org.opentox.error.ErrorSource;
import org.opentox.ontology.rdf.Dataset;
import org.opentox.ontology.rdf.Model;
import org.opentox.resource.OTResource.URIs;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import weka.classifiers.pmml.consumer.PMMLClassifier;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
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
public class MlrPredictor extends ErrorSource implements IPredictor {

    /**
     * Constructor.
     */
    public MlrPredictor() {
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
        Dataset test_dataset = new Dataset();

        try {


            // the model as an instance of org.opentox.rdf.Model (model)
            FileInputStream in = new FileInputStream(Directories.modelRdfDir + "/" + model_id);
            model = new Model(in);

            // The dataset as an instance of org.opentox.rdf.Dataset (dataset)
            URI d_set = new URI(form.getFirstValue("dataset_uri"));
            test_dataset = new Dataset(d_set);

            // The dataset content as an instance of weka.core.Instances (testData):
            Instances testData = test_dataset.getInstaces(null, false);


            // check the compatibility of the model with the testData:
            assert (model.compatibleWith(testData));

            // The target variable of the model:
            String target = model.getDependentFeatureUri();

            // set the target:
            testData.setClass(testData.attribute(target));

            // Define the predicted dataset:
            double[] vals = new double[2];
            vals[0] = Instance.missingValue();
            vals[1] = Instance.missingValue();

            FastVector predictedDS_atts = new FastVector();
            Attribute compound_uriAtt = new Attribute("compound_uri", (FastVector) null);
            Attribute predictedAtt = new Attribute(model.getPredictedFeatureUri());
            predictedDS_atts.addElement(compound_uriAtt);
            predictedDS_atts.addElement(predictedAtt);
            Instances predictedData = new Instances(form.getFirstValue("dataset_uri")
                    +" ---> "+URIs.modelURI+"/"+model_id, predictedDS_atts, 0);



            // Create an instance of PMMLModel
            final PMMLModel mlrModel = PMMLFactory.getPMMLModel(
                    new File(Directories.modelPmmlDir + "/" + model_id));


            if (mlrModel instanceof PMMLClassifier) {

                final PMMLClassifier classifier = (PMMLClassifier) mlrModel;


                final int indexOfCompoundUri = predictedData.attribute("compound_uri").index();
                final int indexOfPredictedFeature = predictedData.attribute(model.getPredictedFeatureUri()).index();
                Instance current = null;
                Instance temp = null;

                for (int k = 0; k < testData.numInstances(); k++) {
                    current = testData.instance(k);
                    if (!current.hasMissingValue()) {
                        vals = new double[2];
                        vals[indexOfCompoundUri] =
                                predictedData.attribute(indexOfCompoundUri).
                                addStringValue(current.stringValue(indexOfCompoundUri));
                        vals[indexOfPredictedFeature] =
                                classifier.classifyInstance(current);

                        temp = new Instance(1.0, vals);

                        if (predictedData.checkInstance(temp)) {
                            predictedData.add(temp);
                        }
                    }
                }

               Dataset predictedDataset = test_dataset.populateDataset(predictedData);
               
               ByteArrayOutputStream out = new ByteArrayOutputStream();
               predictedDataset.jenaModel.write(out);
               rep = new StringRepresentation(out.toString() + "\n", MediaType.TEXT_PLAIN);
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
        } finally {
            errorRep.append(model.errorRep);
            errorRep.append(test_dataset.errorRep);
        }

        return errorRep.getErrorLevel() == 0 ? rep : errorRep;
    }
}
