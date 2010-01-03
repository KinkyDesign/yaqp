package org.opentox.auth;

import java.util.ArrayList;
import java.util.List;
import javax.security.auth.Subject;
import org.opentox.OpenToxApplication;
import org.restlet.Context;
import org.restlet.data.ChallengeScheme;
import org.restlet.data.Method;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.security.Authenticator;
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

    public GuardDog() {
    }

    ;
    private volatile List<Method> unauth = new ArrayList<Method>();

    ;

    /**
     *
     * @param application Main Application
     * @param verifier Instance of org.restlet.security.Verifier used to verify that
     * the client is the one it claims it is.
     * @param optional Indicates if the authentication success is optional.
     * @param guardedMethods The set of all methods that are guarded. Methods excluded
     * from that set are completely forbidden.
     * @param unauthenticatedMethods Methods that can be applied by unauthenticated clients.
     * @param targetClass Subclass of ServerResource.
     * @return The guard dog for the specified resource.
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


        Authenticator authenticator = new ChallengeAuthenticatorImpl(
                application.getContext(), optional, ChallengeScheme.HTTP_BASIC, 
                "Authentication Realm: please provide your credentials!");

        guard.setNext(targetClass);
        guard.setContext(application.getContext());
        guard.setAuthenticator(authenticator);
        guard.getAuthenticator().setVerifier(verifier);
        guard.getAuthenticator().setEnroler(enroler);
        guard.setAuthorizer(authorizer);
        return guard;
    }

    /**
     * An Implementation of ChallengeAuthenticator
     */
    private class ChallengeAuthenticatorImpl extends ChallengeAuthenticator {

        public ChallengeAuthenticatorImpl(Context context, boolean optional, ChallengeScheme challengeScheme, String realm) {
            super(context, optional, challengeScheme, realm);
        }

        @Override
        protected boolean authenticate(Request request, Response response) {
            if (unauth.contains(request.getMethod())) {
                return true;
            } else {
                return super.authenticate(request, response);
            }
        }
    }
}



