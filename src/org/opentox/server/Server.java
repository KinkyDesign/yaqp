package org.opentox.server;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.opentox.Applications.OpenToxApplication;
import org.opentox.Resources.AbstractResource;
import org.restlet.data.*;
import org.restlet.*;

/**
 *
 * @author chung
 */
public class Server {

    public static void main(String[] args){
        // Create a component
           Component component = new Component();

           component.getServers().add(Protocol.HTTP, 3000);

           component.getClients().add(Protocol.FILE);


           LocalReference javadoc =
                    LocalReference.createFileReference(
                  AbstractResource.javadocDir);

           LocalReference home =
                   LocalReference.createFileReference(
                  AbstractResource.HTMLDir);


           Directory javadocDirectory = new Directory(component.getContext().createChildContext(), javadoc);
           Directory homeDirectory = new Directory(component.getContext().createChildContext(), home);


           Application application = new OpenToxApplication();

           VirtualHost host = new VirtualHost();

           host.attach("/OpenToxServices",application);
           host.attach("",homeDirectory);
           host.attach("/OpenToxServices/javadoc", javadocDirectory);



           component.setDefaultHost(host);
        try {
            component.start();
        } catch (Exception ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
