package org.opentox.algorithm;

import java.util.ArrayList;

/**
 * Meta-Information about an algorithm including its name, description, parameters
 * and other meta-info.
 * @author OpenTox - http://www.opentox.org/
 * @author Sopasakis Pantelis
 * @author Sarimveis Harry
 * @version 1.3.3 (Last update: Dec 20, 2009)
 */
public class AlgorithmMetaInf {

    public org.opentox.namespaces.AlgorithmTypes.Class algorithmType;
    
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

    public void setAlgorithm(ArrayList<String> statisticsSupported, ArrayList<AlgorithmParameter> Parameters) {
        this.statisticsSupported=statisticsSupported;
        this.Parameters=Parameters;
    }

}