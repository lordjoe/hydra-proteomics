package org.systemsbiology.hadoop;

import com.lordjoe.utilities.*;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.util.*;
import org.systemsbiology.common.*;
import org.systemsbiology.remotecontrol.*;

import java.io.*;
import java.util.*;

/**
 * org.systemsbiology.hadoop.LocalHadoopController
 * User: steven
 * Date: Jun 14, 2010
 */
public class LocalHadoopController implements IHadoopController {
    public static final LocalHadoopController[] EMPTY_ARRAY = {};

    private String m_DefaultDirectory;

    public LocalHadoopController() {
        m_DefaultDirectory = System.getProperty("user.dir");
        HadoopJob.setJarRequired(false);

    }

    @Override
    public String getDefaultDirectory() {
        return m_DefaultDirectory;
    }


    @Override
    public void copyToHDFS(File localFile, String hdfsdest) {

    }

    @Override
    public void copyDirectoryToHDFS(String src, String hdfsdest) {

    }

    @Override
    public void copyFileToHDFS(String src, String hdfsdest) {

    }

    @Override
    public void setDefaultDirectory(final String pDefaultDirectory) {
        m_DefaultDirectory = pDefaultDirectory;
    }

    @Override
    public boolean runJobs(final IHadoopJob[] jobs) {
        if(jobs == null)
            return true; // nothing to do
        boolean ret = true;
        //noinspection ForLoopReplaceableByForEach
        for (int i = 0; i < jobs.length; i++) {
            IHadoopJob job = jobs[i];
            //noinspection UnnecessaryLocalVariable,UnusedDeclaration,UnusedAssignment,ConstantConditions
            ret &= runJob( job);
            if(!ret)
                return ret;
        }
        return ret;
    }

    @Override
    public boolean runJob(final IHadoopJob job) {
        List<String> holder = new ArrayList<String>();
        Configuration conf = new Configuration();

         holder.add(job.getHDFSFilesDirectory());
        String e = job.getOutputDirectory();
        if (e == null || e.length() == 0)
            throw new IllegalStateException("bad output directory"); // ToDo change

        IConfigureFileSystem fileSystemConfigurer = HadoopUtilities.getFileSystemConfigurer();
        // maybe point at a different file system
        fileSystemConfigurer.configureFileSystem(conf, e);

        IFileSystem fs =  HDFSAccessor.getFileSystem(conf);
        fs.expunge(e);
   //     FileUtilities.expungeDirectory(new File(e));
        holder.add(e);
        String[] others = job.getOtherArgs();
        if (others != null) {
            Collections.addAll(holder, others);
        }

        String[] args = new String[holder.size()];
        holder.toArray(args);

        String mainClass = job.getMainClass();

        String[] allArgs = job.getAllArgs();

        try {
            //noinspection unchecked
            Class<? extends Tool> mClass = (Class<? extends Tool>) Class.forName(mainClass);
            Tool realMain = mClass.newInstance();

            Configured cfg = (Configured)realMain;
            cfg.setConf(conf);
            int result = realMain.run(allArgs);

            if(realMain instanceof IJobRunner)
                  job.setJob(((IJobRunner)realMain).getJob());
            return result == 0;
//            if (result.contains("Job Failed"))
//                throw new IllegalStateException(result);
//            System.out.println(result);
        }
        catch (ClassNotFoundException ex) {
            throw new RuntimeException(ex);

        }
        catch (InstantiationException ex) {
            throw new RuntimeException(ex);

        }
        catch (IllegalAccessException ex) {
            throw new RuntimeException(e);

        }
        catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(e);

        }

        //              if (main.isAssignableFrom(Tool.class)) {
//                  try {
//                     Tool tool = (Tool)main.newInstance();
//                     tool.run(args);
//                  }
//                  catch (Exception e1) {
//                      throw new RuntimeException(e1);
//                   }
//              }
//            else {
//                Method met = main.getMethod("main", String[].class);
//                Object[] objArgs = {args};
//                met.invoke(null, objArgs);
//            }
//            return;
//        }
//        catch (ClassNotFoundException e) {
//            throw new RuntimeException(e);
//
//        }
//        catch (NoSuchMethodException e) {
//            throw new RuntimeException(e);
//
//        }
//        catch (IllegalAccessException e) {
//            throw new RuntimeException(e);
//
//        }
//        catch (InvocationTargetException exception) {
//            Throwable e = exception;
//            while (e.getCause() != null && e.getCause() != e)
//                e = e.getCause();
//            if (e instanceof RuntimeException)
//                throw (RuntimeException) e;
//            throw new RuntimeException(e);
//
//        }

    }

    /**
     * deal with all the details of setting up a local job to use a file system such as s3
     *
     * @param conf
     * @param holder
     */
    @SuppressWarnings("UnusedDeclaration")
    protected void handleFileSystem(Configuration conf, List<String> holder) {

    }


    @Override
    public void guaranteeFilesOnHDFS(final File srcDir, final String dest, final String hdfsdest) {

    }


    @Override
    public void guaranteeFiles(final File srcDir, final String dest) {

    }

    @Override
    public boolean guaranteeFile(final File pJar, final String dest) {
        return true;
    }

    @Override
    public boolean hasFile(final File pJar, final String dest) {
        return false;
    }

    @Override
    public HDFSFile[] getFiles(final String directory) {
        return new HDFSFile[0];
    }

    @Override
    public String getTemporaryDirectory() {
        return null;
    }

    @Override
    public boolean isHDFSDirectory(final String dst) {
        return true;
    }

    @Override
    public void guaranteeHDFSDirectory(final String dst) {

    }

    @Override
    public void uploadFileToHDFS(final String fname, final String dst) {

    }

    @Override
    public void uploadFile(final String fname, final String dst) {

    }

    @Override
    public void uploadFile(final File fname, final String dst) {

    }


    @Override
    public void uploadFile(final InputStream inp, final String dst) {
        FileOutputStream os = null;
        try {
            os = new FileOutputStream(dst);
            FileUtilities.copyStream(inp, os);
        }
        catch (IOException e) {
            if (os != null)
                try {
                    os.close();
                }
                catch (IOException e1) {
                    // forgive
                }

        }
    }

    @Override
    public boolean isSingleCore() {
        return true;
    }

      /*
        private static void runYeastHowdahProcess(final String[] args) {
            Class<PartitioningHowdahTask> mainClass = PartitioningHowdahTask.class;
            String jobName = mainClass.getSimpleName();
            if (args.length > 0)
                jobName = args[0];

            LocalHadoopController launcher = new LocalHadoopController();
            IHadoopJob job = HadoopJob.buildJob(
                    mainClass,
                  //  "D:/YeastLargeData",     // data on hdfs
                      "YeastData",     // data on hdfs
                   //"YeastSmallData",     // data on hdfs
                    "jobs",      // jar location
                    "yeast_output2",             // output location - will have outputN added
                      "-D",
                    "org.systemsbiology.configfile=config/HowdahBreaks.config",     // Config file
                    "-D",
                    "org.systemsbiology.reportfile=yeasthowdahreport.xml"  // report file
            );
            launcher.runJob(job);
        }



        private static void runHumanHowdahProcess(final String[] args) {
            Class<PartitioningHowdahTask> mainClass = PartitioningHowdahTask.class;
            String jobName = mainClass.getSimpleName();
            if (args.length > 0)
                jobName = args[0];

            LocalHadoopController launcher = new LocalHadoopController();
            IHadoopJob job = HadoopJob.buildJob(
                    mainClass,
                  //  "D:/YeastLargeData",     // data on hdfs
                      "I:/NA19239Small",     // data on hdfs
                   //"YeastSmallData",     // data on hdfs
                    "jobs",      // jar location
                    "human_output2",             // output location - will have outputN added
                      "-D",
                    "org.systemsbiology.configfile=config/HowdahBreaks.config",     // Config file
                    "-D",
                    "org.systemsbiology.reportfile=humanhowdahreport.xml"  // report file
            );
            launcher.runJob(job);
        }

    */

       @SuppressWarnings("UnusedDeclaration")
    private static boolean runWholeFileTest(final String[] args) {
        Class<WholeFileTest> mainClass = WholeFileTest.class;
        //noinspection UnnecessaryLocalVariable,UnusedDeclaration,UnusedAssignment
        String jobName = mainClass.getSimpleName();
        if (args.length > 0)
            //noinspection UnnecessaryLocalVariable,UnusedDeclaration,UnusedAssignment
            jobName = args[0];
        LocalHadoopController launcher = new LocalHadoopController();

        IHadoopJob job = HadoopJob.buildJob(
                mainClass,
                "H:/Madhu/motifscanning/TINY_format_mouse_matrices",     // data on hdfs
                "jobs",      // jar location
                "outputMotif"             // output location - will have outputN added

        );
        return launcher.runJob(job);
    }

    @SuppressWarnings("UnusedDeclaration")
    private static boolean runHelloTest(final String[] args) {
        Class<RunExecutableTest> mainClass = RunExecutableTest.class;
        //noinspection UnnecessaryLocalVariable,UnusedDeclaration,UnusedAssignment
        String jobName = mainClass.getSimpleName();
        if (args.length > 0)
            //noinspection UnnecessaryLocalVariable,UnusedDeclaration,UnusedAssignment
            jobName = args[0];
        LocalHadoopController launcher = new LocalHadoopController();

        IHadoopJob job = HadoopJob.buildJob(
                mainClass,
                "H:/Madhu/motifscanning/TINY_format_mouse_matrices",     // data on hdfs
                "jobs",      // jar location
                "outputMotif"             // output location - will have outputN added

        );
        return launcher.runJob(job);
    }



    private static boolean runWordCount(final String[] args) {
        Class<CapitalWordCount> mainClass = CapitalWordCount.class;
        //noinspection UnnecessaryLocalVariable,UnusedDeclaration,UnusedAssignment
        String jobName = mainClass.getSimpleName();
        if (args.length > 0)
            //noinspection UnnecessaryLocalVariable,UnusedDeclaration,UnusedAssignment
            jobName = args[0];
        LocalHadoopController launcher = new LocalHadoopController();

        IHadoopJob job = HadoopJob.buildJob(
                mainClass,
                "E:/moby",  // data on hdfs
                "jobs",      // jar location
                "wordcount"    // output location - will have outputN added

        );
        return launcher.runJob(job);
    }

    /**
     * shut down all running sessions
     */
    @Override
    public void disconnect() {
        // do nothing

    }

    public static void main(String[] args) {
        // No need to make s jar for local processes
        HadoopJob.setJarRequired(false);
        // runWholeFileTest(args);
        // runHelloTest(args);
        //  runHowdahTest(args);
        runWordCount(args);
        //  runStatisticalWordCount(args);
        //  runYeastHowdahProcess(args);
        // runHumanHowdahProcess(args);
        //     runYeastProcess(args);
        // runSimProcess(args);
        // runHumanProcess(args);

    }

}
