package org.opentox.resource;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.SimpleSelector;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import java.io.FileInputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.opentox.resource.AbstractResource;
import org.opentox.error.ErrorSource;
import org.opentox.namespaces.Namespace;
import org.opentox.namespaces.OT;
import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.data.Reference;
import org.restlet.data.ReferenceList;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.representation.Variant;
import org.restlet.resource.ResourceException;

/**
 * This resource return meta information about a model such as its dependent variables
 * , its independent and predicted variables.
 * @author OpenTox - http://www.opentox.org/
 * @author Sopasakis Pantelis
 * @author Sarimveis Harry
 * @version 1.3.3 (Last update: Dec 20, 2009)
 */
public class ModelInfoResource extends AbstractResource {


    private Status internalStatus = Status.SUCCESS_ACCEPTED;

    private String model_id, info;

    /**
     * Default Class Constructor.Available MediaTypes of Variants: TEXT_XML
     * @param context
     * @param request
     * @param response
     */
    @Override
    public void doInit() throws ResourceException {
        super.doInit();
        Collection<Method> allowedMethods = new ArrayList<Method>();
        allowedMethods.add(Method.GET);
        getAllowedMethods().addAll(allowedMethods);
        super.doInit();
        List<Variant> variants = new ArrayList<Variant>();
        variants.add(new Variant(MediaType.TEXT_URI_LIST));
        variants.add(new Variant(MediaType.TEXT_HTML));
        getVariants().put(Method.GET, variants);
        model_id = Reference.decode(getRequest().getAttributes().get("model_id").toString());
        info = Reference.decode(getRequest().getAttributes().get("info").toString());
    }

    /**
     * Returns a text/uri list which corresponds to the requested meta-information.
     * @param variant
     * @return StringRepresentation
     */
    @Override
    public Representation get(Variant variant) {
        Representation rep = null;
        try {
            FileInputStream in = new FileInputStream(Directories.modelRdfDir + "/" + model_id);
            OntModel jenaModel = Namespace.createModel();
            jenaModel.read(in, null);
            StmtIterator stmtIt = null;
            ReferenceList uri_list = new ReferenceList();
            Property prop = null;
            if (info.equalsIgnoreCase("dependent")) {
                prop = OT.dependentVariables;
            } else if (info.equalsIgnoreCase("independent")) {
                prop = OT.independentVariables;
            } else if (info.equalsIgnoreCase("predicted")) {
                prop = OT.predictedVariables;
            }
            stmtIt = jenaModel.listStatements(new SimpleSelector(null, prop, (Resource) null));
            while (stmtIt.hasNext()) {
                uri_list.add(new Reference(new URI(stmtIt.next().getObject().as(Resource.class).getURI())));
            }

            if (MediaType.TEXT_URI_LIST.equals(variant.getMediaType())) {
                rep = uri_list.getTextRepresentation();
            } else if (MediaType.TEXT_HTML.equals(variant.getMediaType())) {
                rep = uri_list.getWebRepresentation();
            }
        } catch (Exception ex) {
            Logger.getLogger(ModelInfoResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rep;
    }


}
