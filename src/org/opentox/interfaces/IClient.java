package org.opentox.interfaces;

import java.net.URI;
import org.restlet.data.MediaType;

/**
 * For future developement.
 * @author OpenTox - http://www.opentox.org
 * @author Sopasakis Pantelis
 * @author Sarimveis Harry
 */
public interface IClient {

    public boolean isServerAlive(URI serverUri, int attempts) throws InterruptedException;

    public boolean IsMimeAvailable(URI serviceUri, MediaType mime, boolean followRedirects);
}
