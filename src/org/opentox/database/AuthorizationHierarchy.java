package org.opentox.database;

/**
 *
 * @author chung
 */
public interface AuthorizationHierarchy  {

    public int compareTo(AuthorizationHierarchy other);
    public String getLevel();
}
