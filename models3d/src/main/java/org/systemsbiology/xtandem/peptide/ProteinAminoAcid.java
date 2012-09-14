package org.systemsbiology.xtandem.peptide;

import org.biojava.bio.symbol.*;
import org.systemsbiology.jmol.*;
import org.systemsbiology.xtandem.*;
import org.systemsbiology.xtandem.fragmentation.*;

import java.util.*;

/**
 * org.systemsbiology.xtandem.peptide.ProteinAminoAcid
 * User: steven
 * Date: 9/10/12
 */
public class ProteinAminoAcid implements Comparable<ProteinAminoAcid>,IAminoAcidAtLocation {
    public static final ProteinAminoAcid[] EMPTY_ARRAY = {};

    private final FastaAminoAcid m_AminoAcid;
    private int m_Location;
    private ChainEnum  m_Chain;
    private boolean m_Detected;
    private boolean m_PotentialCleavage;
    private boolean m_MissedCleavage;
    private boolean m_ObservedCleavage;
    private final Set<UniprotFeatureType> m_Features = new HashSet<UniprotFeatureType>();

    public ProteinAminoAcid(FastaAminoAcid aminoAcid, int location) {
       this(null,aminoAcid,location);
    }


    public ProteinAminoAcid(ChainEnum chain,FastaAminoAcid aminoAcid) {
        this(chain,aminoAcid,0);
     }

    public ProteinAminoAcid(ChainEnum chain,FastaAminoAcid aminoAcid, int location) {
        m_AminoAcid = aminoAcid;
        m_Location = location;
        m_Chain = chain;
    }


    public boolean isPotentialCleavage() {
        return m_PotentialCleavage;
    }

    public void setPotentialCleavage(boolean potentialCleavage) {
        m_PotentialCleavage = potentialCleavage;
    }

    public boolean isMissedCleavage() {
        return m_MissedCleavage;
    }

    public void setMissedCleavage(boolean missedCleavage) {
        m_MissedCleavage = missedCleavage;
    }

    public boolean isObservedCleavage() {
        return m_ObservedCleavage;
    }

    public void setObservedCleavage(boolean observedCleavage) {
        m_ObservedCleavage = observedCleavage;
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


    public UniprotFeatureType getStructure() {
        for (UniprotFeatureType type  : m_Features) {
            if(type.isStructure())
                return type;
         }
        return null;
    }

    public void setStructure(final UniprotFeatureType structure) {
        // only one allowed
        UniprotFeatureType prev = getStructure();
        if(prev != null)  {
            if(prev == structure)
                return;
        }
        else {
            addFeature(structure);
        }
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
    public ChainEnum getChain() {
        return m_Chain;
    }

    public void setChain(final ChainEnum chain) {
        m_Chain = chain;
    }


    public void setLocation(final int location) {
        m_Location = location;
    }

    @Override
    public boolean isDiSulphideBond() {
        return hasFeature(UniprotFeatureType.DISULFID);
    }

    @Override
    public boolean isSometimesMissedCleavage() {
        return isMissedCleavage();
    }

    @Override
    public int compareTo(ProteinAminoAcid o) {
        int loc = getLocation();
        int oloc = o.getLocation();
        if(oloc != loc)
            return loc < oloc ? -1 : 1;
        return getAminoAcid().compareTo(o.getAminoAcid());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getAminoAcid().toString());
        if(!m_Features.isEmpty()) {
            sb.append("[");
            for (UniprotFeatureType f  : m_Features) {
                sb.append(f.toString().charAt(0));
            }
            sb.append("]");
        }
        return sb.toString();

    }
}
