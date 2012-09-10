package org.systemsbiology.xtandem.peptide;

import org.biojava.bio.symbol.*;
import org.systemsbiology.xtandem.*;
import org.systemsbiology.xtandem.fragmentation.*;

import java.util.*;

/**
 * org.systemsbiology.xtandem.peptide.ProteinAminoAcid
 * User: steven
 * Date: 9/10/12
 */
public class ProteinAminoAcid implements Comparable<ProteinAminoAcid> {
    public static final ProteinAminoAcid[] EMPTY_ARRAY = {};

    private final FastaAminoAcid m_AminoAcid;
    private final int m_Location;
    private boolean m_Detected;
    private final Set<UniprotFeatureType> m_Features = new HashSet<UniprotFeatureType>();

    public ProteinAminoAcid(FastaAminoAcid aminoAcid, int location) {
        m_AminoAcid = aminoAcid;
        m_Location = location;
    }

    public boolean isDetected() {
        return m_Detected;
    }

    public void setDetected(boolean detected) {
        m_Detected = detected;
    }

    public FastaAminoAcid getAminoAcid() {
        return m_AminoAcid;
    }

    public int getLocation() {
        return m_Location;
    }

    public void addFeature(UniprotFeatureType ft)
    {
        m_Features.add(ft);
    }


    public void removeFeature(UniprotFeatureType ft)
    {
        m_Features.remove(ft);
    }


    public boolean hasFeature(UniprotFeatureType ft)
    {
        return m_Features.contains(ft);
    }


    public UniprotFeatureType[] getFeatures( )
    {
        UniprotFeatureType[] uniprotFeatureTypes = m_Features.toArray(UniprotFeatureType.EMPTY_ARRAY);
        Arrays.sort(uniprotFeatureTypes);
        return uniprotFeatureTypes;
    }


    public boolean inLocation(Location loc)
    {
        throw new UnsupportedOperationException("Fix This"); // ToDo
    }

    @Override
    public int compareTo(ProteinAminoAcid o) {
        int loc = getLocation();
        int oloc = o.getLocation();
        if(oloc != loc)
            return loc < oloc ? -1 : 1;
        return getAminoAcid().compareTo(o.getAminoAcid());
    }
}