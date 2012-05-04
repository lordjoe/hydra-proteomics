package org.systemsbiology.hadoop;

import org.apache.hadoop.conf.*;
import org.apache.hadoop.mapred.jobcontrol.*;
import org.apache.hadoop.mapreduce.Job;

/**
 * org.systemsbiology.hadoop.ConfiguredJobRunner
 * User: steven
 * Date: 9/26/11
 */
public class ConfiguredJobRunner extends Configured
{
    public static final ConfiguredJobRunner[] EMPTY_ARRAY = {};

    private Job m_Job;

    /**
     * after the job is run return the Job
     * @return
     */
    public Job getJob() {
        return m_Job;
    }

    public void setJob(final Job pJob) {
        m_Job = pJob;
    }
}
