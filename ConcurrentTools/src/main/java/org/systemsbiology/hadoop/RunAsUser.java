package org.systemsbiology.hadoop;

import java.lang.reflect.*;

/**
 * org.systemsbiology.hadoop.RunAsUser
 *
 * @author Steve Lewis
 * @date 21/05/13
 */
public interface RunAsUser {
    public static RunAsUser[] EMPTY_ARRAY = {};
    public static Class THIS_CLASS = RunAsUser.class;

    /**
     * run the method in the name of the user user
     * @param staticMethod
     * @param user
     * @param args
     */
    void runAsUser(  String user, Object[] args);
}
