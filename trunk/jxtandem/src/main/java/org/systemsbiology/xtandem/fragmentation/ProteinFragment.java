package org.systemsbiology.xtandem.fragmentation;

import org.systemsbiology.xtandem.peptide.*;

import java.util.*;

/**
 * org.systemsbiology.xtandem.fragmentation.ProteinFragment
 * User: Steve
 * Date: 6/21/12
 */
public class ProteinFragment {
    public static final ProteinFragment[] EMPTY_ARRAY = {};

    private final Protein m_ParentProtein;
    private final Polypeptide m_Peptide;
    private int[] m_StartLocations;

    public ProteinFragment(final Protein parentProtein, final Polypeptide peptide) {
        m_ParentProtein = parentProtein;
        m_Peptide = peptide;
    }

    public Protein getParentProtein() {
        return m_ParentProtein;
    }

    public Polypeptide getPeptide() {
        return m_Peptide;
    }

    public int getStartLocation() {
        guaranteeStartLocation();
        if(m_StartLocations.length == 0)
            return -1;
        return m_StartLocations[0];
    }
    public int[] getStartLocations() {
        guaranteeStartLocation();
        return m_StartLocations;
    }

    protected void guaranteeStartLocation() {
        if(m_StartLocations  != null)
              return;
        List<Integer> holder = new ArrayList<Integer>();
        String proteinSequence = getParentProtein().getSequence();
        String fragmentSequence = getPeptide().getSequence();

        int start = 0;
        int index =  proteinSequence.indexOf(fragmentSequence,start);
        while(index > -1) {
            holder.add(index);
            start = index + 1;
            index =  proteinSequence.indexOf(fragmentSequence,start);
        }

        Integer[] ret = new Integer[holder.size()];
        holder.toArray(ret);
        m_StartLocations = new int[ret.length];
        for (int i = 0; i < m_StartLocations.length; i++) {
             m_StartLocations[i] = ret[i];
        }
     }

    @Override
    public String toString() {
        return   m_Peptide.toString();
    }

    public boolean containsPosition(int index) {
        int length = getPeptide().getSequence().length();
        int[] locs = getStartLocations();
        for (int i = 0; i < locs.length; i++) {
            int loc = locs[i];
            if(index >= loc && index < loc + length)
                return true ; // inside fragment
        }
        return false;
    }
}
