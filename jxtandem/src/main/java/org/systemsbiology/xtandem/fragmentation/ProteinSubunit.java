package org.systemsbiology.xtandem.fragmentation;

import org.systemsbiology.jmol.*;
import org.systemsbiology.xtandem.*;
import org.systemsbiology.xtandem.peptide.*;

import java.util.*;

/**
 * org.systemsbiology.xtandem.fragmentation.ProteinSubunit
 * User: Steve
 * Date: 7/3/12
 */
public class ProteinSubunit {
    public static final ProteinSubunit[] EMPTY_ARRAY = {};

    public static final Comparator<ProteinSubunit> CHAIN_SORTER = new ChainSortComparator();

    /**
     * order by chain - we assume no two have same vhain
     */
    private static class ChainSortComparator implements Comparator<ProteinSubunit> {
        private ChainSortComparator() {
        }

        @Override
        public int compare(final ProteinSubunit o1, final ProteinSubunit o2) {
            return o1.getChain().compareTo(o2.getChain());
        }
    }

    private final ChainEnum m_Chain;
    private final Protein m_Protein;
    private AminoAcidAtLocation[] m_Locations;
    private Map<Integer, AminoAcidAtLocation> m_LocToAminoAcid = new HashMap<Integer, AminoAcidAtLocation>();
    private final String m_Sequence;
    private final ChainEnum[] m_ChainSet; // only non-null if multiple chains
    private int m_MinLoc = 0;

    public ProteinSubunit(final ChainEnum chain, Protein prot, final AminoAcidAtLocation[] locations) {
        m_Chain = chain;
        m_Protein = prot;
        m_Locations = locations;
        m_Sequence = AminoAcidAtLocation.toSequence(locations);
        ChainEnum[] chains = AminoAcidAtLocation.toChains(locations);
        if (chains.length > 1)
            m_ChainSet = chains;
        else
            m_ChainSet = null;
    }

    public Protein getProtein() {
        return m_Protein;
    }

    public ChainEnum getChain() {
        return m_Chain;
    }

    public AminoAcidAtLocation[] getLocations() {
        return m_Locations;
    }

    public String getSequence() {
        return m_Sequence;
    }


    public void setRealLocation(AminoAcidAtLocation loc, int pos) {
        if(loc.getLocation() != -1)
            throw new IllegalStateException("reset location " + loc);
        loc.setLocation(pos);
        m_LocToAminoAcid.put(pos, loc);
        m_MinLoc = Math.max(m_MinLoc,pos);
    }

    public int getMinLoc() {
        return m_MinLoc;
    }

    public AminoAcidAtLocation getAminoAcidAtLocation(int loc) {
        return m_LocToAminoAcid.get(loc);
    }


    public AminoAcidAtLocation[] getAminoAcidsForSequence(String foundSequence) {
        AminoAcidAtLocation[] ret = internalAminoAcidsForSequence(foundSequence);
        if (ret != null)
            return ret;
        return getAminoAcidsForCloseSequence(foundSequence);
    }

    public AminoAcidAtLocation[] internalAminoAcidsForSequence(String foundSequence) {
        FastaAminoAcid[] aas = FastaAminoAcid.asAminoAcids(foundSequence);
        for (int i = 0; i < m_Locations.length - aas.length; i++) {
            AminoAcidAtLocation test = m_Locations[i];
            if (test.getLocation() != -1 && test.getAminoAcid() == aas[0]) {
                AminoAcidAtLocation[] ret = maybeBuildSequence(aas, i);
                if (ret != null)
                    return ret;
            }

        }
        return null;

    }

    /**
     * @param foundSequence
     * @return
     */
    public AminoAcidAtLocation[] getAminoAcidsForCloseSequence(String foundSequence) {
        String modelSequence = getSequence();
        SmithWaterman sw = new SmithWaterman(modelSequence, foundSequence);
        List<SimpleChainingMatch> matches = sw.getMatches();
        if (matches.size() == 0) {
            // try again
//            sw = new SmithWaterman(modelSequence, foundSequence,SmithWaterman.DEFAULT_SCORE_THRESHOLD / 3);
//            matches = sw.getMatches();
//            for (Iterator<SimpleChainingMatch> iterator = matches.iterator(); iterator.hasNext(); ) {
//                SimpleChainingMatch best =  iterator.next();
//                String sequenceInTarget = modelSequence.substring(best.getFromA(),best.getToA());
//                System.out.println(sequenceInTarget);
//            }
//            System.out.println(foundSequence + " sought");
//            System.out.println(modelSequence);
//            System.out.println();
            return null;
        }
        SimpleChainingMatch best = matches.get(0);
        String sequenceInTarget = modelSequence.substring(best.getFromA(), best.getToA());
        return internalAminoAcidsForSequence(sequenceInTarget);


    }


    private AminoAcidAtLocation[] maybeBuildSequence(FastaAminoAcid[] aas, int start) {
        List<AminoAcidAtLocation> holder = new ArrayList<AminoAcidAtLocation>();
        holder.add(m_Locations[start]);
        for (int i = 1; i < aas.length; i++) {
            AminoAcidAtLocation test = m_Locations[start + i];
            if (test.getLocation() != -1 && test.getAminoAcid() == aas[i]) {
                holder.add(test);
            }
            else {
                return null; // bad
            }
        }

        AminoAcidAtLocation[] ret = new AminoAcidAtLocation[holder.size()];
        holder.toArray(ret);
        return ret;
    }
}
