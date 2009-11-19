package org.opentox.Resources.Algorithms;

import java.util.ArrayList;
import org.opentox.Resources.AbstractResource;
import org.restlet.data.MediaType;

/**
 *
 * @author OpenTox - http://www.opentox.org
 * @author Sopasakis Pantelis
 * @author Sarimveis Harry
 */
public class AlgorithmReporter {


    /**
     * XML representation of algorithms.
     */
    public static class XML {



        private static String xmlIntro = org.opentox.Resources.AbstractResource.xmlIntro;

        public static String svmXml() {
            StringBuilder builder = new StringBuilder();
            builder.append(xmlIntro);
            builder.append("<algorithm name=\"Support Vector Machine\" id=\"svm\">\n");
            builder.append("<AlgorithmType>regression</AlgorithmType>\n");
            builder.append("<Parameters>\n");
            builder.append("<!-- \n" +
                    "The id of the dataset used for the training\n" +
                    "of the model\n" +
                    "-->\n");
            builder.append("<param type=\"String\" defaultvalue=\"0\">dataset</param>\n");
            builder.append("<param type=\"String\" defaultvalue=\"0\">target</param>\n");
            builder.append("<param type=\"String\" defaultvalue=\"RBF\">kernel</param>\n");
            builder.append("<param type=\"Double\" defaultvalue=\"10\">cost</param>\n");
            builder.append("<param type=\"Double\" defaultvalue=\"0.1\">epsilon</param>\n");
            builder.append("<param type=\"Double\" defaultvalue=\"1\">gamma</param>\n");
            builder.append("<param type=\"Double\" defaultvalue=\"0\">coeff0</param>\n");
            builder.append("<param type=\"Integer\" defaultvalue=\"3\">degree</param>\n");
            builder.append("<param type=\"Double\" defaultvalue=\"1E-4\">tolerance</param>\n");
            builder.append("<param type=\"Integer\" defaultvalue=\"50\">cacheSize</param>\n");
            builder.append("</Parameters>\n");
            builder.append("<statisticsSupported>\n");
            builder.append("<statistic>RMSE</statistic>\n");
            builder.append("<statistic>MSE</statistic>\n");
            builder.append("</statisticsSupported>\n");
            builder.append("</algorithm>\n\n");
            return builder.toString();
        }

        public static String plsrXml() {
            StringBuilder builder = new StringBuilder();
            builder.append(xmlIntro);
            builder.append("<algorithm name=\"Partial Least Squares\" id=\"pls\">\n");
            builder.append("<AlgorithmType>regression</AlgorithmType>\n");
            builder.append("<Parameters>\n");
            builder.append("<param type=\"String\" defaultvalue=\"0\">dataset</param>\n");
            builder.append("<param type=\"String\" defaultvalue=\"0\">target</param>\n");
            builder.append("<param type=\"Integer\" defaultvalue=\"1\">nComp</param>\n");
            builder.append("</Parameters>\n");
            builder.append("<statisticsSupported>\n");
            builder.append("<statistic>x</statistic>\n");
            builder.append("</statisticsSupported>\n");
            builder.append("</algorithm>\n\n");
            return builder.toString();
        }

        public static String mlrXml() {
            StringBuilder builder = new StringBuilder();
            builder.append(xmlIntro);
            builder.append("<algorithm name=\"Multiple Linear Regression\" id=\"mlr\">\n");
            builder.append("<AlgorithmType>regression</AlgorithmType>\n");
            builder.append("<Parameters>\n");
            builder.append("<!-- \n" +
                    "The id of the dataset used for the training\n" +
                    "of the model\n" +
                    "-->\n");
            builder.append("<param type=\"String\" defaultvalue=\"0\">dataset</param>\n");
            builder.append("<param type=\"String\" defaultvalue=\"0\">target</param>\n");
            builder.append("</Parameters>\n");
            builder.append("<statisticsSupported>\n");
            builder.append("<statistic>RootMeanSquaredError</statistic>\n");
            builder.append("<statistic>RelativeAbsoluteError</statistic>\n");
            builder.append("<statistic>RootRelativeSquaredError</statistic>\n");
            builder.append("<statistic>MeanAbsolutError</statistic>\n");
            builder.append("</statisticsSupported>\n");
            builder.append("</algorithm>\n\n");
            return builder.toString();
        }
    }

    public static class JSON {

        public static String mlrJson() {
            StringBuilder builder = new StringBuilder();
            builder.append("\"Algorithm\": \n{\n");
            builder.append("\"name\" : \"Multiple Linear Regression\",\n");
            builder.append("\"id\" : \"mlr\",\n");
            builder.append("\"AlgorithmType\" : \"regression\",\n");
            builder.append("\"Parameters\" : {\n" +
                    "\"dataset\" : {\"type\" : \"URI\", \"defaultValue\"  : \"\"},\n" +
                    "\"target\"  : {\"type\" : \"URI\",  \"defaultValue\" : \"\"}\n" +
                    "},\n");
            builder.append("\"statisticsSupported\" : {\n");
            builder.append("\"statistic\" : \"RootMeanSquaredError\",\n");
            builder.append("\"statistic\" : \"RelativeAbsoluteError\",\n");
            builder.append("\"statistic\" : \"RootRelativeSquaredError\",\n");
            builder.append("\"statistic\" : \"MeanAbsolutError\"\n");
            builder.append("}\n");
            return builder.toString();
        }

        public static String svmJson() {
            StringBuilder builder = new StringBuilder();
            builder.append("\"Algorithm\": \n{\n");
            builder.append("\"name\" : \"Support Vector Machine\",\n");
            builder.append("\"id\" : \"svm\",\n");
            builder.append("\"AlgorithmType\" : \"regression\",\n");
            builder.append("\"Parameters\" : {\n" +
                    "\"dataset\" : {\"type\" : \"URI\", \"defaultValue\"  : \"\"},\n" +
                    "\"target\"  : {\"type\" : \"URI\",  \"defaultValue\" : \"\"},\n" +
                    "\"kernel\"  : {\"type\" : \"String\",  \"defaultValue\" : \"rbf\"},\n" +
                    "\"cost\"    : {\"type\" : \"Double\",  \"defaultValue\" : \"10\"},\n" +
                    "\"epsilon\" : {\"type\" : \"Double\",  \"defaultValue\" : \"0.1\"},\n" +
                    "\"gamma\"   : {\"type\" : \"Double\",  \"defaultValue\" : \"1\"},\n" +
                    "\"coeff0\"  : {\"type\" : \"Double\",  \"defaultValue\" : \"0\"},\n" +
                    "\"degree\"  : {\"type\" : \"Integer\",  \"defaultValue\" : \"3\"},\n" +
                    "\"tolerance\"  : {\"type\" : \"Double\",  \"defaultValue\" : \"1E-4\"},\n" +
                    "\"cacheSize\"  : {\"type\" : \"Integer\",  \"defaultValue\" : \"50\"}\n" +
                    "},\n");
            builder.append("\"statisticsSupported\" : {\n");
            builder.append("\"statistic\" : \"RootMeanSquaredError\",\n");
            builder.append("\"statistic\" : \"RelativeAbsoluteError\",\n");
            builder.append("\"statistic\" : \"RootRelativeSquaredError\",\n");
            builder.append("\"statistic\" : \"MeanAbsolutError\"\n");
            builder.append("}\n");
            return builder.toString();
        }
    }

    
    public static class RDF_XML {

        

        public static String mlrRdf() {
            AlgorithmRdfFormater mlrRdf = new AlgorithmRdfFormater(AbstractResource.URIs.mlrAlgorithmURI);
            mlrRdf.setTitle("Multiple Linear Regression");
            mlrRdf.setSubject("MLR, regression, training algorithm");
            mlrRdf.setDescription("Algorithm for the training of Multiple Linear Regression Models");
            mlrRdf.setType("http://purl.org/dc/dcmitype/Service");
            mlrRdf.setSource(AbstractResource.URIs.learningAlgorithmURI);
            mlrRdf.setRelation(AbstractResource.URIs.regressionAlgorithmURI);
            mlrRdf.setRights(AbstractResource.URIs.licenceUri);
            mlrRdf.setLanguage("en");
            mlrRdf.setCreator(AbstractResource.URIs.baseURI);
            mlrRdf.setPublisher(AbstractResource.URIs.baseURI);
            mlrRdf.setContributor("http://opentox.org/");
            mlrRdf.setDate("2009-11-18");
            mlrRdf.setFormat(MediaType.TEXT_XML.toString());
            mlrRdf.setIdentifier(AbstractResource.URIs.mlrAlgorithmURI);
            mlrRdf.setAudience("");
            mlrRdf.setProvenance("");
            mlrRdf.setAlgorithmType("Algorithm:MLDMTox:Learning:Regression:eager:1_target_variable:mlr");
            ArrayList<String> statisticsSupported = new ArrayList<String>();
            statisticsSupported.add("RootMeanSquaredError");
            statisticsSupported.add("RelativeAbsoluteError");
            statisticsSupported.add("RootRelativeSquaredError");
            statisticsSupported.add("MeanAbsoluteError");
            String[][] Parameters = {
                {"dataset", "String","null"},
                {"target",  "String","null"}
            };
            mlrRdf.setAlgorithm(statisticsSupported, Parameters);
            return mlrRdf.rdfXmlRepresentation();
            
        }

        public static String svmRdf() {
            AlgorithmRdfFormater svmRdf = new AlgorithmRdfFormater(AbstractResource.URIs.mlrAlgorithmURI);
            svmRdf.setTitle("Support Vector Machine Regression");
            svmRdf.setSubject("SVM, support vector machine, training algorithm, machine learning");
            svmRdf.setDescription("Algorithm for the training of SVM Regression Models");
            svmRdf.setType("http://purl.org/dc/dcmitype/Service");
            svmRdf.setSource(AbstractResource.URIs.learningAlgorithmURI);
            svmRdf.setRelation(AbstractResource.URIs.regressionAlgorithmURI);
            svmRdf.setRights(AbstractResource.URIs.licenceUri);
            svmRdf.setLanguage("en");
            svmRdf.setCreator(AbstractResource.URIs.baseURI);
            svmRdf.setPublisher(AbstractResource.URIs.baseURI);
            svmRdf.setContributor("http://opentox.org/");
            svmRdf.setDate("2009-11-18");
            svmRdf.setFormat(MediaType.TEXT_XML.toString());
            svmRdf.setIdentifier(AbstractResource.URIs.svmAlgorithmURI);
            svmRdf.setAudience("");
            svmRdf.setProvenance("");
            svmRdf.setAlgorithmType("Algorithm:MLDMTox:Learning:Regression:eager:1_target_variable:svm");
            ArrayList<String> statisticsSupported = new ArrayList<String>();
            statisticsSupported.add("RootMeanSquaredError");
            statisticsSupported.add("RelativeAbsoluteError");
            statisticsSupported.add("RootRelativeSquaredError");
            statisticsSupported.add("MeanAbsoluteError");
            String[][] Parameters = {
                {"dataset", "String","null"},
                {"target",  "String","null"},
                {"kernel",  "List:{rbf,linear,sigmoid,polynomial}"  , "rbf"},
                {"cost",    "Double", "10"},
                {"epsilon", "Double", "0.1"},
                {"gamma", "Double", "1"},
                {"coeff0", "Double", "0"},
                {"Degree", "Integer", "3"},
                {"tolerance", "Double", "1E-4"},
                {"cacheSize", "Integer", "50"}
            };
            svmRdf.setAlgorithm(statisticsSupported, Parameters);
            return svmRdf.rdfXmlRepresentation();
            
        }
    }

    public static class YAML {

        public static String mlrYaml() {
            StringBuilder builder = new StringBuilder();
            builder.append("---\nAlgorithm:\n");
            builder.append("    name : Multiple Linear Regression\n");
            builder.append("    id : mlr\n");
            builder.append("    AlgorithmType : regression\n");
            builder.append("    Parameters:\n");
            builder.append("        -dataset:\n");
            builder.append("            type:URI\n");
            builder.append("            defaultValue:none\n");
            builder.append("        -target:\n");
            builder.append("            type:URI\n");
            builder.append("            defaultValue:none\n");
            builder.append("    statisticsSupported:\n");
            builder.append("            -RootMeanSquaredError\n");
            builder.append("            -RelativeAbsoluteError\n");
            builder.append("            -RootRelativeSquaredError\n");
            builder.append("            -MeanAbsolutError\n");
            return builder.toString();
        }

        public static String svmYaml() {
            StringBuilder builder = new StringBuilder();
            builder.append("---\nAlgorithm:\n");
            builder.append("    name : Support Vector Machine\n");
            builder.append("    id : mlr\n");
            builder.append("    AlgorithmType : regression\n");
            builder.append("    Parameters:\n");
            builder.append("        -dataset:\n");
            builder.append("            type:URI\n");
            builder.append("            defaultValue:none\n");
            builder.append("        -target:\n");
            builder.append("            type:URI\n");
            builder.append("            defaultValue:none\n");
            builder.append("        -kernel:\n");
            builder.append("            type:String\n");
            builder.append("            defaultValue:rbf\n");
            builder.append("        -cost:\n");
            builder.append("            type:Double\n");
            builder.append("            defaultValue:10\n");
            builder.append("        -epsilon:\n");
            builder.append("            type:Double\n");
            builder.append("            defaultValue:0.1\n");
            builder.append("        -gamma:\n");
            builder.append("            type:Double\n");
            builder.append("            defaultValue:1\n");
            builder.append("        -coeff0:\n");
            builder.append("            type:Double\n");
            builder.append("            defaultValue:0\n");
            builder.append("        -degree:\n");
            builder.append("            type:Integer\n");
            builder.append("            defaultValue:3\n");
            builder.append("        -tolerance:\n");
            builder.append("            type:Double\n");
            builder.append("            defaultValue:0.1\n");
            builder.append("        -cacheSize:\n");
            builder.append("            type:Integer\n");
            builder.append("            defaultValue:50\n");
            builder.append("    statisticsSupported:\n");
            builder.append("            -RootMeanSquaredError\n");
            builder.append("            -RelativeAbsoluteError\n");
            builder.append("            -RootRelativeSquaredError\n");
            builder.append("            -MeanAbsolutError\n");
            return builder.toString();
        }
    }

    public static class TURTLE{

        public static String mlrTurtle(){


            return null;
        }

        public static String svmTurtle(){
            return null;
        }

    }
}
