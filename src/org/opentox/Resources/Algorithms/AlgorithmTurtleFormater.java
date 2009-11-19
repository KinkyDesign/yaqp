package org.opentox.Resources.Algorithms;

import java.util.ArrayList;

/**
 *
 * @author OpenTox - http://www.opentox.org
 * @author Sopasakis Pantelis
 * @author Sarimveis Harry
 */
public class AlgorithmTurtleFormater {

    private String title,
            description,
            subject,
            type,
            source,
            relation,
            creator,
            publisher,
            contributor,
            rights,
            date,
            format,
            identifier,
            language,
            audience,
            provenance,
            about,
            rdfNamespace = "http://www.w3.org/1999/02/22-rdf-syntax-ns#",
            dcNamespace = "http://purl.org/dc/elements/1.1/",
            opentoxNamespace = "http://opentox.org/api.1-1/algorithm",
            algorithmType;
    private ArrayList<String> statisticsSupported;
    private String[][] Parameters;
}
