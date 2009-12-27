package org.opentox.algorithm.reporting;

import org.opentox.algorithm.ConstantParameters;
import org.opentox.algorithm.AlgorithmParameter;
import org.opentox.algorithm.AlgorithmMetaInf;
import org.opentox.algorithm.AlgorithmEnum;
import org.opentox.formatters.AlgorithmYamlFormatter;
import org.opentox.formatters.AlgorithmXmlFormatter;
import org.opentox.formatters.AlgorithmRdfFormatter;
import org.opentox.formatters.AlgorithmJsonFormatter;
import java.util.ArrayList;
import org.opentox.media.OpenToxMediaType;
import org.opentox.resource.AbstractResource;
import org.opentox.namespaces.AlgorithmTypes;
import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;

/**
 *
 * @author OpenTox - http://www.opentox.org/
 * @author Sopasakis Pantelis
 * @author Sarimveis Harry
 * @version 1.3.3 (Last update: Dec 28, 2009)
 */
public class AlgorithmReporter extends AbstractAlgorithmReporter {


    private Status internalStatus = Status.SUCCESS_ACCEPTED;

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
            AlgorithmMetaInf metainf, MediaType media) {
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

        AlgorithmMetaInf genericMetaInf = new AlgorithmMetaInf();
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
        genericMetaInf.source = (AbstractResource.baseURI);
        genericMetaInf.relation = ("http://opentox.org");


        if (algorithm == AlgorithmEnum.mlr) {
            AlgorithmMetaInf MlrMetaInf = genericMetaInf;
            MlrMetaInf.setAbout(AbstractResource.URIs.mlrAlgorithmURI);
            MlrMetaInf.title = ("mlr");
            MlrMetaInf.subject = ("MLR, Multiple Linear Regression");
            MlrMetaInf.algorithmType = AlgorithmTypes.Class.RegressionEagerSingleTarget;
            MlrMetaInf.description = ("Multiple Linear Regression Training Algorithm");
            MlrMetaInf.identifier = (AbstractResource.URIs.mlrAlgorithmURI);
            MlrMetaInf.setAlgorithm(statisticsSupported(AlgorithmEnum.mlr),
                    Parameters(AlgorithmEnum.mlr));

            representation = getRepresentationForMetaInf(MlrMetaInf, media);

        } else if (algorithm == AlgorithmEnum.svm) {
            AlgorithmMetaInf SvmMetaInf = genericMetaInf;
            SvmMetaInf.setAbout(AbstractResource.URIs.svmAlgorithmURI);
            SvmMetaInf.title = ("svm");
            SvmMetaInf.subject = ("SVM Regression");
            SvmMetaInf.algorithmType = AlgorithmTypes.Class.RegressionEagerSingleTarget;
            SvmMetaInf.description = ("Training Algorithm for Support Vector"
                    + "Machine Regression Models");
            SvmMetaInf.setAlgorithm(statisticsSupported(AlgorithmEnum.svm),
                    Parameters(AlgorithmEnum.svm));
            SvmMetaInf.identifier = (AbstractResource.URIs.svmAlgorithmURI);

            representation = getRepresentationForMetaInf(SvmMetaInf, media);

        } else if (algorithm == AlgorithmEnum.svc) {
            AlgorithmMetaInf SvcMetaInf = genericMetaInf;
            SvcMetaInf.setAbout(AbstractResource.URIs.svcAlgorithmURI);
            SvcMetaInf.title = ("svc");
            SvcMetaInf.subject = ("SVC Classification");
            SvcMetaInf.algorithmType = AlgorithmTypes.Class.ClassificationEagerSingleTarget;
            SvcMetaInf.description = ("Training Algorithm for Support Vector"
                    + "Machine Classification Models");
            SvcMetaInf.setAlgorithm(statisticsSupported(AlgorithmEnum.svc),
                    Parameters(AlgorithmEnum.svc));
            SvcMetaInf.identifier = (AbstractResource.URIs.svcAlgorithmURI);

            representation = getRepresentationForMetaInf(SvcMetaInf, media);
        }
        return representation;
    }



    /**
     * Suggest a status for the Resource calling the trainer. The status can be
     * updated only in the following cases:
     * <ul>
     * <li>The current status is less than 300, i.e. No errors occured up to the current state</li>
     * <li>The current status is 4XX and the new status is also
     * a 4XX or a 5XX status (client or server error).</li>
     * <li>If the current status is a 5XX (eg 500), it will not be updated</li>
     * </ul>
     * @param status The status
     */
    public void setInternalStatus(Status status){
        if ( ((internalStatus.getCode()>=400)&&(internalStatus.getCode()<500)&&status.getCode()>=400)||
                ((internalStatus.getCode())<300)){
            this.internalStatus=status;
        }
    }

    public Status getInternalStatus(){
        return this.internalStatus;
    }



}
