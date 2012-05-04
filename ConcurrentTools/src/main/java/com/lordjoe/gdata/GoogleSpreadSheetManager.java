package com.lordjoe.gdata;

import com.google.gdata.client.spreadsheet.*;
import com.google.gdata.data.spreadsheet.*;
import com.google.gdata.util.*;
import com.lordjoe.credentials.*;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 * com.lordjoe.gdata.GoogleSpreadSheetManager
 * User: Steve
 * Date: 4/6/12
 */
public class GoogleSpreadSheetManager {
    public static final GoogleSpreadSheetManager[] EMPTY_ARRAY = {};

    private static GoogleSpreadSheetManager gInstance;

    public static GoogleSpreadSheetManager getInstance() {
        if (gInstance == null)
            gInstance = new GoogleSpreadSheetManager();
        return gInstance;
    }

    /**
     * Our view of Google Spreadsheets as an authenticated Google user.
     */
    private final SpreadsheetService m_Service = new SpreadsheetService("Documents");

    /**
     * The URL of the cells feed.
     */
    // private final URL m_CellFeedUrl;

    /**
     * A factory that generates the appropriate feed URLs.
     */
    private final FeedURLFactory m_Factory = FeedURLFactory.getDefault();
    private boolean m_LoggedIn;
    private Map<String, GoogleSpreadSheet> m_Sheets = new HashMap<String, GoogleSpreadSheet>();

    private GoogleSpreadSheetManager() {

    }


    public boolean isLoggedIn() {
        return m_LoggedIn;
    }

    public void setLoggedIn(final boolean pLoggedIn) {
        m_LoggedIn = pLoggedIn;
    }


    public void guaranteeLoggedIn() {
        if (!isLoggedIn())
            login();
    }


    /**
     * Log in to Google, under the Google Spreadsheets account.
     *
     * @param username name of user to authenticate (e.g. yourname@gmail.com)
     * @param password password to use for authentication
     * @throws com.google.gdata.util.AuthenticationException
     *          if the service is unable to validate the
     *          username and password.
     */
    public void login() {
        Credentials cred = Credentials.getInstance();
        String username = cred.getCredentials(CredentialType.GoogleDataUser);
        String password = cred.getCredentials(CredentialType.GoogleDataPassword);

        try {
// Authenticate
            getService().setUserCredentials(username, password);
            setLoggedIn(true);
        }
        catch (AuthenticationException e) {
            setLoggedIn(false);
            throw new GoogleDataException(e);

        }
    }

    public void guaranteeSpreadSheets() {
        if (!m_Sheets.isEmpty())
            return;
        try {
// Get the spreadsheet to load
            guaranteeLoggedIn();
            URL spreadsheetsFeedUrl = getFactory().getSpreadsheetsFeedUrl();
            SpreadsheetFeed feed = getService().getFeed(spreadsheetsFeedUrl, SpreadsheetFeed.class);
            List<SpreadsheetEntry> spreadsheets = feed.getEntries();
            for (SpreadsheetEntry spr : spreadsheets) {
                GoogleSpreadSheet sheet = new GoogleSpreadSheet(spr, this);
                m_Sheets.put(sheet.getTitle(), sheet);
            }

        }
        catch (IOException e) {
            throw new GoogleDataException(e);

        }
        catch (ServiceException e) {
            throw new GoogleDataException(e);

        }

    }


    public GoogleSpreadSheet[] getSpreadSheets() {
        guaranteeSpreadSheets();
        GoogleSpreadSheet[] googleSpreadSheets = m_Sheets.values().toArray(GoogleSpreadSheet.EMPTY_ARRAY);
        Arrays.sort(googleSpreadSheets);
        return googleSpreadSheets;
    }


    public GoogleSpreadSheet getSpreadSheet(String title) {
        guaranteeSpreadSheets();
        return m_Sheets.get(title);
    }


    public SpreadsheetService getService() {
        return m_Service;
    }

//    public URL getCellFeedUrl() {
//        return m_CellFeedUrl;
//    }

    public FeedURLFactory getFactory() {
        return m_Factory;
    }

    /**
     * write the names of existing spreadsheets to System.out
     */
    public void showSpreadsheets() {
        showSpreadsheets(System.out);
    }


    /**
     * write the names of existing spreadsheets to System.out
     *
     * @param app !null appendable
     */
    public void showSpreadsheets(Appendable app) {
        try {
            GoogleSpreadSheet[] spreadSheets = getSpreadSheets();
            for (int i = 0; i < spreadSheets.length; i++) {
                GoogleSpreadSheet spreadSheet = spreadSheets[i];
                app.append(spreadSheet.getTitle() + "\n");
            }
        }
        catch (IOException e) {
            throw new GoogleDataException(e);

        }

    }

    public static void main(String[] args) {
        GoogleSpreadSheetManager mgr = GoogleSpreadSheetManager.getInstance();
        mgr.login();
        GoogleSpreadSheet ss = mgr.getSpreadSheet("TestAccess");
        GoogleWorksheet ws = ss.getFirstWorksheet();
        GoogleColumn[] columns = ws.getColumns();
        ws.showRows();
          ws.addRandomRow();
        //ws.addIncrementingRow();
        System.out.println("++++++++++++++++++++++++++");
        int lastRow = ws.showRows();
        System.out.println("++++++++++++++++++++++++++");
          int cols = ws.getColCount();
        for (int i = 0; i < cols; i++) {
            String val = ws.getCell(lastRow, i + 1);
            if(val != null)
                System.out.print(val);
            System.out.print("\t");

        }
        System.out.println();
    }
}
