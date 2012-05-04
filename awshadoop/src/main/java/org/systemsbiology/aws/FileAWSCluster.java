package org.systemsbiology.aws;

import java.io.*;
import java.util.*;

/**
 * org.systemsbiology.aws.FileAWSCluster
 * User: steven
 * Date: Aug 10, 2010
 */
public class FileAWSCluster implements IAWSCluster {
    public static final FileAWSCluster[] EMPTY_ARRAY = {};

    public static final String CLUSTER_DIRECTORY = "_ASWJOBS";

    public static IAWSCluster[] getActiveClusters() {
        List<IAWSCluster> holder = new ArrayList<IAWSCluster>();
        File cd = new File(CLUSTER_DIRECTORY);
        File[] files = cd.listFiles();
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                File file = files[i];
                if (file.getName().endsWith(".props")) {
                    IAWSCluster cluster = getActiveCluster(file);
                    if (cluster != null) {
                        holder.add(cluster);
                    }
                }
            }
        }
        IAWSCluster[] ret = new IAWSCluster[holder.size()];
        holder.toArray(ret);
        return ret;
    }

    public static IAWSCluster getActiveCluster(File file) {
       throw new UnsupportedOperationException("Fix This"); // ToDo
    }

    private String m_JobId;

    private AWSInstanceSize m_InstanceSize;

    private int m_NumberInstances;

    private Date m_StartTime;

    private int m_LifetimeMillisec;

    private boolean m_InUse;


    public FileAWSCluster() {
    }

    public FileAWSCluster(Properties data) {
        this();
        setJobId(data.getProperty("JobId"));
        setInstanceSize(AWSInstanceSize.valueOf(data.getProperty("InstanceSize")));
        setNumberInstances(Integer.parseInt(data.getProperty("NumberInstances")));
        setStartTime(new Date(Long.parseLong(data.getProperty("StartTime"))));
        setLifetimeMillisec(Integer.parseInt(data.getProperty("LifetimeMillisec")));

    }

    public void save() {
        Properties props = new Properties();
        props.setProperty("JobId", getJobId());
        props.setProperty("InstanceSize", getInstanceSize().toString());
        props.setProperty("NumberInstances", Integer.toString(getNumberInstances()));
        props.setProperty("StartTime", Long.toString(getStartTime().getTime()));
        props.setProperty("LifetimeMillisec", Long.toString(getLifetimeMillisec()));
        try {
            props.store(makeWriter(), null);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    protected Writer makeWriter() {
        try {
            File cd = new File(CLUSTER_DIRECTORY);
            cd.mkdirs();
            File f = new File(cd, getJobId() + ".props");
            return new FileWriter(f);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getJobId() {
        return m_JobId;
    }

    public void setJobId(final String pJobId) {
        m_JobId = pJobId;
    }

    public AWSInstanceSize getInstanceSize() {
        return m_InstanceSize;
    }

    public void setInstanceSize(final AWSInstanceSize pInstanceSize) {
        m_InstanceSize = pInstanceSize;
    }

    public int getNumberInstances() {
        return m_NumberInstances;
    }

    public void setNumberInstances(final int pNumberInstances) {
        m_NumberInstances = pNumberInstances;
    }

    public Date getStartTime() {
        return m_StartTime;
    }

    public void setStartTime(final Date pStartTime) {
        m_StartTime = new Date(pStartTime.getTime());
    }

    public Date getEndTime() {
        return new Date(getStartTime().getTime() + getLifetimeMillisec());
    }


    public int getLifetimeMillisec() {
        return m_LifetimeMillisec;
    }

    public void setLifetimeMillisec(final int pLifetimeMillisec) {
        m_LifetimeMillisec = pLifetimeMillisec;

    }

    public boolean isInUse() {
        return m_InUse;
    }

    public void setInUse(final boolean pInUse) {
        if (pInUse == true && isInUse())
            throw new IllegalStateException("Cluster is in use"); // ToDo change
        m_InUse = pInUse;
        save();
    }

    public void terminate() {
        throw new UnsupportedOperationException("Fix This"); // ToDo
    }
}
