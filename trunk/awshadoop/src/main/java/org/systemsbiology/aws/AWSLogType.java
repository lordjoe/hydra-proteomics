package org.systemsbiology.aws;

/**
 * org.systemsbiology.aws.AWSLogType
 * User: steven
 * Date: Jun 16, 2010
 */
public enum AWSLogType {
    stdout,stderr,syslog,controller;
    public static final AWSLogType[] EMPTY_ARRAY = {};

}
