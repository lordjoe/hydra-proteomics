package org.systemsbiology.tcga;

import java.util.*;

/**
 * org.systemsbiology.tcga.TCGAPatient
 * written by Steve Lewis
 * on Apr 9, 2010
 */
public class TCGAPatient
{
    public static final TCGAPatient[] EMPTY_ARRAY = {};
    public static final Class THIS_CLASS = TCGAPatient.class;

    private static final Map<String, TCGAPatient> m_Patients =
            new HashMap<String, TCGAPatient>();

    public static TCGAPatient getPatient(String name) {
        synchronized (m_Patients) {
            TCGAPatient patient = m_Patients.get(name);
            if (patient != null)
                return patient;
            patient = new TCGAPatient(name);
            m_Patients.put(name, patient);
            return patient;
        }
    }

    private final String m_PatientId;

    private TCGAPatient(String pPatientId) {
        m_PatientId = pPatientId;
    }

    public String getPatientId() {
        return m_PatientId;
    }
}
