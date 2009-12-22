package org.opentox.database;


import java.sql.*;
import java.util.logging.Level;
import org.opentox.Applications.OpenToxApplication;


/**
 * This is a class to manage access to the database behind the services. 
 * In this database one finds 3 tables. The first one contains information about the trained
 * models such as their internal id, their URI (globally recognizable) and the URI
 * of the algorithm that was used to train every model. The second table contains
 * information about which is the next id to be assigned to a new model and the third
 * one contains authorization/authentication information such as the user name, the
 * password and the priviledged assigned to each user.
 *
 * @author OpenTox - http://www.opentox.org
 * @author Kolotouros Dimitris
 * @author Sopasakis Pantelis
 */
public class InHouseDB implements DataBaseAccess {

    

    private static Driver myDriver = null;

    /**
     * Static Connection to the databse.
     */
    protected static Connection connection = null;

    /**
     * Database Constructor.
     * Connects to the existing database or creates a new one if database doesn't exist.
     */
    public InHouseDB() {
        loadDriver();
        getConnection();
        loadTables();
    }

    /**
     * Loads the Driver that is used to establish a new connection.
     */
    private void loadDriver() {


        try {
            myDriver = (Driver) Class.forName(DRIVER).newInstance();
        } catch (InstantiationException ex) {
            OpenToxApplication.opentoxLogger.log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            OpenToxApplication.opentoxLogger.log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            OpenToxApplication.opentoxLogger.log(Level.SEVERE, null, ex);
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
