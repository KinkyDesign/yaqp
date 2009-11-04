package org.opentox.Resources.Algorithms;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.opentox.Applications.OpenToxApplication;
import weka.core.Instances;
import weka.filters.unsupervised.attribute.Normalize;

/**
 *
 * @author tartoufo1973
 */
public class Preprocessing {



    public static Instances scale(Instances data){

        Instances scaledData = data;
        Normalize filter = new Normalize();
        try {
            filter.setInputFormat(scaledData);
            String[] scaleOptions = {"-S","2","-T","-1"};
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

    public static void main(String[] a) throws FileNotFoundException, IOException, Exception{
        Instances mydata = new Instances(
                new BufferedReader(
                new FileReader(System.getProperty("user.home")+"/Documents/RESTfulWebServices/uploads/data/arff/dataSet-8")));
        mydata = scale(mydata);
        System.out.println(mydata);
    }
}
