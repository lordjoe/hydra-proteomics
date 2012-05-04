package org.systemsbiology.tcga;

import java.util.*;

/**
 * org.systemsbiology.tcga.TCGASampleType
 * written by Steve Lewis
 * on Apr 9, 2010
 */
public class TCGASampleType
{
    public static final TCGAPatient[] EMPTY_ARRAY = {};
    public static final Class THIS_CLASS = TCGAPatient.class;

    private static final Map<String, TCGASampleType> m_Patients =
            new HashMap<String, TCGASampleType>();

    public static TCGASampleType getSampleType(String name) {
        synchronized (m_Patients) {
            TCGASampleType patient = m_Patients.get(name);
            if (patient != null)
                return patient;
            patient = new TCGASampleType(name);
            m_Patients.put(name, patient);
            return patient;
        }
    }

    private final String m_SampleId;
    private final TCGASampleBaseType m_BaseType;

    private TCGASampleType(String name) {
        m_SampleId = name;
        m_BaseType = TCGAUtilities.sampleBaseTypeFromString(name);
    }




}