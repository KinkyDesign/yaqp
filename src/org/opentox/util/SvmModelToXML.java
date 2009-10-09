package org.opentox.util;

import org.restlet.data.Status;

/**
 * Under Developement!
 * @author OpenTox - http://www.opentox.org
 * @author Sopasakis Pantelis
 * @author Sarimveis Harry
 * @version 1.0 (Last Update: Aug 18, 2009)
 */
public class SvmModelToXML {

    private static final long serialVersionUID=100121700023001L;

    /**
     * Path to an existing SVM model
     */
    private String SvmModel="";

    /**
     * Path to the XML representation
     * of the XML model that is to be created.
     */
    private String SvmXML="";

    /**
     * Data set id...
     */
    private String dataSetId="";

    /**
     * Status initially set to 202
     */
    private Status status=Status.SUCCESS_ACCEPTED;

    /**
     * Model Type: Classification/Regression
     */
    private String modelType="";


    /**
     * Private constructor for internal use only.
     */
    private SvmModelToXML(){

    }

    /**
     * Constructor. Initializes a SvmModelToXML object providing
     * the source and target file paths.
     * @param SvmModel Path to SVM model
     * @param SvmXML Path to XML file.
     * @param dataSetId Id of the data set used to build the model
     */
    public SvmModelToXML(String SvmModel, String SvmXML, String dataSetId, String[] ClassifierOptions){
        this.SvmModel=SvmModel;
        this.SvmXML=SvmXML;
        this.dataSetId=dataSetId;
    }

    /**
     * Returns the status.
     * @return status The status (possible exceptions...)
     */
    public Status getStatus(){
        return this.status;
    }

    /**
     * Private Method. Sets the status.
     * @param st status to be set.
     */
    private void setStatus(Status st){
        this.status=st;
    }

    public void convertToXML(){


    }

}
