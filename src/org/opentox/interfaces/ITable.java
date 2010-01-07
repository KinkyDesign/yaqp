package org.opentox.interfaces;

import org.opentox.interfaces.IDataBaseAccess;

/**
 * A Table in the databse of the server.
 * @author Sopasakis Pantelis
 */
public interface ITable {
    /**
     * Removes the table from the database.
     */
    public void getRidOf();
    /**
     * Creates the table.
     */
    @IDataBaseAccess.CreateTable public void create();
        

}
