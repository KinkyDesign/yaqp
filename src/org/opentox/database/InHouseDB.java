/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.opentox.database;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author tartoufo1973
 */
public class InHouseDB {

    private int modelsStack;

    private final static String DRIVER = "org.apache.derby.jdbc.EmbeddedDriver";
    private final static String DATABASENAME = "testdb1";
    private final static String URL = "jdbc:derby:" + DATABASENAME;
    private final static String USER = "itsme";
    private final static String PASSWORD = "letmein";
    private final static String TABLE1_NAME = "MODELS_STACK";
    private final static String TABLE2_NAME = "MODELS";

    private static Driver myDriver = null;
    private static Connection connection = null;

    /**
     *Database Constructor. Connects to the existing database or creates a new one if database doesn't exist.
     */
    public InHouseDB(){
        loadDriver();
        getConnection();
        loadTables();
    }

    private void loadDriver(){
        try {
            try {
                myDriver = (Driver) Class.forName(DRIVER).newInstance();
            } catch (InstantiationException ex) {
                Logger.getLogger(InHouseDB.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                Logger.getLogger(InHouseDB.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(InHouseDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void getConnection(){
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            if(e.getErrorCode() == 40000)  {
                createDataBase();
            } else {
                System.out.println(e);
            }
        }
    }

    private void loadTables(){
        DatabaseMetaData md = null;
        ResultSet rs = null;
        Statement stmt = null;
        Boolean exists1 = false;
        Boolean exists2 = false;

        try {
            md = connection.getMetaData();
            try {
                rs = md.getTables(null, null, "%", null);
                try {
                    while (rs.next() && !(exists1 && exists2)) {
                        if (rs.getString(3).equals(TABLE1_NAME)){
                            exists1 = true;
                        }
                        if (rs.getString(3).equals(TABLE2_NAME)){
                            exists2 = true;
                        }
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(InHouseDB.class.getName()).log(Level.SEVERE, null, ex);
                }

                if (!exists1){
                    createTable1();
                }
                if (!exists2){
                    createTable2();
                }

                try {
                    stmt = connection.createStatement();
                    rs = stmt.executeQuery("SELECT * FROM " + TABLE1_NAME);
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

    private static void createDataBase(){
        System.out.println("Creating databse: " + DATABASENAME);
        try {
            connection = DriverManager.getConnection(URL + ";create=true", USER, PASSWORD);
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }

    private static void createTable1(){
        System.out.println("Creating table : "+TABLE1_NAME);
        String CreateTable = "create table " + TABLE1_NAME + "(STACK INTEGER)";

        try {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate(CreateTable);
        } catch (SQLException ex) {
            Logger.getLogger(InHouseDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        String CreateValue = "insert into " + TABLE1_NAME + " (\"STACK\")\n" +
                "values (0)";
        try {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate(CreateValue);
        } catch (SQLException ex) {
            Logger.getLogger(InHouseDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void createTable2(){
        System.out.println("Creating table: " + TABLE2_NAME);
        String CreateTable = "create table " + TABLE2_NAME + "(" +
                                  "MODEL_ID INTEGER, " +
                                  "ALGORITHM_ID VARCHAR(150)," +
                                  "MODEL_URI VARCHAR(150))";

        try {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate(CreateTable);
        } catch (SQLException ex) {
            Logger.getLogger(InHouseDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    private void IncreaseModelStack(){
        String increaseValue = "update " + TABLE1_NAME + " set STACK = " + (modelsStack+1) +
                " where STACK = " + modelsStack;
        try {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate(increaseValue);
        } catch (SQLException ex) {
            Logger.getLogger(InHouseDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        modelsStack++;
    }

    public int getModelsStack(){
            return modelsStack;
    }

    /**
     * Registers a new trained model in database
     * @param AlgID URI of the algorithm used to train the model
     * @return New model's ID
     */
    public int registerNewModel(String AlgID) {
        int id = getModelsStack()+1;
        String uri = org.opentox.Resources.AbstractResource.baseURI + "/model/" + id;
        String CreateValue = "insert into " + TABLE2_NAME + " values (" + id + ",'" + AlgID + "','" + uri + "')";
        //System.out.println(CreateValue);

        Statement stmt;
        try {
            stmt = connection.createStatement();
            stmt.executeUpdate(CreateValue);
            IncreaseModelStack();
        } catch (SQLException ex) {
            Logger.getLogger(InHouseDB.class.getName()).log(Level.SEVERE, null, ex);
        }

        return id;
        
    }

    public static void main(String [] args){
        long before0  = java.lang.System.currentTimeMillis();
        InHouseDB dbcon = new InHouseDB();
        System.out.println(System.currentTimeMillis()-before0+"ms for connection");
        //System.out.println(dbcon.getModelsStack());

        int id =  dbcon.registerNewModel("http://www.opentox.org:3000/algorithm/learning/classification/svc");
        System.out.println(id);
        

        System.err.println(dbcon.modelsStack);
    }
}
