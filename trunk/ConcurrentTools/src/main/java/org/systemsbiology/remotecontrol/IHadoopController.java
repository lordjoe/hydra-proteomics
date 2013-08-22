package org.systemsbiology.remotecontrol;

import org.systemsbiology.hadoop.*;

import java.io.*;

/**
 * org.systemsbiology.remotecontrol.IHadoopController
 * User: steven
 * Date: Jun 14, 2010
 */
public interface IHadoopController {
    public static final IHadoopController[] EMPTY_ARRAY = {};


    @SuppressWarnings("UnusedDeclaration")
    public String getDefaultDirectory();

    @SuppressWarnings("UnusedDeclaration")
    public void setDefaultDirectory(final String pDefaultDirectory);

    /**
     * true is success
     * @param job
     * @return
     */
    public boolean runJob(IHadoopJob job);

    @SuppressWarnings("UnusedDeclaration")
    public boolean runJobs(final IHadoopJob[] jobs);


    @SuppressWarnings("UnusedDeclaration")
    public void guaranteeFilesOnHDFS(File srcDir, String dest, String hdfsdest);

    @SuppressWarnings("UnusedDeclaration")
    public void copyToHDFS(File localFile, String hdfsdest);
 
    public void copyDirectoryToHDFS(String src, String hdfsdest);

    @SuppressWarnings("UnusedDeclaration")
    public void copyFileToHDFS(String src, String hdfsdest);

    public void guaranteeFiles(File srcDir, String dest);

    public boolean guaranteeFile(File pJar, String dest);

    public boolean hasFile(File pJar, String dest);

    @SuppressWarnings("UnusedDeclaration")
    public HDFSFile[] getFiles(String directory);

    public String getTemporaryDirectory();

    @SuppressWarnings("UnusedDeclaration")
    public boolean isHDFSDirectory(String dst);

    public void guaranteeHDFSDirectory(String dst);

    @SuppressWarnings("UnusedDeclaration")
    public void uploadFileToHDFS(String fname, String dst);

    @SuppressWarnings("UnusedDeclaration")
    public void uploadFile(InputStream fname, String dst);

    public void uploadFile(String fname, String dst);

   public  void uploadFile(File fname, String dst);

    @SuppressWarnings("UnusedDeclaration")
    public boolean isSingleCore();

    /**
     * shut down all running sessions
     */
    public void disconnect();
 }
