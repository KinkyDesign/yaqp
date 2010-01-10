package org.opentox.algorithm;

import java.util.Iterator;
import java.util.NoSuchElementException;
import org.opentox.interfaces.IAlgorithm;

/**
 * Enumeration of all available algorithms on the server. If one needs to extend
 * the services to support one more algorithm,this is the place to start.
 * @author OpenTox - http://www.opentox.org/
 * @author Sopasakis Pantelis
 * @author Sarimveis Harry
 * @version 1.3.3 (Last update: Dec 20, 2009)
 */
public enum AlgorithmEnum implements Iterable<String> {

    

    /**
     * Support Vector Machines Regression Algorithm.
     */
    svm("svm", new SvmAlgorithm()),
    /**
     * Support Vector Machines Classification Algorithm.
     */
    svc("svc", new SvcAlgorithm()),
    /**
     * Multiple Linear Regression Algorithm.
     */
    mlr("mlr", new MlrAlgorithm()),
    /**
     * Data clean up service
     */
    cleanup("cleanup", new CleanUpAlgorithm()),
    /**
     * Unknown Algorithm.
     */
    unknown("unknown", null);
    /**
     * The name of the algorithm.
     */
    private final String name;

    private IAlgorithm algorithm;

    /**
     * Private Constructor. Constructs an AlgorithmEnum object of a given name.
     * @param name
     */
    private AlgorithmEnum(String name, IAlgorithm algorithm) {
        this.name = name;
        this.algorithm = algorithm;
    }

    /**
     * Returns the name of the algorithm.
     * @return the formal name of the algorithm.
     */
    public String getAlgorithmName() {
        return name;
    }

    public IAlgorithm getAlgorithm(){
        return algorithm;
    }

    /**
     * Returns the AlgorithmEnum enumeration element which has a certain name.
     * If the given name does not correspond to a registered algorithm, it returns
     * {@link AlgorithmEnum#unknown }
     * @param name Name of an algorithm (case sensitive)
     * @return the AlgorithmEnum object for the given name.
     */
    public static AlgorithmEnum getAlgorithmEnum(String name) {
        try {
            return valueOf(name);
        } catch (IllegalArgumentException ex) {
            return unknown;
        }
    }


    private class IteratorImpl implements Iterator<String> {

        private int count = -1;
        private AlgorithmEnum[] values = AlgorithmEnum.values();
        private final int SIZE = values.length - 1;

        public boolean hasNext() {
            return count < SIZE - 1;
        }

        public String next() {
            if (count == SIZE - 1) {
                throw new NoSuchElementException();
            }
            count++;
            return values[count].getAlgorithmName();
        }

        public void remove() {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

    /**
     * Iterator over all algorithm names.
     * @return Iterator.
     */
    public Iterator<String> iterator() {
        return new IteratorImpl();
    }

    public static Iterator<String> getIterator() {
        return AlgorithmEnum.mlr.iterator();
    }

    public static void main(String[] args){
        Iterator<String > it = AlgorithmEnum.getIterator();
        while (it.hasNext()){
            System.out.println(it.next());
        }

    }


}
