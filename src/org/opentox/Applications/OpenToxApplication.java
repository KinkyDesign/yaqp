 package org.opentox.Applications;


import java.util.HashMap;
import java.util.Map;
import org.opentox.Resources.aux.TrainerMLR;
import org.opentox.Resources.aux.ValidatorMLR;
import org.opentox.Resources.aux.Selector;
import org.opentox.Resources.aux.TrainerSVC;
import org.opentox.Resources.Algorithms.TestSetValidation;
import org.opentox.Resources.Models.Model;
import org.opentox.Resources.List.ListAllModels;
import org.opentox.Resources.Algorithms.AttributeSelection;
import org.opentox.Resources.List.ListLearningAlgorithms;
import org.opentox.Resources.Algorithms.Classification;
import org.opentox.Resources.List.ListAlgorithms;
import org.opentox.Resources.Algorithms.Regression;
import org.opentox.Resources.List.ListClassificationAlgorithms;
import org.opentox.Resources.List.ListValidationRoutines;
import org.opentox.Resources.List.ListRegressionAlgorithms;
import org.opentox.Resources.Datasets.DataSet;
import org.opentox.Resources.List.ListDataSets;
import org.opentox.Resources.IndexResource;
import org.opentox.Resources.AbstractResource;
import org.opentox.Resources.List.ListSomeModels;
import org.opentox.Resources.StyleSheetResource;
import org.opentox.Resources.ValidationResults.ValidationResult;
import org.opentox.Resources.aux.TrainerSVM;
import org.opentox.Resources.aux.UploadArff;
import org.opentox.Resources.aux.UploadFromWeb;
import org.opentox.Resources.aux.ValidatorSVC;
import org.restlet.Application;
import org.restlet.Component;
import org.restlet.Directory;
import org.restlet.Guard;
import org.restlet.Restlet;
import org.restlet.Router; 
import org.restlet.VirtualHost;
import org.restlet.data.ChallengeScheme;
import org.restlet.data.LocalReference;
import org.restlet.data.Protocol;
import org.restlet.data.Request;


/**
 *  Initial Implementation of the OpenTox Restful WebServices.<br/>
 *  Copyright &copy; 2009,  OpenTox http://www.opentox.org/ http://opentox.ntua.gr:3000/
 *
 *  <p style="width:75%" align=justify>
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *  </p>
 *  <p style="width:75%" align=justify>
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *  </p>
 *  <p style="width:75%" align=justify>
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <a href=http://www.gnu.org/licenses/>http://www.gnu.org/licenses/</a>.
 *  </p>
 *
 * @author OpenTox Team - http://www.opentox.org/
 * @author Sopasakis Pantelis
 * @author Sarimveis Harry
 * @version 1.4 (Last Update: Aug 27, 2009)
 *
 */
 public class OpenToxApplication extends Application{

     private static final long serialVersionUID = 749479721274764426L;

     
    

     /**
      * Constructor.
      */
     public OpenToxApplication(){
        System.gc();
        
        }
     

     /**
      * Creates a root Restlet that will receive all incoming calls.
      * <p>Brief list of services:
      * <ul>
      * <li><tt>GET /algorithm/learning</tt>
      *     List of all available learning algorithms</li>
      * <li><tt>GET /algorithm/learning/regression</tt>
      *     List of all available regression algorithms</li>
      * <li><tt>GET /algorithm/learning/regression/{id}</tt>
      *     XML representation of a regression algorithm.</li>
      * <li><tt>POST /algorithm/learning/regression/{id}</tt>
      *     Train a new regression model.</li>
      * <li><tt>GET /algorithm/learning/classification</tt>
      *     List of classification algorithm (URIs)</li>
      * <li><tt>GET /algorithm/learning/classification/{id}</tt>
      *     XML representation of classification algorithm</li>
      * <li><tt>POST /algorithm/learning/classification/svm/{id}</tt>
      *     Train a new classification model.</li>
      * <li><tt>GET /model</tt>
      *     List (in xml format) of all available models</li>
      * <li><tt>GET /model/{model_type}/{algorithm_id}</tt>
      *     List of all available models of a specified type (classification or regression)</li>
      * <li><tt>GET /model/{model_type}/{algorithm_id}/{model_id}</tt>
      *     Retrieve a stored model.</li>
      * <li><tt>GET /fileupload</tt>
      *     HTML form that facilitates the user in uploading a new datafile on the server.</li>
      * </ul>
      * </p>
      */
     @Override
         public final synchronized Restlet createRoot() {

         /**
          * Guard sensitive resources!
          * Create a guard - set username and password...
          */
         final Guard UploalGuard = new Guard(getContext(), ChallengeScheme.HTTP_DIGEST,
                 "To avoid spamming we ask you to provide your username and password..."){
             @Override
             public int authenticate(Request req)
             {
                if (req.getMethod().equals(org.restlet.data.Method.GET)) return AUTHENTICATION_VALID;
                return super.authenticate(req);
             }
         };

         /**
          * Create another guard...
          */
         final Guard UploadFromWWWGuard = new Guard(getContext(), ChallengeScheme.HTTP_BASIC,
                 "To avoid spamming we ask you to provide your username and password...")
         {
             @Override
             public int authenticate(Request req){
                if (req.getMethod().equals(org.restlet.data.Method.GET)) return AUTHENTICATION_VALID;
                return super.authenticate(req);
             }
         };
        

         Map<String,char[]> secrets = SetUserIds();


         UploalGuard.getSecrets().putAll(secrets);
         UploalGuard.setNonceLifespan(4000);
         UploalGuard.setNext(UploadArff.class);

         UploadFromWWWGuard.getSecrets().putAll(secrets);
         UploadFromWWWGuard.setNext(UploadFromWeb.class);


         /**
          * Guard for the deletion of resources.
          * Only administrators can delete Resources.
          * ..........
          * ..........
          */
          final Guard ModelGuard = new Guard(getContext(), ChallengeScheme.HTTP_BASIC,
                 "Only administrators are authorized to delete models...")
          {
             @Override
             public int authenticate(Request req){
                if (req.getMethod().equals(org.restlet.data.Method.GET)) return AUTHENTICATION_VALID;
                return super.authenticate(req);
             }
          };
         ModelGuard.getSecrets().putAll(SetAdminIds());
         ModelGuard.setNext(Model.class);

         /**
          * Guards the removal of datasets...
          */
         final Guard DataSetGuard = new Guard(getContext(), ChallengeScheme.HTTP_BASIC,
                 "Only administrators are authorized to delete dataset...")
          {
             @Override
             public int authenticate(Request req){
                if (req.getMethod().equals(org.restlet.data.Method.GET)) return AUTHENTICATION_VALID;
                return super.authenticate(req);
             }
          };
         DataSetGuard.getSecrets().putAll(SetAdminIds());
         DataSetGuard.setNext(DataSet.class);



         Router router = new Router(getContext());

         /**
          * We set Retry Delay to 1sec
          */
         router.setRetryDelay(1L);

         /**
          * We set maximum attempts to 5
          */
         router.setMaxAttempts(5);

         /**
          * Index Resource and stylesheet
          * (This is soon going to be removed, as we will move to a
          * new Web Interface...)
          */
         router.attach("", IndexResource.class);
         router.attach("/styles/style1.css",StyleSheetResource.class);

         /**
          * Resources compliant to the
          * OpenTox API specifications:
          */
         router.attach("/algorithm",ListAlgorithms.class);
         router.attach("/algorithm/learning", ListLearningAlgorithms.class);
         router.attach("/algorithm/learning/regression", ListRegressionAlgorithms.class);
         router.attach("/algorithm/learning/regression/{id}", Regression.class);// {id} stands for the algoritm's id
         router.attach("/algorithm/learning/classification", ListClassificationAlgorithms.class);
         router.attach("/algorithm/learning/classification/{id}", Classification.class);// {id} stands for the algoritm's id
         router.attach("/algorithm/preprocessing/featureselection/{algorithm_id}",AttributeSelection.class);
         router.attach("/validation",ListValidationRoutines.class);
         router.attach("/validation/test_set_validation/{algorithm_id}",TestSetValidation.class);
         router.attach("/validation_result/{model_type}/{algorithm_id}/{id}", ValidationResult.class);// e.g. /validation_result/classification/svc/validation-36-28-15
         router.attach("/model",ListAllModels.class);
         router.attach("/model/{model_type}/{algorithm_id}",ListSomeModels.class);
         router.attach("/model/{model_type}/{algorithm_id}/{model_id}", ModelGuard);// The deletion of models is guarded!!!
         router.attach("/dataset",ListDataSets.class);// {type} stands for the dataset mediatype
         router.attach("/dataset/{id}",DataSetGuard);// The deletion of datasets is guarded!!!
                  
         /**
          * Auxiliary Resources
          * HTML forms for the consumption of the webservices.
          */
           router.attach("/fileupload/arff",UploalGuard);// File upload is guarded!!!
           router.attach("/fileupload/fromwww",UploadFromWWWGuard);// File upload is guarded!!!
           router.attach("/trainer/svc",TrainerSVC.class);
           router.attach("/trainer/svm",TrainerSVM.class);
           router.attach("/trainer/mlr",TrainerMLR.class);
           router.attach("/selector",Selector.class);
           router.attach("/validator/mlr", ValidatorMLR.class);
           router.attach("/validator/svc", ValidatorSVC.class);

                 
         

         return router;
     }


     /**
      * The server can run using this main method...
      * @param args
      * @throws Exception
      */
     public static void main(String[] args) throws Exception {

           // Create a component
           Component component = new Component();

           component.getServers().add(Protocol.HTTP, 3000);

           component.getClients().add(Protocol.FILE);
           

           LocalReference javadoc =
                    LocalReference.createFileReference(
                  AbstractResource.javadocDir);

           LocalReference home =
                   LocalReference.createFileReference(
                  AbstractResource.HTMLDir);

           LocalReference sourceCode =
                   LocalReference.createFileReference("/home/chung/NetBeansProjects/RESTful/OpenToxServices/src.tar.gz");

           Directory javadocDirectory = new Directory(component.getContext().createChildContext(), javadoc);
           Directory homeDirectory = new Directory(component.getContext().createChildContext(), home);
           Directory sourceCodeDirectory = new Directory(component.getContext().createChildContext(), sourceCode);
                
           Application application = new OpenToxApplication();
         
           VirtualHost host = new VirtualHost();

           host.attach("/OpenToxServices",application);
           host.attach("",homeDirectory);
           host.attach("/OpenToxServices/javadoc", javadocDirectory);
           host.attach("/OpenToxServices/source", sourceCodeDirectory);


           component.setDefaultHost(host);

           component.start();

       }



    private Map<String, char[]> SetUserIds() {
        Map<String, char[]> secret = new HashMap<String, char[]>();
        secret.put("itsme", "letmein".toCharArray());
        secret.putAll(SetAdminIds());
        return secret;
    }

    /**
     * True Random Passwords generated from
     * http://www.goodpassword.com/
     * @return secret
     */
    private Map<String, char[]> SetAdminIds() {
        Map<String, char[]> secret = new HashMap<String, char[]>();
        secret.put("????","?".toCharArray());
        secret.put("??","?".toCharArray());
        return secret;
    }

     
     }
 
