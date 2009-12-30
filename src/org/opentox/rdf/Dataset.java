package org.opentox.rdf;

import org.opentox.namespaces.OT;
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
import com.hp.hpl.jena.vocabulary.DC;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import weka.core.Instances;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.opentox.resource.AbstractResource.URIs;
import org.restlet.data.Status;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.filters.unsupervised.attribute.NumericToNominal;

/**
 * @author OpenTox - http://www.opentox.org
 * @author Sopasakis Pantelis
 * @author Sarimveis Harry
 */
public class Dataset extends RDFHandler implements Serializable{

    private static final long serialVersionUID = -920482801546239926L;

    /**
     * Initializes a new Dataset object given an input stream which can either
     * correspond to a file on the disk or some web resource.
     * @param in
     * @see Dataset#Dataset(java.net.URI)
     * @see Dataset#Dataset(java.net.URL)
     */
    public Dataset(InputStream in) {
        super(in);
    }

    protected Dataset(OntModel ontological_model) {
        jenaModel = ontological_model;
    }

    /**
     * Initializes a Dataset given a URI.
     * @param dataset_uri
     * @see Dataset#Dataset(java.net.URL)
     * @see Dataset#Dataset(java.io.InputStream)
     */
    public Dataset(URI dataset_uri) {
        super();
        HttpURLConnection.setFollowRedirects(false);
        HttpURLConnection con = null;
        try {
            URL dataset_url = dataset_uri.toURL();
            con = (HttpURLConnection) dataset_url.openConnection();
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setUseCaches(false);
            con.addRequestProperty("Accept", "application/rdf+xml");
            jenaModel = OT.createModel();
            jenaModel.read(con.getInputStream(), null);

        } catch (MalformedURLException ex) {
            errorRep.append(ex, "The dataset_uri cannot be cast as a valid URL!", Status.CLIENT_ERROR_BAD_REQUEST);
        } catch (IllegalArgumentException ex) {
            errorRep.append(ex, "Check the dataset URI you posted !", Status.CLIENT_ERROR_BAD_REQUEST);
        } catch (SecurityException ex) {
            errorRep.append(ex, "A security exception occured! It is possible that a resource"
                    + "requires user authentication.", Status.SERVER_ERROR_INTERNAL);
        } catch (IllegalStateException ex) {
            errorRep.append(ex, "HTTP connection cannot be configured correctly!",
                    Status.SERVER_ERROR_INTERNAL);
        } catch (FileNotFoundException ex) {
            errorRep.append(ex, "The requested dataset resource could not be found!", Status.CLIENT_ERROR_BAD_REQUEST);
        } catch (IOException ex) {
            errorRep.append(ex, "Input/Output Error while trying to open a connection!", Status.SERVER_ERROR_INTERNAL);
        } catch (Exception ex) {
            errorRep.append(ex, "The dataset uri you provided seems that does not correspond to an existing resource!",
                    Status.CLIENT_ERROR_BAD_REQUEST);
            Logger.getLogger(Dataset.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Initializes a Dataset object given a URL.
     * @param dataset_url
     * @throws URISyntaxException
     * @see Dataset#Dataset(java.net.URI)
     * @see Dataset#Dataset(java.io.InputStream)
     */
    public Dataset(URL dataset_url) throws URISyntaxException {
        this(dataset_url.toURI());
    }

    /**
     * Initialized a void Dataset object; invokes a call to the super-class constructor
     * @see RDFHandler
     * @see Dataset#Dataset(java.io.InputStream)
     * @see Dataset#Dataset(java.net.URI)
     * @see Dataset#Dataset(java.net.URL) 
     */
    public Dataset() {
        super();
    }

    /**
     * Returns the set of features in the dataset.
     * @return the set of all features in the dataset.
     */
    public Set<String> setOfFeatures() {
        Set<String> set = new HashSet<String>();
        StmtIterator features =
                jenaModel.listStatements(
                new SimpleSelector(null, RDF.type, OT.Class.Feature.getOntClass(jenaModel)));
        while (features.hasNext()) {
            set.add(features.next().getSubject().toString());
        }
        return set;
    }

    /**
     * This method is used to encapsulate the data of the RDF document in a
     * weka.core.Instances object which can be used to create Regression and
     * classification models using weka algorithms.
     *
     * <!-- SCOPE -->
     * <p>
     * <b>Description:</b><br/>
     * This method was developed to
     * generate datasets (as Instances) in order to be used as input to training
     * algorithms of weka.
     * </p>
     *
     * <!-- MORE INFO -->
     * <p>
     * <b>Characteristics of generated Instances:</b><br/>
     * The relation name of the generated instances is the same with the identifier of
     * the dataset. If no identifier is available, then this is set to some arbitraty URI.
     * If <tt>isClassNominal</tt> is set to false, the class attribute is not defined in
     * this method but it can be set externally (from the method that calls getWekaDataset).
     * If <tt>isClassNominal</tt> is set to true, the target of the datset is defined by the
     * first agument of the method (String target).<br/>
     * The attributes of the Instances object coincides with the set of features of the
     * dataset in RDF format.
     * </p>
     * @param target URI of the target feature of the dataset. It is optional (you
     * may leave it null) if you are going to use the Instances for regression models
     * and isClassNominal is set to false, otherwise you have to specify a valid feature
     * URI.
     * @param isClassNominal Set to true if the class attribute should be considered to
     * be nominal.
     * @return The Instances object which encapsulates the data in the RDF document.
     */
    public Instances getWekaDatasetForTraining(String target, boolean isClassNominal) throws Exception {

        /**
         * Check if some error occured while constructing the
         * Dataset object.
         */
        if (errorRep.getErrorLevel() > 0) {
            throw new Exception();
        }



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

            StmtIterator compoundNamesIterator =
                    jenaModel.listStatements(new SimpleSelector(dataEntry.getSubject(), OT.compound, (Resource) null));
            String compoundName = null;
            if (compoundNamesIterator.hasNext()) {
                compoundName = compoundNamesIterator.next().getObject().as(Resource.class).getURI();
            }

            vals[data.attribute("compound_uri").index()] = data.attribute("compound_uri").addStringValue(compoundName);

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
                    try {
                        vals[data.attribute(atName).index()] = Double.parseDouble(atVal);
                        /**
                         * The following catch rule, handles cases where some values are declared
                         * as numeric (double, float etc) but their value cannot be cast as
                         * double.
                         */
                    } catch (NumberFormatException ex) {
                    }
                } else if (stringXSDtypes().contains(featureTypeMap.get(jenaModel.createResource(atName)))) {
                    vals[data.attribute(atName).index()] = data.attribute(atName).addStringValue(atVal);
                } else if (XSDDatatype.XSDdate.getURI().equals(atName)) {
                    try {
                        vals[data.attribute(atName).index()] = data.attribute(atName).parseDate(atVal);
                    } catch (ParseException ex) {
                        Logger.getLogger(Dataset.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }



            }
            temp = new Instance(1.0, vals);

            // Add the Instance only if its compatible with the dataset!
            if (data.checkInstance(temp)) {
                data.add(temp);
            } else {
                System.err.println("Warning! The instance " + temp + " is not compatible with the dataset!");
            }


        }
        dataEntryIterator.close();


        // C. Handle Nominal Class Attributes...
        if (isClassNominal) {
            NumericToNominal filter = new NumericToNominal();
            try {
                filter.setInputFormat(data);
                int[] filtered_attributes = {data.attribute(target).index()};
                filter.setAttributeIndicesArray(filtered_attributes);
                data = new Instances(NumericToNominal.useFilter(data, filter));
                data.setRelationName(relationName);
            } catch (NullPointerException ex) {
                errorRep.append(ex, "(Dataset Parser) The target you specified seems not to be valid!",
                        Status.CLIENT_ERROR_BAD_REQUEST);
            } catch (Exception ex) {
                errorRep.append(ex, "(Dataset Parser) Internal Error occured while trying to "
                        + "parse the given dataset. The target could not be converted to nominal!",
                        Status.SERVER_ERROR_INTERNAL);
                Logger.getLogger(Dataset.class.getName()).log(Level.SEVERE, null, ex);
            }

        }


        return data;

    };

    

    /**
     * Similar to {@link org.opentox.rdf.Dataset#getWekaDatasetForTraining(java.lang.String, boolean)  }
     * but the generated Instances is constructed with respect to a certain model.
     * @param model_id
     * @return Instances for prediction using a given model.
     */
    public Instances getWekaDatasetForPrediction(String model_id) {
        return null;
    }

    ;

    /**
     * The set of XSD data types that should be cast as numeric.
     * @return the set of XSD datatypes that should be considered as numeric.
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
     * @return the set of XSD datatypes that should be considered as strings.
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
        // atts is the FastVector containing all attributes of the dataset:
        FastVector atts = new FastVector();
        Iterator<Entry<Resource, String>> mapIterator = featureTypeMap.entrySet().iterator();
        Entry<Resource, String> entry;
        // All datasets must have an attribute called 'compound_uri'
        atts.addElement(new Attribute("compound_uri", (FastVector) null));
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
    public static void createRandomDataset(int numOfCompounds, int numOfFeatures,
            OutputStream out, String Lang) {
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


            Individual dataEntry = null, compound = null, feature = null, featureValue = null;



            for (int i = 0; i < numOfCompounds; i++) {
                dataEntry = datasetModel.createIndividual(OT.Class.DataEntry.getOntClass(datasetModel));
                dataset.addProperty(OT.dataEntry, dataEntry);

                compound = datasetModel.createIndividual(
                        "http://sth.com/compound/" + i, OT.Class.Compound.getOntClass(datasetModel));
                dataEntry.addProperty(OT.compound, compound);

                for (int j = 0; j < numOfFeatures; j++) {
                    feature = datasetModel.createIndividual("http://sth.com/feature/" + j,
                            OT.Class.Feature.getOntClass(datasetModel));
                    featureValue = datasetModel.createIndividual(
                            OT.Class.FeatureValue.getOntClass(datasetModel));
                    featureValue.addProperty(OT.feature, feature);
                    featureValue.addLiteral(OT.value, datasetModel.createTypedLiteral(Math.random(), XSDDatatype.XSDdouble));
                    dataEntry.addProperty(OT.values, featureValue);
                }

                dataset.addProperty(OT.dataEntry, dataEntry);

            }
            if (out == null) {
                out = System.out;
            }
            if (Lang == null) {
                Lang = "RDF/XML";
            }
            datasetModel.write(out, Lang);

        } catch (Exception ex) {
            Logger.getLogger(Dataset.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     *
     * @param predictedData
     * @return
     */
    public synchronized Dataset populateDataset(Instances predictedData) {
        OntModel datasetModel = null;
        try {
            datasetModel = OT.createModel();
            Individual dataset = datasetModel.createIndividual(OT.Class.Dataset.createOntClass(datasetModel));
            dataset.addRDFType(OT.Class.Dataset.createProperty(datasetModel));
            dataset.addProperty(jenaModel.createAnnotationProperty(DC.creator.getURI()), URIs.baseURI);
            dataset.addProperty(jenaModel.createAnnotationProperty(RDFS.comment.getURI()),
                    predictedData.relationName());


            OT.Class.Dataset.createOntClass(datasetModel);
            OT.Class.DataEntry.createOntClass(datasetModel);
            OT.Class.Feature.createOntClass(datasetModel);
            OT.Class.FeatureValue.createOntClass(datasetModel);
            OT.Class.Compound.createOntClass(datasetModel);


            Individual dataEntry = null, compound = null, feature = null, featureValue = null;



            for (int i = 0; i < predictedData.numInstances(); i++) {
                dataEntry = datasetModel.createIndividual(OT.Class.DataEntry.getOntClass(datasetModel));
                dataset.addProperty(OT.dataEntry, dataEntry);

                compound = datasetModel.createIndividual(
                        predictedData.instance(i).stringValue(predictedData.attribute("compound_uri")),
                        OT.Class.Compound.getOntClass(datasetModel));
                dataEntry.addProperty(OT.compound, compound);

                feature = datasetModel.createIndividual(predictedData.attribute(1).name(),
                        OT.Class.Feature.getOntClass(datasetModel));
                featureValue = datasetModel.createIndividual(
                        OT.Class.FeatureValue.getOntClass(datasetModel));
                featureValue.addProperty(OT.feature, feature);
                featureValue.addLiteral(OT.value, datasetModel.createTypedLiteral(predictedData.instance(i).value(1),
                        XSDDatatype.XSDdouble));
                dataEntry.addProperty(OT.values, featureValue);


                dataset.addProperty(OT.dataEntry, dataEntry);

            }


        } catch (Exception ex) {
            errorRep.append(ex, "Unexpectable Exception!", Status.SERVER_ERROR_INTERNAL);

        }

        return new Dataset(datasetModel);
    }


    public static void main(String[] atts) throws URISyntaxException, Exception {
        Dataset ds = new Dataset(new URI("http://localhost/small.rdf"));
        System.out.println(ds.getWekaDatasetForTraining(null, false));
    }
}
