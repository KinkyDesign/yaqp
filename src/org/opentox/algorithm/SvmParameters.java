package org.opentox.algorithm;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO: Move this class.
 * Tuning Parameters for the SVM algorithm.
 * @author OpenTox - http://www.opentox.org/
 * @author Sopasakis Pantelis
 * @author Sarimveis Harry
 * @version 1.3.3 (Last update: Dec 28, 2009)
 */
public class SvmParameters {

    /**
     * The kernel used in the SVM model.
     * This can be rbf, linear, sigmoid or polynomial.
     */
    public String kernel = null;
    /**
     * The name of the target attribute which normally is the
     * URI of a feature definition.
     */
    public  String targetAttribute = null;
    /**
     * The degree of the polynomial kernel (when used).
     */
    public String degree = null;
    /**
     * The cahed memory used in model training.
     */
    public String cacheSize = null;
    /**
     * The Cost coefficient.
     */
    public String cost = null;
    /**
     * The parameter epsilon used in SVM models.
     */
    public String epsilon = null;
    /**
     * The kernel parameter gamma used in various kernel functions.
     */
    public String gamma = null;
    /**
     * The bias of the support vector model.
     */
    public String coeff0 = null;
    /**
     * The tolerance used in model training.
     */
    public String tolerance = null;

    /**
     * Returns the SVM parameters as a list of {@link AlgorithmParameter } objects.
     * @return list of algorithm parameters.
     */
    public List<AlgorithmParameter> getListOfParameters() {
        List<AlgorithmParameter> paramList = new ArrayList<AlgorithmParameter>();
        paramList.add(ConstantParameters.COEFF0(Double.parseDouble(coeff0)));
        paramList.add(ConstantParameters.COST(Double.parseDouble(cost)));
        paramList.add(ConstantParameters.DEGREE(Integer.parseInt(degree)));
        paramList.add(ConstantParameters.KERNEL(kernel));
        paramList.add(ConstantParameters.EPSILON(Double.parseDouble(epsilon)));
        paramList.add(ConstantParameters.GAMMA(Double.parseDouble(gamma)));
        paramList.add(ConstantParameters.TARGET(targetAttribute));
        return paramList;
    }
}
