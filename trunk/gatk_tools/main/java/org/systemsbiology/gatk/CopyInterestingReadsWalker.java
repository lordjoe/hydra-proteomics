package org.systemsbiology.gatk;

import net.sf.samtools.*;
import org.broadinstitute.sting.commandline.*;
import org.broadinstitute.sting.gatk.contexts.*;
import org.broadinstitute.sting.gatk.refdata.*;
import org.broadinstitute.sting.gatk.walkers.*;
import org.broadinstitute.sting.utils.*;
import org.broadinstitute.sting.utils.fragments.*;
import org.broadinstitute.sting.utils.pileup.*;

import java.io.*;
import java.util.*;

/**
 * Walks over the input data set, calculating the total number of covered loci for diagnostic purposes.
 * <p/>
 * <p>
 * Simplest example of a locus walker.
 * <p/>
 * <p/>
 * <h2>Input</h2>
 * <p>
 * One or more BAM files.
 * </p>
 * <p/>
 * <h2>Output</h2>
 * <p>
 * Number of loci traversed.
 * </p>
 * <p/>
 * <h2>Examples</h2>
 * <pre>
 * java -Xmx2g -jar GenomeAnalysisTK.jar \
 *   -R ref.fasta \
 *   -T CountLoci \
 *   -o output.txt \
 *   -I input.bam \
 *   [-L input.intervals]
 * </pre>
 */
public class CopyInterestingReadsWalker extends LocusWalker<Integer, Long> implements TreeReducible<Long> {

    private static final Map<GeneLocation, InterestingVariation> gLocations = new HashMap<GeneLocation, InterestingVariation>();
    private static SAMFileWriter gWriter;


    public static void setLocations(InterestingVariation[] locations) {
        for (int i = 0; i < locations.length; i++) {
            InterestingVariation location = locations[i];
            gLocations.put(location.getLocation(), location);
        }
    }


    public static InterestingVariation getInterestingVariation(GenomeLoc loc) {
        GeneLocation key = buildGeneLocation(loc);
        return gLocations.get(key);
    }

    public static GeneLocation buildGeneLocation(GenomeLoc loc) {
        return new GeneLocation(loc.getContig(),loc.getStart());
    }


    public static SAMFileWriter getWriter() {
        return gWriter;
    }

    public static void setWriter(SAMFileWriter writer) {
        gWriter = writer;
    }

    @Output(doc = "Write count to this file instead of STDOUT")
    PrintStream out;

    private InterestingVariation m_Current;

    public InterestingVariation getCurrent() {
        return m_Current;
    }

    public void setCurrent(InterestingVariation current) {
        m_Current = current;
    }

    @Override
    public Integer map(RefMetaDataTracker tracker, ReferenceContext ref, AlignmentContext context) {
         InterestingVariation iv = getInterestingVariation(ref.getLocus());
        if (getCurrent() == null || !getCurrent().equals(iv) ) {
            setCurrent(iv);
        }
        byte[] bases = ref.getBases();
        byte[] forwardBases = ref.getForwardBases();
        GenomeLocParser gp = ref.getGenomeLocParser();
        ReadBackedPileup bp = context.getBasePileup();
        FragmentCollection<PileupElement> fc = bp.toFragments();
        SAMFileWriter writer = getWriter();
         Collection<PileupElement> srs = fc.getSingletonReads();
        for (PileupElement pe : srs) {
            String eventBases = pe.getEventBases();
            SAMRecord rec = pe.getRead();
            writer.addAlignment(rec);
        }
        return 1;
    }


    public Long reduceInit() {
        return 0l;
    }

    @Override
    public Long reduce(Integer value, Long sum) {
         return value + sum;
    }

    /**
     * Reduces two subtrees together.  In this case, the implementation of the tree reduce
     * is exactly the same as the implementation of the single reduce.
     */
    @Override
    public Long treeReduce(Long lhs, Long rhs) {
        return lhs + rhs;
    }

    @Override
    public void onTraversalDone(Long c) {
        out.println(c);
    }
}
