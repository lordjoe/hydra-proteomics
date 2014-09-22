package org.systemsbiology.gatk;

/**
 * org.systemsbiology.gatk.GeneRegion
 * User: steven
 * Date: 6/12/12
 */
public class GeneRegion implements Comparable<GeneRegion> {
    public static final GeneRegion[] EMPTY_ARRAY = {};

    private final GeneInterval m_Interval;
    private final String m_Description;

    public GeneRegion(String chromosome, int end, int start, String description) {
        m_Interval = new GeneInterval(chromosome, start, end);
        m_Description = description;
    }

    public GeneRegion(String chromosome, int end, int start) {
        this(chromosome, end, start, chromosome + ":" + start + "-" + end);
    }

    public GeneRegion(String interval, String description) {
        m_Interval = new GeneInterval(interval);
        m_Description = description;
    }

    public GeneRegion(String interval) {
        m_Interval = new GeneInterval(interval);
        m_Description = m_Interval.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GeneRegion that = (GeneRegion) o;

        if (!m_Interval.equals(that.m_Interval)) return false;
        if (m_Description != null ? !m_Description.equals(that.m_Description) : that.m_Description != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = m_Interval.hashCode();
        result = 31 * result + (m_Description != null ? m_Description.hashCode() : 0);
        return result;
    }

    public String getChromosome() {
        return m_Interval.getChromosome();
    }

    public long getStart() {
        return m_Interval.getStart();
    }

    public long getEnd() {
        return m_Interval.getEnd();
    }

    public String getDescription() {
        return m_Description;
    }

    public GeneInterval getInterval() {
        return m_Interval;
    }

    @Override
    public String toString() {
        return getDescription();
    }

    public boolean isWithin(GeneInterval test) {
        return getInterval().isWithin(test);

    }

    public boolean isWithin(GeneLocation test) {
        return getInterval().isWithin(test);

    }

    public boolean isWithin(GeneRegion test) {
        return getInterval().isWithin(test.getInterval());

    }

    public boolean isOverLapping(GeneRegion test) {
        return getInterval().isOverLapping(test.getInterval());
    }

    public boolean isOverLapping(GeneInterval test) {
        return getInterval().isOverLapping(test);
    }

    @Override
    public int compareTo(GeneRegion o) {
        if (this == o)
            return 0;
        int ret = getInterval().compareTo(o.getInterval());
        if (ret != 0)
            return ret;
        return getDescription().compareTo(o.getDescription());
    }
}
