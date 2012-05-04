package org.systemsbiology.windowedanalysis;

import com.lordjoe.lib.xml.*;
import org.apache.hadoop.mapreduce.*;
import org.systemsbiology.common.*;
import org.systemsbiology.hadoop.*;
import org.systemsbiology.location.*;
import org.systemsbiology.partitioning.*;
import org.systemsbiology.sam.*;

import java.io.*;
import java.util.*;

/**
 * org.systemsbiology.windowedanalysis.BreakFinderAnalysis
 * written by Steve Lewis
 * on Apr 26, 2010
 */
public class CoverageAnalysisSet extends ImplementationBase implements IPartitionedSetAnalysis {
    public static final CoverageAnalysisSet[] EMPTY_ARRAY = {};
    public static final Class THIS_CLASS = CoverageAnalysisSet.class;
    public static final int MINIMAL_COVERAGE = 12;
    public static final int MINIMAL_INTERVAL_RECORDS = 1000;
    public static final int KEEP_ALIVE_RECORDS = 100;


    public static final String SCRIPT_NAME = "CoverageAnalysisSet";

    private Map<String,ICoverageAnalyzer> m_Analyzers =
            new HashMap<String,ICoverageAnalyzer>();


    public CoverageAnalysisSet() {
    }

    public ICoverageAnalyzer getAnalyzer(String s) {
         return m_Analyzers.get(s);
    }

    public void addAnalyzer(String s,ICoverageAnalyzer added) {
          m_Analyzers.put(s,added);
    }

    public ICoverageAnalyzer[] getAnalyzers() {
        return m_Analyzers.values().toArray(ICoverageAnalyzer.EMPTY_ARRAY);
    }

    /**
     * perform some analysis on a data set
     *
     * @param data
     * @param more
     */
    public void analyze(IPartitionedSamSet data, Object... more) {
        GenomeStatistics bst = null;
        if (m_Analyzers.isEmpty())
            return;
        if(data.getNumberRecords() < MINIMAL_INTERVAL_RECORDS)
            return;
        String className = getClass().getName();
         TaskInputOutputContext ctx = null;
        if (more.length > 0 && more[0] instanceof TaskInputOutputContext) {
            ctx = (TaskInputOutputContext) more[0];
            System.out.println("Analyzing " + data);
            //BamHadoopUtilities.writeText(ctx, className, "Analyzing " + data );

        }

        GenomeInterval geneInterval = (GenomeInterval) data.getInterval();
        GenomeInterval activeInterval = (GenomeInterval) data.getActiveInterval();
        IExtendedPairedSamRecord[] allRecords = data.getAllRecords();
        SinglePositionCoverage coverage = new SinglePositionCoverage(geneInterval.getChromosome(),  geneInterval.getStart());


        ICoverageAnalyzer[] analyzers = getAnalyzers();
        for (int i = 0; i < analyzers.length; i++) {
            ICoverageAnalyzer analyzer = analyzers[i];
            analyzer.startWindow(more);
        }
         HadoopUtilities.keepAlive();  // don't kill the reducer
        int start = allRecords[0].getAlignmentStart();
        while (start > coverage.getCurrentPosition())
            advancePosition(coverage,activeInterval,more);
  
        for (int i = 1; i < allRecords.length; i++) {
            IExtendedPairedSamRecord rec = allRecords[i];

            if(i % KEEP_ALIVE_RECORDS == 0)
                HadoopUtilities.keepAlive();
            
            boolean changed = coverage.addRecord(rec);
            if (changed) {
                // handle old data
                for (int j = 0; j < analyzers.length; j++) {
                    ICoverageAnalyzer analyzer = analyzers[j];
                    analyzer.visit(coverage, more);
                }
                start = rec.getAlignmentStart();
                while (start > coverage.getCurrentPosition())
                    advancePosition(coverage,activeInterval,more);
                coverage.addRecord(rec);
            }


        }
        // add any notifications we left off
        if (!coverage.isCurrentWindowEntered()) {
            for (int j = 0; j < analyzers.length; j++) {
                ICoverageAnalyzer analyzer = analyzers[j];
                analyzer.startActiveWindow(more);
            }
        }
        // add any notifications we left off
        if (!coverage.isCurrentWindowLeft()) {
            for (int j = 0; j < analyzers.length; j++) {
                ICoverageAnalyzer analyzer = analyzers[j];
                analyzer.leaveActiveWindow(more);
            }
        }

        // we are done
        for (int i = 0; i < analyzers.length; i++) {
            ICoverageAnalyzer analyzer = analyzers[i];
            analyzer.endWindow(more);
        }

    }

    protected int advancePosition(SinglePositionCoverage cvg, GenomeInterval activeInterval, Object... more) {
        ICoverageAnalyzer[] analyzers = getAnalyzers();

        int pos = cvg.getCurrentPosition();
        cvg.advancePosition(++pos);
        if (!cvg.isCurrentWindowEntered()) {
            if (pos >= activeInterval.getStart()) {
                cvg.setCurrentWindowEntered(true);
                for (int j = 0; j < analyzers.length; j++) {
                    ICoverageAnalyzer analyzer = analyzers[j];
                    analyzer.startActiveWindow(more);
                }
            }
        }
        if (!cvg.isCurrentWindowLeft()) {
            if (pos > activeInterval.getEnd()) {
                cvg.setCurrentWindowLeft(true);
                for (int j = 0; j < analyzers.length; j++) {
                    ICoverageAnalyzer analyzer = analyzers[j];
                    analyzer.leaveActiveWindow(more);
                }
            }
        }
        for (int j = 0; j < analyzers.length; j++) {
            ICoverageAnalyzer analyzer = analyzers[j];
            analyzer.visit(cvg,more);
        }

        return cvg.getCurrentPosition();
    }


    @Override
    protected void appendCollectionProperties(XMLPropertySet props, Appendable sb, int indent) throws IOException {
        super.appendCollectionProperties(props, sb, indent);
        for (ICoverageAnalyzer rg : getAnalyzers()) {
            if(rg instanceof IXMLWriter)
                ((IXMLWriter)rg).appendXML(props, sb, indent + 1);
        }
    }

    /**
     * take action at the end of a process
     *
     * @param added other data
     */
    public void finish(Object... added) {
        for (ICoverageAnalyzer rg : getAnalyzers()) {
           rg.finish(added);
        }
    }


}