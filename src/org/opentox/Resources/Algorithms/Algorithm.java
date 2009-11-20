package org.opentox.Resources.Algorithms;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
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
 * @author OpenTox - http://www.opentox.org
 * @author Sopasakis Pantelis
 * @author Sarimveis Harry
 */
public class Algorithm extends AbstractResource {

    private static final long serialVersionUID = -9058627046190364530L;


    /**
     * The id of the regression algorithm.
     * This can be either mlr or svm.
     */
    private volatile String algorithmId;


    /**
     * Initialize the resource. Supported Variants are:
     * <ul>
     * <li>application/rdf+xml (default)</li>
     * <li>text/uri-list</li>
     * <li>text/xml</li>
     * <li>text/x-yaml</li>
     * <li>application/json</li>
     * <li>application/x-turtle</li>
     * </ul>
     * Allowed Methods are:
     * <ul>
     * <li>GET</li>
     * <li>POST</li>
     * </ul>
     * URI:<br/>
     * http://opentox.ntua.gr:3000/algorithm/id
     * @throws ResourceException
     */
    @Override
    public void doInit() throws ResourceException {
        super.doInit();
        Collection<Method> allowedMethods = new ArrayList<Method>();
        allowedMethods.add(Method.GET);
        allowedMethods.add(Method.POST);
        getAllowedMethods().addAll(allowedMethods);

        List<Variant> variants = new ArrayList<Variant>();
        /** default variant : **/
        variants.add(new Variant(MediaType.APPLICATION_RDF_XML));  //-- (application/rdf+xml)
        /** other supported variants: **/       
        variants.add(new Variant(MediaType.TEXT_URI_LIST));
        variants.add(new Variant(MediaType.TEXT_XML));
        variants.add(new Variant(OpenToxMediaType.TEXT_YAML));
        variants.add(new Variant(MediaType.APPLICATION_JSON));
        variants.add(new Variant(MediaType.APPLICATION_RDF_TURTLE));  //-- (application/x-turtle)
        getVariants().put(Method.GET, variants);

        /** The algorithm id can be one of {svm, mlr, svc} **/
        this.algorithmId = Reference.decode(getRequest().getAttributes().get("id").toString());
    }

    /**
     * Implementation of the GET method.
     * Returns XML representations for the supported regression algorithms
     * @param variant
     * @return XML representation of algorithm
     */
    @Override
    protected Representation get(Variant variant) {

        long algorithm = 0;
        Representation representation = null;


        if (variant.getMediaType().equals(MediaType.TEXT_URI_LIST)) {
            ReferenceList list = new ReferenceList();
            list.add(getReference());
            representation = list.getTextRepresentation();
        } else {
            if (algorithmId.equalsIgnoreCase("svm")) {
                algorithm = AlgorithmReporter.SVM;
                representation = new AlgorithmReporter().FormatedRepresntation(variant.getMediaType(), algorithm);
            } else if (algorithmId.equalsIgnoreCase("mlr")) {
                algorithm = AlgorithmReporter.MLR;
                representation = new AlgorithmReporter().FormatedRepresntation(variant.getMediaType(), algorithm);
            } else if (algorithmId.equalsIgnoreCase("svc")) {
                algorithm = AlgorithmReporter.SVC;
                representation = new AlgorithmReporter().FormatedRepresntation(variant.getMediaType(), algorithm);
            } else {
                getResponse().setStatus(Status.CLIENT_ERROR_NOT_FOUND);
                representation = new StringRepresentation("Algorithm Not Found!\n", MediaType.TEXT_PLAIN);
            }

        }

        return representation;
    }


    @Override
    protected Representation post(Representation entity) throws ResourceException {
        Form form = new Form(entity);
        if (algorithmId.equalsIgnoreCase("mlr")) {
        } else if (algorithmId.equalsIgnoreCase("svm")) {
        } else if (algorithmId.equalsIgnoreCase("svc")) {
        }
        return super.post(entity);
    }
}