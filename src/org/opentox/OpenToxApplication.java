package org.opentox;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import javax.security.auth.Subject;
import org.opentox.resource.AbstractResource;
import org.opentox.resource.Algorithm;
import org.opentox.resource.ModelResource;
import org.opentox.resource.ListModels;
import org.opentox.resource.ListAlgorithms;
import org.opentox.resource.IndexResource;
import org.opentox.resource.ModelInfoResource;
import org.opentox.resource.ShutDownResource;
import org.opentox.auth.CredentialsVerifier;
import org.opentox.auth.GuardDog;
import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.data.ChallengeScheme;
import org.restlet.data.Request;
import org.opentox.database.InHouseDB;
import org.opentox.auth.Priviledges;
import org.opentox.resource.TaskResource;
import org.restlet.data.Method;
import org.restlet.data.Response;
import org.restlet.routing.Router;
import org.restlet.security.ChallengeAuthenticator;
import org.restlet.security.ChallengeGuard;
import org.restlet.security.Enroler;
import org.restlet.security.MethodAuthorizer;
import org.restlet.security.UniformGuard;
import org.restlet.security.Verifier;
import org.restlet.service.TaskService;
import org.restlet.util.Template;

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
 * @author OpenTox - http://www.opentox.org/
 * @author Sopasakis Pantelis
 * @author Sarimveis Harry
 * @version 1.3.3 (Last update: Dec 20, 2009)
 */
public class OpenToxApplication extends Application {

    private static final long serialVersionUID = 749479721274764426L;

    /**
     * Connection to the Models' database.
     */
    public static InHouseDB dbcon;
    /**
     * Server logs.
     */
    public static Logger opentoxLogger;
    public static ExecutorService executor;
    private static final int THREADS = 100;

    private static OpenToxApplication instanceOfThis = null;

    public final static OpenToxApplication INSTANCE = getInstance();

    private static OpenToxApplication getInstance()  {
        if (instanceOfThis == null) {
            try {
                instanceOfThis = new OpenToxApplication();
            } catch (IOException ex) {
                Logger.getLogger(OpenToxApplication.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return instanceOfThis;
    }




    /**
     * Constructor.
     */
    private OpenToxApplication() throws IOException {
        if (!(new File(AbstractResource.Directories.logDir)).exists()) {
            new File(AbstractResource.Directories.logDir).mkdirs();
        }
        AbstractResource.Directories.checkDirs();
        opentoxLogger = Logger.getLogger("org.restlet");
        FileHandler fileHand = new FileHandler(AbstractResource.Directories.logDir + "/" + new Date());
        fileHand.setFormatter(new SimpleFormatter());
        opentoxLogger.addHandler(fileHand);
        opentoxLogger.setLevel(Level.INFO);
        dbcon = InHouseDB.INSTANCE;
        executor = Executors.newFixedThreadPool(THREADS);
    }





    /**
     * Creates a root Restlet that will receive all incoming calls.
     * <p>Brief list of services:
     * <ul>
     * <li><tt>GET /algorithm</tt>
     *     List of all available algorithms</li>
     * <li><tt>GET /algorithm/{id}</tt>
     *     Representation of an algorithm.</li>
     * <li><tt>POST /algorithm/{id}</tt>
     *     Run the algorithm.</li>
     * <li><tt>GET /model?searchAlgorithm=keyword</tt>
     *      List (in text/uri-list format) of all available models. You can also
     *      use the optional query ?searchAlgorithm=keyword to search for model URIs
     *      that were built using an algorithm relevant to a keyword. For example, this
     *      keyword could be 'classificataion' or 'regression' or 'svc' etc...</li>
     * <li><tt>GET /model/{id}</tt>
     *      Representation of a model.
     * </li>
     * <li><tt>POST /model/{id}</tt>
     *      Perform a prediction.
     * </li>
     * </ul>
     * </p>
     */
    @Override
    public final synchronized Restlet createRoot() {


        /**
         * Authenticate authorized users.
         */
        Verifier verifier = new CredentialsVerifier(this, Priviledges.USER);
        List<Method> modelMethods = new ArrayList<Method>();
        modelMethods.add(Method.GET);
        modelMethods.add(Method.POST);
        modelMethods.add(Method.DELETE);
        List<Method> modelFreeMethods = new ArrayList<Method>();
        modelFreeMethods.add(Method.GET);
        modelFreeMethods.add(Method.POST);
        UniformGuard modelKerberos = new GuardDog().
                createGuard(
                this,
                verifier,
                false,
                modelMethods,
                modelFreeMethods,
                ModelResource.class);
        modelKerberos.setNext(ModelResource.class);

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
         * Some other configurations
         */
        router.setDefaultMatchingMode(Template.MODE_STARTS_WITH);
        router.setRoutingMode(Router.BEST);

        /**
         * Index Resource and stylesheet
         * (This is soon going to be removed, as we will move to a
         * new Web Interface...)
         */
        router.attach(
                "", IndexResource.class);


        /**
         * Resources compliant to the
         * OpenTox API specifications:
         */
        router.attach("/algorithm", ListAlgorithms.class);
        router.attach("/algorithm/{id}", Algorithm.class);

        router.attach("/model", ListModels.class);
        router.attach("/model/{model_id}", modelKerberos);  // The deletion of models is guarded!!!
        router.attach("/model/{model_id}/{info}", ModelInfoResource.class);

        router.attach("/task", TaskResource.class);


        /**
         * Shutdown Resource
         */

        CredentialsVerifier shutDownVerifier = new CredentialsVerifier(this, Priviledges.ADMIN);
        List<Method> shutdownMethods = new ArrayList<Method>();
        shutdownMethods.add(Method.GET);
        shutdownMethods.add(Method.POST);
        List<Method> shutDownFree = new ArrayList<Method>();
        shutDownFree.add(Method.GET);
        UniformGuard shutDownKerberos = new GuardDog().
                createGuard(
                this,
                shutDownVerifier,
                false,
                shutdownMethods,
                shutDownFree,
                ShutDownResource.class);
        router.attach("/shutdown", shutDownKerberos);




        return router;
    }
}
