package org.opentox;

import org.opentox.interfaces.IServer;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.opentox.client.opentoxClient;
import org.opentox.interfaces.IClient;
import org.opentox.resource.OTResource.URIs;

/**
 *
 * @author chung
 */
public class Main {

    private static void exitWithHelp() {
        System.out.println("\nUsage: \n"
                + "java -jar server.jar [--port 1234] [--serverName localhost] "
                + "[--dataBase myDataBase] or \n"
                + "java -jar server.jar [-p 1234] [-s localhost] [-d myDataBase]\n"
                + "Make sure that you use the sun Java version 6!\n\n");
        System.exit(10012);
    }

    /**
     * Usage: Server --port PORT --serverName SERVER_NAME --dataBase DATABASE_NAME
     * @param args
     * @throws IOException
     * @throws Exception
     */
    public static void main(String[] args) throws IOException, Exception {

        IServer server = Server.INSTANCE;

        try {
            if (!(args.length == 0)) {
                for (int i = 0; i < args.length; i++) {

                    if ((args[i].equals("--port")) || (args[i].equals("-p"))) {
                        Server.__PORT_ = args[i + 1];
                    }
                    if ((args[i].equals("--serverName")) || (args[i].equals("-s"))) {
                        Server.__DOMAIN_NAME_ = args[i + 1];
                    }
                    if ((args[i].equals("--dataBase")) || (args[i].equals("-d"))) {
                        Server.__DATABASE_NAME_ = args[i + 1];
                    }

                }
            }
        } catch (ArrayIndexOutOfBoundsException ex) {
            exitWithHelp();
        }



        try {
            IClient client = opentoxClient.INSTANCE;
            if (client.isServerAlive(new URI("http://localhost:" + Server.__PORT_), 2)) {
                System.out.println("Port " + Server.__PORT_ + " is busy! Seems this or another"
                        + " server has locked and uses this port.\n"
                        + "Try setting up the server on some other port.\n"
                        + "The following ones are available:");
                for (int i = 1; i < 6; i++) {
                    if (!opentoxClient.INSTANCE.isServerAlive(new URI("http://localhost:" + (Integer.parseInt(URIs.port) + i)), 2)) {
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


        server.run();

    }
}
