package org.systemsbiology.xtandem.peptide;

import org.systemsbiology.xtandem.sax.*;

import java.util.*;

/**
 * org.systemsbiology.xtandem.peptide.ProteomicExperiment
 * User: Steve
 * Date: 3/15/13
 */
public class ProteomicExperiment {
    public static final ProteomicExperiment[] EMPTY_ARRAY = {};

    private int m_NumberNotAdded;

    private final Map<String, ProtXMLDetection> m_UniprotIdToUniprot = new HashMap<String, ProtXMLDetection>();

    public ProteomicExperiment() {
    }

    public Map<String, ProtXMLDetection> getUniprotIdToUniprot() {
        return m_UniprotIdToUniprot;
    }

    public void addDetection(ProtXMLDetection added)   {
        if(added.getProbability() < 0.1)     {
            m_NumberNotAdded++;
            return;
        }
        if(added.getFractionCoverage() < 0.001)     {
                    m_NumberNotAdded++;
                    return;
                }
         m_UniprotIdToUniprot.put(added.getName(),added) ;
    }

    public String[] getProteinIds()
    {
        String[] ret = new String[m_UniprotIdToUniprot.size()];
        int index = 0;
        m_UniprotIdToUniprot.keySet().toArray(ret);
        Arrays.sort(ret);
        return ret;
    }

    public  ProtXMLDetection getDetection(String key) {
        return m_UniprotIdToUniprot.get(key);
    }

    public ProtXMLDetection[] getDetections()
    {
        String[] ids = getProteinIds();
        ProtXMLDetection[] ret = new ProtXMLDetection[m_UniprotIdToUniprot.size()];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = getDetection(ids[i]);

        }
        return ret;
    }


}
