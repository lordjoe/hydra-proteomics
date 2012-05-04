package org.systemsbiology.coverage;

import org.systemsbiology.picard.*;

import java.util.*;

/**
 * org.systemsbiology.coverage.CoverageSet
 * User: steven
 * Date: Jun 2, 2010
 */
public class CoverageSet {
    public static final CoverageSet[] EMPTY_ARRAY = {};

    private int m_TotalCoverage;
    private int m_TotalPositions;

    private final Set<IExtendedSamRecord> m_CurrentWindow =
            new HashSet<IExtendedSamRecord>();
    private int m_NextEnd;
    private int m_Position;

    public void addRecord(IExtendedSamRecord record) {
        m_CurrentWindow.add(record);
        final int alignmentEnd = record.getAlignmentEnd();
         if(m_NextEnd > 0) {
             m_NextEnd = Math.min(m_NextEnd, alignmentEnd);
        }
        else
            m_NextEnd =  alignmentEnd;
    }

    public void resetCount() {
        m_TotalCoverage = 0;
        m_TotalPositions = 0;
    }

    public int getTotalCoverage() {
        return m_TotalCoverage;
    }

    public double getAverageCoverage() {
        if (getTotalPositions() == 0)
            return 0;
        return (double) getTotalCoverage() / (double) getTotalPositions();
    }

    public int getTotalPositions() {
        return m_TotalPositions;
    }


    public int getCoverage() {
        return m_CurrentWindow.size();
    }

    public int getPosition() {
        return m_Position;
    }

    public int incrementPosition() {
        setPosition(getPosition() + 1);
        m_TotalCoverage += getCoverage();
        m_TotalPositions++;
        return getPosition();
    }

    protected void setPosition(final int pPosition) {
        // we need to drop items
        if (!m_CurrentWindow.isEmpty() && m_NextEnd > 0 && pPosition >= m_NextEnd) {
            m_NextEnd = 0;
            List<IExtendedSamRecord> holder = new ArrayList<IExtendedSamRecord>();
            for (IExtendedSamRecord test : m_CurrentWindow) {
                final int end = test.getAlignmentEnd();
                if (test.getAlignmentStart() > pPosition ||
                        end < pPosition)
                    holder.add(test);
                else {
                     m_NextEnd = Math.min(m_NextEnd,end);
                }
            }
            m_CurrentWindow.removeAll(holder);
        }
        m_Position = pPosition;

    }


}
