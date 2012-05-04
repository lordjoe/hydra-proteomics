package org.systemsbiology.windowedanalysis;

import org.systemsbiology.chromosome.*;
import org.systemsbiology.sam.*;

import java.util.*;

/**
 * org.systemsbiology.windowedanalysis.SinglePositionCoverage
 * User: steven
 * Date: Jun 16, 2010
 */
public class SinglePositionCoverage {
    public static final SinglePositionCoverage[] EMPTY_ARRAY = {};

    private final IChromosome m_Chromosome;
    private int m_CurrentPosition;
    private boolean m_CurrentWindowEntered;
    private boolean m_CurrentWindowLeft;

    private final Set<IExtendedPairedSamRecord> m_CoveringRecords = new HashSet<IExtendedPairedSamRecord>();

    public SinglePositionCoverage(final IChromosome pChromosome, final int pCurrentPosition) {
        m_Chromosome = pChromosome;
        m_CurrentPosition = pCurrentPosition;
    }

    public boolean isCurrentWindowEntered() {
        return m_CurrentWindowEntered;
    }

    public void setCurrentWindowEntered(final boolean pCurrentWindowEntered) {
        m_CurrentWindowEntered = pCurrentWindowEntered;
    }

    public boolean isCurrentWindowLeft() {
        return m_CurrentWindowLeft;
    }

    public void setCurrentWindowLeft(final boolean pCurrentWindowLeft) {
        m_CurrentWindowLeft = pCurrentWindowLeft;
    }

    public IChromosome getChromosome() {
        return m_Chromosome;
    }

    public int getCurrentPosition() {
        return m_CurrentPosition;
    }

    public void setCurrentPosition(final int pCurrentPosition) {
        m_CurrentPosition = pCurrentPosition;
    }

    /**
     *
     * @param record recodr to add to a set at a position
     * @return true if the record is NOT in the coverage and we need to advance
     */
    public boolean addRecord(IExtendedPairedSamRecord record) {
        if (record.getAlignmentStart() > getCurrentPosition()) {
            return true; // do not add
        }
        else {
            m_CoveringRecords.add(record);
            return false; // added
        }
    }

    public IExtendedPairedSamRecord[] getCoveringRecords() {
        return m_CoveringRecords.toArray(IExtendedPairedSamRecord.EMPTY_ARRAY);
    }

    public boolean advancePosition(int newPosition) {
        boolean isChanges = false;
        if (!m_CoveringRecords.isEmpty()) {
            List<IExtendedPairedSamRecord> holder = new ArrayList<IExtendedPairedSamRecord>();
            for (IExtendedPairedSamRecord rec : m_CoveringRecords) {
                int end = rec.getAlignmentEnd();
                if (end < newPosition)
                    holder.add(rec); // drop him
            }
            isChanges = holder.isEmpty();
            m_CoveringRecords.removeAll(holder);
        }
        setCurrentPosition(newPosition);
        return !isChanges; // tru if coverage changed
    }


    @Override
    public String toString() {
        return getChromosome().toString() + ":" + getCurrentPosition();
    }
}
