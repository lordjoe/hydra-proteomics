package org.systemsbiology.jmol;


import org.biojava.bio.structure.*;
import org.biojava.bio.structure.io.*;
import org.systemsbiology.xtandem.*;
import org.systemsbiology.xtandem.peptide.*;

import java.io.*;
import java.util.*;


/**
 * org.systemsbiology.jmol.BioJavaModel
 * User: Steve
 * Date: 8/21/12
 */
public class BioJavaModel {
    public static final BioJavaModel[] EMPTY_ARRAY = {};

    private IProtein m_Protein;
    private Structure m_Structure;
    private int m_SequenceLength;
    private int m_FitLength;
    private File m_File;


    public BioJavaModel(final IProtein protein) {
        m_Protein = protein;
        m_SequenceLength = protein.getSequenceLength();
    }

    public int getSequenceLength() {
        return m_SequenceLength;
    }

    public int getFitLength() {
        return m_FitLength;
    }

    public void readFile(File f) {

        m_File = f;
        if (!f.exists())
            throw new IllegalArgumentException("file " + f + " does not exist");
        try {
            PDBFileReader rdr = new PDBFileReader();
            m_Structure = rdr.getStructure(f);
        }
        catch (IOException e) {
            throw new RuntimeException(e);

        }
    }

    public File getFile() {
        return m_File;
    }

    public IProtein getProtein() {
        return m_Protein;
    }

    public Structure getStructure() {
        return m_Structure;
    }

    public PDBHeader getHeader() {
        return getStructure().getPDBHeader();
    }

    public String getPdbCode() {
        return getStructure().getPDBCode();
    }

    public ExperimentalType getExperimentalType() {
        String technique = getHeader().getTechnique();
        return ExperimentalType.fromString(technique);
    }

    public long getId() {
        long id = getHeader().getId();
        return id;
    }


    public void showModel() {
        Structure s = getStructure();
        PDBHeader hdr = s.getPDBHeader();
        List<Compound> compounds = s.getCompounds();
        for (Iterator<Compound> iterator = compounds.iterator(); iterator.hasNext(); ) {
            Compound next = iterator.next();
            String biologicalUnit = next.getBiologicalUnit();
            String details = next.getDetails();
        }
        List<Chain> chains = s.getChains();
        for (Iterator<Chain> iterator = chains.iterator(); iterator.hasNext(); ) {
            Chain chn = iterator.next();
            List<Group> groups = chn.getAtomGroups("amino");
            String atomSequence = chn.getAtomSequence();
        }
        s = null;
    }


    public double resolveChains() {
        Structure s = getStructure();
        double ret = 0;
        IProtein protein = getProtein();
        List<Chain> chains = s.getChains();
        for (Iterator<Chain> iterator = chains.iterator(); iterator.hasNext(); ) {
            Chain chn = iterator.next();
            List<Group> groups = chn.getAtomGroups("amino");
            double fraction = 0;
            try {
                fraction = resolveGroups(groups);
            }
            catch (Exception e) {
               fraction = 0; // I guess this did not work

            }
//            System.out.println(protein.getId() + " matches " +
//                String.format("%5.3f",fraction) + " of " + protein.getSequenceLength() +
//                    " on chain " + chn.getChainID()
//
//            );
            ret = Math.max(ret, fraction);

        }
        double f2 = (double)getFitLength() / (double )getSequenceLength();
        return ret;
    }

    public String getGroupSequence(List<Group> groups) {
        StringBuilder sb = new StringBuilder();
        for (Group group : groups) {
            AminoAcidImpl aa = (AminoAcidImpl) group;
            sb.append(aa.getAminoType());

        }
        return sb.toString();
    }

    public double resolveGroups(List<Group> groups) {
        IProtein p = getProtein();
        int matches = 0;
        int nonmatches = 0;
        String sequence = p.getSequence();
        int sequenceLength = p.getSequenceLength();
        int length = sequence.length();
        String chainSequence = getGroupSequence(groups);

        int fitLength = 0;
        int nonFit = 0;
        boolean inFit = false;


        for (int i = 0; i < groups.size(); i++) {
            AminoAcidImpl aa = (AminoAcidImpl) groups.get(i);
            ResidueNumber rn = aa.getResidueNumber();
            String chainId = rn.getChainId();
            Integer seqNum = rn.getSeqNum();
            FastaAminoAcid fa = FastaAminoAcid.fromChar(aa.getAminoType());
            if (seqNum < 1)
                continue;
            if (seqNum >= length)
                break;
            char seqChar = sequence.charAt(seqNum - 1);
            FastaAminoAcid sa = FastaAminoAcid.fromChar(seqChar);
            if (fa == sa) {
                if (!inFit) {
                    fitLength = 1;
                    inFit = true;
                    nonFit = 0;
                }
                else {
                  fitLength++;
                }
                matches++;
            }
            else {
                nonmatches++;
                nonFit++;
                if (!inFit && nonFit > 2) {
                    m_FitLength = Math.max(m_FitLength,fitLength);
                    fitLength = 0;
                    inFit = false;
                }
            }
        }
        m_FitLength = Math.max(m_FitLength,fitLength);

        return (double) matches / (double) length;
    }


//
//    private static void showPDBFile(final File f) {
//        BioJavaModel mdl = new BioJavaModel();
//        mdl.readFile(f);
//        mdl.showModel();
//    }
//
//
//    public static void main(String[] args) {
//        File dir = new File(System.getProperty("user.dir"));
//        File[] files = dir.listFiles();
//        if(files == null)
//            return;
//        for (int i = 0; i < files.length; i++) {
//            File f = files[i];
//            showPDBFile(f);
//
//        }
//
//    }


}
