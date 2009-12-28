package org.opentox.resource;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.opentox.resource.*;

import org.opentox.algorithm.reporting.AlgorithmReporter.*;
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
public class TestResource extends AbstractResource {

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
        StringBuilder builder = new StringBuilder();
        Form form = new Form(entity);
        builder.append(form.getNames().toArray()[0] + "\n");
        builder.append(form.getNames().toArray()[1] + "\n");
        

        return new StringRepresentation(builder.toString());
    }

    @Override
    protected Representation get(Variant variant) throws ResourceException {            

        try {
            getResponse().setEntity(new StringRepresentation("1"));
            Thread.sleep(1000);
            getResponse().setEntity(new StringRepresentation("2"));
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(TestResource.class.getName()).log(Level.SEVERE, null, ex);
        }

        
        return getResponseEntity();
    }
}
