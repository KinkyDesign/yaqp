package org.opentox.error;

import org.opentox.interfaces.IProne2Error;



/**
 * ErrorSource is an interface implemented by all those classes that may throw an
 * error that affects a service. Every class which contains methods which are invoked
 * by some child of {@link org.opentox.resource.OTResource } should implement this interface.
 * @author OpenTox - http://www.opentox.org/
 * @author Sopasakis Pantelis
 * @author Sarimveis Harry
 * @version 1.3.3 (Last update: Dec 20, 2009)
 */
public abstract class ErrorSource implements IProne2Error {

    public ErrorRepresentation errorRep = new ErrorRepresentation();

    public ErrorRepresentation getErrorRep() {
        return errorRep;
    }




}
