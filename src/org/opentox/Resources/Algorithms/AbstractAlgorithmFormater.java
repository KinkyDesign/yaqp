/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.opentox.Resources.Algorithms;

import java.util.ArrayList;
import org.restlet.representation.StringRepresentation;

/**
 *
 * @author OpenTox - http://www.opentox.org
 * @author Sopasakis Pantelis
 * @author Sarimveis Harry
 */
public abstract class AbstractAlgorithmFormater {

    protected ArrayList<String> statisticsSupported;
    protected String[][] Parameters;
    protected String title, identifier, algorithmType;
    protected String rdfNamespace = "http://www.w3.org/1999/02/22-rdf-syntax-ns#",
            dcNamespace = "http://purl.org/dc/elements/1.1/",
            opentoxNamespace = "http://opentox.org/api.1-1/algorithm";

    /**
     * <b>Label:</b> Title<br/><br/>
     * <b>Element Description:</b> The name given to the resource.
     * Typically, a Title will be a name by which the resource is formally known.<br/><br/>
     * <b>Guidelines for creation of content:</b>
     * If in doubt about what constitutes the title, repeat the Title element and
     * include the variants in second and subsequent Title iterations.
     * If the item is in HTML, view the source document and make sure that the
     * title identified in the title header (if any) is also included as a Title.<br/><br/>
     * <b>Examples:</b>
     * <pre> Title="A Pilot's Guide to Aircraft Insurance"
     * Title="The Sound of Music"
     * Title="Green on Greens"
     * Title="AOPA's Tips on Buying Used Aircraft"</pre>
     * @see http://dublincore.org/documents/usageguide/elements.shtml
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
    }

/**
     * <b>Label:</b> Resource Identifier<br/><br/>
     * <b>Element Description:</b> An unambiguous reference to the resource
     * within a given context. Recommended best practice is to identify the
     * resource by means of a string or number conforming to a formal
     * identification system. Examples of formal identification systems
     * include the Uniform Resource Identifier (URI) (including the
     * Uniform Resource Locator (URL), the Digital Object
     * Identifier (DOI) and the International Standard Book Number (ISBN).<br/><br/>
     * <b>Guidelines for content creation:</b>
     * This element can also be used for local identifiers (e.g. ID numbers
     * or call numbers) assigned by the Creator of the resource to apply to
     * a particular item. It should not be used for identification of the
     * metadata record itself.<br/><br/>
     * <b>Examples</b>
     * <pre> Identifier="http://purl.oclc.org/metadata/dublin_core/"
     * Identifier="ISBN:0385424728"
     * Identifier="H-A-X 5690B" [publisher number]</pre>
     * @param identifier The resource identifier
     * @see http://dublincore.org/documents/usageguide/elements.shtml
     */
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    
    /**
     * Specify the algorithm type according to a formal ontology.
     * @param AlgorithmType
     */
    public void setAlgorithmType(String AlgorithmType){
        this.algorithmType=AlgorithmType;
    }

    /**
     * Define the characteristics of the algorithm. The <em>ArrayList&lt;String&gt;
     * </em>element statisticsSupported is a list of the supported statistics by the
     * algorithm. The String array <em>Parameters</em> is the set of tuning parameters
     * of the algorithm, i.e. data that can be posted to the algorithm.<br/><br/>
     * <b>Note:</b> Parameters is a N-by-3 matrix of type String. The first
     * column contains the name of the parameter, the second the datatype and
     * the third the default value.
     * @param statisticsSupported
     * @param Parameters
     */
    public void setAlgorithm(ArrayList<String> statisticsSupported,
         String[][] Parameters   )
    {
        this.statisticsSupported=statisticsSupported;
        this.Parameters=Parameters;
    }

    public abstract StringRepresentation getStringRepresentation();


}
