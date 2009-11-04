package org.opentox.util.converters;

import java.io.*;
import java.util.StringTokenizer;
/**
 *
 * @author OpenTox - http://www.opentox.org
 * @author Sopasakis Pantelis
 * @author Sarimveis Harry
 * @version 1.0 (Last Update: Aug 16, 2009)
 * @deprecated
 */
@Deprecated
public class txt2dsd {

    private static final long serialVersionUID=100121700027001L;

    /**
     * The private String filename stands for
     * the full path to the input text file name
     * (in the standard raw format)
     */
    private String filename;
    /**
     * The private String filename stands for
     * the full path to the target file, i.e. the
     * produced DSD file
     */
    private String outputFilename;
    /**
     * The private integer paramter numberOfAttributes
     * stants for the number of attributes of the
     * data set
     */
    private int numberOfAttributes;
    /**
     * The private String parameter moleculesFile
     * stands for the the full path of the produced file
     * containing a list of the names of the molecules
     */
    private String moleculesFile;


    public txt2dsd(String filename,
            String outputFileName,
            String moleculesFile){
        this.filename=filename;
        this.outputFilename=outputFileName;
        this.moleculesFile=moleculesFile;
    }

    private void exitWithHelp()
            throws Throwable
    {
    System.out.println("\nUsage: txt2dsd raw_data_fileName\n" +
            "It is mandatory that you provide both input arguments!\n" +
            "This java application converts raw data into the standard DSD format for" +
            "use within LibSVM. \n" +
            "Sopasakis Pantelis, Automatic Control Laboratory, National Technical University of Athens" +
            "\n");
    super.finalize();
    this.finalize();
    System.exit(0);

    }

 

    private void checkFile()
            throws Throwable
    {
        File dsdDataFile = new File(filename);
        File txtDataFile = new File(outputFilename);
        File molFile = new File(moleculesFile);

        boolean dsdExists = dsdDataFile.exists();
        boolean txtExists = txtDataFile.exists();
        boolean molExists = molFile.exists();
        boolean dsdIsFile = dsdDataFile.isFile();

        if (!dsdExists)
        {
            System.err.println("\n\u001b[1;37m The input file you specified does not exist! \u001b[m");
            exitWithHelp();
        }

        if (!dsdIsFile)
        {
            System.err.println("\n\u001b[1;37m Please specify a full file path! \u001b[m");
            exitWithHelp();
        }

        if (txtExists)
        {
            System.err.println("\n\u001b[1;37m Warning: The output file you specified already exists \u001b[m");
            System.out.println("All information in the previous version will be lost. " +
                    "\nAvoid overwritting by choosing different filenames");
        }

        if (molExists)
        {
            System.err.println("\n\u001b[1;37m Warning: The molecules file you specified already exists \u001b[m");
        }


    }


    public void run()
            throws Exception, IOException, Throwable
    {
        checkFile();


        // Read data from RAW (args[0])
        FileReader fr = new FileReader(filename);
        BufferedReader input = new BufferedReader(fr);

        // Write to TXT(args[1])
        FileWriter fwTxt = new FileWriter(outputFilename);
        BufferedWriter outputTxt = new BufferedWriter(fwTxt);

        // Write to MOL
        FileWriter fwMol = new FileWriter(moleculesFile);
        BufferedWriter outputMol = new BufferedWriter(fwMol);


        String line = input.readLine();
        String molName = "";
        while (line instanceof String){

            StringTokenizer st = new StringTokenizer(line);
            numberOfAttributes = st.countTokens();
            String newLine=new String();

            int i=0;
            String cl = "", index="";

            // Read Every line of the raw data file
            while ((st.hasMoreTokens())
                    && !(line.contains("%"))
                    && !(line.contains("//"))
                    && !(line.contains("!"))){

                // Read after the third column of each line (the parameters of
                // each molecule)
                if (i>=3)
                {
                newLine=newLine+(i-2)+":"+st.nextToken()+" ";
                }
                else{
                    if (i==0)
                    {
                        // Index at the first column
                        index = st.nextToken();
                    }
                    else if (i==1)
                    {
                        // Molecules's name at 2nd position (column)
                        molName=st.nextToken();
                    }
                    else if(i==2)
                    {
                        // Class (numeric) at third column
                        cl=st.nextToken();
                    }else{
                        // Other attributes
                        st.nextToken();
                    }

                }
                // Go to the next line
                i=i+1;

            }
            if(!line.contains("%")
                    && !line.contains("//")
                    && !line.contains("!"))
            {
                newLine = cl+" "+newLine;
                outputTxt.write(newLine+"\n");
            outputMol.write(index+" "+molName+"\n");

            }
            line = input.readLine();

            numberOfAttributes=i-2;

        }

            outputMol.flush();
            outputTxt.flush();

            outputMol.close();
            outputTxt.close();


    }




}