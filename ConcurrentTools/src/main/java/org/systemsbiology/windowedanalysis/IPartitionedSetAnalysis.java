package org.systemsbiology.windowedanalysis;

import org.systemsbiology.common.*;
import org.systemsbiology.partitioning.*;

/**
 * org.systemsbiology.windowedanalysis.IPartitionedSetAnalysis
 * written by Steve Lewis
 * on Apr 20, 2010
 */
public interface IPartitionedSetAnalysis extends Finishable
{
    public static final IPartitionedSetAnalysis[] EMPTY_ARRAY = {};
    public static final Class THIS_CLASS = IPartitionedSetAnalysis.class;

    /**
     * perform some analysis on a data set
     * @param data
     * @param more
     */
    public void analyze(IPartitionedSamSet data,Object ... more);


}