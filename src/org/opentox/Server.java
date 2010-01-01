package org.opentox;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.opentox.client.opentoxClient;
import org.opentox.resource.AbstractResource.URIs;
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

    public static String __PORT_ = "3000";
    public static String __DOMAIN_NAME_ = "opentox.ntua.gr";
    public static String __DATABASE_NAME = "modelsDb";
    private static Component component;


    public static void exitWithHelp() {
        System.out.println("\nUsage: \n"
                + "java -jar server.jar [--port 1234] [--serverName localhost] or "
                + "java -jar server.jar [-p 1234] [-s localhost]\n"
                + "Make sure that you use the sun Java version 6!\n\n");
        System.exit(10012);
    }

    /**
     * Usage: Server --port PORT --serverName SERVER_NAME
     * @param args
     * @throws IOException
     * @throws Exception
     */
    public static void main(String[] args) throws IOException, Exception {

        try {
            if (!(args.length == 0)) {
                for (int i = 0; i < args.length; i++) {

                    if ((args[i].equals("--port")) || (args[i].equals("-p"))) {
                        __PORT_ = args[i + 1];
                    }
                    if ((args[i].equals("--serverName")) || (args[i].equals("-s"))) {
                        __DOMAIN_NAME_ = args[i + 1];
                    }
                    if ((args[i].equals("--dataBase")) || (args[i].equals("-d"))) {
                        __DATABASE_NAME = args[i + 1];
                    }

                }
            }
        } catch (ArrayIndexOutOfBoundsException ex) {
            exitWithHelp();
        }


        try {
            if (opentoxClient.isServerAlive(new URI("http://localhost:" + URIs.port), 2)) {
                System.out.println("Port " + URIs.port + " is busy! Seems this or another"
                        + " server has locked and uses this port.\n"
                        + "Try setting up the server on some other port.\n"
                        + "The following ones are available:");
                for (int i = 1; i < 6; i++) {
                    if (!opentoxClient.isServerAlive(new URI("http://localhost:" + (Integer.parseInt(URIs.port) + i)), 2)) {
                        System.out.println("* " + (Integer.parseInt(URIs.port) + i));
                    }
                }
                System.out.println("Could not start OpenToxServer!");
                System.exit(0);
            }

        } catch (URISyntaxException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }

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

    /**
     * Gracefully terminates the server!
     * @throws Exception
     */
    public static void shutdown() throws Exception {
        component.stop();
    }

    public static void restart() throws Exception {
        // ???
    }
}
