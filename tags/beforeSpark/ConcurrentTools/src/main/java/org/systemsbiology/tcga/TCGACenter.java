package org.systemsbiology.tcga;

import java.util.*;

/**
 * org.systemsbiology.tcga.TCGAPatient
 * written by Steve Lewis
 * on Apr 9, 2010
 */
public class TCGACenter
{
    public static final TCGACenter[] EMPTY_ARRAY = {};
    public static final Class THIS_CLASS = TCGACenter.class;

    private static final Map<String, TCGACenter> m_Patients =
            new HashMap<String, TCGACenter>();

    public static TCGACenter getCenter(String name) {
        synchronized (m_Patients) {
            TCGACenter patient = m_Patients.get(name);
            if (patient != null)
                return patient;
            patient = new TCGACenter(name);
            m_Patients.put(name, patient);
            return patient;
        }
    }

    private final String m_PatientId;

    private TCGACenter(String pPatientId) {
        m_PatientId = pPatientId;
    }

    public String getPatientId() {
        return m_PatientId;
    }
}