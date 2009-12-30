package org.opentox.algorithm.trainer;

import java.util.concurrent.Callable;
import org.opentox.error.ErrorSource;
import org.restlet.representation.Representation;

/**
 *
 * @author chung
 */
public class CallableTrainer extends ErrorSource implements Callable<Representation>{

    private AbstractTrainer trainer = null;

    public CallableTrainer(AbstractTrainer trainer){
        this.trainer = trainer;
    }

    /**
     * Returns the Represe
     * @return
     * @throws Exception
     */
    public Representation call() throws Exception {
        return trainer.train();
    }

}
