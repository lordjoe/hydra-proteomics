package org.systemsbiology.hadoop;

import org.apache.hadoop.*;

/**
 * org.systemsbiology.hadoop.HadoopMajorVersion
 *   enumeration of major Hadoop versions
 * @author Steve Lewis
 * @date 19/05/13
 */
public enum HadoopMajorVersion {
    Version0("0.2"), Version1("1."), Version2("2.");

    private final String m_StartText;

    private HadoopMajorVersion(String startText) {
        m_StartText = startText;
    }

    public String getStartText() {
        return m_StartText;
    }

    /**
     * figure out the major hadoop version by looking at     HadoopVersionAnnotation in
     * package
     * @return !null majoe version
     * @throws  IllegalStateException if presented with a version it does not understand
     */
    public static HadoopMajorVersion getHadoopVersion() {
        // force the class loader to load a class in the package so we can read the package
        Class hadoopVersionAnnotation = HadoopVersionAnnotation.class; // make cure the class loader know about packages
        Package aPackage = Package.getPackage("org.apache.hadoop");
        HadoopVersionAnnotation annotation = (HadoopVersionAnnotation) aPackage.getAnnotation(HadoopVersionAnnotation.class);
        String version = annotation.version();
        HadoopMajorVersion[] versions = values();
        for (HadoopMajorVersion v : versions) {
             if(version.startsWith(v.getStartText()))
                 return v;
        }
        throw new IllegalStateException("Unknown Hadoop version " + version);
    }
}
