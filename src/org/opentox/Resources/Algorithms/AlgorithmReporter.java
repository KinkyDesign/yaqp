package org.opentox.Resources.Algorithms;

import java.util.ArrayList;
import org.opentox.MediaTypes.OpenToxMediaType;
import org.opentox.Resources.AbstractResource;
import org.restlet.data.MediaType;
import org.restlet.representation.StringRepresentation;

/**
 *
 * @author OpenTox - http://www.opentox.org
 * @author Sopasakis Pantelis
 * @author Sarimveis Harry
 */
public class AlgorithmReporter {

    public static final long
            SVM = 32847182479542L,
            SVC = 7734871623000153L,
            MLR = -87319812475327L;

    private static final String
            RegressionOntology = "Algorithm:MLDMTox:Learning:Regression:eager:1_target_variable";

    private static ArrayList<String> statisticsSupported(long algorithm) {
        ArrayList<String> statisticsSupported = new ArrayList<String>();
        statisticsSupported.add("RootMeanSquaredError");
        statisticsSupported.add("RelativeAbsoluteError");
        statisticsSupported.add("RootRelativeSquaredError");
        statisticsSupported.add("MeanAbsoluteError");
        return statisticsSupported;
    }

    private static String[][] Parameters(long algorithm) {
        String[][] Parameters = null;
        if (algorithm == SVM) {
            Parameters = new String[][]{
                        {"dataset", "String", "null"},
                        {"target", "String", "null"},
                        {"kernel", "List:{rbf,linear,sigmoid,polynomial}", "rbf"},
                        {"cost", "Double", "10"},
                        {"epsilon", "Double", "0.1"},
                        {"gamma", "Double", "1"},
                        {"coeff0", "Double", "0"},
                        {"Degree", "Integer", "3"},
                        {"tolerance", "Double", "1E-4"},
                        {"cacheSize", "Integer", "50"}
                    };
        } else if (algorithm == SVC) {
            Parameters = new String[][]{
                        {"dataset", "String", "null"},
                        {"target", "String", "null"},
                        {"kernel", "List:{rbf,linear,sigmoid,polynomial}", "rbf"},
                        {"cost", "Double", "10"},
                        {"gamma", "Double", "1"},
                        {"coeff0", "Double", "0"},
                        {"Degree", "Integer", "3"},
                        {"tolerance", "Double", "1E-4"},
                        {"cacheSize", "Integer", "50"}
                    };
        } else if (algorithm == MLR) {
            Parameters = new String[][]{
                        {"dataset", "String", "null"},
                        {"target", "String", "null"}
                    };
        }
        return Parameters;
    }



    private StringRepresentation getStringRepresentationForMetaInf(AlgorithmMetaInf metainf, MediaType media){
        StringRepresentation representation = null;
        if (MediaType.APPLICATION_RDF_XML.equals(media)) {
                AlgorithmRdfFormater formater = new AlgorithmRdfFormater(metainf);
                representation = formater.getStringRepresentation();
            } else if (MediaType.APPLICATION_JSON.equals(media)) {
                AlgorithmJsonFormater formater = new AlgorithmJsonFormater(metainf);
                representation = formater.getStringRepresentation();
            } else if (MediaType.APPLICATION_RDF_TURTLE.equals(media)) {
                AlgorithmTurtleFormater formater = new AlgorithmTurtleFormater(metainf);
                representation = formater.getStringRepresentation();
            } else if (MediaType.TEXT_XML.equals(media)) {
                AlgorithmXmlFormater formater = new AlgorithmXmlFormater(metainf);
                representation = formater.getStringRepresentation();
            } else if (OpenToxMediaType.TEXT_YAML.equals(media)) {
                AlgorithmYamlFormater formater = new AlgorithmYamlFormater(metainf);
                representation = formater.getStringRepresentation();
            } else {
            }
        return representation;
    }



    public StringRepresentation FormatedRepresntation(MediaType media, long algorithm) {

        StringRepresentation representation = null;

        AlgorithmMetaInf genericMetaInf = new AlgorithmMetaInf();
        genericMetaInf.setType("http://purl.org/dc/dcmitype/Service");
        genericMetaInf.setRights(AbstractResource.URIs.licenceUri);
        genericMetaInf.setLanguage("en");
        genericMetaInf.setCreator(AbstractResource.URIs.baseURI);
        genericMetaInf.setPublisher(AbstractResource.URIs.baseURI);
        genericMetaInf.setContributor("http://opentox.org/");
        genericMetaInf.setDate("2009-11-18");
        genericMetaInf.setFormat(MediaType.TEXT_XML.toString());
        genericMetaInf.setAudience("QSAR Experts, Biologists, Toxicologists");
        genericMetaInf.setProvenance("");
        genericMetaInf.setSource(AbstractResource.baseURI);
        genericMetaInf.setRelation("http://opentox.org");

       
        if (algorithm == MLR) {
            AlgorithmMetaInf MlrMetaInf = genericMetaInf;
            MlrMetaInf.setAbout(AbstractResource.URIs.mlrAlgorithmURI);
            MlrMetaInf.setTitle("Multiple Linear Regression");
            MlrMetaInf.setAlgorithmType(RegressionOntology+":mlr");
            MlrMetaInf.setDescription("Multiple Linear Regression Training Algorithm");
            MlrMetaInf.setIdentifier(AbstractResource.URIs.mlrAlgorithmURI);
            MlrMetaInf.setAlgorithm(statisticsSupported(MLR), Parameters(MLR));

            representation = getStringRepresentationForMetaInf(MlrMetaInf, media);
            
        } else if (algorithm == SVM) {
            AlgorithmMetaInf SvmMetaInf = genericMetaInf;
            SvmMetaInf.setAbout(AbstractResource.URIs.svmAlgorithmURI);
            SvmMetaInf.setTitle("Support Vector Machine Regression");
            SvmMetaInf.setAlgorithmType(RegressionOntology+":svm");
            SvmMetaInf.setDescription("Training Algorithm for Support Vector" +
                    "Machine Regression Models");
            SvmMetaInf.setAlgorithm(statisticsSupported(SVM), Parameters(SVM));
            SvmMetaInf.setIdentifier(AbstractResource.URIs.svmAlgorithmURI);

            representation = getStringRepresentationForMetaInf(SvmMetaInf, media);

        } else if (algorithm == SVC) {
            AlgorithmMetaInf SvcMetaInf = genericMetaInf;
            SvcMetaInf.setAbout(AbstractResource.URIs.svcAlgorithmURI);
            SvcMetaInf.setTitle("Support Vector Machine Classification");
            SvcMetaInf.setAlgorithmType(RegressionOntology+":svc");
            SvcMetaInf.setDescription("Training Algorithm for Support Vector" +
                    "Machine Classification Models");
            SvcMetaInf.setAlgorithm(statisticsSupported(SVC), Parameters(SVC));
            SvcMetaInf.setIdentifier(AbstractResource.URIs.svcAlgorithmURI);

            representation = getStringRepresentationForMetaInf(SvcMetaInf, media);       
        }
        return representation;
    }
}