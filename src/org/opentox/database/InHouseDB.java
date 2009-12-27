package org.opentox.database;

import java.sql.*;
import java.util.logging.Level;
import org.opentox.OpenToxApplication;

/**
 * This is a class to manage access to the database behind the services. This
 * class adopts the Singleton Design Pattern (see
 * <a href="http://www.javaworld.com/javaworld/jw-04-2003/jw-0425-designpatterns.html?page=1">this
 * article</a> for details ). Only one object of this class can be created! The access point
 * pf this class is {@link InHouseDB#INSTANCE } and is the only object that can be created.
 * The contructor of InHouseDB is private and cannot be accessed from other classes and
 * what is more, InHouseDB cannot be subclassed. The interface {@link  DataBaseAccess } provides
 * an API for classes used to access certain tables of the database such as the Models' and
 * the Users' ones. 
 *
 * @author OpenTox - http://www.opentox.org/
 * @author Sopasakis Pantelis
 * @author Sarimveis Harry
 * @version 1.3.3 (Last update: Dec 28, 2009)
 */
public final class InHouseDB implements DataBaseAccess {

    /**
     * Static Connection to the databse.
     */
    protected static Connection connection = null;
    /**
     *
     */
    private static InHouseDB instanceOfThis = null;
    /**
     * Thread-safe instantiation of the database through a final
     * instance of it.
     */
    public final static InHouseDB INSTANCE = getInstance();

    /**
     * Database Constructor.
     * Connects to the existing database or creates a new one if database doesn't exist.
     */
    private InHouseDB() {
        loadDriver();
        getConnection();
        loadTables();
    }

    private static InHouseDB getInstance() {
        if (instanceOfThis == null) {
            instanceOfThis = new InHouseDB();
        }
        return instanceOfThis;
    }

    /**
     * Loads the Driver that is used to establish a new connection.
     */
    private void loadDriver() {


        try {
            Driver myDriver = (Driver) Class.forName(DRIVER).newInstance();
            assert (myDriver.jdbcCompliant());
        } catch (InstantiationException ie) {
            OpenToxApplication.opentoxLogger.log(Level.SEVERE, null, ie);
        } catch (IllegalAccessException iae) {
            OpenToxApplication.opentoxLogger.log(Level.SEVERE, null, iae);
        } catch (ClassNotFoundException cnf) {
            OpenToxApplication.opentoxLogger.log(Level.SEVERE, null, cnf);
        } catch (AssertionError ae){
            OpenToxApplication.opentoxLogger.log(Level.SEVERE, null, ae);
        }
    }

    /**
     * Establishes a connection to the database.
     */
    private void getConnection() {

        try {
            OpenToxApplication.opentoxLogger.info("Attempting connection to " + DATABASENAME);
            connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
            OpenToxApplication.opentoxLogger.info("Successfully connected to " + DATABASENAME);
        } catch (SQLException e) {
            if (e.getErrorCode() == 40000) {
                createDataBase();
            } else {
                OpenToxApplication.opentoxLogger.log(Level.SEVERE, null, e);
            }
        }
    }

    /**
     * Load the tables in the database.
     * If one or more tables do not exist, they are created ab-initio.
     */
    private void loadTables() {
        DatabaseMetaData md = null;
        ResultSet rs = null;
        Statement stmt = null;
        Boolean exists1 = false,
                exists2 = false,
                exists3 = false;

        try {
            md = connection.getMetaData();

            rs = md.getTables(null, null, "%", null);

            while (rs.next() && !(exists1 && exists2 && exists3)) {
                if (rs.getString(3).equals(ModelsDB.MODELS_STACK_TABLE)) {
                    exists1 = true;
                }
                if (rs.getString(3).equals(ModelsDB.MODEL_INFO_TABLE)) {
                    exists2 = true;
                }
                if (rs.getString(3).equals(UsersDB.USER_ACCOUNTS_TABLE)) {
                    exists3 = true;
                }
            }


            if (!exists1) {
                ModelsDB.createModelsStackTable();
            }
            if (!exists2) {
                ModelsDB.createModelInfoTable();
            }
            if (!exists3) {
                UsersDB.createUsersTable();
            }


            stmt = connection.createStatement();
            rs = stmt.executeQuery("SELECT * FROM " + ModelsDB.MODELS_STACK_TABLE);
            while (rs.next()) {
                ModelsDB.modelsStack = rs.getInt("STACK");
            }

        } catch (SQLException ex) {
            OpenToxApplication.opentoxLogger.log(Level.SEVERE, null, ex);
        }
    }

    /**
     * If the database does not exist, it is created.
     */
    private static void createDataBase() {
        OpenToxApplication.opentoxLogger.info("CREATING DATABASE :" + DATABASENAME);
        try {
            connection = DriverManager.getConnection(DB_URL + ";create=true", USER, PASSWORD);

        } catch (SQLException ex) {
            OpenToxApplication.opentoxLogger.severe("InHouseDB Exception with error code :" + ex.getErrorCode() + "\n"
                    + "Details: " + ex.getMessage());
        }
    }
    


}// end of class

