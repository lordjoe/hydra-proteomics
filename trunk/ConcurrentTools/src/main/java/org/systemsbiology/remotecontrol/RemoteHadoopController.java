package org.systemsbiology.remotecontrol;

import org.apache.hadoop.conf.*;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.hdfs.*;
import org.apache.hadoop.util.*;
import org.systemsbiology.common.*;
import org.systemsbiology.hadoop.*;

import java.io.*;
import java.lang.reflect.*;
import java.net.*;
import java.util.*;

/**
 * org.systemsbiology.remotecontrol.RemoteHadoopController
 * User: steven
 * Date: Jun 4, 2010
 */
public class RemoteHadoopController implements IHadoopController {
    public static final RemoteHadoopController[] EMPTY_ARRAY = {};

    public static final int MIN__FILE_LENGTH = 30;

    private final RemoteSession m_Session;
    private final IFileSystem m_HDFSAccessor;
    private String m_DefaultDirectory;
    private Set<String> m_ExistingDirectories = new HashSet<String>();

    public static final String HADOOP_COMMAND = "/home/www/hadoop/bin/hadoop ";  //     "hadoop ";

    public RemoteHadoopController(final RemoteSession pSession) {
        m_Session = pSession;
        m_HDFSAccessor = new HDFSAccessor(m_Session.getHost(), RemoteUtilities.getPort());
        //  m_FTPAccessor = new FTPWrapper(m_Session.getUser(), m_Session.getPassword(), m_Session.getHost());
    }

    public RemoteSession getSession() {
        return m_Session;
    }

    public IFileSystem getHDFSAccessor() {
        return m_HDFSAccessor;
    }


    public String getDefaultDirectory() {
        return m_DefaultDirectory;
    }

    public void setDefaultDirectory(final String pDefaultDirectory) {
        m_DefaultDirectory = pDefaultDirectory;
    }

    @Override
    public boolean runJobs(final IHadoopJob[] jobs) {
        if (jobs == null)
            return true; // nothing to do
        boolean ret = true;
        for (int i = 0; i < jobs.length; i++) {
            IHadoopJob job = jobs[i];
            ret &= runJob(job);
            if (!ret)
                return ret;
        }
        return ret;
    }


    @Override
    public boolean runJob(IHadoopJob job) {
        String jarFile = job.getJarFile();
        InputStream is = null;

        // who do we want to run as
        RemoteSession session = getSession();
        String user = session.getUser();

        File jar = null;
        if (jarFile != null && jarFile.startsWith("res://")) {
            String substring = jarFile.substring("res://".length());
            is = getClass().getResourceAsStream(substring);
            if (is == null)
                throw new IllegalStateException("Jar resource " + jarFile + " does not exist");
        }

        //    if (HadoopJob.isJarRequired() && is == null) {

        jar = new File(jarFile);
        if (!jar.exists())
            throw new IllegalStateException("Jar file " + jar + " does not exist");
        //      guaranteeFiles(inputs, job.getFilesDirectory());
        Configuration conf = buildConfiguration(jarFile);
        //    conf.set("mapred.job.reuse.jvm.num.tasks", "1");

        conf.set("user.name",user);

        IFileSystem hdfsAccessor = getHDFSAccessor();
        String s = job.getOutputDirectory();
        String emptyOutputDirectory = HDFSUtilities.getEmptyOutputDirectory(s, hdfsAccessor);

        //      _ think this is bad
        // hdfsAccessor.guaranteeDirectory(emptyOutputDirectory);

        // todo fix this total hack
        if ("org.systemsbiology.hadoopgenerated.HadoopTest".equals(job.getMainClass()))
            hdfsAccessor.expunge(s); // which is it
        if ("org.systemsbiology.hadoopgenerated.HadoopTest".equals(job.getMainClass()))
            hdfsAccessor.expunge(s); // which is it
        //      String cmd = HADOOP_COMMAND + "fs -mkdir " + s;
        //      ech.execCommand(cmd, IOutputListener.DEFAULT_LISTENERS);

        job.setEmptyOutputDirectory(emptyOutputDirectory);
        job.setOutputDirectory(emptyOutputDirectory);
        hdfsAccessor.expunge(emptyOutputDirectory);

        String command = job.buildCommandString();
        System.out.println(command);
        String mainClass = job.getMainClass();

        String[] allArgs = job.getAllArgs();

        try {
            Class<?> mainCls = Class.forName(mainClass);
            if (IJobRunner.class.isAssignableFrom(mainCls)) {
                Class<? extends IJobRunner> mClass = (Class<? extends IJobRunner>) mainCls;
                IJobRunner realMain = mClass.newInstance();

                int result = realMain.runJob(conf, allArgs);
                ((HadoopJob) job).setJob(realMain.getJob());
                return result == 0;
            }
            else {
                if (Tool.class.isAssignableFrom(mainCls)) {
                    Tool realMain = (Tool) mainCls.newInstance();
                    String[] args = buildArgsFromConf(emptyOutputDirectory, conf, allArgs);
                    WrappedToolRunner tr = new WrappedToolRunner(realMain);
                    int result = tr.runJob(conf, allArgs);
                    ((HadoopJob) job).setJob(tr.getJob());
                     return result == 0;
                }
                else {
                    throw new UnsupportedOperationException("Fix This"); // ToDo

                }
            }
            //            if (result.contains("Job Failed"))
//                throw new IllegalStateException(result);
//            System.out.println(result);
        }
        catch (ClassNotFoundException e) {
            throw new RuntimeException(e);

        }
        catch (InstantiationException e) {
            throw new RuntimeException(e);

        }
        catch (IllegalAccessException e) {
            throw new RuntimeException(e);

        }
        catch (Exception e) {
            throw new RuntimeException(e);

        }


        // get the output
        //     final String fileName = job.getEmptyOutputDirectory();
        //     final String s1 = HDFSUtilities.baseFile(fileName);
        //    command = HADOOP_COMMAND + "fs -copyToLocal " + job.getEmptyOutputDirectory() + " " + s1;
        //    System.out.println(command);
        //    String outcome = executeCommand(command);
        //    System.out.println(outcome);

        // drop the hdfs output directory
        // final String s2 = job.getEmptyOutputDirectory();
        //      executeCommand(HADOOP_COMMAND + "fs -rmr " + s2);
        //     System.out.println(outcome);


        //  System.out.println("DONE!!!");
    }

    private String[] buildArgsFromConf(String emptyOutputDirectory, Configuration conf, String[] allArgs) {

        String maxMamory = HadoopUtilities.getProperty("maxClusterMemory");
        if (maxMamory == null)
            maxMamory = "1024";
        List<String> holder = new ArrayList<String>();

        // set twice max memory
        String value = Long.toString((Integer.parseInt(maxMamory) * 2048) + 200 * 1024);
        holder.add("-Dmapred.child.ulimit=" + value);  // in kb
        // DO NOT - DO NOT SET   -xx:-UseGCOverheadLimit   leads to error - Error reading task outputhttp://glad
        holder.add("-Dmapred.child.java.opts=" + "Xmx" + maxMamory + "m");
        holder.add("-Djava.net.preferIPv4Stack=true");

        for (int i =  0; i < allArgs.length ; i++) {
            String allArg = allArgs[i];
            holder.add(allArg);
        }

        String[] ret = new String[holder.size()];
        holder.toArray(ret);
        return ret;
    }

    private Configuration buildConfiguration(String jarFile) {
        Configuration conf = new Configuration();
        conf.set("mapred.jar", jarFile);
        //     }


        String hdfshost = "hdfs://" + RemoteUtilities.getHost() + ":" + RemoteUtilities.getPort();
        conf.set("fs.default.name", hdfshost);
        String jobTracker = RemoteUtilities.getJobTracker();
        conf.set("mapred.job.tracker", jobTracker);

        String maxMamory = HadoopUtilities.getProperty("maxClusterMemory");
        if (maxMamory == null)
            maxMamory = "1024";

        // set twice max memory
        Long.toString((Integer.parseInt(maxMamory) * 2048) + 200 * 1024);
        conf.set("mapred.child.ulimit", Long.toString((Integer.parseInt(maxMamory) * 2048) + 200 * 1024));  // in kb
        // DO NOT - DO NOT SET   -xx:-UseGCOverheadLimit   leads to error - Error reading task outputhttp://glad
        conf.set("mapred.child.java.opts", "-Xmx" + maxMamory + "m" + " "
                + "-Djava.net.preferIPv4Stack=true"
        ); // NEVER DO THIS!!!! +   " -xx:-UseGCOverheadLimit");
        return conf;
    }


    private void guaranteeEmptyDirectory(String dir) {
        IFileSystem accessor = getHDFSAccessor();
        accessor.expunge(dir);
    }

    @Override
    public void guaranteeFilesOnHDFS(final File srcDir, String dest, String hdfsdest) {
        guaranteeFiles(srcDir, dest);
        copyDirectoryToHDFS(dest, hdfsdest);

    }

    @Override
    public void copyToHDFS(File localFile, String hdfsdest) {
        try {
            Configuration conf = new Configuration();
            HftpFileSystem fs = new HftpFileSystem();
            String host = getSession().getHost();
            host = "hdfs://" + host + ":8020/";
            URI ur = new URI(host);

            fs.initialize(ur, conf);
            fs.setConf(conf);

            Path src = new Path(localFile.getAbsolutePath());
            Path dst = new Path(hdfsdest);
            fs.copyFromLocalFile(src, dst);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public String executeCommand(String command) {
        return executeCommand(command, IOutputListener.DEFAULT_LISTENERS);
    }

    public String executeCommand(String command, List<IOutputListener> listeners) {
        RemoteSession session = getSession();
        ExecChannel ech = new ExecChannel(session);
        System.out.println(command);

        String result = ech.execCommand(command, listeners);
        return result;

    }

    public String executeMain(String className, Configuration conf, String[] args) {
        try {
            Class cls = Class.forName(className);
            Class[] types = {Configuration.class, String[].class};
            Method met = cls.getMethod("runJob", types);
            met.invoke(null, conf, args);
            return "";
        }
        catch (ClassNotFoundException e) {
            throw new RuntimeException(e);

        }
        catch (NoSuchMethodException e) {
            throw new RuntimeException(e);

        }
        catch (IllegalAccessException e) {
            throw new RuntimeException(e);

        }
        catch (InvocationTargetException e) {
            Throwable cause = e;
            while (cause.getCause() != null && cause.getCause() != cause)
                cause = cause.getCause();
            throw new RuntimeException(cause);

        }
    }

    @Override
    public void copyFileToHDFS(String src, String hdfsdest) {
        String result = executeCommand(HADOOP_COMMAND + "fs -copyFromLocal " + src + " " + hdfsdest);
        System.out.println(result);

    }


    @Override
    public void copyDirectoryToHDFS(String src, String hdfsdest) {
        String result = executeCommand(HADOOP_COMMAND + "fs -rmr " + hdfsdest);
        System.out.println(result);
        result = executeCommand(HADOOP_COMMAND + "fs -copyFromLocal " + src + " " + hdfsdest);
        System.out.println(result);

    }

    @Override
    public void guaranteeFiles(final File srcDir, String dest) {
        if (!srcDir.isDirectory())
            throw new IllegalArgumentException("File " + srcDir.getAbsolutePath() + " is not directory");
        File[] files = srcDir.listFiles();
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                File file = files[i];
                String fileDest = dest + "/" + file.getName();
                guaranteeFile(file, fileDest);
            }
        }

    }

    @Override
    public boolean guaranteeFile(final File pJar, String dest) {
        if (hasFile(pJar, dest))
            return true;
        uploadFile(pJar, dest);
        return false;
    }

    @Override
    public boolean hasFile(final File pJar, String dest) {
        RemoteSession remoteSession = getSession();
        IFileSystem fs = getHDFSAccessor();
        return fs.exists(dest);
    }

    @Override
    public HDFSFile[] getFiles(String directory) {
        String result = executeCommand(HADOOP_COMMAND + "fs -ls " + directory);
        String[] items = result.split("\n");
        List<HDFSFile> holder = new ArrayList<HDFSFile>();
        for (int i = 1; i < items.length; i++) {
            String item = items[i];
            if (item.length() > MIN__FILE_LENGTH) {
                try {
                    holder.add(new HDFSFile(item));
                }
                catch (Exception e) {
                }
            }
        }
        HDFSFile[] ret = new HDFSFile[holder.size()];
        holder.toArray(ret);
        return ret;

    }

    @Override
    public String getTemporaryDirectory() {
        String defaultPath = RemoteUtilities.getDefaultPath();
        String temp = defaultPath + "/temp";
         IFileSystem fs = getHDFSAccessor();
        fs.guaranteeDirectory(temp);
        return temp;
    }


    @Override
    public boolean isHDFSDirectory(String dst) {
        String result = executeCommand(HADOOP_COMMAND + "fs -stat " + dst);
        if (result.contains("No such file or directory"))
            return false;
        return true;
    }


    @Override
    public void guaranteeHDFSDirectory(String dst) {
        String result = executeCommand(HADOOP_COMMAND + "fs -mkdir " + dst);
    }

    @Override
    public void uploadFileToHDFS(String fname, String dst) {
        File file = new File(fname);
        String dst1 = getTemporaryDirectory();
        uploadFile(fname, dst1);
        guaranteeHDFSDirectory(dst);
        ExecChannel ech = new ExecChannel(getSession());
        String result = executeCommand(HADOOP_COMMAND + "fs -copyFromLocal " + dst1 + "/" + file.getName() + " " + dst);
    }

    @Override
    public void uploadFile(String fname, String dst) {
        uploadFile(new File(fname), dst);
    }


    @Override
    public boolean isSingleCore() {
        return false;
    }

    @Override
    public void uploadFile(InputStream fname, String dst) {
        IFileSystem fs = getHDFSAccessor();
        fs.writeToFileSystem(dst, fname);
    }

    @Override
    public void uploadFile(File fname, String dst) {
        IFileSystem fs = getHDFSAccessor();
        fs.writeToFileSystem(dst, fname);

    }

//    public void guaranteeParentDirectory(final String dst, final FTPChannel pEch) {
//        String m_ParentStream = HDFSUtilities.parentDirectory(dst);
//        guaranteeDirectory(pEch, m_ParentStream);
//    }
//
//    public void guaranteeDirectory(final FTPChannel pEch, final String pParent) {
//        if (pParent != null) {
//            if (m_ExistingDirectories.contains(pParent))
//                return;
//            try {
//                pEch.mkdir(pParent);  // make sure we have the directory
//            }
//            catch (SftpException e) {
//                // fail if the directory is there
//                String message = e.getMessage();
//                if (!message.equals("Failure"))
//                    throw new RuntimeException(e);
//            }
//            m_ExistingDirectories.add(pParent);
//        }
//    }


}
