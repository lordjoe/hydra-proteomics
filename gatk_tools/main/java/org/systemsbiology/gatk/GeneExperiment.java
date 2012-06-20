package org.systemsbiology.gatk;

import java.io.*;
import java.util.*;

/**
 * org.systemsbiology.gatk.GeneExperiment
 * User: Steve
 * Date: 6/14/12
 */
public class GeneExperiment {
    public static final GeneExperiment[] EMPTY_ARRAY = {};

    private Set<GeneVariant> m_CommonVariants = new HashSet<GeneVariant>();
    private final List<GeneRegion> m_Regions = new ArrayList<GeneRegion>();
    private final Map<ExperimentalSubject, GeneSampleSet> m_Subjects = new HashMap<ExperimentalSubject, GeneSampleSet>();

    public ExperimentalSubject[] getSubjects() {
        ExperimentalSubject[] ret = m_Subjects.keySet().toArray(ExperimentalSubject.EMPTY_ARRAY);
        Arrays.sort(ret);
        return ret;
    }


    public GeneSampleSet getGeneSampleSet(ExperimentalSubject subj) {
        GeneSampleSet ret = m_Subjects.get(subj);
        if (ret == null) {
            ret = new GeneSampleSet(this, subj);
            m_Subjects.put(subj, ret);
        }
        return ret;
    }

    public GeneRegion[] getRegions() {
        GeneRegion[] geneRegions = m_Regions.toArray(GeneRegion.EMPTY_ARRAY);
        Arrays.sort(geneRegions);
        return geneRegions;
    }

    public GeneRegion getRegionContaining(GeneLocation loc) {
        for (GeneRegion region : m_Regions) {
            if (region.isWithin(loc))
                return region;
        }
        return null;
    }


    public void readGeneMap(final File geneFile) {
        String[] lines = GeneUtilities.readInLines(geneFile);
        GeneRegion[] regions = GeneUtilities.regionsFromLines(lines);
        for (int i = 0; i < regions.length; i++) {
            GeneRegion region = regions[i];
            m_Regions.add(region);
        }
        Collections.sort(m_Regions);
    }


    public GeneSampleSet[] getGeneSampleSets() {
        GeneSampleSet[] ret = m_Subjects.values().toArray(GeneSampleSet.EMPTY_ARRAY);
        Arrays.sort(ret);
        return ret;
    }


    public GeneVariant[] getAllVariants() {
        List<GeneVariant> holder = new ArrayList<GeneVariant>();
        ExperimentalRun[] rts = getRuns();
        for (int i = 0; i < rts.length; i++) {
            ExperimentalRun rt = rts[i];
            holder.addAll(Arrays.asList(rt.getAllVariants()));
        }
        GeneVariant[] ret = new GeneVariant[holder.size()];
        holder.toArray(ret);
        return ret;
    }

    public GeneVariant[] getCommonVariants() {
        GeneVariant[] vts = null;
        if (m_CommonVariants.isEmpty()) {
            GeneVariant[] allVarients = getAllVariants();
            vts = GeneUtilities.commonVariants(allVarients, getRuns().length);
            m_CommonVariants.addAll(Arrays.asList(vts));
        }
        vts = m_CommonVariants.toArray(GeneVariant.EMPTY_ARRAY);
        Arrays.sort(vts);
        return vts;
    }


    public ExperimentalRun[] getRuns() {
        List<ExperimentalRun> holder = new ArrayList<ExperimentalRun>();
        for (ExperimentalSubject subject : getSubjects()) {
            GeneSampleSet set = getGeneSampleSet(subject);
            holder.addAll(Arrays.asList(set.getRuns()));
        }
        ExperimentalRun[] ret = new ExperimentalRun[holder.size()];
        holder.toArray(ret);
        return ret;
    }


    private void readExperiments(final File describingFile) {
        String[] Strings = GeneUtilities.readInLines(describingFile);
        for (int i = 0; i < Strings.length; i++) {
            String string = Strings[i];
            handleLine(string);
        }

    }

    protected void handleLine(String s) {
        s = s.trim();
        if (s.length() == 0)
            return;
        if (s.startsWith("#"))
            return;
        String[] items = s.split("\t");
        String id = "Sample" + items[0];
        ExperimentalSubject gs = ExperimentalSubject.getSubject(items[1]);
        ExperimentalCondition cond = ExperimentalCondition.valueOf(items[2]);

        GeneSampleSet set = getGeneSampleSet(gs);
        ExperimentalRun region = set.getRegion(id, cond);

    }

    public void showVariants(PrintStream out) {
        GeneVariant[] vts = getCommonVariants();
        System.out.println("Common " + vts.length);
        for (int j = 0; j < vts.length; j++) {
            GeneVariant commonVariant = vts[j];
            out.println(commonVariant);
        }
        ExperimentalSubject[] subjects = getSubjects();
        for (int i = 0; i < subjects.length; i++) {
            ExperimentalSubject subject = subjects[i];
            GeneSampleSet gs = getGeneSampleSet(subject);
            ExperimentalRun[] runs = gs.getRuns();
            GeneVariant[] all = gs.getAllVariants();
            GeneVariant[] commonVariants = gs.getCommonVariants();
            out.println("Subject " + subject + " " + commonVariants.length + " in " + (all.length / runs.length) + " per run");
            for (int j = 0; j < commonVariants.length; j++) {
                GeneVariant commonVariant = commonVariants[j];
                out.println(commonVariant);
            }
            out.println();
            GeneVariant[] uncommon = gs.getUncommonVariants();
            out.println("Subject " + subject + "- Unshared   " + uncommon.length);
            for (int j = 0; j < uncommon.length; j++) {
                GeneVariant commonVariant = uncommon[j];
                out.println(commonVariant);
            }

        }

    }


    public void showCommonInterestingGenes(PrintStream out) {
        GeneRegion loc = null;
        GeneVariant[] uncommon = getCommonVariants();
        out.println("Common Interesting Genes   " + uncommon.length);
        for (int j = 0; j < uncommon.length; j++) {
            GeneVariant var = uncommon[j];
            GeneRegion myGene = this.getRegionContaining(var.getLocation());
            if (myGene != loc) {
                loc = myGene;
                out.println(loc);

            }
            out.println("     " + var  + "\t" + var.getLocation().asIGVInterval());
        }

    }



    public void showInterestingGenes(PrintStream out) {
        GeneRegion loc = null;
        ExperimentalSubject[] subjects = getSubjects();
        for (int i = 0; i < subjects.length; i++) {
            ExperimentalSubject subject = subjects[i];
            GeneSampleSet gs = getGeneSampleSet(subject);
            GeneVariant[] uncommon = gs.getUncommonVariants();
             out.println(  );
             out.println(  );
            out.println("Subject " + subject + "- Interesting Genes   " + uncommon.length);
            for (int j = 0; j < uncommon.length; j++) {
                GeneVariant var = uncommon[j];
                GeneRegion myGene = this.getRegionContaining(var.getLocation());
                if (myGene != loc) {
                    loc = myGene;
                    out.println(loc );

                }
                out.println("     " + var + "\t" + var.getLocation().asIGVInterval());
            }

        }

    }


    public static void main(String[] args) {
        File describingFile = new File(args[0]);
        File geneFile = new File(args[1]);
        GeneExperiment exp = new GeneExperiment();

        exp.readExperiments(describingFile);
        exp.readGeneMap(geneFile);
        ExperimentalSubject[] subjects = exp.getSubjects();
        for (int i = 0; i < subjects.length; i++) {
            ExperimentalSubject subject = subjects[i];
            GeneSampleSet gs = exp.getGeneSampleSet(subject);
            ExperimentalRun[] runs = gs.getRuns();
            for (int j = 0; j < runs.length; j++) {
                ExperimentalRun run = runs[j];
                run.readSampleDirectory();
            }

        }
      //  exp.showVariants(System.out);
        System.out.println(  );
        System.out.println(  );
        exp.showCommonInterestingGenes(System.out);
        System.out.println(  );
        System.out.println(  );
         exp.showInterestingGenes(System.out);
    }


}
