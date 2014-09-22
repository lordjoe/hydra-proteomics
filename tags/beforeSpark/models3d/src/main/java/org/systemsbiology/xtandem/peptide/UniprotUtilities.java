package org.systemsbiology.xtandem.peptide;

import com.lordjoe.utilities.*;

import java.io.*;
import java.util.*;

/**
 * org.systemsbiology.xtandem.peptide.UniprotUtilities
 * User: steven
 * Date: 3/20/13
 */
public class UniprotUtilities {
    public static final UniprotUtilities[] EMPTY_ARRAY = {};

    public static final String ID_ITEM_SEPARATOR = "@@@" ;
     public static final String ID_ITEM_LINE_SEPARATOR = "||" ;


    private static File gStringtoUniprotIdFile = new File("StringToUniprot.tsv");
    private static File gFailedSearchFile = new File("UniprotFailedSearch.txt");
    private static boolean gStringtoUniprotIdFileRead;
    private static boolean gStringtoUniprotIdFileDirty;
    private static boolean gFailedSearchFileDirty;
     private static final Map<String,String> gIdToUniprotId = new HashMap<String,String>( );
    private static final Set<String> gUniprotFailedSearch = new HashSet<String>( );

    public static File getStringtoUniprotIdFile() {
        return gStringtoUniprotIdFile;
    }

    public static void setStringtoUniprotIdFile(File stringtoUniprotIdFile) {
        gStringtoUniprotIdFile = stringtoUniprotIdFile;
        gStringtoUniprotIdFileRead = false;
    }

    public static File getFailedSearchFile() {
        return gFailedSearchFile;
    }

    public static void setFailedSearchFile(File failedSearchFile) {
        gFailedSearchFileDirty = false;
        gFailedSearchFile = failedSearchFile;
    }

    public static void guaranteeStringtoUniprotIdFile( ) {
        if(gStringtoUniprotIdFileRead)
            return;
        gStringtoUniprotIdFileRead = true;
        if(gStringtoUniprotIdFile.exists())   {
            String[] lines = FileUtilities.readInLines(gStringtoUniprotIdFile);
            for (int i = 0; i < lines.length; i++) {
                String line = lines[i];
                String[] items = line.split(ID_ITEM_SEPARATOR) ;
                if(items.length == 2)
                    gIdToUniprotId.put(items[0],items[1]);
            }
        }
        if(gFailedSearchFile.exists())   {
            String[] lines = FileUtilities.readInLines(gFailedSearchFile);
            gUniprotFailedSearch.addAll(Arrays.asList(lines));
        }
    }

    public static void saveStringtoUniprotIdFile( ) {
        guaranteeStringtoUniprotIdFile( );
        gStringtoUniprotIdFileRead = true;
        if(gStringtoUniprotIdFileDirty){
            try {
                PrintWriter out = new PrintWriter(getStringtoUniprotIdFile());

                for(String key : gIdToUniprotId.keySet())  {
                    out.println(key + ID_ITEM_SEPARATOR + gIdToUniprotId.get(key)  );
                }
                out.close();
            }
            catch (FileNotFoundException e) {
                throw new RuntimeException(e);

            }
        }
        if(gFailedSearchFileDirty){
             try {
                 PrintWriter out = new PrintWriter(getFailedSearchFile() );

                 for(String key : gUniprotFailedSearch)  {
                     out.println(key );
                 }
                 out.close();
             }
             catch (FileNotFoundException e) {
                 throw new RuntimeException(e);

             }
         }
     }




    public static String lookupUniProtId(String test)
    {
        guaranteeStringtoUniprotIdFile( );
        String data = gIdToUniprotId.get(test);
        return data;
    }

    /**
     * 
     * @param id it to add
     * @param uniprotId   unparsed string from uniprot
     */
    public static void addUniProtId(String id,String uniprotId)
     {
         guaranteeStringtoUniprotIdFile( );
         if(!gIdToUniprotId.containsKey(id))
             gStringtoUniprotIdFileDirty = true;
          gIdToUniprotId.put(id,uniprotId);
     }

    public static void addFailedSearch(String test)
     {
         guaranteeStringtoUniprotIdFile( );
         if(!gUniprotFailedSearch.contains(test))
             gFailedSearchFileDirty = true;
          gUniprotFailedSearch.add(test);
     }


    public static boolean isFailedSearch(String test)
     {
         guaranteeStringtoUniprotIdFile( );
         boolean contains = gUniprotFailedSearch.contains(test);
         return contains;
     }


    private static String[] DEFAULT_EXCLUDED_PROTEIN_NAME_FRAGMENTS = {"DECOY","GENEFINDER000","GENSCAN000"};
      private static final Set<String> gExcludedFragments = new HashSet<String>(Arrays.asList(DEFAULT_EXCLUDED_PROTEIN_NAME_FRAGMENTS));
      public static void addExcludedFragment(String added)
      {
          gExcludedFragments.add(added.toUpperCase());
      }
      public static boolean containsExcludedFragment(String inp)
        {
            String inpu = inp.toUpperCase();
            for(String test : gExcludedFragments){
                if(inpu.contains(test))
                    return true; // has an excluded fragment
            }
            return false; // no excluded fragment
        }




}
