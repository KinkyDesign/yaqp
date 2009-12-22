package org.opentox.Resources;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.IOUtils;
import org.opentox.MediaTypes.OpenToxMediaType;
import org.opentox.Resources.*;

import org.opentox.Resources.Algorithms.AlgorithmReporter.*;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.data.Reference;
import org.restlet.data.ReferenceList;
import org.restlet.data.Status;
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
}
