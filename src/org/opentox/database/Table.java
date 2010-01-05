package org.opentox.database;

/**
 * A Table in the databse of the server.
 * @author Sopasakis Pantelis
 */
public interface Table {
    /**
     * Removes the table from the database.
     */
    public void getRidOf();
    /**
     * Creates the table.
     */
    public void create();

}
