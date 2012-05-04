package com.lordjoe.dataapi;

import com.lordjoe.gdata.*;

import java.io.*;

/**
 * com.lordjoe.dataapi.DataUtilities
 * A collection of routines operating on a table
 * User: Steve
 * Date: 4/11/12
 */
public  class DataUtilities {
    public static final DataUtilities[] EMPTY_ARRAY = {};


    public static void showCells(IDataTable table) {
       showCells(table, System.out);

    }

    public static void showCells(IDataTable table, PrintStream out) {
        IDataCell[] allNonEnptyCells = table.getAllNonEnptyCells();
        for (int i = 0; i < allNonEnptyCells.length; i++) {
            IDataCell cell = allNonEnptyCells[i];
            showCell(  cell,   out);
        }
    }

    public static void showCell(IDataCell cell) {
       showCell(cell,System.out);

    }

    public static void showCell(IDataCell cell, PrintStream out) {
        int col = cell.getCol();
        int row = cell.getRow();
        String value = cell.getValue();
        out.println("" + row + ":" + col + "=" + value);
    }


    public static void showRow(IDataTable table, int row) {
        showRow(table, row, System.out);
    }


    public static void showRow(IDataTable table, int row, PrintStream out) {
        RowData data = table.getRow(row);
        for (IColumn col : table.getColumns()) {
            if (col == null)
                continue;
            String value = data.getCell(col.getName());
            out.println(col.toString() + " " + value);
        }

    }

    public static void showRow(RowData data, PrintStream out) {
        for (IColumn col : data.getColumns()) {
            if (col == null)
                continue;
            String value = data.getCell(col.getName());
            if (value != null)
                out.print(value);
            out.print("\t");
        }
        out.println();
    }

    public static int showRows(IDataTable table) {
        return showRows(table, System.out);
    }

    public static int showRows(IDataTable table, PrintStream out) {

        IColumn[] columns = table.getColumns();
        for (IColumn col : columns) {
            if (col == null)
                continue;
            out.print(col.toString() + "\t");
        }
        out.println();
        RowData[] rd = table.getAllRows();

        int lastFiledRow = 1;
        for (int i = 0; i < rd.length; i++) {
            RowData data = rd[i];
            if (data == null)
                continue;
            lastFiledRow = i;
            showRow(data, out);
        }
        return lastFiledRow;
    }


    public static int showRows(final GoogleWorksheet pWs) {
        if(true)
            throw new UnsupportedOperationException("Fix This"); // ToDo
        return 0;
    }
}
