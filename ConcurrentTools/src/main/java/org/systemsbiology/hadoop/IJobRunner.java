package org.systemsbiology.hadoop;

import org.apache.hadoop.conf.*;
import org.apache.hadoop.mapred.jobcontrol.*;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.util.*;

/**
 * org.systemsbiology.hadoop.IJobRunner
 *  extension of Tool which captures the Job to allow readout of Job
 * User: steven
 * Date: 9/26/11
 */
public interface IJobRunner extends Tool {
    public static final IJobRunner[] EMPTY_ARRAY = {};

    /**
     * after
     * @return
     */
    public Job getJob();

    /**
     * just like run but allowing a configuration to be passed in
     * @param conf !null conf
     * @param args  !null arguments
     * @return 0 for success
     */
    public int runJob(Configuration conf, final String[] args) throws Exception ;

}
