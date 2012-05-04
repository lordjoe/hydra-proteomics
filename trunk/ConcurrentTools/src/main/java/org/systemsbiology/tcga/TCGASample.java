package org.systemsbiology.tcga;

/**
 * org.systemsbiology.tcga.TCGASample
 * written by Steve Lewis
 * on Apr 9, 2010
 */
public class TCGASample
{
    public static final TCGASample[] EMPTY_ARRAY = {};
    public static final Class THIS_CLASS = TCGASample.class;

    private final String m_Name;
    private final TCGACollectionSite m_Site;
    private final TCGACenter m_Center;
    private final TCGAPatient m_Patient;
    private final TCGASampleType m_Type;

    public TCGASample(String pName) {
        m_Name = pName;
        m_Site = TCGAUtilities.siteFromName(pName);
        m_Center = TCGAUtilities.centerFromName(pName);
        m_Patient = TCGAUtilities.patientFromName(pName);
        m_Type = TCGAUtilities.sampleTypeFromString(pName);
    }

    public String getName() {
        return m_Name;
    }

    public TCGAPatient getPatient() {
        return m_Patient;
    }

    public TCGASampleType getType() {
        return m_Type;
    }

    public TCGACollectionSite getSite() {
        return m_Site;
    }

    public TCGACenter getCenter() {
        return m_Center;
    }
}
