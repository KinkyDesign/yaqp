package org.opentox.interfaces;

import org.opentox.interfaces.IDataBaseAccess.CreateTable;


/**
 * A Table in the databse of the server. The structure of the table (number of
 * columns, datatype of each column etc) are specified once the table is created
 * which is done by the method create(). This interface provides useful methods for
 * handling database tables.
 * @author Sopasakis Pantelis
 */
public interface ITable {

    /**
     * Removes completely the table from the database. After this operation
     * all data stored in the table will get lost, so make sure that you either
     * really don't need these data or that you have kept backup of your database
     * which is in general highly recommended.
     */
    public void getRidOf();


    /**
     * Provides an iterator for all elements in a specified column of the database.
     * @param ColumnName The name of the column.
     * @return An instance of Jterator&lt;String&gt;
     */
    public Jterator<String> iterator(String ColumnName);


    /**
     * Performs a search in the table. Searches the entries of the column defined
     * by SearchColumn to check if its entries resemble a given keyword and returns
     * an iterator over the corresponding entries of the column specified by the
     * argument IterableColumn. The search is performed by a database SQL-type
     * query containing  'LIKE %...%'.
     * @param IterableCoumn The column of the table on whose entries the iterator
     * takes values.
     * @param SearchColumn The column of the table which should be searched.
     * @param keyword A keyword
     * @return An instance of a Jterator (in fact this is an Iterator) which can
     * be used to iterate over the search results.
     */
    public Jterator<String> search(String IterableCoumn, String SearchColumn, String keyword);

    /**
     * Creates the table.
     */
    @CreateTable
    public void create();
        

}
