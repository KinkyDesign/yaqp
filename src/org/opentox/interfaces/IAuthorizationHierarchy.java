package org.opentox.interfaces;

/**
 * Defines an Hierarchy of authorization levels such as guest &lt; user &lt; priviledged
 * user &lt; administrator as these are defined in {@link org.opentox.auth.Priviledges }.
 * Extends the Comparable Interface and uses the method compareTo(IAuthorizationHierarchy other)
 * to define a relationship between the various access levels.
 * @author OpenTox - http://www.opentox.org/
 * @author Sopasakis Pantelis
 * @author Sarimveis Harry
 * @author Kolotouros Dimitris
 * @version 1.3.3 (Last update: Jan 11, 2009)
 */
public interface IAuthorizationHierarchy extends Comparable<IAuthorizationHierarchy> {


    /**
     * Compares two Access Levels and returns an integer that describes a
     * comparison relationship between them. 0 stands for equality, if a.compareTo(b)
     * returns 1 it should be interpreted as a &gt; b while -1 should mean a &lt; b.
     * @param other The Access Level ({@link IAuthorizationHierarchy } ) to compare with.
     * @return Comparison Flag.
     */
    public int compareTo(IAuthorizationHierarchy other);

    /**
     * The priviledges as a string, i.e. "user/simple".
     * @return priviledges as a string.
     */
    public String getLevel();
}
