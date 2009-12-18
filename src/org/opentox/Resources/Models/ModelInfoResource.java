package org.opentox.Resources.Models;

import com.hp.hpl.jena.ontology.OntModel;
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
import org.opentox.Resources.AbstractResource;
import org.opentox.ontology.Namespace;
import org.opentox.ontology.OT;
import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.data.Reference;
import org.restlet.data.ReferenceList;
import org.restlet.representation.Representation;
import org.restlet.representation.Variant;
import org.restlet.resource.ResourceException;

/**
 *
 * @author chung
 */
public class ModelInfoResource extends AbstractResource{

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
     *
     * @param variant
     * @return StringRepresentation
     */
    @Override
    public Representation get(Variant variant) {
        Representation rep = null;
        try {
            FileInputStream in = new FileInputStream(Directories.modelRdfDir + "/" + model_id);
            OntModel jenaModel = Namespace.createModel();
            jenaModel.read(in,null);
            StmtIterator stmtIt = null;
            ReferenceList uri_list = new ReferenceList();
            if (info.equalsIgnoreCase("dependent")){
                stmtIt = jenaModel.listStatements(new SimpleSelector(null, OT.dependentVariables, (Resource) null));
            }else if (info.equalsIgnoreCase("independent")){
                stmtIt = jenaModel.listStatements(new SimpleSelector(null, OT.independentVariables, (Resource) null));
            }
            while (stmtIt.hasNext()){
                uri_list.add(new Reference(new URI(stmtIt.next().getObject().as(Resource.class).getURI())));
            }

            if (MediaType.TEXT_URI_LIST.equals(variant.getMediaType())){
                rep = uri_list.getTextRepresentation();
            }else if (MediaType.TEXT_HTML.equals(variant.getMediaType())){
                rep = uri_list.getWebRepresentation();
            }            
        } catch (Exception ex) {
            Logger.getLogger(ModelInfoResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rep;
    }


}
