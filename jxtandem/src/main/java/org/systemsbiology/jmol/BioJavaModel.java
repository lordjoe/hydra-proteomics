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

    private Protein m_Protein;
    private Structure m_Structure;

    public BioJavaModel(final Protein protein) {
        m_Protein = protein;
    }

    public void readFile(File f) {
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

    public Protein getProtein() {
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
        Protein protein = getProtein();
        List<Chain> chains = s.getChains();
         for (Iterator<Chain> iterator = chains.iterator(); iterator.hasNext(); ) {
            Chain chn = iterator.next();
            List<Group> groups = chn.getAtomGroups("amino");
            double fraction = resolveGroups(groups);
//            System.out.println(protein.getId() + " matches " +
//                String.format("%5.3f",fraction) + " of " + protein.getSequenceLength() +
//                    " on chain " + chn.getChainID()
//
//            );
            ret = Math.max(ret,fraction);

        }
        return ret;
    }

    public double resolveGroups(List<Group> groups) {
        Protein p = getProtein();
        int matches = 0;
        String sequence = p.getSequence();
        int length = sequence.length();
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
             if(fa == sa){
                 matches++;
             }
        }
        return (double )matches / (double ) length;
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
