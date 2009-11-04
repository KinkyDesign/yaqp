package org.opentox.database;


import java.io.IOException;
import java.sql.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import org.restlet.data.ReferenceList;
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
public class InHouseDB {

    private int modelsStack;
    private final static String DRIVER = "org.apache.derby.jdbc.EmbeddedDriver";
    private final static String DATABASENAME = "modelsDb";
    private final static String DB_URL = "jdbc:derby:" + DATABASENAME;
    private final static String USER = "itsme";
    private final static String PASSWORD = "letmein";
    private final static String MODELS_STACK_TABLE = "MODELS_STACK";
    private final static String MODEL_INFO_TABLE = "MODELS";
    private final static String USER_ACCOUNTS_TABLE = "USERS";
    private static Driver myDriver = null;
    private static Connection connection = null;

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


    /**
     * Establishes a connection to the database.
     */
    private void getConnection() {
        
        try {
            OpenToxApplication.opentoxLogger.info("Attempting connection to "+DATABASENAME);
            connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
            OpenToxApplication.opentoxLogger.info("Successfully connected to "+DATABASENAME);
        } catch (SQLException e) {
            if (e.getErrorCode() == 40000) {
                createDataBase();
            } else {
               OpenToxApplication.opentoxLogger.log(Level.SEVERE,null,e);
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
                    OpenToxApplication.opentoxLogger.log(Level.SEVERE, null, ex);
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
                    OpenToxApplication.opentoxLogger.log(Level.SEVERE, null, ex);
                }
            } catch (SQLException ex) {
                OpenToxApplication.opentoxLogger.log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            OpenToxApplication.opentoxLogger.log(Level.SEVERE, null, ex);
        }
    }


    /**
     * If the database does not exist, it is created.
     */
    private static void createDataBase() {
        OpenToxApplication.opentoxLogger.info("CREATING DATABASE :"+DATABASENAME);
        try {
            connection = DriverManager.getConnection(DB_URL + ";create=true", USER, PASSWORD);

        } catch (SQLException ex) {
            OpenToxApplication.opentoxLogger.severe("InHouseDB Exception with error code :"+ex.getErrorCode()+"\n" +
                    "Details: "+ex.getMessage());
        }
    }


    /**
     * Creates the table MODELS_STACK which contains information about the id
     * of the next model to be trained.
     */
    private static void createModelsStackTable() {
        OpenToxApplication.opentoxLogger.info("Creating Table : "+MODELS_STACK_TABLE);
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


    /**
     * Generates the table that contains information about models.
     */
    private static void createModelInfoTable() {
        OpenToxApplication.opentoxLogger.info("Creating table: " + MODEL_INFO_TABLE);
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


    /**
     * Generates the table which contains a/a data.
     */
    private static void createUsersTable(){
        OpenToxApplication.opentoxLogger.info("Creating table: " + USER_ACCOUNTS_TABLE);
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


    /**
     * This method is used to add a new user to the USERS table. The username,
     * the password
     * @param UserName
     * @param PassWord
     * @param Priviledges
     */
    public static void addUser(String UserName, String PassWord, Priviledges priviledges){
        String addUser = "INSERT INTO "+USER_ACCOUNTS_TABLE+ " VALUES ('"+UserName+"' , '"+
                PassWord+"' , '"+priviledges.getLevel()+"' )";
        try {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate(addUser);
        } catch (SQLException ex) {
            OpenToxApplication.opentoxLogger.log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Delete a user.
     * @param UserName
     */
    public static void removeUser(String UserName){
        String removeUser = "DELETE FROM "+USER_ACCOUNTS_TABLE+" WHERE USER_NAME = '"+UserName+"'";
        Statement stmt;
        try {
            stmt = connection.createStatement();
            stmt.executeUpdate(removeUser);
            OpenToxApplication.opentoxLogger.info("The user '"+UserName+"' was deleted!");
        } catch (SQLException ex) {
            OpenToxApplication.opentoxLogger.log(Level.SEVERE, null, ex);
        }
    }


    /**
     * Verify if a given pair of user name and password are registered in the
     * database. If yes, returns true, else returns false. Both username
     * and password are case sensitive.<br/><br/>
     * <b>Note:</b> If some credentials correspond to an administrator, then
     * <tt>verifyCredentials(userName,pass,Privilegdges.ADMIN)</tt>, returns true
     * while <tt>verifyCredentials(userName,pass,Privilegdges.USER)</tt> returns
     * false. Of course an administrator has all the authorization that a simple
     * user has, but this method verifies just if a given triplet of username, password
     * and given provilegdes is valid.
     * @param userName The user name.
     * @param password character array of the password.
     * @param priviledges User authorization level.
     * @return true/false.
     */
    public static boolean verifyCredentials(String userName, char[] password, Priviledges priviledges){
        HashMap<String, char[]> map = (HashMap<String, char[]>) getCredentialsAsMap(priviledges);
        return Arrays.equals(map.get(userName),  password);
    }


    /**
     * Returns a Map&lt;String,char[]&gt; for the credentials of a given authorization level.
     * @param priviledges
     * @return
     */
    public static Map<String,char[] > getCredentialsAsMap(Priviledges priviledges){
        Map<String, char[]> secret = new HashMap<String, char[]>();
        String getCredentials = "SELECT * FROM "+USER_ACCOUNTS_TABLE+" WHERE AUTH LIKE '%"+
                priviledges.getLevel()+"%'";
        ResultSet rs = null;
        try {
            Statement stmt = connection.createStatement();
            rs = stmt.executeQuery(getCredentials);
            while (rs.next()) {
                secret.put(rs.getString("USER_NAME"),rs.getString("USER_PASSWORD").toCharArray());
            }

        } catch (SQLException ex) {
            OpenToxApplication.opentoxLogger.log(Level.SEVERE, null, ex);
        }
        return secret;
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
        String CreateValue = "INSERT INTO " + MODEL_INFO_TABLE + " values (" + id + ",'" + AlgID + "','" + uri + "')";
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
     * This method is used to delete a model registered in the database.
     * @param ID
     */
    public void removeModel(String ID){
        String removeModel = "DELETE FROM "+MODEL_INFO_TABLE+" WHERE MODEL_ID="+ID;
        Statement stmt;
        try {
            stmt = connection.createStatement();
            stmt.executeUpdate(removeModel);
        } catch (SQLException ex) {
            OpenToxApplication.opentoxLogger.log(Level.SEVERE, null, ex);
        }       
    }

    /**
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
        ResultSet rs = null;
        ReferenceList list = new ReferenceList();

        Statement stmt;
        try {
            stmt = connection.createStatement();
            rs = stmt.executeQuery(SelectAllSvc);
            while (rs.next()) {
                list.add(rs.getString("MODEL_URI"));
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
    public static void main(String[] args) throws IOException {

        OpenToxApplication a = new OpenToxApplication();

        
    }
}
