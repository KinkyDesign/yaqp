package org.opentox.resource;

import java.io.File;
import org.restlet.resource.ServerResource;
import org.opentox.OpenToxApplication;
import org.opentox.Server;

/**
 * Every Resource of the package extends this class.
 * @author OpenTox - http://www.opentox.org/
 * @author Sopasakis Pantelis
 * @author Sarimveis Harry
 * @version 1.3.3 (Last update: Dec 20, 2009)
 */
public class OTResource extends ServerResource {

    /**
     * version unique serial number.
     */
    private static final long serialVersionUID = 1048422959964500138L;

    /**
     * URIs of the available services.
     */
    public static final class URIs {

        /**
         * Opentox Project URI
         */
        public static final String OpentoxUri = "http://opentox.org/";

        /**
         * Server Port on which the services are available.
         */
        public static final String port = Server.__PORT_;
        /**
         * The domain name assigned to the IP of the server.
         */
        public static final String domainName = Server.__DOMAIN_NAME_;
        /**
         * Full URI of the service.
         */
        public static final String baseURI = "http://" + domainName + ":" + port;
        /**
         * URI for all models stored in the server.
         */
        public static final String modelURI = baseURI + "/model";
        /**
         * URI for all algorithms.
         */
        public static final String algorithmURI = baseURI + "/algorithm";
        /*
         * URI for Support vector classifier.
         */
        public static final String svcAlgorithmURI = algorithmURI + "/svc";
        /**
         * URI for Support vector machine regression algorithm.
         */
        public static final String svmAlgorithmURI = algorithmURI + "/svm";
        /**
         * URI for Multiple Linear Regression Algorithm.
         */
        public static final String mlrAlgorithmURI = algorithmURI + "/mlr";
        public static final String licenceUri = "http://www.gnu.org/licenses/gpl.txt";
    }

    /**
     * Directories on the server.
     * <pre>
     *
     * /OpenToxServer
     *      |
     *      |--> /log
     *      |--> /model
     *      |       |-->/rdf
     *      |       |-->/pmml
     *      |       |-->/weka
     *      |-->/data
     *            |-->/scaled
     *            |-->/ranges
     *            |-->/dsd
     * </pre>
     */
    public static class Directories {

        /**
         * Base Directory for all files the services needs to store or to read from.
         */
        private static final String baseDir = System.getProperty("user.home") + "/OpenToxServer";
        /**
         * Folder where server logs are stored.
         */
        public static final String logDir = baseDir + "/log";
        /**
         * Folder where all model-related files are stored.
         */
        private static final String modelDir = baseDir + "/model";
        /**
         * Folder where all RDF representations of models are stored.
         * Each model has an RDF file stored in that folder.
         */
        public static final String modelRdfDir = modelDir + "/rdf";
        /**
         * Folder where all PMML documents are stored.
         */
        public static final String modelPmmlDir = modelDir + "/pmml";
        /**
         * Folder where all RAW (dsd-like) files are stored.
         */
        public static final String modelWekaDir = modelDir + "/weka";
        /**
         * Directory of data files.
         */
        public static final String dataDir = baseDir + "/data";
        
        /**
         * Checks if the necessary directories already exist.
         */
        public static void checkDirs() {
            if (!(new File(modelDir)).exists()) {
                new File(modelDir).mkdirs();
                new File(modelRdfDir).mkdirs();
                new File(modelPmmlDir).mkdirs();
                new File(modelWekaDir).mkdirs();
                OpenToxApplication.opentoxLogger.warning("No Model Folders Found and they were created!");
            }

            if (!(new File(dataDir)).exists()) {
                new File(dataDir).mkdirs();        
                OpenToxApplication.opentoxLogger.warning("No Model Folders Found and they were created!");
            }
        }
    }
    /**
     * The first line of every XML file.
     */
    public static final String xmlIntro = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n";
    /**
     * Head of HTML files
     */
    protected String htmlHEAD = "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\""
            + "\"http://www.w3.org/TR/html4/loose.dtd\">\n"
            + "<html>\n<head>\n<title>NTUA - RESTful Web Services</title></head><body>";
    /**
     * End of HTML files
     */
    protected static final String htmlEND = "</body>\n</html>";
    /**
     * Head of PMML files
     */
    public static final String PMMLIntro = "<PMML version=\"3.2\" "
            + " xmlns=\"http://www.dmg.org/PMML-3_2\"  "
            + " xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n"
            + " <Header copyright=\"Copyleft (c) OpenTox - An Open Source Predictive Toxicology Framework, http://www.opentox.org, 2009\" />\n";
    
        
    

}
