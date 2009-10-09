package org.opentox.Resources.List;

import java.io.File;
import org.opentox.Resources.AbstractResource;
import org.restlet.Context;
import org.restlet.data.MediaType;
import org.restlet.data.Reference;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.data.Status;
import org.restlet.resource.Representation;
import org.restlet.resource.StringRepresentation;
import org.restlet.resource.Variant;

/**
 * Corresponding URI: /model/{model_type}/{algorithm_id}.<br/>
 *   e.g. /model/classification/svm<br/>
 *    or  /model/regression/mlr<br/>
 * @author OpenTox - http://www.opentox.org
 * @author Sopasakis Pantelis
 * @author Sarimveis Harry
 * @version 1.0 (Last Update: Aug 19, 2009)
 */
public class ListSomeModels extends AbstractResource {
    
    private static final long serialVersionUID = 10012190007001001L;
    
    private String model_type, algorithm_id;


    public ListSomeModels(Context context, Request request, Response response) {
        super(context, request, response);
        getVariants().add(new Variant(MediaType.TEXT_URI_LIST));
        algorithm_id = Reference.decode(request.getAttributes().get("algorithm_id").toString());
        model_type = Reference.decode(request.getAttributes().get("model_type").toString());
    }


    @Override
    public Representation represent(Variant variant){
        /**
         * 1. Classification Models
         */
        if (model_type.equalsIgnoreCase("classification")){
            /**
             * 1.1. SVM Classifiers
             */
            if (algorithm_id.equalsIgnoreCase("svc"))
            {
                File CLS_SVM_Folder = new File(CLS_SVM_modelsDir);
                File[] CLS_SVM_Models = CLS_SVM_Folder.listFiles();
                StringBuilder ClsSvmModelList = new StringBuilder();

                for (int i=0;i<CLS_SVM_Models.length;i++){
                ClsSvmModelList.append(baseURI+"/model/classification/svc/"+CLS_SVM_Models[i].getName()+"\n");
                }
                getResponse().setStatus(Status.SUCCESS_OK);
                return new StringRepresentation(ClsSvmModelList.toString(), MediaType.TEXT_PLAIN);
            }
            /**
             * 1.2. Other Classifiers
             */
            else{
                getResponse().setStatus(Status.SERVER_ERROR_NOT_IMPLEMENTED);
                return new StringRepresentation("Algorithm id not found!");
                
            }
        }
        /**
         * 2. Regression Models
         */
        else if (model_type.equalsIgnoreCase("regression")){
            /**
             * 2.1. MLR Models
             */
            if (algorithm_id.equalsIgnoreCase("mlr")){
                File REG_MLR_Folder = new File(REG_MLR_modelsDir);
                File[] REG_MLR_Models = REG_MLR_Folder.listFiles();
                StringBuilder RegMlrModelList = new StringBuilder();
                String modelName="";
                for (int i=0;i<REG_MLR_Models.length;i++){
                    modelName = REG_MLR_Models[i].getName();
                    
                    
                RegMlrModelList.append(baseURI+"/model/regression/mlr/"+modelName+"\n");
                modelName="";
                }
                getResponse().setStatus(Status.SUCCESS_OK);
                return new StringRepresentation(RegMlrModelList.toString(), MediaType.TEXT_PLAIN);
                
                
            }
            /**
             * 2.2. Other Regression Models
             */
            else{
                getResponse().setStatus(Status.SERVER_ERROR_NOT_IMPLEMENTED);
                return null;
                
            }
        }else{
            getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
            return null;
        }
        
    }



}
