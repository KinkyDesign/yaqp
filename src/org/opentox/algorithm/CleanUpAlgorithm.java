package org.opentox.algorithm;

import java.util.ArrayList;
import java.util.Locale;
import org.opentox.interfaces.IAlgorithm;
import org.opentox.ontology.meta.AlgorithmMeta;
import org.opentox.ontology.namespaces.AlgorithmTypes;
import org.opentox.resource.OTResource.URIs;
import org.restlet.data.MediaType;

/**
 *
 * @author chung
 */
public class CleanUpAlgorithm implements IAlgorithm{

    public AlgorithmMeta getMeta() {
        ArrayList<AlgorithmParameter> Parameters = new ArrayList<AlgorithmParameter>();
        AlgorithmMeta CleanUpMeta = new AlgorithmMeta();
        CleanUpMeta.Parameters = Parameters;
        CleanUpMeta.identifier = URIs.algorithmURI+"/"+AlgorithmEnum.cleanup.getAlgorithmName();
        CleanUpMeta.algorithmType = AlgorithmTypes.DataCleanup;
        CleanUpMeta.audience = "Data Miners, Users of OpenTox Services, Biologists, QSAR experts";
        CleanUpMeta.date = "2010-01-11";
        CleanUpMeta.format = MediaType.APPLICATION_RDF_XML.toString();
        CleanUpMeta.description = "Service that cleans a dataset from string features and illegal entries.";        
        CleanUpMeta.subject = "CleanUp Service";
        CleanUpMeta.title = "CleanUp Services";                

        return CleanUpMeta;
    }

}
