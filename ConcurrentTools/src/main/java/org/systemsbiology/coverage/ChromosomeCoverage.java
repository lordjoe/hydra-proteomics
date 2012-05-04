package org.systemsbiology.coverage;

import com.lordjoe.lib.xml.*;
import com.lordjoe.utilities.*;
import org.systemsbiology.chromosome.*;
import org.systemsbiology.common.*;
import org.systemsbiology.picard.*;
import org.systemsbiology.sam.*;

import java.io.*;

/**
 * org.systemsbiology.coverage.ChromosomeCoverage
 * written by Steve Lewis
 * on Apr 9, 2010
 */
public class ChromosomeCoverage extends ImplementationBase implements ISamRecordVisitor
{
    public static final ChromosomeCoverage[] EMPTY_ARRAY = {};
    public static final Class THIS_CLASS = ChromosomeCoverage.class;
    public static final int DEFAULT_COURSE_WIDTH = 10000;


    private final IChromosome m_Chromosome;
    private int m_BinWidth = DEFAULT_COURSE_WIDTH;
    private final IntegerHistogram m_Values;

    public ChromosomeCoverage(IChromosome pChromosome, int pBinWidth) {
        setName(pChromosome.toString());
        m_Chromosome = pChromosome;
        m_BinWidth = pBinWidth;
        m_Values = new IntegerHistogram(m_BinWidth);
        m_Values.setNumberBins((int)(m_Chromosome.getLength() / m_BinWidth));
    }


    public IChromosome getChromosome() {
        return m_Chromosome;
    }

    public int getBinWidth() {
        return m_BinWidth;
    }

    public void addStart(int start)
    {
        m_Values.addItemAt(start);
    }

    public IntegerHistogramBin[] getAllBins()
      {
          return m_Values.getAllBins();
      }

      public IntegerHistogramBin[] getAllNonZeroBins()  {
          return m_Values.getAllNonZeroBins();

      }

     public void writeData(PrintWriter out) {
         IntegerHistogramBin[] bins = getAllNonZeroBins();
         for (int i = 0; i < bins.length; i++) {
             IntegerHistogramBin bin = bins[i];
             out.print(getChromosome().getAlternateName() + "\t");
             out.println(bin);
         }
     }



    @Override
    protected void appendCollectionProperties(XMLPropertySet props, Appendable sb, int indent) throws IOException {
        super.appendCollectionProperties(props, sb, indent);
        for(IntegerHistogramBin v : getAllNonZeroBins())  {
              Util.indent(sb,indent + 1);
              sb.append("<bin start=\"" + v.getStart() + "\" end=\"" + v.getEnd() + "\" count=\"" + v.getCount() + "\" />\n");
         }

    }
    

    /**
     * implemt the visitor pattern for a SAMRecord
     *
     * @param record
     */
    public boolean visit(IExtendedSamRecord record,Object... added) {
        if(record.getChromosome() != getChromosome())
            return false;
        m_Values.addItemAt(record.getAlignmentStart());
        return true;
    }

    /**
     * initialize the item
     *
     * @param added any additional data
     */
    public void initialize(Object... added) {
        reset();
    }

    /**
      * if true then then the algorithm will not work on unsorted data
      *
      * @return as above
      */
     public boolean isSortedRequired()
     {
         return true;
     }


    /**
     * reset to base state
     */
    public void reset() {
        m_Values.reset();
        m_Values.setNumberBins((int)(m_Chromosome.getLength() / m_BinWidth));
     }

    /**
     * reset to base state
     * @param added
     */
    public void finish(Object... added) {
        // do nothing
    }
}
