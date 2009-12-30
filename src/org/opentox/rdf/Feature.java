package org.opentox.rdf;

import java.io.IOException;
import org.opentox.namespaces.OT;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.vocabulary.DC;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.URI;
import org.opentox.resource.AbstractResource;
import org.restlet.Client;
import org.restlet.data.MediaType;
import org.restlet.data.Protocol;
import org.restlet.data.Response;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.ResourceException;

/**
 * 
 * @author OpenTox - http://www.opentox.org/
 * @author Sopasakis Pantelis
 * @author Sarimveis Harry
 * @version 1.3.3 (Last update: Dec 23, 2009)
 */
public class Feature extends RDFHandler  implements Serializable {

    public Feature() {
        super();
    }

    /**
     * Creates a new Feature and writes it to an OutputStream.
     * @param sameAs Declares a same-as relationship between this feature and some
     * other feature.
     * @param output Outputstream used to write the output.
     */
    public void createNewFeature(String sameAs, OutputStream output) {
        OntModel featureModel = OT.createModel();
        Individual feature = featureModel.createIndividual(featureModel.getOntClass(OT.Class.Dataset.getURI()));
        feature.addRDFType(OT.Class.Feature.createProperty(featureModel));
        feature.addProperty(featureModel.createAnnotationProperty(DC.creator.getURI()),
                featureModel.createTypedLiteral(AbstractResource.URIs.baseURI));
        feature.setSameAs(featureModel.createResource(sameAs, OT.Class.Feature.getResource()));
        OT.Class.Feature.createOntClass(featureModel);
        featureModel.write(output);
    }


    /**
     * Generates a new Feature and POSTs it to a feature service
     * @param sameAs Declares a same-as relationship between this feature and some
     * other feature.
     * @param featureService Some feature service where the generated feature should be stored.
     * @return The response of the feature service to the request for feature creation.
     */
    public Response createNewFeature(String sameAs, URI featureService) throws ResourceException, IOException{
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Feature feature = new Feature();
        feature.createNewFeature(sameAs, out);
        Representation featureToPost = new StringRepresentation(out.toString());
        featureToPost.setMediaType(MediaType.APPLICATION_RDF_XML);
        Client cli = new Client(Protocol.HTTP);
        int n_RETRY = 5, i = 0;
        boolean success = false;
        Response response = new Response(null);

        while (!success && i < n_RETRY){
             response = cli.post(featureService.toString(), featureToPost);
             success = (response.getStatus().equals(Status.SUCCESS_OK));
             i++;
        }
        
        return response;
    }

    


}
