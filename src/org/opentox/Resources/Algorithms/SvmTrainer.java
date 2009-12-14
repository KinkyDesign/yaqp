/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.opentox.Resources.Algorithms;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import org.opentox.ontology.Dataset;
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
        Representation rep = checkParameters();
        throw new UnsupportedOperationException("Not supported yet.");
    }



    /**
     * Check the consistency of the POSTed svm parameters and assign default
     * values to the parameters that where not posted.
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
         * Get and Check the posted dataset.
         * Check whether the posted dataset id parameter is indeed
         * a URI. If yes, obtain the Instances.
         */
        try {
            dataseturi = new URI(form.getFirstValue("dataset"));
            HttpURLConnection.setFollowRedirects(false);
            HttpURLConnection con = null;
            try {
                con = (HttpURLConnection) dataseturi.toURL().openConnection();
                con.setDoInput(true);
                con.setDoOutput(true);
                con.setUseCaches(false);
                con.addRequestProperty("Accept", "application/rdf+xml");

                Dataset data = new Dataset(con.getInputStream());

                dataInstances = data.getWekaDataset();


            } catch (IOException ex) {
                setInternalStatus(Status.SERVER_ERROR_INTERNAL);
                errorDetails = errorDetails + "[Internal ]: Internal Error " + ex;
            }

        } catch (URISyntaxException ex) {
            setInternalStatus(clientPostedWrongParametersStatus);
            errorDetails = errorDetails + "[Wrong Posted Parameter ]: The client did"
                    + " not post a valid URI for the dataset";
        }


        /**
         * Check the consistency of the target.
         */
        targetAttribute = form.getFirstValue("target");
        dataInstances.setClass(dataInstances.attribute(targetAttribute));

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
        }


        /**
         * Epsilon should be convertible to Double and strictly
         * positive.
         */
        try {
            d = Double.parseDouble(epsilon);
            if (d <= 0) {
                errorDetails = errorDetails + "* [Inacceptable Parameter Value] Epsinlon must be strictly positive!\n";
                setInternalStatus(clientPostedWrongParametersStatus);
            }
        } catch (NumberFormatException e) {
            errorDetails = errorDetails + "* [Inacceptable Parameter Value] Epsilon must be a striclty positive number!\n";
            setInternalStatus(clientPostedWrongParametersStatus);
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
        }


        /**
         * coeff0 should be convertible to Double.
         */
        try {
            d = Double.parseDouble(coeff0);
        } catch (NumberFormatException e) {
            errorDetails = errorDetails + "* [Inacceptable Parameter Value] coeff must be a number!\n";
            setInternalStatus(clientPostedWrongParametersStatus);
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
                        +
                    "Error 400: Client Error, Bad Requset\n"
                    + "The request could not be understood by the server due "
                    + "to malformed syntax.\n"
                    + "Details: cache size (in MB) should be an Integer, while you specified "
                    + "a non Integer value : " + cacheSize + "\n\n";
            setInternalStatus(clientPostedWrongParametersStatus);
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

