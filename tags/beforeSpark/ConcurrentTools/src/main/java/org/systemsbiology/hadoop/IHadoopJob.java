package org.systemsbiology.hadoop;

import org.apache.hadoop.mapreduce.*;

import java.util.*;

/**
 * org.systemsbiology.hadoop.IHadoopJob
 * User: steven
 * Date: Jun 14, 2010
 */
public interface IHadoopJob {
    public static final IHadoopJob[] EMPTY_ARRAY = {};

    public Map<String, Long> getAllCounterValues();

    public Job getJob();

    public String getName();

    @SuppressWarnings("UnusedDeclaration")
    public UUID getUID();

    public void setJob(Job pJob);

    public void makeJarAsNeeded();

    public int getStepNumber();

    @SuppressWarnings("UnusedDeclaration")
    public void incrementStepNumber();

    @SuppressWarnings("UnusedDeclaration")
    public int getNumberInstances();

    @SuppressWarnings("UnusedDeclaration")
    public void setNumberInstances(final int pNumberInstances);

    public String getJarFile();

    public void setJarFile(String pJarFile);

    public String getMainClass();

    public void setMainClass(String pMainClass);

    @SuppressWarnings("UnusedDeclaration")
    public String getSourceFiles();

    @SuppressWarnings("UnusedDeclaration")
    public void setSourceFiles(String pSourceFiles);

    public String getOutputDirectory();

    public void setOutputDirectory(String pOutputDirectory);

    @SuppressWarnings("UnusedDeclaration")
    public String getJobDirectory();

    public void setJobDirectory(String pJobDirectory);

    public String getHDFSFilesDirectory();

    public void setHDFSFilesDirectory(String pHDFSFilesDirectory);

    @SuppressWarnings("UnusedDeclaration")
    public String getEmptyOutputDirectory();

    public void setEmptyOutputDirectory(String pEmptyOutputDirectory);

    public String[] getOtherArgs();

    public String[] getAllArgs();

    public void setOtherArgs(String[] pOtherArgs);

    public String buildCommandString();

    /**
     * build command string to make all files in directoru public
     * as this is not the default
     *
     * @return
     */
    public String buildChmodCommandString();
}
