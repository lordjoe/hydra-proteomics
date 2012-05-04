package com.lordjoe.dataapi;

import com.lordjoe.gdata.*;

import java.util.*;

/**
 * com.lordjoe.dataapi.IDataTable
 * User: Steve
 * Date: 4/11/12
 */
public interface IDataTable {
    public static final IDataTable[] EMPTY_ARRAY = {};

    /**
     * return all columns sorte by index
     * @return
     */
    public IColumn[] getColumns();

    /**
     * look up a column by name
     * @param title  !null name
     * @return   possibly null column
     */
    public IColumn getColumn(String title);

    /**
      * look up a column by index  note first is 1
      * @param title  !null index
      * @return   possibly null column
      */
    public IColumn getColumn(Integer index);

    /**
      * get all data in a row
      * @param row  row number - the first row is 2 since 1 is column headers
      * @return   possibly null row - null says no such row
      */
    public RowData getRow(int row);

    /**
     * ger rows from start to and including end
     * @param start start row - first data row is 2
     * @param end  end row - will include this row
     * @return Array of rows - those without data will be null
     */
    public RowData[] getRows(int start,int end);

    /**
     * get all rows in the table - starting at 2 and goinr to the last row containing data
     * @return !null array - may have null entries
     */
    public RowData[] getAllRows( );

    /**
     * get the number of rows including headers - guaranteed to include all rows but some may be empty
     * @return
     */
    public int getRowCount();


    /**
     * get the number of columns including headers - guaranteed to include all rows but some may be empty
     * @return
     */
    public int getColCount();

    /**
       * Sets the particular cell at row, col to the specified formula or value.
       *
       * @param row            the row number, starting with 1   but first data row is 2
       * @param col            the column number, starting with 1
       * @param formulaOrValue the value if it doesn't start with an '=' sign; if it
       *                       is a formula, be careful that cells are specified in R1C1 format
       *                       instead of A1 format.
       * @throws com.google.gdata.util.ServiceException
       *                             when the request causes an error in the Google
       *                             Spreadsheets service.
       * @throws java.io.IOException when an error occurs in communication with the Google
       *                             Spreadsheets service.
       */
        public void setCell(int row, int col, String formulaOrValue);

    /**
      * return all non-null cells in the declared space
      * @return !null array
      */
       public IDataCell[] getAllNonEnptyCells();

    /**
      * return all cells in the declared space
      * @return !null array
      */
       public IDataCell[] getAllCells();


}
