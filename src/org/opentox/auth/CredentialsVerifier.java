package org.opentox.auth;

import org.opentox.OpenToxApplication;
import org.opentox.database.UsersTable;
import org.restlet.security.SecretVerifier;

/**
 * Verifies if some given credentials are valid performing a
 * database query.
 * @author OpenTox - http://www.opentox.org
 * @author Sopasakis Pantelis
 * @author Sarimveis Harry
 */
public class CredentialsVerifier extends SecretVerifier {

    private OpenToxApplication application;
    private Priviledges authorizationLevel;

    /**
     * Initialize the CredentialsVerifier.
     * @param application
     */
    public CredentialsVerifier(OpenToxApplication application) {
        super();
        this.application = application;
    }

    /**
     * Verifies credentials only if their corresponding authorization level is
     * equal or greater to a specified level.
     * @param application
     * @param authorizationLevel
     */
    public CredentialsVerifier(OpenToxApplication application, Priviledges authorizationLevel) {
        super();
        this.application = application;
        this.authorizationLevel = authorizationLevel;
    }

    /**
     * Verify if a given pair of username and password are valid performing
     * a database query. The given password is compared to a digest in the
     * database.
     * @param identifier the username.
     * @param inputSecret Character Array of the password/
     * @return true if the user is authenticated and false if not.
     */
    @Override
    public boolean verify(String identifier, char[] inputSecret) {

        if (authorizationLevel.equals(Priviledges.GUEST)) {
            return true;
        } else {

            if (UsersTable.INSTANCE.getAuthorizationForUser(identifier).compareTo(authorizationLevel) >= 0) {
                String pass = "";
                for (int i = 0; i < inputSecret.length; i++) {
                    pass = pass + inputSecret[i];
                }
                if (UsersTable.INSTANCE.verifyCredentials(identifier, pass)) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }

        }
    }
}
