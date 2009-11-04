/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.opentox.util.converters;

import java.io.File;
import weka.core.Instances;

/**
 *
 * @author OpenTox - http://www.opentox.org
 * @author Sopasakis Pantelis
 * @author Sarimveis Harry
 */
public abstract class AbstractConverter {
    private static final long serialVersionUID = -828374928532234117L;

    public abstract void convert(final Instances instances, File dsdFile);
    public abstract void convert(final File dsdFile, Instances instances);
}
