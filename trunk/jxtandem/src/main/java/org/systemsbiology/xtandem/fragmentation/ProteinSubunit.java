package org.systemsbiology.xtandem.fragmentation;

import org.systemsbiology.jmol.*;
import org.systemsbiology.xtandem.peptide.*;

import java.util.*;

/**
 * org.systemsbiology.xtandem.fragmentation.ProteinSubunit
 * User: Steve
 * Date: 7/3/12
 */
public class ProteinSubunit  {
    public static final ProteinSubunit[] EMPTY_ARRAY = {};

    public static final Comparator<ProteinSubunit>  CHAIN_SORTER = new ChainSortComparator();

    /**
     * order by chain - we assume no two have same vhain
     */
    private static class  ChainSortComparator implements Comparator<ProteinSubunit> {
        private ChainSortComparator() {
        }

        @Override
        public int compare(final ProteinSubunit o1, final ProteinSubunit o2) {
            if (true) throw new UnsupportedOperationException("Fix This"); // ToDo
            return o1.getChain().compareTo(o2.getChain());
        }
    }

    private final ChainEnum m_Chain;
    private final Protein m_Protein;
    private AminoAcidAtLocation[] m_Locations;
    private final String m_Sequence;
    private final ChainEnum[] m_ChainSet; // only non-null if multiple chains

    public ProteinSubunit(final ChainEnum chain,Protein prot, final AminoAcidAtLocation[] locations) {
        m_Chain = chain;
        m_Protein = prot;
        m_Locations = locations;
        m_Sequence = AminoAcidAtLocation.toSequence(locations);
         ChainEnum[] chains = AminoAcidAtLocation.toChains(locations);
         if(chains.length > 1)
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
}
