/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.opentox.interfaces;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import org.restlet.data.Response;
import org.restlet.resource.ResourceException;

/**
 *
 * @author chung
 */
public interface IFeature extends IProne2Error{

    /**
     * Creates a new Feature and writes it to an OutputStream.
     * @param sameAs Declares a same-as relationship between this feature and some
     * other feature.
     * @param output Outputstream used to write the output.
     */
    void createNewFeature(String sameAs, OutputStream output);

    /**
     * Generates a new Feature and POSTs it to a feature service
     * @param sameAs Declares a same-as relationship between this feature and some
     * other feature.
     * @param featureService Some feature service where the generated feature should be stored.
     * @return The response of the feature service to the request for feature creation.
     */
    Response createNewFeature(String sameAs, URI featureService) throws ResourceException, IOException;

}
