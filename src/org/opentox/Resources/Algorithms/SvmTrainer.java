package org.opentox.Resources.Algorithms;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.opentox.Resources.AbstractResource.Directories;
import org.opentox.Resources.AbstractResource.URIs;
import org.opentox.database.ModelsDB;
import org.opentox.ontology.Dataset;
import org.opentox.ontology.Model;
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
 * @version 1.3.3 (Last update: Dec 20, 2009)
 */
public class SvmTrainer extends AbstractTrainer {

    private static final long serialVersionUID = -1522085945071581484L;
    
    private int i;

    private double d;
    /**
     * The name of the target attribute which normally is the
     * URI of a feature definition.
     */
    private String targetAttribute;
    /**
     * The kernel used in the SVM model.
     * This can be rbf, linear, sigmoid or polynomial.
     */
    private String kernel;
    /**
     * The degree of the polynomial kernel (when used).
     */
    private String degree;
    /**
     * The cahed memory used in model training.
     */
    private String cacheSize;
    /**
     * The Cost coefficient.
     */
    private String cost;
    /**
     * The parameter epsilon used in SVM models.
     */
    private String epsilon;
    /**
     * The kernel parameter gamma used in various kernel functions.
     */
    private String gamma;
    /**
     * The bias of the support vector model.
     */
    private String coeff0;
    /**
     * The tolerance used in model training.
     */
    private String tolerance;
    /**
     * The id of the generated model.
     */
    private int model_id;
    /**
     * An Instances object used to store the data.
     */
    private Instances dataInstances;

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
     * @return
     */
    @Override
    public Representation train() {

        // 1. Check the parameters posted by the client:
        Representation rep = checkParameters();

        if (Status.SUCCESS_ACCEPTED.equals(internalStatus)) {
            // 2. Remove all string attributes from the dataset:
            Preprocessing.removeStringAtts(dataInstances);
            // 3. Lock an ID for the model:
            model_id = ModelsDB.getModelsStack() + 1;

            // 4. Define the temporary arff file that will be used to store data
            //    and the path to the model file that will be created.
            long rand = System.currentTimeMillis();
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
                String[] regressorOptions = {"-P", epsilon,
                    "-T", tolerance};

                Kernel svm_kernel = null;
                if (this.kernel.equalsIgnoreCase("rbf")) {
                    RBFKernel rbf_kernel = new RBFKernel();
                    rbf_kernel.setGamma(Double.parseDouble(gamma));
                    rbf_kernel.setCacheSize(Integer.parseInt(cacheSize));
                    svm_kernel = rbf_kernel;
                } else if (this.kernel.equalsIgnoreCase("polynomial")) {
                    PolyKernel poly_kernel = new PolyKernel();                    
                    poly_kernel.setExponent(Double.parseDouble(degree));
                    poly_kernel.setCacheSize(Integer.parseInt(cacheSize));
                    poly_kernel.setUseLowerOrder(true);
                    svm_kernel = poly_kernel;
                } else if (this.kernel.equalsIgnoreCase("linear")){
                    PolyKernel linear_kernel = new PolyKernel();
                    PolyKernel poly_kernel = new PolyKernel();
                    poly_kernel.setExponent((double)1.0);
                    poly_kernel.setCacheSize(Integer.parseInt(cacheSize));
                    poly_kernel.setUseLowerOrder(true);
                    svm_kernel = poly_kernel;
                }
                System.out.println(svm_kernel);
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
                    List<AlgorithmParameter> paramList = new ArrayList<AlgorithmParameter>();
                    paramList.add(ConstantParameters.COEFF0(Double.parseDouble(coeff0)));
                    paramList.add(ConstantParameters.COST(Double.parseDouble(cost)));
                    paramList.add(ConstantParameters.TARGET(targetAttribute));
                    paramList.add(ConstantParameters.DEGREE(Integer.parseInt(degree)));
                    paramList.add(ConstantParameters.KERNEL(kernel));
                    paramList.add(ConstantParameters.EPSILON(Double.parseDouble(epsilon)));

                    Model opentoxModel = new Model();

                    opentoxModel.createModel(Integer.toString(model_id),
                            dataseturi.toString(),
                            targetAttribute,
                            dataInstances,
                            paramList,
                            URIs.svmAlgorithmURI,
                            new FileOutputStream(Directories.modelRdfDir + "/" + model_id));
                    setInternalStatus(opentoxModel.internalStatus);

                    if (Status.SUCCESS_OK.equals(internalStatus)) {
                        // if status is OK(200), register the new model in the database and
                        // return the URI to the client.
                        rep = new StringRepresentation(URIs.modelURI + "/"
                                + ModelsDB.registerNewModel(URIs.svmAlgorithmURI) + "\n");
                    } else {
                        rep = new StringRepresentation(internalStatus.toString());
                    }
                }
            } catch (AssertionError e) {
                Logger.getLogger(SvmTrainer.class.getName()).log(Level.SEVERE, null, e);
            } catch (IOException ex) {
                Logger.getLogger(SvmTrainer.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(SvmTrainer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return rep;

    }

    /**
     * Check the consistency of the POSTed svm parameters and assign default
     * values to the parameters that where not posted. The dataInstances are
     * updated according to the dataset uri.
     * @return
     */
    @Override
    public Representation checkParameters() {
        // Some initial definitions:
        Representation rep = null;
        MediaType errorMediaType = MediaType.TEXT_PLAIN;
        Status clientPostedWrongParametersStatus = Status.CLIENT_ERROR_BAD_REQUEST;
        String errorDetails = "";
        setInternalStatus(Status.SUCCESS_ACCEPTED);

        /**
         * Assign default values to the parameters that where not
         * posted.
         */
        kernel = form.getFirstValue("kernel");
        if (kernel == null) {
            kernel = ConstantParameters.KERNEL.paramValue.toString();
        }

        cost = form.getFirstValue("cost");
        if (cost == null) {
            cost = ConstantParameters.COST.paramValue.toString();
        }
        gamma = form.getFirstValue("gamma");
        if (gamma == null) {
            gamma = ConstantParameters.GAMMA.paramValue.toString();
        }
        epsilon = form.getFirstValue("epsilon");
        if (epsilon == null) {
            epsilon = ConstantParameters.EPSILON.paramValue.toString();
        }
        coeff0 = form.getFirstValue("coeff0");
        if (coeff0 == null) {
            coeff0 = ConstantParameters.COEFF0.paramValue.toString();
        }
        degree = form.getFirstValue("degree");
        if (degree == null) {
            degree = ConstantParameters.DEGREE.paramValue.toString();
        }
        tolerance = form.getFirstValue("tolerance");
        if (tolerance == null) {
            tolerance = ConstantParameters.TOLERANCE.paramValue.toString();
        }
        cacheSize = form.getFirstValue("cacheSize");
        if (cacheSize == null) {
            cacheSize = "50";
        }

        /**
         * Get and Check the posted dataset_uri parameter.
         * Check whether the posted dataset_uri parameter is indeed
         * a URI. If yes, obtain the Instances.
         */
        try {
            dataseturi = new URI(form.getFirstValue("dataset_uri"));            
            Dataset data = new Dataset(dataseturi);
            
            dataInstances = data.getWekaDatasetForTraining(null, false);



        } catch (URISyntaxException ex) {
            setInternalStatus(clientPostedWrongParametersStatus);
            errorDetails = errorDetails + "[Wrong Posted Parameter ] The client did"
                    + " not post a valid URI for the dataset\n";
        } catch (IllegalArgumentException ex) {
            setInternalStatus(clientPostedWrongParametersStatus);
            errorDetails = errorDetails + "* [Wrong Posted Parameter ] The dataset URI"
                    + " you POSTed seems not to be valid: " + dataseturi + "\n";
        } catch (Throwable thr) {
            setInternalStatus(Status.SERVER_ERROR_INTERNAL);
            errorDetails = errorDetails + "* [SEVERE] Severe Internal Error! Excpeption: " + thr;
        }



        /**
         * Check the consistency of the target.
         */
        targetAttribute = form.getFirstValue("target");
        try {
            new URI(targetAttribute);
        } catch (URISyntaxException ex) {
            setInternalStatus(clientPostedWrongParametersStatus);
            errorDetails = errorDetails + "* [Wrong Posted Parameter ] The target URI"
                    + " you POSTed seems not to be valid: " + dataseturi + "\n";
        } catch (Throwable thr) {
            setInternalStatus(Status.SERVER_ERROR_INTERNAL);
            errorDetails = errorDetails + "* [SEVERE] Severe Internal Error! Excpeption: " + thr;
        }

        try {
            dataInstances.setClass(dataInstances.attribute(targetAttribute));
        } catch (NullPointerException ex) {
            setInternalStatus(clientPostedWrongParametersStatus);
            errorDetails = errorDetails + "* [Inacceptable Parameter Value] "
                    + "The target you posted in not a feature of the dataset: " + targetAttribute + "\n";
        } catch (Throwable thr) {
            setInternalStatus(Status.SERVER_ERROR_INTERNAL);
            errorDetails = errorDetails + "* [SEVERE] Severe Internal Error! Excpeption: " + thr;
        }

        if (!((kernel.equalsIgnoreCase("linear"))
                || (kernel.equalsIgnoreCase("polynomial"))
                || (kernel.equalsIgnoreCase("rbf"))
                || (kernel.equalsIgnoreCase("sigmoid")))) {
            errorDetails = errorDetails + "* [Inacceptable Parameter Value] Invalid Kernel Type!\n";
            setInternalStatus(clientPostedWrongParametersStatus);
        }

        /**
         * Cost should be convertible to Double and strictly
         * positive.
         */
        try {
            d = Double.parseDouble(cost);
            System.out.println(d);
            if (d <= 0) {
                System.out.println("x2");
                errorDetails = errorDetails + "* [Inacceptable Parameter Value] The cost should be strictly positive\n";
                setInternalStatus(clientPostedWrongParametersStatus);
            }
        } catch (NumberFormatException e) {
            errorDetails = errorDetails
                    + "* [Inacceptable Parameter Value]  The cost should be Double type, while you specified "
                    + "a non double value : " + cost + "\n";
            setInternalStatus(clientPostedWrongParametersStatus);
        } catch (Throwable thr) {
            setInternalStatus(Status.SERVER_ERROR_INTERNAL);
            errorDetails = errorDetails + "* [SEVERE] Severe Internal Error! Excpeption: " + thr;
        }


        /**
         * Epsilon should be convertible to Double and strictly
         * positive.
         */
        try {
            d = Double.parseDouble(epsilon);
            if (d <= 0) {
                errorDetails = errorDetails + "* [Inacceptable Parameter Value] Epsilon must be strictly positive!\n";
                setInternalStatus(clientPostedWrongParametersStatus);
            }
        } catch (NumberFormatException e) {
            errorDetails = errorDetails + "* [Inacceptable Parameter Value] Epsilon must be a striclty positive number!\n";
            setInternalStatus(clientPostedWrongParametersStatus);
        } catch (Throwable thr) {
            setInternalStatus(Status.SERVER_ERROR_INTERNAL);
            errorDetails = errorDetails + "* [SEVERE] Severe Internal Error! Excpeption: " + thr;
        }


        /**
         * Degree should be a strictly positive integer
         */
        try {
            i = Integer.parseInt(degree);
            if (i <= 0) {
                errorDetails = errorDetails + "* [Inacceptable Parameter Value] The degree must be a strictly positive integer!\n";
                setInternalStatus(clientPostedWrongParametersStatus);
            }
        } catch (NumberFormatException e) {
            errorDetails = errorDetails + "* [Inacceptable Parameter Value] The degree must be a strictly positive integer!\n";
            setInternalStatus(clientPostedWrongParametersStatus);
        } catch (Throwable thr) {
            setInternalStatus(Status.SERVER_ERROR_INTERNAL);
            errorDetails = errorDetails + "* [SEVERE] Severe Internal Error! Excpeption: " + thr;
        }



        /**
         * Gamma should be convertible to Double and strictly
         * positive.
         */
        try {
            d = Double.parseDouble(gamma);
            if (d <= 0) {
                errorDetails = errorDetails + "* [Inacceptable Parameter Value] gamma must be a strictly positive double!\n";
                setInternalStatus(clientPostedWrongParametersStatus);
            }
        } catch (NumberFormatException e) {
            errorDetails = errorDetails + "* [Inacceptable Parameter Value] gamma must be a strictly positive double!\n";
            setInternalStatus(clientPostedWrongParametersStatus);
        } catch (Throwable thr) {
            setInternalStatus(Status.SERVER_ERROR_INTERNAL);
            errorDetails = errorDetails + "* [SEVERE] Severe Internal Error! Excpeption: " + thr;
        }


        /**
         * coeff0 should be convertible to Double.
         */
        try {
            d = Double.parseDouble(coeff0);
        } catch (NumberFormatException e) {
            errorDetails = errorDetails + "* [Inacceptable Parameter Value] coeff must be a number!\n";
            setInternalStatus(clientPostedWrongParametersStatus);
        } catch (Throwable thr) {
            setInternalStatus(Status.SERVER_ERROR_INTERNAL);
            errorDetails = errorDetails + "* [SEVERE] Severe Internal Error! Excpeption: " + thr;
        }


        /**
         * Tolerance
         */
        try {
            d = Double.parseDouble(tolerance);
            if (d <= 0) {
                errorDetails = errorDetails
                        + "* [Inacceptable Parameter Value] Tolerance must be a strictly positive double (preferably small)!\n";
                setInternalStatus(clientPostedWrongParametersStatus);
            }
        } catch (NumberFormatException e) {
            errorDetails = errorDetails
                    + "* [Inacceptable Parameter Value] Tolerance must be a strictly positive double (preferably small)!\n";
            setInternalStatus(clientPostedWrongParametersStatus);
        } catch (Throwable thr) {
            setInternalStatus(Status.SERVER_ERROR_INTERNAL);
            errorDetails = errorDetails + "* [SEVERE] Severe Internal Error! Excpeption: " + thr;
        }



        /**
         * cache size
         */
        try {
            i = Integer.parseInt(cacheSize);
            if (d <= 0) {
                setInternalStatus(clientPostedWrongParametersStatus);
            }
        } catch (NumberFormatException e) {
            errorDetails = errorDetails
                    + "Error 400: Client Error, Bad Requset\n"
                    + "The request could not be understood by the server due "
                    + "to malformed syntax.\n"
                    + "Details: cache size (in MB) should be an Integer, while you specified "
                    + "a non Integer value : " + cacheSize + "\n\n";
            setInternalStatus(clientPostedWrongParametersStatus);
        } catch (Throwable thr) {
            setInternalStatus(Status.SERVER_ERROR_INTERNAL);
            errorDetails = errorDetails + "* [SEVERE] Severe Internal Error! Excpeption: " + thr;
        }


        if (!(errorDetails.equalsIgnoreCase(""))) {
            rep = new StringRepresentation("Error Code : " + clientPostedWrongParametersStatus.toString() + "\n"
                    + "Error Code Desription : The request could not be understood by the server due to "
                    + "malformed syntax.\nThe client SHOULD NOT repeat the request without modifications.\n"
                    + "Error Explanation :\n" + errorDetails + "\n", errorMediaType);
            return rep;
        } else {
            return null;
        }


    }
}
