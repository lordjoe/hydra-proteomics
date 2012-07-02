package org.systemsbiology.jmol;

import org.systemsbiology.xtandem.*;

/**
 * org.systemsbiology.jmol.AminoAcidAtLocation
 * User: steven
 * Date: 5/15/12
 */
public class AminoAcidAtLocation {
    public static final AminoAcidAtLocation[] EMPTY_ARRAY = {};

    private final FastaAminoAcid m_AminoAcid;
    private final int m_Location;
    private final ChainEnum m_Chain;

    public AminoAcidAtLocation(FastaAminoAcid aminoAcid, int location,ChainEnum chain) {
        m_AminoAcid = aminoAcid;
        m_Location = location;
        m_Chain = chain;
    }

    public AminoAcidAtLocation(FastaAminoAcid aminoAcid, int location) {
        this(aminoAcid,location,ChainEnum.A);
    }

    public ChainEnum getChain() {
        return m_Chain;
    }

    public FastaAminoAcid getAminoAcid() {
        return m_AminoAcid;
    }

    public int getLocation() {
        return m_Location;
    }

    @Override
    public String toString() {
        return  m_AminoAcid.getAbbreviation() + m_Location;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AminoAcidAtLocation that = (AminoAcidAtLocation) o;

        if (m_Location != that.m_Location) return false;
        if (m_AminoAcid != that.m_AminoAcid) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = m_AminoAcid != null ? m_AminoAcid.hashCode() : 0;
        result = 31 * result + m_Location;
        return result;
    }
}
