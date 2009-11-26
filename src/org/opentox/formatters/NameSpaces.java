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
    static class OT {
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
        public static final Resource algorithm = m.createResource(uri + "Algorithm");
        public static final Resource model = m.createResource(uri + "Model");
        public static final Resource statistic = m.createResource(uri + "Statistic");
        public static final Resource parameter = m.createResource(uri + "Parameter");
        // Properties (Literals) :
        public static final Property paramValue = m.createProperty(uri, "paramValue");
        public static final Property paramScope = m.createProperty(uri, "paramScope");
        public static final Property parameters = m.createProperty(uri, "parameters");
        public static final Property statisticsSupported = m.createProperty(uri, "statisticsSupported");
    }
}

