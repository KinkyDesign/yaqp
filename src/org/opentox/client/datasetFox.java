package org.opentox.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.net.HttpURLConnection;
import java.net.URLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.httpclient.HttpStatus;
import org.restlet.Client;
import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.data.Preference;
import org.restlet.data.Protocol;
import org.restlet.data.Reference;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.data.Status;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Attribute;
import weka.core.FastVector;


/**
 * Given a dataset URI on a server that is compliant with the OpenTox
 * specifications, retrieve the compounds, the feature definitions and
 * store the features in a <em>weka.core.Instances</em> object.
 * @author OpenTox - http://www.opentox.org
 * @author Sopasakis Pantelis
 * @author Kolotouros Dimitris
 * @author Sarimveis Harry
 */
public class datasetFox {



    private Instances datasetInstances = new Instances("x", new FastVector(), 1);
    private URL serviceBaseUrl;
    private String datasetID;
    private int statusCode = 0;

    /**
     * You'd better avoid using this constructor!
     */
    public datasetFox(){
        throw new UnsupportedOperationException("datasetFox: Avoid using this constructor!!!");
    }


    /**
     *
     * @param serverURL the URL of the server including the port. The base URL of
     * the server, e.g. "http://ambit.uni-plovdiv.bg:8080/ambit2"
     * @param id the id of the dataset
     */
    public datasetFox(URL serverURL, String id){
        this.serviceBaseUrl = serverURL;
        this.datasetID = id;
    }

    /**
     * Return the Status Code of the Client. Status Codes are summarized as follows:
     * <ul>
     * <li>0: Ok</li>
     * <li>1: The server is probably dead (Not Responding). No Service Found!</li>
     * <li>2: Invalid Dataset URI. Correct URI for datasets but specified Dataset ID not found (404) or
     * service is unavailable (50x).</li>
     * <li>3: DataSet URI responded with a Success Status Code, but no feature definitions found corresponding
     * to that dataset URI, i.e. /dataset/{id}/feature_definitions responded with an error status.</li>
     * <li>4: Dataset URI found (success 20x), feature definitions where also found, but no compounds where
     * detected in this dataset. The URI /dataset/{id}/compound responds with an error code.</li>
     * </ul>
     * @return
     */
    public int getStatusCode(){
        return statusCode;
    }

    /**
     *
     * @return
     */
    public Instances getInstances(){

        /** Chech if the server is alive... **/
        if (isServerAlive(serviceBaseUrl) ){
            
            final Client client = new Client(Protocol.HTTP);
            URL datasetURL = null;
            try {
                datasetURL = new URL(serviceBaseUrl.toString() + "/dataset/" + datasetID + "/");

                /** Check if the specified dataset URI does exist **/
                if (IsServiceAvailable(datasetURL,"text/xml",false)){

                    /** Retrieve all feature definitions and load them in a fast vector **/
                    String featDefsInDataset = serviceBaseUrl + "/dataset/" + datasetID + "/feature_definition/";
                    Request request = new Request();
                    request.setResourceRef(featDefsInDataset);
                    request.setMethod(Method.GET);
                    request.getClientInfo().getAcceptedMediaTypes().add(new
                            Preference<MediaType>(MediaType.TEXT_URI_LIST));
                    Response response = client.handle(request);
                    
                    
                        BufferedReader br = null;
                        FastVector attributes = new FastVector();
                        try {

                             br = new BufferedReader(
                                     new InputStreamReader(
                                     response.getEntity().getStream()));
                             String featDefLine;
                             while ((featDefLine=br.readLine())!=null){
                                 attributes.addElement( new Attribute (featDefLine));
                                 /* Uncomment for debugging only! */ //System.out.println("+"+featDefLine);
                             }

                             // Initialize the dataset (An Instances Object):
                             // Add FDs as attributes
                             datasetInstances = new Instances(datasetURL.toString(), attributes, 0);
                             br.close();

                             // Now search for compounds in the dataset and
                             // for each compound get its features.
                             URL compoundsListURL = new URL(serviceBaseUrl+"/dataset/"+datasetID+"/compound/");
                             
                             /** Check if the compounds service is available(optional)... **/
                             if (IsServiceAvailable(compoundsListURL, "text/uri-list", false)){
                                 request.setResourceRef(compoundsListURL.toString());
                                 response = client.handle(request);
                                 

                                 br = new BufferedReader(
                                     new InputStreamReader(
                                     response.getEntity().getStream()));
                                 String compoundLine=null, feature=null;

                                 
                                 final int numberOfAttributes=datasetInstances.numAttributes();
                                 double[] values = new double[numberOfAttributes];

                                 /** For all compounds... **/
                                 long currentTime=System.currentTimeMillis();
                                 while ((compoundLine=br.readLine())!=null){

                                         /** For all feature definitions **/
                                      for (int i=0;i<numberOfAttributes;i++){

                                             feature=serviceBaseUrl+"/feature/compound/"+
                                                     new Reference(compoundLine).getLastSegment()+
                                                     "/feature_definition/"+
                                                     new Reference(datasetInstances.attribute(i).name()).getLastSegment();
                                             request = new Request();
                                             request.setResourceRef(feature);
                                             request.setMethod(Method.GET);
                                             request.getClientInfo().getAcceptedMediaTypes().add(new
                                                Preference<MediaType>(MediaType.TEXT_PLAIN));
                                             response=client.handle(request);
                                             try{
                                                 values[i]=Double.parseDouble(response.getEntity().getText());
                                             }catch (NumberFormatException nex){
                                                 values[i]=-101.1;
                                             }
                                             datasetInstances.add(new Instance(1.0,values));
                                             response.release();                                                                                       
                                         }
                                 

                                    //     System.out.print("|");
                                  }
                                  //System.out.println((System.currentTimeMillis()-currentTime)/1000.0+"s");
                                 
                             }else{
                                 statusCode=4;
                                 System.err.println("No compounds are available in this dataset!");
                             }



                        } catch (IOException ex) {
                            System.err.println("Error: Invlalid URI or other exception!\nDetails: "+ex.getMessage());
                            Logger.getLogger(datasetFox.class.getName()).log(Level.SEVERE, null, ex);
                        }finally{
                            if (!(br.equals(null))){
                                try {
                                    br.close();
                                } catch (IOException ex) {
                                    System.out.println("IOException caught. Details: "+ex.getMessage());
                                    Logger.getLogger(datasetFox.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                        }

                    

                }else{
                    statusCode=2;
                    System.err.println("Invalid dataset uri : "+datasetURL);
                }
            } catch (MalformedURLException ex) {
                Logger.getLogger(datasetFox.class.getName()).log(Level.SEVERE, null, ex);
            }finally{
                /** Stop the HTTP client! **/
                try {
                    //client.stop();
                } catch (Exception ex) {
                    Logger.getLogger(datasetFox.class.getName()).log(Level.SEVERE, null, ex);
                }
            }                  
            
        }else{
            System.err.println("The server "+serviceBaseUrl.getHost()+", port "+
                    serviceBaseUrl.getPort()+", seems to be dead!");
        }

        
        return datasetInstances;
    }





    /**
     *
     * @param serviceUri the URI of the service
     * @param mime - A valid mime type, e.g. "text/xml" or "text/uri-list" etc
     * @param followRedirects Boolean parameter that specifies whether you want to follow possible redirections.
     * @return true, if the status code is success
     */
private boolean IsServiceAvailable(URL serviceUri, String mime, boolean followRedirects){
            HttpURLConnection.setFollowRedirects(false);
                HttpURLConnection con = null;
            try {
                con = (HttpURLConnection) serviceUri.openConnection();
                con.setDoInput(true);
                con.setDoOutput(true);
                con.setUseCaches(false);
                con.addRequestProperty("Accept", "text/xml");                
                
                if((con.getResponseCode()>=200)&&(con.getResponseCode()<300)){
                    return true;
                }else{
                    return false;
                }                

            } catch (IOException ex) {
                Logger.getLogger(datasetFox.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }

//    return true;
}




    /**
     *
     * @param serviceURL
     * @return
     */
     public static boolean isServerAlive(URL serverUri) {
        java.net.Socket soc = null;
        try {
            java.net.InetAddress addr = java.net.InetAddress.getByName(serverUri.getHost());
            soc = new Socket(serverUri.getHost(), serverUri.getPort());
            return true;
        } catch (java.io.IOException e) {
            return false;
        } finally {
            if (soc != null) {
                try {
                    soc.close();
                } catch (java.io.IOException e) {
                }
            }
        }
    }


     public static void main(String[] args) throws MalformedURLException{
         URL url = new URL("http://ambit.uni-plovdiv.bg:8081/ambit2");
         datasetFox fox = new datasetFox(url, "35");
         System.out.println(fox.getInstances().numInstances());
         //System.out.println(fox.IsServiceAvailable(new URL("http://ambit.uni-plovdiv.bg:8080/ambit2/dataset/999"),"text/xml",false));
     }



}
