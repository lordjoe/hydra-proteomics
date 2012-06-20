package org.systemsbiology.gatk;

/**
 * org.systemsbiology.gatk.GeneRegion
 * User: steven
 * Date: 6/12/12
 */
public class GeneInterval implements Comparable<GeneInterval> {
    public static final GeneInterval[] EMPTY_ARRAY = {};

    private final String m_Chromosome;
    private final int m_Start;
    private final int m_End;

    public GeneInterval(String chromosome, int end, int start ) {
        m_Chromosome = chromosome;
          m_End = end;
        m_Start = start;
    }

    /**
     * build from string like chr15:39873279-39889668
     * @param data
     */
    public GeneInterval(String data ) {
        String[] items = data.split(":");
        m_Chromosome = items[0];
        items = items[1].split("-");
          m_End = Integer.parseInt(items[1]);
        m_Start = Integer.parseInt(items[0]);;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GeneInterval that = (GeneInterval) o;

        if (m_End != that.m_End) return false;
        if (m_Start != that.m_Start) return false;
        if (m_Chromosome != null ? !m_Chromosome.equals(that.m_Chromosome) : that.m_Chromosome != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = m_Chromosome != null ? m_Chromosome.hashCode() : 0;
        result = 31 * result + m_Start;
        result = 31 * result + m_End;
         return result;
    }

    public String getChromosome() {
        return m_Chromosome;
    }

    public int getStart() {
        return m_Start;
    }

    public int getEnd() {
        return m_End;
    }




    public boolean isWithin(GeneInterval test)
    {
        if(!(getChromosome().equals(test.getChromosome()) ))
            return false;
        if(test.getEnd() < getStart())
            return false;
        if(test.getStart() > getEnd())
             return false;
        if(test.getStart() < getStart())
             return false;
        if(test.getEnd() > getEnd())
             return false;
        return true;

    }
    public boolean isWithin(GeneLocation test)
    {
        if(!(getChromosome().equals(test.getChromosome()) ))
            return false;
          if(test.getLocation() > getEnd())
             return false;
        if(test.getLocation() < getStart())
             return false;
         return true;

    }

    public boolean isOverLapping(GeneInterval test)
    {
        if(!(getChromosome().equals(test.getChromosome()) ))
            return false;
         if(test.getEnd() < getStart())
            return false;
        if(test.getStart() > getEnd())
            return false;
        return true;
    }

    @Override
    public int compareTo(GeneInterval o) {
        if (this == o)
            return 0;
        int ret = getChromosome().compareTo(o.getChromosome());
        if (ret != 0)
            return ret;
        int start0 = o.getStart();
        int start = getStart();
        if (start != start0)
            return start < start0 ? -1 : 1;
        int end0 = o.getEnd();
        int end = getEnd();
        if (end != end0)
            return end > end0 ? -1 : 1;
        return 0;
    }

    @Override
    public String toString() {
        return   m_Chromosome + ":" +
                 + m_Start +
                "-" + m_End;
    }
}
