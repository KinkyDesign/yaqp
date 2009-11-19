package org.opentox.Resources.Algorithms;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.opentox.MediaTypes.OpenToxMediaType;
import org.opentox.Resources.*;

import org.opentox.Resources.Algorithms.AlgorithmReporter.*;
import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.data.Reference;
import org.restlet.data.Status;
import org.restlet.representation.Variant;
import org.restlet.resource.ResourceException;
import weka.core.Instances;

/**
 *
 * @author OpenTox - http://www.opentox.org
 * @author Sopasakis Pantelis
 * @author Sarimveis Harry
 */
public class Algorithm extends AbstractResource {

       private static final long  serialVersionUID = -9058627046190364530L;
    /**
     * The id of the regression algorithm.
     * This can be either mlr or svm.
     */
    private volatile String algorithmId;
    private int i;
    private double d;
    /**
     * The name of the target attribute which normally is the
     * URI of a feature definition.
     */
    private String targetAttribute;
    /**
     * The URI of the dataset which is used to build the regression model.
     */
    private URI datasetURI;
    /**
     * The name of the dataset.
     */
//    private String dataset;
    /**
     * The kernel used in the SVM model.
     * This can be rbf, linear, sigmoid or polynomial.
     */
    private String kernel;
    /**
     * The degree of the polynomial kernel (when used).
     */
    private String degree;
    /**
     * The cahed memory used in model training.
     */
    private String cacheSize;
    /**
     * The Cost coefficient.
     */
    private String cost;
    /**
     * The parameter epsilon used in SVM models.
     */
    private String epsilon;
    /**
     * The kernel parameter gamma used in various kernel functions.
     */
    private String gamma;
    /**
     * The bias of the support vector model.
     */
    private String coeff0;
    /**
     * The tolerance used in model training.
     */
    private String tolerance;
    /**
     * The id of the generated model.
     */
    private int model_id;
    /**
     * An Instances object used to store the data.
     */
    private Instances dataInstances;
    /**
     * The status of the Resource. It is initialized with
     * success/created (201) according to RFC 2616.
     */
    private Status internalStatus = Status.SUCCESS_CREATED;

    /**
     * Initialize the resource. Supported Variants are:
     * <ul>
     * <li>application/rdf+xml</li>
     * <li>text/plain</li>
     * <li>text/xml</li>
     * <li>text/html</li>
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
        variants.add(new Variant(MediaType.TEXT_PLAIN ));
        variants.add(new Variant(MediaType.TEXT_URI_LIST ));
        variants.add(new Variant(MediaType.TEXT_XML));
        variants.add(new Variant(MediaType.TEXT_HTML));
        variants.add(new Variant(OpenToxMediaType.TEXT_YAML));
        variants.add(new Variant(MediaType.APPLICATION_JSON));
        variants.add(new Variant(MediaType.APPLICATION_RDF_TURTLE));  //-- (application/x-turtle)
        getVariants().put(Method.GET, variants);


        /** The algorithm id can be one of {svm, mlr, svc} **/
        this.algorithmId = Reference.decode(getRequest().getAttributes().get("id").toString());

    }

}
