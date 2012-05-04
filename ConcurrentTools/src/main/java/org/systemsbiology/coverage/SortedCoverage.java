package org.systemsbiology.coverage;

import org.systemsbiology.chromosome.*;
import org.systemsbiology.picard.*;
import org.systemsbiology.sam.*;

import java.io.*;
import java.util.*;

/**
 * org.systemsbiology.coverage.SortedCoverage
 * User: steven
 * Date: Jun 1, 2010
 */
public class SortedCoverage implements ISamRecordVisitor {
    public static final SortedCoverage[] EMPTY_ARRAY = {};

    public static final String SCRIPT_NAME = "SortedCoverage";

    public static final int DEFAULT_RESOLUTION = 10000;

    private Map<IChromosome, SortedChromosomeCoverage> m_UsedCoverages =
            new HashMap<IChromosome, SortedChromosomeCoverage>();

    private int m_CoarseResolution = DEFAULT_RESOLUTION;

    public SortedCoverage() {
    }

    public int getCoarseResolution() {
        return m_CoarseResolution;
    }

    public void setCoarseResolution(final int pCoarseResolution) {
        m_CoarseResolution = pCoarseResolution;
    }

    /**
     * implemt the visitor pattern for a SAMRecord
     *
     * @param record
     * @param added
     */
    @Override
    public boolean visit(final IExtendedSamRecord record, final Object... added) {
        IChromosome chr = record.getChromosome();
        if (chr == null)
            return false;
        SortedChromosomeCoverage coverage = getCoverageForChromosome(chr);
        return coverage.visit(record, added);
    }

    protected SortedChromosomeCoverage getCoverageForChromosome(final IChromosome pChr) {
        SortedChromosomeCoverage coverage = m_UsedCoverages.get(pChr);
        if (coverage == null) {
            coverage = SortedChromosomeCoverage.getChromosomeCoverage(pChr);
            m_UsedCoverages.put(pChr, coverage);
        }
        return coverage;
    }

    /**
     * initialize the item
     *
     * @param added any additional data
     */
    @Override
    public void initialize(final Object... added) {
        m_UsedCoverages.clear();
    }

    /**
     * take action at the end of a process
     *
     * @param added other data
     */
    @Override
    public void finish(final Object... added) {
        for (SortedChromosomeCoverage coverage : m_UsedCoverages.values()) {
            coverage.finish(added);
        }
        buildCoarseResolitionFile();
    }

    public void buildCoarseResolitionFile() {
        try {
            IAnalysisParameters ap = AnalysisParameters.getInstance();
            String name = ap.getJobName();
            PrintWriter out = new PrintWriter(new FileWriter(name + "." + getCoarseResolution() + ".wig"));
             
            out.println("track type=wiggle_0 name=\"" + name + "\"");
            final SortedChromosomeCoverage[] scs = m_UsedCoverages.values().toArray(SortedChromosomeCoverage.EMPTY_ARRAY);
            Arrays.sort(scs);
            for (int i = 0; i < scs.length; i++) {
                SortedChromosomeCoverage sc = scs[i];
                appendCoverage(out, sc);
                System.out.println("Added chromosome " + sc.getChromosome());
            }
            out.close();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void appendCoverage(final PrintWriter pOut, final SortedChromosomeCoverage pCoverage) {
        try {
            String chr = pCoverage.getChromosome().toString();
            int binstart = 0;
            double[] count = new double[2];  // coverage and count
            pOut.println("variableStep chrom=" + chr + " span=" + getCoarseResolution());
            LineNumberReader rdr = new LineNumberReader(new FileReader(pCoverage.getOutputFileName()));
            String s = rdr.readLine();
            while (s != null) {
                binstart = handleLine(pOut, chr, s, binstart, count);
                s = rdr.readLine();
            }
            String valueStr;
            if (count[0] > 0 &&  count[1] > 0) {
                double value = count[0] / count[1];
                if(value > 100)  {
                    value = 99;
                }
                  valueStr = String.format("%6.1f",value).trim();
            }
            else {
                  valueStr = "0.0";

            }
            pOut.println(Integer.toString(binstart) + "\t" + valueStr);
            count[0] = 0;
            count[1] = 0;


        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private int handleLine(final PrintWriter pOut, String chr, final String pS, int binstart, double[] count) {
        String[] values = pS.split("=");
        double number = Double.parseDouble(values[1].trim());
        String[] range = values[0].split("-");
        int start = Integer.parseInt(range[0]);
        final int res = getCoarseResolution();
        int binEnd = binstart + res;
        count[0] += number;
        count[1] += 1;
        if (start < binEnd) {
            return binstart; // same bin
        }
        else {
            final double currentCount = count[0];
            if (currentCount > 0) {
                pOut.println(Integer.toString(start) + "\t" + String.format("%6.1f", count[0] / count[1]).trim());
                count[0] = 0;
                count[1] = 0;
                 }
            while (start > binstart)
                binstart += res;

            return binstart;
        }
    }

    /**
     * if true then then the algorithm will not work on unsorted data
     *
     * @return as above
     */
    @Override
    public boolean isSortedRequired() {
        return true;
    }

    /**
     * reset to base state
     */
    @Override
    public void reset() {
        m_UsedCoverages.clear();
    }

    public static void main(String[] args) {
        System.setProperty("line.separator","\n");
        IAnalysisParameters ap = AnalysisParameters.getInstance();
        String jobName = "TestRead";
        if (args.length > 0)
            jobName = args[0];
        ap.setJobName(jobName);
        DefaultChromosome.setDefaultChromosomeSet("human");
        SortedCoverage sc = new SortedCoverage();
        File[] itels = new File(System.getProperty("user.dir")).listFiles();
        for (int i = 0; i < itels.length; i++) {
            File itel = itels[i];
            final String fName = itel.getName();
            int index = fName.indexOf("_chr");
            if (index == -1 || !fName.endsWith(".coverage"))
                continue;
            String chromosome = fName.substring(index + 1).replace(".coverage", "");
            IChromosome chr = DefaultChromosome.parseChromosome(chromosome);
            sc.getCoverageForChromosome(chr);
        }
        for (int i = 100; i < 100000; i *= 10) {
            sc.setCoarseResolution(i);
            sc.buildCoarseResolitionFile();
            System.out.println("Build file " + i);
        }
    }
}
