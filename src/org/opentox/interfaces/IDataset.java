/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.opentox.interfaces;

import org.opentox.ontology.rdf.Dataset;
import weka.core.Instances;

/**
 *
 * @author chung
 */
public interface IDataset extends IProne2Error{

    /**
     * This method is used to encapsulate the data of the RDF document in a
     * weka.core.Instances object which can be used to create Regression and
     * classification models using weka algorithms.
     *
     * <!-- SCOPE -->
     * <p>
     * <b>Description:</b><br/>
     * This method was developed to
     * generate datasets (as Instances) in order to be used as input to training
     * algorithms of weka.
     * </p>
     *
     * <!-- MORE INFO -->
     * <p>
     * <b>Characteristics of generated Instances:</b><br/>
     * The relation name of the generated instances is the same with the identifier of
     * the dataset. If no identifier is available, then this is set to some arbitraty URI.
     * If <tt>isClassNominal</tt> is set to false, the class attribute is not defined in
     * this method but it can be set externally (from the method that calls getWekaDataset).
     * If <tt>isClassNominal</tt> is set to true, the target of the datset is defined by the
     * first agument of the method (String target).<br/>
     * The attributes of the Instances object coincides with the set of features of the
     * dataset in RDF format.
     * </p>
     * @param target URI of the target feature of the dataset. It is optional (you
     * may leave it null) if you are going to use the Instances for regression models
     * and isClassNominal is set to false, otherwise you have to specify a valid feature
     * URI.
     * @param isClassNominal Set to true if the class attribute should be considered to
     * be nominal.
     * @return The Instances object which encapsulates the data in the RDF document.
     */
    Instances getInstaces(String target, boolean isClassNominal) throws Exception;

    /**
     * Similar to {@link org.opentox.ontology.rdf.Dataset#getInstaces(java.lang.String, boolean) }
     * but the generated Instances is constructed with respect to a certain model.
     * @param model_id
     * @return Instances for prediction using a given model.
     */
    Instances getInstances(String model_id);

    /**
     *
     * @param predictedData
     * @return Populated Dataset.
     */
    Dataset populateDataset(Instances predictedData);

}
