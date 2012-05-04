package org.systemsbiology.chromosome;

import net.sf.samtools.*;

import java.util.*;

/**
 * org.systemsbiology.chromosome.ChromosomeSet
 * written by Steve Lewis
 * on Apr 16, 2010
 */
public class ChromosomeSet
{
    public static final ChromosomeSet[] EMPTY_ARRAY = {};
    public static final Class THIS_CLASS = ChromosomeSet.class;

 
    public static ChromosomeSet buildSetFromHeader(SAMFileHeader hdr)
    {
        ChromosomeSet ret = null;
        SAMSequenceDictionary sd = hdr.getSequenceDictionary();
         for(SAMSequenceRecord rec : sd.getSequences() ) {
             if(ret == null)   {
                 ret = new ChromosomeSet(rec.getAssembly(),rec.getSpecies());
             }
             String name = rec.getSequenceName();
             int lemgth = rec.getSequenceLength();
             short index = 0;
             IChromosome chr = new DynamicChromosome(name,lemgth,index);
             ret.addChromosome(chr);
         }
        return ret;
    }

    private final String m_Name;
    private final String m_Species;

    private List<IChromosome> m_Sources =
                new ArrayList<IChromosome>();

    public ChromosomeSet(String pName, String pSpecies) {
        m_Name = pName;
        m_Species = pSpecies;
    }

    public IChromosome[] getChromosomes() {
        return m_Sources.toArray(IChromosome.EMPTY_ARRAY);
    }

    public void addChromosome(IChromosome added)  {
        if(!m_Sources.contains(added))
            m_Sources.add(added);
    }
}
