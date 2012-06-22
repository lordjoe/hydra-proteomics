package org.systemsbiology.xtandem.fragmentation;

import org.systemsbiology.xtandem.peptide.*;

/**
 * org.systemsbiology.xtandem.fragmentation.ProteinFragment
 * User: Steve
 * Date: 6/21/12
 */
public class ProteinFragment {
    public static final ProteinFragment[] EMPTY_ARRAY = {};

    private final Protein m_ParentProtein;
    private final Polypeptide m_Peptide;

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
}
