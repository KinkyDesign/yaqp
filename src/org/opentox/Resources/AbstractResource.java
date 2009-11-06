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
 * @version 1.4 (Last Update: Aug 26, 2009)
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
        private static final String algorithmURI = baseURI + "/algorithm";
        /**
         * URI for all learning algorithms.
         */
        private static final String learningAlgorithmURI = algorithmURI + "/learning";
        /**
         * URI for all classification algorithms.
         */
        private static final String classificationAlgorithmURI = learningAlgorithmURI + "/classification";
        /**
         * URI for all regression algorithms.
         */
        private static final String regressionAlgorithmURI = learningAlgorithmURI + "/regression";

        /*
         * URI for Support vector classifier.
         */
        public static final String svcAlgorithmURI = classificationAlgorithmURI + "/svc";
        /**
         * URI for Support vector machine regression algorithm.
         */
        public static final String svmAlgorithmURI = regressionAlgorithmURI + "/svm";
        /**
         * URI for Multiple Linear Regression Algorithm.
         */
        public static final String mlrAlgorithmURI = regressionAlgorithmURI + "/mlr";
    }

    /**
     * Directories on the server.
     */
    public static class Directories {

        /**
         * Base Directory for all files the services needs to store or to read from.
         */
        private static final String baseDir = System.getProperty("user.home") + "/OpenToxServer";
        /**
         * Server Logs.
         */
        public static final String logDir = baseDir + "/log";
        private static final String tempDir = baseDir + "/.temp";
        public static final String tempScaledDir = tempDir +"/.scaled";
        private static final String modelDir = baseDir + "/model";
        public static final String trash = modelDir+"/.trash";
        public static final String modelXmlDir = modelDir + "/xml";
        private static final String classificationModel = modelDir + "/classification";
        private static final String regressionModel = modelDir + "/regression";
        public static final String svcModel = classificationModel + "/svc";
        public static final String svmModel = regressionModel + "/svm";
        public static final String mlrModel = regressionModel + "/mlr";

        /**
         * Checks if the necessary directories already exist.
         * @param createFolders specifies whether folders that do not exist will
         * be created.
         * @return true if the directories exist.
         */
        public static void checkDirs() {
            if (!(new File(baseDir)).exists()) {
                new File(baseDir).mkdirs();
                new File(logDir).mkdirs();
                new File(modelDir).mkdirs();
                new File(modelXmlDir).mkdirs();
                new File(classificationModel).mkdirs();
                new File(regressionModel).mkdirs();
                new File(svcModel).mkdirs();
                new File(svmModel).mkdirs();
                new File(mlrModel).mkdirs();
                OpenToxApplication.opentoxLogger.warning("No Data Folders Found and they were created!");
            } else {
                if (!(new File(logDir)).exists()) {
                    new File(logDir).mkdirs();
                    OpenToxApplication.opentoxLogger.warning("The /log folder was not found and it was created!");
                }
                if (!(new File(modelDir)).exists()) {
                    new File(modelDir).mkdirs();
                    new File(classificationModel).mkdirs();
                    new File(regressionModel).mkdirs();
                    new File(svcModel).mkdirs();
                    new File(svmModel).mkdirs();
                    new File(mlrModel).mkdirs();
                    new File(modelXmlDir).mkdirs();
                    OpenToxApplication.opentoxLogger.warning("The /model folder was not found and it was created" +
                            "with all its subfolders!");
                } else {
                    if (!(new File(modelXmlDir)).exists()) {
                        new File(modelXmlDir).mkdirs();
                        OpenToxApplication.opentoxLogger.warning("The /model/xml folder was not found and it was created!");
                    }
                    if (!(new File(classificationModel)).exists()) {
                        new File(classificationModel).mkdirs();
                        new File(svcModel).mkdirs();
                        OpenToxApplication.opentoxLogger.warning("The /model/classification folder was not " +
                                "found and it was created with all its subfolders!");
                    } else {
                        if (!(new File(svcModel)).exists()) {
                            new File(svcModel).mkdirs();
                            OpenToxApplication.opentoxLogger.warning("The /model/classification/svc " +
                                    "folder was not found and it was created!");
                        }

                    }
                    if (!(new File(regressionModel)).exists()) {
                        new File(regressionModel).mkdirs();
                        new File(svmModel).mkdirs();
                        new File(mlrModel).mkdirs();
                        OpenToxApplication.opentoxLogger.warning("The /model/regression folder was not " +
                                "found and it was created with all its subfolders!");
                    } else {
                        if (!(new File(svmModel)).exists()) {
                            new File(svmModel).mkdirs();
                            OpenToxApplication.opentoxLogger.warning("The /model/regression/svm " +
                                    "folder was not found and it was created!");
                        }
                        if (!(new File(mlrModel)).exists()) {
                            new File(mlrModel).mkdirs();
                            OpenToxApplication.opentoxLogger.warning("The /model/regression/mlr " +
                                    "folder was not found and it was created!");
                        }
                    }

                }
                

            }

        }
    }

    /**
     * The first line of every XML file.
     */
    protected static final String xmlIntro = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n";
    /**
     * Head of HTML files
     */
    protected String htmlHEAD = "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\"" +
            "\"http://www.w3.org/TR/html4/loose.dtd\">\n" +
            "<html>\n<head>\n<title>NTUA - RESTful Web Services</title></head><body>";
    /**
     * End of HTML files
     */
    protected static final String htmlEND = "</body>\n</html>";
    /**
     * Head of PMML files
     */
    protected static final String PMMLIntro = "<PMML version=\"3.2\" " +
            " xmlns=\"http://www.dmg.org/PMML-3_2\"  " +
            " xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" +
            " <Header copyright=\"Copyleft (c) OpenTox - An Open Source Predictive Toxicology Framework, http://www.opentox.org, 2009\" />\n";

    private static final String baseDir = System.getProperty("user.home") + "/Documents/RESTfulWebServices";
    /**
     * Log
     */
    public static final String logDir = baseDir + "/log";
    /**
     * Data directory.
     * Directory where users have permission to upload data files.
     */
    protected static final String uploadDir = baseDir + "/uploads/data";
    /**
     * Repository for arff files uploaded by the users.
     */
    protected static final String arffDir = uploadDir + "/arff";
    /**
     * Repository for xrff files
     */
    protected static final String xrffDir = uploadDir + "/xrff";
    /**
     * Repository for the generated DSD files which are automatically
     * generated each time a user uploads an ARFF file.
     */
    protected static final String dsdDir = uploadDir + "/dsd";
    /**
     * Repository for the generated meta-inf XML files which are automatically
     * generated each time a user uploads an ARFF file.
     */
    protected static final String metaDir = uploadDir + "/meta";
    /**
     * Repository for the generated scaled-DSD files which are automatically
     * generated each time a user uploads an ARFF file.
     */
    protected static final String scaledDir = uploadDir + "/scaled";
    /**
     * Repository for the generated range files which are automatically
     * generated each time a user uploads an ARFF file.
     */
    protected static final String rangeDir = uploadDir + "/range";
    /**
     * Repository for classification and regression models.
     */
    private static final String modelsDir = baseDir + "/models";
    /**
     * Repository for xml representations of models
     *
     */
    public static final String modelsXmlDir = modelsDir + "/xml";
    /**
     * Repository for classification models.
     */
    @Deprecated
    private static final String CLS_modelsDir = modelsDir + "/classification";
    /**
     * Repository for regreesion models.
     */
    @Deprecated
    private static final String REG_modelsDir = modelsDir + "/regression";
    /**
     * Repository for svc (classification models).
     */
    @Deprecated
    protected static final String CLS_SVM_modelsDir = CLS_modelsDir + "/svc";
    /**
     * Repository for svm regression models
     */
    @Deprecated
    protected static final String REG_SVM_modelsDir = REG_modelsDir + "/svm";
    /**
     * Repository for MLR regression models. The MLR models generated by the
     * users are stored in this directory.
     */
    @Deprecated
    protected static final String REG_MLR_modelsDir = REG_modelsDir + "/mlr";
    /**
     * Prefix of uploaded arff files
     */
    @Deprecated
    protected static final String dataSetPrefix = "dataSet-";
    /**
     * Prefix of all models.
     */
    @Deprecated
    protected static final String modelPrefix = "model-";
    /**
     * General Prefix.
     */
    @Deprecated
    protected static final String OpenToxPrefix = "OpenTox-";
    /**
     * Prefix of validation results.
     */
    @Deprecated
    protected static final String validationResultPrefix = "validation-";
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
    public static final String ModelURI = baseURI + "/model";
    /**
     * Server IP.
     */
    @Deprecated
    protected static String serverIP = "147.102.82.32";
    /**
     * Validation Results.
     */
    @Deprecated
    private static final String validationResultsDir = baseDir + "/validationResults";
    /**
     * Regression Validation Results.
     */
    @Deprecated
    private static final String RegressionValidationResultsDir = validationResultsDir + "/regression";
    /**
     * Classification Validation Results.
     */
    @Deprecated
    private static final String ClassificationValidationResultsDir = validationResultsDir + "/classification";
    /**
     * Path to the repository of validation results for MLR regression models.
     */
    @Deprecated
    protected static final String MlrValidationResultsDir = RegressionValidationResultsDir + "/mlr";
    /**
     * Path to the repository of validation results for SVC classification
     * models.
     */
    @Deprecated
    protected static final String SvcValidationResultsDir = ClassificationValidationResultsDir + "/svc";
    /**
     * Path to the repository of static files (HTML, CSS and other Static content)
     */
    @Deprecated
    private static final String StaticDir = baseDir + "/Static";
    /**
     * Path to the repository of HTML files (Web Interface)
     */
    @Deprecated
    public static final String HTMLDir = StaticDir + "/HTML";
    /**
     * Path to the javadoc directory
     */
    @Deprecated
    public static final String javadocDir = StaticDir + "/javadoc";

    @Override
    public void doInit() throws ResourceException {
        super.doInit();
        System.gc();
    }

    /**
     * Class Constructor.
     * @param context
     * @param request
     * @param response
     */
//    public AbstractResource(Context context, Request request,
//            Response response)
//    {
//        super(context, request, response);
//
//        logger=Logger.getLogger(getClass().getName());
//        logger.setUseParentHandlers(false);
//        ConsoleHandler ch = new ConsoleHandler();
//        logger.addHandler(ch);
//        System.gc();
//    }
    protected String getBaseDirectory() {
        return baseDir;
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
    private Set<String> AlgorithmsSet() {
        Set<String> algorithmsSet = new HashSet<String>();
        algorithmsSet.addAll((Collection<String>) ClassificationAlgorithmsSet());
        algorithmsSet.addAll((Collection<String>) RegressionAlgorithmsSet());
        algorithmsSet.addAll((Collection<String>) FeatureSelectionAlgorithmsSet());
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
