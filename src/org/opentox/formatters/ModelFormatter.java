package org.opentox.formatters;

import com.hp.hpl.jena.ontology.OntModel;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.opentox.MediaTypes.OpenToxMediaType;
import org.opentox.Resources.AbstractResource.Directories;
import org.opentox.ontology.Namespace;
import org.opentox.util.RepresentationFactory;
import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;

/**
 * Reads a stored or online model (in RDF format) and returns it in a prefered
 * MIME type (format).
 * @author Sopasakis Pantelis
 */
public class ModelFormatter extends AbstractModelFormatter {

    public ModelFormatter(int model_id){
        super(model_id);
    }

    @Override
    public StringRepresentation getStringRepresentation(MediaType mediatype) {
        Representation rep = null;
        if (    (MediaType.TEXT_PLAIN.equals(mediatype))||
                (MediaType.APPLICATION_RDF_XML.equals(mediatype))||
                (MediaType.APPLICATION_RDF_TURTLE.equals(mediatype))||
                (OpenToxMediaType.TEXT_TRIPLE.equals(mediatype))||
                (OpenToxMediaType.TEXT_N3.equals(mediatype))||
                (MediaType.TEXT_HTML.equals(mediatype))
        ){
            String Lang="RDF/XML";
                    if (MediaType.APPLICATION_RDF_TURTLE.equals(mediatype)){
                        Lang="TTL";
                    }else if (OpenToxMediaType.TEXT_TRIPLE.equals(mediatype)){
                        Lang="N-TRIPLE";
                    }else if (OpenToxMediaType.TEXT_N3.equals(mediatype)){
                        Lang="N3";
                    }
            try {
                FileInputStream in = new FileInputStream(Directories.modelRdfDir + "/" + model_id);
                try {

                    OntModel jenaModel = Namespace.createModel();
                    jenaModel.read(in, null);
                    ByteArrayOutputStream str = new ByteArrayOutputStream();
                    jenaModel.write(str, Lang);
                    rep = new StringRepresentation(str.toString());
                } catch (Exception ex) {
                    Logger.getLogger(ModelFormatter.class.getName()).log(Level.SEVERE, null, ex);
                }
            } catch (FileNotFoundException ex) {
                rep = new StringRepresentation("Model not Found!\n");
            }
        }else if (MediaType.APPLICATION_XML.equals(mediatype)){
            RepresentationFactory rf = new RepresentationFactory(Directories.modelPmmlDir+"/"+model_id);
            try {
                rep = new StringRepresentation(rf.getString().toString(), mediatype);
            } catch (FileNotFoundException ex) {
                rep = new StringRepresentation("Model not Found!\n");
            } catch (IOException ex) {
                Logger.getLogger(ModelFormatter.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return (StringRepresentation) rep;
    }

}
