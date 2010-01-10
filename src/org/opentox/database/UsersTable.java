package org.opentox.database;

import java.util.Iterator;
import org.opentox.interfaces.ITable;
import org.opentox.interfaces.IDataBaseAccess;
import org.opentox.auth.Priviledges;
import java.sql.*;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import org.opentox.OpenToxApplication;
import org.opentox.interfaces.Jterator;
import static org.opentox.auth.DigestGenerator.*;

/**
 *
 * @author Sopasakis Pantelis
 */
public class UsersTable implements IDataBaseAccess, ITable {

    protected final static String USER_ACCOUNTS_TABLE = "USERS";
    private final static String
            COL_USER_NAME = "USER_NAME",
            COL_USER_PASSWRD = "USER_PASSWORD",
            COL_USER_AUTH = "AUTH";
    private static UsersTable instanceOfThis = null;
    public final static UsersTable INSTANCE = getInstance();

    private static UsersTable getInstance() {
//        InHouseDB ihdb = InHouseDB.INSTANCE;
        if (instanceOfThis == null) {
            instanceOfThis = new UsersTable();
        }
        return instanceOfThis;
    }

    private UsersTable() {
    }

    /**
     * Generates the table which contains a/a data. The structure of this table is
     * the following:
     *
     * <pre>
     *
     * |                       USERS                        |
     * |====================================================|
     * | USER_NAME (TXT) | USER_PASSWORD (TXT) | AUTH (TXT) |
     * |-----------------|---------------------|------------|
     * |        *        |           *         |      *     |
     * |        *        |           *         |      *     |
     * </pre>
     *
     * Passwords are saved as digests!
     */
    @CreateTable
    public void create() {
        OpenToxApplication.opentoxLogger.info("Creating table: " + USER_ACCOUNTS_TABLE);
        String CreateTable = "create table " + USER_ACCOUNTS_TABLE + "("
                + COL_USER_NAME + " VARCHAR(20), "
                + COL_USER_PASSWRD +" VARCHAR(150),"
                + COL_USER_AUTH +" VARCHAR(20))";
        try {
            Statement stmt = InHouseDB.connection.createStatement();
            stmt.executeUpdate(CreateTable);
            stmt.close();
        } catch (SQLException ex) {
            OpenToxApplication.opentoxLogger.log(Level.SEVERE, null, ex);
        }
    }

    /**
     * This method is used to add a new user to the USERS table. The username,
     * the password
     * @param UserName username
     * @param PassWord password
     */
    @Registration
    public void addUser(String UserName, String PassWord, Priviledges priviledges) {
        removeUser(UserName);
        String addUser = "INSERT INTO " + USER_ACCOUNTS_TABLE + " VALUES ('" + UserName + "' , '"
                + generateDigest(generateDigest(PassWord, DigestAlgorithm.MD5), DigestAlgorithm.SHA_512) + "' , '"
                + priviledges.getLevel() + "' )";
        OpenToxApplication.opentoxLogger.info("The user '" + UserName + "' is registered as '"
                + priviledges.getLevel() + "' .");
        try {
            Statement stmt = InHouseDB.connection.createStatement();
            stmt.executeUpdate(addUser);
            stmt.close();
        } catch (SQLException ex) {
            OpenToxApplication.opentoxLogger.log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Delete a user.
     * @param UserName
     */
    @Removal
    public void removeUser(String UserName) {
        String removeUser = "DELETE FROM " + USER_ACCOUNTS_TABLE + " WHERE "+COL_USER_NAME+" = '" + UserName + "'";
        Statement stmt;
        try {
            stmt = InHouseDB.connection.createStatement();
            stmt.executeUpdate(removeUser);
            OpenToxApplication.opentoxLogger.info("The user '" + UserName + "' was deleted!");
            stmt.close();
        } catch (SQLException ex) {
            OpenToxApplication.opentoxLogger.log(Level.SEVERE, null, ex);
        }
    }

    public Priviledges getAuthorizationForUser(String username) {
        Priviledges priviledges = Priviledges.GUEST;
        String dbQuery = "SELECT * FROM " + USER_ACCOUNTS_TABLE + " WHERE "+COL_USER_NAME+
                " = '" + username + "'";
        ResultSet rs = null;
        try {
            Statement stmt = InHouseDB.connection.createStatement();
            rs = stmt.executeQuery(dbQuery);
            if (rs.next()) {
                priviledges = new Priviledges(rs.getString("AUTH"));
            }
            stmt.close();
        } catch (SQLException ex) {
            OpenToxApplication.opentoxLogger.log(Level.SEVERE, null, ex);
        }

        return priviledges;
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
     * @return true/false.
     */
    public boolean verifyCredentials(String userName, String password) {
        String dbQuery = "SELECT * FROM " + USER_ACCOUNTS_TABLE + " WHERE "+COL_USER_NAME+"='" + userName
                + "' AND "+COL_USER_PASSWRD+" ='" +
                generateDigest(generateDigest(password, DigestAlgorithm.MD5), DigestAlgorithm.SHA_512) + "'";
        ResultSet rs = null;
        boolean verify = false;
        try {
            Statement stmt = InHouseDB.connection.createStatement();
            rs = stmt.executeQuery(dbQuery);
            if (rs.next()) {
                verify = true;
            }
            stmt.close();
        } catch (SQLException ex) {
            OpenToxApplication.opentoxLogger.log(Level.SEVERE, null, ex);
        }
        return verify;
    }

    /**
     * Returns a Map&lt;String,char[]&gt; for the credentials of a given authorization level.
     * @param priviledges
     * @return All credentials as a Map.
     */
    public Map<String, char[]> getCredentialsAsMap(Priviledges priviledges) {
        Map<String, char[]> secret = new HashMap<String, char[]>();
        String getCredentials = "SELECT * FROM " + USER_ACCOUNTS_TABLE + " WHERE AUTH LIKE '%"
                + priviledges.getLevel() + "%'";
        ResultSet rs = null;
        try {
            Statement stmt = InHouseDB.connection.createStatement();
            rs = stmt.executeQuery(getCredentials);
            while (rs.next()) {
                secret.put(rs.getString(COL_USER_NAME), rs.getString(COL_USER_PASSWRD).toCharArray());
            }
            stmt.close();
        } catch (SQLException ex) {
            OpenToxApplication.opentoxLogger.log(Level.SEVERE, null, ex);
        }
        return secret;
    }


    // TODO: Improve the format of the returned string by this method!
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("DATABASE : " + DB_URL + "\n");
        builder.append("TABLE    : " + USER_ACCOUNTS_TABLE + "\n\n");
        builder.append("----------------   DATA   ----------------\n");
        String content = "SELECT * FROM " + USER_ACCOUNTS_TABLE;
        ResultSet rs = null;
        try {
            Statement stmt = InHouseDB.connection.createStatement();
            rs = stmt.executeQuery(content);
            while (rs.next()) {
                builder.append("* "+rs.getString(COL_USER_NAME) + " (" + rs.getString(COL_USER_AUTH) + ") ,"
                        + rs.getString(COL_USER_PASSWRD) + "\n");
            }
            stmt.close();
        } catch (SQLException ex) {
            OpenToxApplication.opentoxLogger.log(Level.SEVERE, null, ex);
        }
        builder.append("---------------- END DATA ----------------\n");
        return builder.toString();
    }

    public void getRidOf() {
        OpenToxApplication.opentoxLogger.info("Droping the Table '" + USER_ACCOUNTS_TABLE + "' !");
        String drop = "DROP TABLE " + USER_ACCOUNTS_TABLE;
        OpenToxApplication.opentoxLogger.info("The table '" + USER_ACCOUNTS_TABLE
                + "' was removed from the DB '" + DATABASENAME + "'");
        Statement stmt;
        try {
            stmt = InHouseDB.connection.createStatement();
            stmt.executeUpdate(drop);
            stmt.close();
        } catch (SQLException ex) {
            OpenToxApplication.opentoxLogger.log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) {
        UsersTable udb = UsersTable.INSTANCE;                
        System.out.printf(Locale.UK, "%s t", udb);
    }

    public Jterator<String> iterator(String ColumnName) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Jterator<String> search(String IterableCoumn, String SearchColumn, String keyword) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
