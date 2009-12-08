package org.opentox.Resources.Algorithms;

import com.hp.hpl.jena.rdf.model.RDFNode;
import java.util.ArrayList;

/**
 *
 * @author OpenTox - http://www.opentox.org
 * @author Sopasakis Pantelis
 * @author Sarimveis Harry
 */
public class AlgorithmMetaInf {

    public org.opentox.ontology.AlgorithmTypes.Class algorithmType;
    
    public String
            title,
            description,
            subject,
            type,
            source,
            relation,
            rights,
            provenance,
            audience,
            identifier,
            date,
            format,
            language,
            creator,
            publisher,
            contributor,
            about;

    public ArrayList<String> statisticsSupported;
    
    public ArrayList<AlgorithmParameter> Parameters;


    public AlgorithmMetaInf(){

    }


    public AlgorithmMetaInf(String about){
        this.about=about;
    }


    public void setAbout(String about) {
        this.about = about;
    }

    void setAlgorithm(ArrayList<String> statisticsSupported, ArrayList<AlgorithmParameter> Parameters) {
        this.statisticsSupported=statisticsSupported;
        this.Parameters=Parameters;
    }

}