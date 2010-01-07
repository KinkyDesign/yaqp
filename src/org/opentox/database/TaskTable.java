package org.opentox.database;

import org.opentox.interfaces.ITable;
import org.opentox.interfaces.IDataBaseAccess;

/**
 *
 * @author Sopasakis Pantelis
 */
public final class TaskTable implements IDataBaseAccess, ITable {

    private static TaskTable instanceOfThis = null;
    public final static TaskTable INSTANCE = getInstance();

    private static TaskTable getInstance() {
//        InHouseDB ihdb = InHouseDB.INSTANCE;
        if (instanceOfThis == null) {
            instanceOfThis = new TaskTable();
        }
        return instanceOfThis;
    }

    public void getRidOf() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void create() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
