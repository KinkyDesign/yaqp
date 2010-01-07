package org.opentox.database;

import org.opentox.interfaces.ITable;
import java.sql.*;
import java.util.logging.Level;
import org.restlet.data.ReferenceList;
import org.opentox.OpenToxApplication;
import org.opentox.algorithm.AlgorithmEnum;
import org.opentox.interfaces.IDataBaseAccess;
import org.opentox.interfaces.IDataBaseAccess.CreateTable;
import org.opentox.interfaces.IDataBaseAccess.Removal;
import org.opentox.resource.OTResource.URIs;

/**
 *
 * @author OpenTox - http://www.opentox.org/
 * @author Sopasakis Pantelis
 * @author Sarimveis Harry
 * @version 1.3.3 (Last update: Dec 28, 2009)
 */
public class ModelsTable implements IDataBaseAccess, ITable {

    protected static int modelsStack;
    protected final static String COL_MODEL_ID = "MODEL_ID",
            COL_MODEL_URI = "MODEL_URI",
            COL_ALGORITHM_ID = "ALGORITHM_ID",
            MODELS_STACK_TABLE = "MODELS_STACK",
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
     * Creates new tables.
     * @see ModelsTables#createModelInfoTable()
     * @see ModelsTables#createModelsStackTable() 
     */
    public void create() {
        ModelsTable.INSTANCE.createModelInfoTable();
        ModelsTable.INSTANCE.createModelsStackTable();
    }

    /**
     * Creates the table MODELS_STACK which contains information about the id
     * of the next model to be trained. The structure of this table is:
     *
     * <pre>
     *
     * | MODELS_STACK |
     * |==============|
     * | STAACK (INT) |
     * |--------------|
     * |      *       |
     * |      *       |
     * </pre>
     *
     * @see ModelsTable#createModelInfoTable()
     * @see ModelsTable#isModel(java.lang.String, java.lang.String)
     * @see ModelsTable#registerNewModel(java.lang.String)
     * @see ModelsTable#removeModel(java.lang.String)
     */
    @CreateTable
    protected void createModelsStackTable() {
        OpenToxApplication.opentoxLogger.info("Creating Table : " + MODELS_STACK_TABLE);
        String CreateTable = "create table " + MODELS_STACK_TABLE + "(STACK INTEGER)";

        try {
            Statement stmt = InHouseDB.connection.createStatement();
            stmt.executeUpdate(CreateTable);
            stmt.close();
        } catch (SQLException ex) {
            OpenToxApplication.opentoxLogger.log(Level.SEVERE, null, ex);
        }

        String CreateValue = "insert into " + MODELS_STACK_TABLE + " (\"STACK\")\n"
                + "values (0)";
        try {
            Statement stmt = InHouseDB.connection.createStatement();
            stmt.executeUpdate(CreateValue);
            stmt.close();
        } catch (SQLException ex) {
            OpenToxApplication.opentoxLogger.log(Level.SEVERE, null, ex);
        }
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
     * @see ModelsDB#createModelsStackTable()
     * @see ModelsDB#IncreaseModelStack()
     * @see ModelsDB#isModel(java.lang.String, java.lang.String)
     * @see ModelsDB#registerNewModel(java.lang.String)
     * @see ModelsDB#removeModel(java.lang.String)
     */
    @CreateTable
    protected void createModelInfoTable() {
        OpenToxApplication.opentoxLogger.info("Creating table: " + MODEL_INFO_TABLE);
        String CreateTable = "create table " + MODEL_INFO_TABLE + "("
                + COL_MODEL_ID + " INTEGER, "
                + COL_ALGORITHM_ID + " VARCHAR(150),"
                + COL_MODEL_URI + " VARCHAR(150))";

        try {
            Statement stmt = InHouseDB.connection.createStatement();
            stmt.executeUpdate(CreateTable);
            stmt.close();
        } catch (SQLException ex) {
            OpenToxApplication.opentoxLogger.log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Increases the model stack. This method is invoked after a successful training
     * of a new model when the model is registered in the database.
     * @see ModelsDB#registerNewModel(java.lang.String)
     */
    private synchronized void IncreaseModelStack() {
        String increaseValue = "update " + MODELS_STACK_TABLE + " set STACK = " + (modelsStack + 1)
                + " where STACK = " + modelsStack;
        try {
            Statement stmt = InHouseDB.connection.createStatement();
            stmt.executeUpdate(increaseValue);
            stmt.close();
        } catch (SQLException ex) {
            OpenToxApplication.opentoxLogger.log(Level.SEVERE, null, ex);
        }
        modelsStack++;
    }

    /**
     * Returns the number of models currently in the database.
     * @return the first available model id.
     */
    public synchronized int getModelsStack() {
        return modelsStack;
    }

    /**
     * Registers a new trained model in database
     * @param AlgID URI of the algorithm used to train the model
     * @return New model's ID
     * @see ModelsDB#IncreaseModelStack()
     * @see ModelsDB#removeModel(java.lang.String)
     */
    @Registration
    public synchronized int registerNewModel(String AlgID) {
        int id = getModelsStack() + 1;
        String uri = URIs.baseURI + "/model/" + id;
        String CreateValue = "INSERT INTO " + MODEL_INFO_TABLE + " values (" + id + ",'" + AlgID + "','" + uri + "')";
        Statement stmt;
        try {
            stmt = InHouseDB.connection.createStatement();
            stmt.executeUpdate(CreateValue);
            IncreaseModelStack();
            stmt.close();
        } catch (SQLException ex) {
            OpenToxApplication.opentoxLogger.log(Level.SEVERE, null, ex);
        }

        return id;
    }

    /**
     * This method is used to delete a model registered in the database.
     * @param ID The ID of the model to be deleted
     * @see ModelsDB#registerNewModel(java.lang.String)
     */
    @Removal
    public synchronized void removeModel(String ID) {
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

    /**
     * Checks if a model was built with a given algorithm. e.g. isModel(10,"svm")
     * returns true if the model with ID = 10 is an SVM model.
     * @param ID the id of the model.
     * @param Model
     * @return The logical value of the answer to the submitted query.
     * @see ModelsDB#registerNewModel(java.lang.String)
     * @see ModelsDB#removeModel(java.lang.String)
     * @see ModelsDB#getAlgorithm(java.lang.String)
     */
    public synchronized boolean isModel(String ID, String Model) {
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
     * @param ID
     * @return Algorithm Enumeration Object for the given ID.
     * @see ModelsDB#isModel(java.lang.String, java.lang.String)
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

    /**
     *
     * @return all Models as a list of references.
     */
    public ReferenceList getModelsAsReferenceList() {

        String SelectAllModels = "SELECT * FROM " + MODEL_INFO_TABLE;
        ResultSet rs = null;
        ReferenceList list = new ReferenceList();

        Statement stmt;
        try {
            stmt = InHouseDB.connection.createStatement();
            rs = stmt.executeQuery(SelectAllModels);
            while (rs.next()) {
                list.add(rs.getString(COL_MODEL_URI));
            }
            stmt.close();
        } catch (SQLException ex) {
            OpenToxApplication.opentoxLogger.log(Level.SEVERE, null, ex);
        }
        return list;
    }

    /**
     * Returns a reference list for the models that where trained usign a
     * specific.
     * TODO: Change String algorithmId to AlgorithmEnum algorithm
     * @param algorithmId
     * @return Reference list for the given algorithm id.
     */
    public ReferenceList getReferenceListFromAlgId(String algorithmId) {
        String SelectAllSvc = "SELECT * FROM " + MODEL_INFO_TABLE
                + " WHERE " + COL_ALGORITHM_ID + " LIKE '%" + algorithmId + "%'";
        ResultSet rs = null;
        ReferenceList list = new ReferenceList();

        Statement stmt;
        try {
            stmt = InHouseDB.connection.createStatement();
            rs = stmt.executeQuery(SelectAllSvc);
            while (rs.next()) {
                list.add(rs.getString(COL_MODEL_URI));
            }
            stmt.close();
        } catch (SQLException ex) {
            OpenToxApplication.opentoxLogger.log(Level.SEVERE, null, ex);
        }
        return list;
    }

    public void getRidOf() {
        OpenToxApplication.opentoxLogger.info("Droping the Table '" + MODELS_STACK_TABLE + "' !");
        String dropStack = "DROP TABLE " + MODELS_STACK_TABLE;
        String dropInfo = "DROP TABLE " + MODEL_INFO_TABLE;
        OpenToxApplication.opentoxLogger.info("The table '" + MODELS_STACK_TABLE
                + "' was removed from the DB '" + DATABASENAME + "'");
        OpenToxApplication.opentoxLogger.info("The table '" + MODEL_INFO_TABLE
                + "' was removed from the DB '" + DATABASENAME + "'");
        Statement stmt;
        try {
            stmt = InHouseDB.connection.createStatement();
            stmt.executeUpdate(dropStack);
            stmt.executeUpdate(dropInfo);
            stmt.close();
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
                builder.append("* " + rs.getString(COL_MODEL_ID) + " , " + rs.getString(COL_ALGORITHM_ID) + " ,"
                        + rs.getString(COL_MODEL_URI) + "\n");
            }
            stmt.close();
        } catch (SQLException ex) {
            OpenToxApplication.opentoxLogger.log(Level.SEVERE, null, ex);
        }
        builder.append("---------------- END DATA ----------------\n");
        return builder.toString();
    }

//    public static void main(String[] args) {
//        ModelsTable mdb = ModelsTable.INSTANCE;
//        System.out.println(mdb);
//    }
}
