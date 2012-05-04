package org.systemsbiology.windowedanalysis;

import org.systemsbiology.location.*;
import org.systemsbiology.sam.*;

/**
 * org.systemsbiology.windowedanalysis.PositionOfInterest
 * written by Steve Lewis
 * on Apr 27, 2010
 */
public class PositionOfInterest {
    public static final PositionOfInterest[] EMPTY_ARRAY = {};
    public static final Class THIS_CLASS = PositionOfInterest.class;

    private final IGeneLocation m_Location;
    private final short m_Coverage;
    private final short[] m_Distances;
    private final IExtendedPairedSamRecord[] m_Reads;
    private double m_DistanceProbability;
    private double m_CoverageProbability;
    private double m_TotalProbability;

    public PositionOfInterest(IGeneLocation pLocation, short pCoverage, short[] pDistances, IExtendedPairedSamRecord[] reads) {
        m_Location = pLocation;
        m_Coverage = pCoverage;
        m_Distances = new short[pDistances.length];
        System.arraycopy(pDistances, 0, m_Distances, 0, pDistances.length);
        m_Reads = new IExtendedPairedSamRecord[reads.length];;
        System.arraycopy(reads, 0, m_Reads, 0, m_Reads.length);
      }

    public double getDistanceProbability() {
        return m_DistanceProbability;
    }

    public void setDistanceProbability(double pDistanceProbability) {
        m_DistanceProbability = pDistanceProbability;
    }

    public double getCoverageProbability() {
        return m_CoverageProbability;
    }

    public void setCoverageProbability(double pCoverageProbability) {
        m_CoverageProbability = pCoverageProbability;
    }

    public double getTotalProbability() {
        return m_TotalProbability;
    }

    public void setTotalProbability(double pTotalProbability) {
        m_TotalProbability = pTotalProbability;
    }

    public IGeneLocation getLocation() {
        return m_Location;
    }

    public short getCoverage() {
        return m_Coverage;
    }

    public short[] getDistances() {
        return m_Distances;
    }

    public IExtendedPairedSamRecord[] getReads() {
        return m_Reads;
    }
}
