package org.systemsbiology.awscluster;

import com.amazonaws.services.elasticmapreduce.*;
import com.amazonaws.services.elasticmapreduce.model.*;

import java.util.*;

/**
 * org.systemsbiology.awscluster.ClusterKiller
 *
 * @author Steve Lewis
 * @date Oct 1, 2010
 */
public class ClusterKiller
{
    public static ClusterKiller[] EMPTY_ARRAY = {};
    public static Class THIS_CLASS = ClusterKiller.class;

    public static final long CHECK_JOB_INZTERVAL = 5 * 60 * 1000; // 5 minutes

    private static ClusterKiller gInstance;

    public static synchronized ClusterKiller getInstance()
    {
        if (gInstance == null)
            gInstance = new ClusterKiller();
        return gInstance;
    }

    private final Map<AmazonElasticMapReduce, Map<String, Long>> m_IdToDeathTime =
            new HashMap<AmazonElasticMapReduce, Map<String, Long>>();

    private ClusterKiller()
    {
        Thread killerThread = new Thread(new KillThread(), "AWS Job killer");
        killerThread.setDaemon(true);
        killerThread.start();
    }


    protected Map<String, Long> getJobMap(AmazonElasticMapReduce service)
    {
        synchronized (m_IdToDeathTime) {
            Map<String, Long> ret = m_IdToDeathTime.get(service);
            if (ret == null) {
                ret = new HashMap<String, Long>();
                m_IdToDeathTime.put(service, ret);
            }
            return ret;
        }
    }

    public void addJob(AmazonElasticMapReduce service, String id, int maxLife)
    {
        Map<String, Long> holder = getJobMap(service);
        // when can cluster be killed
        holder.put(id, System.currentTimeMillis() + maxLife);
    }


    protected void removeJob(AmazonElasticMapReduce service, String id)
    {
        Map<String, Long> holder = getJobMap(service);
        // when can cluster be killed
        holder.remove(id);
    }


    protected void checkJobs()
    {
        synchronized (m_IdToDeathTime) {
            for (AmazonElasticMapReduce service : m_IdToDeathTime.keySet()) {
                checkJobs(service);
            }
        }

    }

    /**
     * try to kill expired jobs
     *
     * @param service
     */
    protected void checkJobs(AmazonElasticMapReduce service)
    {
        long now = System.currentTimeMillis();
        Map<String, Long> holder = getJobMap(service);
        Set<String> killedJobs = new HashSet<String>();
        for (String id : holder.keySet()) {
            final Long end = holder.get(id);
            final long diff = end - now;
            if (diff < 0) {
                if (tryKillJob(service, id))
                    killedJobs.add(id);
            }
        }
        for (String id : killedJobs) {
            holder.remove(id);
        }


    }

    //     * <b>Pattern: </b>COMPLETED|FAILED|TERMINATED|RUNNING|SHUTTING_DOWN|STARTING|WAITING|BOOTSTRAPPING<br/>

    protected boolean tryKillJob(AmazonElasticMapReduce service, String id)
    {
        JobFlowDetail jd = AWSClusterUtilities.findJobWithId(  service,id);
        final JobFlowExecutionStatusDetail status = jd.getExecutionStatusDetail();
        final String state = status.getState();
        return maybeKillJob(service,jd.getJobFlowId(),state);

    }


    /**
     * Kill a job of it is not running, starting or terminating
     * @param pService !null service
     * @param pJd !null job id
     * @param pState Job state
     * @return  true if job killed
     */
    protected boolean maybeKillJob(AmazonElasticMapReduce pService, String id, String pState)
    {
        if("RUNNING".equals(pState))
            return false; // dont kill a running job
        if("STARTING".equals(pState))
            return false; // dont kill a running job
        if("SHUTTING_DOWN".equals(pState))
            return false; // dont kill a running job
        TerminateJobFlowsRequest request = new TerminateJobFlowsRequest();
        request.withJobFlowIds(id);
        pService.terminateJobFlows(request);
        return true;
    }


    /**
     * check and kill any expired jobs
     */
    public class KillThread implements Runnable
    {
        public void run()
        {
            try {
                while (true) {
                    Thread.sleep(CHECK_JOB_INZTERVAL);
                    checkJobs();
                }
            }
            catch (InterruptedException e) {
                return;
            }

        }
    }
}
