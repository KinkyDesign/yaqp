package org.opentox.ontology;

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Literal;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.SimpleSelector;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.vocabulary.RDF;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import weka.core.Instances;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.restlet.data.Status;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;

/**
 * @author OpenTox - http://www.opentox.org
 * @author Sopasakis Pantelis
 * @author Sarimveis Harry
 */
public class Dataset extends RDFParser{

    private static final long serialVersionUID = -920482801546239926L;


    public Dataset(InputStream in) {
        super(in);
    }

    public Dataset(){
        super();
    }    


    /**
     * Returns the set of features in the dataset.
     * @return
     */
    public Set<String> setOfFeatures(){
        Set<String> set = new HashSet<String>();
        StmtIterator features =
                jenaModel.listStatements(
                new SimpleSelector(null, RDF.type, OT.Class.Feature.getOntClass(jenaModel)));
        while (features.hasNext()){
            set.add(features.next().getSubject().toString());
        }
        return set;
    }


    /**
     * This method is used to encapsulate the data of the RDF document in a
     * weka.core.Instances object which can be used to create Regression and
     * classification models using weka algorithms.
     * @return The Instances object which encapsulates the data in the RDF document.
     */
    public Instances getWekaDataset() {

        // A1. Some initial definitions:
        Resource dataEntryResource = OT.Class.DataEntry.getOntClass(jenaModel),
                dataSetResource = OT.Class.Dataset.getOntClass(jenaModel),
                featureResource = OT.Class.Feature.getOntClass(jenaModel);
        FastVector attributes = null;
        Instances data = null;
        StmtIterator dataSetIterator = null,
                featureIterator = null,
                valuesIterator = null,
                dataEntryIterator = null;


        /**
         * A2. Set the relation name for the data:
         * If there is no DataSet element declaration set the relation name to
         * "NoName". Otherwise use the URI of the dataset. If more than one datasets
         * are declared in the RDF, throw an error!
         */
        dataSetIterator =
                jenaModel.listStatements(new SimpleSelector(null, RDF.type, dataSetResource));
        String relationName = null;
        if (dataSetIterator.hasNext()) {
            relationName = dataSetIterator.next().getSubject().getURI();
            if (dataSetIterator.hasNext()) {
                return null;
            }
        } else {
            relationName = "http://someserver.com/dataset/NoName" + jenaModel.hashCode();
        }
        dataSetIterator.close();

        /**
         * Create a Map<String, String> such that its first entry is a feature in
         * the dataset and the second is its datatype.
         */
        Map<Resource, String> featureTypeMap = new HashMap<Resource, String>();


        //  A3. Iterate over all Features.
        featureIterator =
                jenaModel.listStatements(new SimpleSelector(null, RDF.type, featureResource));
        while (featureIterator.hasNext()) {
            Statement feature = featureIterator.next();

            // A4. For every single feature in the dataset, pick a "values" node.
            valuesIterator =
                    jenaModel.listStatements(new SimpleSelector(null, OT.feature, feature.getSubject()));
            if (valuesIterator.hasNext()) {
                Statement values = valuesIterator.next();

                // A5. For this values node, get the value
                StmtIterator valueInValuesIter =
                        jenaModel.listStatements(new SimpleSelector(values.getSubject(), OT.value, (Resource) null));
                if (valueInValuesIter.hasNext()) {
                    featureTypeMap.put(feature.getSubject(), valueInValuesIter.next().getLiteral().getDatatypeURI());
                }
            }
            valuesIterator.close();
        }
        featureIterator.close();


        /**
         * A6. Now update the attributes of the dataset.
         */
        attributes = getAttributes(featureTypeMap);
        data = new Instances(relationName, attributes, 0);



        /**
         * B1. Iterate over all dataentries
         */
        dataEntryIterator =
                jenaModel.listStatements(new SimpleSelector(null, RDF.type, dataEntryResource));
        while (dataEntryIterator.hasNext()) {
            Statement dataEntry = dataEntryIterator.next();

            /**
             * B2. For every dataEntry, iterate over all values nodes.
             */
            Instance temp = null;
            valuesIterator =
                    jenaModel.listStatements(new SimpleSelector(dataEntry.getSubject(), OT.values, (Resource) null));

            double[] vals = new double[data.numAttributes()];
            for (int i = 0; i < data.numAttributes(); i++) {
                vals[i] = Instance.missingValue();
            }

            while (valuesIterator.hasNext()) {
                Statement values = valuesIterator.next();

                /*
                 * B3. A pair of the form (AttributeName, AttributeValue) is created.
                 * This will be registered in an Instance-type object which
                 * is turn will be used to update the dataset.
                 */

                // atVal is the value of the attribute
                String atVal = values.getProperty(OT.value).getObject().as(Literal.class).getValue().toString();
                // and atName is the name of the corresponding attribute.
                String atName = values.getProperty(OT.feature).getObject().as(Resource.class).getURI();


                if (numericXSDtypes().contains(featureTypeMap.get(jenaModel.createResource(atName)))) {
                    vals[data.attribute(atName).index()] = Double.parseDouble(atVal);
                } else if (stringXSDtypes().contains(featureTypeMap.get(jenaModel.createResource(atName)))) {
                    vals[data.attribute(atName).index()] = data.attribute(atName).addStringValue(atVal);
                } else if (XSDDatatype.XSDdate.getURI().equals(atName)) {
                    try {
                        vals[data.attribute(atName).index()] = data.attribute(atName).parseDate(atVal);
                    } catch (ParseException ex) {
                        internalStatus = Status.SERVER_ERROR_INTERNAL;
                        Logger.getLogger(Dataset.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }


            }
            temp = new Instance(1.0, vals);

            // Add the Instance only if its compatible with the dataset!
            if (data.checkInstance(temp)) {
                data.add(temp);
            }else{
                System.err.println("Warning! The instance "+temp+" is not compatible with the dataset!");
            }


        }
        dataEntryIterator.close();


        return data;

    }

    /**
     * The set of XSD data types that should be cast as numeric.
     * @return
     */
    private static Set<String> numericXSDtypes() {
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
        return numericXSDtypes;
    }

    /**
     * The set of XSD data types that should be cast as string.
     * @return
     */
    private static Set<String> stringXSDtypes() {
        Set<String> stringXSDtypes = new HashSet<String>();
        stringXSDtypes.add(XSDDatatype.XSDstring.getURI());
        stringXSDtypes.add(XSDDatatype.XSDanyURI.getURI());
        return stringXSDtypes;
    }

    /**
     * Returns a FastVector for the attributes of the dataset as an Instaces
     * object.
     * @param featureTypeMap
     * @return FastVector of Attributes
     */
    private FastVector getAttributes(Map<Resource, String> featureTypeMap) {
        FastVector atts = new FastVector();
        Iterator<Entry<Resource, String>> mapIterator = featureTypeMap.entrySet().iterator();
        Entry<Resource, String> entry;
        while (mapIterator.hasNext()) {
            entry = mapIterator.next();
            String dataType = entry.getValue();
            if (numericXSDtypes().contains(dataType)) {
                atts.addElement(new Attribute(entry.getKey().getURI()));
            } else if (stringXSDtypes().contains(dataType)) {
                atts.addElement(new Attribute(entry.getKey().getURI(), (FastVector) null));
            }
        }
        return atts;
    }



    /**
     * Generates a random dataset of prescribed dimensions.
     * @param numOfCompounds The number of compounds in the dataset.
     * @param numOfFeatures The number of features of the dataset.
     * @param out The output stream to be used to write the dataset. Can be System.out
     * (The standard system output), a FileOutputStream or other stream. If set to
     * null, System.out will be used.
     * @param Lang The prefered language of the representation. Choose among
     * "RDF/XML", "RDF/XML-ABBREV", "N-TRIPLE" and "N3"
     */
    public static void createNewDataset(int numOfCompounds, int numOfFeatures,
            OutputStream out, String Lang){
        OntModel datasetModel;
        try {
            datasetModel = OT.createModel();
            Individual dataset = datasetModel.createIndividual("http://sth.com/dataset/1",
                datasetModel.getOntClass(OT.Class.Dataset.getURI()));
        dataset.addRDFType(OT.Class.Dataset.createProperty(datasetModel));


        OT.Class.Dataset.createOntClass(datasetModel);
        OT.Class.DataEntry.createOntClass(datasetModel);
        OT.Class.Feature.createOntClass(datasetModel);
        OT.Class.FeatureValue.createOntClass(datasetModel);
        OT.Class.Compound.createOntClass(datasetModel);


        Individual dataEntry = null, compound = null, feature = null, featureValue=null;



        for (int i = 0; i < numOfCompounds; i++) {
        dataEntry = datasetModel.createIndividual(OT.Class.DataEntry.getOntClass(datasetModel));
        dataset.addProperty(OT.dataEntry, dataEntry);        

            compound = datasetModel.createIndividual(
                    "http://sth.com/compound/"+i, OT.Class.Compound.getOntClass(datasetModel));
            dataEntry.addProperty(OT.compound, compound);

            for (int j=0;j<numOfFeatures;j++){
                feature = datasetModel.createIndividual("http://sth.com/feature/"+j,
                OT.Class.Feature.getOntClass(datasetModel));
                featureValue = datasetModel.createIndividual(
                        OT.Class.FeatureValue.getOntClass(datasetModel));
                featureValue.addProperty(OT.feature, feature);
                featureValue.addLiteral(OT.value, datasetModel.
                        createTypedLiteral(Math.random(), XSDDatatype.XSDdouble));
                dataEntry.addProperty(OT.values, featureValue);
            }

            dataset.addProperty(OT.dataEntry, dataEntry);

        }
        if (out==null){
            out = System.out;
        }
        if (Lang==null){
            Lang="RDF/XML";
        }
        datasetModel.write(out, Lang);

        } catch (Exception ex) {
            Logger.getLogger(Dataset.class.getName()).log(Level.SEVERE, null, ex);
        }

    }


    /**
     * This main method is for testing purposes only and will be removed.
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws IOException, URISyntaxException {

        URI d_set = new URI("http://ambit.uni-plovdiv.bg:8080/ambit2/dataset/6");
        
        //URI d_set = new URI("http://localhost/files/1.rdf");
        HttpURLConnection.setFollowRedirects(false);
        HttpURLConnection con = null;
        try {
            con = (HttpURLConnection) d_set.toURL().openConnection();
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setUseCaches(false);
            con.addRequestProperty("Accept", "application/rdf+xml");

            Dataset data = new Dataset(con.getInputStream());
            //Dataset data = new Dataset(new FileInputStream(System.getProperty("user.home")+"/Files/myDs.rdf"));
            ///// Instances wekaData = data.getWekaDataset();
            Set<String > set = data.setOfFeatures();
            Iterator<String> it = set.iterator();
            while (it.hasNext()){
                System.out.println(it.next());
            }

        } catch (IOException ex) {
        }
    }
}
