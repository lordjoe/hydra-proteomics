package org.systemsbiology.remotecontrol;

import org.systemsbiology.hadoop.*;

import java.io.*;
import java.util.*;

/**
 * org.systemsbiology.remotecontrol.IHadoopController
 * User: steven
 * Date: Jun 14, 2010
 */
public interface IHadoopController {
    public static final IHadoopController[] EMPTY_ARRAY = {};


    public String getDefaultDirectory();

    public void setDefaultDirectory(final String pDefaultDirectory);

    /**
     * true is success
     * @param job
     * @return
     */
    public boolean runJob(IHadoopJob job);

    public boolean runJobs(final IHadoopJob[] jobs);


    public void guaranteeFilesOnHDFS(File srcDir, String dest, String hdfsdest);

    public void copyToHDFS(File localFile, String hdfsdest);
 
    public void copyDirectoryToHDFS(String src, String hdfsdest);

    public void copyFileToHDFS(String src, String hdfsdest);

    public void guaranteeFiles(File srcDir, String dest);

    public boolean guaranteeFile(File pJar, String dest);

    public boolean hasFile(File pJar, String dest);

    public HDFSFile[] getFiles(String directory);

    public String getTemporaryDirectory();

    public boolean isHDFSDirectory(String dst);

    public void guaranteeHDFSDirectory(String dst);

    public void uploadFileToHDFS(String fname, String dst);

    public void uploadFile(InputStream fname, String dst);

    public void uploadFile(String fname, String dst);

   public  void uploadFile(File fname, String dst);

    public boolean isSingleCore();
 }
