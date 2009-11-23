package org.opentox.Resources.Algorithms;

/**
 *
 * @author OpenTox - http://www.opentox.org
 * @author Sopasakis Pantelis
 * @author Kolotouros Dimitris
 * @author Sarimveis Harry
 */
public enum AlgorithmEnum {
    /**
     * Support Vector Machines Regression Algorithm.
     */
    svm("svm"),
    /**
     * Support Vector Machines Classification Algorithm.
     */
    svc("svc"),
    /**
     * Multiple Linear Regression Algorithm.
     */
    mlr("mlr"),
    /**
     * Unknown Algorithm.
     */
    unknown("unknown");

    /**
     * The name of the algorithm.
     */
    private final String name;

    /**
     * Private Constructor.
     * @param name
     */
    private AlgorithmEnum(String name) {
        this.name=name;
    }

    /**
     * Returns the name of the algorithm.
     * @return
     */
    public String getAlgorithmName(){
        return name;
    }


    /**
     * Returns the AlgorithmEnum enumeration element which has a certain name.
     * If the given name does not correspond to a registered algorithm, it returns
     * {@link org.opentox.Resources.Algorithms.AlgorithmEnum#UNKNOWN}
     * @param name
     * @return
     */
    public static AlgorithmEnum getAlgorithmEnum(String name){
        try{
            return valueOf(name);
        }catch(IllegalArgumentException ex){
           return unknown;
        }
    }
    


}
