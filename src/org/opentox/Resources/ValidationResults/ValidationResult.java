package org.opentox.Resources.ValidationResults;


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
 *
 * @author OpenTox - http://www.opentox.org/
 * @author Sopasakis Pantelis
 * @author Sarimveis Harry
 * @version 1.0 (Last update: Aug 23, 2009)
 *
 */
public class ValidationResult extends AbstractResource{

    private static final long serialVersionUID = 10012190002001001L;

    private String id, algorithm_id, model_type;
    

    /**
     * Default Class Constructor.Available MediaTypes of Variants: TEXT_XML
     * @param context
     * @param request
     * @param response
     */
    public ValidationResult(Context context, Request request, Response response) {
        super(context, request, response);
        getVariants().add(new Variant(MediaType.TEXT_XML));
        getVariants().add(new Variant(MediaType.TEXT_PLAIN));
        id = Reference.decode(request.getAttributes().get("id").toString());
        algorithm_id = Reference.decode(request.getAttributes().get("algorithm_id").toString());
        model_type = Reference.decode(request.getAttributes().get("model_type").toString());
    }

    /**
     *
     * @param variant
     * @return StringRepresentation
     */
    @Override
    public Representation represent(Variant variant)
    {
        /**
         * 1. Return XML representations for regression models:
         */
        if (model_type.equalsIgnoreCase("regression")){
            /**
             * 1.1. MLR validation Results
             * xml representation
             */
            if (algorithm_id.equalsIgnoreCase("mlr")){
                //////// ToDo! ////////////////////////////////////////
                    RepresentationFactory mlrValidationResult = new RepresentationFactory(MlrValidationResultsDir+"/"+id);
                try {
                    return new StringRepresentation(mlrValidationResult.getString().toString(), MediaType.TEXT_XML);
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(ValidationResult.class.getName()).log(Level.SEVERE, null, ex);
                    return null;
                } catch (IOException ex) {
                    Logger.getLogger(ValidationResult.class.getName()).log(Level.SEVERE, null, ex);
                    return null;
                }
                }else{
                    getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
                    return new StringRepresentation("Not found!");
                }// end of MLR representation
                
                
                /**
                 * 1.2. Other regression models
                 */
        }
            
        
        /**
         * 2. Return XML representations for classification models:
         */
        else if (model_type.equalsIgnoreCase("classification")){
            if (algorithm_id.equalsIgnoreCase("svc")){
                RepresentationFactory svcValidationResult = new RepresentationFactory(SvcValidationResultsDir+"/"+id);
                try {
                    return new StringRepresentation(svcValidationResult.getString().toString(), MediaType.TEXT_XML);
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(ValidationResult.class.getName()).log(Level.SEVERE, null, ex);
                    return null;
                } catch (IOException ex) {
                    Logger.getLogger(ValidationResult.class.getName()).log(Level.SEVERE, null, ex);
                    return null;
                }
            }else{
                return null;
            }
        }
        /**
         * Other than classification or regression
         */
        else{
            getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
            return new StringRepresentation("model_type can be either classification or regression. "+model_type+"" +
                    " is not an acceptable value!");
        }
        
            
    }

    /**
     * Allow validation results to be deleted.
     * @return
     */
    @Override
    public boolean allowDelete(){
        return true;
    }

    /**
     * Handle DELETE requests.
     */
    @Override
    public void removeRepresentations() 
    {
        getResponse().setEntity("Error 501: Not implemented yet!\n" +
                "The server does not support the functionality required to fulfill the request.\n",
                MediaType.TEXT_PLAIN);
        getResponse().setStatus(Status.SERVER_ERROR_NOT_IMPLEMENTED);
    }

}// End of class
