package org.opentox.auth;

/**
 *
 * @author chung
 */
public interface AuthorizationHierarchy extends Comparable<AuthorizationHierarchy> {

    public int compareTo(AuthorizationHierarchy other);
    public String getLevel();
}
