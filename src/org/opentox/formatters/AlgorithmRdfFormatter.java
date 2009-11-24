package org.opentox.formatters;

import org.opentox.Resources.Algorithms.*;
import org.opentox.Resources.AbstractResource;
import org.restlet.data.MediaType;
import org.restlet.representation.StringRepresentation;


/**
 * Build an RDF representation for an Algorithm.
 * Validated at <a href="http://www.rdfabout.com/demo/validator/">
 * http://www.rdfabout.com/demo/validator/</a> !
 * @author OpenTox - http://www.opentox.org
 * @author Sopasakis Pantelis
 * @author Kolotouros Dimitris
 * @author Sarimveis Harry
 */
public class AlgorithmRdfFormatter extends AbstractAlgorithmFormatter {

    private static final MediaType mime = MediaType.APPLICATION_RDF_XML;

    private static final long serialVersionUID = 52795861750765264L;

    /**
     * Class Constructor.
     * @param metainf Algorithm Meta-information one has to provide to
     * construct an AlgorithmRdfFormater object.
     */
    public AlgorithmRdfFormatter(AlgorithmMetaInf metainf) {
        super.metainf = metainf;
    }




    @Override
    public StringRepresentation getStringRepresentation() {        
        return null;
    }
       

    
    /**
     * @param rdfns A new namespace for RDF
     */
    public void setRdfNamespace(String rdfns) {
        this.rdf = rdfns;
    }

    /**
     * Update the namespace for OpenTox Models and Algorithms.
     * @param otns
     */
    public void setOpentoxNamespace(String otns) {
        this.ot = otns;
    }

    /**
     * Update the Dublin Core namespace.
     * @param dcns new DCNS
     */
    public void setDcNamespace(String dcns) {
        this.dc = dcns;
    }

   
}
