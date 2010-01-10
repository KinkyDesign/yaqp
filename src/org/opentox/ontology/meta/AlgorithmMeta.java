package org.opentox.ontology.meta;

import java.util.ArrayList;
import org.opentox.algorithm.AlgorithmParameter;

/**
 * Meta-Information about an algorithm.
 * @author OpenTox - http://www.opentox.org/
 * @author Sopasakis Pantelis
 * @author Sarimveis Harry
 * @version 1.3.3 (Last update: Dec 20, 2009)
 */
public class AlgorithmMeta extends DCMeta {

    public org.opentox.ontology.namespaces.AlgorithmTypes.Class algorithmType;
           
    
    public ArrayList<AlgorithmParameter> Parameters;


    public AlgorithmMeta(){
        super();
    }


    public AlgorithmMeta(String about){
        this.identifier=about;
    }

    

    public void setParameters(ArrayList<AlgorithmParameter> Parameters) {
        this.Parameters=Parameters;
    }

}