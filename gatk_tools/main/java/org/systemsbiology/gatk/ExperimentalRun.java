package org.systemsbiology.gatk;

import net.sf.samtools.*;

import java.io.*;
import java.util.*;

/**
 * org.systemsbiology.gatk.RegionWithVariants
 * User: steven
 * Date: 6/12/12
 */
public class ExperimentalRun implements Comparable<ExperimentalRun> {
    public static final ExperimentalRun[] EMPTY_ARRAY = {};

    private final GeneExperiment m_Experiment;
    private final ExperimentalSubject m_Subject;
    private final String m_Id;
    private final ExperimentalCondition m_Condition;
    private final Map<GeneRegion, RegionWithVariants> m_RegionsToVariants = new HashMap<GeneRegion, RegionWithVariants>();


    public ExperimentalRun(GeneExperiment exp, final ExperimentalSubject subject, final String id) {
        this(exp, subject, id, ExperimentalCondition.unknown);
    }

    public ExperimentalRun(GeneExperiment exp, final ExperimentalSubject subject, final String id, ExperimentalCondition ec) {
        m_Subject = subject;
        m_Id = id;
        m_Condition = ec;
        m_Experiment = exp;
    }

    public GeneExperiment getExperiment() {
        return m_Experiment;
    }

    public ExperimentalCondition getCondition() {
        return m_Condition;
    }

    public ExperimentalSubject getSubject() {
        return m_Subject;
    }

    public String getId() {
        return m_Id;
    }


    public RegionWithVariants[] getRegionsWithVariants() {
        Collection<RegionWithVariants> values = m_RegionsToVariants.values();
        synchronized (values) {
            RegionWithVariants[] ret = values.toArray(RegionWithVariants.EMPTY_ARRAY);
            Arrays.sort(ret);
            return ret;
        }
    }

    public RegionWithVariants getRegionsWithVariants(GeneRegion region) {
        synchronized (m_RegionsToVariants) {
            RegionWithVariants ret = m_RegionsToVariants.get(region);
            if (ret == null) {
                ret = new RegionWithVariants(getSubject(), getId(), region);
                m_RegionsToVariants.put(region, ret);
            }
            return ret;
        }
    }

    public GeneRegion getRegionContaining(GeneLocation loc) {
        return getExperiment().getRegionContaining(loc);
    }

    public void addVariant(GeneVariant variant) {
        GeneRegion regionContaining = getRegionContaining(variant.getLocation());
        if (regionContaining == null)
            return; // todo throw exception???
        RegionWithVariants regionsWithVariants = getRegionsWithVariants(regionContaining);
        regionsWithVariants.addVariant(variant);

    }

    public GeneVariant[] getAllVariants() {
        List<GeneVariant> holder = new ArrayList<GeneVariant>();
        RegionWithVariants[] rts = getRegionsWithVariants();
        for (int i = 0; i < rts.length; i++) {
            RegionWithVariants rt = rts[i];
            holder.addAll(Arrays.asList(rt.getVariants()));
        }
        GeneVariant[] ret = new GeneVariant[holder.size()];
        holder.toArray(ret);
        return ret;

    }


    @Override
    public int compareTo(ExperimentalRun o) {
        if (this == o)
            return 0;
        if (getSubject() != o.getSubject())
            return getSubject().compareTo(o.getSubject());

        int ret = getId().compareTo(o.getId());
        if (ret != 0)
            return ret;
        return ret;
    }

    @Override
    public String toString() {
        return getSubject() + ":" +
                getId() + "-" + getCondition();

    }

    public void readSampleDirectory() {
        File dir = new File(getId());
        File[] files = dir.listFiles();
        if (files == null)
            return;
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            String name = file.getName();
            if (name.endsWith(".vcf")) {
                readVCF(file);
            }
        }

    }

    public void saveReadsWithLocations(InterestingVariation[] loc, SAMFileWriter out, File intervals) {
        File dir = new File(getId());
        File[] files = dir.listFiles();
        if (files == null)
            return;
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            String name = file.getName();
            if (name.endsWith(".sortedByPos.Grouped.Reordered.bam")) {
                SAMFileHeader fh = out.getFileHeader();
                SAMSequenceDictionary sd = fh.getSequenceDictionary();
                if (sd == null) {
                    fh.setSequenceDictionary(GeneUtilities.getReferenceDictionary());
                }
                SamToolsRunner.runCountLoci(file, out, loc, intervals);
                //   throw new UnsupportedOperationException("Fix This"); // ToDo
            }
        }

    }


    private void readVCF(final File file) {
        String[] lines = GeneUtilities.readInLines(file);
        GeneVariant[] variants = GeneUtilities.variantsFromLines(lines);
        for (int j = 0; j < variants.length; j++) {
            GeneVariant variant = variants[j];
            addVariant(variant);
        }
    }

    public void mapRecordsToMouse(InterestingVariation[] vars,Map<String, SAMRecord> interestingRecords, Map<String, SAMRecord> allRecords) {
        File dir = new File(getId());
        File[] files = dir.listFiles();
        if (files == null)
            return;
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            String name = file.getName();
            if (name.startsWith("mouse") && name.endsWith(".bam")) {
                   SamToolsRunner.runFindInMouse(file, vars,interestingRecords, allRecords);
                //   throw new UnsupportedOperationException("Fix This"); // ToDo
            }
        }

    }

    public void mapRecordsToHuman(InterestingVariation[] vars,Map<String, SAMRecord> interestingRecords, Map<String, SAMRecord> allRecords) {
        File dir = new File(getId());
        File[] files = dir.listFiles();
        if (files == null)
            return;
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            String name = file.getName();
            if (name.startsWith("human") && name.endsWith("sortedByPos.Grouped.Reordered.bam")) {
                   SamToolsRunner.runFindInMouse(file, vars,interestingRecords, allRecords);
                //   throw new UnsupportedOperationException("Fix This"); // ToDo
            }
        }

    }
}
