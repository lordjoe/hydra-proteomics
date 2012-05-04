package org.systemsbiology.aws;

import com.amazonaws.*;
import com.amazonaws.auth.*;
import com.amazonaws.services.elasticmapreduce.*;
import com.amazonaws.services.elasticmapreduce.model.*;
import org.systemsbiology.awscluster.*;
import org.systemsbiology.hadoop.*;
import org.systemsbiology.hadoopgenerated.*;
import org.systemsbiology.remotecontrol.*;
import org.systemsbiology.xtandem.*;

import java.io.*;
import java.util.*;

/**
 * org.systemsbiology.aws.AWSMapReduceLauncher User: Steven Date: May 27, 2010
 */
public class AWSMapReduceLauncher implements IHadoopController {
    public static final AWSMapReduceLauncher[] EMPTY_ARRAY = {};

    public static final int ONE_HOUR = 60 * 60 * 1000;
    public static final long MAX_RUN_TIME = 24 * ONE_HOUR; // 24 hours

    public static final String DEFAULT_HADOOP_VERSION = "0.20";


    private static final int DEFAULT_NUMBER_INSTANCES = 3;

    private final String m_BaseJobName;
    private AWSInstanceSize m_SlaveSize = AWSClusterUtilities.getMasterSize();
    private AWSInstanceSize m_MasterSize = AWSClusterUtilities.getSlaveSize(); // AWSInstanceSize.Large;
    private String m_DefaultDirectory = AWSUtilities.getDefaultBucketName(); // really a bucket
    private int m_NumberInstances;
    private long m_MaxRunRime = MAX_RUN_TIME;
    private List<StepConfig> m_Steps = new ArrayList<StepConfig>();
    private RunJobFlowRequest m_JobFlowRequest;
    private JobFlowInstancesConfig m_FlowInstancesConfig;
    private String m_JobId;


    private final AWSCredentials m_Credentials;
    private final ClientConfiguration m_Config;
    private final AmazonElasticMapReduce m_Service;

    public AWSMapReduceLauncher() {
        this("AWSJob", AWSUtilities.getCredentials());
    }


    public AWSMapReduceLauncher(String baseJob) {
        this(baseJob, AWSUtilities.getCredentials());
    }

    public AWSMapReduceLauncher(String baseJob, AWSCredentials creds) {
        m_BaseJobName = baseJob;
        m_Credentials = creds;
        m_Config = new ClientConfiguration();
        AmazonElasticMapReduceClient svc;
        try {
            svc = new AmazonElasticMapReduceClient(getCredentials(), m_Config);
        }
        catch (AmazonClientException e) {
            throw new RuntimeException(e);
        }

        m_Service = svc;
        AWSUtilities.setClient(svc);
    }

// https://forums.aws.amazon.com/message.jspa?messageID=260426#260426
//    AddInstanceGroupsRequest request = new AddInstanceGroupsRequest();
//request.setJobFlowId( jobFlowId );
//
//InstanceGroupConfig conf = new InstanceGroupConfig();
//conf.setInstanceCount( 1 );
//conf.setName( UUID.randomUUID().toString() );
//conf.setInstanceRole( "TASK" );
//conf.setInstanceType( "c1.medium" );
//
//request.setInstanceGroups( Arrays.asList( conf ) );
//
//AmazonElasticMapReduce service = new AmazonElasticMapReduceClient(
//AmazonSettings.getCredentials());
//
//service.addInstanceGroups( request )
//
// https://forums.aws.amazon.com/thread.jspa?messageID=300778&#300778
//    new InstanceGroupConfig()
//    .withMarket("SPOT")
//    .withBidPrice("0.25")
//    .withInstanceRole("CORE")
//    .withInstanceType(type)
//    .withInstanceCount(count);

    public long getMaxRunRime() {
        return m_MaxRunRime;
    }

    public void setMaxRunRime(final long pMaxRunRime) {
        m_MaxRunRime = pMaxRunRime;
    }

    public int getNumberInstances() {
        return m_NumberInstances;
    }

    public void setNumberInstances(final int pNumberInstances) {
        m_NumberInstances = pNumberInstances;
    }

    @Override
    public String getDefaultDirectory() {
        return m_DefaultDirectory;
    }

    @Override
    public void setDefaultDirectory(final String pDefaultDirectory) {
        m_DefaultDirectory = pDefaultDirectory;
    }

    public AWSInstanceSize getMasterSize() {
        return m_MasterSize;
    }

    public void setMasterSize(final AWSInstanceSize pMasterSize) {
        m_MasterSize = pMasterSize;
    }

    @Override
    public void uploadFile(final InputStream fname, final String dst) {
        throw new UnsupportedOperationException("Fix This"); // ToDo
    }

    public void runJobInCluster(final IHadoopJob job) {

        job.makeJarAsNeeded();
        long maxTime = getMaxRunRime();
        long start = System.currentTimeMillis();
        long entTime = maxTime + start;
        final String jarFile = job.getJarFile();
        File jar = new File(jarFile);
        if (!jar.exists())
            throw new IllegalStateException("Jar file " + jar
                    + " does not exist");
        // guaranteeFiles(inputs, job.getFilesDirectory());

        final String dir = job.getJobDirectory();
        guaranteeFile(jar, dir + "/" + jar.getName());

        // use server
        //   String jobId = ClusterServerClient.getMinimalCluster();


        final AmazonElasticMapReduce service = getService();

        AWSClusterUtilities.setDefaultDirectory(getDefaultDirectory());

        setNumberInstances(job.getNumberInstances());

        JobFlowInstancesConfig flowInstancesConfig = getFlowInstancesConfig();
        flowInstancesConfig.setKeepJobFlowAliveWhenNoSteps(Boolean.TRUE);
        final JobFlowInstancesDetail cluster = AWSClusterUtilities.getClusterRequestFromConfig(flowInstancesConfig);
        //       final JobFlowInstancesDetail cluster = AWSClusterUtilities.getMinimalClusterRequest();
        RunJobFlowRequest request = getJobFlowRequest(cluster);
        final JobFlowDetail jf = AWSClusterUtilities.guaranteeJob(service, cluster, request);
        m_JobId = jf.getJobFlowId();
        int step_number = jf.getSteps().size();

        AddJobFlowStepsRequest req = addJobStep(job, m_JobId);

        service.addJobFlowSteps(req);
        JobMonitor monitor = new JobMonitor(m_JobId, job, getDefaultDirectory(),
                getService());

        final PrintingMonitor listener = new PrintingMonitor();
        monitor.addJobStatusChangeListener(listener);

        AWSUtilities.waitForStates(monitor, "WAITING", "RUNNING");

        AWSUtilities.waitForStates(monitor, "RUNNING");

        AWSUtilities.waitForStates(monitor, "WAITING");

        JobFlowDetail detail = monitor.getJobFlowDetails();
        monitor.readLogs(1);


        System.err.println(monitor.getStderr());
        System.err.println((monitor.getStdout()));
        String sysLog = monitor.getSysLog();
        AWSUtilities.parseCounters(sysLog, job.getAllCounterValues());
        System.err.println(sysLog);
        System.err.println(monitor.getController());


        System.out.println("DONE!!!");

    }

    public boolean runJobsInCluster(final IHadoopJob[] jobs) {
        if (jobs == null || jobs.length == 0)
            return true;
        IHadoopJob job1 = jobs[0];
        job1.makeJarAsNeeded();
        long maxTime = getMaxRunRime();
        long start = System.currentTimeMillis();
        long entTime = maxTime + start;
        final String jarFile = job1.getJarFile();
        File jar = new File(jarFile);
        if (!jar.exists())
            throw new IllegalStateException("Jar file " + jar
                    + " does not exist");
        // guaranteeFiles(inputs, job.getFilesDirectory());
        final String dir = job1.getJobDirectory();
        String name = jar.getName();
        if (!name.startsWith(dir + "/")) {
            name = dir + "/" + name;
        }
        guaranteeFile(jar, name);

        final AmazonElasticMapReduce service = getService();
        AWSClusterUtilities.setDefaultDirectory(getDefaultDirectory());
        final JobFlowInstancesDetail cluster = AWSClusterUtilities.getMinimalClusterRequest();
        int clusterSize = getNumberInstances();
        cluster.setInstanceCount(clusterSize);

        boolean keepAfterJob = AWSClusterUtilities.isKeepJobAlive();
        cluster.setKeepJobFlowAliveWhenNoSteps(keepAfterJob);
        RunJobFlowRequest request = getJobFlowRequest(cluster);
        final JobFlowDetail jf = AWSClusterUtilities.guaranteeJob(service, cluster, request);
        m_JobId = jf.getJobFlowId();

        AddJobFlowStepsRequest req = addJobSteps(jobs, m_JobId);


        service.addJobFlowSteps(req);
        JobMonitor monitor = new JobMonitor(m_JobId, job1, getDefaultDirectory(),
                getService());

        final PrintingMonitor listener = new PrintingMonitor();
        monitor.addJobStatusChangeListener(listener);

        AWSUtilities.waitForStates(monitor, "WAITING", "RUNNING");

        AWSUtilities.waitForStates(monitor, "RUNNING");

        AWSUtilities.waitForStates(monitor, "WAITING");

        for (int i = 0; i < jobs.length; i++) {
            IHadoopJob hj = jobs[i];
            monitor.setJobObject(hj);
            monitor.clearLogs();
            monitor.readLogs(jobs.length);
            System.err.println(monitor.getStderr());
            System.err.println((monitor.getStdout()));
            String sysLog = monitor.getSysLog();
            AWSUtilities.parseCounters(sysLog, hj.getAllCounterValues());
            System.err.println(sysLog);
            System.err.println(monitor.getController());

        }


        System.out.println("DONE!!!");
        return true;
    }


    @Override
    public boolean runJob(final IHadoopJob job) {
        job.makeJarAsNeeded();
        String jarFile = job.getJarFile();
        File jar = new File(jarFile);
        if (!jar.exists())
            throw new IllegalStateException("Jar file " + jar
                    + " does not exist");
        // guaranteeFiles(inputs, job.getFilesDirectory());

        String dest = job.getJobDirectory() + "/" + jar.getName();
        guaranteeFile(jar, dest);

        addJobToFlow(job);
        runJobFlow(job);
//        String name = request.getName();
//        JobMonitor monitor = invokeRunJobFlow(request);
//        final PrintingMonitor listener = new PrintingMonitor();
//        monitor.addJobStatusChangeListener(listener);
//
//        final AmazonElasticMapReduce service = getService();
//        String state = monitor.showJobStatus();
//        while (!("COMPLETED".equals(state)) && !state.startsWith("FAILED")
//                && !state.startsWith("Shut down")
//                && !state.startsWith("Terminated")) {
//            AWSUtilities.waitFor(JobMonitor.MINIMUM_QUERY_TIME);
//            state = monitor.showJobStatus();
//            if (System.currentTimeMillis() > entTime) {
//                monitor.killJob();
//            }
//
//        }
//        monitor.readLogs();
//
//        System.err.println(monitor.getStderr());
//        System.err.println((monitor.getStdout()));
//        System.err.println(monitor.getSysLog());
//        System.err.println(monitor.getController());
//
//        System.out.println("DONE!!!");
        return true;
    }

    @Override
    public boolean runJobs(final IHadoopJob[] jobs) {
        if (jobs == null || jobs.length == 0)
            return true;
        jobs[0].makeJarAsNeeded();
        String jarFile = jobs[0].getJarFile();
        File jar = new File(jarFile);
        if (!jar.exists())
            throw new IllegalStateException("Jar file " + jar
                    + " does not exist");
        // guaranteeFiles(inputs, job.getFilesDirectory());
        String dest = jobs[0].getJobDirectory() + "/" + jar.getName();
        guaranteeFile(jar, dest);

        for (int i = 0; i < jobs.length; i++) {
            IHadoopJob job = jobs[i];
            addJobToFlow(job);
        }

        runJobsFlow(jobs);
//        String name = request.getName();
//        JobMonitor monitor = invokeRunJobFlow(request);
//        final PrintingMonitor listener = new PrintingMonitor();
//        monitor.addJobStatusChangeListener(listener);
//
//        final AmazonElasticMapReduce service = getService();
//        String state = monitor.showJobStatus();
//        while (!("COMPLETED".equals(state)) && !state.startsWith("FAILED")
//                && !state.startsWith("Shut down")
//                && !state.startsWith("Terminated")) {
//            AWSUtilities.waitFor(JobMonitor.MINIMUM_QUERY_TIME);
//            state = monitor.showJobStatus();
//            if (System.currentTimeMillis() > entTime) {
//                monitor.killJob();
//            }
//
//        }
//        monitor.readLogs();
//
//        System.err.println(monitor.getStderr());
//        System.err.println((monitor.getStdout()));
//        System.err.println(monitor.getSysLog());
//        System.err.println(monitor.getController());
//
//        System.out.println("DONE!!!");
        return true;
    }

    public boolean runJobFlow(final IHadoopJob job) {
        long maxTime = getMaxRunRime();
        long start = System.currentTimeMillis();
        long entTime = maxTime + start;
        RunJobFlowRequest request = getJobFlowRequest(null);
        String name = request.getName();
        JobMonitor monitor = invokeRunJobFlow(request);
        final PrintingMonitor listener = new PrintingMonitor();
        monitor.addJobStatusChangeListener(listener);

        final AmazonElasticMapReduce service = getService();
        String state = monitor.showJobStatus();
        while (!("COMPLETED".equals(state)) && !state.startsWith("FAILED")
                && !state.startsWith("Shut down")
                && !state.startsWith("Terminated")) {
            AWSUtilities.waitFor(JobMonitor.MINIMUM_QUERY_TIME);
            state = monitor.showJobStatus();
            if (System.currentTimeMillis() > entTime) {
                monitor.killJob();
            }

        }
        monitor.readLogs(1);

        System.err.println(monitor.getStderr());
        System.err.println((monitor.getStdout()));
        String sysLog = monitor.getSysLog();
        AWSUtilities.parseCounters(sysLog, job.getAllCounterValues());
        System.err.println(sysLog);
        System.err.println(monitor.getController());

        System.out.println("DONE!!!");
        return true;

    }

    public boolean runJobsFlow(final IHadoopJob[] jobs) {
        long maxTime = getMaxRunRime();
        long start = System.currentTimeMillis();
        long entTime = maxTime + start;
        RunJobFlowRequest request = getJobFlowRequest(null);
        String name = request.getName();
        JobMonitor monitor = invokeRunJobFlow(request);
        final PrintingMonitor listener = new PrintingMonitor();
        monitor.addJobStatusChangeListener(listener);

        final AmazonElasticMapReduce service = getService();
        String state = monitor.showJobStatus();
        while (!("COMPLETED".equals(state)) && !state.startsWith("FAILED")
                && !state.startsWith("Shut down")
                && !state.startsWith("Terminated")) {
            AWSUtilities.waitFor(JobMonitor.MINIMUM_QUERY_TIME);
            state = monitor.showJobStatus();
            if (System.currentTimeMillis() > entTime) {
                monitor.killJob();
            }

        }
        ElapsedTimer elapsed = new ElapsedTimer();
        for (int i = 0; i < jobs.length; i++) {
            IHadoopJob job = jobs[i];
            monitor.clearLogs();
            monitor.setJobObject(job);
            monitor.readLogs(jobs.length);
            System.err.println(monitor.getStderr());
            System.err.println((monitor.getStdout()));
            String sysLog = monitor.getSysLog();
            System.err.println(sysLog);
            System.err.println(monitor.getController());
            AWSUtilities.parseCounters(sysLog, job.getAllCounterValues());

        }
        elapsed.showElapsed("Read all Logs ");


        System.out.println("DONE!!!");
        return true;

    }


    public String[] getJobFlowIds() {
        final AmazonElasticMapReduce service = getService();

        List<String> holder = new ArrayList<String>();

        DescribeJobFlowsRequest reg = new DescribeJobFlowsRequest();

        DescribeJobFlowsResult jfr = service.describeJobFlows(reg);
        List<JobFlowDetail> details = jfr.getJobFlows();
        for (JobFlowDetail detail : details) {
            String id = detail.getJobFlowId();
            JobFlowExecutionStatusDetail status = detail.getExecutionStatusDetail();
            String state = status.getState();
            Date end = status.getEndDateTime();
            holder.add(id);

        }

        String[] ret = new String[holder.size()];
        holder.toArray(ret);
        return ret;

    }


    @Override
    public void guaranteeFilesOnHDFS(final File srcDir, final String dest,
                                     final String hdfsdest) {
        throw new UnsupportedOperationException("Fix This"); // ToDo

    }

    @Override
    public void copyToHDFS(File localFile, String hdfsdest) {
        if (true) throw new UnsupportedOperationException("Fix This");

    }

    @Override
    public void copyDirectoryToHDFS(String src, String hdfsdest) {
        if (true) throw new UnsupportedOperationException("Fix This");

    }

    @Override
    public void copyFileToHDFS(String src, String hdfsdest) {
        if (true) throw new UnsupportedOperationException("Fix This");

    }

    @Override
    public void guaranteeFiles(final File srcDir, String dest) {
        if (!srcDir.isDirectory())
            throw new IllegalArgumentException("File "
                    + srcDir.getAbsolutePath() + " is not directory");
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
        long size = pJar.length();
        long start = System.currentTimeMillis();
        uploadFile(pJar, dest);
        long end = System.currentTimeMillis();
        System.out.println("Uploaded " + size / 1000 + "kb in" + (end - start) / 1000 + " sec");
        return false;

    }

    @Override
    public boolean hasFile(final File pJar, final String dest) {
        String defaultDirectory = getDefaultDirectory();
        return AWSUtilities.hasFile(pJar, defaultDirectory, dest);
    }

    @Override
    public HDFSFile[] getFiles(final String directory) {
        String[] strings = AWSUtilities.listFiles(directory);
        List<HDFSFile> holder = new ArrayList<HDFSFile>();

        for (int i = 0; i < strings.length; i++) {
            holder.add(new HDFSFile(strings[i]));

        }
        HDFSFile[] ret = new HDFSFile[holder.size()];
        holder.toArray(ret);
        return ret;
    }

    @Override
    public String getTemporaryDirectory() {
        throw new UnsupportedOperationException("Fix This"); // ToDo
    }

    @Override
    public boolean isHDFSDirectory(final String dst) {
        throw new UnsupportedOperationException("Fix This"); // ToDo
    }

    @Override
    public void guaranteeHDFSDirectory(final String dst) {
        if (isHDFSDirectory(dst))
            return;
        throw new UnsupportedOperationException("Fix This"); // ToDo

    }

    @Override
    public void uploadFileToHDFS(final String fname, final String dst) {
        uploadFile(fname, dst); // on amazon this is a freeby from s3

    }

    @Override
    public void uploadFile(final String fname, final String dst) {
        uploadFile(new File(fname), dst);

    }

    @Override
    public void uploadFile(final File fname, final String dst) {
        AWSUtilities.uploadFile(getDefaultDirectory(),
                AWSUtilities.getItemDirectory(dst), fname);

    }


    @Override
    public boolean isSingleCore() {
        return false;
    }


    public AWSInstanceSize getSlaveSize() {
        return m_SlaveSize;
    }

    public void setSlaveSize(final AWSInstanceSize pSlaveSize) {
        m_SlaveSize = pSlaveSize;
    }

    public AWSCredentials getCredentials() {
        return m_Credentials;
    }

    public String getBaseJobName() {
        return m_BaseJobName;
    }

    public ClientConfiguration getConfig() {
        return m_Config;
    }

    public AmazonElasticMapReduce getService() {
        return m_Service;
    }


    protected void addJobToFlow(IHadoopJob job) {

        RunJobFlowRequest request = getJobFlowRequest(null);
        List<StepConfig> steps = request.getSteps();

        StepConfig step = buildJobStep(job);
        m_Steps.add(step);
        steps.add(step);
    }

    protected StepConfig buildJobStep(IHadoopJob job) {
        StepConfig stepConfig = new StepConfig();
        stepConfig.setActionOnFailure("TERMINATE_JOB_FLOW");
        String dir = job.getOutputDirectory();
        HadoopJarStepConfig jarsetup = buildJarSetup(dir, job);
        stepConfig.setHadoopJarStep(jarsetup);
        stepConfig.setName(dir);
        return stepConfig;

    }

    /**
     * build a request to run a number of jobs in succession
     *
     * @param job   job
     * @param jobId !null jobid
     * @return !null request
     */
    protected AddJobFlowStepsRequest addJobStep(IHadoopJob job, String jobId) {
        AddJobFlowStepsRequest request = new AddJobFlowStepsRequest();
        List steps = new LinkedList();

        request.setJobFlowId(jobId);
        String stepname = AWSClusterUtilities.buildJobName(job);
        StepConfig stepConfig = new StepConfig();
        stepConfig.setActionOnFailure("CANCEL_AND_WAIT");

        HadoopJarStepConfig jarsetup = buildJarSetup(stepname, job);
        stepConfig.setHadoopJarStep(jarsetup);

        stepConfig.setName(stepname);
        steps.add(stepConfig);

        request.setSteps(steps);

        return request;
    }

    /**
     * build a request to run a number of jobs in succession
     *
     * @param job   !null array of jobs
     * @param jobId !null jobid
     * @return !null request
     */
    protected AddJobFlowStepsRequest addJobSteps(IHadoopJob[] job, String jobId) {
        AddJobFlowStepsRequest request = new AddJobFlowStepsRequest();
        List steps = new LinkedList();

        request.setJobFlowId(jobId);
        for (int i = 0; i < job.length; i++) {
            IHadoopJob hj = job[i];
            String stepname = AWSClusterUtilities.buildJobName(hj);
            StepConfig stepConfig = new StepConfig();
            stepConfig.setActionOnFailure("CANCEL_AND_WAIT");

            String outputDirectory = hj.getOutputDirectory();
            HadoopJarStepConfig jarsetup = buildJarSetup(outputDirectory, hj);
            stepConfig.setHadoopJarStep(jarsetup);

            stepConfig.setName(outputDirectory);
            steps.add(stepConfig);

        }

        request.setSteps(steps);

        return request;
    }


    protected JobMonitor invokeRunJobFlow(RunJobFlowRequest request) {
        try {
            final AmazonElasticMapReduce service = getService();
            final RunJobFlowResult result = service.runJobFlow(request);
            String id = result.getJobFlowId();
            System.err.println("Starting job with id " + id);
            JobMonitor ret = new JobMonitor(id, null, getDefaultDirectory(), getService());
            return ret;
        }
        catch (AmazonClientException e) {
            throw new RuntimeException(e);
        }

    }

    protected JobMonitor addRunJobFlow(AddJobFlowStepsRequest request) {
        try {
            final AmazonElasticMapReduce service = getService();
            service.addJobFlowSteps(request);
            String id = request.getJobFlowId();
            System.err.println("Starting job with id " + id);
            JobMonitor ret = new JobMonitor(id, null, getDefaultDirectory(),
                    getService());
            return ret;
        }
        catch (AmazonClientException e) {
            throw new RuntimeException(e);
        }

    }

    protected HadoopJarStepConfig buildJarSetup(final String pStepname,
                                                IHadoopJob job) {
        HadoopJarStepConfig jarsetup = new HadoopJarStepConfig();

        List<String> arguments = new ArrayList<String>();
        String defaultBucket = getBucketUrl();
        String files = job.getHDFSFilesDirectory();
        arguments.add(files);
        arguments.add("-D");
        arguments.add(HadoopUtilities.STEP_NAME_KEY + "=" + pStepname);
        arguments.add("-D");
        arguments.add(HadoopUtilities.UUID_KEY + "=" + job.getUID().toString());

        final String outputDir = pStepname + "/";
        arguments.add(outputDir);
        String[] strings = job.getOtherArgs();
        if (strings != null) {
            for (int i = 0; i < strings.length; i++) {
                String s = strings[i];
                if (s.length() != 0)
                    arguments.add(s);
            }
        }

        jarsetup.setArgs(arguments);

        //   String s = defaultBucket + job.getJobDirectory() + "/"
        //            + job.getJarFile();

        String jarFile = job.getJarFile();
        if (!jarFile.startsWith("s3n://")) {
            String jobDirectory = job.getJobDirectory();
            if (!jobDirectory.startsWith("s3n://") && !jarFile.startsWith(jobDirectory))
                jarFile = jobDirectory + "/" + jarFile;

            if (!jarFile.startsWith("s3n://"))
                jarFile = defaultBucket + jarFile;
        }
        jarsetup.setJar(jarFile);
        jarsetup.setMainClass(job.getMainClass());
        return jarsetup;
    }

    private synchronized RunJobFlowRequest getJobFlowRequest(JobFlowInstancesDetail cluster) {
        if (m_JobFlowRequest == null) {
            JobFlowInstancesConfig flowInstancesConfig = getFlowInstancesConfig();
            String bucketUrl = getBucketUrl();
            String baseJobName = getBaseJobName();
            if (cluster != null) {
                flowInstancesConfig.setKeepJobFlowAliveWhenNoSteps(cluster.isKeepJobFlowAliveWhenNoSteps());
                flowInstancesConfig.setHadoopVersion(cluster.getHadoopVersion());
                flowInstancesConfig.setEc2KeyName(cluster.getEc2KeyName());
                //           flowInstancesConfig.setMasterInstanceType(cluster.getMasterInstanceType());
                //           flowInstancesConfig.setSlaveInstanceType(cluster.getSlaveInstanceType());
                //           flowInstancesConfig.setInstanceCount(cluster.getInstanceCount());

            }

            RunJobFlowRequest request = makeJobFlowRequest(bucketUrl, baseJobName, m_Steps, flowInstancesConfig);
            m_JobFlowRequest = request;
        }
        return m_JobFlowRequest;
    }

    public static RunJobFlowRequest makeJobFlowRequest(String defaultBucket, String baseJobName, Collection<StepConfig> steps, JobFlowInstancesConfig flowInstancesConfig) {
        RunJobFlowRequest request = new RunJobFlowRequest();
        request.setInstances(flowInstancesConfig);
        request.setLogUri(defaultBucket + "logs");
        String jobFlowName = baseJobName + new Date().toString();
        request.setSteps(steps);
        System.err.println(jobFlowName);

        request.setName(jobFlowName);
        return request;
    }

    protected String getBucketUrl() {
        return AWSUtilities.AWS_URL + getDefaultDirectory() + "/";
    }

    public synchronized JobFlowInstancesConfig getFlowInstancesConfig() {
        if (m_FlowInstancesConfig == null) {
            JobFlowInstancesConfig conf = new JobFlowInstancesConfig();
            conf.setEc2KeyName(AWSUtilities.getDefaultKeyName());
            int numberInstances = getNumberInstances();
            //       conf.setInstanceCount(numberInstances);
            conf.setKeepJobFlowAliveWhenNoSteps(AWSClusterUtilities.isKeepJobAlive());
            conf.setHadoopVersion(DEFAULT_HADOOP_VERSION);
            List<InstanceGroupConfig> holder = new ArrayList<InstanceGroupConfig>();

            // add one master
            AWSInstanceSize size = getMasterSize();
            // make the whole cluster spot
            if (AWSClusterUtilities.isUseSpotMaster()) {
                InstanceGroupConfig group = AWSSpotPricing.makeSpotBid(size, 1, InstanceRoleType.MASTER,MarketType.SPOT);
                  holder.add(group);
             }
            // master is on demand all others spot
            else {
                InstanceGroupConfig group = AWSSpotPricing.makeSpotBid(size, 1, InstanceRoleType.MASTER,MarketType.ON_DEMAND);
                  holder.add(group);
              }
            if (numberInstances > 1) {
                size = getSlaveSize();
                InstanceGroupConfig group2 = AWSSpotPricing.makeSpotBid(size, numberInstances - 1, InstanceRoleType.CORE,MarketType.SPOT);
                holder.add(group2);
            }
            //       InstanceGroupConfig group1 = AWSSpotPricing.makeSpotBid(size, numberInstances, InstanceRoleType.TASK);
            //        holder.add(group1);


            conf.setInstanceGroups(holder);
            m_FlowInstancesConfig = conf;
        }
        return m_FlowInstancesConfig;
    }
//
//    private JobFlowInstancesConfig buildConfig(IAWSCluster cluster) {
//        JobFlowInstancesConfig conf = new JobFlowInstancesConfig();
//        conf.setEc2KeyName(AWSUtilities.getDefaultKeyName());
//        conf.setInstanceCount(cluster.getNumberInstances());
//        conf.setKeepJobFlowAliveWhenNoSteps(false);
//        conf.setHadoopVersion(DEFAULT_HADOOP_VERSION);
//        final String sizeStr = cluster.getInstanceSize().toString();
//        conf.setMasterInstanceType(sizeStr);
//        conf.setPlacement(AWSUtilities.getDefaultPlacement());
//        conf.setSlaveInstanceType(sizeStr);
//        return conf;
//    }
//


    private static class PrintingMonitor implements JobStatusChangeListener {
        @Override
        public void onJobStatusChange(final JobMonitor monitor,
                                      final String state) {
            System.out.println("Status change to " + state);
        }

        @Override
        public void onJobMessageChange(final JobMonitor monitor,
                                       final String state) {
            System.out.println("Message change to " + state);

        }

        @Override
        public void onJobTerminate(final JobMonitor monitor, final String state) {
            System.out.println("Terminated with status " + state);

        }

        @Override
        public void onLogChange(final AWSLogType type, final String added) {
            if (type == AWSLogType.stderr) {
                System.err.print(added);
            }
            else {
                System.out.print(added);

            }

        }

        @Override
        public void onLogComplete(final AWSLogType type, final String added) {
            if (type == AWSLogType.stderr) {
                System.err.print(added);
            }
            else {
                System.out.print(added);

            }

        }

    }

    private static void runOneShot(final String[] args) {
        Class<OneShotHadoop> mainClass = OneShotHadoop.class;
        String jobName = mainClass.getSimpleName();
        if (args.length > 0)
            jobName = args[0];
        final AWSMapReduceLauncher launcher = new AWSMapReduceLauncher(jobName);
        // if(true)
        // throw new UnsupportedOperationException("Fix This"); // ToDo
        // launcher.setMainClass();
        launcher.setDefaultDirectory(WORD_COUNT_BUCKET_NAME);

        IHadoopJob job = HadoopJob.buildJob(mainClass, "moby", // data on hdfs
                "jobs", // jar location
                "output" // output location - will have outputN added

        );
        // launcher.runJob(job);
        launcher.runJobInCluster(job);
    }

    private static void runNShot(final String[] args) {
        Class<NShotTest> mainClass = NShotTest.class;
        String jobName = mainClass.getSimpleName();
        if (args.length > 0)
            jobName = args[0];
        final AWSMapReduceLauncher launcher = new AWSMapReduceLauncher(jobName);
        // if(true)
        // throw new UnsupportedOperationException("Fix This"); // ToDo
        // launcher.setMainClass();
        launcher.setDefaultDirectory(WORD_COUNT_BUCKET_NAME);

        IHadoopJob job = HadoopJob.buildJob(mainClass, "moby", // data on hdfs
                "jobs", // jar location
                "output" // output location - will have outputN added

        );
        // launcher.runJob(job);
        launcher.runJobInCluster(job);
    }


    private static void runWordCount(final String[] args) {
        Class<CapitalWordCount> mainClass = CapitalWordCount.class;
        String jobName = mainClass.getSimpleName();
        if (args.length > 0)
            jobName = args[0];
        final AWSMapReduceLauncher launcher = new AWSMapReduceLauncher(jobName);

        //    HadoopJob.setJarRequired(REUSE_JAR == null);
        // if(true)
        // throw new UnsupportedOperationException("Fix This"); // ToDo
        // launcher.setMainClass();
        launcher.setDefaultDirectory(WORD_COUNT_BUCKET_NAME);

        IHadoopJob job = HadoopJob.buildJob(mainClass, "moby", // data on hdfs
                "jobs", // jar location
                "output" // output location - will have outputN added

        );
        job.setNumberInstances(1);
        //      job.setJarFile(REUSE_JAR);
        //   controller.runJob(job);
        // launcher.runJob(job);
        launcher.runJobInCluster(job);
    }


//    private static void runStatisticalWordCount(final String[] args) {
//     Class<StatisticalWordCount> mainClass = StatisticalWordCount.class;
//     String jobName = mainClass.getSimpleName();
//     if (args.length > 0)
//         jobName = args[0];
//     final AWSMapReduceLauncher launcher = new AWSMapReduceLauncher(jobName);
//     // if(true)
//     // throw new UnsupportedOperationException("Fix This"); // ToDo
//     // launcher.setMainClass();
//     launcher.setDefaultDirectory(WORD_COUNT_BUCKET_NAME);
//
//     IHadoopJob job = HadoopJob.buildJob(mainClass, "moby", // data on hdfs
//             "jobs", // jar location
//             "output" // output location - will have outputN added
//
//     );
//     launcher.runJobInCluster(job);
// }


    private static void runHelloTest(final String[] args) {
        Class<RunExecutableTest> mainClass = RunExecutableTest.class;
        String jobName = mainClass.getSimpleName();
        AWSMapReduceLauncher launcher = new AWSMapReduceLauncher(jobName);
        launcher.setDefaultDirectory(MAIN_BUCKET_NAME);

        launcher.setNumberInstances(1);
        launcher.setSlaveSize(AWSInstanceSize.Small);
        if (args.length > 0)
            launcher.setNumberInstances(Integer.parseInt(args[0]));
        if (args.length > 1)
            launcher.setSlaveSize(AWSInstanceSize.parse(args[1]));


        IHadoopJob job = HadoopJob.buildJob(
                mainClass,
                "SINGLE_format_mouse_matrices",     // data on hdfs
                "jobs",      // jar location
                "outputTheme"             // output location - will have outputN added

        );
        launcher.runJob(job);
    }

//
//    private static void runMotifLocator(final String[] args) {
//        Class<RunMotifLocator> mainClass = RunMotifLocator.class;
//        String jobName = mainClass.getSimpleName();
//        AWSMapReduceLauncher launcher = new AWSMapReduceLauncher(jobName);
//        launcher.setDefaultDirectory(MAIN_BUCKET_NAME);
//
//        launcher.setNumberInstances(1);
//        launcher.setSlaveSize(AWSInstanceSize.Small);
//        if (args.length > 0)
//            launcher.setNumberInstances(Integer.parseInt(args[0]));
//        if (args.length > 1)
//            launcher.setSlaveSize(AWSInstanceSize.parse(args[1]));
//
//
//        IHadoopJob job = HadoopJob.buildJob(
//                mainClass,
//                "SINGLE_format_mouse_matrices",     // data on hdfs
//                "jobs",      // jar location
//                "outputTheme"             // output location - will have outputN added
//
//        );
//        launcher.runJob(job);
//    }
//

    private static void runNShotTest(final String[] args) {
        Class<NShotTest> mainClass = NShotTest.class;
        String jobName = mainClass.getSimpleName();
        AWSMapReduceLauncher launcher = new AWSMapReduceLauncher(jobName);
        launcher.setDefaultDirectory(WORD_COUNT_BUCKET_NAME);

        launcher.setNumberInstances(1);
        launcher.setSlaveSize(AWSInstanceSize.Small);
        if (args.length > 0)
            launcher.setNumberInstances(Integer.parseInt(args[0]));
        if (args.length > 1)
            launcher.setSlaveSize(AWSInstanceSize.parse(args[1]));


        IHadoopJob job = HadoopJob.buildJob(
                mainClass,
                "FeeFie.txt",     // data on hdfs
                "jobs",      // jar location
                "NShot"             // output location - will have outputN added

        );
        launcher.runJob(job);
    }


    private static void runCapabilitiesTester(final String[] args) {
        Class<CapabilitiesTester> mainClass = CapabilitiesTester.class;
        String jobName = mainClass.getSimpleName();
        AWSMapReduceLauncher launcher = new AWSMapReduceLauncher(jobName);
        launcher.setDefaultDirectory(MAIN_BUCKET_NAME);

        launcher.setNumberInstances(1);
        launcher.setSlaveSize(AWSInstanceSize.Small);

        IHadoopJob job = HadoopJob.buildJob(
                mainClass,
                "SINGLE_format_mouse_matrices",     // data on hdfs
                "jobs",      // jar location
                "outputTheme"             // output location - will have outputN added

        );
        launcher.runJob(job);
    }


    public static final int DEFAULT_YEAST_INSTANCES = 3;

    public static final AWSInstanceSize DEFAULT_YEAST_SIZE = AWSInstanceSize.Small;


    public static final int DEFAULT_HUMAN_INSTANCES = 100;

    public static final AWSInstanceSize DEFAULT_HUMAN_SIZE = AWSInstanceSize.Small;

    public static final String WORD_COUNT_BUCKET_NAME = AWSUtilities.getDefaultBucketName(); //"moby50";
    public static final String MAIN_BUCKET_NAME = AWSUtilities.getDefaultBucketName();

    public static void main(String[] args) {
        //     runNShot(args);
        //    runOneShot(args);
        runWordCount(args);
        //   runStatisticalWordCount(args);
        //  runCapabilitiesTester(args);
        // runYeastProcess(args);
        // runHumanProcess(args);
        // runNShotTest(args);
        //runHelloTest(args);
        //runMotifLocator(args);
    }

}
