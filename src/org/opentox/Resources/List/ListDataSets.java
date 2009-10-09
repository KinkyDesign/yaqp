package org.opentox.Resources.List;

import java.io.File;
import org.opentox.Resources.Algorithms.*;
import org.opentox.Resources.*;
import org.restlet.Context;
import org.restlet.data.MediaType;
import org.restlet.data.Reference;
import org.restlet.data.ReferenceList;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.data.Status;
import org.restlet.resource.Representation;
import org.restlet.resource.StringRepresentation;
import org.restlet.resource.Variant;

/**
 * List of all classification algorithms.
 * <p>
 * URI: /algorithm/learningalgorithm/classification<br/>
 * Allowed Methods: GET<br/>
 * Status Codes: 200, 503<br/>
 * Supported Mediatypes: TEXT_URI_LIST
 * </p>
 * @author OpenTox - http://www.opentox.org
 * @author Sopasakis Pantelis
 * @author Sarimveis Harry
 * @version 1.0 (Last Update: Aug 23, 2009)
 */
public class ListDataSets extends AbstractResource {

    private static final long serialVersionUID = 10012190001001001L;

    private String type="";
    

    public ListDataSets(Context context, Request request, Response response) {
        super(context, request, response);
        getVariants().add(new Variant(MediaType.TEXT_URI_LIST));
    }

    @Override
    public Representation represent(Variant variant)
    {

        StringBuilder builder = new StringBuilder();

        
        
            File arffFolder = new File (arffDir);
            File[] arffFiles = arffFolder.listFiles();
            for (int i=0;i<arffFiles.length;i++){
                builder.append(baseURI+"/dataset/"+arffFiles[i].getName()+"\n");
            }
        
    getResponse().setStatus(Status.SUCCESS_OK);
    return new StringRepresentation(builder.toString(), MediaType.TEXT_PLAIN);
    }
}
