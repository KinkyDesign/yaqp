package org.opentox.Resources.Algorithms;

import java.net.URI;
import java.net.URISyntaxException;
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
public class MlrTrainer extends AbstractTrainer {

    protected Instances data;

    
    
    public MlrTrainer(Form form) {
        super(form);
    }


    @Override
    public Representation train() {
        throw new UnsupportedOperationException("Not supported yet.");
    }


    /**
     * Check wether the dataset and target values are valid URIs.
     * @return
     */
    @Override
    public Representation checkParameters() {
        Representation rep = null;
        MediaType errorMediaType = MediaType.TEXT_PLAIN;
        Status clientPostedWrongParametersStatus = Status.CLIENT_ERROR_BAD_REQUEST;
        String errorDetails = "";
        setInternalStatus(Status.SUCCESS_ACCEPTED);


        try {
            dataseturi = new URI(form.getFirstValue("dataset"));
        } catch (URISyntaxException ex) {
            setInternalStatus(clientPostedWrongParametersStatus);
            errorDetails = errorDetails + "[Wrong Posted Parameter ]: The client did" +
                    " not post a valid URI for the dataset";
        }
        try {
            targeturi = new URI(form.getFirstValue("target"));
        } catch (URISyntaxException ex) {
            setInternalStatus(clientPostedWrongParametersStatus);
            errorDetails = errorDetails + "[Wrong Posted Parameter ]: The client did" +
                    " not post a valid URI for the target feature";
        }
        if (!(errorDetails.equalsIgnoreCase(""))) {
            rep = new StringRepresentation(
                    "Error Code            : " + clientPostedWrongParametersStatus.toString() + "\n" +
                    "Error Code Desription : The request could not be understood by the server due to " +
                    "malformed syntax.\nThe client SHOULD NOT repeat the request without modifications.\n" +
                    "Error Explanation     :\n" + errorDetails + "\n", errorMediaType);
            return rep;
        } else {
            return null;
        }
    }
}
