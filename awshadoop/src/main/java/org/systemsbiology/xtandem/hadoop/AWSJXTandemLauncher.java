package org.systemsbiology.xtandem.hadoop;

import com.amazonaws.services.ec2.*;
import com.lordjoe.utilities.*;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.fs.LocalFileSystem;
import org.systemsbiology.aws.*;
import org.systemsbiology.awscluster.*;
import org.systemsbiology.common.*;
import org.systemsbiology.hadoop.*;
import org.systemsbiology.remotecontrol.*;
import org.systemsbiology.xml.XMLUtilities;
import org.systemsbiology.xtandem.*;
import org.systemsbiology.xtandem.taxonomy.*;

import java.io.*;
import java.util.*;

/**
 * org.systemsbiology.xtandem.hadoop.AWSJXTandemLauncher
 * User: steven
 * Date: 12/8/11
 */
public class AWSJXTandemLauncher extends JXTandemLauncher {
    public static final AWSJXTandemLauncher[] EMPTY_ARRAY = {};

    private static String gBucketName = AWSUtilities.getDefaultBucketName();


    private static boolean gLocalForced = false;

    public static boolean isLocalForced() {
        return gLocalForced;
    }

    public static void setLocalForced(final boolean pLocalForced) {
        gLocalForced = pLocalForced;
    }

    public static String getBucketName() {
        return gBucketName;
    }

    public static void setBucketName(final String pBucketName) {
        gBucketName = pBucketName;
    }

    private String m_HostPrefix = "";

    public AWSJXTandemLauncher(final InputStream is, final String url, final Configuration cfg) {
        super(is, url, cfg);
        String remoteBaseDirectory = getRemoteBaseDirectory();
        if(!remoteBaseDirectory.startsWith("s3n://"))
            throw new IllegalStateException("Amazon jon=bs must have a remotebase directory starting s3n://"); // ToDo change
    }

    /**
     * parse the initial file and get run parameters
     *
     * @param is
     */
    @Override
    public void handleInputs(final InputStream is, final String url, final Configuration cfg) {
        super.handleInputs(is, url, cfg);
        HadoopTandemMain application = getApplication();
        AWSClusterUtilities.configure(application);

    }

    @Override
    public void guaranteeRemoteFiles() {
        //   String rbase = getRemoteBaseDirectory();
        //   if(rbase.startsWith("s3n:/"))
        //        return; // assume we already have files on S3
        super.guaranteeRemoteFiles();
    }

    @Override
    protected String getHostPrefix() {
        return m_HostPrefix;
    }

    public void setHostPrefix(final String pHostPrefix) {
        m_HostPrefix = pHostPrefix;
    }

    @Override
    public void runJobs(IHadoopController launcherX) {
        if (!(launcherX instanceof AWSMapReduceLauncher)) {
            super.runJobs(launcherX);
            return;
        }

        AWSMapReduceLauncher launcher = (AWSMapReduceLauncher) launcherX;
        launcher.setNumberInstances(AWSClusterUtilities.getClusterSize());
        XTandemUtilities.outputLine("Starting Job");
        guaranteeRemoteFiles();

        ElapsedTimer elapsed = getElapsed();

        JXTandemStatistics statistics = getStatistics();
        statistics.startJob("Total Scoring");
        // remember the input file foe a final report
        HadoopTandemMain application = getApplication();
        String parameter = application.getParameter("spectrum, path");
        if (parameter != null)
            statistics.setData("Input file", parameter);
        ITaxonomy taxonomy = application.getTaxonomy();
        String organism = taxonomy.getOrganism();
        if (organism != null)
            statistics.setData("Taxonomy database", organism);

        boolean buildDatabase = isDatabaseBuildRequired();
        IHadoopJob job = null;
        boolean ret;
        //  XTandemUtilities.outputLine("Temporarily reusing data directory");
        List<IHadoopJob> holder = new ArrayList<IHadoopJob>();
        if (buildDatabase) {

            statistics.startJob("Build Database");
            job = buildJobSequenceFinder();
            holder.add(job);
            job = buildJobMassFinder();
            holder.add(job);

            IHadoopJob[] jobs = new IHadoopJob[holder.size()];
            holder.toArray(jobs);
            holder.clear();
            ret = launcher.runJobsInCluster(jobs);
            if (!ret)
                throw new IllegalStateException("Build Database failed");

            saveDatabaseSizes(jobs);
            if (isDatabaseBuildOnly()) {
                if ("true".equalsIgnoreCase(HadoopUtilities.getProperty(DELETE_OUTPUT_DIRECTORIES_PROPERTY)))
                    deleteRemoteIntermediateDirectories();
                return;

            }
            // return; // for now lets just get this part right
        }


        statistics.startJob("Scorer");
        job = buildJobPass1();
        holder.add(job);

//        ret = launcher.runJob(job);
//        if (!ret)
//            throw new IllegalStateException("Scorer job failed");
//
//        statistics.endJob("Scorer");
//        handleCounters(job);

        statistics.startJob("Score Combiner");
//        elapsed.showElapsed("Finished Pass1");
//        elapsed.reset();

        String jarString = getPassedJarFile();
        if (jarString == null)
            jarString = job.getJarFile();

        job = buildJobPass2();
        job.setJarFile(jarString);
        holder.add(job);

//        ret = launcher.runJob(job);
//        if (!ret)
//            throw new IllegalStateException("Score Combiner job failed");
//        statistics.endJob("Score Combiner");
//
//        handleCounters(job);


//        elapsed.showElapsed("Finished Pass2");
//        elapsed.reset();

        statistics.startJob("Consolidator");
        job = buildRemoteConsolidatorJob();
        job.setJarFile(jarString);
        holder.add(job);

//        ret = launcher.runJob(job);
//        if (!ret)
//            throw new IllegalStateException("Consolidator job failed");
//        statistics.endJob("Consolidator");
//
//        handleCounters(job);
        IHadoopJob[] jobs = new IHadoopJob[holder.size()];
        holder.toArray(jobs);
        holder.clear();
        ret = launcher.runJobsInCluster(jobs);
        if (!ret)
            throw new IllegalStateException("Build Database failed");

        if ("true".equalsIgnoreCase(HadoopUtilities.getProperty(DELETE_OUTPUT_DIRECTORIES_PROPERTY)))
            deleteRemoteIntermediateDirectories();

        elapsed.showElapsed("Finished Consolidation");
        statistics.endJob("Total Scoring");

        // runJob(hc);
    }

    // Call with
    // params=tandem.params remoteHost=Glados remoteBaseDirectory=/user/howdah/JXTandem/data/largeSample

    public static void main(String[] args) {
        if (args.length == 0) {
            usage();
            return;
        }
        if ("params=".equals(args[1])) {
            usage2();
            return;
        }

        ElapsedTimer total = new ElapsedTimer();


        try {
            handleArguments(args);
            String passedBaseDirctory = getPassedBaseDirctory(); //.replace("s3n:/","");
            boolean isRemote = !isTaskLocal();
            Configuration cfg = new Configuration();
            if (isRemote) {
                IConfigureFileSystem awsConfigureFileSystem = AWSUtilities.AWS_CONFIGURE_FILE_SYSTEM;
                HadoopUtilities.setFileSystemConfigurer(awsConfigureFileSystem);
                String passedBaseDirctory1 = getPassedBaseDirctory();
                XTandemHadoopUtilities.setDefaultPath(passedBaseDirctory1);
                awsConfigureFileSystem.configureFileSystem(cfg, passedBaseDirctory1);
//                AWSCredentials credentials = AWSUtilities.getCredentials();
//                 cfg.set("fs.default.name",AWSUtilities.getHadoopFileSystemURL(getBucketName() ));
//                 cfg.set("fs.s3n.awsAccessKeyId",credentials.getAWSAccessKeyId());
//                 cfg.set("fs.s3n.awsSecretAccessKey", credentials.getAWSSecretKey());
            }
            else {
                cfg.set("fs.default.name", "file:///");    // use local

            }

            String paramsFile = getParamsFile();
            InputStream is = buildInputStream(paramsFile);
            if (is == null) {
                File test = new File(paramsFile);
                XTandemUtilities.outputLine("CANNOT RUN BECAUSE CANNOT READ PARAMS FILE " + test.getAbsolutePath());
                return;
            }
            AWSJXTandemLauncher main = new AWSJXTandemLauncher(is, paramsFile, cfg);
            main.setHostPrefix(passedBaseDirctory);
            HadoopJob.setDefaultJarName(HadoopUtilities.getReuseJar());

            if (getPassedJarFile() != null) {   // start with a jar file
                main.setJarFile(getPassedJarFile());
                main.setBuildJar(false);
            }
            // lazy for development
            if (HadoopUtilities.getReuseJar() != null) {   // start with a jar file
                  main.setJarFile("jobs/" + HadoopUtilities.getReuseJar());
                  main.setBuildJar(false);
              }

            ElapsedTimer elapsed = main.getElapsed();
            IHadoopController launcher = null;

            if (isRemote) {

                // make sure directory exists
                IFileSystem access = new AmazonS3Accessor(getBucketName());
                main.setAccessor(access);
                if (!isLocalForced()) {
                    launcher = new AWSMapReduceLauncher("JXTandem");
                }
                else {
                    HadoopJob.setJarRequired(false);
                    launcher = new LocalHadoopController();

                }

            }
            else {
                IFileSystem access = new LocalMachineFileSystem(new File(main.getRemoteBaseDirectory()));
                main.setAccessor(access);
                main.setBuildJar(false);
                HadoopJob.setJarRequired(false);
                  HadoopJob.setJarRequired(false);
                launcher = new LocalHadoopController();

            }
            IFileSystem accessor = main.getAccessor();
            accessor.guaranteeDirectory(passedBaseDirctory);

            main.setParamsPath(paramsFile);


            main.loadTaxonomy();
            main.setPassNumber(1);
            String outFile = main.getOutputFileName();


            String defaultBasePath = passedBaseDirctory; //RemoteUtilities.getDefaultPath() + "/JXTandem/JXTandemOutput";
            accessor.guaranteeDirectory(defaultBasePath);
            main.setOutputLocationBase(defaultBasePath + "/OutputData");
            if (isRemote) {
                // make sure directory exists
                // we will kill output directories to guarantee empty
                HDFSUtilities.setOutputDirectoriesPrecleared(true);
                //   main.expungeRemoteDirectories();
                elapsed.showElapsed("Finished Setup");
            }
            else {
                main.expungeLocalDirectories();

            }
            main.runJobs(launcher);

            //   XTandemDebugging.loadXTandemValues("log1.txt");

            File f = null;
            if (!isDatabaseBuildOnly() && passedBaseDirctory != null) {
                String hdfsPath = passedBaseDirctory + "/" + XMLUtilities.asLocalFile(outFile);
                //       String asLocal = XTandemUtilities.asLocalFile("/user/howdah/JXTandem/data/SmallSample/yeast_orfs_all_REV01_short.2011_11_325_10_35_19.t.xml");
                //       String hdfsPathEric = passedBaseDirctory + "/" + "yeast_orfs_all_REV01_short.2011_11_325_10_35_19.t.xml";


                try {
                    //          f = main.readRemoteFile(hdfsPathEric, outFile);
                    f = main.readRemoteFile(hdfsPath, outFile);
                    if (f != null && f.length() < MAX_DISPLAY_LENGTH) {
                        String s = FileUtilities.readInFile(f);
                        XTandemUtilities.outputLine(s);
                    }
                    // read in the larger scans file
                    outFile += ".scans";
                    f = main.readRemoteFile(hdfsPath, outFile);
                    if(f != null )
                        XTandemUtilities.outputLine("Created output file " + f.getAbsolutePath());
                }
                catch (IllegalArgumentException e) {
                    XTandemUtilities.outputLine("Cannot copy remote file " + hdfsPath + " to local file " + outFile +
                            " because " + e.getMessage());
                    e.printStackTrace();
                    throw e;

                }
            }


            XTandemUtilities.outputLine("Fragment Database Size " + main.getTotalFragments());

            main.getElapsed().showElapsed("Capture Output", System.out);

            XTandemUtilities.outputLine("");
            XTandemUtilities.outputLine("");
            JXTandemStatistics stats = main.getStatistics();
            XTandemUtilities.outputLine(stats.toString());
            total.showElapsed("Total Time", System.out);

            // I think hadoop has launched some threads so we can shut down now
        }
        catch (Throwable e) {
            e.printStackTrace();
            if (e != e.getCause() && e.getCause() != null) {
                while (e != e.getCause() && e.getCause() != null) {
                    e = e.getCause();
                }
                XTandemUtilities.outputLine(e.getMessage());
                StackTraceElement[] stackTrace = e.getStackTrace();
                for (int i = 0; i < stackTrace.length; i++) {
                    StackTraceElement se = stackTrace[i];
                    XTandemUtilities.outputLine(se.toString());
                }
            }
        }
        finally {
            System.exit(0);
        }
    }


}
