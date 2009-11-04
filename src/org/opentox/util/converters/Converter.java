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
public class Converter extends AbstractConverter{

    private static final long serialVersionUID = 8525170373664066039L;

    
    @Override
    public void convert(final Instances instances, File dsdFile) {
        
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void convert(final File dsdFile, Instances instances) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
