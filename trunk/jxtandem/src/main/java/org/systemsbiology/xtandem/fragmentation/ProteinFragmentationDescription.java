package org.systemsbiology.xtandem.fragmentation;

import com.lordjoe.utilities.*;
import org.systemsbiology.jmol.*;
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

    private final ProteinCollection m_Parent;
    private final String m_UniprotId;
    private Protein m_Protein;
    private final List<ProteinFragment> m_Fragments = new ArrayList<ProteinFragment>();
    private short[] m_Coverage;
    private double m_FractionalCoverage; // fraction of amino acids in any fragment
    private PDBObject m_Model;

    public ProteinFragmentationDescription(final String uniprotId, ProteinCollection parent) {
        m_UniprotId = uniprotId;
        m_Parent = parent;
        m_Protein = parent.getProtein(uniprotId);
    }

    public String getUniprotId() {
        return m_UniprotId;
    }

    public ProteinCollection getParent() {
        return m_Parent;
    }

    public Protein getProtein() {
        return m_Protein;
    }

    public void setProtein(final Protein protein) {
        m_Protein = protein;
    }

    public PDBObject getModel() {
        return m_Model;
    }

    public void setModel(PDBObject model) {
        m_Model = model;
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
        ProteinFragment pf = new ProteinFragment(protein, fragment);
        m_Fragments.add(pf);

    }

    public ProteinFragment[] getFragments() {
        ProteinFragment[] proteinFragments = m_Fragments.toArray(ProteinFragment.EMPTY_ARRAY);
        return proteinFragments;
    }

    public void guaranteeFragments() {
        File fragmentsDirectory = getParent().getFragmentDirectory();
        if (!m_Fragments.isEmpty())
            return;
        File fragmentsFile = new File(fragmentsDirectory, getUniprotId() + ".fragments");
        if (fragmentsFile.exists() && fragmentsFile.length() > MINIMUM_LENGTH) {
            String[] lines = FileUtilities.readInLines(fragmentsFile);
            buildFragments(lines);
            return;
        }
        String[] lines = downloadProteinFragments();
        if (lines.length < 2) {
            System.out.println("No fragments for " + getUniprotId());
        }
        FileUtilities.writeFileLines(fragmentsFile, lines);
        buildFragments(lines);
        System.out.println(" wrote " + (lines.length - 1) + " fragments for " + getUniprotId());

    }


    public  Map<ProteinFragment,AminoAcidAtLocation[]> getAminoAcidLocations()
    {
        PDBObject model = getModel();
        Map<ProteinFragment,AminoAcidAtLocation[]> ret = new HashMap<ProteinFragment, AminoAcidAtLocation[]>();
         if(model == null)
             return ret;
         ProteinFragment[] frage = getFragments();
        for (int i = 0; i < frage.length; i++) {
            ProteinFragment pf = frage[i];
            AminoAcidAtLocation[] aas = model.getAminoAcidsForSequence(pf.getSequence());
            if(aas == null || aas.length == 0)
                continue;
            ret.put(pf,aas);
        }
        return ret;
    }

    public void guaranteeCoverage() {
        if (m_Coverage != null)
            return;
        buildCoverage();
    }

    public int getCoverage(int index) {
       guaranteeCoverage();
        if(index < 0 || index >= m_Coverage.length)
            return 0;
        return m_Coverage[index];
    }

    public short[] getAllCoverage()
    {
        guaranteeCoverage();
           return m_Coverage ;

    }

    public double getFractionalCoverage() {
        guaranteeCoverage();
        return m_FractionalCoverage;
    }

    public void buildCoverage() {
        ProteinFragment[] fragments = getFragments();
        Protein protein = getProtein();
        int length = protein.getSequence().length();
        m_Coverage = new short[length];
        int nCovered = 0;
        for (int i = 0; i < m_Coverage.length; i++) {
            short i1 = computeCoverage(i, fragments);
            if(i1 > 0)
                nCovered++;
            m_Coverage[i] = i1;

        }
        m_FractionalCoverage = (double)nCovered / (double )length;

    }

    private short computeCoverage(int i,ProteinFragment[] fragments) {
        short ret = 0;
        for (int j = 0; j < fragments.length; j++) {
            ProteinFragment fragment = fragments[j];
            if(fragment.containsPosition(i))
                ret++;
        }
        return ret;
    }


//    public static void main(String[] args) throws Exception {
//        downloadProteinFragments(args[0]);
//    }

}
