package org.opentox;

import java.io.File;
import java.io.IOException;
import java.util.Date;
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
import org.opentox.resource.TestResource;
import org.opentox.auth.CredentialsVerifier;
import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.data.ChallengeScheme;
import org.restlet.data.Request;
import org.opentox.database.InHouseDB;
import org.opentox.auth.Priviledges;
import org.restlet.data.Method;
import org.restlet.data.Response;
import org.restlet.routing.Router;
import org.restlet.security.ChallengeAuthenticator;
import org.restlet.security.ChallengeGuard;
import org.restlet.security.Enroler;
import org.restlet.security.MethodAuthorizer;
import org.restlet.security.UniformGuard;
import org.restlet.security.Verifier;

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

    /**
     * Constructor.
     */
    public OpenToxApplication() throws IOException {
        if (!(new File(AbstractResource.Directories.logDir)).exists()) {
            new File(AbstractResource.Directories.logDir).mkdirs();
        }
        opentoxLogger = Logger.getLogger("org.restlet");
        FileHandler fileHand = new FileHandler(AbstractResource.Directories.logDir + "/" + new Date());
        fileHand.setFormatter(new SimpleFormatter());
        opentoxLogger.addHandler(fileHand);
        opentoxLogger.setLevel(Level.INFO);
        dbcon = new InHouseDB();
        AbstractResource.Directories.checkDirs();        
    }



    protected UniformGuard createGuard(Verifier verifier, boolean optional) {


        Enroler enroler = new Enroler() {
            @Override
            public void enrole(Subject subject) {
                //System.out.println(subject);
            }
        };

        /*
         * Simple authorizer: Not completed yet...
         */
        MethodAuthorizer authorizer = new MethodAuthorizer("authorizer"){
            @Override
            public boolean authorize(Request request, Response response) {                                
                    return super.authorize(request, response);                
            }
        };

        authorizer.getAuthenticatedMethods().add(Method.DELETE);
        authorizer.getAuthenticatedMethods().add(Method.GET);
        authorizer.getAuthenticatedMethods().add(Method.POST);        

                
        // Create a Guard
        ChallengeGuard guard = new ChallengeGuard(getContext(),
                ChallengeScheme.HTTP_BASIC, "realm");

        ChallengeAuthenticator authenticator = new ChallengeAuthenticator(
                getContext(), optional, ChallengeScheme.HTTP_BASIC, "realm") {

            @Override
            protected boolean authenticate(Request request, Response response) {
                /** Allow everyone to GET but only Admins to apply DELETE!**/
                if (
                        (Method.GET.equals(request.getMethod()))
                        || (Method.POST.equals(request.getMethod()))
                ){
                    System.out.println("PASS without password!");
                    return true;
                }else{
                    return super.authenticate(request, response);
                }
            }
        };
         guard.setContext(getContext());
         guard.setAuthenticator(authenticator);
         guard.getAuthenticator().setVerifier(verifier);
         guard.getAuthenticator().setEnroler(enroler);
        guard.setAuthorizer(authorizer);
        return guard;
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
        CredentialsVerifier verifier = new CredentialsVerifier(
                this, Priviledges.USER);
        UniformGuard modelKerberos = createGuard(verifier, false);
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

        router.attach("/test", TestResource.class);


        return router;
    }
}
