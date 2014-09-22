package org.systemsbiology.aws;

/**
 * org.systemsbiology.aws.JobStatusChangeListener
 * User: steven
 * Date: May 27, 2010
 */
public interface JobStatusChangeListener {
    public static final JobStatusChangeListener[] EMPTY_ARRAY = {};

    public void onJobStatusChange(JobMonitor monitor,String state);

    public void onJobMessageChange(JobMonitor monitor,String state);

    public void onJobTerminate(JobMonitor monitor,String state);


    public void onLogChange(AWSLogType type,String added);


    public void onLogComplete(AWSLogType type,String added);

}
