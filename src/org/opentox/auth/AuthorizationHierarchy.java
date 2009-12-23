package org.opentox.auth;

/**
 *
 * @author chung
 */
public interface AuthorizationHierarchy  {

    public int compareTo(AuthorizationHierarchy other);
    public String getLevel();
}
