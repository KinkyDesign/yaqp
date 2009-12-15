package org.opentox.database;

import java.sql.*;
import java.util.logging.Level;
import org.restlet.data.ReferenceList;
import org.opentox.Applications.OpenToxApplication;

/**
 *
 * @author Sopasakis Pantelis
 */
public class ModelsDB extends InHouseDB {

    protected static int modelsStack;

    protected final static String
            COL_MODEL_ID = "MODEL_ID",
            COL_MODEL_URI = "MODEL_URI",
            COL_ALGORITHM_ID = "ALGORITHM_ID",
            MODELS_STACK_TABLE = "MODELS_STACK",
            MODEL_INFO_TABLE = "MODELS";

    public ModelsDB() {

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
     */
    protected static void createModelsStackTable() {
        OpenToxApplication.opentoxLogger.info("Creating Table : " + MODELS_STACK_TABLE);
        String CreateTable = "create table " + MODELS_STACK_TABLE + "(STACK INTEGER)";

        try {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate(CreateTable);
        } catch (SQLException ex) {
            OpenToxApplication.opentoxLogger.log(Level.SEVERE, null, ex);
        }

        String CreateValue = "insert into " + MODELS_STACK_TABLE + " (\"STACK\")\n"
                + "values (0)";
        try {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate(CreateValue);
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
     */
    protected static void createModelInfoTable() {
        OpenToxApplication.opentoxLogger.info("Creating table: " + MODEL_INFO_TABLE);
        String CreateTable = "create table " + MODEL_INFO_TABLE + "("
                + COL_MODEL_ID + " INTEGER, "
                + COL_ALGORITHM_ID + " VARCHAR(150),"
                + COL_MODEL_URI + " VARCHAR(150))";

        try {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate(CreateTable);
        } catch (SQLException ex) {
            OpenToxApplication.opentoxLogger.log(Level.SEVERE, null, ex);
        }
    }

    private static void IncreaseModelStack() {
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

    public static int getModelsStack() {
        return modelsStack;
    }

    /**
     * Registers a new trained model in database
     * @param AlgID URI of the algorithm used to train the model
     * @return New model's ID
     */
    public static int registerNewModel(String AlgID) {
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
    public static void removeModel(String ID){
        String removeModel = "DELETE FROM "+MODEL_INFO_TABLE+" WHERE "+COL_MODEL_ID+"="+ID;
        Statement stmt;
        try {
            stmt = connection.createStatement();
            stmt.executeUpdate(removeModel);
        } catch (SQLException ex) {
            OpenToxApplication.opentoxLogger.log(Level.SEVERE, null, ex);
        }
    }


    /**
     * Checks if a model was built with a given algorithm. e.g. isModel(10,"svm")
     * returns true if the model with ID = 10 is an SVM model.
     * @param ID the id of the model.
     * @param Model
     * @return
     */
    public static boolean isModel(String ID, String Model){
        boolean result = false;
        String searchMLR = "SELECT * FROM "+MODEL_INFO_TABLE+
                " WHERE "+COL_MODEL_ID+"="+ID+
                " AND "+COL_ALGORITHM_ID+" LIKE '%"+Model+"%'";
        Statement stmt;
        ResultSet rs = null;
        try {
            stmt = connection.createStatement();
            rs = stmt.executeQuery(searchMLR);
            result=rs.next();
        } catch (SQLException ex) {
            OpenToxApplication.opentoxLogger.log(Level.SEVERE, null, ex);
        }
        return result;
    }

    /**
     *
     * @return
     */
    public static ReferenceList getModelsAsReferenceList() {

        String SelectAllModels = "SELECT * FROM " + MODEL_INFO_TABLE;
        ResultSet rs = null;
        ReferenceList list = new ReferenceList();

        Statement stmt;
        try {
            stmt = connection.createStatement();
            rs = stmt.executeQuery(SelectAllModels);
            while (rs.next()) {
                list.add(rs.getString(COL_MODEL_URI));
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
    public static ReferenceList getReferenceListFromAlgId(String algorithmId) {
        String SelectAllSvc = "SELECT * FROM " + MODEL_INFO_TABLE +
                " WHERE "+COL_ALGORITHM_ID+" LIKE '%" + algorithmId + "%'";
        ResultSet rs = null;
        ReferenceList list = new ReferenceList();

        Statement stmt;
        try {
            stmt = connection.createStatement();
            rs = stmt.executeQuery(SelectAllSvc);
            while (rs.next()) {
                list.add(rs.getString(COL_MODEL_URI));
            }
        } catch (SQLException ex) {
            OpenToxApplication.opentoxLogger.log(Level.SEVERE, null, ex);
        }
        return list;
    }

}
