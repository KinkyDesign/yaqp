package org.opentox.algorithm.trainer;

import org.opentox.algorithm.ConstantParameters;
import org.opentox.algorithm.AlgorithmParameter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import org.opentox.error.ErrorRepresentation;
import org.opentox.resource.AbstractResource.Directories;
import org.opentox.resource.AbstractResource.URIs;
import org.opentox.algorithm.dataprocessing.DataCleanUp;
import org.opentox.algorithm.trainer.AbstractTrainer.Classification;
import org.opentox.client.opentoxClient;
import org.opentox.database.ModelsDB;
import org.opentox.rdf.Dataset;
import org.opentox.rdf.Model;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.ServerResource;
import weka.classifiers.Evaluation;
import weka.classifiers.functions.SMO;
import weka.classifiers.functions.supportVector.Kernel;
import weka.classifiers.functions.supportVector.PolyKernel;
import weka.classifiers.functions.supportVector.RBFKernel;
import weka.core.Instances;
import weka.core.converters.ArffSaver;

/**
 *
 * @author OpenTox - http://www.opentox.org/
 * @author Sopasakis Pantelis
 * @author Sarimveis Harry
 * @version 1.3.3 (Last update: Dec 20, 2009)
 */
@Classification(name="SVC") public class SvcTrainer extends AbstractTrainer {

    private static final long serialVersionUID = -6751396783635831385L;

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
    public SvcTrainer(Form form, final ServerResource resource) {
        super(form, resource);
        super.form = form;
    }



    /**
     * Train a support vector machine classification model.
     * @return representation of the training result.
     */
    @Override
    public Representation train() {
        Representation rep = checkParameters();

        if (errorRep.getErrorLevel()==0) {
            // 2. Remove all string attributes from the dataset:
            DataCleanUp.removeStringAtts(dataInstances);
            // 3. Lock an ID for the model:
            model_id = ModelsDB.INSTANCE.getModelsStack() + 1;

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

                // 6. Build the Classification Model:
                
                 weka.classifiers.functions.SMO classifier = new SMO();
                 classifier.setEpsilon(Double.parseDouble(epsilon));
                 classifier.setToleranceParameter(Double.parseDouble(tolerance));

                Kernel svc_kernel = null;
                if (this.kernel.equalsIgnoreCase("rbf")) {
                    RBFKernel rbf_kernel = new RBFKernel();
                    rbf_kernel.setGamma(Double.parseDouble(gamma));
                    rbf_kernel.setCacheSize(Integer.parseInt(cacheSize));
                    svc_kernel = rbf_kernel;
                } else if (this.kernel.equalsIgnoreCase("polynomial")) {
                    PolyKernel poly_kernel = new PolyKernel();
                    poly_kernel.setExponent(Double.parseDouble(degree));
                    poly_kernel.setCacheSize(Integer.parseInt(cacheSize));
                    poly_kernel.setUseLowerOrder(true);
                    svc_kernel = poly_kernel;
                } else if (this.kernel.equalsIgnoreCase("linear")) {                    
                    PolyKernel linear_kernel = new PolyKernel();
                    linear_kernel.setExponent((double) 1.0);
                    linear_kernel.setCacheSize(Integer.parseInt(cacheSize));
                    linear_kernel.setUseLowerOrder(true);
                    svc_kernel = linear_kernel;
                }                
                classifier.setKernel(svc_kernel);                

                String[] generalOptions = {
                    "-c", Integer.toString(dataInstances.classIndex() + 1),
                    "-t", temporaryArffFile,
                    /// Save the model in the following directory
                    "-d", modelFile};

                Evaluation.evaluateModel(classifier, generalOptions);

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
                            dataInstances,
                            paramList,
                            URIs.svcAlgorithmURI,
                            new FileOutputStream(Directories.modelRdfDir + "/" + model_id));
                    //setInternalStatus(opentoxModel.internalStatus);

                    if (errorRep.getErrorLevel()==0) {
                        // if status is OK(200), register the new model in the database and
                        // return the URI to the client.
                        rep = new StringRepresentation(URIs.modelURI + "/"
                                + ModelsDB.INSTANCE.registerNewModel(URIs.svcAlgorithmURI) + "\n");
                    } else {
                        //rep = new StringRepresentation(internalStatus.toString());
                    }
                }
            } catch (AssertionError e) {
                rep = new StringRepresentation("It seems the dataset you posted is void!\n");
                //setInternalStatus(Status.CLIENT_ERROR_BAD_REQUEST);
            } catch (IOException ex) {
                rep = new StringRepresentation("Communications error while trainig an SVC model!\n" +
                        "Error Details: "+ex+"\n");
                //setInternalStatus(Status.SERVER_ERROR_INTERNAL);
            } catch (Exception ex) {
                rep = new StringRepresentation("The traing was terminated unexpectedly\n" +
                        "Error Details: "+ex+"\n");
                //setInternalStatus(Status.SERVER_ERROR_INTERNAL);
            }
        }
        return rep;

    }

    @Override
    public ErrorRepresentation checkParameters() {
        // Some initial definitions:
        Status clientPostedWrongParametersStatus = Status.CLIENT_ERROR_BAD_REQUEST;
        String errorDetails = "";

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
         * Check the dataset_uri parameter.........
         */
        try {
            dataseturi = new URI(form.getFirstValue("dataset_uri"));
            dataseturi.toURL();
            if (!(opentoxClient.IsMimeAvailable(dataseturi, MediaType.APPLICATION_RDF_XML, false))) {
                errorRep.append(new Exception(), "The dataset uri that client provided "
                        + "does not seem to support the MIME: application/rdf+xml", clientPostedWrongParametersStatus);
            }
        } catch (MalformedURLException ex) {
            errorDetails = "The client did not post a valid URI for the dataset";
            errorRep.append(ex, errorDetails, clientPostedWrongParametersStatus);
        } catch (URISyntaxException ex) {
            errorDetails = "The client did not post a valid URI for the dataset";
            errorRep.append(ex, errorDetails, clientPostedWrongParametersStatus);
        } catch (IllegalArgumentException ex) {
            errorDetails = "The client did not post a valid URI for the dataset";
            errorRep.append(ex, errorDetails, clientPostedWrongParametersStatus);
        }


        /**
         * Check the target parameter.........
         */
        try {
            targeturi = new URI(form.getFirstValue("target"));
            targeturi.toURL();
            targetAttribute = targeturi.toString();
        } catch (MalformedURLException ex) {
            errorDetails = "[Wrong Posted Parameter ]: The client did"
                    + " not post a valid URI for the target feature";
            errorRep.append(ex, errorDetails, clientPostedWrongParametersStatus);
        } catch (URISyntaxException ex) {
            errorDetails = "[Wrong Posted Parameter ]: The client did"
                    + " not post a valid URI for the target feature";
            errorRep.append(ex, errorDetails, clientPostedWrongParametersStatus);
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
                dataInstances.setClass(dataInstances.attribute(targetAttribute));
            } catch (NullPointerException ex) {
                errorDetails = "The target you posted in not a feature of the dataset: " + targetAttribute;
                errorRep.append(ex, errorDetails, clientPostedWrongParametersStatus);
            } catch (Throwable thr) {
                errorRep.append(thr, "Severe Internal Error!", Status.SERVER_ERROR_INTERNAL);
            }


        } catch (Exception ex) {
            errorRep.append(ex, "Error while trying to parse the dataset!",
                    clientPostedWrongParametersStatus);
        }







        if (!((kernel.equalsIgnoreCase("linear"))
                || (kernel.equalsIgnoreCase("polynomial"))
                || (kernel.equalsIgnoreCase("rbf"))
                || (kernel.equalsIgnoreCase("sigmoid")))) {
            errorRep.append(new Exception("Invalid Kernel"), "Invalid Kernel Type", clientPostedWrongParametersStatus);
        }

        /**
         * Cost should be convertible to Double and strictly
         * positive.
         */
        try {
            d = Double.parseDouble(cost);
            if (d <= 0) {
                errorRep.append(new NumberFormatException("Strictly positive number was expected"),
                        "The cost should be strictly positive", Status.CLIENT_ERROR_BAD_REQUEST);
            }
        } catch (NumberFormatException e) {
            errorDetails =
                    "The cost should be Double type, while you specified "
                    + "a non double value : " + cost + "\n";
            errorRep.append(e, errorDetails, Status.CLIENT_ERROR_BAD_REQUEST);
        } catch (Throwable thr) {
            errorRep.append(thr, "Severe Internal Error! Excpeption", Status.SERVER_ERROR_INTERNAL);

        }


        /**
         * Epsilon should be convertible to Double and strictly
         * positive.
         */
        try {
            d = Double.parseDouble(epsilon);
            if (d <= 0) {
                errorRep.append(new NumberFormatException("Positive double was expected"),
                        "Epsilon must be strictly positive!", clientPostedWrongParametersStatus);
            }
        } catch (NumberFormatException e) {
            errorRep.append(e, "Epsilon must be a striclty positive number!", clientPostedWrongParametersStatus);
        } catch (Throwable thr) {
            errorRep.append(thr, "Severe Internal Error!", Status.SERVER_ERROR_INTERNAL);
        }


        /**
         * Degree should be a strictly positive integer
         */
        try {
            i = Integer.parseInt(degree);
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



        /**
         * Gamma should be convertible to Double and strictly
         * positive.
         */
        try {
            d = Double.parseDouble(gamma);
            if (d <= 0) {
                errorRep.append(new NumberFormatException(),
                        "gamma must be a strictly positive double!", clientPostedWrongParametersStatus);
            }
        } catch (NumberFormatException e) {
            errorRep.append(e, "gamma must be a strictly positive double!", clientPostedWrongParametersStatus);
        } catch (Throwable thr) {
            errorRep.append(thr, "Severe Internal Error!", Status.SERVER_ERROR_INTERNAL);
        }


        /**
         * coeff0 should be convertible to Double.
         */
        try {
            d = Double.parseDouble(coeff0);
        } catch (NumberFormatException e) {
            errorRep.append(e, "coeff must be a double!", clientPostedWrongParametersStatus);
        } catch (Throwable thr) {
            errorRep.append(thr, "Severe Internal Error!", Status.SERVER_ERROR_INTERNAL);
        }


        /**
         * Tolerance
         */
        try {
            d = Double.parseDouble(tolerance);
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



        /**
         * cache size
         */
        try {
            i = Integer.parseInt(cacheSize);
            if (i <= 0) {
                errorRep.append(new NumberFormatException("A positive integer was expected!"),
                        "cacheSize must be a strictly positive integer!", clientPostedWrongParametersStatus);
            }
        } catch (NumberFormatException e) {
            errorRep.append(e, "cacheSize should be an integer!", clientPostedWrongParametersStatus);
        } catch (Throwable thr) {
            errorRep.append(thr, "Severe Internal Error!", Status.SERVER_ERROR_INTERNAL);
        }


        return errorRep;

    }





}
