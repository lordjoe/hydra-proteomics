package org.systemsbiology.gatk;

import java.util.*;

/**
 * org.systemsbiology.gatk.GeneSampleSet
 * User: Steve
 * Date: 6/14/12
 */
public class GeneSampleSet {
    public static final GeneSampleSet[] EMPTY_ARRAY = {};

    private final GeneExperiment m_Experiment;
    private final ExperimentalSubject m_Subject;
    private final Map<String,ExperimentalRun> m_Samples = new HashMap<String, ExperimentalRun>();

    public GeneSampleSet(final GeneExperiment experiment, final ExperimentalSubject subject) {
        m_Experiment = experiment;
        m_Subject = subject;
    }

    public ExperimentalSubject getSubject() {
        return m_Subject;
    }

    public GeneExperiment getExperiment() {
        return m_Experiment;
    }

    public ExperimentalRun[] getRuns() {
        ExperimentalRun[] ret = m_Samples.values().toArray(ExperimentalRun.EMPTY_ARRAY);
        Arrays.sort(ret);
        return ret;
    }

    public GeneVariant[] getCommonVariants()
    {
        List<GeneVariant> holder = new ArrayList<GeneVariant>();
        ExperimentalRun[] runs = getRuns();
        if(runs.length == 0)
            return  GeneVariant.EMPTY_ARRAY;
         GeneVariant[] vts = getAllVariants( );

        return GeneUtilities.commonVariants(vts,runs.length);

    }

    public GeneVariant[] getUncommonVariants()
    {
         GeneVariant[] all = getCommonVariants();
        GeneVariant[] commonToAll = getExperiment().getCommonVariants();
        Set<GeneVariant> holder = new HashSet<GeneVariant>(Arrays.asList(all));
        holder.removeAll(Arrays.asList(commonToAll));
        GeneVariant[] uncommon = holder.toArray(GeneVariant.EMPTY_ARRAY);
        Arrays.sort(uncommon);
        return uncommon;
    }


    public GeneVariant[] getAllVariants( ) {
         List<GeneVariant> holder = new ArrayList<GeneVariant>();
        ExperimentalRun[] runs =  getRuns();
        for (int i = 0; i < runs.length; i++) {
            ExperimentalRun region = runs[i];
            holder.addAll(Arrays.asList(region.getAllVariants() ));
        }

         GeneVariant[] ret = new GeneVariant[holder.size()];
         holder.toArray(ret);
         return ret;
    }

    public ExperimentalRun getRegion(String id,ExperimentalCondition ec) {
        GeneExperiment experiment = getExperiment();
        ExperimentalRun ret = m_Samples.get(id);
        if(ret == null) {
            ret = new  ExperimentalRun(getExperiment(), getSubject() ,id,ec);
             m_Samples.put(id, ret);
        }
        return ret;
    }



}
