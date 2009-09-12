package org.opentox.Resources.List;

import org.opentox.Resources.*;
import java.io.File;

import org.restlet.Context;
import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.resource.Representation;
import org.restlet.resource.StringRepresentation;
import org.restlet.resource.Variant;

/**
 * Lists all classificantion and regression models - returns an xml representation
 * for that list.
 * @author OpenTox Team, http://www.opentox.org/
 * @author Sopasakis Pantelis
 * @author Sarimveis Harry
 * @version 1.1 (Last update: Aug 27, 2009)
 */
public class ListAllModels extends AbstractResource {

    private static final long serialVersionUID = 10012190007002001L;
    
    public ListAllModels(Context context, Request request, Response response) {
        super(context, request, response);
        getVariants().add(new Variant(MediaType.TEXT_XML));
    }


    /**
     *
     * @param variant
     * @return XML file containings the ids of all classification and regression models.
     */
    @Override
    public Representation represent(Variant variant) {
    	

    	
    	File CLS_SVM_folder = new File(CLS_SVM_modelsDir);
    	File[] CLS_SVM_Models = CLS_SVM_folder.listFiles();

        File REG_MLR_folder = new File(REG_MLR_modelsDir);
        File[] REG_MLR_Models = REG_MLR_folder.listFiles();

        File REG_SVM_folder = new File(REG_SVM_modelsDir);
        File[] REG_SVM_Models = REG_SVM_folder.listFiles();

        StringBuilder builder = new StringBuilder();
        builder.append(xmlIntro);
        builder.append("<ListOfModels date=\""+java.util.GregorianCalendar.getInstance().getTime()+"\">");
        

        builder.append("<ClassificationModels uri=\""+baseURI+"/models/classification/{algorithm_id}/{model_id}\" >");
        builder.append("<!--" +
                "The SVC model with id model-x-y (where x,y are two nonnegative integers)\n corresponds to a" +
                "model that was trained using the data set dataSet-x. The number y\n is unique for every model." +
                "-->");
        builder.append("<SVC numberOfModels=\""+CLS_SVM_Models.length+"\" uri=\""+baseURI+"/models/classification/svc/{model_id}\">");
        for(int i = 0; i < CLS_SVM_Models.length; i++){
        	if ((CLS_SVM_Models[i].isFile())&&!(CLS_SVM_Models[i].isHidden())&&!(CLS_SVM_Models[i].toString().contains("~"))){
                    builder.append("<Model id=\""+CLS_SVM_Models[i].getName()+"\" />");
        	}
        }
        builder.append("</SVC>");
        builder.append("</ClassificationModels>");

        
        
        builder.append("<RegressionModels uri=\""+baseURI+"/models/regression/{algorithm_id}/{model_id}\" >");

        builder.append("<!--" +
                "The MLR model model-x (where x is a nonnegative integer)\n was trained using the dataset with id: dataSet-x" +
                "-->");
        builder.append("<MLR numberOfModels=\""+REG_MLR_Models.length+"\"  uri=\""+baseURI+"/models/regression/mlr/{model_id}\" >");
                
        for(int i = 0; i < REG_MLR_Models.length; i++){
        	if ((REG_MLR_Models[i].isFile())&&!(REG_MLR_Models[i].isHidden())&&!(REG_MLR_Models[i].toString().contains("~"))){
                                        
                    builder.append("<Model id=\""+REG_MLR_Models[i].getName()+"\" />");
        	}
        }
        
        builder.append("</MLR>");
        builder.append("<!--" +
                "The SVM model with id model-x-y (where x,y are two nonnegative integers)\n corresponds to a" +
                "model that was trained using the data set dataSet-x. The number y\n is unique for every model." +
                "-->");
        builder.append("<SVM numberOfModels=\""+REG_SVM_Models.length+"\" uri=\""+baseURI+"/models/regression/svm/{model_id} \">");

        for (int i = 0 ; i< REG_SVM_Models.length; i++){
            if ((REG_SVM_Models[i].isFile())&&!(REG_SVM_Models[i].isHidden())&&!(REG_SVM_Models[i].toString().contains("~"))){

                    builder.append("<Model id=\""+REG_SVM_Models[i].getName()+"\" />");
        	}
        }

        builder.append("</SVM>");
        builder.append("</RegressionModels>");

        builder.append("</ListOfModels>");
        return new StringRepresentation(builder.toString(), MediaType.TEXT_XML);
    }

}