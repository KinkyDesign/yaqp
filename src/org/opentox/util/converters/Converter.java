package org.opentox.util.converters;


import org.opentox.interfaces.IConverter;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import weka.core.Instances;
import java.io.BufferedWriter;
import org.opentox.algorithm.dataprocessing.DataCleanUp;
/**
*
* @author OpenTox - http://www.opentox.org
* @author Sopasakis Pantelis
* @author Sarimveis Harry
* @author Kolotouros Dimitris
*/
public class Converter implements  IConverter{

    private static final long serialVersionUID = 85251113527439L;

    public void Converter(){
    }


    /**
     * Saves a given Instances object in a DSD file. The given Instances should have <b>no
     * string attributes</b>. <br/><br/>Consider applying
     * {@link org.opentox.algorithm.dataprocessing.DataCleanUp#removeStringAtts(Instances) }
     * before using this method. The columns of the produced DSD file have exactly
     * the same ordering with the attributes of the Instances. For example, if the
     * attributes are x1, x2, x3, ..., xn, then the DSD file will have n columns
     * , and precisely x1,x2,...,xn (with the same ordering). Make sure that the
     * Instances has only numeric attributes!
     * @param instances {@link weka.core.Instances } object which has only numeric
     * attributes!
     * @param dsdFile Destination {@link java.io.File } where the result is stored.
     */

    @Override
    public void convert(final Instances instances, File dsdFile) {

        int i, j, targetIdx;

        StringBuilder DsdString = new StringBuilder();


        targetIdx = instances.classIndex();
        System.out.println(targetIdx);
        for(i=0; i<instances.numInstances(); i++){
            DsdString.append(instances.instance(i).value(targetIdx) + " ");

            for(j=0; j<targetIdx; j++){
                DsdString.append((j+1) + ":" + instances.instance(i).value(j) + " ");
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


    /**
     * Will be supported in some next version.
     * @param dsdFile
     * @param instances
     */
    @Override
    public void convert(final File dsdFile, Instances instances) {
        throw new UnsupportedOperationException("Not supported yet.");
    }



    @Override
    public void convert(InputStream input_RDF_file, Instances instances) {
        throw new UnsupportedOperationException("Not Supported yet!");
    }

    

}
