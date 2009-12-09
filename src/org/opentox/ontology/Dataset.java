package org.opentox.ontology;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntResource;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.InputStream;
import com.hp.hpl.jena.graph.Triple;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.sparql.core.Var;
import com.hp.hpl.jena.sparql.syntax.ElementGroup;
import com.hp.hpl.jena.util.FileManager;
import com.hp.hpl.jena.vocabulary.DC;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import weka.core.Instances;
import java.io.BufferedWriter;
import org.opentox.ontology.OT;
import weka.core.Attribute;
import weka.core.FastVector;

/**
 *
 * @author OpenTox - http://www.opentox.org
 * @author Sopasakis Pantelis
 * @author Sarimveis Harry
 */
public class Dataset {

    private OntModel jenaModel;

    public Dataset(InputStream in) {
        try {
            jenaModel = OT.createModel();
            jenaModel.read(in, null);



        } catch (Exception ex) {
            Logger.getLogger(Dataset.class.getName()).log(Level.SEVERE, null, ex);
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
    public ExtendedIterator<? extends OntResource> getIteratorFor(Namespace.Class someClass) {
        OntClass myClass = someClass.getOntClass(jenaModel);
        return myClass.listInstances();
    }


    public OntModel getModel() {
        return jenaModel;
    }

    

    public Instances getWekaDataset() {
        FastVector attributes;
        Instances data = null;

        attributes = new FastVector();
        ExtendedIterator<? extends OntResource> it = getIteratorFor(OT.Class.Feature);
        for (; it.hasNext() ;){
            attributes.addElement(new Attribute(it.next().getURI()));
        }

        ExtendedIterator<? extends OntResource> it2 = getIteratorFor(OT.Class.Dataset);
        if (it2.hasNext()){
            data = new Instances(it2.next().getURI(), attributes, 0);
        }        

        return data;

    }


}
