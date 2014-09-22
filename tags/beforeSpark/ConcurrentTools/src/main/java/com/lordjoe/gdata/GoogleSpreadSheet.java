package com.lordjoe.gdata;

import com.google.gdata.client.spreadsheet.*;
import com.google.gdata.data.spreadsheet.*;
import com.google.gdata.util.*;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 * com.lordjoe.gdata.GoogleSpreadSheet
 * User: Steve
 * Date: 4/6/12
 */
public class GoogleSpreadSheet implements Comparable<GoogleSpreadSheet> {
    public static final GoogleSpreadSheet[] EMPTY_ARRAY = {};


    private final SpreadsheetEntry m_SpreadSheet;
    private final GoogleSpreadSheetManager m_Manager;
    private final Map<String, GoogleWorksheet> m_Sheets = new HashMap<String, GoogleWorksheet>();
    private final List< GoogleWorksheet> m_SheetList = new ArrayList<GoogleWorksheet>();

    public GoogleSpreadSheet(final SpreadsheetEntry pSpreadSheet, final GoogleSpreadSheetManager pManager) {
        m_SpreadSheet = pSpreadSheet;
        m_Manager = pManager;
    }

    public SpreadsheetEntry getSpreadSheet() {
        return m_SpreadSheet;
    }

    public GoogleSpreadSheetManager getManager() {
        return m_Manager;
    }

    public String getTitle() {
        return m_SpreadSheet.getTitle().getPlainText();
    }

    @Override
    public String toString() {
        return getTitle();
    }


    public void guaranteeWorkSheets() {
        if (!m_Sheets.isEmpty())
            return;
        try {
            SpreadsheetEntry spreadsheet = getSpreadSheet();
            // Get the worksheet to load
            List<WorksheetEntry> worksheets1 = spreadsheet.getWorksheets();
            int index = 1;
            for(WorksheetEntry we : worksheets1) {
                GoogleWorksheet ws = new GoogleWorksheet(this,we,index++);
                m_SheetList.add(ws);
            }
         }
        catch (IOException e) {
            throw new GoogleDataException(e);

        }
        catch (ServiceException e) {
            throw new GoogleDataException(e);

        }

    }


    public GoogleWorksheet[] getWorksheets() {
        guaranteeWorkSheets();
        GoogleWorksheet[] googleSpreadSheets = m_SheetList.toArray(GoogleWorksheet.EMPTY_ARRAY);
        Arrays.sort(googleSpreadSheets);
        return googleSpreadSheets;
    }


    public SpreadsheetService getService() {
        return GoogleSpreadSheetManager.getInstance().getService();
    }

    public GoogleWorksheet getFirstWorksheet() {
        guaranteeWorkSheets();
        return m_SheetList.get(0);
    }

    public GoogleWorksheet getWorksheets(String title) {
        guaranteeWorkSheets();
        return m_Sheets.get(title);
    }


    @Override
    public int compareTo(final GoogleSpreadSheet o) {
        return getTitle().compareTo(o.getTitle());
    }
}
