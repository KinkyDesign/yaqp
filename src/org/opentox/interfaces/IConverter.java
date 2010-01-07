package org.opentox.interfaces;

import java.io.File;
import java.io.InputStream;
import weka.core.Instances;

/**
 *
 * @author OpenTox - http://www.opentox.org
 * @author Sopasakis Pantelis
 * @author Sarimveis Harry
 */
public interface IConverter {

    /**
     * Converts an Instances object into a DSD file.
     * TODO: Change this method into:
     * <code>
     * public abstract void convert(final Instances instances, OutputStream dsdFile);
     * </code>
     * So that it can handle URIs too.
     * @param instances
     * @param dsdFile
     */
    public abstract void convert(final Instances instances, File dsdFile);

    /**
     * Creates an Instances object using a DSD file.
     * @param dsdFile
     * @param instances
     */
    public abstract void convert(final File dsdFile, Instances instances);

    /**
     * Using the InputStream provided by a remote or local resource (file, URL etc)
     * produces an Instances object accepted by weka.
     * @param input_RDF_file InputStream From some RDF resource
     * @param instances The produces instances object.
     */
    public abstract void convert(final InputStream input_RDF_file, Instances instances);


}
