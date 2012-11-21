package org.systemsbiology.uniprot;

import org.systemsbiology.xtandem.peptide.*;

import java.io.*;
import java.util.*;

/**
 * org.systemsbiology.uniprot.ProteinDatabase
 * User: Steve
 * Date: 11/21/12
 */
public class ProteinDatabase {
    public static final ProteinDatabase[] EMPTY_ARRAY = {};

    private static ProteinDatabase gInstance;

    public static ProteinDatabase getInstance() {
        if(gInstance == null)
            gInstance = new ProteinDatabase();
        return gInstance;
    }

    public static final String DATABASE_FILE = "uniprot/uniprot_sprot.fasta";

    private final Map<String,Protein>   m_IdToSequence = new HashMap<String,Protein>();

    private ProteinDatabase() {
    }

    protected void guaranteeDatabasePopulated()
    {
        if(!m_IdToSequence.isEmpty())
            return;
        populateDatabase();
    }

    public Protein getProtein(String id) {
        guaranteeDatabasePopulated();
        return m_IdToSequence.get(id);
    }

    public static final int NUMBER_FASTA_SPLITS = 64; // make it easy to search for sequences
    public static final int START_ID = 4;
    protected void populateDatabase()
    {
        try {
            LineNumberReader rdr = new LineNumberReader(new FileReader(DATABASE_FILE)) ;
            String line = rdr.readLine();
            StringBuilder sb = new StringBuilder();

            String sequence = sb.toString();
             String id = null;
            String annotation = null;
             while(line != null) {
                if(line.startsWith(">"))  {
                    if(sb.length() > 0)  {
                        Protein p = Protein.buildProtein(id,annotation,sb.toString(),DATABASE_FILE);
                        m_IdToSequence.put(id,p);
                        id = null;
                        annotation = null;
                        sb.setLength(0);
                    }
                    annotation = line;
                    int endId = line.indexOf("|",START_ID + 1);
                    id = line.substring(START_ID,endId);
                }
                 else {
                    sb.append(line);
                }
                 line = rdr.readLine();
            }
            if(sb.length() > 0)  {
                Protein p = Protein.buildProtein(id,annotation,sb.toString(),DATABASE_FILE);
                m_IdToSequence.put(id,p);
                id = null;
                annotation = null;
                sb.setLength(0);
            }

        }
        catch (IOException e) {
            throw new RuntimeException(e);

        }


    }

    public static void main(String[] args) {
         ProteinDatabase pd = ProteinDatabase.getInstance();
        Protein p = pd.getProtein("O00154");
        if(p == null)
            throw new IllegalStateException("problem"); // ToDo change
        p = pd.getProtein("O00154");
               if(p == null)
                   throw new IllegalStateException("problem"); // ToDo change
        p = pd.getProtein("O00625");
               if(p == null)
                   throw new IllegalStateException("problem"); // ToDo change
        p = pd.getProtein("A2RUC4");
               if(p == null)
                   throw new IllegalStateException("problem"); // ToDo change
    }

}
