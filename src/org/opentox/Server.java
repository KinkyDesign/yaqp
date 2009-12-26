package org.opentox;

import java.io.IOException;
import org.opentox.OpenToxApplication;
import org.opentox.resource.AbstractResource;
import org.restlet.Application;
import org.restlet.Component;
import org.restlet.data.Protocol;
import org.restlet.service.LogService;

/**
 * The Server that runs the services.
 * @author OpenTox - http://www.opentox.org/
 * @author Sopasakis Pantelis
 * @author Sarimveis Harry
 * @version 1.3.3 (Last update: Dec 20, 2009)
 */
public class Server {

    public static void main(String[] args) throws IOException {

        // Create a component
        Component component = new Component();

        // Configure the logging service:
        LogService myLog = new LogService(true);
        myLog.setLoggerName("org.opentox");
        component.setLogService(myLog);

        component.getServers().add(Protocol.HTTP, Integer.parseInt(AbstractResource.port));


        Application application = new OpenToxApplication();
        application.setContext(component.getContext().createChildContext());
        component.getDefaultHost().attach("", application);
        
        /** NOTE:
         * The web interface from now on will run on an apache server
         * on port 80, and the web services will run as a standalone
         * application on 3000, based on Restlet 2.0 milestone 3.
         */

        try {
            component.start();
        } catch (Exception ex) {
            org.opentox.OpenToxApplication.opentoxLogger.severe("Exception while " +
                    "starting the component : "+ex.getMessage());
        }

    }
}
