package org.opentox.Resources.Algorithms;

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;

/**
 *
 * @author OpenTox - http://www.opentox.org/
 * @author Sopasakis Pantelis
 * @author Sarimveis Harry
 * @version 1.3.3 (Last update: Dec 20, 2009)
 */
public class ConstantParameters {

    public static final AlgorithmParameter<String> TARGET = TARGET("http://someserver.com/feature/100");
    public static final AlgorithmParameter<String> KERNEL = KERNEL("rbf");
    public static final AlgorithmParameter<Double> COST = COST(10.0);
    public static final AlgorithmParameter<Double> EPSILON = EPSILON(0.10);
    public static final AlgorithmParameter<Double> GAMMA = GAMMA(1.0);
    public static final AlgorithmParameter<Double> COEFF0 = COEFF0(0.0);
    public static final AlgorithmParameter<Integer> DEGREE = DEGREE((int) 3);
    public static final AlgorithmParameter<Double> TOLERANCE = TOLERANCE(0.0001);
    public static final AlgorithmParameter<Integer> CACHESIZE = CACHESIZE((int)50);

    public static final AlgorithmParameter<String> TARGET(String value) {
        return new AlgorithmParameter<String>("target",
                XSDDatatype.XSDanyURI, value, "mandatory");
    }

    public static final AlgorithmParameter<String> KERNEL(String value) {
        return new AlgorithmParameter<String>("kernel",
                XSDDatatype.XSDstring, value, "optional");
    }

    public static final AlgorithmParameter<Double> COST(double value) {
        return new AlgorithmParameter<Double>("cost", XSDDatatype.XSDdouble,
                10.0, "optional");
    }

    public static final AlgorithmParameter<Double> EPSILON(double value) {
        return new AlgorithmParameter<Double>("epsilon", XSDDatatype.XSDdouble, value, "optional");
    }

    public static final AlgorithmParameter<Double> GAMMA(double value) {
        return new AlgorithmParameter<Double>("gamma", XSDDatatype.XSDdouble, value, "optional");
    }

    public static final AlgorithmParameter<Double> COEFF0(double value) {
        return new AlgorithmParameter<Double>("coeff0", XSDDatatype.XSDdouble, value, "optional");
    }

    public static final AlgorithmParameter<Integer> DEGREE(int value) {
        return new AlgorithmParameter<Integer>("degree",
                XSDDatatype.XSDpositiveInteger, value, "optional");
    }

    private static final AlgorithmParameter<Double> TOLERANCE(double value) {
        return new AlgorithmParameter<Double>("tolerance",
                XSDDatatype.XSDdouble, value, "optional");
    }

    private static final AlgorithmParameter<Integer> CACHESIZE(int value) {
        return new AlgorithmParameter<Integer>("cacheSize",
                XSDDatatype.XSDdouble, value, "optional");
    }
}
