package org.opentox.resource;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import org.opentox.algorithm.AlgorithmEnum;
import org.opentox.interfaces.IProvidesHttpAccess;
import org.opentox.media.OpenToxMediaType;
import org.opentox.ontology.namespaces.OTClass;
import org.opentox.ontology.namespaces.OTProperties;
import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.data.ReferenceList;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.representation.Variant;
import org.restlet.resource.ResourceException;

/**
 * List of all algorithms.
 * <p>
 * URI: /algorithm/<br/>
 * Allowed Methods: GET<br/>
 * Status Codes: 200, 503<br/>
 * Supported Mediatypes: text/uri-list, text/html, application/rdf+xml,
 * application/x-turtle, text/x-triple
 * </p>
 * @author OpenTox - http://www.opentox.org/
 * @author Sopasakis Pantelis
 * @author Sarimveis Harry
 * @version 1.3.3 (Last update: Dec 20, 2009)
 */
public class ListAlgorithms extends OTResource
        implements IProvidesHttpAccess {

    private static final long serialVersionUID = 75712481009764L;

    /**
     * Initialize the resource.
     * @throws ResourceException
     */
    @Override
    public void doInit() throws ResourceException {
        super.doInit();
        Collection<Variant> variants = new ArrayList<Variant>();
        variants.add(new Variant(MediaType.TEXT_URI_LIST));
        variants.add(new Variant(MediaType.TEXT_HTML));
        variants.add(new Variant(MediaType.APPLICATION_RDF_XML));
        variants.add(new Variant(MediaType.APPLICATION_RDF_TURTLE));
        variants.add(new Variant(OpenToxMediaType.TEXT_TRIPLE));
        variants.add(new Variant(OpenToxMediaType.TEXT_N3));
        getVariants().put(Method.GET, variants);
    }

    @Override
    public Representation get(Variant variant) {


        MediaType mediatype = variant.getMediaType();


        Representation rep = null;
        Iterator<String> algorithmIterator = AlgorithmEnum.getIterator();

        if (mediatype.equals(MediaType.TEXT_URI_LIST)) {
            ReferenceList list = new ReferenceList();
            while (algorithmIterator.hasNext()) {
                list.add(URIs.algorithmURI + "/" + algorithmIterator.next());
            }
            rep = list.getTextRepresentation();
            rep.setMediaType(MediaType.TEXT_URI_LIST);
        } else if (mediatype.equals(MediaType.TEXT_HTML)) {
            ReferenceList list = new ReferenceList();
            while (algorithmIterator.hasNext()) {
                list.add(URIs.algorithmURI + "/" + algorithmIterator.next());
            }
            rep = list.getWebRepresentation();
            rep.setMediaType(MediaType.TEXT_HTML);
        } else if ((mediatype.equals(MediaType.APPLICATION_RDF_XML))
                || (mediatype.equals(MediaType.APPLICATION_RDF_TURTLE))
                || (mediatype.equals(OpenToxMediaType.TEXT_TRIPLE))
                || (mediatype.equals(OpenToxMediaType.TEXT_N3))) {

            ByteArrayOutputStream outStream = null;
            OntModel jenaModel = null;

            jenaModel = OTProperties.createModel();


            OTClass.Algorithm.createOntClass(jenaModel);

            Individual algorithm;
            while (algorithmIterator.hasNext()) {
                algorithm = jenaModel.createIndividual(URIs.algorithmURI + "/" + algorithmIterator.next(),
                        jenaModel.createOntResource(OTClass.Algorithm.getURI()));

            }

            outStream = new ByteArrayOutputStream();
            String Lang = "RDF/XML";
            if (MediaType.APPLICATION_RDF_TURTLE.equals(mediatype)) {
                Lang = "TTL";
            } else if (OpenToxMediaType.TEXT_TRIPLE.equals(mediatype)) {
                Lang = "N-TRIPLE";
            } else if (OpenToxMediaType.TEXT_N3.equals(mediatype)) {
                Lang = "N3";
            }
            jenaModel.write(outStream, Lang);
            rep = new StringRepresentation(outStream.toString());
        }

        getResponse().setStatus(Status.SUCCESS_OK);
        return rep;


    }
}
