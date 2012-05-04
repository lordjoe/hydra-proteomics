package org.systemsbiology.hadoop;

import org.apache.hadoop.mapreduce.*;

import java.io.*;
import java.util.*;

/**
 * org.systemsbiology.hadoop.HadoopJobSet
 * User: steven
 * Date: Jun 7, 2010
 */
public class HadoopJobSet {
    public static final HadoopJobSet[] EMPTY_ARRAY = {};


    private final List<Job> m_Jobs = new ArrayList<Job>();

    public HadoopJobSet() {
    }

    public HadoopJobSet(Job theJob) {
        this();
        addJob(theJob);
    }

    public void addJob(Job added) {
        m_Jobs.add(added);
    }


    public void removeJob(Job removed) {
        m_Jobs.remove(removed);
    }

    public Job[] getJobs() {
        return m_Jobs.toArray(new Job[0]);
    }

     public boolean runJobs()
    {
        return runJobs(false);
    }


    public boolean runJobs(boolean verbose)
    {
        try {
            boolean success = true;
            for(Job j : m_Jobs)    {
                success |= j.waitForCompletion(verbose);
                if(!success)
                    return false;
            }
            return true;
        }
        catch (Exception e) {
             throw new RuntimeException(e);
         }
     }


}
