package org.opentox.server;

import java.io.IOException;
import org.opentox.Applications.OpenToxApplication;
import org.opentox.Resources.AbstractResource;
import org.restlet.Application;
import org.restlet.Component;
import org.restlet.data.Protocol;

/**
 *
 * @author chung
 */
public class Server {

    public static void main(String[] args) throws IOException {

        // Create a component
        Component component = new Component();

        component.getServers().add(Protocol.HTTP, Integer.parseInt(AbstractResource.port));


        Application application = new OpenToxApplication();
        application.setContext(component.getContext().createChildContext());
        component.getDefaultHost().attach("", application);
        
        /** NOTE:
         * The web interface from now on will run on an apache server
         * on port 80, and the web services will run as a standalone
         * application on 3000, based on Restlet 2.0 milestone 3
         */

        try {
            component.start();
        } catch (Exception ex) {
            org.opentox.Applications.OpenToxApplication.opentoxLogger.severe("Exception while " +
                    "starting the component : "+ex.getMessage());
        }

    }
}
