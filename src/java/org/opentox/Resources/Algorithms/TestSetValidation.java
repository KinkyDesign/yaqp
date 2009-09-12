package org.opentox.Resources.Algorithms;

import java.io.IOException;
import org.opentox.Resources.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.opentox.util.svm_predict;
import org.restlet.Context;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Reference;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.data.Status;
import org.restlet.resource.Representation;
import org.restlet.resource.Variant;


import weka.classifiers.Evaluation;
import weka.core.pmml.PMMLFactory;
import weka.core.pmml.PMMLModel;
import weka.classifiers.pmml.consumer.PMMLClassifier;
import weka.core.Instances;



/**
 *
 * @author OpenTox - http://www.opentox.org
 * @author Sopasakis Pantelis
 * @author Sarimveis Harry
 * @version 1.5 (Last Updated: Aug 20, 2009)
 */
public class TestSetValidation extends AbstractResource{

    private static final long serialVersionUID = 10012190006009910L;
    
    private volatile String dataid, modelid, algorithm_id;
    private int i;
    private double d;

    public TestSetValidation(Context context, Request request, Response response) {
        super(context, request, response);
        getVariants().add(new Variant(MediaType.TEXT_HTML));
        algorithm_id=Reference.decode(request.getAttributes().get("algorithm_id").toString());
        System.gc();
   }

    @Override
    public boolean allowGet()
    {
        return false;
    }

    @Override
    public boolean allowPost(){
        return true;
    }


    @Override
   public synchronized void acceptRepresentation(Representation entity){

        /**
         * Retrieve POSTed parameters...
         */
        Form form = new Form(entity);
        dataid=form.getFirstValue("dataid");
        modelid=form.getFirstValue("modelid");

        /**
         * Validate MLR regression models...
         */
        if (algorithm_id.equalsIgnoreCase("mlr"))
        {
            getResponse().setStatus(Status.SUCCESS_ACCEPTED);

            
            try{
                i=Integer.parseInt(dataid);
                File dataFile = new File(arffDir+"/"+dataSetPrefix+dataid);
                if (!(dataFile.exists())){
                    getResponse().setEntity(
                            "Error 400: Client Error, Bad Requset\n" +
                            "The request could not be understood by the server due " +
                            "to malformed syntax.\n" +
                            "Details: Data File Not Found on the server!\n", MediaType.TEXT_PLAIN);
                    getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
                }
            }catch(NumberFormatException e){
                getResponse().setEntity(
                        "Error 400: Client Error, Bad Requset\n" +
                        "The request could not be understood by the server due " +
                        "to malformed syntax.\n" +
                        "Details: The data file id that you provided is not valid" +
                        " : "+dataid+"\n", MediaType.TEXT_PLAIN);
                getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
            }

            if (getResponse().getStatus().equals(Status.SUCCESS_ACCEPTED)){
                try{
                    i=Integer.parseInt(modelid);
                    File modelFile = new File(REG_MLR_modelsDir+"/"+modelPrefix+modelid);
                    if (!(modelFile.exists())){
                        getResponse().setEntity(
                                "Error 400: Client Error, Bad Requset\n" +
                                "The request could not be understood by the server due " +
                                "to malformed syntax.\n" +
                                "Details: Model File Not Found on the server!\n", MediaType.TEXT_PLAIN);
                        getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
                    }
                }catch(NumberFormatException e){
                    getResponse().setEntity(
                            "Error 400: Client Error, Bad Requset\n" +
                            "The request could not be understood by the server due " +
                            "to malformed syntax.\n" +
                            "Details: The model file id that you provided is not valid" +
                            " : "+modelid+"\n", MediaType.TEXT_PLAIN);
                    getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
                }
            }


            if (getResponse().getStatus().equals(Status.SUCCESS_ACCEPTED)){
                try {
                    PMMLModel model = PMMLFactory.getPMMLModel(REG_MLR_modelsDir+"/"+modelPrefix+modelid);
                    if (model instanceof PMMLClassifier){
                        PMMLClassifier classifier = (PMMLClassifier)model;
                        Instances data = new Instances(
                            new BufferedReader(
                            new FileReader(arffDir+"/"+dataSetPrefix+dataid)));
                        data.setClassIndex(data.numAttributes()-1);
                        Evaluation evaluation = new Evaluation(data);
                        evaluation.evaluateModel(classifier, data);

                        StringBuilder builder = new StringBuilder();
                        builder.append(xmlIntro);
                        builder.append("<ValidationResult version=\"1.0\">");
                        builder.append("<Regression MeanAbsolutError=\""+evaluation.meanAbsoluteError()+"\" " +
                                " RootMeanSquaredError=\""+evaluation.rootMeanSquaredError()+"\" " +
                                " RelativeAbsoluteError=\""+evaluation.relativeAbsoluteError()+"\" " +
                                " RootRelativeSquaredError=\""+evaluation.rootRelativeSquaredError()+"\"></Regression>");
                        builder.append("</ValidationResult>\n\n");
                        BufferedWriter validationResultWriter =
                            new BufferedWriter(
                            new FileWriter(
                            MlrValidationResultsDir+"/"+validationResultPrefix+dataid+"-"+modelid));
                        validationResultWriter.write(builder.toString());
                        validationResultWriter.flush();
                        validationResultWriter.close();
                        /**
                         * Return the validation result URI:
                         */
                        getResponse().setEntity(
                                baseURI+"/validation_result/regression/mlr/"+validationResultPrefix+dataid+"-"+modelid+"\n\n",
                            MediaType.TEXT_PLAIN );
                        getResponse().setStatus(Status.SUCCESS_OK);
                    }
                }catch (Exception ex) {
                    getResponse().setEntity(
                            "Error 500: Server Error, Internal\n" +
                            "The server encountered an unexpected condition which prevented it from fulfilling the request.\n"+
                            "Details: " +ex.getMessage()+"\n\n"
                            , MediaType.TEXT_PLAIN);
                    getResponse().setStatus(Status.SERVER_ERROR_INTERNAL);
                    Logger.getLogger(TestSetValidation.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        }
        /**
         * Validate SVC classification models
         */
        else if (algorithm_id.equalsIgnoreCase("svc"))
        {
            String[] predictOptions =
            {
                // use probability distributions...
               "-b","1",
               // The scaled data...
               scaledDir+"/"+dataSetPrefix+dataid,
               // model built using the scaled data...
               CLS_SVM_modelsDir+"/"+modelPrefix+modelid,
               // temp file contains predictions and probabilities per instance...
               SvcValidationResultsDir+"/temp"
            };

            try {
                svm_predict.main(predictOptions);
                svcXmlValidationResult();
                //getResponse().setEntity(baseURI+"/validation_result/classification/svc/"+validationResultPrefix+dataid+"-"+modelid,MediaType.TEXT_PLAIN);
            } catch (IOException ex) {
                Logger.getLogger(TestSetValidation.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }// end of method: acceptRepresentation


    /**
     * Produce an XML representation for the validation result
     * corresponding to the SVC algorithm. The produced result is
     * saved at SvcValidationResultDir/validationResultPrefix-dataid-modelid
     *
     */
    private synchronized void svcXmlValidationResult() {

        String line="";
        int correctlyClassifiedInstances=0;
        String predictedValue="";
         
            try {
                    
                    Instances testInstances = new Instances(
                            new BufferedReader(
                            new FileReader(arffDir+"/"+dataSetPrefix+dataid)));

                    BufferedReader temp = new BufferedReader(new FileReader(SvcValidationResultsDir+"/temp"));

                    
                    
                    StringTokenizer FirstlineTokens = new StringTokenizer(temp.readLine());
                    String[] classNames = new String[FirstlineTokens.countTokens()-1];
                    String nss = FirstlineTokens.nextToken();

                    int p=0;
                    while(FirstlineTokens.hasMoreTokens()){
                        classNames[p]=FirstlineTokens.nextToken();
                        p++;
                    }
                    

                    BufferedWriter svcXmlOut =
                            new BufferedWriter(
                            new FileWriter(
                            SvcValidationResultsDir+"/"+validationResultPrefix+dataid+"-"+modelid));

                    StringBuilder builder = new StringBuilder();

                    svcXmlOut.write(xmlIntro);
                    svcXmlOut.write("<ValidationResult testDataSetId=\""+dataSetPrefix+dataid+"\" modelId=\""+modelPrefix+modelid+"\">\n");
                    svcXmlOut.write("<Header />\n");

                    builder.append("<ForEveryInstanceResults>\n");

                    int[][] comfusionMatrix = new int[p][p];
                    /**
                     * classifiedAs[i] stands for the number of instances that
                     * were classified as classNames[i]
                     */
                    double[] perClassSuccessRate = new double[p];
                    

                    for (int j=0;j<testInstances.numInstances();j++){

                        /**
                         * Read and tokenize the lines of the temp file where
                         * the predicted values are stored (with the corresponding probabilities)
                         */
                        line = temp.readLine();
                        StringTokenizer lineTokens = new StringTokenizer(line);


                          builder.append("<ForInstance id=\""+testInstances.instance(j).stringValue(0)+"\" >\n");
                          builder.append("<knownValue>\n"+
                                  classNames[
                                    Math.round((float) testInstances.instance(j).value(testInstances.numAttributes()-1))
                                    ]+"</knownValue>\n");
                          
                          predictedValue = lineTokens.nextToken();
                          builder.append("<predictedValue>"+Math.round((float)Double.parseDouble(predictedValue))+"</predictedValue>\n");

                          /**
                           * If the predicted value is equal to the real one,
                           * increase the number of correctly classified instances by one!
                           */
                          if (
                                  Integer.toString(Math.round((float)Double.parseDouble(predictedValue))).equalsIgnoreCase(
                                  classNames[Math.round((float) testInstances.instance(j).value(testInstances.numAttributes()-1))]
                                  )
                                  )
                          {
                            correctlyClassifiedInstances += 1;
                            // Increment the j-th diagonal entry of the comfusion matrix
                            comfusionMatrix[Math.round((float) testInstances.instance(j).value(testInstances.numAttributes()-1))][Math.round((float) testInstances.instance(j).value(testInstances.numAttributes()-1))] ++;
                          }else{
                              // Increment the corresponding entry of the comfusion matrix
                              int tempInt=0;
                              for (int l=0;l<classNames.length;l++){
                                if (predictedValue.equalsIgnoreCase(classNames[l])){
                                    tempInt=l;
                                }
                              }

                              comfusionMatrix[Math.round((float) testInstances.instance(j).value(testInstances.numAttributes()-1))][tempInt]++;

                          }

                          


                          builder.append("<ProbabilitiesPerClass>\n");
                                for (int k=0;k<classNames.length;k++){
                                    builder.append("<ForClass id=\""+classNames[k]+"\">"+lineTokens.nextToken()+"</ForClass>\n");
                                }
                            builder.append("</ProbabilitiesPerClass>\n");
                          builder.append("</ForInstance>\n");
                    }
                    builder.append("</ForEveryInstanceResults>\n");

                    svcXmlOut.write("<Summary>\n");
                    svcXmlOut.write("<testSetInstances>"+testInstances.numInstances()+"</testSetInstances>\n");
                    svcXmlOut.write("<correctlyClassified>"+correctlyClassifiedInstances+"</correctlyClassified>\n");
                    double successRate =0;
                    successRate=(double)correctlyClassifiedInstances;
                    successRate /= testInstances.numInstances();
                    svcXmlOut.write("<overallSuccessRate>"+successRate+"</overallSuccessRate>\n");
                    /**
                     * Comfusion Matrix XML
                     */
                    svcXmlOut.write("<ComfusionMatrix>\n");
                        for (int q=0;q<classNames.length;q++){
                            svcXmlOut.write("<ForClass id=\""+classNames[q]+"\">\n");
                                for (int j=0; j<classNames.length;j++){
                                    svcXmlOut.write("<ClassifiedAs id=\""+classNames[j]+"\" >"+comfusionMatrix[q][j]+"</ClassifiedAs>\n");
                                }
                            svcXmlOut.write("</ForClass>\n");
                        }
                    svcXmlOut.write("</ComfusionMatrix>\n");
                    /**
                     * Per Class Success Rates
                     */
                    double tempNumberOfInstances=0;
                    for (int j=0;j<p;j++){
                        tempNumberOfInstances=0;
                        for (int r=0;r<p;r++){
                            tempNumberOfInstances += comfusionMatrix[j][r];
                        }
                        perClassSuccessRate[j] = comfusionMatrix[j][j]/ tempNumberOfInstances;
                    }
                    svcXmlOut.write("<PerClassSuccess>\n");
                    for (int q=0;q<classNames.length;q++){
                        svcXmlOut.write("<ForClass id=\""+classNames[q]+"\" successRate=\""+perClassSuccessRate[q]+"\" />\n");
                    }
                    svcXmlOut.write("</PerClassSuccess>\n");
                    svcXmlOut.write("</Summary>\n");
                    svcXmlOut.write("<!--\n Information for every instance\n" +
                            "-->\n");
                    svcXmlOut.write(builder.toString());

                    svcXmlOut.write("</ValidationResult>\n");



                    svcXmlOut.flush();
                    svcXmlOut.close();
                   
                    getResponse().setEntity(baseURI+"/validation_result/classification/svc/"+
                            validationResultPrefix+dataid+"-"+modelid+"\n", MediaType.TEXT_PLAIN);

                } catch (Exception ex) {
                    Logger.getLogger(TestSetValidation.class.getName()).log(Level.SEVERE, null, ex);
                    getResponse().setEntity(
                            "Error 500: Server Error, Internal\n" +
                            "The server encountered an unexpected condition which prevented it from fulfilling the request.\n"+
                            "Details: " +ex.getMessage()+"\n\n"
                            , MediaType.TEXT_PLAIN);
                    getResponse().setStatus(Status.SERVER_ERROR_INTERNAL);
                }

    }






}// end of class


