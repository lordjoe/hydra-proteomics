package org.systemsbiology.gatk;

import net.sf.samtools.*;
import org.broadinstitute.sting.gatk.*;
import org.broadinstitute.sting.gatk.contexts.*;
import org.broadinstitute.sting.gatk.refdata.*;
import org.broadinstitute.sting.gatk.samples.*;
import org.broadinstitute.sting.gatk.walkers.*;
import org.broadinstitute.sting.utils.sam.*;

import java.util.*;

/**
 * Walks over the input data set, calculating the number of reads seen for diagnostic purposes.
 *
 * <p>
 * Can also count the number of reads matching a given criterion using read filters (see the
 * --read-filter command line argument).  Simplest example of a read-backed analysis.
 *
 *
 * <h2>Input</h2>
 * <p>
 * One or more BAM files.
 * </p>
 *
 * <h2>Output</h2>
 * <p>
 * Number of reads seen.
 * </p>
 *
 * <h2>Examples</h2>
 * <pre>
 * java -Xmx2g -jar GenomeAnalysisTK.jar \
 *   -R ref.fasta \
 *   -T CountReads \
 *   -o output.txt \
 *   -I input.bam \
 *   [-L input.intervals]
 * </pre>
 *
 */
@Requires({DataSource.READS, DataSource.REFERENCE})
public class FindInAlternateSpeciesWalker extends ReadWalker<Integer, Integer> {

    private static Set<String> gInterestingReads;

    public static Set<String> getInterestingReads() {
        return gInterestingReads;
    }

    public static void setInterestingReads(Set<String> interestingReads) {
        gInterestingReads = interestingReads;
    }

    private final Map<String,SAMRecord>  m_SequenceToRecord = new HashMap<String, SAMRecord>();

    public FindInAlternateSpeciesWalker() {
    }

    public Integer map(ReferenceContext ref, GATKSAMRecord read, ReadMetaDataTracker tracker) {
        String readString = read.getReadString();
        Set<String> interestingReads = getInterestingReads();
        if(interestingReads.contains(readString))
            m_SequenceToRecord.put(readString,read);
         return 1;
    }

    public Integer reduceInit() { return 0; }

    public Integer reduce(Integer value, Integer sum) {
        return value + sum;
    }

    @Override
    public boolean requiresOrderedReads() {
        return super.requiresOrderedReads();    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public void setToolkit(GenomeAnalysisEngine toolkit) {
        super.setToolkit(toolkit);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    protected SAMSequenceDictionary getMasterSequenceDictionary() {
        return super.getMasterSequenceDictionary();    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public SampleDB getSampleDB() {
        return super.getSampleDB();    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    protected Sample getSample(String id) {
        return super.getSample(id);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public void initialize() {
        super.initialize();
        m_SequenceToRecord.clear();
    }

    @Override
    public boolean isDone() {
        return super.isDone();    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public void onTraversalDone(Integer result) {
        super.onTraversalDone(result);    //To change body of overridden methods use File | Settings | File Templates.
    }
}
