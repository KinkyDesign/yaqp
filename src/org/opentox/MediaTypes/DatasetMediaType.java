package org.opentox.MediaTypes;


import org.restlet.data.MediaType;
import org.restlet.data.Parameter;
import org.restlet.util.Series;


/**
 *
 * Media types for datasets.
 * @author OpenTox - http://www.opentox.org
 * @author Sopasakis Pantelis
 * @author Sarimveis Harry
 * @version 1.0 (Last Update: Sep 2, 2009)
 */
public class DatasetMediaType {

    private static Series<Parameter> params;

    /**
     * Constructor.
     */
    public DatasetMediaType(){
        params.add("type","dataset");

    }
   

    /**
     * Attribute Relation File Format.
     */
    public static final MediaType DATASET_ARFF = new MediaType("dataset/arff",params,"ARFF");

    /**
     * XML representation of an Attritube-Relation-File-Format (ARFF) file.
     */
    public static final MediaType DATASET_XRFF = new MediaType("dataset/xrff",params,"XRFF");
    /**
     * Delimiter Seperated Data Format.
     */
    public static final MediaType DATASET_DSD = new MediaType("dataset/dsd",params,"DSD");
    /**
     * PMML Data Dictionary of the dataset.
     */
    public static final MediaType DATASET_META_INF = new MediaType("dataset/meta",params,"META_INF");


}
