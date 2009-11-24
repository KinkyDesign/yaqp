package org.opentox.formatters;

/**
 *
 * @author OpenTox - http://www.opentox.org
 * @author Sopasakis Pantelis
 * @author Kolotouros Dimitris
 * @author Sarimveis Harry
 */
public class Elements{

        static class RDF{
            static String description = "rdf:Description",
                    about = "rdf:about",
                    datatype="rdf:datatype",
                    type="rdf:type",
                    RDF="rdf:RDF";
        }

        static class DC{
            static String title="dc:title",
                    subject="dc:subject",
                    type="dc:type",
                    source="dc:source",
                    relation="dc:relation",
                    rights="dc:rights",
                    creator="dc:creator",
                    publisher="dc:publisher",
                    contributor="dc:contributor",
                    description="dc:description",
                    date="dc:date",
                    format="dc:format",
                    identifier="dc:identifier",
                    audience="dc:audience",
                    provenance="dc:provenance",
                    language="dc:language";
        }

        static class OT{
            static String
                    paramScope="ot:paramScope",
                    paramValue="ot:paramValue";
        }

        static class RDFS{
            static String
                    comment = "rdfs:comment";
        }

    }

