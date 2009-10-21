package org.opentox.Resources.Models;


import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.opentox.Resources.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.opentox.util.RepresentationFactory;
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
 * GET the representation of a model given its id. The corresponding URIs have
 * the structure: http://opentox.ntua.gr:3000/OpenToxServices/models/classification/{algorithm_id}/{model_id} and
 * http://opentox.ntua.gr:3000/OpenToxServices/models/classification/{algorithm_id}/{model_id}
 * where algorithm_id in case of classification is one of svm, knn, j48 or pls and in case of
 * regression is one of mlr, pls or svm.
 *
 * @author OpenTox - http://www.opentox.org/
 * @author Sopasakis Pantelis
 * @author Sarimveis Harry
 * @version 1.3 (Last update: Aug 27, 2009)
 * @since 1.1
 *
 */
public class Model extends AbstractResource{

    private static final long serialVersionUID = 10012190007003005L;

    private String model_id, algorithm_id, model_type;
    

    /**
     * Default Class Constructor.Available MediaTypes of Variants: TEXT_XML
     * @param context
     * @param request
     * @param response
     */
    public Model(Context context, Request request, Response response) {
        super(context, request, response);
        getVariants().add(new Variant(MediaType.TEXT_XML));
        getVariants().add(new Variant(MediaType.TEXT_PLAIN));
        model_id = Reference.decode(request.getAttributes().get("model_id").toString());
        //algorithm_id = Reference.decode(request.getAttributes().get("algorithm_id").toString());
        //model_type = Reference.decode(request.getAttributes().get("model_type").toString());
    }

    /**
     *
     * @param variant
     * @return StringRepresentation
     */
    @Override
    public Representation represent(Variant variant)
    {
        /*
        if (model_type.equalsIgnoreCase("regression")){
            if (algorithm_id.equalsIgnoreCase("mlr")){
                File MLRmodelFile = new File(REG_MLR_modelsDir+"/"+model_id);
                if (MLRmodelFile.exists()){
                    RepresentationFactory model = new RepresentationFactory(MLRmodelFile.getAbsolutePath());
                    try {
                        getResponse().setStatus(Status.SUCCESS_OK);
                        return new StringRepresentation(model.getString().toString(), MediaType.TEXT_XML);
                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(Model.class.getName()).log(Level.SEVERE, null, ex);
                        getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
                        return new StringRepresentation("Model not found! Exception Details: "+ex.getMessage());
                    } catch (IOException ex) {
                        Logger.getLogger(Model.class.getName()).log(Level.SEVERE, null, ex);
                        getResponse().setStatus(Status.SERVER_ERROR_INTERNAL);
                        return new StringRepresentation("IO Exception! Details: "+ex.getMessage());
                    }
                }
                else{
                    getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
                    return new StringRepresentation("Model not found!");
                }// end of MLR representation
                
                
            }else if (algorithm_id.equalsIgnoreCase("svm")){
                    File SVMmodelFile = new File(REG_SVM_modelsDir+"/xml/"+model_id+".xml");
                    logger.info("Looking for : "+REG_SVM_modelsDir+"/xml/"+model_id+".xml");
                    RepresentationFactory model = new RepresentationFactory(REG_SVM_modelsDir+"/xml/"+model_id+".xml");
                    try {
                        getResponse().setStatus(Status.SUCCESS_OK);
                        return new StringRepresentation(model.getString().toString(), MediaType.TEXT_XML);
                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(Model.class.getName()).log(Level.SEVERE, null, ex);
                        getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
                        return new StringRepresentation("Model not found! Exception Details: "+ex.getMessage());
                    } catch (IOException ex) {
                        Logger.getLogger(Model.class.getName()).log(Level.SEVERE, null, ex);
                        getResponse().setStatus(Status.SERVER_ERROR_INTERNAL);
                        return new StringRepresentation("IO Exception! Details: "+ex.getMessage());
                    }
                }
            else{
                getResponse().setStatus(Status.SERVER_ERROR_NOT_IMPLEMENTED);
                return new StringRepresentation("Not implemented yet! Only MLR is implemented for the moment...");
            }
            
        }
        else if (model_type.equalsIgnoreCase("classification")){
            if (algorithm_id.equalsIgnoreCase("svc")){
                File SvmClsModel = new File(CLS_SVM_modelsDir+"/"+model_id);
                if (SvmClsModel.exists()){
                    //todo : delete the following line and return xml ok
                    RepresentationFactory model = new RepresentationFactory(CLS_SVM_modelsDir+"/xml/"+model_id + ".xml");
                    
                    try {
                        return new StringRepresentation(model.getString().toString(), MediaType.TEXT_XML);
                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(Model.class.getName()).log(Level.SEVERE, null, ex);
                        getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
                        return new StringRepresentation("Model not found! Exception Details: "+ex.getMessage());
                    } catch (IOException ex) {
                        Logger.getLogger(Model.class.getName()).log(Level.SEVERE, null, ex);
                        getResponse().setStatus(Status.SERVER_ERROR_INTERNAL);
                        return new StringRepresentation("IO Exception! Details: "+ex.getMessage());
                    }
                }else{// The requested model does not exist :(
                    getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
                    return new StringRepresentation("Model not found!");
                }
            }


            return new StringRepresentation("Not implemented yet!");
        }
        else{
            getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
            return new StringRepresentation("model_type can be either classification or regression. "+model_type+"" +
                    " is not an acceptable value!");
        }
        */
        File modelXmlFile = new File(modelsXmlDir + "/" + model_id);
        if (modelXmlFile.exists()){
                RepresentationFactory model = new RepresentationFactory(modelXmlFile.getAbsolutePath());
                try {
                    getResponse().setStatus(Status.SUCCESS_OK);
                    return new StringRepresentation(model.getString().toString(), MediaType.TEXT_XML);
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(Model.class.getName()).log(Level.SEVERE, null, ex);
                    getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
                    return new StringRepresentation("Model not found! Exception Details: "+ex.getMessage());
                } catch (IOException ex) {
                    Logger.getLogger(Model.class.getName()).log(Level.SEVERE, null, ex);
                    getResponse().setStatus(Status.SERVER_ERROR_INTERNAL);
                    return new StringRepresentation("IO Exception! Details: "+ex.getMessage());
                }
            }
            else{
                getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
                return new StringRepresentation("Model not found!");
            }
    }

    @Override
    public boolean allowDelete(){
        return true;
    }

    @Override
    public void removeRepresentations() {
        if (model_type.equalsIgnoreCase("classification")){
            
            if (algorithm_id.equalsIgnoreCase("svc")){

                if (!(model_id.equals("allOfThem"))){
                    File svmModelToBeDeleted = new File(CLS_SVM_modelsDir+"/"+model_id);
                    if (!(svmModelToBeDeleted.exists())){
                        getResponse().setEntity("The specified model was not found on the server.\n",
                                MediaType.TEXT_PLAIN);
                        getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
                    }else{
                        svmModelToBeDeleted.delete();
                        if (svmModelToBeDeleted.exists()){
                            getResponse().setEntity("Internal Server Error!\n" +
                                "The model was not deleted!\n", MediaType.TEXT_PLAIN);
                            getResponse().setStatus(Status.SERVER_ERROR_INTERNAL);

                        }
                    }
                }else{
                    File SvmModelsFolder = new File(CLS_SVM_modelsDir);
                    File[] SvmModels = SvmModelsFolder.listFiles();
                    if (SvmModels.length>0){
                        for (int i=0;i<SvmModels.length;i++){
                            SvmModels[i].delete();
                        }
                    }else{
                        getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
                        getResponse().setEntity("No SVM models were found on the server!\n", MediaType.TEXT_PLAIN);
                    }

                }
                
            }else{
                getResponse().setStatus(Status.SERVER_ERROR_NOT_IMPLEMENTED);
                // other than svm classification models
            }

        }else if (model_type.equalsIgnoreCase("regression")){

            if (algorithm_id.equalsIgnoreCase("mlr")){

                if (!(model_id.equals("allOfThem"))){
                    File MlrModelToBeDeleted = new File(REG_MLR_modelsDir+"/"+model_id);
                    if (!(MlrModelToBeDeleted.exists())){
                        getResponse().setEntity("The specified model was not found on the server.\n",
                                MediaType.TEXT_PLAIN);
                        getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
                    }else{
                        MlrModelToBeDeleted.delete();
                        if (MlrModelToBeDeleted.exists()){
                            getResponse().setEntity("Internal Server Error!\n" +
                                "The model was not deleted!\n", MediaType.TEXT_PLAIN);
                            getResponse().setStatus(Status.SERVER_ERROR_INTERNAL);
                        }
                    }
                }else{
                    File MlrModelsFolder = new File(REG_MLR_modelsDir);
                    File[] MlrModels = MlrModelsFolder.listFiles();
                    if (MlrModels.length>0){
                        for (int i=0;i<MlrModels.length;i++)
                        {
                            MlrModels[i].delete();
                        }
                    }else{
                        getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
                        getResponse().setEntity("No MLR models were found on the server!\n", MediaType.TEXT_PLAIN);
                    }
                }
                

            }else{
                getResponse().setStatus(Status.SERVER_ERROR_NOT_IMPLEMENTED);
                // other than MLR regression models
            }

        }else{
            getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
            getResponse().setEntity("Error 404. Bad Request!", MediaType.TEXT_PLAIN);
        }
    }

}// End of class
