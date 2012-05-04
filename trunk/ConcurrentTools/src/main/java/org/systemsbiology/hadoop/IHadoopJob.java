package org.systemsbiology.hadoop;

import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.*;
import org.systemsbiology.hadoopgenerated.*;

import java.util.*;

/**
 * org.systemsbiology.hadoop.IHadoopJob
 * User: steven
 * Date: Jun 14, 2010
 */
public interface IHadoopJob {
    public static final IHadoopJob[] EMPTY_ARRAY = {};

    public Map<String,Long> getAllCounterValues();

    public Job getJob();

    public String getName();

     public UUID getUID();

     public void setJob(Job pJob);

    public void makeJarAsNeeded();

    public int getStepNumber();

     public void incrementStepNumber();

    public int getNumberInstances();

     public void setNumberInstances(final int pNumberInstances);

    public String getJarFile();

    public void setJarFile(String pJarFile);

    public String getMainClass();

    public void setMainClass(String pMainClass);

    public String getSourceFiles();

    public void setSourceFiles(String pSourceFiles);

    public String getOutputDirectory();

    public void setOutputDirectory(String pOutputDirectory);

    public String getJobDirectory();

    public void setJobDirectory(String pJobDirectory);

    public String getHDFSFilesDirectory();

    public void setHDFSFilesDirectory(String pHDFSFilesDirectory);

    public String getEmptyOutputDirectory();

    public void setEmptyOutputDirectory(String pEmptyOutputDirectory);

    public String[] getOtherArgs();

    public String[] getAllArgs();

    public void setOtherArgs(String[] pOtherArgs);

    public String buildCommandString();
}
