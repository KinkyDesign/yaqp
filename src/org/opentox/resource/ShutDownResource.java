package org.opentox.resource;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.opentox.OpenToxApplication;
import org.opentox.Server;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.representation.Variant;
import org.restlet.resource.ResourceException;

/**
 *
 * @author chung
 */
public class ShutDownResource extends AbstractResource {

    private static boolean sentShutDownRequest = false;
    private static long timeOfShutDownRequest = 0;

    @Override
    public void doInit() throws ResourceException {
        super.doInit();
        Collection<Method> allowedMethods = new ArrayList<Method>();
        allowedMethods.add(Method.GET);
        allowedMethods.add(Method.POST);
        getAllowedMethods().addAll(allowedMethods);

        List<Variant> variants = new ArrayList<Variant>();
        variants.add(new Variant(MediaType.TEXT_PLAIN));
        variants.add(new Variant(MediaType.TEXT_HTML));
        getVariants().put(Method.GET, variants);

    }

    @Override
    protected Representation post(Representation entity) throws ResourceException {

        Representation rep = new StringRepresentation("Will not shut down!\n");
        Form form = new Form(entity);

        String doShutDown = "";
        String verifyShutDown = "";

        verifyShutDown = form.getFirstValue("verify");
        doShutDown = form.getFirstValue("shutdown");

        if ((doShutDown == null) || (verifyShutDown == null)) {
            getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
            return new StringRepresentation("Server will not shut down!\n");
        }

        if (sentShutDownRequest) {
            long timeLeft = 10500L-(System.currentTimeMillis()-timeOfShutDownRequest);
            return new StringRepresentation("\n--\nShutDown request has already been sent! Time left: "
                    + timeLeft +"ms\n\n");
        } else {
            if ((doShutDown.equals("tRue")) && (verifyShutDown.equals("iMeanIt!"))) {

                sentShutDownRequest = true;
                timeOfShutDownRequest = System.currentTimeMillis();
                Thread stoppingServer = new Thread("StoppingServer") {

                    @Override
                    public void run() {
                        try {
                            Thread.sleep(10000);
                            Server.shutdown();
                        } catch (Exception ex) {
                            System.exit(0);
                        }
                    }
                };

                rep = new StringRepresentation("\n--\nSending graceful shutdown request to the server....\n"
                        + "Server will be down in 10secs\n"
                        + "Goodbye!!!\n\n");
                stoppingServer.start();
            } else {
                getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
                rep = new StringRepresentation("The server will not shutdown!!!\n");
            }


        }

        return rep;

    }

    @Override
    protected Representation get(Variant variant) throws ResourceException {
        return new StringRepresentation("ShutDown service...\n" +
                "[ Only Administrators can shutdown the server! ]\n");
    }
}
