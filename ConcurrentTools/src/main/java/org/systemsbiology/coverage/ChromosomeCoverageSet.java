package org.systemsbiology.coverage;

import com.lordjoe.lib.xml.*;
import org.apache.hadoop.mapreduce.*;
import org.systemsbiology.chromosome.*;
import org.systemsbiology.common.*;
import org.systemsbiology.data.*;
import org.systemsbiology.picard.*;
import org.systemsbiology.sam.*;

import java.io.*;
import java.util.*;

/**
 * org.systemsbiology.coverage.ChromosomeCoverageSet
 * written by Steve Lewis
 * on Apr 9, 2010
 */
public class ChromosomeCoverageSet extends ImplementationBase implements ISamRecordVisitor
{
    public static final ChromosomeCoverageSet[] EMPTY_ARRAY = {};
    public static final Class THIS_CLASS = ChromosomeCoverageSet.class;

    public static final String SCRIPT_NAME = "ChromosomeCoverage";
    public static final int DEFAULT_BIN_WIDTH = 1000;


    private int m_BinWidth;
    private int m_AlternateResolution;
    private final Map<IChromosome, ChromosomeCoverage> m_Coverages =
            new HashMap<IChromosome, ChromosomeCoverage>();

    public ChromosomeCoverageSet(int binwidth) {
        setBinWidth(binwidth);
    }

    public ChromosomeCoverageSet() {
        this(DEFAULT_BIN_WIDTH);
    }

    public int getBinWidth() {
        return m_BinWidth;
    }

    public void setBinWidth(int pBinWidth) {
        if (m_BinWidth == pBinWidth)
            return;
        m_BinWidth = pBinWidth;
        m_Coverages.clear();
        IChromosome[] chromosomes = AnalysisParameters.getInstance().getDefaultChromosomes();
        for (int i = 0; i < chromosomes.length; i++) {
            IChromosome chromosome = chromosomes[i];
            m_Coverages.put(chromosome, new ChromosomeCoverage(chromosome, m_BinWidth));
        }
    }

    public int getAlternateResolution() {
        return m_AlternateResolution;
    }

    public void setAlternateResolution(int pAlternateResolution) {
        m_AlternateResolution = pAlternateResolution;
    }

    public ChromosomeCoverage[] getCoverages() {
        IChromosome[] chromosomes = AnalysisParameters.getInstance().getDefaultChromosomes();
        ChromosomeCoverage[] ret = new ChromosomeCoverage[chromosomes.length];
        for (int i = 0; i < chromosomes.length; i++) {
            IChromosome chromosome = chromosomes[i];
            ret[i] = m_Coverages.get(chromosome);
        }
        return ret;
    }

    public ChromosomeCoverage getCoverage(IChromosome chr) {
        return m_Coverages.get(chr);
    }

    @Override
    protected void appendCollectionProperties(XMLPropertySet props, Appendable sb, int indent) throws IOException {
        super.appendCollectionProperties(props, sb, indent);
        for (ChromosomeCoverage v : getCoverages()) {
            v.appendXML(props, sb, indent + 1);
        }

    }

    /**
     * implemt the visitor pattern for a SAMRecord
     *
     * @param record
     * @param added
     */
    public boolean visit(IExtendedSamRecord record, Object... added) {
        ChromosomeCoverage cvg = getCoverage(record.getChromosome());
        if (cvg == null)
            return false;
        return cvg.visit(record);
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
        ChromosomeCoverage[] coverages = getCoverages();
        for (int i = 0; i < coverages.length; i++) {
            ChromosomeCoverage coverage = coverages[i];
            coverage.reset();
        }
    }

    /**
     * reset to base state
     *
     * @param added
     */
    public void finish(Object... added) {
        PrintWriter out = null;
        try {
            IAnalysisParameters ap = AnalysisParameters.getInstance();
            String name = ap.getJobName();
            if(added.length > 0 && added[0] instanceof TaskInputOutputContext)
                  name = ((TaskInputOutputContext)added[0]).getJobName();

            IStreamFactory sf = ap.getDefaultStreamFactory();
            if(added.length > 1 && added[1] instanceof IStreamFactory)
                sf = (IStreamFactory)added[1];
             OutputStream os = sf.openOutputStream(name + ".coverage");
            out = new PrintWriter(new OutputStreamWriter(os));
            ChromosomeCoverage[] coverages = getCoverages();
            for (int i = 0; i < coverages.length; i++) {
                ChromosomeCoverage coverage = coverages[i];
                coverage.writeData(out);
            }
        }
         finally {
            if (out != null)
                out.close();
        }
        int alrRes = getAlternateResolution();
        if (alrRes != 0) {
            // todo generate alternate coverage
        }
    }


    @Override
    public Object handleTag(String TagName, NameValue[] attributes) {
        if("BamStatistics".equals(TagName))
              return this;
        if("ChromosomeCoverage".equals(TagName)) {
            String s = XMLUtil.handleRequiredNameValueString("name", attributes);
            IAnalysisParameters ap = AnalysisParameters.getInstance();

            IChromosome chr = DefaultChromosome.parseChromosome(s);
            ChromosomeCoverage  cst = new ChromosomeCoverage(chr,1000);
            m_Coverages.put(chr,cst);
            return cst;

        }

          return super.handleTag(TagName, attributes);
    }
}
