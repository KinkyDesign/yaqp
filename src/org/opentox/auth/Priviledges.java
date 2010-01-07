package org.opentox.auth;

import org.opentox.interfaces.IAuthorizationHierarchy;



/**
 * Defines different three levels of authorization. These are user/simple,
 * user/priviledged and user/admin.
 * @author OpenTox - http://www.opentox.org
 * @author Sopasakis Pantelis
 * @author Kolotouros Dimitris
 * @author Sarimveis Harry
 */
public final class Priviledges implements IAuthorizationHierarchy {

    /**
     * Constructor.
     * @param level
     */
    public Priviledges(String level) {
        this.level = level;        
    }

    /**
     * User priviledges level.
     * This can be one of "guest", "user/simple", "user/priviledged" or "user/admin"
     */
    private String level;

    /**
     * Guest Users with no priviledges.
     */
    public static Priviledges GUEST = new Priviledges("guest");   

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
    public static Priviledges ADMIN = new Priviledges("admin/admin");

    


    /**
     * Returns the identifier of a Priviledge. e.g. Priviledges.ADMIN.getLevel() returns
     * the String "user/admin"
     * @return the error level.
     */
    public String getLevel(){
        return level;
    }

    /**
     * Compares two authorization levels.<br/>
     * Implements the ordering:
     * <pre>"guest" &lt; "user/simple" &lt; "user/priviledged" &lt; "user/admin"</pre>
     * Returns 1 if a.compareTo(b) means that a is more priviledged that b,
     * Return 0 if the two Priviledges are equal and -1 else. The method returns
     * -2 if no decission is taken...
     * @param other
     * @return integer used for the comparison.
     */
    @Override
    public int compareTo(IAuthorizationHierarchy other) {

        if (Priviledges.GUEST.getLevel().equals(level)){ /** this=guest **/
            if (other.getLevel().equals(level)){
                return 0; // this == other
            }else{
                return -1; // this < other
            }
        }else if (Priviledges.USER.getLevel().equals(level)){/** this=user **/
            if (other.getLevel().equals(USER.getLevel())){
                return 0;   // this == other
            }else if (other.getLevel().equals(GUEST.getLevel())){
                return 1;   // this > other
            }else if (other.getLevel().equals(PRIVILEDGED_USER.getLevel())){
                return -1;  // this < other
            }else if (other.getLevel().equals(ADMIN.getLevel())){
                return -1;  // this< other
            }else{
                return -2;  // ????
            }
        }else if (Priviledges.PRIVILEDGED_USER.getLevel().equals(level)){/** this=priviledged **/
            if (other.getLevel().equals(PRIVILEDGED_USER.getLevel())){
                return 0;   // this == other
            }else if (other.getLevel().equals(GUEST.getLevel())){
                return 1;   // this > other
            }else if (other.getLevel().equals(USER.getLevel())){
                return 1;   // this > other
            } else if (other.getLevel().equals(ADMIN.getLevel())){
                return -1;  // this > other
            }else{
                return -2;  // ????
            }
        }else if (Priviledges.ADMIN.getLevel().equals(level)){
            if (other.getLevel().equals(ADMIN.getLevel())){
                return 0;   // this == other
            }else{
                return 1;   // this > other
            }
        }else{
            return -2;      // ????
        }

    }


    

    
}
