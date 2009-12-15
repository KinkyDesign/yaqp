package org.opentox.database;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import org.opentox.Applications.OpenToxApplication;

/**
 *
 * @author Sopasakis Pantelis
 */
public class FeaturesDB extends InHouseDB implements DBase{

    private final static String FEAT_SEQUENCE_TABLE = "FEAT_SEQUENCE_";

    public FeaturesDB(){
        
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
    public static void createFeatureSequenceTable(int model_id){
        OpenToxApplication.opentoxLogger.info("Creating Table : " + FEAT_SEQUENCE_TABLE + model_id);
        String CreateTable = "CREATE TABLE "+FEAT_SEQUENCE_TABLE+model_id+ "(INDEX INTEGER, FEATURES TEXT)";
        try {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate(CreateTable);
            OpenToxApplication.opentoxLogger.info("Table : " + FEAT_SEQUENCE_TABLE + model_id +
                    "was successfully created! ");
        } catch (SQLException ex) {
            OpenToxApplication.opentoxLogger.log(Level.SEVERE, null, ex);
        }
    }

    


}
