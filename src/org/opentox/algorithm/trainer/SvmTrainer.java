package org.opentox.algorithm.trainer;

import org.opentox.algorithm.ConstantParameters;
import org.opentox.algorithm.AlgorithmParameter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.opentox.resource.AbstractResource.Directories;
import org.opentox.resource.AbstractResource.URIs;
import org.opentox.algorithm.dataprocessing.DataCleanUp;
import org.opentox.algorithm.trainer.AbstractTrainer.Regression;
import org.opentox.error.ErrorRepresentation;
import org.opentox.client.opentoxClient;
import org.opentox.database.ModelsTable;
import org.opentox.ontology.meta.ModelMeta;
import org.opentox.ontology.rdf.Dataset;
import org.opentox.ontology.rdf.Model;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.ServerResource;
import weka.classifiers.Evaluation;
import weka.classifiers.functions.SVMreg;
import weka.classifiers.functions.supportVector.Kernel;
import weka.classifiers.functions.supportVector.PolyKernel;
import weka.classifiers.functions.supportVector.RBFKernel;
import weka.core.Instances;
import weka.core.converters.ArffSaver;

/**
 * Trainer for Support Vector Machine Regression Models.
 * @author OpenTox - http://www.opentox.org/
 * @author Sopasakis Pantelis
 * @author Sarimveis Harry
 * @version 1.3.3 (Last update: Dec 28, 2009)
 */
@Regression(name="SVM") public class SvmTrainer extends AbstractTrainer {

    private static final long serialVersionUID = -1522085945071581484L;
    private int i = 0;
    private double d = 0.0;
    /**
     * Tuning Parameters for the SVM algorithm.
     */
    private SvmParameters prm = new SvmParameters();
    /**
     * The id of the generated model.
     */
    private int model_id;
    /**
     * An Instances object used to store the data.
     */
    private volatile Instances dataInstances;

    /**
     * Constructor of the trainer.
     * @param form The posted data.
     */
    public SvmTrainer(Form form, ServerResource resource) {
        super(form, resource);
    }

    /**
     * Trains a new SVM model.
     * Stores a serialized version of the weka model and produces an RDF
     * representation which is also stored in a file.
     * @return representation of training result.
     */
    @Override
    public Representation train() {

        // 1. Check the parameters posted by the client:
        Representation representation = checkParameters();

        if (errorRep.getErrorLevel() == 0) {

            // 2. Remove all string attributes from the dataset:
            DataCleanUp.removeStringAtts(dataInstances);

            // 3. Lock an ID for the model:
            model_id = ModelsTable.INSTANCE.getModelsStack() + 1;

            // 4. Define the temporary arff file that will be used to store data
            //    and the path to the model file that will be created.
            long rand = new Random().nextInt() + System.currentTimeMillis();
            String temporaryArffFile = Directories.dataDir + "/" + rand + ".arff",
                    modelFile = Directories.modelWekaDir + "/" + model_id;

            try {
                assert (dataInstances != null);

                // 5. Save the dataset in a temporary ARFF file:
                ArffSaver dataSaver = new ArffSaver();
                dataSaver.setInstances(dataInstances);
                dataSaver.setDestination(new FileOutputStream(temporaryArffFile));
                dataSaver.writeBatch();

                // 6. Build the Regression Model:
                weka.classifiers.functions.SVMreg regressor = new SVMreg();
                String[] regressorOptions = {"-P", prm.epsilon,
                    "-T", prm.tolerance};

                Kernel svm_kernel = null;
                if (prm.kernel.equalsIgnoreCase("rbf")) {
                    RBFKernel rbf_kernel = new RBFKernel();
                    rbf_kernel.setGamma(Double.parseDouble(prm.gamma));
                    rbf_kernel.setCacheSize(Integer.parseInt(prm.cacheSize));
                    svm_kernel = rbf_kernel;
                } else if (prm.kernel.equalsIgnoreCase("polynomial")) {
                    PolyKernel poly_kernel = new PolyKernel();
                    poly_kernel.setExponent(Double.parseDouble(prm.degree));
                    poly_kernel.setCacheSize(Integer.parseInt(prm.cacheSize));
                    poly_kernel.setUseLowerOrder(true);
                    svm_kernel = poly_kernel;
                } else if (prm.kernel.equalsIgnoreCase("linear")) {
                    PolyKernel linear_kernel = new PolyKernel();
                    PolyKernel poly_kernel = new PolyKernel();
                    poly_kernel.setExponent((double) 1.0);
                    poly_kernel.setCacheSize(Integer.parseInt(prm.cacheSize));
                    poly_kernel.setUseLowerOrder(true);
                    svm_kernel = poly_kernel;
                }
                regressor.setKernel(svm_kernel);
                regressor.setOptions(regressorOptions);

                String[] generalOptions = {
                    "-c", Integer.toString(dataInstances.classIndex() + 1),
                    "-t", temporaryArffFile,
                    /// Save the model in the following directory
                    "-d", modelFile};

                Evaluation.evaluateModel(regressor, generalOptions);

                // Delete the temporary file:
                new File(temporaryArffFile).delete();


                if ((new File(modelFile).exists()) && (new File(modelFile).length() > 0)) {

                    /**
                     * 7. Generate the RDF representation of the model and
                     * store it in a file.
                     */
                    List<AlgorithmParameter> paramList = prm.getListOfParameters();

                    Model opentoxModel = new Model();

                    ModelMeta meta = new ModelMeta(
                            Integer.toString(model_id),
                            dataseturi.toString(),
                            dataInstances,
                            paramList,
                            URIs.svmAlgorithmURI);
                    opentoxModel.createModel(meta,
                            new FileOutputStream(Directories.modelRdfDir + "/" + model_id));
                    errorRep.append(opentoxModel.errorRep);

                    if (errorRep.getErrorLevel() == 0) {
                        // if status is OK(200), register the new model in the database and
                        // return the URI to the client.
                        representation = new StringRepresentation(URIs.modelURI + "/"
                                + ModelsTable.INSTANCE.registerNewModel(URIs.svmAlgorithmURI) + "\n");
                    }
                }
            } catch (AssertionError ex) {
                errorRep.append(ex, "Dataset id empty!", Status.CLIENT_ERROR_BAD_REQUEST);
            } catch (IOException ex) {
                errorRep.append(ex, "Communication/Connection to a remote server" +
                        " ended up unexpectedly: The server encountered an error " +
                        " while acting as a gateway!",
                        Status.SERVER_ERROR_BAD_GATEWAY);
            } catch (Exception ex) {
                errorRep.append(ex, "Some internal error occured!", Status.SERVER_ERROR_INTERNAL);
            }
        }

        return errorRep.getErrorLevel() == 0 ? representation : errorRep;

    }

    /**
     * Check the consistency of the POSTed svm parameters and assign default
     * values to the parameters that where not posted. The dataInstances are
     * updated according to the dataset uri.
     * @return representation of the errors that might occur during parameter
     * checking.
     */
    @Override
    public synchronized ErrorRepresentation checkParameters() {
        // Some initial definitions:
        final Status clientPostedWrongParametersStatus = Status.CLIENT_ERROR_BAD_REQUEST;


        /**
         * Assign default values to the parameters that where not
         * posted.
         */
        prm.kernel = form.getFirstValue("kernel");
        if (prm.kernel == null) {
            prm.kernel = ConstantParameters.KERNEL.paramValue.toString();
        }

        prm.cost = form.getFirstValue("cost");
        if (prm.cost == null) {
            prm.cost = ConstantParameters.COST.paramValue.toString();
        }
        prm.gamma = form.getFirstValue("gamma");
        if (prm.gamma == null) {
            prm.gamma = ConstantParameters.GAMMA.paramValue.toString();
        }
        prm.epsilon = form.getFirstValue("epsilon");
        if (prm.epsilon == null) {
            prm.epsilon = ConstantParameters.EPSILON.paramValue.toString();
        }
        prm.coeff0 = form.getFirstValue("coeff0");
        if (prm.coeff0 == null) {
            prm.coeff0 = ConstantParameters.COEFF0.paramValue.toString();
        }
        prm.degree = form.getFirstValue("degree");
        if (prm.degree == null) {
            prm.degree = ConstantParameters.DEGREE.paramValue.toString();
        }
        prm.tolerance = form.getFirstValue("tolerance");
        if (prm.tolerance == null) {
            prm.tolerance = ConstantParameters.TOLERANCE.paramValue.toString();
        }
        prm.cacheSize = form.getFirstValue("cacheSize");
        if (prm.cacheSize == null) {
            prm.cacheSize = Integer.toString(ConstantParameters.CACHESIZE.paramValue);
        }


        /**
         * Check the dataset_uri parameter.........
         */
        Thread checkDataset = new Thread() {

            @Override
            public void run() {

                String message = "";
                try {
                    dataseturi = new URI(form.getFirstValue("dataset_uri"));
                    dataseturi.toURL();
                    if (!(opentoxClient.IsMimeAvailable(dataseturi, MediaType.APPLICATION_RDF_XML, false))) {
                        errorRep.append(new Exception(), "The dataset uri that client provided "
                                + "does not seem to support the MIME: application/rdf+xml", clientPostedWrongParametersStatus);
                    }
                } catch (MalformedURLException ex) {
                    message = "The client did not post a valid URI for the dataset";
                    errorRep.append(ex, message, clientPostedWrongParametersStatus);
                } catch (URISyntaxException ex) {
                    message = "The client did not post a valid URI for the dataset";
                    errorRep.append(ex, message, clientPostedWrongParametersStatus);
                } catch (IllegalArgumentException ex) {
                    message = "The client did not post a valid URI for the dataset";
                    errorRep.append(ex, message, clientPostedWrongParametersStatus);
                }




                /**
                 * Check the target parameter.........
                 */
                try {
                    targeturi = new URI(form.getFirstValue("target"));
                    targeturi.toURL();
                    prm.targetAttribute = targeturi.toString();
                } catch (MalformedURLException ex) {
                    message = "[Wrong Posted Parameter ]: The client did"
                            + " not post a valid URI for the target feature";
                    errorRep.append(ex, message, clientPostedWrongParametersStatus);
                } catch (URISyntaxException ex) {
                    message = "[Wrong Posted Parameter ]: The client did"
                            + " not post a valid URI for the target feature";
                    errorRep.append(ex, message, clientPostedWrongParametersStatus);
                }


                /**
                 * Parse the dataset
                 */
                Dataset dataset = new Dataset(dataseturi);
                errorRep.append(dataset.errorRep);
                try {
                    dataInstances = dataset.getWekaDatasetForTraining(null, false);
                    /**
                     * If the data were successfully parsed, try to set the class attribute.
                     */
                    try {
                        dataInstances.setClass(dataInstances.attribute(prm.targetAttribute));
                    } catch (NullPointerException ex) {
                        message = "The target you posted in not a feature of the dataset: "
                                + prm.targetAttribute;
                        errorRep.append(ex, message, clientPostedWrongParametersStatus);
                    } catch (Throwable thr) {
                        errorRep.append(thr, "Severe Internal Error!", Status.SERVER_ERROR_INTERNAL);
                    }


                } catch (Exception ex) {
                    errorRep.append(ex, "Error while trying to parse the dataset!",
                            clientPostedWrongParametersStatus);
                }
            }
        };


        Thread checkKernel = new Thread() {

            @Override
            public void run() {
                if (!((prm.kernel.equalsIgnoreCase("linear"))
                        || (prm.kernel.equalsIgnoreCase("polynomial"))
                        || (prm.kernel.equalsIgnoreCase("rbf"))
                        || (prm.kernel.equalsIgnoreCase("sigmoid")))) {
                    errorRep.append(new Exception("Invalid Kernel"), "Invalid Kernel Type", clientPostedWrongParametersStatus);
                }
            }
        };

        Thread checkCost = new Thread() {

            @Override
            public void run() {
                /**
                 * Cost should be convertible to Double and strictly
                 * positive.
                 */
                String message;
                try {
                    d = Double.parseDouble(prm.cost);
                    if (d <= 0) {
                        errorRep.append(new NumberFormatException("Strictly positive number was expected"),
                                "The cost should be strictly positive", Status.CLIENT_ERROR_BAD_REQUEST);
                    }
                } catch (NumberFormatException e) {
                    message =
                            "The cost should be Double type, while you specified "
                            + "a non double value : " + prm.cost + "\n";
                    errorRep.append(e, message, Status.CLIENT_ERROR_BAD_REQUEST);
                } catch (Throwable thr) {
                    errorRep.append(thr, "Severe Internal Error! Excpeption", Status.SERVER_ERROR_INTERNAL);

                }

            }
        };

        Thread checkEpsilon = new Thread() {

            @Override
            public void run() {

                /**
                 * Epsilon should be convertible to Double and strictly
                 * positive.
                 */
                try {
                    d = Double.parseDouble(prm.epsilon);
                    if (d <= 0) {
                        errorRep.append(new NumberFormatException("Positive double was expected"),
                                "Epsilon must be strictly positive!", clientPostedWrongParametersStatus);
                    }
                } catch (NumberFormatException e) {
                    errorRep.append(e, "Epsilon must be a striclty positive number!", clientPostedWrongParametersStatus);
                } catch (Throwable thr) {
                    errorRep.append(thr, "Severe Internal Error!", Status.SERVER_ERROR_INTERNAL);
                }

            }
        };

        Thread checkDegree = new Thread() {

            @Override
            public void run() {
                /**
                 * Degree should be a strictly positive integer
                 */
                try {
                    i = Integer.parseInt(prm.degree);
                    if (i <= 0) {
                        errorRep.append(new NumberFormatException(),
                                "The degree must be a strictly positive integer!",
                                clientPostedWrongParametersStatus);
                    }
                } catch (NumberFormatException e) {
                    errorRep.append(e, "The degree must be a strictly positive integer!", clientPostedWrongParametersStatus);
                } catch (Throwable thr) {
                    errorRep.append(thr, "Severe Internal Error!", Status.SERVER_ERROR_INTERNAL);
                }

            }
        };

        Thread checkGamma = new Thread() {

            @Override
            public void run() {
                /**
                 * Gamma should be convertible to Double and strictly
                 * positive.
                 */
                try {
                    d = Double.parseDouble(prm.gamma);
                    if (d <= 0) {
                        errorRep.append(new NumberFormatException(),
                                "gamma must be a strictly positive double!", clientPostedWrongParametersStatus);
                    }
                } catch (NumberFormatException e) {
                    errorRep.append(e, "gamma must be a strictly positive double!", clientPostedWrongParametersStatus);
                } catch (Throwable thr) {
                    errorRep.append(thr, "Severe Internal Error!", Status.SERVER_ERROR_INTERNAL);
                }


            }
        };

        Thread checkCoeff0 = new Thread() {

            @Override
            public void run() {
                /**
                 * coeff0 should be convertible to Double.
                 */
                try {
                    d = Double.parseDouble(prm.coeff0);
                } catch (NumberFormatException e) {
                    errorRep.append(e, "coeff must be a double!", clientPostedWrongParametersStatus);
                } catch (Throwable thr) {
                    errorRep.append(thr, "Severe Internal Error!", Status.SERVER_ERROR_INTERNAL);
                }
            }
        };

        Thread checkTolerance = new Thread() {

            @Override
            public void run() {
                /**
                 * Tolerance
                 */
                try {
                    d = Double.parseDouble(prm.tolerance);
                    if (d <= 0) {
                        errorRep.append(new NumberFormatException("A positive double was expected!"),
                                "Tolerance must be a strictly positive double (preferably small)!", clientPostedWrongParametersStatus);
                    }
                } catch (NumberFormatException e) {
                    errorRep.append(e,
                            "Tolerance must be a strictly positive double (preferably small)!", clientPostedWrongParametersStatus);
                } catch (Throwable thr) {
                    errorRep.append(thr, "Severe Internal Error!", Status.SERVER_ERROR_INTERNAL);
                }
            }
        };

        Thread checkCache = new Thread() {

            @Override
            public void run() {
                /**
                 * cache size
                 */
                try {
                    i = Integer.parseInt(prm.cacheSize);
                    if ((i < 0)&&(i != -1)) {
                        errorRep.append(new NumberFormatException("A non-negative integer was expected!"),
                                "cacheSize must be a non-negative integer or -1 for no cache!", clientPostedWrongParametersStatus);
                    }
                } catch (NumberFormatException e) {
                    errorRep.append(e, "cacheSize should be an integer!", clientPostedWrongParametersStatus);
                } catch (Throwable thr) {
                    errorRep.append(thr, "Severe Internal Error!", Status.SERVER_ERROR_INTERNAL);
                }

            }
        };


        ExecutorService checker = Executors.newFixedThreadPool(9);
        checker.execute(checkDataset);
        checker.execute(checkKernel);
        checker.execute(checkCost);
        checker.execute(checkEpsilon);
        checker.execute(checkDegree);
        checker.execute(checkGamma);
        checker.execute(checkCoeff0);
        checker.execute(checkTolerance);
        checker.execute(checkCache);
        checker.shutdown();

        /**
         * Wait until all checks are terminated!
         */
        while (!checker.isTerminated()) {
            try {
                wait(5);
            } catch (InterruptedException ex) {
            }
        }
        notifyAll();
        return errorRep;

    }
}
