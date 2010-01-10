package org.opentox.database;

import java.util.logging.Logger;
import org.opentox.interfaces.ITable;
import java.sql.*;
import java.util.NoSuchElementException;
import java.util.logging.Level;
import org.opentox.OpenToxApplication;
import org.opentox.algorithm.AlgorithmEnum;
import org.opentox.interfaces.IDataBaseAccess;
import org.opentox.interfaces.IDataBaseAccess.CreateTable;
import org.opentox.interfaces.IDataBaseAccess.Removal;
import org.opentox.interfaces.Jterator;
import org.opentox.resource.OTResource.URIs;

/**
 *
 * @author OpenTox - http://www.opentox.org/
 * @author Sopasakis Pantelis
 * @author Sarimveis Harry
 * @version 1.3.3 (Last update: Dec 28, 2009)
 */
public class ModelsTable implements IDataBaseAccess, ITable {


    public final static String COL_MODEL_ID = "MODEL_ID",
            COL_MODEL_URI = "MODEL_URI",
            COL_ALGORITHM_ID = "ALGORITHM_ID",
            MODEL_INFO_TABLE = "MODELS";
    private static ModelsTable instanceOfThis = null;
    public final static ModelsTable INSTANCE = getInstance();

    private static ModelsTable getInstance() {
        if (instanceOfThis == null) {
            instanceOfThis = new ModelsTable();
        }
        return instanceOfThis;
    }

    private ModelsTable() {
    }

    /**
     * Generates the table that contains information about models. The structure of
     * this table is:
     *
     * <pre>
     *
     * |                     MODEL_INFO                         |
     * |========================================================|
     * | MODEL_ID (INT) | MODEL_URI (TXT) | ALGORITHM_URI (TXT) |
     * |----------------|-----------------|---------------------|
     * |        *       |        *        |          *          |
     * |        *       |        *        |          *          |
     *
     * </pre>
     *
     * @see ModelsTable#create()
     * @see ModelsTable#isModel(java.lang.String, java.lang.String)
     * @see ModelsTable#register(java.lang.String)
     * @see ModelsTable#remove(java.lang.String)
     */
    @CreateTable
    public void create() {
        OpenToxApplication.opentoxLogger.info("Creating table: " + MODEL_INFO_TABLE);
        String CreateTable = "create table " + MODEL_INFO_TABLE + "("
                + COL_MODEL_ID + " INTEGER, "
                + COL_ALGORITHM_ID + " VARCHAR(80),"
                + COL_MODEL_URI + " VARCHAR(60))";

        try {
            Statement stmt = InHouseDB.connection.createStatement();
            stmt.executeUpdate(CreateTable);
            stmt.close();
        } catch (SQLException ex) {
            OpenToxApplication.opentoxLogger.log(Level.SEVERE, null, ex);
        }
    }

    /**
     *
     * TODO: IMPORTANT!!! Rewrite this method!!!
     * Returns the number of models currently in the database.
     * @return the first available model id.
     */
    public synchronized int getModelsStack() {
        int I = 0;
        String count = "SELECT COUNT ("+COL_MODEL_ID+") FROM " + MODEL_INFO_TABLE;
        Statement stmt;
        ResultSet rs;
        try {
            stmt = InHouseDB.connection.createStatement();
            rs = stmt.executeQuery(count);
            if (rs.next()){
                I = rs.getInt(1);
            }
            stmt.close();
        } catch (SQLException ex) {
            OpenToxApplication.opentoxLogger.log(Level.SEVERE, null, ex);
        }
        return I;
    }

    /**
     * Registers a new trained model in database
     * @param AlgID URI of the algorithm used to train the model
     * @return New model's ID
     * @see ModelsTable#remove(java.lang.String)
     */
    @Registration
    public synchronized int register(String AlgID) {
        int id = getModelsStack() + 1;
        String uri = URIs.baseURI + "/model/" + id;
        String CreateValue = "INSERT INTO " + MODEL_INFO_TABLE
                + " values (" + id + ",'" + AlgID + "','" + uri + "')";
        Statement stmt;
        try {
            stmt = InHouseDB.connection.createStatement();
            stmt.executeUpdate(CreateValue);
            stmt.close();
        } catch (SQLException ex) {
            OpenToxApplication.opentoxLogger.log(Level.SEVERE, null, ex);
        }
        return id;
    }

    /**
     * This method is used to delete a model registered in the database.
     * @param ID The ID of the model to be deleted
     * @see ModelsTable#register(java.lang.String)
     */
    @Removal
    public synchronized void remove(String ID) {
        String removeModel = "DELETE FROM " + MODEL_INFO_TABLE + " WHERE " + COL_MODEL_ID + "=" + ID;
        Statement stmt;
        try {
            stmt = InHouseDB.connection.createStatement();
            stmt.executeUpdate(removeModel);
            stmt.close();
        } catch (SQLException ex) {
            OpenToxApplication.opentoxLogger.log(Level.SEVERE, null, ex);
        }
    }

    public Jterator<String> iterator(String ColumnName) {
        return new JteratorImpl(ColumnName, null);
    }

    public Jterator<String> search(String IterableCoumn, String SearchColumn, String keyword) {
        return new JteratorImpl(IterableCoumn, " WHERE " + SearchColumn + " LIKE '%" + keyword + "%'");
    }

    /**
     * A private implementation of Jterator.
     */
    private class JteratorImpl implements Jterator<String> {

        private Statement stmt;
        private String column = "";
        private String searchQuery = "";
        private ResultSet RESULT_SET;

        public JteratorImpl(String column, String searchQuery) {
            this.column = column;
            if (searchQuery != null) {
                this.searchQuery = searchQuery;
            }
            RESULT_SET = getRs();
        }

        private ResultSet getRs() {
            ResultSet rs = null;

            try {
                stmt = InHouseDB.connection.createStatement();
                rs = stmt.executeQuery("SELECT * FROM " + MODEL_INFO_TABLE + " " + searchQuery);
            } catch (SQLException ex) {
                OpenToxApplication.opentoxLogger.log(Level.SEVERE, null, ex);
            }
            return rs;
        }

        public boolean hasNext() {
            boolean hasNext = false;
            try {
                hasNext = RESULT_SET.next();
            } catch (SQLException ex) {
                // ????
            }
            return hasNext;

        }

        public String next() {
            String result = "";
            try {
                result = RESULT_SET.getString(column);
            } catch (SQLException ex) {
                throw new NoSuchElementException();
            }
            return result;
        }

        public void remove() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void close() {
            try {
                stmt.close();
            } catch (SQLException ex) {
                Logger.getLogger(ModelsTable.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void getRidOf() {
        OpenToxApplication.opentoxLogger.info("Droping the Table '" + MODEL_INFO_TABLE + "' !");
        String dropInfo = "DROP TABLE " + MODEL_INFO_TABLE;
        Statement stmt;
        try {
            stmt = InHouseDB.connection.createStatement();
            stmt.executeUpdate(dropInfo);
            stmt.close();
            OpenToxApplication.opentoxLogger.info("The table '" + MODEL_INFO_TABLE
                    + "' was removed from the DB '" + DATABASENAME + "'");
        } catch (SQLException ex) {
            OpenToxApplication.opentoxLogger.log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("DATABASE : " + DB_URL + "\n");
        builder.append("TABLE    : " + MODEL_INFO_TABLE + "\n\n");
        builder.append("----------------   DATA   ----------------\n");
        String content = "SELECT * FROM " + MODEL_INFO_TABLE;
        ResultSet rs = null;
        try {
            Statement stmt = InHouseDB.connection.createStatement();
            rs = stmt.executeQuery(content);
            while (rs.next()) {
                builder.append("* " + rs.getString(COL_MODEL_ID) + " | "
                        + rs.getString(COL_ALGORITHM_ID) + " | "
                        + rs.getString(COL_MODEL_URI) + "\n");
            }
            stmt.close();
        } catch (SQLException ex) {
            OpenToxApplication.opentoxLogger.log(Level.SEVERE, null, ex);
        }
        builder.append("---------------- END DATA ----------------\n");
        return builder.toString();
    }

    /**
     * Checks if a model was built with a given algorithm. e.g. isModel(10,"svm")
     * returns true if the model with ID = 10 is an SVM model.
     * @param ID the id of the model.
     * @param Model
     * @return The logical value of the answer to the submitted query.
     * @see ModelsTable#register(java.lang.String)
     * @see ModelsTable#remove(java.lang.String)
     * @see ModelsTable#getAlgorithm(java.lang.String)
     */
    private synchronized boolean isModel(String ID, String Model) {
        boolean result = false;
        String searchMLR = "SELECT * FROM " + MODEL_INFO_TABLE
                + " WHERE " + COL_MODEL_ID + "=" + ID
                + " AND " + COL_ALGORITHM_ID + " LIKE '%" + Model + "%'";
        Statement stmt;
        ResultSet rs = null;
        try {
            stmt = InHouseDB.connection.createStatement();
            rs = stmt.executeQuery(searchMLR);
            result = rs.next();
            stmt.close();
        } catch (SQLException ex) {
            OpenToxApplication.opentoxLogger.log(Level.SEVERE, null, ex);
        }
        return result;
    }

    /**
     * Returns an {@link org.opentox.algorithm.AlgorithmEnum }
     * characterization of a given model.
     * @param ID identity number of a model (as a String).
     * @return Algorithm Enumeration instance for the given ID.
     * @see ModelsTable#isModel(java.lang.String, java.lang.String)
     */
    public AlgorithmEnum getAlgorithm(String ID) {
        AlgorithmEnum algorithm = AlgorithmEnum.unknown;
        if (isModel(ID, "svm")) {
            algorithm = AlgorithmEnum.svm;
        } else if (isModel(ID, "mlr")) {
            algorithm = AlgorithmEnum.mlr;
        } else if (isModel(ID, "svc")) {
            algorithm = AlgorithmEnum.svc;
        }
        return algorithm;
    }

    public static void main(String[] args) throws SQLException {
        ModelsTable mdb = ModelsTable.INSTANCE;
    }
}
