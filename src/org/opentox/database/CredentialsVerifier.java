package org.opentox.database;

import org.opentox.Applications.OpenToxApplication;
import org.restlet.security.SecretVerifier;

/**
 * Verifies if some given credentials are valid performing a
 * database query.
 * @author OpenTox - http://www.opentox.org
 * @author Sopasakis Pantelis
 * @author Sarimveis Harry
 */
public class CredentialsVerifier<Privilegdes> extends SecretVerifier{

    /**
     * Initialize the CredentialsVerifier.
     * @param application
     */
    public CredentialsVerifier(OpenToxApplication application){
        super();
    }

    /**
     * Verify if a given pair of username and password are valid performing
     * a database query.
     * @param identifier the username.
     * @param inputSecret Character Array of the password/
     * @return true if the user is authenticated and false if not.
     */
    @Override
    public boolean verify(String identifier, char[] inputSecret) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
