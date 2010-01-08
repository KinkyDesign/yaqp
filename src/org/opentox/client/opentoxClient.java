package org.opentox.client;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URI;
import org.opentox.interfaces.IClient;
import org.restlet.data.MediaType;

/**
 * This class is used to make HTTP requests to other servers.
 * @author OpenTox - http://www.opentox.org
 * @author Sopasakis Pantelis
 * @author Kolotouros Dimitris
 * @author Sarimveis Harry
 */
public class opentoxClient implements IClient {

    private static final long serialVersionUID = -2394809235191723442L;

    private static opentoxClient instanceOfThis = null;

    public static opentoxClient INSTANCE = getInstance();

    private static opentoxClient getInstance() {
        if (instanceOfThis == null) {
            instanceOfThis = new opentoxClient();
        }
        return instanceOfThis;
    }

    private opentoxClient(){
        
    }

    public boolean isServerAlive(URI serverUri, int attempts) throws InterruptedException {
        java.net.Socket soc = null;

        int i = 1;
        boolean isalive = false;
        while (isalive == false) {
            // System.out.println("Attempt : " + i);
            if (i == attempts) {
                break;
            }
            try {
                java.net.InetAddress addr = java.net.InetAddress.getByName(serverUri.getHost());
                soc = new Socket(serverUri.getHost(), serverUri.getPort());
                isalive = true;
            } catch (java.io.IOException e) {
                Thread.sleep(500);
                i++;
                isalive = false;
            }
        }
        if (!(soc == null)) {
            try {
                soc.close();
            } catch (IOException ex) {
            }
        }
        return isalive;

    }

    public boolean IsMimeAvailable(URI serviceUri, MediaType mime, boolean followRedirects) {

        HttpURLConnection.setFollowRedirects(followRedirects);
        HttpURLConnection con = null;
        try {
            con = (HttpURLConnection) serviceUri.toURL().openConnection();
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setUseCaches(false);
            con.addRequestProperty("Accept", mime.toString());

            if ((con.getResponseCode() >= 200) && (con.getResponseCode() < 300)) {
                return true;
            } else {
                return false;
            }

        } catch (IOException ex) {
            return false;
        }
    }

}
