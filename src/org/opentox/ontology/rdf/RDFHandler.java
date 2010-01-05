package org.opentox.ontology.rdf;

import org.opentox.namespaces.OT;
import org.opentox.namespaces.Namespace;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntResource;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.opentox.error.ErrorSource;
import org.restlet.data.Status;

/**
 * A Handler for RDF resources. Classes that extend RDFHandler have methods
 * that are used to parse and generate RDF documents.
 * @see Feature
 * @see Dataset
 * @see Model
 * @author OpenTox - http://www.opentox.org/
 * @author Sopasakis Pantelis
 * @author Sarimveis Harry
 * @version 1.3.3 (Last update: Dec 23, 2009)
 */
public abstract class RDFHandler extends ErrorSource {

    private static final long serialVersionUID = 6602541954910338287L;
    
    /**
     * The Jena Ontological Model used to read/write/modify
     * an RDF document.
     */
    public OntModel jenaModel;
    

    /**
     * Void constructor - to be handled by subclasses.
     */
    public RDFHandler() {
    }

    /**
     * Initialized the RDFParser with an InputStream that will be used
     * for reading the data.
     * @param in
     */
    public RDFHandler(InputStream in) {
        try {
            jenaModel = OT.createModel();
            jenaModel.read(in, null);
        } catch (Exception ex) {
            Logger.getLogger(Dataset.class.getName()).log(Level.SEVERE, null, ex);
            errorRep.append(ex, "Severe Error while reading from input stream!", Status.SERVER_ERROR_INTERNAL);
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
     * @return An iterator for some class members specified in the arguments of
     * this method.
     */
    public ExtendedIterator<? extends OntResource> getClassMemberIteratorFor(
            Namespace.Class someClass) {
        OntClass myClass = someClass.getOntClass(jenaModel);
        return myClass.listInstances();
    }

    /**
     * Returns the jena Model.
     * @return jena Model.
     */
    public OntModel getJenaModel() {
        return jenaModel;
    }

    
}


