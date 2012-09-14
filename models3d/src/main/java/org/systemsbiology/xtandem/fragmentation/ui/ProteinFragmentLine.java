package org.systemsbiology.xtandem.fragmentation.ui;

import org.systemsbiology.xtandem.fragmentation.*;
import org.systemsbiology.xtandem.peptide.*;

import javax.xml.ws.*;
import java.util.*;

/**
 * org.systemsbiology.xtandem.fragmentation.ui.ProteinFragmentLine
 * User: Steve
 * Date: 6/25/12
 */
public class ProteinFragmentLine {
    public static final ProteinFragmentLine[] EMPTY_ARRAY = {};
    
    public static ProteinFragmentLine[] generateLines(ProteinFragmentationDescription desc)
    {
        List<ProteinFragmentLine> holder = new ArrayList<ProteinFragmentLine>();
        String sequence = desc.getProtein().getSequence();

        int numberLines = (sequence.length() + CoverageFragment.AMINO_ACID_LINE_WIDTH - 1) / CoverageFragment.AMINO_ACID_LINE_WIDTH;
        for (int i = 0; i < numberLines; i++) {
             int index = i * CoverageFragment.AMINO_ACID_LINE_WIDTH;
            holder.add(new ProteinFragmentLine(desc, index));
        }

        ProteinFragmentLine[] ret = new ProteinFragmentLine[holder.size()];
        holder.toArray(ret);
        return ret;
    }
    
    private final ProteinFragmentationDescription m_Fragments;
    private final int m_Start;

    public ProteinFragmentLine(final ProteinFragmentationDescription fragments, final int start) {
        m_Fragments = fragments;
        m_Start = start;
    }

    public boolean containsFragent(ProteinFragment test) {
        int fStart = test.getStartLocation();
        int fEnd = fStart + test.getSequence().length();
        int myEnd = getEnd();
        if(fStart >= myEnd)
            return false;
        int myStart = getStart();
        if(fEnd < myStart)
            return false;
        return true;

    }

    public ProteinFragmentationDescription getFragments() {
        return m_Fragments;
    }

    public int getStart() {
        return m_Start;
    }

    public int getEnd() {
        String fullSequence = getFragments().getProtein().getSequence();
        int end = Math.min(getStart() + CoverageFragment.AMINO_ACID_LINE_WIDTH,fullSequence.length());
        return end;
    }

    public String getSequence()
    {
        ProteinFragmentationDescription fragments = getFragments();
        Protein protein = fragments.getProtein();
        String fullSequence = protein.getSequence();
        int start = getStart();
        int end = getEnd();
        return fullSequence.substring(start, end);
    }
}
