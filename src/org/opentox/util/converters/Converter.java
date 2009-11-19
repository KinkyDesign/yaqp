/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.opentox.util.converters;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import weka.core.Instances;
import java.io.BufferedWriter;
import java.io.FileReader;

/**
 *
 * @author OpenTox - http://www.opentox.org
 * @author Sopasakis Pantelis
 * @author Sarimveis Harry
 * @author Kolotouros Dimitris
 */
public class Converter extends AbstractConverter{

    private static final long serialVersionUID = 8525170373664066039L;

    public void Converter(){
    }
    
    @Override
    public void convert(final Instances instances, File dsdFile) {
        int i, j, targetIdx;
        
        StringBuilder DsdString = new StringBuilder();
        
        
        targetIdx = instances.classIndex();
        System.out.println(targetIdx);
        for(i=0; i<instances.numInstances(); i++){
            DsdString.append(instances.instance(i).value(targetIdx) + " ");

            for(j=0; j<targetIdx; j++){
                DsdString.append((j+1) + ":" + instances.instance(i).value(j)  + " ");
            }
            for(j=targetIdx+1; j<instances.numAttributes(); j++){
                DsdString.append(j + ":" + instances.instance(i).value(j));
                if(j!=instances.numAttributes()){
                    DsdString.append(" ");
                }
            }
            DsdString.append("\n");
        }

        try {

            FileWriter fstream = new FileWriter(dsdFile);
            BufferedWriter out = new BufferedWriter(fstream);
            out.write(DsdString.toString());
            out.flush();
            out.close();
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    @Override
    public void convert(final File dsdFile, Instances instances) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public static void main(String [] a) throws IOException{
        Instances data = new Instances(new BufferedReader(new FileReader("/home/tartoufo1973/Documents/RESTfulWebServices/uploads/data/arff/dataSet-8")));
        data.setClassIndex(1);
        Converter asd = new Converter();
        File output = new File("/home/tartoufo1973/Desktop/dsdoutput.txt");
        asd.convert(data, output);
    }

}
