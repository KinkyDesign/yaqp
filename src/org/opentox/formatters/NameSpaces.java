package org.opentox.formatters;

import com.hp.hpl.jena.rdf.model.*;

/**
 *
 * @author OpenTox - http://www.opentox.org
 * @author Sopasakis Pantelis
 * @author Kolotouros Dimitris
 * @author Sarimveis Harry
 */
public class NameSpaces {

    /**
     * The OpenTox NameSpace and its elements.
     */
    public static class OT {
        /**
         * The URI of the opentox namespace
         */
        protected static final String uri = "http://opentox.org/api/1.1/#";
        /**
         * The suggested prefix for the opentox namespace.
         */
        public static final String namespacePrefix = "ot";

        /** returns the URI for this schema
         * @return the URI for this schema
         */
        public static String getURI() {
            return uri;
        }
        // Jena Model:
        private static Model m = ModelFactory.createDefaultModel();
        // Resources:
        public static final Resource OT_TYPE_Algorithm = m.createResource(uri + "Algorithm");
        public static final Resource OT_TYPE_Model = m.createResource(uri + "Model");
        public static final Resource OT_TYPE_Statistic = m.createResource(uri + "Statistic");
        public static final Resource OT_TYPE_Parameter = m.createResource(uri + "Parameter");
        public static final Resource OT_TYPE_Dataset = m.createResource(uri + "Dataset");
        public static final Resource OT_TYPE_DataEntry = m.createResource(uri + "DataEntry");
        public static final Resource OT_TYPE_Compound = m.createResource(uri + "DataEntry");
        public static final Resource OT_TYPE_Feature = m.createResource(uri + "Feature");
        public static final Resource OT_TYPE_FeatureValue = m.createResource(uri + "FeatureValue");
        // Properties (Literals) :
        public static final Property paramValue = m.createProperty(uri, "paramValue");
        public static final Property paramScope = m.createProperty(uri, "paramScope");
        public static final Property parameters = m.createProperty(uri, "parameters");
        public static final Property statisticsSupported = m.createProperty(uri, "statisticsSupported");
        public static final Property dataEntry = m.createProperty(uri, "dataEntry");
        public static final Property compound = m.createProperty(uri, "compound");
        public static final Property value = m.createProperty(uri, "value");
        public static final Property values = m.createProperty(uri, "values");
        public static final Property feature = m.createProperty(uri, "feature");
    }
}

