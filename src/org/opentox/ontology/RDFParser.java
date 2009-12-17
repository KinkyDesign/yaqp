package org.opentox.ontology;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntResource;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.restlet.data.Status;

/**
 *
 * @author Sopasakis Pantelis
 */
public abstract class RDFParser {

    private static final long serialVersionUID = 6602541954910338287L;


    /**
     * The Jena Ontological Model used to read/write/modify
     * an RDF document.
     */
    public OntModel jenaModel;

    public Status internalStatus = Status.SUCCESS_OK;

    /**
     * Void constructor - to be handled by subclasses.
     */
    public RDFParser(){

    }

    /**
     * Initialized the RDFParser with an InputStream that will be used
     * for reading the data.
     * @param in
     */
    public RDFParser(InputStream in) {
        try {
            jenaModel = OT.createModel();
            jenaModel.read(in, null);
        } catch (Exception ex) {
            Logger.getLogger(Dataset.class.getName()).log(Level.SEVERE, null, ex);
            internalStatus = Status.SERVER_ERROR_INTERNAL;
        }
    }

    /**
     * Creates an ExtendedIterator over the set of all resources which correspond
     * to a specific class. Exampe of use:<br/><br/>
     * <pre> Dataset data = new Dataset(new FileInputStream("/path/to/some/file.rdf"));
     * ExtendedIterator&lt;? extends OntResource&gt; it = data.getIteratorFor(OT.Class.Feature);
     * while (it.hasNext()){
     *   System.out.println(it.next());
     * }
     * </pre>
     * @param someClass
     * @return
     */
    public ExtendedIterator<? extends OntResource> getClassMemberIteratorFor(
            Namespace.Class someClass) {
        OntClass myClass = someClass.getOntClass(jenaModel);
        return myClass.listInstances();
    }

    /**
     * Returns the jena Model.
     * @return
     */
    public OntModel getModel() {
        return jenaModel;
    }



}


