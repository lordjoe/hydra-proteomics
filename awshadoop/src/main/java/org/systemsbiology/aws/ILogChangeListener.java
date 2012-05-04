package org.systemsbiology.aws;

/**
 * org.systemsbiology.aws.ILogChangeListener
 * User: steven
 * Date: Jun 16, 2010
 */
public interface ILogChangeListener {
    public static final ILogChangeListener[] EMPTY_ARRAY = {};

    public void onLogChange(AWSLogType type,String added);


    public void onLogComplete(AWSLogType type,String added);


}
