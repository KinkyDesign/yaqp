package org.opentox;

import org.opentox.interfaces.IServer;
import org.opentox.database.InHouseDB;
import org.opentox.resource.OTResource.URIs;
import org.restlet.Application;
import org.restlet.Component;
import org.restlet.data.Protocol;
import org.restlet.service.LogService;

/**
 * @author OpenTox - http://www.opentox.org/
 * @author Sopasakis Pantelis
 * @author Sarimveis Harry
 * @version 1.3.3 (Last update: Dec 20, 2009)
 */
public class Server implements IServer {

    public static String __PORT_ = "3000";
    public static String __DOMAIN_NAME_ = "opentox.ntua.gr";
    public static String __DATABASE_NAME_ = "modelsDb";
    private static Server instanceOfThis = null;
    public static Server INSTANCE = getInstace();

    private static Server getInstace() {
        if (instanceOfThis == null) {
            instanceOfThis = new Server();
        }
        return instanceOfThis;
    }
    private static Component component;
    

    /**
     * Private Constructor.
     */
    private Server() {
        
    }

    

    /**
     * Gracefully terminates the server!
     * @throws Exception
     */
    public void shutdown() throws Exception {
        OpenToxApplication.opentoxLogger.info("Closing Database Connection...");
        InHouseDB.INSTANCE.close();
        OpenToxApplication.opentoxLogger.info("Server is shutting down Gracefully");
        component.stop();
    }

    public void run() {

        
        // Create a component
        component = new Component();

        // Configure the logging service:
        LogService myLog = new LogService(true);
        myLog.setLoggerName("org.opentox");
        component.setLogService(myLog);

        component.getServers().add(Protocol.HTTP, Integer.parseInt(URIs.port));


        Application application = OpenToxApplication.INSTANCE;
        application.setContext(component.getContext().createChildContext());
        component.getDefaultHost().attach("", application);
        OpenToxApplication.opentoxLogger.info("Server started and listening on port: " + URIs.port);

        /** NOTE:
         * The web interface from now on will run on an apache server
         * on port 80, and the web services will run as a standalone
         * application on 3000, based on Restlet 2.0 milestone 3.
         */
        try {
            component.start();
        } catch (Exception ex) {
            org.opentox.OpenToxApplication.opentoxLogger.severe("Exception while "
                    + "starting the component : " + ex.getMessage());
        }
    }

    
}
