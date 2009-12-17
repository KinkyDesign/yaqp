package org.opentox.Resources;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;
import org.opentox.Applications.OpenToxApplication;

/**
 * Every Resource of the package extends this class.
 * @author OpenTox - http://www.opentox.org
 * @author Sopasakis Pantelis
 * @author Sarimveis Harry
 * @version 1.5 (Last Update: Nov 23, 2009)
 */
public abstract class AbstractResource extends ServerResource {

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
        public static final int port = 3000;
        /**
         * The domain name assigned to the IP of the server.
         */
        public static final String domainName = "opentox.ntua.gr";
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
    /**
     * Port used by the web services. This variable has to be overriden if the
     * services are deployed on another domain.
     */
    public static String port = "3000";
    /**
     * Base URI of the web services. This variable has to be overriden if the
     * services are deployed on another domain.
     */
    public static String baseURI = "http://opentox.ntua.gr:" + port;

    @Override
    public void doInit() throws ResourceException {
        super.doInit();
        System.gc();
    }

    /**
     * Returns a Map&lt;String, Set&lt;String&gt;&gt; which maps a <tt>key<tt> to
     * a set of algorithm ids. For example if we want to retrieve the validation
     * algorithms' ids as a Set, that would be:<br/><br/>
     * <code>
     * Set&lt;String&gt; validationAlgorithmsIds = getAlgorithmIdsAsMap().get("validation");<br/>
     * </code><br/>
     * Valid <tt>key</tt> values are classification, regression, featureselection and validation.
     * For example you can get the set of classification algorithms using the following line:
     * <br/><br/>
     * <code>
     * Set&lt;String&gt; ClassificationIdsSet= getAlgorithmIdsAsMap().get("classification");<br/>
     * </code>
     * @return
     */
    protected Map<String, Set<String>> getAlgorithmIdsAsMap() {
        Map<String, Set<String>> map = new HashMap<String, Set<String>>();
        map.put("regression", RegressionAlgorithmsSet());
        map.put("classification", ClassificationAlgorithmsSet());
        map.put("featureselection", FeatureSelectionAlgorithmsSet());
        map.put("validation", ValidationRoutinesSet());
        return map;
    }

    /**
     *
     * @return the set of regression algorithms
     */
    private Set<String> RegressionAlgorithmsSet() {
        Set<String> regressionAlgorithms = new HashSet<String>();
        regressionAlgorithms.add("mlr");
        regressionAlgorithms.add("svm");
        //regressionAlgorithms.add("plsr");
        return regressionAlgorithms;
    }

    /**
     *
     * @return The set of classification algorithms
     */
    private Set<String> ClassificationAlgorithmsSet() {
        Set<String> classificationAlgorithms = new HashSet<String>();
        classificationAlgorithms.add("svc");
        //classificationAlgorithms.add("j48c");
        //classificationAlgorithms.add("plsc");
        //classificationAlgorithms.add("knnc");
        return classificationAlgorithms;
    }

    /**
     *
     * @return The set of all feature selection algorithms
     */
    private Set<String> FeatureSelectionAlgorithmsSet() {
        Set<String> classificationAlgorithms = new HashSet<String>();
        classificationAlgorithms.add("infoGainAttributeEvaluation");
        return classificationAlgorithms;
    }

    /**
     * Returns the set of all algorithms, that is classification, regression
     * and feature selection. Every algorithm has a unique algorithm id.
     * @return The set of all algorithms
     */
    public Set<String> AlgorithmsSet() {
        Set<String> algorithmsSet = new HashSet<String>();
        algorithmsSet.addAll((Collection<String>) ClassificationAlgorithmsSet());
        algorithmsSet.addAll((Collection<String>) RegressionAlgorithmsSet());
        //algorithmsSet.addAll((Collection<String>) FeatureSelectionAlgorithmsSet());
        return algorithmsSet;
    }

    /**
     * Returns the set of all validation routines such as "test_set_validation/svc"
     * and "test_set_validation_mlr".
     * @return The set of validation routines
     */
    private Set<String> ValidationRoutinesSet() {
        Set<String> validationRoutinesSet = new HashSet<String>();
        validationRoutinesSet.add("test_set_validation/svc");
        validationRoutinesSet.add("test_set_validation/mlr");
        return validationRoutinesSet;
    }
}
