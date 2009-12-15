package org.opentox.database;

/**
 * Database Interface.
 * @author Sopasakis Pantelis
 */
public interface DBase {
    final static String DRIVER = "org.apache.derby.jdbc.EmbeddedDriver";
    final static String DATABASENAME = "modelsDb";
    final static String DB_URL = "jdbc:derby:" + DATABASENAME;
    final static String USER = "itsme";
    final static String PASSWORD = "letmein";
}
