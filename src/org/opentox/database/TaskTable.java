package org.opentox.database;

/**
 *
 * @author Sopasakis Pantelis
 */
public final class TaskTable implements DataBaseAccess, Table {

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
