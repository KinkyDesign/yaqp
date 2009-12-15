package org.opentox.Resources.Algorithms;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.opentox.Resources.AbstractResource.Directories;
import org.opentox.database.FeaturesDB;
import org.opentox.database.ModelsDB;
import org.opentox.ontology.Dataset;
import org.opentox.util.libSVM.svm_scale;
import org.opentox.util.libSVM.svm_train;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import weka.core.Instances;

/**
 *
 * @author OpenTox - http://www.opentox.org
 * @author Sopasakis Pantelis
 * @author Sarimveis Harry
 */
public class SvmTrainer extends AbstractTrainer {

    private static final long serialVersionUID = -9058627046190364530L;
    /**
     * The id of the regression algorithm.
     * This can be either mlr or svm.
     */
    private volatile String algorithmId;
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
    public SvmTrainer(Form form) {
        super(form);
        super.form = form;
    }


    /**
     * Trains a new SVM model.
     * @return
     */
    @Override
    public Representation train() {
        /**
         * 1. Check the parameters posted by the client.
         */
        Representation rep = checkParameters();

        /**
         * 2. Remove String attributes
         */
        Preprocessing.removeStringAtts(dataInstances);

        /**
         * 3. Register the list of features in the database.
         */
        model_id = ModelsDB.getModelsStack() + 1;
        List<String> listOfFeatures = new ArrayList<String>();
        for (int k = 0; k < dataInstances.numAttributes(); k++) {
            listOfFeatures.add(dataInstances.attribute(k).name());
        }
        FeaturesDB.registerFeatureList(model_id, listOfFeatures);

        /**
         * !!!! Some important definitions
         */
        String  dataDSDFile = Directories.dataDSDDir + "/" + model_id,
                scaledFile = Directories.dataScaledDir + "/" + model_id,
                rangeFile = Directories.dataRangesDir + "/" + model_id,
                modelDSDFile  = Directories.modelRawDir+"/"+model_id;

        /**
         * 4. Store the DSD representation of the dataset in DSD format.
         */
        org.opentox.util.converters.Converter converter =
                new org.opentox.util.converters.Converter();
        converter.convert(dataInstances, new File(dataDSDFile));

        /**
         * 5. Scale the DSD file using libSVM (svm_scale)
         */
        String[] scalingOptions = {
            "-l", "-1",
            "-u", "1",
            "-s", rangeFile,
            Directories.dataDSDDir + "/" + model_id
        };
        svm_scale scaler = new svm_scale();
        try {
            scaler.scale(scalingOptions, scaledFile);

            /**
             * 6. Train the model and store its DSD representation
             */
            svm_train.main(getSvmOptions(scaledFile, modelDSDFile));

            /**
             * 7. Check if the model was indeed generated
             */
            if (new File(modelDSDFile).exists()){



            }


        } catch (IOException ex) {
            Logger.getLogger(SvmTrainer.class.getName()).log(Level.SEVERE, null, ex);
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
            kernel = "RBF";
        }
        cost = form.getFirstValue("cost");
        if (cost == null) {
            cost = "100";
        }
        gamma = form.getFirstValue("gamma");
        if (gamma == null) {
            gamma = "1.5";
        }
        epsilon = form.getFirstValue("epsilon");
        if (epsilon == null) {
            epsilon = "0.1";
        }
        coeff0 = form.getFirstValue("coeff0");
        if (coeff0 == null) {
            coeff0 = "0";
        }
        degree = form.getFirstValue("degree");
        if (degree == null) {
            degree = "3";
        }
        tolerance = form.getFirstValue("tolerance");
        if (tolerance == null) {
            tolerance = "0.0001";
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
            HttpURLConnection.setFollowRedirects(false);
            HttpURLConnection con = null;

            con = (HttpURLConnection) dataseturi.toURL().openConnection();
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setUseCaches(false);
            con.addRequestProperty("Accept", "application/rdf+xml");

            Dataset data = new Dataset(con.getInputStream());

            dataInstances = data.getWekaDataset();



        } catch (URISyntaxException ex) {
            setInternalStatus(clientPostedWrongParametersStatus);
            errorDetails = errorDetails + "[Wrong Posted Parameter ] The client did"
                    + " not post a valid URI for the dataset\n";
        } catch (IllegalArgumentException ex) {
            setInternalStatus(clientPostedWrongParametersStatus);
            errorDetails = errorDetails + "* [Wrong Posted Parameter ] The dataset URI"
                    + " you POSTed seems not to be valid: " + dataseturi + "\n";
        } catch (UnknownHostException ex) {
            setInternalStatus(clientPostedWrongParametersStatus);
            errorDetails = errorDetails + "* [Wrong Posted Parameter ] Unknown host: "
                    + dataseturi.getHost() + "\n";
        } catch (IOException ex) {
            setInternalStatus(Status.SERVER_ERROR_INTERNAL);
            errorDetails = errorDetails + "* [Internal Error ] Internal Error. "
                    + "The following exception was thrown: " + ex + "\n";
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
            errorDetails = errorDetails + "* [Inacceptable Parameter Value] The target you posted in not a feature of the dataset: " + targetAttribute + "\n";
        } catch (Throwable thr) {
            setInternalStatus(Status.SERVER_ERROR_INTERNAL);
            errorDetails = errorDetails + "* [SEVERE] Severe Internal Error! Excpeption: " + thr;
        }


        if (!((kernel.equalsIgnoreCase("rbf"))
                || (kernel.equalsIgnoreCase("linear"))
                || (kernel.equalsIgnoreCase("sigmoid"))
                || (kernel.equalsIgnoreCase("polynomial")))) {
            errorDetails = errorDetails + "* [Inacceptable Parameter Value] Invalid Kernel Type!\n";
            setInternalStatus(clientPostedWrongParametersStatus);
        }

        /**
         * Cost should be convertible to Double and strictly
         * positive.
         */
        try {
            d = Double.parseDouble(cost);
            if (d <= 0) {
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



    /**
     *
     * @param scaledPath Path to the scaled DSD data file.
     * @param modelPath Model Destination.
     * @return
     */
    private String[] getSvmOptions(String scaledPath, String modelPath) {
        String[] options = {""};


        String ker = "";
        if (kernel.equalsIgnoreCase("linear")) {
            ker = "0";
        } else if (kernel.equalsIgnoreCase("polynomial")) {
            ker = "1";
        } else if (kernel.equalsIgnoreCase("rbf")) {
            ker = "2";
        } else if (kernel.equalsIgnoreCase("sigmoid")) {
            ker = "3";
        } else {
            ker = "2";
        }


        if (ker.equalsIgnoreCase("0")) {
            String[] ops = {
                "-s", "3",// epsilon-SVr
                "-t", "0",
                "-c", cost,
                "-e", tolerance,
                "-q",
                scaledPath,
                modelPath
            };
            options =
                    ops;
        } else if (ker.equalsIgnoreCase("1")) {
            String[] ops = {
                "-s", "3",// epsilon-SVr
                "-t", ker,
                "-c", cost,
                "-g", gamma,
                "-d", degree,
                "-r", coeff0,
                "-e", tolerance,
                "-q",
                scaledPath,
                modelPath
            };
            options =
                    ops;
        } else if (ker.equalsIgnoreCase("2")) {
            String[] ops = {
                "-s", "3",// epsilon-SVr
                "-t", ker,
                "-c", cost,
                "-g", gamma,
                "-e", tolerance,
                "-q",
                scaledPath,
                modelPath
            };
            options =
                    ops;
        } else if (ker.equalsIgnoreCase("3")) {
            String[] ops = {
                "-s", "3",// epsilon-SVr
                "-t", ker,
                "-c", cost,
                "-g", gamma,
                "-e", tolerance,
                "-q",
                scaledPath,
                modelPath
            };
            options =
                    ops;
        }
        return options;
    }
}

