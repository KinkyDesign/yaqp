package org.opentox.interfaces;

import java.lang.annotation.Inherited;
import org.opentox.Server;

/**
 * Database Interface.
 * @author Sopasakis Pantelis
 */
public interface IDataBaseAccess {
    
    final static String DRIVER = "org.apache.derby.jdbc.EmbeddedDriver";
    final static String DATABASENAME = Server.__DATABASE_NAME_;
    final static String DB_URL = "jdbc:derby:" + DATABASENAME;
    
    

    /**
     * Methods which are used to created a new table in the database.
     */
    @Inherited public @interface CreateTable{
        String[] value() default {};
    }

    /**
     * Methods which are used to add new entried in a table of the database are
     * annotated using this annotation interface.
     */
    @Inherited public @interface Registration{

    }

    @Inherited public @interface Removal{   
    }


}
