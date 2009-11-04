/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.opentox.database;

/**
 *
 * @author chung
 */
public interface AuthorizationHierarchy  {

    public int compareTo(AuthorizationHierarchy other);
    public String getLevel();
}
