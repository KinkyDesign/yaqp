package org.opentox.database;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.opentox.Applications.OpenToxApplication;

/**
 *
 * @author Sopasakis Pantelis
 */
public class FeaturesDB extends InHouseDB {

    private final static String FEAT_SEQUENCE_TABLE = "FEAT_SEQUENCE_";

    public FeaturesDB() {
    }

    /**
     * Creates a new Table in the database, namely FEAT_SEQUENCE_TABLE_{model_id}
     * which will be used to store the list of features of a model of given
     * id. The structure of this table is:
     * <pre>
     *
     * |      FEAT_SEQUENCE_123       |
     * |==============================|
     * | INDEX (INT) | FEATURES (TXT) |
     * |-------------|----------------|
     * |     ***     |      ***       |
     * |     ***     |      ***       |
     * </pre>
     * @param model_id
     */
    @CreateTable({"model_id"})
    private static void createFeatureSequenceTable(int model_id) {
        OpenToxApplication.opentoxLogger.info("Creating Table : " + FEAT_SEQUENCE_TABLE + model_id);
        String CreateTable = "CREATE TABLE " + FEAT_SEQUENCE_TABLE + model_id + "(IND INTEGER, FEATURES VARCHAR(200))";
        try {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate(CreateTable);
            OpenToxApplication.opentoxLogger.info("Table : " + FEAT_SEQUENCE_TABLE + model_id
                    + " was successfully created! ");
        } catch (SQLException ex) {
            OpenToxApplication.opentoxLogger.log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Create a new table in the database called FEAT_SEQUENCE_{model_id} and
     * put in it the {@link java.util.List} of features. The method cannot be applied
     * to an existing table. Each invokation of this method creates a new and immutable
     * table in the dataset.
     * @param model_id
     */
    @Registration
    public static void registerFeatureList(int model_id, final List<String> features) {
        if (!(exists(model_id))) {
            try {
                createFeatureSequenceTable(model_id);
                String addFeature;
                for (int i = 0; i < features.size(); i++) {
                    addFeature = "INSERT INTO " + FEAT_SEQUENCE_TABLE + model_id
                            + " VALUES " + "(" + (i + 1) + " , '" + features.get(i) + "' )";
                    System.out.println(addFeature);
                    Statement stmt = connection.createStatement();
                    stmt.executeUpdate(addFeature);
                }

            } catch (SQLException ex) {
                OpenToxApplication.opentoxLogger.log(Level.SEVERE, null, ex);
            }

        }else{
            OpenToxApplication.opentoxLogger.warning("The table "+FEAT_SEQUENCE_TABLE+model_id+
                    " already exists!");
        }


    }

    private static boolean exists(int model_id) {
        boolean exists = false;
        try {
            DatabaseMetaData meta = connection.getMetaData();
            ResultSet results = meta.getTables(null, null, "%", null);
            while (results.next()){
                if (results.getString(3).equals(FEAT_SEQUENCE_TABLE+model_id)){
                    exists = true;
                    break;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(FeaturesDB.class.getName()).log(Level.SEVERE, null, ex);
        }

        return exists;
    }
}
