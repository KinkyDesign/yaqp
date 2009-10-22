package org.opentox.database;


import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.restlet.data.ReferenceList;
import org.opentox.Applications.OpenToxApplication;

/**
 *
 * @author OpenTox - http://www.opentox.org
 * @author tartoufo1973
 */
public class InHouseDB {

    private int modelsStack;
    private final static String DRIVER = "org.apache.derby.jdbc.EmbeddedDriver";
    private final static String DATABASENAME = "modelsDb";
    private final static String URL = "jdbc:derby:" + DATABASENAME;
    private final static String USER = "itsme";
    private final static String PASSWORD = "letmein";
    private final static String MODELS_STACK_TABLE = "MODELS_STACK";
    private final static String MODEL_INFO_TABLE = "MODELS";
    private final static String USER_ACCOUNTS_TABLE = "USERS";
    private static Driver myDriver = null;
    private static Connection connection = null;

    /**
     *Database Constructor. Connects to the existing database or creates a new one if database doesn't exist.
     */
    public InHouseDB() {
        loadDriver();
        getConnection();
        loadTables();
    }

    private void loadDriver() {
        try {
            try {
                myDriver = (Driver) Class.forName(DRIVER).newInstance();
            } catch (InstantiationException ex) {
                OpenToxApplication.opentoxLogger.log(Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                OpenToxApplication.opentoxLogger.log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            OpenToxApplication.opentoxLogger.log(Level.SEVERE, null, ex);
        }
    }



    private void getConnection() {
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            if (e.getErrorCode() == 40000) {
                createDataBase();
            } else {
                OpenToxApplication.opentoxLogger.log(Level.SEVERE,null,e);
            }
        }
    }



    private void loadTables() {
        DatabaseMetaData md = null;
        ResultSet rs = null;
        Statement stmt = null;
        Boolean exists1 = false, 
                exists2 = false,
                exists3 = false;

        try {
            md = connection.getMetaData();
            try {
                rs = md.getTables(null, null, "%", null);
                try {
                    while (rs.next() && !(exists1 && exists2 && exists3)) {
                        if (rs.getString(3).equals(MODELS_STACK_TABLE)) {
                            exists1 = true;
                        }
                        if (rs.getString(3).equals(MODEL_INFO_TABLE)) {
                            exists2 = true;
                        }
                        if (rs.getString(3).equals(USER_ACCOUNTS_TABLE)){
                            exists3 = true;
                        }
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(InHouseDB.class.getName()).log(Level.SEVERE, null, ex);
                }

                if (!exists1) {
                    createModelsStackTable();
                }
                if (!exists2) {
                    createModelInfoTable();
                }
                if (!exists3){
                    createUsersTable();
                }

                try {
                    stmt = connection.createStatement();
                    rs = stmt.executeQuery("SELECT * FROM " + MODELS_STACK_TABLE);
                    while (rs.next()) {
                        modelsStack = rs.getInt("STACK");
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(InHouseDB.class.getName()).log(Level.SEVERE, null, ex);
                }
            } catch (SQLException ex) {
                Logger.getLogger(InHouseDB.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(InHouseDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    private static void createDataBase() {
        OpenToxApplication.opentoxLogger.info("CREATING DATABASE :"+DATABASENAME);
        try {
            connection = DriverManager.getConnection(URL + ";create=true", USER, PASSWORD);

        } catch (SQLException ex) {
            OpenToxApplication.opentoxLogger.severe("InHouseDB Exception with error code :"+ex.getErrorCode()+"\n" +
                    "Details: "+ex.getMessage());
        }
    }



    private static void createModelsStackTable() {
        System.out.println("Creating table : " + MODELS_STACK_TABLE);
        String CreateTable = "create table " + MODELS_STACK_TABLE + "(STACK INTEGER)";

        try {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate(CreateTable);
        } catch (SQLException ex) {
            OpenToxApplication.opentoxLogger.log(Level.SEVERE, null, ex);
        }

        String CreateValue = "insert into " + MODELS_STACK_TABLE + " (\"STACK\")\n" +
                "values (0)";
        try {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate(CreateValue);
        } catch (SQLException ex) {
            OpenToxApplication.opentoxLogger.log(Level.SEVERE, null, ex);
        }
    }

    private static void createModelInfoTable() {
        System.out.println("Creating table: " + MODEL_INFO_TABLE);
        String CreateTable = "create table " + MODEL_INFO_TABLE + "(" +
                "MODEL_ID INTEGER, " +
                "ALGORITHM_ID VARCHAR(150)," +
                "MODEL_URI VARCHAR(150))";

        try {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate(CreateTable);
        } catch (SQLException ex) {
            OpenToxApplication.opentoxLogger.log(Level.SEVERE, null, ex);
        }
    }


    private static void createUsersTable(){
        System.out.println("Creating table: " + USER_ACCOUNTS_TABLE);
        String CreateTable = "create table " + USER_ACCOUNTS_TABLE + "(" +
                "USER_NAME VARCHAR(20), " +
                "USER_PASSWORD VARCHAR(20)," +
                "AUTH VARCHAR(20))";
        try {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate(CreateTable);
        } catch (SQLException ex) {
            OpenToxApplication.opentoxLogger.log(Level.SEVERE, null, ex);
        }
    }

    private void IncreaseModelStack() {
        String increaseValue = "update " + MODELS_STACK_TABLE + " set STACK = " + (modelsStack + 1) +
                " where STACK = " + modelsStack;
        try {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate(increaseValue);
        } catch (SQLException ex) {
            OpenToxApplication.opentoxLogger.log(Level.SEVERE, null, ex);
        }
        modelsStack++;
    }

    public int getModelsStack() {
        return modelsStack;
    }

    /**
     * Registers a new trained model in database
     * @param AlgID URI of the algorithm used to train the model
     * @return New model's ID
     */
    public int registerNewModel(String AlgID) {
        int id = getModelsStack() + 1;
        String uri = org.opentox.Resources.AbstractResource.baseURI + "/model/" + id;
        String CreateValue = "insert into " + MODEL_INFO_TABLE + " values (" + id + ",'" + AlgID + "','" + uri + "')";
        //System.out.println(CreateValue);

        Statement stmt;
        try {
            stmt = connection.createStatement();
            stmt.executeUpdate(CreateValue);
            IncreaseModelStack();
        } catch (SQLException ex) {
            OpenToxApplication.opentoxLogger.log(Level.SEVERE, null, ex);
        }

        return id;

    }

    /**
     * TODO:
     *
     * @return
     */
    public ReferenceList getModelsAsReferenceList() {

        String SelectAllModels = "SELECT * FROM " + MODEL_INFO_TABLE;
        ResultSet rs = null;
        ReferenceList list = new ReferenceList();

        Statement stmt;
        try {
            stmt = connection.createStatement();
            rs = stmt.executeQuery(SelectAllModels);
            while (rs.next()) {
                list.add(rs.getString("MODEL_URI"));
            }
        } catch (SQLException ex) {
            OpenToxApplication.opentoxLogger.log(Level.SEVERE,null,ex);
        }
        return list;
    }

    public ReferenceList getClassificationModelsAsReferenceList() {
        return getReferenceListFromAlgId(
                org.opentox.Resources.AbstractResource.baseURI + "/algorithm/learning/classification/");
    }

    public ReferenceList getRegressionModelsAsReferenceList() {
        return getReferenceListFromAlgId(
                org.opentox.Resources.AbstractResource.baseURI + "/algorithm/learning/regression/");
    }

    /**
     * Returns a reference list for the models that where trained usign a
     * specific
     * @param algorithmId
     * @return
     */
    public ReferenceList getReferenceListFromAlgId(String algorithmId) {
        String SelectAllSvc = "SELECT * FROM " + MODEL_INFO_TABLE +
                " WHERE ALGORITHM_ID LIKE '%" + algorithmId + "%'";
        System.out.println(SelectAllSvc);
        ResultSet rs = null;
        ReferenceList list = new ReferenceList();

        Statement stmt;
        try {
            stmt = connection.createStatement();
            rs = stmt.executeQuery(SelectAllSvc);
            while (rs.next()) {
                list.add(rs.getString("MODEL_URI"));
                System.err.println(rs.getString("MODEL_URI"));
            }
        } catch (SQLException ex) {
            OpenToxApplication.opentoxLogger.log(Level.SEVERE, null, ex);
        }
        return list;
    }

    /**
     * For testing purposes only.
     * Is not used within the service.
     * @param args
     */
    public static void main(String[] args) {
        long before0 = java.lang.System.currentTimeMillis();
        InHouseDB dbcon = new InHouseDB();
        dbcon.getRegressionModelsAsReferenceList();
    }
}
