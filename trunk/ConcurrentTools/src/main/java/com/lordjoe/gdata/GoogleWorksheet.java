package com.lordjoe.gdata;

import com.google.gdata.client.spreadsheet.*;
import com.google.gdata.data.spreadsheet.*;
import com.google.gdata.util.*;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 * com.lordjoe.gdata.GoogleWorksheet
 * User: Steve
 * Date: 4/6/12
 */
public class GoogleWorksheet implements Comparable<GoogleWorksheet> {
    public static final GoogleWorksheet[] EMPTY_ARRAY = {};

    private final GoogleSpreadSheet m_SpreadSheet;
    private final WorksheetEntry m_WorkSheet;
    private final int m_Index;
    private final Map<String, GoogleColumn> m_ColumnsByName = new HashMap<String, GoogleColumn>();
    private final Map<Integer, GoogleColumn> m_ColumnsByIndex = new HashMap<Integer, GoogleColumn>();
    private final URL m_CellUrl;
    private final URL m_ListUrl;

    public GoogleWorksheet(final GoogleSpreadSheet pSpreadSheet, final WorksheetEntry pManager, int index) {
        m_SpreadSheet = pSpreadSheet;
        m_WorkSheet = pManager;
        m_CellUrl = pManager.getCellFeedUrl();
        m_ListUrl = pManager.getListFeedUrl();
        int cols = getColCount();
        m_Index = index;
        readColumns();
    }

    protected void readColumns() {
        CellQuery query = new CellQuery(getCellUrl());
        int cols = getColCount();
        query.setMinimumRow(0);
        query.setMaximumRow(1);
        query.setMinimumCol(0);
        query.setMaximumCol(cols);
        try {
            CellFeed feed = getService().query(query, CellFeed.class);
            int i = 0;
            for (CellEntry entry : feed.getEntries()) {
                String id = entry.getId();
                Cell cell = entry.getCell();
                int row = GoogleWorksheetUtilities.getCellRow(entry);
                int colnum = GoogleWorksheetUtilities.getCellColumn(entry);
                String title = cell.getValue();
                GoogleColumn col = new GoogleColumn(title, this, colnum);
                m_ColumnsByName.put(title, col);
                m_ColumnsByIndex.put(colnum, col);

            }
        }
        catch (IOException e) {
            throw new GoogleDataException(e);

        }
        catch (ServiceException e) {
            throw new GoogleDataException(e);

        }
    }

    public int getIndex() {
        return m_Index;
    }

    public GoogleColumn[] getColumns() {
        GoogleColumn[] cols = m_ColumnsByName.values().toArray(GoogleColumn.EMPTY_ARRAY);
        Arrays.sort(cols);
        return cols;
    }

    public GoogleColumn getColumn(String title) {
        GoogleColumn cols = m_ColumnsByName.get(title);
        return cols;
    }

    public GoogleColumn getColumn(Integer index) {
        GoogleColumn cols = m_ColumnsByIndex.get(index);
        return cols;
    }

    public Map<GoogleColumn, String> getRow(int rowNumber) {
        Map<GoogleColumn, String> ret = new HashMap<GoogleColumn, String>();
        URL cellUrl = getCellUrl();
        CellQuery query = new CellQuery(cellUrl);
        int cols = getColCount();
        query.setMinimumRow(rowNumber);
        query.setMaximumRow(rowNumber);
        query.setMinimumCol(1);
        query.setMaximumCol(cols);
        try {
            CellFeed feed = getService().query(query, CellFeed.class);
            int i = 0;
            for (CellEntry entry : feed.getEntries()) {
                String id = entry.getId();
                Cell cell = entry.getCell();
                int row = GoogleWorksheetUtilities.getCellRow(entry);
                int colnum = GoogleWorksheetUtilities.getCellColumn(entry);
                String value = cell.getValue();
                if(value != null && value.length() > 0) {
                    GoogleColumn col = getColumn(colnum);
                      ret.put(col, value);

                }
             }
            return ret;
        }
        catch (IOException e) {
            throw new GoogleDataException(e);

        }
        catch (ServiceException e) {
            throw new GoogleDataException(e);

        }

    }

    public CellEntry[] getAllCells() {
        try {
            CellFeed feed = getService().getFeed(getCellUrl(), CellFeed.class);
            List<CellEntry> holder = new ArrayList<CellEntry>();

            CellEntry[] ret = new CellEntry[holder.size()];
            holder.toArray(ret);
            for (CellEntry entry : feed.getEntries()) {
                showCell(entry);
            }
            return ret;
        }
        catch (IOException e) {
            throw new GoogleDataException(e);

        }
        catch (ServiceException e) {
            throw new GoogleDataException(e);

        }

    }

    @Override
    public int compareTo(final GoogleWorksheet o) {
        return getId().compareTo(o.getId());
    }

    public void showCell(CellEntry entry) {
        String id = entry.getId();
        Cell cell = entry.getCell();
        int col = cell.getCol();
        int row = cell.getRow();
        String value = cell.getValue();

    }


    public void showRow(int row) {
        showRow(row, System.out);
    }


    public void showRow(int row, PrintStream out) {
        Map<GoogleColumn, String> data = getRow(row);
        for (GoogleColumn col : getColumns()) {
            if (col == null)
                continue;
            String value = data.get(col);
            out.println(col.toString() + " " + value);
        }

    }

    public int showRows( ) {
        return showRows(System.out);
    }

    public int showRows(PrintStream out) {

        GoogleColumn[] columns = getColumns();
        for (GoogleColumn col : columns) {
            if (col == null)
                continue;
            out.print(col.toString() + "\t");
        }
        out.println();
        int lastFiledRow = 1;
        int rows = getRowCount();
        for (int i = 0; i < rows; i++) {
             Map<GoogleColumn, String> data = getRow(i);
            if(data == null || data.size() == 0)
                continue;
            lastFiledRow = i ;
            for (GoogleColumn col : columns) {
                if (col == null)
                    continue;
                String value = data.get(col);
                if(value != null)
                    out.print(value );
                out.print( "\t");
             }
            out.println();
        }
        return lastFiledRow;
    }


    @Override
    public String toString() {
        String ret = getSpreadSheet().toString();
        return ret + ":" + getIndex();
    }


    public int getColCount() {
        return getWorkSheet().getColCount();
    }

    public int getRowCount() {
        return getWorkSheet().getColCount();
    }

    public String getId() {
        return getWorkSheet().getId();
    }


    public String getCell(int row, int col) {
        Map<GoogleColumn, String> ret = new HashMap<GoogleColumn, String>();
        URL cellUrl = getCellUrl();
        CellQuery query = new CellQuery(cellUrl);
        int cols = getColCount();
        query.setMinimumRow(row);
        query.setMaximumRow(row);
        query.setMinimumCol(col);
        query.setMaximumCol(col);
        try {
            CellFeed feed = getService().query(query, CellFeed.class);
            int i = 0;
            for (CellEntry entry : feed.getEntries()) {
                String id = entry.getId();
                Cell cell = entry.getCell();
                 String value = cell.getValue();
                 return value;
                }
            return null; // or maybe this is am exception
        }
        catch (IOException e) {
            throw new GoogleDataException(e);

        }
        catch (ServiceException e) {
            throw new GoogleDataException(e);

        }
      }

    /**
     * Sets the particular cell at row, col to the specified formula or value.
     *
     * @param row            the row number, starting with 1
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
    public void setCell(int row, int col, String formulaOrValue) {

        CellEntry newEntry = new CellEntry(row, col, formulaOrValue);
        SpreadsheetService service = getService();
        try {
            service.insert(getCellUrl(), newEntry);
        }
        catch (IOException e) {
            throw new GoogleDataException(e);

        }
        catch (ServiceException e) {
            throw new GoogleDataException(e);

        }
    }

    public void setHeaders(String[] values)
    {
        for (int i = 0; i < values.length; i++) {
            String value = values[i];
            setCell(1, i + 1,value);
        }
    }

    public SpreadsheetService getService() {
        return GoogleSpreadSheetManager.getInstance().getService();
    }


    public URL getListUrl() {
        return m_ListUrl;
    }

    public URL getCellUrl() {
        return m_CellUrl;
    }

    public GoogleSpreadSheet getSpreadSheet() {
        return m_SpreadSheet;
    }

    public WorksheetEntry getWorkSheet() {
        return m_WorkSheet;
    }

    public static final Random RND = new Random();

    /**
     * testcode for below
     */
    public   void addRandomRow() {
        Map<GoogleColumn, String> added = new HashMap<GoogleColumn, String>();
        for (GoogleColumn column : getColumns()) {
            added.put(column, Integer.toString(RND.nextInt(100)));
        }
        addRow(added);
    }

    /**
     * testcode for below
     */
    public   void addIncrementingRow() {
        Map<GoogleColumn, String> added = new HashMap<GoogleColumn, String>();
        int value = 10;
        for (GoogleColumn column : getColumns()) {
            added.put(column, Integer.toString(value++));
        }
        addRow(added);
    }

    /**
     *
     * @param added
     * @throws GoogleDataException
     */
    public void addRow(Map<GoogleColumn, String> added)  throws GoogleDataException
    {

        // Instantiate a SpreadsheetQuery object to retrieve spreadsheets.
        URL listFeed = getListUrl();


        ListEntry newEntry = new ListEntry();
        for (GoogleColumn column : getColumns()) {

            // Then, split by the equal sign.
            String tag = column.getName(); // such as "name"
            String value = added.get(column); // such as "Fred"

            newEntry.getCustomElements().setValueLocal(tag, value);
        }
        // Create a local representation of the new row.
        try {
            SpreadsheetService service = getService();
            service.insert(listFeed, newEntry);
        }
        catch (IOException e) {
            throw new GoogleDataException(e);

        }
        catch (ServiceException e) {
            throw new GoogleDataException(e);

        }

    }
}
