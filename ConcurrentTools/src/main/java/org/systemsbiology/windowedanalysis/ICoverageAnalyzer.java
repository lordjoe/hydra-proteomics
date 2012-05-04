package org.systemsbiology.windowedanalysis;

import org.systemsbiology.common.*;
import org.systemsbiology.partitioning.*;

/**
 * org.systemsbiology.windowedanalysis.IPartitionedSetAnalysis
 * written by Steve Lewis
 * on Apr 20, 2010
 */
public interface ICoverageAnalyzer extends Finishable
{
    public static final ICoverageAnalyzer[] EMPTY_ARRAY = {};
    public static final Class THIS_CLASS = ICoverageAnalyzer.class;

    public void startWindow(Object ... more);

    public void startActiveWindow(Object ... more);

    public void leaveActiveWindow(Object ... more);

    public void endWindow(Object ... more);

    /**
     * perform some analysis on a data set
     * @param data
     * @param more
     */
    public void visit(SinglePositionCoverage data,Object ... more);


}