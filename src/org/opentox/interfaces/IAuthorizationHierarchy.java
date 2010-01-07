package org.opentox.interfaces;

/**
 *
 * @author chung
 */
public interface IAuthorizationHierarchy extends Comparable<IAuthorizationHierarchy> {

    public int compareTo(IAuthorizationHierarchy other);
    public String getLevel();
}
