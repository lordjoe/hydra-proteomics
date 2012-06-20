package org.systemsbiology.gatk;

import scala.io.*;

/**
 * org.systemsbiology.gatk.GeneRegion
 * User: steven
 * Date: 6/12/12
 */
public class GeneLocation implements Comparable<GeneLocation> {
    public static final GeneLocation[] EMPTY_ARRAY = {};



    private final String m_Chromosome;
    private final long m_Location;

    public GeneLocation(String chromosome, long start) {
        m_Chromosome = chromosome;
        m_Location = start;
    }

    /**
     * build from the results of toString
     * @param data
     */
    public GeneLocation(String data) {
        this(data.substring(0,data.indexOf(":")),Integer.parseInt(data.substring( data.indexOf(":") + 1)));
    }

    public String asIGVInterval()
    {
        int startDel = (int)(m_Location % 100);
        if(startDel < 4)
            startDel += 100;
          long start = m_Location - startDel;
        long end = m_Location + 100 - startDel;
        if(startDel > 100)
            end -= 50;

        return getChromosome() + ":" + start + "-" + end;

    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GeneLocation that = (GeneLocation) o;

        if (m_Location != that.m_Location) return false;
        if (m_Chromosome != null ? !m_Chromosome.equals(that.m_Chromosome) : that.m_Chromosome != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = m_Chromosome != null ? m_Chromosome.hashCode() : 0;
        result = 31 * result + (int)m_Location;
        return result;
    }

    public String getChromosome() {
        return m_Chromosome;
    }

    public long getLocation() {
        return m_Location;
    }


    @Override
    public String toString() {
        return getChromosome() + ":" + getLocation();
    }


    @Override
    public int compareTo(GeneLocation o) {
        if (this == o)
            return 0;
        int ret = getChromosome().compareTo(o.getChromosome());
        if (ret != 0)
            return ret;
        long start0 = o.getLocation();
        long start = getLocation();
        if (start != start0)
            return start < start0 ? -1 : 1;
        return 0;
    }
}
