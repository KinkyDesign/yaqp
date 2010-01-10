package org.opentox.interfaces;

import java.util.Iterator;

/**
 * An extension of the well-known interface Iterator of java to cope with the
 * need of out iterators to "close" some objects generated internally. An extra
 * method is included.
 * @author Sopasakis Pantelis
 */
public interface Jterator<E extends Object> extends Iterator<E> {

    public void close();

}
