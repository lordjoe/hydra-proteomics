package com.lordjoe.dataapi;



import java.util.*;

/**
 * com.lordjoe.dataapi.RowData
 * User: Steve
 * Date: 4/11/12
 */
public class RowData {
    public static final RowData[] EMPTY_ARRAY = {};

    private final int m_Row;
    private final Map<IColumn, String> m_Data = new HashMap<IColumn, String>();
    private final IDataTable m_Table;

    public RowData(final int pRow,  IDataTable table) {
        m_Row = pRow;
        m_Table = table;
      }

    public int getRow() {
        return m_Row;
    }

    public IColumn[] getColumns() {
        return getTable().getColumns();

    }

    public IDataTable getTable() {
        return m_Table;
    }

    public void setCell(String name, String value)  {
         IColumn col = getTable().getColumn(name);
         m_Data.put(col,value);
    }

    public void setCell(Integer name, String value)  {
         IColumn col = getTable().getColumn(name);
         m_Data.put(col,value);
    }
    public String getCell(String name )  {
          IColumn col = getTable().getColumn(name);
          return m_Data.get(col );
     }


    public String getCell(Integer name )  {
          IColumn col = getTable().getColumn(name);
          return m_Data.get(col );
     }

}
