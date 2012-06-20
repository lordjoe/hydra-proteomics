package org.systemsbiology.gatk;

import java.util.*;

/**
 * org.systemsbiology.gatk.ExperimentalSubject
 * User: Steve
 * Date: 6/14/12
 */
public class ExperimentalSubject implements Comparable<ExperimentalSubject> {
    public static final ExperimentalSubject[] EMPTY_ARRAY = {};

    private static Map<String, ExperimentalSubject> gSubjects = new HashMap<String, ExperimentalSubject>();

    public static ExperimentalSubject getSubject(String id) {
        synchronized (gSubjects) {
            ExperimentalSubject ret = gSubjects.get(id);
            if (ret == null) {
                ret = new ExperimentalSubject(id);
                gSubjects.put(id, ret);
            }
            return ret;
        }
    }

    private final String m_Id;

    /**
     * use  getSubject to guarantee oe object per id
     * @param id
     */
    private ExperimentalSubject(final String id) {
        m_Id = id;
    }

    public String getId() {
        return m_Id;
    }

    @Override
    public String toString() {
        return getId();
    }

    @Override
    public int compareTo(final ExperimentalSubject o) {
         return getId().compareTo(o.getId());
    }
}
