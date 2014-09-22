package org.systemsbiology.hadoop;

import com.lordjoe.utilities.*;
import org.apache.hadoop.mapreduce.*;

/**
 * org.systemsbiology.hadoop.HadoopProgressManager
 *
 * @author Steve Lewis
 * @date 14/11/13
 */
public class HadoopProgressManager implements IProgressHandler {
    public static final int DEFAULT_REPORT_INTERVAL = 40000;

    private static enum HadoopProgress {
        progress
    }

    private long reportInterval = DEFAULT_REPORT_INTERVAL;
    private final TaskAttemptContext context;
    private long last_increment_time = System.currentTimeMillis();

    public HadoopProgressManager(TaskAttemptContext context) {
        this.context = context;
    }

    public long getReportInterval() {
        return reportInterval;
    }

    @SuppressWarnings("UnusedDeclaration")
    public void setReportInterval(long reportInterval) {
        this.reportInterval = reportInterval;
    }

    /**
     * progress is incremented - what this does or means is unclear
     *
     * @param increment amount to increment
     */
    @Override
    public void incrementProgress(int increment) {
        long now = System.currentTimeMillis();
        if (now - last_increment_time > getReportInterval()) {
            Counter counter;
            if (context instanceof Mapper.Context) {
                //noinspection unchecked
                counter = ((Mapper.Context) context).getCounter(HadoopProgress.progress);
                counter.increment(increment);
                last_increment_time = now;
                return;
            }
            if (context instanceof Reducer.Context) {
                 //noinspection unchecked
                counter = ((Reducer.Context) context).getCounter(HadoopProgress.progress);
                counter.increment(increment);
                last_increment_time = now;
                return;
             }
            throw new IllegalStateException("context is not a Mapper or a Reducer Context");
        }

    }

    /**
     * set progress to 0
     */
    @Override
    public void resetProgress() {
        // nothing to do here
    }

}
