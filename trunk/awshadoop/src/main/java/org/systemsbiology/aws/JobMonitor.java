package org.systemsbiology.aws;

import com.amazonaws.*;
import com.amazonaws.services.elasticmapreduce.*;
import com.amazonaws.services.elasticmapreduce.model.*;
import com.lordjoe.utilities.*;
import org.systemsbiology.hadoop.*;
import org.systemsbiology.xtandem.*;

import java.util.*;
import java.util.concurrent.*;

/**
 * org.systemsbiology.aws.JobMonitor User: steven Date: May 27, 2010
 */
public class JobMonitor {
    public static final JobMonitor[] EMPTY_ARRAY = {};

    /**
     * http://meghsoft.com/blog/?p=30 says 10000 ms too fast
     */
    public static final int MINIMUM_QUERY_TIME = 30000; //     60000;
    public static final int S3_WAIT_TIME = 120000; //  120Sec

    private final String m_JobName;
    private IHadoopJob m_JobObject;
    private final AmazonElasticMapReduce m_Service;
    private String m_Status;
    private String m_LastMessage;
    private final List<JobStatusChangeListener> m_JobStatusChangeListeners = new CopyOnWriteArrayList<JobStatusChangeListener>();
    private boolean m_Running;
    private final StringBuilder m_JobStdout = new StringBuilder();
    private final StringBuilder m_JobStderr = new StringBuilder();
    private final StringBuilder m_JobSysLog = new StringBuilder();
    private final StringBuilder m_JobController = new StringBuilder();
    private final String m_DefaultDirectory;

    public JobMonitor(final String pJobName, IHadoopJob job, String defaultDirectory,
                      final AmazonElasticMapReduce pService) {
        m_JobName = pJobName;
        m_Service = pService;
        m_DefaultDirectory = defaultDirectory;
        m_JobObject = job;
        m_Running = true;
    }

    public IHadoopJob getJobObject() {
        return m_JobObject;
    }

    public void setJobObject(final IHadoopJob pJobObject) {
        m_JobObject = pJobObject;
    }

    public void clearLogs() {
        m_JobStdout.setLength(0);
        m_JobStderr.setLength(0);
        m_JobSysLog.setLength(0);
        m_JobController.setLength(0);
    }

    public String getDefaultDirectory() {
        return m_DefaultDirectory;
    }

    public JobFlowDetail getJobFlowDetails() {
        final DescribeJobFlowsResult jf = getService().describeJobFlows();
        for (JobFlowDetail details : jf.getJobFlows()) {
            if (details.getJobFlowId().equals(getJobName())) {
                return details;
            }
        }
        return null;
    }

    public String showJobStatus() {
        String state = null;
        try {
            JobFlowDetail details = getJobFlowDetails();
            if (details != null) {
                final JobFlowExecutionStatusDetail status = details
                        .getExecutionStatusDetail();
                state = status.getState();
                return state;

            }
            else {
                return null;
            }
//			final DescribeJobFlowsResult jf = getService().describeJobFlows();
//			for (JobFlowDetail details : jf.getJobFlows()) {
//				if (details.getJobFlowId().equals(getJobName())) {
//					final JobFlowExecutionStatusDetail status = details
//							.getExecutionStatusDetail();
//					state = status.getState();
//					String reason = status.getLastStateChangeReason();
//					if (reason != null) {
//						System.out.println(reason);
//						if (reason.startsWith("Shut down"))
//							break;
//                        if (reason.startsWith("Terminated"))
//                            break;
//               //         if (reason.startsWith("Waiting after step failed"))
//               //             throw new IllegalStateException("step failed");
//					}
//
//				}
//			}
//			// readLogs();
//			return state;
        }
        catch (AmazonClientException ex) {
            throw new RuntimeException(ex);
        }

    }

    public void readLogs(int lastRun) {
        ElapsedTimer elapsed = new  ElapsedTimer();
        clearLogs();
        String uuid = getJobObject().getUID().toString();
        int numberRetries = 0;
        String defaultDirectory = getDefaultDirectory();
        String jobName = getJobName();
        String logDirectory = findLogDirectory(jobName, lastRun, defaultDirectory, uuid);
        while (logDirectory == null && numberRetries++ < 3) {
            System.out.println("Waiting for logs");
            Util.pause(S3_WAIT_TIME * numberRetries);   // wait for file to exist
            logDirectory = findLogDirectory(jobName,lastRun, defaultDirectory, uuid);
        }

        if (logDirectory == null) {
            logDirectory = findLogDirectory(jobName, lastRun, defaultDirectory, uuid);
            throw new IllegalStateException("cannot find logs");
        }
        //       String logDirectory = "logs/" + getJobName() + "/steps/" + (step + 1) + "/";
        String test = "";
        while (!test.contains("INFO Execution ended with ret val ")) {
            handleLog(AWSLogType.controller, logDirectory, m_JobController);
            test = m_JobController.toString();
        }
        handleLog(AWSLogType.stdout, logDirectory, m_JobStdout);
        handleLog(AWSLogType.stderr, logDirectory, m_JobStderr);

        test = "";
        while (!test.contains("Reduce input records=")) {
            handleLog(AWSLogType.syslog, logDirectory, m_JobSysLog);
            test = m_JobSysLog.toString();
        }
        System.out.println();
        elapsed.showElapsed("Read Logs from " + logDirectory);

    }

    /**
     * look in the  controller logs for  in the job steps subdirectory to find the correct step
     * ToDo wait if they do not exist
     *
     * @return
     */
    public static String findLogDirectory(String jobName, int lastRun, String defaultDirectory, String uuid) {
        int step = lastStepsDirectory(jobName, lastRun, defaultDirectory);
        if (step == -1)
            return null;
        String test = "org.systemsbiology.UUID=" + uuid;
        String bucket = AWSUtilities.getDefaultBucketName();
        String logDirectory = "logs/" + jobName + "/steps/" + step + "/";
        int lastStep = Math.max(0, step - lastRun - 3);
        while (step > lastStep) {
            if (AWSUtilities.exists(bucket, logDirectory + "controller")) {
                String s = handleLogStatic(AWSLogType.controller, bucket, logDirectory, defaultDirectory);
                if (s.length() > 0) {
                    if (s.contains(test))
                        return logDirectory;
                }
            }
            step--;
            logDirectory = "logs/" + jobName + "/steps/" + step + "/";
        }
        return null;
    }

    protected static int lastStepsDirectory(String jobName, int lastRun, String defaultDirectory) {
        int step = 1;
        String bucket = AWSUtilities.getDefaultBucketName();
        String logDirectory = "logs/" + jobName + "/steps/" + (step + 1) + "/";
        while (AWSUtilities.exists(bucket, logDirectory)) {
            step += 10;
            logDirectory = "logs/" + jobName + "/steps/" + step + "/";
        }
        if (step == 1)     // no step directory
            return -1;
        while (!AWSUtilities.exists(bucket, logDirectory)) {
            step -= 1;
            logDirectory = "logs/" + jobName + "/steps/" + step + "/";
        }

        return step;
    }

    protected void handleLog(AWSLogType type, String logDirectory,
                             StringBuilder holder) {
        String logName = logDirectory + type.toString();
        try {
            String data = AWSUtilities.downloadFile(getDefaultDirectory(),
                    logName);
            if (data.length() <= holder.length())
                return;
            String added = data.substring(holder.length());
            holder.append(added);
            notifyLogChangeListeners(type, added);
        }
        catch (Exception e) {
            Throwable cause = Util.getUltimateCause(e);
            System.err.println("cannot download log " + logName + " because of " + cause.getMessage()
            );

        }
    }

    /**
     * more testable version of log fetcher
     *
     * @param type
     * @param bucket
     * @param logDirectory
     * @param defaultDirectory
     * @return
     */
    protected static String handleLogStatic(AWSLogType type, String bucket, String logDirectory, String defaultDirectory) {
        String logName = logDirectory + type.toString();
        if (!AWSUtilities.exists(bucket, logName))
            return "";
        try {
            String data = AWSUtilities.downloadFile(bucket, logName);
            return data;
        }
        catch (Exception e) {
            Throwable cause = Util.getUltimateCause(e);
            System.err.println("cannot download log " + logName + " because of " + cause.getMessage()
            );

        }
        return "";
    }

    /**
     * add a listener
     *
     * @param listener non-null listener to remove
     */
    public void addJobStatusChangeListener(JobStatusChangeListener listener) {
        m_JobStatusChangeListeners.add(listener);
    }

    /**
     * remove a listener
     *
     * @param listener non-null listener to remove
     */
    public void removeJobStatusChangeListener(JobStatusChangeListener listener) {
        m_JobStatusChangeListeners.remove(listener);
    }

    public void killJob() {
        try {
            TerminateJobFlowsRequest request = new TerminateJobFlowsRequest();
            final DescribeJobFlowsResult jf = m_Service.describeJobFlows();
            List<String> holder = new ArrayList<String>();
            for (JobFlowDetail details : jf.getJobFlows()) {
                final String name1 = details.getJobFlowId();
                holder.add(name1);
            }
            request.setJobFlowIds(holder);
            m_Service.terminateJobFlows(request);
        }
        catch (AmazonClientException ex) {
            throw new RuntimeException(ex);
        }

    }

    /**
     * Notify any listners
     */
    public void notifyJobStatusChangeListeners(String arg) // todo arg
    {
        JobStatusChangeListener[] listeners = null;
        if (m_JobStatusChangeListeners.isEmpty())
            return;
        listeners = new JobStatusChangeListener[m_JobStatusChangeListeners
                .size()];
        m_JobStatusChangeListeners.toArray(listeners);


        for (int i = 0; i < listeners.length; i++) {
            JobStatusChangeListener listener = listeners[i];
            listener.onJobStatusChange(this, arg);
        }

    }

    /**
     * Notify any listners
     */
    public void notifyMessageChangeListeners(String arg) // todo arg
    {
        JobStatusChangeListener[] listeners = null;
        if (m_JobStatusChangeListeners.isEmpty())
            return;
        listeners = new JobStatusChangeListener[m_JobStatusChangeListeners
                .size()];
        m_JobStatusChangeListeners.toArray(listeners);


        for (int i = 0; i < listeners.length; i++) {
            JobStatusChangeListener listener = listeners[i];
            listener.onJobMessageChange(this, arg);
        }

    }

    /**
     * Notify any listners
     */
    public void notifyLogChangeListeners(AWSLogType type, String arg) // todo
    // arg
    {
        JobStatusChangeListener[] listeners = null;
        if (m_JobStatusChangeListeners.isEmpty())
            return;
        listeners = new JobStatusChangeListener[m_JobStatusChangeListeners
                .size()];
        m_JobStatusChangeListeners.toArray(listeners);


        for (int i = 0; i < listeners.length; i++) {
            JobStatusChangeListener listener = listeners[i];
            listener.onLogChange(type, arg);
        }

    }

    /**
     * Notify any listners
     */
    public void notifyJobTerminated() // todo arg
    {
        JobStatusChangeListener[] listeners = null;
        if (m_JobStatusChangeListeners.isEmpty())
            return;
        listeners = new JobStatusChangeListener[m_JobStatusChangeListeners
                .size()];
        m_JobStatusChangeListeners.toArray(listeners);


        for (int i = 0; i < listeners.length; i++) {
            JobStatusChangeListener listener = listeners[i];
            listener.onJobTerminate(this, getStatus());
        }

    }

    public boolean isRunning() {
        return m_Running;
    }

    public void setRunning(final boolean pRunning) {
        m_Running = pRunning;
    }

    public String getJobName() {
        return m_JobName;
    }

    public AmazonElasticMapReduce getService() {
        return m_Service;
    }

    public String getStatus() {
        return m_Status;
    }

    public void setStatus(final String pStatus) {
        if (pStatus.equals(m_Status))
            return;
        m_Status = pStatus;
        notifyJobStatusChangeListeners(m_Status);
    }

    public String getLastMessage() {
        return m_LastMessage;
    }

    public void setLastMessage(final String pLastMessage) {
        if (pLastMessage == m_LastMessage)
            return;
        if (pLastMessage != null && pLastMessage.equals(m_LastMessage))
            return;
        m_LastMessage = pLastMessage;
        notifyJobStatusChangeListeners(pLastMessage);
    }

    public void monitorJob() {
        String pName = getJobName();
        try {
            String state = null;
            final AmazonElasticMapReduce service = getService();
            final DescribeJobFlowsResult jf = service.describeJobFlows();
            while (isRunning()) {
                for (JobFlowDetail details : jf.getJobFlows()) {
                    final String name1 = details.getJobFlowId();
                    if (pName.equals(name1)) {
                        final JobFlowExecutionStatusDetail status = details
                                .getExecutionStatusDetail();
                        state = status.getState();
                        setStatus(state);
                        String reason = status.getLastStateChangeReason();
                        setLastMessage(reason);
                    }
                }
                // readLogs();
                // Too fast and you will get throttled
                AWSUtilities.waitFor(MINIMUM_QUERY_TIME);
            }
            notifyJobTerminated();
        }
        catch (AmazonClientException ex) {
            throw new RuntimeException(ex);
        }

    }

    public String getStdout() {
        return m_JobStdout.toString();
    }

    public String getStderr() {
        return m_JobStderr.toString();
    }

    public String getSysLog() {
        return m_JobSysLog.toString();
    }

    public String getController() {
        return m_JobController.toString();
    }
}
