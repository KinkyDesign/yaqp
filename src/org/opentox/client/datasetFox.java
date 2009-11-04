package org.opentox.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.opentox.MediaTypes.OpenToxMediaType;
import org.restlet.data.MediaType;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;
import weka.core.Instances;

/**
 * This class contains methods that facilitate the restful methods on dataset
 * URIs.
 * @author OpenTox - http://www.opentox.org
 * @author Sopasakis Pantelis
 * @author Kolotouros Dimitris
 * @author Sarimveis Harry
 */
public class datasetFox extends Thread {

    private static final long serialVersionUID = -2854689332253394849L;

    private Instances datasetInstances = null;
    private URI serviceBaseUri = null;
    private URI datasetUri = null;
    private String datasetID = null;     
    private int statusCode = -1;

    
    /**
     * Once a datasetFox object is created, it is examined whether the server is alive
     * and the dataset is valid. Otherwise an exception is thrown.
     * @param serverURL the URL of the server including the port. The base URL of
     * the server, e.g. "http://ambit.uni-plovdiv.bg:8080/ambit2"
     * @param id the id of the dataset
     */
    public datasetFox(URI serverURI, String id) {
        this.serviceBaseUri = serverURI;
        this.datasetID = id;
        try{
            this.datasetUri = new URI(serverURI.toString() + "/dataset/" + id);
        }catch(URISyntaxException e){

        }
    }

    /**
     * Return the Status Code of the Client. 
     * @return
     */
    public int getStatusCode() {
        return statusCode;
    }

    /**
     *
     * @return
     */
    public synchronized Instances getInstances() {
        return datasetInstances;
    }

    
    
    /**
     * @param serviceURL
     * @return 
     */
    
    /**
     * Constructs the Instances object. The dataset id and the server base URI must have been specified in the
     * constructor of the dataset object. Example of use:<br/><br/>
     * <code>URI uri = new URI("http://ambit.uni-plovdiv.bg:8080/ambit2");<br/>
     * dataseFox dsf = new datasetFox(url,"8");<br/>dsf.start();<br/>
     * Instances myData = dsf.getInstances();</code>
     * <br/><br/>
     * <b>Note:</b> The url should not end with a /
     */
    @Override
    public synchronized void run() {
        try {
            if (opentoxClient.isServerAlive(serviceBaseUri, 20)) {
                if (opentoxClient.IsMimeAvailable(datasetUri, OpenToxMediaType.TEXT_ARFF, false)){
                    try {
                        ClientResource client = new ClientResource(new URI(datasetUri.toString()));
                        try {
                            try {
                                InputStream in = client.get(OpenToxMediaType.TEXT_ARFF).getStream();
                                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                                BufferedWriter bw = new BufferedWriter(new FileWriter("temp.arff"));
                                String line = "";
                                while ((line=br.readLine())!=null){
                                    bw.write(line+"\n");
                                }
                                br.close();
                                in.close();
                                bw.flush();
                                bw.close();
                            } catch (IOException ex) {
                                Logger.getLogger(datasetFox.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        } catch (ResourceException ex) {
                            Logger.getLogger(datasetFox.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } catch (URISyntaxException ex) {
                        Logger.getLogger(datasetFox.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }else{
                    statusCode = 2;
                    System.out.println("No text/arff representation for this model");
                }

            }else{
                statusCode = 1;
                System.out.println("The server seems to be dead at the moment!");
            }
        } catch (InterruptedException e) {
        }
    }



    /**
     * This is used only for testing purposes!
     * @param args
     * @throws InterruptedException
     * @throws IOException
     * @throws URISyntaxException
     */
    public static void main(String[] args) throws  InterruptedException, IOException, URISyntaxException {
        URI url = new URI("http://ambit.uni-plovdiv.bg:8080/ambit2");
        datasetFox dsf = new datasetFox(url, "7");
        dsf.start();

    }

}
