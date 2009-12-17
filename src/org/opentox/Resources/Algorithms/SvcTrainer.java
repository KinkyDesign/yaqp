package org.opentox.Resources.Algorithms;


import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import java.net.URI;
import java.net.URISyntaxException;
import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.opentox.client.opentoxClient;
import org.restlet.data.Form;
import weka.core.Instances;

/**
 *
 * @author OpenTox - http://www.opentox.org
 * @author Sopasakis Pantelis
 * @author Sarimveis Harry
 */
public class SvcTrainer extends AbstractTrainer{

    private String kernel;
    private String cost;
    private String gamma;
    private String epsilon;
    private String coeff0;
    private String degree;
    private String tolerance;
    private String cacheSize;
    private int model_id;
    private double d;
    private int i;
    private Instances dataInstances;
    private String targetAttribute;

     public SvcTrainer(Form form){
        super(form);
        super.form = form;
    }

    @Override
    public Representation train() {

        return null;
    }

    

    
    @Override
    public Representation checkParameters() {
        Representation rep = null;
        MediaType errorMediaType = MediaType.TEXT_PLAIN;
        Status clientPostedWrongParametersStatus = Status.CLIENT_ERROR_BAD_REQUEST;
        String errorDetails = "";


        setInternalStatus(Status.SUCCESS_ACCEPTED);

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
         * a URI.
         */
        try {
            dataseturi = new URI(form.getFirstValue("dataset"));
            dataInstances = opentoxClient.getInstances(dataseturi);
        } catch (URISyntaxException ex) {
            setInternalStatus(clientPostedWrongParametersStatus);
            errorDetails = errorDetails + "[Wrong Posted Parameter ]: The client did" +
                    " not post a valid URI for the dataset";
        }



        if (!((kernel.equalsIgnoreCase("rbf")) ||
                (kernel.equalsIgnoreCase("linear")) ||
                (kernel.equalsIgnoreCase("sigmoid")) ||
                (kernel.equalsIgnoreCase("polynomial")))) {
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
                errorDetails = errorDetails +
                        "* [Inacceptable Parameter Value] The cost should be strictly positive\n";
                setInternalStatus(clientPostedWrongParametersStatus);
            }
        } catch (NumberFormatException e) {
            errorDetails = errorDetails +
                    "* [Inacceptable Parameter Value]  The cost should be Double type, while you specified " +
                    "a non double value : " + cost + "\n";
            setInternalStatus(clientPostedWrongParametersStatus);
        }


        /**
         * Epsilon should be convertible to Double and strictly
         * positive.
         */
        try {
            d = Double.parseDouble(epsilon);
            if (d <= 0) {
                errorDetails = errorDetails +
                        "* [Inacceptable Parameter Value] Epsinlon must be strictly positive!\n";
                setInternalStatus(clientPostedWrongParametersStatus);
            }
        } catch (NumberFormatException e) {
            errorDetails = errorDetails +
                    "* [Inacceptable Parameter Value] Epsilon must be a striclty positive number!\n";
            setInternalStatus(clientPostedWrongParametersStatus);
        }


        /**
         * Degree should be a strictly positive integer
         */
        try {
            i = Integer.parseInt(degree);
            if (i <= 0) {
                errorDetails = errorDetails +
                        "* [Inacceptable Parameter Value] The degree must be a strictly positive integer!\n";
                setInternalStatus(clientPostedWrongParametersStatus);
            }
        } catch (NumberFormatException e) {
            errorDetails = errorDetails +
                    "* [Inacceptable Parameter Value] The degree must be a strictly positive integer!\n";
            setInternalStatus(clientPostedWrongParametersStatus);
        }



        /**
         * Gamma should be convertible to Double and strictly
         * positive.
         */
        try {
            d = Double.parseDouble(gamma);
            if (d <= 0) {
                errorDetails = errorDetails +
                        "* [Inacceptable Parameter Value] gamma must be a strictly positive double!\n";
                setInternalStatus(clientPostedWrongParametersStatus);
            }
        } catch (NumberFormatException e) {
            errorDetails = errorDetails +
                    "* [Inacceptable Parameter Value] gamma must be a strictly positive double!\n";
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
                errorDetails = errorDetails +
                        "* [Inacceptable Parameter Value] Tolerance must be a strictly positive double (preferably small)!\n";
                setInternalStatus(clientPostedWrongParametersStatus);
            }
        } catch (NumberFormatException e) {
            errorDetails = errorDetails +
                    "* [Inacceptable Parameter Value] Tolerance must be a strictly positive double (preferably small)!\n";
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
            rep = new StringRepresentation(
                    "Error 400: Client Error, Bad Requset\n" +
                    "The request could not be understood by the server due " +
                    "to malformed syntax.\n" +
                    "Details: cache size (in MB) should be an Integer, while you specified " +
                    "a non Integer value : " + cacheSize + "\n\n",
                    errorMediaType);
            setInternalStatus(clientPostedWrongParametersStatus);
        }

        if (!(errorDetails.equalsIgnoreCase(""))) {
            rep = new StringRepresentation("Error Code : " + clientPostedWrongParametersStatus.toString() + "\n" +
                    "Error Code Desription : The request could not be understood by the server due to " +
                    "malformed syntax.\nThe client SHOULD NOT repeat the request without modifications.\n" +
                    "Error Explanation :\n" + errorDetails + "\n", errorMediaType);
            return rep;
        } else {
            return null;
        }
    }
}
