package org.opentox.algorithm.reporting;

import org.opentox.algorithm.ConstantParameters;
import org.opentox.algorithm.AlgorithmParameter;
import org.opentox.error.ErrorRepresentation;
import org.opentox.ontology.meta.AlgorithmMeta;
import org.opentox.algorithm.AlgorithmEnum;
import org.opentox.formatters.AlgorithmYamlFormatter;
import org.opentox.formatters.AlgorithmXmlFormatter;
import org.opentox.formatters.AlgorithmRdfFormatter;
import org.opentox.formatters.AlgorithmJsonFormatter;
import java.util.ArrayList;
import org.opentox.error.ErrorSource;
import org.opentox.interfaces.IAlgorithmReporter;
import org.opentox.interfaces.IFormatter;
import org.opentox.media.OpenToxMediaType;
import org.opentox.resource.OTResource;
import org.opentox.ontology.namespaces.AlgorithmTypes;
import org.opentox.resource.OTResource.URIs;
import org.restlet.data.MediaType;
import org.restlet.representation.Representation;

/**
 *
 * @author OpenTox - http://www.opentox.org/
 * @author Sopasakis Pantelis
 * @author Sarimveis Harry
 * @version 1.3.3 (Last update: Dec 28, 2009)
 */
public class AlgorithmReporter extends ErrorSource implements  IAlgorithmReporter {



    public AlgorithmReporter(){
        super();
    }

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
        Parameters.add(ConstantParameters.TARGET);
        if (algorithm == AlgorithmEnum.svm) {
            Parameters.add(ConstantParameters.KERNEL);
            Parameters.add(ConstantParameters.COST);
            Parameters.add(ConstantParameters.EPSILON);
            Parameters.add(ConstantParameters.GAMMA);
            Parameters.add(ConstantParameters.COEFF0);
            Parameters.add(ConstantParameters.DEGREE);
            Parameters.add(ConstantParameters.TOLERANCE);
        } else if (algorithm == AlgorithmEnum.svc) {
            Parameters.add(ConstantParameters.KERNEL);
            Parameters.add(ConstantParameters.COST);
            Parameters.add(ConstantParameters.GAMMA);
            Parameters.add(ConstantParameters.COEFF0);
            Parameters.add(ConstantParameters.DEGREE);
            Parameters.add(ConstantParameters.TOLERANCE);
        } else if (algorithm == AlgorithmEnum.mlr) {
        }
        return Parameters;
    }

    private Representation getRepresentationForMetaInf(
            AlgorithmMeta metainf, MediaType media) {
        IFormatter formater;
        Representation representation = null;
        if ((MediaType.APPLICATION_RDF_XML.equals(media))
                || (MediaType.APPLICATION_RDF_TURTLE.equals(media))
                || (OpenToxMediaType.TEXT_N3.equals(media))
                || (OpenToxMediaType.TEXT_TRIPLE.equals(media))) {
            formater = new AlgorithmRdfFormatter(metainf);
            representation = formater.getRepresentation(media);
        } else if (MediaType.APPLICATION_JSON.equals(media)) {
            formater = new AlgorithmJsonFormatter(metainf);
            representation = formater.getRepresentation(media);
        } else if (MediaType.TEXT_XML.equals(media)) {
            formater = new AlgorithmXmlFormatter(metainf);
            representation = formater.getRepresentation(media);
        } else if (OpenToxMediaType.TEXT_YAML.equals(media)) {
            formater = new AlgorithmYamlFormatter(metainf);
            representation = formater.getRepresentation(media);
        } else {
        }
        return representation;
    }

    @Override
    public Representation formatedRepresntation(MediaType media, AlgorithmEnum algorithm) {

        Representation representation = null;

        AlgorithmMeta genericMetaInf = new AlgorithmMeta();
        genericMetaInf.type = ("http://purl.org/dc/dcmitype/Service");
        genericMetaInf.rights = (OTResource.URIs.licenceUri);
        genericMetaInf.language = ("en");
        genericMetaInf.creator = (OTResource.URIs.baseURI);
        genericMetaInf.publisher = (OTResource.URIs.baseURI);
        genericMetaInf.contributor = ("http://opentox.org/");
        genericMetaInf.date = ("2009-11-18");
        genericMetaInf.format = (MediaType.TEXT_XML.toString());
        genericMetaInf.audience = ("QSAR Experts, Biologists, Toxicologists");
        genericMetaInf.provenance = ("");
        genericMetaInf.source = (URIs.baseURI);
        genericMetaInf.relation = ("http://opentox.org");


        if (algorithm == AlgorithmEnum.mlr) {
            AlgorithmMeta MlrMetaInf = genericMetaInf;
            MlrMetaInf.setAbout(OTResource.URIs.mlrAlgorithmURI);
            MlrMetaInf.title = ("mlr");
            MlrMetaInf.subject = ("MLR, Multiple Linear Regression");
            MlrMetaInf.algorithmType = AlgorithmTypes.RegressionEagerSingleTarget;
            MlrMetaInf.description = ("Multiple Linear Regression Training Algorithm");
            MlrMetaInf.identifier = (OTResource.URIs.mlrAlgorithmURI);
            MlrMetaInf.setParameters(Parameters(AlgorithmEnum.mlr));

            representation = getRepresentationForMetaInf(MlrMetaInf, media);

        } else if (algorithm == AlgorithmEnum.svm) {
            AlgorithmMeta SvmMetaInf = genericMetaInf;
            SvmMetaInf.setAbout(OTResource.URIs.svmAlgorithmURI);
            SvmMetaInf.title = ("svm");
            SvmMetaInf.subject = ("SVM Regression");
            SvmMetaInf.algorithmType = AlgorithmTypes.RegressionEagerSingleTarget;
            SvmMetaInf.description = ("Training Algorithm for Support Vector"
                    + "Machine Regression Models");
            SvmMetaInf.setParameters(Parameters(AlgorithmEnum.svm));
            SvmMetaInf.identifier = (OTResource.URIs.svmAlgorithmURI);

            representation = getRepresentationForMetaInf(SvmMetaInf, media);

        } else if (algorithm == AlgorithmEnum.svc) {
            AlgorithmMeta SvcMetaInf = genericMetaInf;
            SvcMetaInf.setAbout(OTResource.URIs.svcAlgorithmURI);
            SvcMetaInf.title = ("svc");
            SvcMetaInf.subject = ("SVC Classification");
            SvcMetaInf.algorithmType = AlgorithmTypes.ClassificationEagerSingleTarget;
            SvcMetaInf.description = ("Training Algorithm for Support Vector"
                    + "Machine Classification Models");
            SvcMetaInf.setParameters(Parameters(AlgorithmEnum.svc));
            SvcMetaInf.identifier = (OTResource.URIs.svcAlgorithmURI);

            representation = getRepresentationForMetaInf(SvcMetaInf, media);
        }
        return representation;
    }

    @Override
    public ErrorRepresentation getErrorRep() {
        return errorRep;
    }






}
