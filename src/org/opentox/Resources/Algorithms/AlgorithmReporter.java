package org.opentox.Resources.Algorithms;

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import org.opentox.formatters.AlgorithmYamlFormatter;
import org.opentox.formatters.AlgorithmXmlFormatter;
import org.opentox.formatters.AlgorithmTurtleFormatter;
import org.opentox.formatters.AlgorithmRdfFormatter;
import org.opentox.formatters.AlgorithmJsonFormatter;
import java.util.ArrayList;
import org.opentox.MediaTypes.OpenToxMediaType;
import org.opentox.Resources.AbstractResource;
import org.opentox.formatters.AlgorithmTriplesFormatter;
import org.restlet.data.MediaType;
import org.restlet.representation.StringRepresentation;

/**
 *
 * @author OpenTox - http://www.opentox.org
 * @author Sopasakis Pantelis
 * @author Sarimveis Harry
 */
public class AlgorithmReporter extends AbstractAlgorithmReporter{

    private static final String
            RegressionOntology = "AlgorithmMLDMToxLearningRegressionEager1TargetVariable";

    private static ArrayList<String> statisticsSupported(AlgorithmEnum algorithm) {
        ArrayList<String> statisticsSupported = new ArrayList<String>();
        statisticsSupported.add("RootMeanSquaredError");
        statisticsSupported.add("RelativeAbsoluteError");
        statisticsSupported.add("RootRelativeSquaredError");
        statisticsSupported.add("MeanAbsoluteError");
        return statisticsSupported;
    }

    private static ArrayList<AlgorithmParameter> Parameters(AlgorithmEnum algorithm) {
        ArrayList<AlgorithmParameter> Parameters = new ArrayList<AlgorithmParameter>();
        Parameters.add(new AlgorithmParameter<String>("target", XSDDatatype.XSDanyURI, "http://opentox.ntua.gr:3000/files/192", "mandatory"));
        if (algorithm == AlgorithmEnum.svm) {
            Parameters.add(new AlgorithmParameter<String>("kernel", XSDDatatype.XSDstring, "rbf", "optional"));
            Parameters.add(new AlgorithmParameter<Double>("cost", XSDDatatype.XSDdouble, 10.0, "optional"));
            Parameters.add(new AlgorithmParameter<Double>("epsilon", XSDDatatype.XSDdouble, 0.10, "optional"));
            Parameters.add(new AlgorithmParameter<Double>("gamma", XSDDatatype.XSDdouble, 1.00, "optional"));
            Parameters.add(new AlgorithmParameter<Double>("coeff0", XSDDatatype.XSDdouble, 0.0, "optional"));
            Parameters.add(new AlgorithmParameter<Integer>("degree", XSDDatatype.XSDpositiveInteger, 3, "optional"));
            Parameters.add(new AlgorithmParameter<Double>("tolerance", XSDDatatype.XSDdouble, 0.0001, "optional"));
            Parameters.add(new AlgorithmParameter<Integer>("tolerance", XSDDatatype.XSDpositiveInteger, 50, "optional"));                    
        } else if (algorithm == AlgorithmEnum.svc) {
            Parameters.add(new AlgorithmParameter<String>("kernel", XSDDatatype.XSDstring, "rbf", "optional"));
            Parameters.add(new AlgorithmParameter<Double>("cost", XSDDatatype.XSDdouble, 10.0, "optional"));
            Parameters.add(new AlgorithmParameter<Double>("gamma", XSDDatatype.XSDdouble, 1.00, "optional"));
            Parameters.add(new AlgorithmParameter<Double>("coeff0", XSDDatatype.XSDdouble, 0.0, "optional"));
            Parameters.add(new AlgorithmParameter<Integer>("degree", XSDDatatype.XSDpositiveInteger, 3, "optional"));
            Parameters.add(new AlgorithmParameter<Double>("tolerance", XSDDatatype.XSDdouble, 0.0001, "optional"));
            Parameters.add(new AlgorithmParameter<Integer>("tolerance", XSDDatatype.XSDpositiveInteger, 50, "optional"));
        } else if (algorithm == AlgorithmEnum.mlr) {
            
        }
        return Parameters;
    }



    private StringRepresentation getStringRepresentationForMetaInf(
            AlgorithmMetaInf metainf, MediaType media){
        StringRepresentation representation = null;
        if (MediaType.APPLICATION_RDF_XML.equals(media)) {
                AlgorithmRdfFormatter formater = new AlgorithmRdfFormatter(metainf);
                representation = formater.getStringRepresentation();
            } else if (MediaType.APPLICATION_JSON.equals(media)) {
                AlgorithmJsonFormatter formater = new AlgorithmJsonFormatter(metainf);
                representation = formater.getStringRepresentation();
            } else if (MediaType.APPLICATION_RDF_TURTLE.equals(media)) {
                AlgorithmTurtleFormatter formater = new AlgorithmTurtleFormatter(metainf);
                representation = formater.getStringRepresentation();
            } else if (MediaType.TEXT_XML.equals(media)) {
                AlgorithmXmlFormatter formater = new AlgorithmXmlFormatter(metainf);
                representation = formater.getStringRepresentation();
            } else if (OpenToxMediaType.TEXT_YAML.equals(media)) {
                AlgorithmYamlFormatter formater = new AlgorithmYamlFormatter(metainf);
                representation = formater.getStringRepresentation();
            }else if (MediaType.APPLICATION_RDF_TRIX.equals(media)){
                AlgorithmTriplesFormatter formater = new AlgorithmTriplesFormatter(metainf);
                representation = formater.getStringRepresentation();
            } else {
            }
        return representation;
    }


    @Override
    public StringRepresentation FormatedRepresntation(MediaType media, AlgorithmEnum algorithm) {

        StringRepresentation representation = null;

        AlgorithmMetaInf genericMetaInf = new AlgorithmMetaInf();
        genericMetaInf.type=("http://purl.org/dc/dcmitype/Service");
        genericMetaInf.rights=(AbstractResource.URIs.licenceUri);
        genericMetaInf.language=("en");
        genericMetaInf.creator=(AbstractResource.URIs.baseURI);
        genericMetaInf.publisher=(AbstractResource.URIs.baseURI);
        genericMetaInf.contributor=("http://opentox.org/");
        genericMetaInf.date=("2009-11-18");
        genericMetaInf.format=(MediaType.TEXT_XML.toString());
        genericMetaInf.audience=("QSAR Experts, Biologists, Toxicologists");
        genericMetaInf.provenance=("");
        genericMetaInf.source=(AbstractResource.baseURI);
        genericMetaInf.relation=("http://opentox.org");

       
        if (algorithm == AlgorithmEnum.mlr) {
            AlgorithmMetaInf MlrMetaInf = genericMetaInf;
            MlrMetaInf.setAbout(AbstractResource.URIs.mlrAlgorithmURI);
            MlrMetaInf.title=("mlr");
            MlrMetaInf.subject=("MLR, Multiple Linear Regression");
            MlrMetaInf.algorithmType=(RegressionOntology+":mlr");
            MlrMetaInf.description=("Multiple Linear Regression Training Algorithm");
            MlrMetaInf.identifier=(AbstractResource.URIs.mlrAlgorithmURI);
            MlrMetaInf.setAlgorithm(statisticsSupported(AlgorithmEnum.mlr),
                    Parameters(AlgorithmEnum.mlr));

            representation = getStringRepresentationForMetaInf(MlrMetaInf, media);
            
        } else if (algorithm == AlgorithmEnum.svm) {
            AlgorithmMetaInf SvmMetaInf = genericMetaInf;
            SvmMetaInf.setAbout(AbstractResource.URIs.svmAlgorithmURI);
            SvmMetaInf.title=("svm");
            SvmMetaInf.subject=("SVM Regression");
            SvmMetaInf.algorithmType=(RegressionOntology+":svm");
            SvmMetaInf.description=("Training Algorithm for Support Vector" +
                    "Machine Regression Models");
            SvmMetaInf.setAlgorithm(statisticsSupported(AlgorithmEnum.svm),
                    Parameters(AlgorithmEnum.svm));
            SvmMetaInf.identifier=(AbstractResource.URIs.svmAlgorithmURI);

            representation = getStringRepresentationForMetaInf(SvmMetaInf, media);

        } else if (algorithm == AlgorithmEnum.svc) {
            AlgorithmMetaInf SvcMetaInf = genericMetaInf;
            SvcMetaInf.setAbout(AbstractResource.URIs.svcAlgorithmURI);
            SvcMetaInf.title=("svc");
            SvcMetaInf.subject=("SVC Classification");
            SvcMetaInf.algorithmType=(RegressionOntology+":svc");
            SvcMetaInf.description=("Training Algorithm for Support Vector" +
                    "Machine Classification Models");
            SvcMetaInf.setAlgorithm(statisticsSupported(AlgorithmEnum.svc),
                    Parameters(AlgorithmEnum.svc));
            SvcMetaInf.identifier=(AbstractResource.URIs.svcAlgorithmURI);

            representation = getStringRepresentationForMetaInf(SvcMetaInf, media);       
        }
        return representation;
    }
}