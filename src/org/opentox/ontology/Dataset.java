package org.opentox.ontology;

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntResource;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.SimpleSelector;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.vocabulary.RDF;
import java.io.InputStream;
import weka.core.Instances;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import weka.core.FastVector;

/**
 *
 * @author OpenTox - http://www.opentox.org
 * @author Sopasakis Pantelis
 * @author Sarimveis Harry
 */
public class Dataset {

    private static final long serialVersionUID = -823419872373642L;
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
    public ExtendedIterator<? extends OntResource> getClassMemberIteratorFor(Namespace.Class someClass) {
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

    /**
     * Parses the Dataset.
     * @param jenaModel
     * @throws IOException
     */
    public void parseDataset(OntModel jenaModel) throws IOException {
        
        Resource dataEntryResource = OT.Class.DataEntry.getOntClass(jenaModel);
        Resource dataSetResource = OT.Class.Dataset.getOntClass(jenaModel);

        StmtIterator dataSetIter = jenaModel.listStatements(new SimpleSelector(null, null, dataSetResource));
        while (dataSetIter.hasNext()){
            System.err.println(dataSetIter.next().getSubject());
        }

        // Iterates over the triples that match the case [?, type, DataEntry]
        StmtIterator dataEntriesIterator = jenaModel.listStatements(new SimpleSelector(null,RDF.type, dataEntryResource));

        while (dataEntriesIterator.hasNext()) {
            Statement st = dataEntriesIterator.next();

            // DataEntry:
            System.out.println(String.format("%s %s", "This is the data entry: ",st.getSubject()));

           
            StmtIterator valuesIterator =
                    jenaModel.listStatements(new SimpleSelector(st.getSubject(), OT.values, (RDFNode) null));
            while (valuesIterator.hasNext()) {
                st = valuesIterator.next();
                if (st.getObject().isResource()) {
                    Resource featureValueResource = (Resource) st.getObject();
                    RDFNode value = featureValueResource.getProperty(OT.value).getObject();
                    System.out.println(String.format("%s = %s",
                            //Feature
                            featureValueResource.getProperty(OT.feature).getObject().toString(),
                            // Value
                            value));
                }
            }
        }
    }



    /**
     * Have you created a new Dataset object, you can cast it as a weka.core.Instances
     * object.
     * @return
     */
    public Instances getWekaDataset() {

        // 1. Some initial definitions:
        Resource dataEntryResource = OT.Class.DataEntry.getOntClass(jenaModel),
                dataSetResource = OT.Class.Dataset.getOntClass(jenaModel),
                featureResource = OT.Class.Feature.getOntClass(jenaModel);
        FastVector attributes;
        Instances data = null;



        /**
         * 2. Set the relation name for the data:
         * If there is no DataSet element declaration set the relation name to
         * "NoName". Otherwise use the URI of the dataset. If more than one datasets
         * are declared in the RDF, throw an error!
         */
        
        StmtIterator dataSetIter =
                jenaModel.listStatements(new SimpleSelector(null, null, dataSetResource));
        String relationName = null;
        if (dataSetIter.hasNext()){
            relationName = dataSetIter.next().getSubject().getURI();
            if (dataSetIter.hasNext()){
                return null;
            }
        }else{
            relationName = "http://someserver.com/dataset/NoName"+jenaModel.hashCode();
        }

        /**
         * Create a Map<String, String> such that its first entry is a feature in
         * the dataset and the second is its datatype.
         */
        Map<Resource, String> featureTypeMap = new HashMap<Resource, String>();


        //  3. Iterate over all Features.
        StmtIterator featureIterator =
                jenaModel.listStatements(new SimpleSelector(null, RDF.type, featureResource));
        while (featureIterator.hasNext()){
            Statement feature = featureIterator.next();

            // 4. For every single feature in the dataset, pick a "values" node.
            StmtIterator valuesIterator =
                    jenaModel.listStatements(new SimpleSelector(null, OT.feature, feature.getSubject()));
            if (valuesIterator.hasNext()){
                Statement values = valuesIterator.next();

                // 5. For this values node, get the value
                StmtIterator valueInValuesIter =
                        jenaModel.listStatements(new SimpleSelector(values.getSubject(), OT.value, (Resource)null));
                if (valueInValuesIter.hasNext()){
                   featureTypeMap.put(feature.getSubject(), valueInValuesIter.next().getLiteral().getDatatypeURI());
                }
            }
        }

        /**
         * 6. Now update the attributes of the dataset.
         */
        attributes = getAttributes(featureTypeMap);



        return data;

    }

    
    
    private FastVector getAttributes(Map<Resource, String> featureTypeMap) {
        FastVector atts = new FastVector();

        Set<String> numericXSDtypes = new HashSet<String>();
        numericXSDtypes.add(XSDDatatype.XSDdouble.getURI());
        numericXSDtypes.add(XSDDatatype.XSDfloat.getURI());
        numericXSDtypes.add(XSDDatatype.XSDint.getURI());
        numericXSDtypes.add(XSDDatatype.XSDinteger.getURI());
        numericXSDtypes.add(XSDDatatype.XSDnegativeInteger.getURI());
        numericXSDtypes.add(XSDDatatype.XSDnonNegativeInteger.getURI());
        numericXSDtypes.add(XSDDatatype.XSDpositiveInteger.getURI());
        numericXSDtypes.add(XSDDatatype.XSDnonPositiveInteger.getURI());
        numericXSDtypes.add(XSDDatatype.XSDlong.getURI());
        numericXSDtypes.add(XSDDatatype.XSDshort.getURI());
        numericXSDtypes.add(XSDDatatype.XSDlong.getURI());
        numericXSDtypes.add(XSDDatatype.XSDunsignedInt.getURI());
        numericXSDtypes.add(XSDDatatype.XSDunsignedLong.getURI());
        numericXSDtypes.add(XSDDatatype.XSDunsignedShort.getURI());

        Iterator<Entry<Resource, String>> mapIterator = featureTypeMap.entrySet().iterator();
        Entry<Resource, String> entry;
        while (mapIterator.hasNext()){
            entry = mapIterator.next();
            String dataType = entry.getValue();
            if (numericXSDtypes.contains(dataType)){
                System.out.println("The feature "+entry.getKey().getURI()+" is numeric!");
            }
        }
        return new FastVector();
    }


}
