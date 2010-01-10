/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.opentox.algorithm;

import java.util.ArrayList;
import org.opentox.interfaces.IAlgorithm;
import org.opentox.ontology.meta.AlgorithmMeta;
import org.opentox.ontology.namespaces.AlgorithmTypes;
import org.opentox.resource.OTResource;
import org.opentox.resource.OTResource.URIs;
import org.restlet.data.MediaType;

/**
 *
 * @author chung
 */
public class MlrAlgorithm implements IAlgorithm {

    public AlgorithmMeta getMeta() {
        ArrayList<AlgorithmParameter> Parameters = new ArrayList<AlgorithmParameter>();
        Parameters.add(ConstantParameters.TARGET);
        AlgorithmMeta MlrMetaInf = new AlgorithmMeta();
        MlrMetaInf.type = "http://purl.org/dc/dcmitype/Service";
        MlrMetaInf.date = "2009-11-18";
        MlrMetaInf.format = MediaType.APPLICATION_RDF_XML.toString();
        MlrMetaInf.audience = "QSAR Experts, Biologists, Toxicologists";
        MlrMetaInf.provenance = "";
        MlrMetaInf.source = URIs.baseURI;
        MlrMetaInf.relation = URIs.OpentoxUri;
        MlrMetaInf.title = "mlr";
        MlrMetaInf.subject = "MLR, Multiple Linear Regression";
        MlrMetaInf.algorithmType = AlgorithmTypes.RegressionEagerSingleTarget;
        MlrMetaInf.description = "Multiple Linear Regression Training Algorithm";
        MlrMetaInf.identifier = OTResource.URIs.mlrAlgorithmURI;
        MlrMetaInf.setParameters(Parameters);
        return MlrMetaInf;
    }
}
