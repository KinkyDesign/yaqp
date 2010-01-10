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
public class SvcAlgorithm implements IAlgorithm {

    public AlgorithmMeta getMeta() {
         ArrayList<AlgorithmParameter> Parameters = new ArrayList<AlgorithmParameter>();
        Parameters.add(ConstantParameters.TARGET);
        Parameters.add(ConstantParameters.KERNEL);
        Parameters.add(ConstantParameters.COST);
        Parameters.add(ConstantParameters.GAMMA);
        Parameters.add(ConstantParameters.COEFF0);
        Parameters.add(ConstantParameters.DEGREE);
        Parameters.add(ConstantParameters.TOLERANCE);
        AlgorithmMeta SvcMetaInf = new AlgorithmMeta();
        SvcMetaInf.date = ("2009-11-18");
        SvcMetaInf.format = (MediaType.TEXT_XML.toString());
        SvcMetaInf.audience = ("QSAR Experts, Biologists, Toxicologists");
        SvcMetaInf.provenance = ("");
        SvcMetaInf.title = ("svc");
        SvcMetaInf.identifier = (OTResource.URIs.svcAlgorithmURI);
        SvcMetaInf.subject = "SVC Classification";
        SvcMetaInf.algorithmType = AlgorithmTypes.RegressionEagerSingleTarget;
        SvcMetaInf.description = ("Training Algorithm for Support Vector"
                + "Machine Classification Models");
        SvcMetaInf.setParameters(Parameters);


        return SvcMetaInf;

    }

}
