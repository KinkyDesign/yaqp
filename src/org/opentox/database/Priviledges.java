/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.opentox.database;

import sun.awt.SunToolkit.InfiniteLoop;

/**
 * Defines different three levels of authorization. These are user/simple,
 * user/priviledged and user/admin.
 * @author OpenTox - http://www.opentox.org
 * @author Sopasakis Pantelis
 * @author Kolotouros Dimitris
 * @author Sarimveis Harry
 */
public class Priviledges implements AuthorizationHierarchy{

    /**
     * Private Constructor.
     * @param level
     */
    private Priviledges(String level) {
        this.level = level;        
    }

    /**
     * User priviledges level.
     * This can be one of "user/simple", "user/priviledged" or "user/admin"
     */
    private String level;

   

    /**
     * Simple Users.
     */
    public static Priviledges USER = new Priviledges("user/simple");

    /**
     * Priviledged Users.
     */
    public static Priviledges PRIVILEDGED_USER = new Priviledges("user/priviledged");

    /**
     * Administrators.
     */
    public static Priviledges ADMIN = new Priviledges("user/admin");

    


    
    public String getLevel(){
        return level;
    }

    /**
     * Compares two authorization levels.<br/>
     * Implements the ordering:
     * <pre>"user/simple" &lt; "user/priviledged" &lt; "user/admin"</pre>
     * Returns 1 if a.compareTo(b) means that a is more priviledged that b,
     * Return 0 if the two Priviledges are equal and -1 else. The method returns
     * -2 if no decission is taken...
     * @param other
     * @return
     */
    @Override
    public int compareTo(AuthorizationHierarchy other) {
       /*** If this is USER then any other user can be equal or
            or greater to this.
        **/
       if (level.equalsIgnoreCase(Priviledges.USER.getLevel())){
           if (other.getLevel().equalsIgnoreCase(level)){
               return 0;
           }else{
               return -1;
           }
       /*** If this is the PRIVILEDGED USER **/
       }if (level.equalsIgnoreCase(Priviledges.PRIVILEDGED_USER.getLevel())){
           if(PRIVILEDGED_USER.getLevel().equalsIgnoreCase(other.getLevel())){
               return 0;
           }else if (USER.getLevel().equalsIgnoreCase(other.getLevel())){
               return 1;
           }else{
               return -1;
           }
       /*** If this is the ADMIN user then any other user is equal
            or less priviledged that this one.
        **/
       } if (level.equalsIgnoreCase(Priviledges.ADMIN.getLevel())){
           if (Priviledges.ADMIN.getLevel().equalsIgnoreCase(other.getLevel())){
               return 0;
           }else{
               return 1;
           }
       }else{
           return -2;
       }

    }

   

    
}
