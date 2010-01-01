package org.opentox.resource;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.opentox.Server;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.representation.Variant;
import org.restlet.resource.ResourceException;

/**
 *
 * @author chung
 */
public class ShutDownResource extends AbstractResource {

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

        Form form = new Form(entity);
        String doShutDown = form.getFirstValue("shutdown");
        String verifyShutDown = form.getFirstValue("verify");
        Representation rep = new StringRepresentation("Will not shut down!\n");

        if ((doShutDown.equals("tRue")) && (verifyShutDown.equals("iMeanIt!"))) {

            Thread stoppingServer = new Thread() {

                @Override
                public void run() {
                    try {
                        Thread.sleep(20000);
                        Server.shutdown();
                    } catch (Exception ex) {
                        System.exit(0);
                    }
                }
            };

            rep = new StringRepresentation("\n--\nSending graceful shutdown request to the server....\n"
                    + "Server will be down in about 20secs\n"
                    + "Goodbye!!!\n\n");
            stoppingServer.start();
        }

        return rep;

    }

    @Override
    protected Representation get(Variant variant) throws ResourceException {
        return new StringRepresentation("ShutDown service...\n");
    }
}
