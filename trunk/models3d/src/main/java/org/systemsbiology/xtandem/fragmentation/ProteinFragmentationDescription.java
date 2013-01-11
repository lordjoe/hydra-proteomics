package org.systemsbiology.xtandem.fragmentation;

import com.lordjoe.utilities.*;
import org.systemsbiology.jmol.*;
import org.systemsbiology.xtandem.*;
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

    public static Comparator<? super ProteinFragmentationDescription> INTERESTING_COMPARATOR =
            new InterestingComparator();


    /**
     * we are interested in interesting PFDS
     */
    private static class InterestingComparator implements Comparator<ProteinFragmentationDescription> {
        @Override
        public int compare(ProteinFragmentationDescription o1, ProteinFragmentationDescription o2) {
            int int1 = o1.getInterestScore();
            int int2 = o2.getInterestScore();
            if (int1 != int2)
                return int1 > int2 ? -1 : 1;
            return o1.getUniprotId().compareTo(o2.getUniprotId());
        }
    }

    public static final int MINIMUM_LENGTH = 20;

    private final ProteinCollection m_Parent;
    private final String m_UniprotId;
    private Protein m_Protein;
    private final Set<ProteinFragment> m_Fragments = new HashSet<ProteinFragment>();
    private short[] m_Coverage;
    private double m_FractionalCoverage; // fraction of amino acids in any fragment
    private File m_ModelFile;
    private PDBObject m_Model;
    private SequenceChainMap[] m_SequenceFromSwissProt;

    private final CoverageStatistics m_Statistics;

    public ProteinFragmentationDescription(final String uniprotId, ProteinCollection parent) {
        m_UniprotId = uniprotId;
        m_Parent = parent;
        m_Protein = parent.getProtein(uniprotId);
        m_Statistics = new CoverageStatistics(this);
    }

    public ProteinFragmentationDescription(final String uniprotId, ProteinCollection parent, Protein protein, FoundPeptide[] peptides) {
        m_UniprotId = uniprotId;
        m_Parent = parent;
        m_Protein = protein;
        m_Statistics = new CoverageStatistics(this, peptides);
        int index = 0;
        for (int i = 0; i < peptides.length; i++) {
            FoundPeptide peptide = peptides[i];
            index = buildFragment(peptide, index);
        }
        if (parent == null) {
            m_SequenceFromSwissProt = buildChainAMapping(protein);
        }
    }

    private SequenceChainMap[] buildChainAMapping(final Protein protein) {
        List<SequenceChainMap> holder = new ArrayList<SequenceChainMap>();
        String sequence = protein.getSequence();
        char[] chars = sequence.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            try {
                FastaAminoAcid aa = FastaAminoAcid.fromChar(c);
                SequenceChainMap sm = new SequenceChainMap(protein, i + 1, aa);
                holder.add(sm);
            }
            catch (BadAminoAcidException e) {
                SequenceChainMap sm = new SequenceChainMap(protein, i + 1, FastaAminoAcid.UNKNOWN);
                holder.add(sm);

            }
        }
        SequenceChainMap[] ret = new SequenceChainMap[holder.size()];
        holder.toArray(ret);
        return ret;
    }

    public int getInterestScore() {
        if (getFractionalCoverage() < 0.2)
            return 0;
        if (getFragments().length < 5)
            return 0;
        CoverageStatistics statistics = getStatistics();
        CoverageStatistics.PartitionStatistics ps = statistics.getPartitionStatistics(2);

        if (ps.getLowestOverHighest() == 0)
            return 0;
        return (int) (100 - (100 * (ps.getLowestOverHighest())));
    }

    public CoverageStatistics getStatistics() {
        return m_Statistics;
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

    public File getModelFile() {
        return m_ModelFile;
    }

    public void setModelFile(File modelFile) {
        m_ModelFile = modelFile;
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
        int index = 0;
        for (int i = 1; i < lines.length; i++) {
            String line = lines[i];
            index = buildFragment(line, index);
        }
    }

    private int buildFragment(final String line, int index) {
        Protein protein = getProtein();
        String[] items = line.split(",");
        String sequence = items[1].trim();

        Polypeptide fragment = new Polypeptide(sequence);
        ProteinFragment pf = new ProteinFragment(protein, fragment, index);
        return addFragment(protein, fragment, index);
    }

    public int addFragment(Protein protein, IPolypeptide fragment, int index) {
        ProteinFragment pf = new ProteinFragment(protein, fragment, index);
        if (!m_Fragments.contains(pf)) {
            m_Fragments.add(pf);
            return ++index;
        }
        else {
            return index;
        }
    }

    public ProteinFragment[] getFragments() {
        guaranteeFragments();
        ProteinFragment[] proteinFragments = m_Fragments.toArray(ProteinFragment.EMPTY_ARRAY);
        Arrays.sort(proteinFragments);
        return proteinFragments;
    }

    public void guaranteeFragments() {
        if (!m_Fragments.isEmpty())
            return;
        File fragmentsDirectory = getParent().getFragmentDirectory();
        File fragmentsFile = new File(fragmentsDirectory, getUniprotId() + ".fragments");
        if (fragmentsFile.exists() && fragmentsFile.length() > MINIMUM_LENGTH) {
            String[] lines = FileUtilities.readInLines(fragmentsFile);
            buildFragments(lines);
            if (!m_Fragments.isEmpty())
                return;

            return;
        }
        String[] lines = downloadProteinFragments();
        if (lines == null) {
            lines = downloadProteinFragments();
            throw new UnsupportedOperationException("Fix This"); // ToDo
        }
        if (lines.length < 2) {
            System.out.println("No fragments for " + getUniprotId());
        }
        FileUtilities.writeFileLines(fragmentsFile, lines);
        buildFragments(lines);
        System.out.println(" wrote " + (lines.length - 1) + " fragments for " + getUniprotId());

    }


    public Map<ProteinFragment, IAminoAcidAtLocation[]> getAminoAcidLocations() {
        PDBObject model = getModel();
        ProteinFragment[] frage = getFragments();
        Map<ProteinFragment, IAminoAcidAtLocation[]> ret = new HashMap<ProteinFragment, IAminoAcidAtLocation[]>();
        if (model != null)
            buildAminoAcidLocationsFromModel(model, frage, ret);
        else
            buildAminoAcidLocationsWithoutModel(frage, ret);
        return ret;
    }

    protected void buildAminoAcidLocationsFromModel(PDBObject model, ProteinFragment[] frage, Map<ProteinFragment, IAminoAcidAtLocation[]> ret) {
        for (int i = 0; i < frage.length; i++) {
            ProteinFragment pf = frage[i];
            IAminoAcidAtLocation[] aas = new IAminoAcidAtLocation[0];
            String sequence = pf.getSequence();
            try {
                aas = model.getAminoAcidsForSequence(sequence);
            }
            catch (IllegalArgumentException e) {
                if (e.getMessage().startsWith("Invalid amino acid character"))
                    continue;
                throw new RuntimeException(e);

            }
            if (aas == null || aas.length == 0) {
                aas = model.getAminoAcidsForSequence(sequence);  // break here
                continue;
            }
            ret.put(pf, aas);
        }

    }


    protected void buildAminoAcidLocationsWithoutModel(ProteinFragment[] frage, Map<ProteinFragment, IAminoAcidAtLocation[]> ret) {
        for (int i = 0; i < frage.length; i++) {
            ProteinFragment pf = frage[i];
            String sequence1 = pf.getSequence();
            int startLocation = pf.getStartLocation();
            IAminoAcidAtLocation[] aas = new IAminoAcidAtLocation[sequence1.length()];
            for (int j = 0; j < aas.length; j++) {
                // note locations are 1 based
                ProteinAminoAcid pa = new ProteinAminoAcid(ChainEnum.A, FastaAminoAcid.fromChar(sequence1.charAt(j)), startLocation + j + 1 );
                aas[j] = new AminoAcidAtLocation(ChainEnum.A, pa);
            }
            ret.put(pf, aas);
        }

    }

    public void guaranteeCoverage() {
        if (m_Coverage != null)
            return;
        buildCoverage();
    }

    public int getCoverage(int index) {
        guaranteeCoverage();
        if (index < 0 || index >= m_Coverage.length)
            return 0;
        return m_Coverage[index];
    }

    public short[] getAllCoverage() {
        guaranteeCoverage();
        return m_Coverage;

    }

    public double getFractionalCoverage() {
        guaranteeCoverage();
        return m_FractionalCoverage;
    }

    private int buildFragment(final FoundPeptide peptide, int index) {
        Protein protein = getProtein();
        index = addFragment(protein, peptide.getPeptide(), index);
        return index;
    }


    public void buildCoverage(FoundPeptide[] peptides) {
        m_Fragments.clear();
        int index = 0;
        for (int i = 0; i < peptides.length; i++) {
            FoundPeptide peptide = peptides[i];
            index = buildFragment(peptide, index);
        }
        buildCoverage();
    }

    public void buildCoverage() {
        Map<ProteinFragment, IAminoAcidAtLocation[]> aminoAcidLocations = getAminoAcidLocations();
        ProteinFragment[] proteinFragments = aminoAcidLocations.keySet().toArray(ProteinFragment.EMPTY_ARRAY);
        Arrays.sort(proteinFragments,ProteinFragment.START_LOCATION_COMPARATOR); // sort by stare location

        Protein protein = getProtein();
        int length = protein.getSequence().length();
        m_Coverage = new short[length];
        int nCovered = 0;

        for (int i = 0; i < proteinFragments.length; i++) {
            ProteinFragment pf = proteinFragments[i];
            IAminoAcidAtLocation[] highlited = aminoAcidLocations.get(pf);
            for (int j = 0; j < highlited.length; j++) {
                IAminoAcidAtLocation aa = highlited[j];
                int location = aa.getLocation() - 1;  // NOTE - location is 1 based
                if (m_Coverage[location] == 0)
                    nCovered++;
                m_Coverage[location]++;

            }
        }
//       Old code
//        ProteinFragment[] fragments = getFragments();
//        for (int i = 0; i < m_Coverage.length; i++) {
//            short i1 = computeCoverage(i, fragments);
//            if (i1 > 0)
//                nCovered++;
//            m_Coverage[i] = i1;
//
//        }
        m_FractionalCoverage = (double) nCovered / (double) length;

    }

    private short computeCoverage(int i, ProteinFragment[] fragments) {
        short ret = 0;
        for (int j = 0; j < fragments.length; j++) {
            ProteinFragment fragment = fragments[j];
            if (fragment.containsPosition(i))
                ret++;
        }
        return ret;
    }

    public SequenceChainMap[] getSequenceFromSwissProt() {
        return m_SequenceFromSwissProt;
    }

    public void setSequenceFromSwissProt(final SequenceChainMap[] sequenceFromSwissProt) {
        m_SequenceFromSwissProt = sequenceFromSwissProt;
    }

    public SequenceChainMap[] getChainMappings() {
        PDBObject model = getModel();
        if (model == null) {
            return m_SequenceFromSwissProt;
        }
        SequenceChainMap[] mappings = model.getMappings();
        return mappings;
    }


//    public static void main(String[] args) throws Exception {
//        downloadProteinFragments(args[0]);
//    }

}
