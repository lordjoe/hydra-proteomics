package org.systemsbiology.xtandem.peptide;

import java.util.*;

/**
 * org.systemsbiology.xtandem.peptide.FoundPeptides
 * User: Steve
 * Date: 8/15/12
 */
public class FoundPeptides {
    public static final FoundPeptides[] EMPTY_ARRAY = {};
    private final Map<String,List<FoundPeptide>> m_ProteinToPeptide = new HashMap<String,List<FoundPeptide>>();

    public FoundPeptides() {
    }

    public FoundPeptide[] getPeptides(String protein)
    {
        throw new UnsupportedOperationException("Fix This"); // ToDo
      //  m_ProteinToPeptide.get()
    }
}
