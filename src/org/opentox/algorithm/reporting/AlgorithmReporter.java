package org.opentox.algorithm.reporting;

import org.opentox.algorithm.ConstantParameters;
import org.opentox.algorithm.AlgorithmParameter;
import org.opentox.ontology.meta.AlgorithmMeta;
import org.opentox.algorithm.AlgorithmEnum;
import org.opentox.formatters.AlgorithmYamlFormatter;
import org.opentox.formatters.AlgorithmXmlFormatter;
import org.opentox.formatters.AlgorithmRdfFormatter;
import org.opentox.formatters.AlgorithmJsonFormatter;
import java.util.ArrayList;
import org.opentox.media.OpenToxMediaType;
import org.opentox.resource.AbstractResource;
import org.opentox.namespaces.AlgorithmTypes;
import org.opentox.resource.AbstractResource.URIs;
import org.restlet.data.MediaType;
import org.restlet.representation.Representation;

/**
 *
 * @author OpenTox - http://www.opentox.org/
 * @author Sopasakis Pantelis
 * @author Sarimveis Harry
 * @version 1.3.3 (Last update: Dec 28, 2009)
 */
public class AlgorithmReporter extends AbstractAlgorithmReporter {



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
        Representation representation = null;
        if ((MediaType.APPLICATION_RDF_XML.equals(media))
                || (MediaType.APPLICATION_RDF_TURTLE.equals(media))
                || (OpenToxMediaType.TEXT_N3.equals(media))
                || (OpenToxMediaType.TEXT_TRIPLE.equals(media))) {
            AlgorithmRdfFormatter formater = new AlgorithmRdfFormatter(metainf);
            representation = formater.getRepresentation(media);
        } else if (MediaType.APPLICATION_JSON.equals(media)) {
            AlgorithmJsonFormatter formater = new AlgorithmJsonFormatter(metainf);
            representation = formater.getRepresentation(media);
        } else if (MediaType.TEXT_XML.equals(media)) {
            AlgorithmXmlFormatter formater = new AlgorithmXmlFormatter(metainf);
            representation = formater.getRepresentation(media);
        } else if (OpenToxMediaType.TEXT_YAML.equals(media)) {
            AlgorithmYamlFormatter formater = new AlgorithmYamlFormatter(metainf);
            representation = formater.getRepresentation(media);
        } else {
        }
        return representation;
    }

    @Override
    public Representation FormatedRepresntation(MediaType media, AlgorithmEnum algorithm) {

        Representation representation = null;

        AlgorithmMeta genericMetaInf = new AlgorithmMeta();
        genericMetaInf.type = ("http://purl.org/dc/dcmitype/Service");
        genericMetaInf.rights = (AbstractResource.URIs.licenceUri);
        genericMetaInf.language = ("en");
        genericMetaInf.creator = (AbstractResource.URIs.baseURI);
        genericMetaInf.publisher = (AbstractResource.URIs.baseURI);
        genericMetaInf.contributor = ("http://opentox.org/");
        genericMetaInf.date = ("2009-11-18");
        genericMetaInf.format = (MediaType.TEXT_XML.toString());
        genericMetaInf.audience = ("QSAR Experts, Biologists, Toxicologists");
        genericMetaInf.provenance = ("");
        genericMetaInf.source = (URIs.baseURI);
        genericMetaInf.relation = ("http://opentox.org");


        if (algorithm == AlgorithmEnum.mlr) {
            AlgorithmMeta MlrMetaInf = genericMetaInf;
            MlrMetaInf.setAbout(AbstractResource.URIs.mlrAlgorithmURI);
            MlrMetaInf.title = ("mlr");
            MlrMetaInf.subject = ("MLR, Multiple Linear Regression");
            MlrMetaInf.algorithmType = AlgorithmTypes.Class.RegressionEagerSingleTarget;
            MlrMetaInf.description = ("Multiple Linear Regression Training Algorithm");
            MlrMetaInf.identifier = (AbstractResource.URIs.mlrAlgorithmURI);
            MlrMetaInf.setParameters(Parameters(AlgorithmEnum.mlr));

            representation = getRepresentationForMetaInf(MlrMetaInf, media);

        } else if (algorithm == AlgorithmEnum.svm) {
            AlgorithmMeta SvmMetaInf = genericMetaInf;
            SvmMetaInf.setAbout(AbstractResource.URIs.svmAlgorithmURI);
            SvmMetaInf.title = ("svm");
            SvmMetaInf.subject = ("SVM Regression");
            SvmMetaInf.algorithmType = AlgorithmTypes.Class.RegressionEagerSingleTarget;
            SvmMetaInf.description = ("Training Algorithm for Support Vector"
                    + "Machine Regression Models");
            SvmMetaInf.setParameters(Parameters(AlgorithmEnum.svm));
            SvmMetaInf.identifier = (AbstractResource.URIs.svmAlgorithmURI);

            representation = getRepresentationForMetaInf(SvmMetaInf, media);

        } else if (algorithm == AlgorithmEnum.svc) {
            AlgorithmMeta SvcMetaInf = genericMetaInf;
            SvcMetaInf.setAbout(AbstractResource.URIs.svcAlgorithmURI);
            SvcMetaInf.title = ("svc");
            SvcMetaInf.subject = ("SVC Classification");
            SvcMetaInf.algorithmType = AlgorithmTypes.Class.ClassificationEagerSingleTarget;
            SvcMetaInf.description = ("Training Algorithm for Support Vector"
                    + "Machine Classification Models");
            SvcMetaInf.setParameters(Parameters(AlgorithmEnum.svc));
            SvcMetaInf.identifier = (AbstractResource.URIs.svcAlgorithmURI);

            representation = getRepresentationForMetaInf(SvcMetaInf, media);
        }
        return representation;
    }






}
