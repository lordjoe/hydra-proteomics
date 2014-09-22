package org.systemsbiology.tcga;

import java.util.*;

/**
 * org.systemsbiology.tcga.TCGAPatient
 * written by Steve Lewis
 * on Apr 9, 2010
 */
public class TCGACollectionSite
{
    public static final TCGACollectionSite[] EMPTY_ARRAY = {};
    public static final Class THIS_CLASS = TCGACollectionSite.class;

    private static final Map<String, TCGACollectionSite> m_Patients =
            new HashMap<String, TCGACollectionSite>();

    public static TCGACollectionSite getCollectionSite(String name) {
        synchronized (m_Patients) {
            TCGACollectionSite patient = m_Patients.get(name);
            if (patient != null)
                return patient;
            patient = new TCGACollectionSite(name);
            m_Patients.put(name, patient);
            return patient;
        }
    }

    private final String m_PatientId;

    private TCGACollectionSite(String pPatientId) {
        m_PatientId = pPatientId;
    }

    public String getPatientId() {
        return m_PatientId;
    }
}