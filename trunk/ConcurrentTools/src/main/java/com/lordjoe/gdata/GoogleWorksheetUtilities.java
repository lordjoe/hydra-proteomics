package com.lordjoe.gdata;

import com.google.gdata.data.spreadsheet.*;



/**
 * com.lordjoe.gdata.GoogleWorksheetUtilities
 * User: Steve
 * Date: 4/6/12
 */
public class GoogleWorksheetUtilities {
    public static final GoogleWorksheetUtilities[] EMPTY_ARRAY = {};

    public static String getCellValue(CellEntry entry) {
        Cell cell = entry.getCell();
         String title = cell.getValue();
        return title;
    }

    public static int getCellRow(CellEntry entry) {
        String id = entry.getId();
        String shortId = id.substring(id.lastIndexOf('/') + 1);
        int index = shortId.indexOf("R") + 1;
        int end = shortId.indexOf("C") ;

         int s = Integer.parseInt(shortId.substring(index, end));
        return s;
    }

    public static int getCellColumn(CellEntry entry) {
        String id = entry.getId();
        String shortId = id.substring(id.lastIndexOf('/') + 1);
        int index = shortId.indexOf("C") + 1;

         int s = Integer.parseInt(shortId.substring(index ));
        return s;
    }


}
