package org.systemsbiology.awscluster;

import com.amazonaws.auth.*;
import com.amazonaws.services.elasticmapreduce.*;
import com.amazonaws.services.elasticmapreduce.model.*;
import com.amazonaws.services.elasticmapreduce.util.*;
import com.lordjoe.utilities.ElapsedTimer;
import org.systemsbiology.aws.*;
import org.systemsbiology.hadoop.*;
import org.systemsbiology.xtandem.*;

import java.util.*;

/**
 * org.systemsbiology.awscluster.AWSClusterUtilities
 * Functions to look for a job on aws elastic map reduce with
 * desired number of properties
 *
 * @author Steve Lewis
 * @date Sep 30, 2010
 */
public class AWSClusterUtilities {
    public static AWSClusterUtilities[] EMPTY_ARRAY = {};
    public static Class THIS_CLASS = AWSClusterUtilities.class;

    public static final String USE_SPOT_MASTER_PROPERTY = "org.systemsbiology.aws.UseSpotMaster";
    public static final String CLUSTER_SIZE_PROPERTY = "org.systemsbiology.aws.ClusterSize";
    public static final String KEEP_AFTER_JOB = "org.systemsbiology.aws.KeepClusterAfterJob";
    public static final String INSTANCE_SIZE_PROPERTY = "org.systemsbiology.aws.InstanceSize";
    public static final int DEFAULT_CLUSTER_SIZE = 5;
    public static final int SMALL_INSTANCE_COUNT = 1;
    public static final int MEDIUM_INSTANCE_COUNT = 10;
    public static final int LARGE_INSTANCE_COUNT = 100;
    public static final AWSInstanceSize DEFAULT_MASTER_SIZEX = AWSInstanceSize.Large;  // AWSInstanceSize.Small;

    private static boolean gKeepJobAlive = false;
    private static boolean gUseSpotMaster = false;
    private static int gClusterSize = DEFAULT_CLUSTER_SIZE;
    private static AWSInstanceSize gMasterSize = DEFAULT_MASTER_SIZEX;
    private static AWSInstanceSize gSlaveSize = null;


    public static AWSInstanceSize getMasterSize() {
        return gMasterSize;
    }

    public static void setMasterSize(final AWSInstanceSize pMasterSize) {
        gMasterSize = pMasterSize;
    }

    public static AWSInstanceSize getSlaveSize() {
        if (gSlaveSize == null)
            return getMasterSize();
        return gSlaveSize;
    }

    public static void setSlaveSize(final AWSInstanceSize pSlaveSize) {
        gSlaveSize = pSlaveSize;
    }

    public static JobFlowInstancesDetail gDefaultClusterRequest;


    private static String gDefaultDirectory;

    public static boolean isKeepJobAlive() {
        return gKeepJobAlive;
    }

    public static void setKeepJobAlive(final boolean pKeepAfterJob) {
        gKeepJobAlive = pKeepAfterJob;
    }

    public static boolean isUseSpotMaster() {
        return gUseSpotMaster;
    }

    public static void setUseSpotMaster(final boolean pUseSpotMaster) {
        gUseSpotMaster = pUseSpotMaster;
    }

    public static int getClusterSize() {
        return gClusterSize;
    }

    public static void setClusterSize(final int pClusterSize) {
        gClusterSize = pClusterSize;
    }

    public static String getDefaultDirectory() {
        return gDefaultDirectory;
    }

    public static void setDefaultDirectory(String pDefaultDirectory) {
        gDefaultDirectory = pDefaultDirectory;
    }

    public static void configure(IMainData params) {
        setKeepJobAlive(params.getBooleanParameter(KEEP_AFTER_JOB, false));
        setClusterSize(params.getIntParameter(CLUSTER_SIZE_PROPERTY, DEFAULT_CLUSTER_SIZE));
        setUseSpotMaster(params.getBooleanParameter(USE_SPOT_MASTER_PROPERTY, false));
        String size = params.getParameter(INSTANCE_SIZE_PROPERTY, getMasterSize().toString());
        AWSInstanceSize sizeObject = AWSInstanceSize.getInstanceSize(size);
        setMasterSize(sizeObject);
        String jarName = params.getParameter(HadoopUtilities.JAR_PROPERTY);
        HadoopUtilities.setReuseJar(jarName);
    }

    /**
     * build default request - here one small instance but that can change
     *
     * @return !null instance detail
     */
    public static synchronized JobFlowInstancesDetail getDefaultClusterRequest() {
        if (gDefaultClusterRequest == null) {
            gDefaultClusterRequest = getMinimalClusterRequest();
        }
        return gDefaultClusterRequest;
    }

    /**
     * build default request - here one small instance but that can change
     *
     * @return !null instance detail
     */
    public static synchronized JobFlowInstancesDetail getMinimalClusterRequest() {
        JobFlowInstancesDetail ret = new JobFlowInstancesDetail();
        ret.setHadoopVersion(AWSMapReduceLauncher.DEFAULT_HADOOP_VERSION);
        int clusterSize = getClusterSize();
        ret.setInstanceCount(clusterSize);
        String masterInstanceType = getMasterSize().toString();
        ret.setMasterInstanceType(masterInstanceType);
        String slaveInstanceType = getSlaveSize().toString();
        ret.setSlaveInstanceType(slaveInstanceType);
        return ret;
    }

    /**
     * build default request - here one small instance but that can change
     *
     * @return !null instance detail
     */
    public static synchronized JobFlowInstancesDetail getMediumClusterRequest() {
        JobFlowInstancesDetail ret = new JobFlowInstancesDetail();
        ret.setHadoopVersion(AWSMapReduceLauncher.DEFAULT_HADOOP_VERSION);
        ret.setInstanceCount(MEDIUM_INSTANCE_COUNT);
        ret.setMasterInstanceType(getMasterSize().toString());
        ret.setSlaveInstanceType(getSlaveSize().toString());
        return ret;
    }

    /**
     * build default request - here one small instance but that can change
     *
     * @return !null instance detail
     */
    public static synchronized JobFlowInstancesDetail getLargeClusterRequest() {
        JobFlowInstancesDetail ret = new JobFlowInstancesDetail();
        ret.setHadoopVersion(AWSMapReduceLauncher.DEFAULT_HADOOP_VERSION);
        ret.setInstanceCount(LARGE_INSTANCE_COUNT);
        ret.setMasterInstanceType(getMasterSize().toString());
        ret.setSlaveInstanceType(getSlaveSize().toString());
        return ret;
    }

    /**
     * find a cluster matching the details
     *
     * @param service !null service
     * @param request !null request with parameters
     * @return !null job
     */
    public static JobFlowDetail guaranteeJob(AmazonElasticMapReduce service,
                                             JobFlowInstancesDetail request, RunJobFlowRequest asks) {
        final JobFlowDetail jobFlowDetail = guaranteeJobRetry(service, request, asks, 0);

        //    ClusterKiller.getInstance().addJob(service, jobFlowDetail.getJobFlowId(), maxLifetime);
        return jobFlowDetail;
    }


    public static final int MAX_RETRIES = 3;

    /**
     * find a cluster matching the details
     *
     * @param service !null service
     * @param request !null request with parameters
     * @return !null job
     */
    public static JobFlowDetail guaranteeJobRetry(AmazonElasticMapReduce service,
                                                  JobFlowInstancesDetail request, RunJobFlowRequest asks,
                                                  int retries) {
        if (retries > MAX_CLUSTER_WAIT)
            throw new IllegalStateException("Cannot start cluster");

        // Look at the jobs we have running
        final DescribeJobFlowsResult res = service.describeJobFlows();
        final List<JobFlowDetail> details = res.getJobFlows();
        boolean hadMatch = false;
        for (JobFlowDetail jd : details) {
            // drop the ones that are terminted or terminating
            JobFlowExecutionStatusDetail executionStatusDetail = jd.getExecutionStatusDetail();
            String state = executionStatusDetail.getState();
            if (isNeverReadyState(state))
                continue; // are we terminated or shutting down

            System.out.println("Examining job " + jd.getJobFlowId() + " " + jd.getName());
            // does it match what we want
            if (matchesDetail(jd, request, true)) {
                hadMatch = true;
                final String id = jd.getJobFlowId();
                // make sure it is ready
                if (waitForClusterReady(service, id)) {
                    System.out.println("Reusing job " + jd.getJobFlowId());
                    return jd;   // use it
                }
            }
        }
        // maybe we need to try after a bit
        if (hadMatch) {
            try {
                Thread.sleep(RETEST_WAIT * 30);  // 30 sec
            }
            catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return guaranteeJobRetry(service, request, asks, retries + 1);
        }
        // no suitable jobs so make one there create one
        request.setEc2KeyName(AWSUtilities.getDefaultKeyName());
        return createJob(service, request, asks);
    }

    /**
     * find a cluster matching the details or close enough
     *
     * @param service !null service
     * @param request !null request with parameters
     * @return !null job
     */
    public static JobFlowDetail guaranteeSimilarJob(AmazonElasticMapReduce service,
                                                    JobFlowInstancesDetail request, RunJobFlowRequest asks) {
        final DescribeJobFlowsResult res = service.describeJobFlows();
        final List<JobFlowDetail> details = res.getJobFlows();
        for (JobFlowDetail jd : details) {
            if (matchesDetail(jd, request, false))
                return jd;
        }
        // nothins there create one
        return createJob(service, request, asks);
    }

    /**
     * find a job with a given id
     *
     * @param service !null service
     * @param id      !null job id
     * @return possibly null job
     */
    public static JobFlowDetail findJobWithId(AmazonElasticMapReduce service,
                                              String id) {
        final DescribeJobFlowsResult res = service.describeJobFlows();
        final List<JobFlowDetail> details = res.getJobFlows();
        for (JobFlowDetail jd : details) {
            if (jd.getJobFlowId().equals(id)) {
                return jd;
            }
        }
        return null; // not found
    }


    /**
     * create a cluster matching the details
     *
     * @param service !null service
     * @param request !null request with parameters
     * @return !null job
     */
    protected static JobFlowDetail createJob(AmazonElasticMapReduce service,
                                             JobFlowInstancesDetail request, RunJobFlowRequest asks) {
        ElapsedTimer et = new ElapsedTimer();
        //     RunJobFlowRequest asks = buildRunJobFlowRequest(request);
        //     RunJobFlowRequest request = getJobFlowRequest();
        // These lines enable debugging
        // see  http://docs.amazonwebservices.com/AWSJavaSDK/latest/javadoc/com/amazonaws/services/elasticmapreduce/util/StepFactory.html
        StepFactory stepFactory = new StepFactory();
        // make a setting step just so the cluster has something to do
        StepConfig enableDebugging = new StepConfig()
                .withName("Enable Debugging")
                .withActionOnFailure("TERMINATE_JOB_FLOW")
                .withHadoopJarStep(stepFactory.newEnableDebuggingStep());
        asks.withSteps(enableDebugging);

        JobFlowInstancesConfig instances = asks.getInstances();
        instances.setKeepJobFlowAliveWhenNoSteps(request.isKeepJobFlowAliveWhenNoSteps());
        //       instances.setMasterInstanceType(request.getMasterInstanceType());
        //      instances.setSlaveInstanceType(request.getSlaveInstanceType());

        System.out.println("Creating new job ");
        final RunJobFlowResult res = service.runJobFlow(asks);

        final DescribeJobFlowsResult resx = service.describeJobFlows();
        final List<JobFlowDetail> details = resx.getJobFlows();
        for (JobFlowDetail jd : details) {
            final String found = jd.getJobFlowId();
            String jobFlowId = res.getJobFlowId();
            if (found.equals(jobFlowId)) {
                if (waitForClusterReady(service, found)) {
                    et.showElapsed("Create new job " + jd.getJobFlowId());
                    return jd;
                }
            }

        }
        throw new UnsupportedOperationException("Cluster failed to start"); // huh???
    }

    protected static JobFlowDetail findJobDetailWithId(AmazonElasticMapReduce service, String id) {
        final DescribeJobFlowsResult resx = service.describeJobFlows();
        final List<JobFlowDetail> details = resx.getJobFlows();
        for (JobFlowDetail jd : details) {
            final String found = jd.getJobFlowId();
            if (found.equals(id)) {
                return jd;
            }

        }
        return null;
    }


    /**
     * copy inslances from a detail into a RunJobFlowRequest
     *
     * @param request !null source
     * @return !null copy
     */
//    protected static RunJobFlowRequest buildRunJobFlowRequest(JobFlowInstancesDetail request,RunJobFlowRequest flow) {
//        JobFlowInstancesConfig config = buildJobFlowInstancesConfig(request );
//        RunJobFlowRequest ret = buildRequest(config);
//
//        StepFactory stepFactory = new StepFactory();
//
//        StepConfig enableDebugging = new StepConfig()
//                .withName("Enable Debugging")
//                .withActionOnFailure("TERMINATE_JOB_FLOW")
//                .withHadoopJarStep(stepFactory.newEnableDebuggingStep());
//
//
//        ret.setInstances(config);
//        return ret;
//    }
//

    /**
     * copy inslances from a detail into a request
     *
     * @param request !null source
     * @return !null copy
     */
    protected static JobFlowInstancesConfig buildJobFlowInstancesConfig(JobFlowInstancesDetail request, boolean useSpots) {
        JobFlowInstancesConfig ret = new JobFlowInstancesConfig();
        ret.setKeepJobFlowAliveWhenNoSteps(true);    // these are persistent clusters so this is true

        if (!useSpots) {
            ret.setInstanceCount(request.getInstanceCount());
            ret.setMasterInstanceType(request.getMasterInstanceType());
            ret.setSlaveInstanceType(request.getSlaveInstanceType());
            ret.setEc2KeyName(request.getEc2KeyName());
            ret.setHadoopVersion(request.getHadoopVersion());
            ret.setPlacement(request.getPlacement());
        }
        else {
            throw new IllegalStateException("problem"); // ToDo change
        }

        return ret;
    }

    protected static RunJobFlowRequest buildRequest(final JobFlowInstancesConfig pConf, IHadoopJob hj) {
        RunJobFlowRequest request = new RunJobFlowRequest();
        request.setInstances(pConf);
        String defaultBucket = getBucketUrl();
        request.setLogUri(defaultBucket + "logs");
        String jobFlowName = buildJobName(hj);
        System.err.println(jobFlowName);

        request.setName(jobFlowName);
        return request;
    }

    public static String buildJobName(IHadoopJob hj) {
        String jobFlowName = hj.getName() + "_" + hj.getUID();
        return jobFlowName;
    }

    protected static String getBucketUrl() {
        return AWSUtilities.AWS_URL + getDefaultDirectory() + "/";
    }


    /**
     * test JobFlowInstancesDetail equivalent to request
     * non-null fields in request are matched in job
     *
     * @param job
     * @param request
     * @return
     */
    public static boolean matchesDetail(JobFlowDetail job, JobFlowInstancesDetail request,
                                        boolean exactMatch) {
        JobFlowInstancesDetail instances = job.getInstances();
        if (exactMatch)
            return matchesDetailExactly(instances, request);
        else
            return matchesDetail(instances, request);

    }

    public static JobFlowInstancesDetail requestFromParams(Map<String, String> params) {
        JobFlowInstancesDetail ret = new JobFlowInstancesDetail();

        String test = null;
        test = params.get("InstanceCount");
        if (test != null) {
            ret.setInstanceCount(Integer.parseInt(test));
        }

        test = params.get("Ec2KeyName");
        if (test != null) {
            ret.setEc2KeyName(test);
        }
        test = params.get("HadeoopVersion");
        if (test != null) {
            ret.setHadoopVersion(test);
        }
        test = params.get("MasterInstanceType");
        if (test != null) {
            ret.setMasterInstanceType(test);
        }
        test = params.get("SlaveInstanceType");
        if (test != null) {
            ret.setSlaveInstanceType(test);
        }

        return ret;
    }

    public static JobFlowInstancesDetail getClusterRequestFromConfig(final JobFlowInstancesConfig config) {
        JobFlowInstancesDetail ret = new JobFlowInstancesDetail();
        List<InstanceGroupConfig> instanceGroups = config.getInstanceGroups();
        List<InstanceGroupDetail> holder = new ArrayList<InstanceGroupDetail>();
        for (InstanceGroupConfig ig : instanceGroups)
            holder.add(fromConfig(ig));
        ret.setInstanceGroups(holder);
        ret.setHadoopVersion(config.getHadoopVersion());
        ret.setInstanceCount(config.getInstanceCount());
        ret.setMasterInstanceType(config.getMasterInstanceType());
        ret.setSlaveInstanceType(config.getSlaveInstanceType());
        ret.setKeepJobFlowAliveWhenNoSteps(config.isKeepJobFlowAliveWhenNoSteps());
        ret.setPlacement(config.getPlacement());
        ret.setEc2KeyName(config.getEc2KeyName());
        return ret;
    }

    private static InstanceGroupDetail fromConfig(final InstanceGroupConfig ig) {
        InstanceGroupDetail ret = new InstanceGroupDetail();
        ret.setBidPrice(ig.getBidPrice());
        ret.setInstanceRole(ig.getInstanceRole());
        ret.setInstanceType(ig.getInstanceType());
        ret.setInstanceRequestCount(ig.getInstanceCount());
        ret.setMarket(ig.getMarket());
        return ret;
    }


    public static Integer getJobRequestInstanceCount(JobFlowInstancesDetail request) {
        List<InstanceGroupDetail> igs = request.getInstanceGroups();
        if (igs == null || igs.size() == 0)
            return request.getInstanceCount();
        int sum = 0;
        for (InstanceGroupDetail test : igs) {
            Integer rc = test.getInstanceRequestCount();
            if (rc != null)
                sum += rc;
        }
        if (sum == 0)
            return null;
        return sum;
    }

    /**
     * test JobFlowInstancesDetail equivalent
     *
     * @param job     !null job1
     * @param request !null job2
     * @return true if all non-null fields in request are matched in job1
     */
    public static boolean matchesDetailExactly(JobFlowInstancesDetail job,
                                               JobFlowInstancesDetail request) {
        Object test = null;
        test = getJobRequestInstanceCount(request);
        if (test != null) {
            final Integer o2 = job.getInstanceCount();
            int comp = ((Integer) test).compareTo(o2);
            if (comp == 1)
                return false;
        }

        test = request.getEc2KeyName();
        if (test != null) {
            String ec2KeyName = job.getEc2KeyName();
            if (!areStringsEqual((String) test, ec2KeyName))
                return false;
        }

        test = request.getHadoopVersion();
        if (test != null) {
            final String o2 = job.getHadoopVersion();
            if (!areStringsEqual((String) test, o2))
                return false;
        }

        test = request.getMasterInstanceType();
        if (test != null) {
            final String instanceType = job.getMasterInstanceType();
            AWSInstanceSize want = AWSInstanceSize.parse((String) test);
            AWSInstanceSize got = AWSInstanceSize.parse(instanceType);
            if (!AWSInstanceSize.iscompatibleWith(want, got))
                return false;
        }

        if (getJobRequestInstanceCount(request) == 1)
            return true;
        test = request.getSlaveInstanceType();
        if (test != null) {
            String instanceType = job.getSlaveInstanceType();
            if (instanceType == null)
                instanceType = job.getMasterInstanceType();
            AWSInstanceSize want = AWSInstanceSize.parse((String) test);
            AWSInstanceSize got = AWSInstanceSize.parse(instanceType);
            if (!AWSInstanceSize.iscompatibleWith(want, got))
                return false;
        }


        return true;

    }


    /**
     * test JobFlowInstancesDetail equivalent
     *
     * @param job     !null job1
     * @param request !null job2
     * @return true if all non-null fields in request are matched in job1
     */
    public static boolean matchesDetail(JobFlowInstancesDetail job, JobFlowInstancesDetail request) {
        Object test = null;
        test = request.getInstanceCount();
        if (test != null) {
            final Integer count = job.getInstanceCount();
            Integer t1 = (Integer) test;
            if (count < t1 || count > 3 * t1)
                return false;
        }

        test = request.getEc2KeyName();
        if (test != null) {
            if (isObjectEqual(test, job.getEc2KeyName())) return false;
        }

        test = request.getHadoopVersion();
        if (test != null) {
            if (!isObjectEqual(test, job.getHadoopVersion()))
                return false;
        }

        final String instanceType = request.getMasterInstanceType();
        test = instanceType;
        if (test != null) {
            AWSInstanceSize inst = AWSInstanceSize.getInstanceSize(instanceType);
            final String mt = job.getMasterInstanceType();
            AWSInstanceSize have = AWSInstanceSize.getInstanceSize(mt);

            if (!AWSInstanceSize.iscompatibleWith(inst, have))
                return false;
        }

        final String slaveInstanceType = request.getSlaveInstanceType();
        test = slaveInstanceType;
        if (test != null) {
            AWSInstanceSize inst = AWSInstanceSize.getInstanceSize(instanceType);
            AWSInstanceSize have = AWSInstanceSize.getInstanceSize(job.getMasterInstanceType());

            if (!AWSInstanceSize.iscompatibleWith(inst, have))
                return false;
        }


        return true;

    }

    /**
     * test for equality handles null well
     *
     * @param o1 possibly null object
     * @param o2 possibly null object
     * @return true if both null or o1.equals(02)
     */
    public static boolean isObjectEqual(Object o1, Object o2) {
        if (o1 == o2) return true;
        if (o1 == null || o2 == null)
            return false;
        return (o1.equals(o2));
    }

    /**
     * test for equality handles null well
     *
     * @param o1 possibly null object
     * @param o2 possibly null object
     * @return true if both null or o1.equals(02)
     */
    public static boolean areStringsEqual(String o1, String o2) {
        if (o1 == o2) return true;
        if (o1 == null || o2 == null)
            return false;
        return (o1.equals(o2));
    }


    public static final int MAX_CLUSTER_WAIT = 1000 * 60 * 30; // 30 minutes
    public static final int RETEST_WAIT = 60 * 1000; // 2 minutes

    public static boolean waitForClusterReady(AmazonElasticMapReduce service, String id) {
        JobFlowDetail detail = findJobDetailWithId(service, id);
        JobFlowExecutionStatusDetail es = detail.getExecutionStatusDetail();

        String state = es.getState();
        int timeout = 0;


        if (isNeverReadyState(state))
            return false;

        long start = System.currentTimeMillis();

        // COMPLETED|FAILED|TERMINATED|RUNNING|SHUTTING_DOWN|STARTING|WAITING|BOOTSTRAPPING<br/>
        while (!isReadyState(state)) {
            final int waitTIme = RETEST_WAIT;
            try {
                Thread.sleep(waitTIme);
            }
            catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            timeout += waitTIme;
            if (timeout > MAX_CLUSTER_WAIT)
                return false;

            detail = findJobDetailWithId(service, id);
            es = detail.getExecutionStatusDetail();
            state = es.getState();
            System.out.println("State is  " + state);
        }
        long end = System.currentTimeMillis();
        int sec = (int) ((end - start) / 60000);
        System.out.println("Cluster started in " + sec);
        return true;
    }

    //     * <b>Pattern: </b>COMPLETED|FAILED|TERMINATED|RUNNING|SHUTTING_DOWN|STARTING|WAITING|BOOTSTRAPPING<br/>

    public static boolean isReadyState(String state) {
        if ("WAITING".equals(state))
            return true;
        if ("COMPLETED".equals(state))
            return true;
        if ("FAILED".equals(state))
            return true;
        if ("TERMINATED".equals(state))
            return false;

        return false;
    }

    public static boolean isNeverReadyState(String state) {
        if ("SHUTTING_DOWN".equals(state))
            return true;
        if ("TERMINATED".equals(state))
            return true;
        if ("COMPLETED".equals(state))
            return true;
        if ("FAILED".equals(state))
            return true;

        return false;
    }

    public static final int DEFAULT_CLUSTER_LIFE = 1000 * 60 * 60 * 2; // 2 hours

    /**
     * sample call and test
     *
     * @param args
     */
    public static void main(String[] args) {
        AWSCredentials creds = AWSUtilities.getCredentials();
        AmazonElasticMapReduce service = new AmazonElasticMapReduceClient(creds);
        JobFlowInstancesDetail request = getDefaultClusterRequest();


        JobFlowDetail detail = guaranteeJob(service, request, null);

        final JobFlowInstancesDetail whatWeGot = detail.getInstances();

        boolean ismatch = matchesDetail(whatWeGot, request);

        System.out.println("Matches " + ismatch);
    }

}
