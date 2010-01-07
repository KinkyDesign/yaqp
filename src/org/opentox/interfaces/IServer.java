package org.opentox.interfaces;

/**
 *
 * @author chung
 */
public interface IServer extends Runnable {

    /**
     * Gracefully terminates the server!
     * @throws Exception
     */
    void shutdown() throws Exception;

}
