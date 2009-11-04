/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.opentox.util.converters;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.StringTokenizer;

/**
 *
 * @author OpenTox - http://www.opentox.org
 * @author Sopasakis Pantelis
 * @author Sarimveis Harry
 * @version 1.0 (Last Update: Aug 19, 2009)
 */
@Deprecated
public class txt2arff {

        private static final long serialVersionUID=100121700011001L;

        private String txtFile=null ,arffFile=null;
        private String[] classes = new String[100];
        private BufferedReader txt_input=null;

        public txt2arff(String txtFile, String arffFile){
                this.txtFile=txtFile;
                this.arffFile=arffFile;
        }


    /**
     * checks if the String word is an element of the String Array C
     * within its first j+1 entries, that is if word belongs to the set
     * {  C[i]  for 0 &lt; = i &lt; = j  }
     *
     * @param word
     * @param C
     * @param j
     * @return
     */
    private boolean alreadyRegistered(String word, String[] C, int j) {

        boolean isContained=false;

        for (int k=0;k<=j;k++){
            isContained = isContained||word.equals(C[k]);
        }

        return isContained;
    }

    private String getClassAttr()
            throws FileNotFoundException, IOException{
        FileReader txtFR = new FileReader(txtFile);
        txt_input = new BufferedReader(txtFR);

        String line, word, ClassAttr="@ATTRIBUTE  class  {";
        int i=0 ,j=0;

        while ((line=txt_input.readLine())!=null){

           StringTokenizer line_tok = new StringTokenizer(line);
           line_tok.nextToken();
           line_tok.nextToken();
           word=line_tok.nextToken();

           if (i==0){
           classes[0]=word;
           }


           /* Uncomment for developing purposes only!...
           System.out.println("CLASS = "+classes[j]+" WORD = "+
                   word+" IS CONTAINED = "+alreadyRegistered(word,classes,j));
             */


           if   (  (!(alreadyRegistered(word,classes,j+1))) && (i>0)  )
           {
               j++;
               classes[j]=word;
           }

           i++;
        }

        for (i=0;i<=j;i++)
        {
            if (i<j){
            ClassAttr += classes[i]+",";
            }
            else if (i==j){
            ClassAttr += classes[i]+"}";
            }
        }


        return ClassAttr;
    }


    private int getNumberOfDescriptors()
            throws FileNotFoundException, IOException{
        FileReader txtFR = new FileReader(txtFile);
        txt_input = new BufferedReader(txtFR);

        String line=txt_input.readLine();
        StringTokenizer line_tok = new StringTokenizer(line);

        int i=0;
        i = line_tok.countTokens();


        return i-3;
    }

    public void run()
            throws FileNotFoundException, IOException{
        String line=null, arffLine="", temp=null, classToken=null;
        int i=0;
        StringTokenizer line_tok=null;

        String ClassAttr = getClassAttr();
        int numberOfDescriptors = getNumberOfDescriptors();

        String arffIntro = "@RELATION openToxRelation\n" +
                "@ATTRIBUTE index String\n" +
                "@ATTRIBUTE compoundName String\n";

        for (int flag=0;flag<numberOfDescriptors;flag++){
            arffIntro += "@ATTRIBUTE descriptor"+flag+" real\n";
        }
        arffIntro += ClassAttr+"\n";
        arffIntro += "@DATA\n";



        FileReader txtFR = new FileReader(txtFile);
        txt_input = new BufferedReader(txtFR);

        FileWriter arffFW = new FileWriter(arffFile);
        BufferedWriter output = new BufferedWriter(arffFW);

        output.write(arffIntro);

        while ((line=txt_input.readLine())!=null){
            line_tok = new StringTokenizer(line);

            i=1;
            while (line_tok.hasMoreTokens()){
                temp = line_tok.nextToken();
                if (i!=3)
                {
                    arffLine +=temp+",";
                }
                else if (i==3){
                    classToken=temp;
                }
                i++;
            }
            arffLine +=classToken;
            output.write(arffLine+"\n");
            arffLine="";

        }


        output.flush();
        output.close();
    }


   
}
