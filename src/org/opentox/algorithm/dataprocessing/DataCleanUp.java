package org.opentox.algorithm.dataprocessing;

import org.opentox.OpenToxApplication;
import org.opentox.interfaces.IPreprocessing;
import weka.core.Instances;
import weka.filters.unsupervised.attribute.Normalize;

/**
 *
 * @author OpenTox - http://www.opentox.org/
 * @author Sopasakis Pantelis
 * @author Kolotouros Dimitris
 * @author Sarimveis Harry
 * @version 1.3.3 (Last update: Dec 20, 2009)
 */
public class DataCleanUp implements IPreprocessing{


    /**
     * Scales the data producing a dataset whose values lie within [-1,1]
     * @param data
     * @return the scaled Instances.
     */
    public static Instances scale(Instances data){

        Instances scaledData = data;
        Normalize filter = new Normalize();
        try {
            filter.setInputFormat(scaledData);
            String[] scaleOptions = {"-S","2","-T","-1","-unset-class-temporarily"};
            filter.setOptions(scaleOptions);
            scaledData=Normalize.useFilter(scaledData, filter);
        } catch (Exception ex) {
            OpenToxApplication.opentoxLogger.severe("Error while trying to scale data :"+ex.toString());
        }
        return scaledData;

    }

    public static void removeStringAtts(Instances data){
        for (int j=0;j<data.numAttributes();j++){
            if (data.attribute(j).isString()){
                data.deleteAttributeAt(j);
                j--;
            }
        }
    }

    
}
