package org.opentox.algorithm;

/**
 * Enumeration of all available algorithms on the server. If one needs to extend
 * the services to support one more algorithm,this is the place to start.
 * @author OpenTox - http://www.opentox.org/
 * @author Sopasakis Pantelis
 * @author Sarimveis Harry
 * @version 1.3.3 (Last update: Dec 20, 2009)
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
     * Private Constructor. Constructs an AlgorithmEnum object of a given name.
     * @param name
     */
    private AlgorithmEnum(String name) {
        this.name=name;
    }

    /**
     * Returns the name of the algorithm.
     * @return the formal name of the algorithm.
     */
    public String getAlgorithmName(){
        return name;
    }


    /**
     * Returns the AlgorithmEnum enumeration element which has a certain name.
     * If the given name does not correspond to a registered algorithm, it returns
     * {@link AlgorithmEnum#unknown }
     * @param name Name of an algorithm (case sensitive)
     * @return the AlgorithmEnum object for the given name.
     */
    public static AlgorithmEnum getAlgorithmEnum(String name){
        try{
            return valueOf(name);
        }catch(IllegalArgumentException ex){
           return unknown;
        }
    }
    


}
