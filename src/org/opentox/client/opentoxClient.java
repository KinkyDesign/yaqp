package org.opentox.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.opentox.media.OpenToxMediaType;
import org.restlet.data.MediaType;
import weka.core.Instances;

/**
 * This class is used to make HTTP requests to other servers.
 * @author OpenTox - http://www.opentox.org
 * @author Sopasakis Pantelis
 * @author Kolotouros Dimitris
 * @author Sarimveis Harry
 */
public class opentoxClient implements AbstractClient{

    private static final long serialVersionUID = -2394809235191723442L;


      public synchronized static boolean isServerAlive(URI serverUri, int attempts) throws InterruptedException {
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


      public synchronized static boolean IsMimeAvailable(URI serviceUri, MediaType mime, boolean followRedirects) {

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


      public static Instances getInstances(URI uri) {
        HttpURLConnection.setFollowRedirects(false);
        HttpURLConnection con = null;
        boolean isMimeAvailable; //?????
        try {
            con = (HttpURLConnection) uri.toURL().openConnection();
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setUseCaches(false);
            con.addRequestProperty("Accept", OpenToxMediaType.TEXT_YAML.toString());


            if ((con.getResponseCode() >= 200) && (con.getResponseCode() < 300)) {
                isMimeAvailable=true;
            } else {
                isMimeAvailable=false;
            }

        } catch (IOException ex) {
            /** Cannot read from that resource**/
            isMimeAvailable=false;
        }
        try {
            Instances data = new Instances(new BufferedReader(new InputStreamReader(con.getInputStream())));
            return data;
        } catch (IOException ex) {
            /** No ARFF representations available... **/
            Logger.getLogger(opentoxClient.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
          
      }


}
