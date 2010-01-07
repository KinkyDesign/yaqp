package org.opentox.ontology.meta;

import org.opentox.ontology.meta.MetaInfo;
import java.util.ArrayList;
import org.opentox.algorithm.AlgorithmParameter;

/**
 * Meta-Information about an algorithm including its name, description, parameters
 * and other meta-info.
 * @author OpenTox - http://www.opentox.org/
 * @author Sopasakis Pantelis
 * @author Sarimveis Harry
 * @version 1.3.3 (Last update: Dec 20, 2009)
 */
public class AlgorithmMetaInf extends MetaInfo{

    public org.opentox.namespaces.AlgorithmTypes.Class algorithmType;
    
    

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