package org.opentox.util.converters;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.restlet.data.Status;

/**
 * <p style="width:60%">
 * Converter of Arff file into equivalent DSD ones.
 * </p>
 * <p style="width:80%">
 * <b>Description:</b><br/>
 * An existing arff file is provided for which a specified structure is assumed:
 *
 * </p>
 * @author OpenTox - http://www.opentox.org/
 * @author Sopasakis Pantelis
 * @author Sarimveis Harry
 * @version 1.1 (Aug 20, 2009)
 */
public class arff2dsd {

    private static final long serialVersionUID=100121700055002L;

    /**
     * Path to an existing arff file
     */
    private String m_ArffFile;

    /**
     * Where to save meta data (in XML format)
     */
    private String  m_MetaFile;

    /**
     * The produced DSD file location
     */
    private String m_DsdFile;

    /**
     * Relation Name
     */
    private String i_RelationName;

    /**
     * Status Codes:
     * 100 --> Continue
     * 202 --> Accepted - Processing
     * 500 --> Some error occured
     *
     */
    private Status m_Status=Status.SUCCESS_ACCEPTED;
    /**
     * Number of Attributes contained in the data set,
     * including the class attribute and the ids.
     */
    private int i_numberOfAttributes=0;



    /**
     * Avoid using this contructor.
     */
    public arff2dsd(){
        throw new UnsupportedOperationException("Avoid using this constructor!");
    }

    /**
     * Constructor.
     * @param ArffFile An existing arff file.
     * @param MetaFile Produced file containing meta-information.
     * @param DsdFile Produced Dsd File.
     */
    public arff2dsd(String ArffFile, String MetaFile, String DsdFile){
        m_ArffFile=ArffFile;

        File arff = new File(m_ArffFile);

        if (arff.exists()) {
            m_MetaFile=MetaFile;
            m_DsdFile=DsdFile;
            m_Status=Status.INFO_CONTINUE;
        }else{
            m_Status=Status.SERVER_ERROR_INTERNAL;
        }

    }


    /**
     * Converts the given ARFF file into a DSD file while a meta-inf XML file
     * is produced which is compliant to the standard PMML schema.
     * @throws FileNotFoundException
     * @throws IOException
     */
    public void convert() throws IOException{

        FileReader fStream;
        try {
            fStream = new FileReader(m_ArffFile);
            BufferedReader arff = new BufferedReader(fStream);
            FileWriter meta_outStream = new FileWriter(m_MetaFile);
            BufferedWriter meta = new BufferedWriter(meta_outStream);

            FileWriter dsd_outStream = new FileWriter(m_DsdFile);
            BufferedWriter dsd = new BufferedWriter(dsd_outStream);

            meta.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                    "<PMML version=\"3.2\" xmlns=\"http://www.dmg.org/PMML-3_2\"   " +
                    "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"> " +
                    "<Header copyright=\"OpenTox: http://www.opentox.org\" />");


            String line="";
            String Descriptors="";
            String Descriptor_Types="", desc="", desctype="", Optypes="", Datatypes="", IDs="";
            String[] classIndices = null;
            boolean isdata=false;


            /// Read the lines of the ARFF file...
            while((line = arff.readLine())!=null)
            {
                /// line_tok is the Tokenized line
                StringTokenizer line_tok = new StringTokenizer(line);

                /// Read the Tokens of every line except for the comment lines
                /// which start with a "%"
                while(
                        line_tok.hasMoreTokens() &&
                        !(line.contains("%"))
                        )
                {
                    // tok: the first token of the line.
                    String tok=line_tok.nextToken();

                    if (isdata){
                        String[] data = tok.split(",");
                        IDs += (data[0]+" ");
                        dsd.write(data[data.length-1]+" ");
                        for (int j=1;j<data.length-1;j++){
                            dsd.write(j+":"+data[j]+" ");
                        }
                        dsd.write("\n");
                    }


                    /// Parse the header of the file...
                    /// **********************************************
                        if (tok.equalsIgnoreCase("@RELATION")){
                            i_RelationName=line_tok.nextToken();
                        }else if (tok.equalsIgnoreCase("@ATTRIBUTE")){
                            desc=line_tok.nextToken();
                            desctype=line_tok.nextToken();
                            Descriptors += (desc+" ");
                            Descriptor_Types += (desctype+" ");
                            if ((desctype.equalsIgnoreCase("numeric"))||
                                    (desctype.equalsIgnoreCase("real"))){
                                Optypes += "continuous ";
                                Datatypes += "double ";
                            }else if (desctype.equalsIgnoreCase("string")){
                                Optypes += "categorical ";
                                Datatypes += "string ";
                            }else{
                                Optypes += "categorical ";
                                Datatypes += "integer";
                                desctype = desctype.replace("{", "");
                                desctype = desctype.replace("}", "");
                                classIndices = desctype.split(",");
                            }
                            i_numberOfAttributes++;
                        }else if (tok.equalsIgnoreCase("@DATA")) {
                            // If the token equals "@DATA", skip this line and
                            // proceed to the parsing of the data.
                            isdata=true;
                        }// ...The parsing of the header ends here.
                    /// **********************************************




                }

            }
            System.gc();
            /// Construct the meta.xml file
            meta.write("<Relation name=\""+i_RelationName+"\" seed=\""+java.util.GregorianCalendar.getInstance().getTimeInMillis()+"\"/>");
            meta.write("<DataDictionary numberOfFields=\""+i_numberOfAttributes+"\">\n");

            // Produce the meta-information file (in XML format)
            StringTokenizer Descriptors_tok = new StringTokenizer(Descriptors);
            StringTokenizer Optypes_tok = new StringTokenizer(Optypes);
            StringTokenizer Datatypes_tok = new StringTokenizer(Datatypes);
            StringTokenizer IDs_tok = new StringTokenizer(IDs);

            String name, optype, datatype;

            for (int i=1;i<=i_numberOfAttributes;i++){
                name=Descriptors_tok.nextToken();
                optype=Optypes_tok.nextToken();
                datatype=Datatypes_tok.nextToken();
                meta.write("<DataField name=\""+name+"\" " +
                        "optype=\""+optype+"\" " +
                        "datatype=\""+datatype+"\"");
                if (name.equalsIgnoreCase("id")){
                    meta.write(">");
                    while(IDs_tok.hasMoreTokens()){
                        meta.write("<Value value=\""+IDs_tok.nextToken()+"\" />");
                    }
                    meta.write("</DataField>");

                }else if (name.equalsIgnoreCase("class")){
                    meta.write(">");
                    for(int k=0;k<classIndices.length;k++){
                        meta.write("<Value value=\""+classIndices[k]+"\" />");
                    }
                    meta.write("</DataField>");
                }else{
                    meta.write("/>");
                }

            }

            meta.write("</DataDictionary></PMML>");



            /// Close the input and output streams:
            arff.close();
            meta.close();
            dsd.close();
            m_Status=Status.SUCCESS_OK;

        } catch (FileNotFoundException ex) {
            m_Status=Status.SERVER_ERROR_INTERNAL;
            Logger.getLogger(arff2dsd.class.getName()).log(Level.SEVERE, null, ex);
        }
       

    }// end method: convert
    /**
     *
     * @return m_Status The status code
     */
    public Status getStatus(){
        return m_Status;
    }




    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException, IOException {
        arff2dsd conv = new arff2dsd(
                args[0], //arff file
                args[1], // meta-inf file
                args[2]  // dsd file
                );
        conv.convert();
    }

}
