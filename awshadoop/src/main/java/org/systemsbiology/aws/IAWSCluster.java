package org.systemsbiology.aws;

import java.util.*;

/**
 * org.systemsbiology.aws.IAWSCluster
 * User: steven
 * Date: Aug 10, 2010
 */
public interface IAWSCluster {
    public static final IAWSCluster[] EMPTY_ARRAY = {};

    public String getJobId();

    public AWSInstanceSize getInstanceSize();

    public int getNumberInstances();

    public Date getStartTime();

    public Date getEndTime();

    public int getLifetimeMillisec();

    public void terminate();

    public boolean isInUse();
    

}
