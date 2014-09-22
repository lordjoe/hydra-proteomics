package com.lordjoe.gdata;

/**
 * com.lordjoe.gdata.InterestingVariant
 * User: Steve
 * Date: 7/23/12
 */
public class InterestingVariant {
    public static final InterestingVariant[] EMPTY_ARRAY = {};

    public static void main(String[] args) {
        GoogleSpreadSheetManager mgr = GoogleSpreadSheetManager.getInstance();
          mgr.login();
          GoogleSpreadSheet ss = mgr.getSpreadSheet("Walters Interesting SNPS");
          GoogleWorksheet ws = ss.getFirstWorksheet();
          GoogleColumn[] columns = ws.getColumns();
        for (int row = 1; row <= ws.getRowCount(); row++) {
            for (int col = 1; col <= columns.length; col++) {
                 String cell = ws.getCell(row, col);
                if(col > 1)
                    System.out.print(",");
                System.out.print(  cell);
             }
            System.out.println();
        }

    }

}
