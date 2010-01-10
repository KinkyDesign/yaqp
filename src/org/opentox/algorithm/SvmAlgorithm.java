package org.opentox.algorithm;

import java.util.ArrayList;
import org.opentox.interfaces.IAlgorithm;
import org.opentox.ontology.meta.AlgorithmMeta;
import org.opentox.ontology.namespaces.AlgorithmTypes;
import org.opentox.resource.OTResource;
import org.restlet.data.MediaType;

/**
 *
 * @author chung
 */
public class SvmAlgorithm implements IAlgorithm {

    public AlgorithmMeta getMeta() {
        ArrayList<AlgorithmParameter> Parameters = new ArrayList<AlgorithmParameter>();
        Parameters.add(ConstantParameters.TARGET);
        Parameters.add(ConstantParameters.KERNEL);
        Parameters.add(ConstantParameters.COST);
        Parameters.add(ConstantParameters.EPSILON);
        Parameters.add(ConstantParameters.GAMMA);
        Parameters.add(ConstantParameters.COEFF0);
        Parameters.add(ConstantParameters.DEGREE);
        Parameters.add(ConstantParameters.TOLERANCE);
        AlgorithmMeta SvmMetaInf = new AlgorithmMeta();
        SvmMetaInf.date = ("2009-11-18");
        SvmMetaInf.format = (MediaType.TEXT_XML.toString());
        SvmMetaInf.audience = ("QSAR Experts, Biologists, Toxicologists");
        SvmMetaInf.title = ("svm");
        SvmMetaInf.identifier = (OTResource.URIs.svmAlgorithmURI);
        SvmMetaInf.subject = ("SVM Regression");
        SvmMetaInf.algorithmType = AlgorithmTypes.RegressionEagerSingleTarget;
        SvmMetaInf.description = ("Training Algorithm for Support Vector"
                + "Machine Regression Models");
        SvmMetaInf.setParameters(Parameters);


        return SvmMetaInf;
    }
}
