package org.opentox.database;

import java.lang.annotation.Inherited;
import org.opentox.Server;

/**
 * Database Interface.
 * @author Sopasakis Pantelis
 */
public interface DataBaseAccess {
    
    final static String DRIVER = "org.apache.derby.jdbc.EmbeddedDriver";
    final static String DATABASENAME = Server.__DATABASE_NAME;
    final static String DB_URL = "jdbc:derby:" + DATABASENAME;
    final static String USER = "itsme";
    final static String PASSWORD = "letmein";


    @Inherited
    public @interface CreateTable{
        String[] value() default {};
    }

    @Inherited
    public @interface Registration{

    }

    @Inherited
    public @interface Removal{
        
    }


}
