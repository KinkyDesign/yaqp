package org.opentox.database;

import java.lang.annotation.Inherited;
import org.opentox.Server;

/**
 * Database Interface.
 * @author Sopasakis Pantelis
 */
public interface DataBaseAccess {
    
    final static String DRIVER = "org.apache.derby.jdbc.EmbeddedDriver";
    final static String DATABASENAME = Server.__DATABASE_NAME_;
    final static String DB_URL = "jdbc:derby:" + DATABASENAME;
    
    

    @Inherited public @interface CreateTable{
        String[] value() default {};
    }

    @Inherited public @interface Registration{

    }

    @Inherited public @interface Removal{   
    }


}
