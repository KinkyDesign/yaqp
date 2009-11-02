 package org.opentox.Applications;


import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import org.opentox.Resources.AbstractResource;
import org.opentox.Resources.Models.Model;
import org.opentox.Resources.List.ListAllModels;
import org.opentox.Resources.List.ListLearningAlgorithms;
import org.opentox.Resources.Algorithms.Classification;
import org.opentox.Resources.List.ListAlgorithms;
import org.opentox.Resources.Algorithms.Regression;
import org.opentox.Resources.List.ListClassificationAlgorithms;
import org.opentox.Resources.List.ListRegressionAlgorithms;
import org.opentox.Resources.IndexResource;
import org.opentox.Resources.StyleSheetResource;
import org.opentox.Resources.fileupload.UploadArff;
import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.data.ChallengeScheme;
import org.restlet.data.Request;
import org.opentox.database.InHouseDB;
import org.restlet.routing.Router;
import org.restlet.security.Guard;


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
      * Connection to the Models' database.
      */
     public  static InHouseDB dbcon;

     /**
      * Server logs.
      */
     public static Logger opentoxLogger;


    

     /**
      * Constructor.
      */
     public OpenToxApplication() throws IOException{
        if (!(new File(AbstractResource.Directories.logDir)).exists()) {
                    new File(AbstractResource.Directories.logDir).mkdirs();
                    System.out.println("x");
        }
        opentoxLogger=Logger.getLogger("org.restlet");
        
        FileHandler fileHand = new FileHandler(AbstractResource.Directories.logDir+"/"+new Date());
        fileHand.setFormatter(new SimpleFormatter());
        opentoxLogger.addHandler(fileHand);
        opentoxLogger.setLevel(Level.INFO);
        dbcon = new InHouseDB();
        AbstractResource.Directories.checkDirs();
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
      * <li><tt>GET /model?searchAlgorithm=keyword</tt>
      *     List (in text/uri-list format) of all available models. You can also
      * use the optional query ?searchAlgorithm=keyword to search for model URIs
      * that were built using an algorithm relevant to a keyword. For example, this
      * keyword could be 'classificataion' or 'regression' or 'svc' etc...</li>
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

         

         Router router = new Router(getContext());
         

         /**
          * We set Retry Delay to 1sec
          */
         router.setRetryDelay(1L);


         /**
          * We set maximum attempts to 5
          */
         router.setMaxAttempts(20);

         /**
          * Index Resource and stylesheet
          * (This is soon going to be removed, as we will move to a
          * new Web Interface...)
          */
         router.attach("", IndexResource.class);
         router.attach("/styles/style1.css",StyleSheetResource.class);
         router.attach("/up",UploadArff.class);

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
         router.attach("/model",ListAllModels.class);
         router.attach("/model/{model_id}", ModelGuard);// The deletion of models is guarded!!!



                    

         return router;
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
 
