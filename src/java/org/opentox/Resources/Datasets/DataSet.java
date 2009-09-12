package org.opentox.Resources.Datasets;



import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.opentox.Resources.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.opentox.util.RepresentationFactory;
import org.restlet.Context;
import org.restlet.data.MediaType;
import org.restlet.data.Reference;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.resource.Representation;
import org.restlet.resource.StringRepresentation;
import org.restlet.resource.Variant;


/**
 * Representation of a data set.
 * @author OpenTox - http://www.opentox.org/
 * @author Sopasakis Pantelis
 * @author Sarimveis Harry
 * @version 1.3 (Last update: Sep 2, 2009)
 *
 */
public class DataSet extends AbstractResource{

    private static final long serialVersionUID = 10012190001002001L;

    private String  id;
    

    /**
     * Default Class Constructor.Available MediaTypes of Variants: TEXT_XML
     * @param context
     * @param request
     * @param response
     */
    public DataSet(Context context, Request request, Response response) {
        super(context, request, response);
        getVariants().add(new Variant(org.opentox.MediaTypes.DatasetMediaType.DATASET_ARFF));
        getVariants().add(new Variant(org.opentox.MediaTypes.DatasetMediaType.DATASET_DSD));
        getVariants().add(new Variant(org.opentox.MediaTypes.DatasetMediaType.DATASET_META_INF));
        getVariants().add(new Variant(org.opentox.MediaTypes.DatasetMediaType.DATASET_XRFF));
        //getVariants().add(new Variant(MediaType.TEXT_HTML));
        id=Reference.decode(request.getAttributes().get("id").toString());
    }

    /**
     *
     * @param variant
     * @return StringRepresentation
     */
    @Override
    public Representation represent(Variant variant)
    {
        RepresentationFactory factory = null;
        if (variant.getMediaType().equals(org.opentox.MediaTypes.DatasetMediaType.DATASET_ARFF)){
            factory = new RepresentationFactory(uploadDir+"/arff/"+id);
        }else if (variant.getMediaType().equals(org.opentox.MediaTypes.DatasetMediaType.DATASET_XRFF)){
            factory = new RepresentationFactory(uploadDir+"/xrff/"+id);
        }else if (variant.getMediaType().equals(org.opentox.MediaTypes.DatasetMediaType.DATASET_DSD)){            
            factory = new RepresentationFactory(uploadDir+"/dsd/"+id);
        }else if (variant.getMediaType().equals(org.opentox.MediaTypes.DatasetMediaType.DATASET_META_INF)){            
            factory = new RepresentationFactory(uploadDir+"/meta/"+id);
        }

  
            
            try {
                return new StringRepresentation(factory.getString().toString(), variant.getMediaType());
            } catch (FileNotFoundException ex) {
                Logger.getLogger(DataSet.class.getName()).log(Level.SEVERE, null, ex);
                return new StringRepresentation(ex.getMessage(),MediaType.TEXT_PLAIN);
            } catch (IOException ex) {
                Logger.getLogger(DataSet.class.getName()).log(Level.SEVERE, null, ex);
                return new StringRepresentation(ex.getMessage(),MediaType.TEXT_PLAIN);
            }

            
        }

    

    @Override
    public boolean allowPost(){
        return false;
    }

    /**
     * Allow clients to delete datasets.
     * @return true
     */
    @Override
    public boolean allowDelete(){
        return true;
    }

    @Override
    public void removeRepresentations() {
        getResponse().setEntity("DataSet deleted!\n", MediaType.ALL);
        logger.info(getRequest().getClientInfo().getAddress()+" deleted the dataset "+id);
        File file1 = new File(arffDir+"/"+id);
        File file2 = new File(dsdDir+"/"+id);
        File file3 = new File(scaledDir+"/"+id);
        File file4 = new File(metaDir+"/"+id);
        File file5 = new File(rangeDir+"/"+id);
        File file6 = new File(xrffDir+"/"+id);
        file1.delete();
        file2.delete();
        file3.delete();
        file4.delete();
        file5.delete();
        file6.delete();
    }

}// End of class
