package org.systemsbiology.windowedanalysis;

import org.systemsbiology.location.*;
import org.systemsbiology.partitioning.*;
import org.systemsbiology.sam.*;

/**
 * org.systemsbiology.windowedanalysis.PartitionSetValidator
 * written by Steve Lewis
 * on Apr 20, 2010
 */
public class PartitionSetValidator implements IPartitionedSetAnalysis
{
    public static final PartitionSetValidator[] EMPTY_ARRAY = {};
    public static final Class THIS_CLASS = PartitionSetValidator.class;



    /**
     * perform some analysis on a data set
     *
     * @param data
     * @param more
     */
    public void analyze(IPartitionedSamSet data, Object... more) {

        IGeneInterval ivl = data.getInterval();
        for(IExtendedPairedSamRecord rec :  data.getAllRecords()) {
            if(!rec.isPaired())
                throw new IllegalStateException("Unpaired Record");
            if(!ivl.isContainedIn(rec.getStartLocation()))
                throw new IllegalStateException("Start outside interval");
        }
    }

    /**
     * take action at the end of a process
     *
     * @param added  other data
     */
    public void finish(Object... added) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
