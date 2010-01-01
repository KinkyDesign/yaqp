package org.opentox.auth;

import java.util.ArrayList;
import java.util.List;
import javax.security.auth.Subject;
import org.opentox.OpenToxApplication;
import org.restlet.Restlet;
import org.restlet.Server;
import org.restlet.data.ChallengeScheme;
import org.restlet.data.Method;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.resource.ServerResource;
import org.restlet.security.ChallengeAuthenticator;
import org.restlet.security.ChallengeGuard;
import org.restlet.security.Enroler;
import org.restlet.security.MethodAuthorizer;
import org.restlet.security.UniformGuard;
import org.restlet.security.Verifier;

/**
 *
 * @author Sopasakis Pantelis
 */
public class GuardDog {
    
    public GuardDog(){
        
    };

    private volatile List<Method> unauth = new ArrayList<Method>();;

    /**
     *
     * @param verifier
     * @param optional
     * @param guardedMethods
     * @param freeMethods
     * @return
     */
    public synchronized UniformGuard createGuard(
            OpenToxApplication application,
            Verifier verifier,
            boolean optional,
            List<Method> guardedMethods,
            List<Method> unauthenticatedMethods,
            Class<?> targetClass) {

        unauth = unauthenticatedMethods;


        Enroler enroler = new Enroler() {

            @Override
            public void enrole(Subject subject) {
            }

        };

        MethodAuthorizer authorizer = new MethodAuthorizer("authorizer") {

            @Override
            public boolean authorize(Request request, Response response) {
                return super.authorize(request, response);
            }
        };

        authorizer.getAuthenticatedMethods().addAll(guardedMethods);


        // Create a Guard
        ChallengeGuard guard = new ChallengeGuard(application.getContext(),
                ChallengeScheme.HTTP_BASIC, "realm");

        ChallengeAuthenticator authenticator = new ChallengeAuthenticator(
                application.getContext(), optional, ChallengeScheme.HTTP_BASIC, "realm") {


            @Override
            protected boolean authenticate(Request request, Response response) {
                /** Allow everyone to GET but only Admins to apply DELETE!**/
                if (unauth.contains(request.getMethod())
                        ) {
                    return true;
                } else {
                    return super.authenticate(request, response);
                }
            }
        };

        guard.setNext(targetClass);
        guard.setContext(application.getContext());
        guard.setAuthenticator(authenticator);
        guard.getAuthenticator().setVerifier(verifier);
        guard.getAuthenticator().setEnroler(enroler);
        guard.setAuthorizer(authorizer);
        return guard;
    }

}



