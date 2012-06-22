package org.systemsbiology.xtandem.fragmentation;

import com.lordjoe.utilities.*;
import org.systemsbiology.xtandem.peptide.*;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 * org.systemsbiology.xtandem.fragmentation.ProteinFragmentationDescription
 * User: Steve
 * Date: 6/21/12
 */
public class ProteinFragmentationDescription {
    public static final ProteinFragmentationDescription[] EMPTY_ARRAY = {};

    public static final int MINIMUM_LENGTH = 20;

     private final String m_UniprotId;
    private Protein m_Protein;
    private final List<ProteinFragment> m_Fragments = new ArrayList<ProteinFragment>();

    public ProteinFragmentationDescription(final String uniprotId) {
        m_UniprotId = uniprotId;
    }

    public String getUniprotId() {
        return m_UniprotId;
    }

    public Protein getProtein() {
        return m_Protein;
    }

    public void setProtein(final Protein protein) {
        m_Protein = protein;
    }

    public static final String ID_STRING = "%UNIPROT_ID%";
    public static final String FRAGMENT_CALL = "http://db.systemsbiology.net/sbeams/cgi/PeptideAtlas/GetPeptides?organism_id=2&biosequence_name_constraint=" + ID_STRING + "&action=QUERY&apply_action=QUERY&output_mode=csv";

    protected String[] downloadProteinFragments() {
        try {
            String uniptotid = getUniprotId();
            String urlstr = FRAGMENT_CALL.replace(ID_STRING, uniptotid);
            URL url = new URL(urlstr);
            String[] strings = FileUtilities.readInLines(url);
            return strings;
        }
        catch (MalformedURLException e) {
            throw new RuntimeException(e);

        }
    }

    protected void buildFragments(String[] lines) {
         m_Fragments.clear();
        // first line is titles
        for (int i = 1; i < lines.length; i++) {
            String line = lines[i];
            buildFragment(line);
        }
    }

    private void buildFragment(final String line) {
        Protein protein = getProtein();
         String[] items = line.split(",");
        String sequence = items[1].trim();

        Polypeptide fragment = new Polypeptide(sequence);
        ProteinFragment pf = new ProteinFragment(protein,fragment);
        m_Fragments.add(pf);

    }

    public void guaranteeFragments(File fragmentsDirectory) {
        if(!m_Fragments.isEmpty())
            return;
        File fragmentsFile = new File(fragmentsDirectory, getUniprotId() + ".fragments");
        if (fragmentsFile.exists() && fragmentsFile.length() > MINIMUM_LENGTH) {
            String[] lines = FileUtilities.readInLines(fragmentsFile);
            buildFragments(lines);
            return;
        }
        String[] lines = downloadProteinFragments();
        if (lines.length < 2)     {
            System.out.println("No fragments for " + getUniprotId());
         }
        FileUtilities.writeFileLines(fragmentsFile, lines);
        buildFragments(lines);
        System.out.println(" wrote " + (lines.length - 1) + " fragments for " + getUniprotId());

    }

//    public static void main(String[] args) throws Exception {
//        downloadProteinFragments(args[0]);
//    }

}
