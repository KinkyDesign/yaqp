package org.opentox.interfaces;

import java.net.URI;
import org.restlet.data.MediaType;

/**
 * Interface for a general use client which manages the access to other web resources.
 * @author OpenTox - http://www.opentox.org
 * @aut hor Sopasakis Pantelis
 * @author Sarimveis Harry
 */
public interface IClient {

    /**
     * Checks if a server is alive.
     * @param serverUri The URI of the server as a {@link java.net.URI }
     * @param attempts Number of attempts. In case of failure on the first try
     * , defines how many times the client should repeat the request (if possible)
     * until a positive response is returned from the server, otherwise the method
     * returns {@link Boolean#FALSE }.
     * @return True if the server identified by the provided URI is alive; false otherwise.
     * @throws InterruptedException 
     */
    public boolean isServerAlive(URI serverUri, int attempts) throws InterruptedException;

    /**
     * Checks if the specified MIME is available at the given URI.
     * @param serviceUri URI of the service.
     * @param mime Prefered MIME type.
     * @param followRedirects Whether to follow redirections.
     * @return True if the prefered MIME is supported.
     */
    public boolean IsMimeAvailable(URI serviceUri, MediaType mime, boolean followRedirects);

}
