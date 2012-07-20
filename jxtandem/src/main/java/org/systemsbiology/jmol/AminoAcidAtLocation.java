package org.systemsbiology.jmol;

import org.systemsbiology.asa.*;
import org.systemsbiology.xtandem.*;

import java.util.*;

/**
 * org.systemsbiology.jmol.AminoAcidAtLocation
 * User: steven
 * Date: 5/15/12
 */
public class AminoAcidAtLocation extends AsaSubunit {
    public static final AminoAcidAtLocation[] EMPTY_ARRAY = {};

    /**
     * convert locations to a sequence
     * @param locs !null locs
     * @return   represented sequence
     */
    public static ChainEnum[] toChains(AminoAcidAtLocation[] locs)
    {
        Set<ChainEnum> holder = new HashSet<ChainEnum>();

        for (int i = 0; i < locs.length; i++) {
            AminoAcidAtLocation loc = locs[i];
            holder.add(loc.getChain());
        }
        ChainEnum[] ret = new ChainEnum[holder.size()];
        holder.toArray(ret);
        return ret;
    }
    /**
     * convert locations to a sequence
     * @param locs !null locs
     * @return   represented sequence
     */
    public static String toSequence(AminoAcidAtLocation[] locs)
    {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < locs.length; i++) {
            AminoAcidAtLocation loc = locs[i];
            sb.append(loc.getAminoAcid().toString());
        }
        return sb.toString();
    }

    private final FastaAminoAcid m_AminoAcid;
    private int m_Location = -1;
    private SecondaryStructure m_Structure = SecondaryStructure.NONE;
    private final ChainEnum m_Chain;
    private boolean m_DiSulphideBond;
    private boolean m_SometimesMissedCleavage;

    public AminoAcidAtLocation(ChainEnum chain,FastaAminoAcid aminoAcid  ) {
        super(chain,aminoAcid.toString(),0);
        m_AminoAcid = aminoAcid;
        m_Chain = chain;
    }
//
//    public AminoAcidAtLocation(FastaAminoAcid aminoAcid, int location) {
//        this(aminoAcid,location,ChainEnum.A);
//    }


    public SecondaryStructure getStructure() {
        return m_Structure;
    }

    public void setStructure(final SecondaryStructure structure) {
        m_Structure = structure;
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

    public void setLocation(int location) {
        if(m_Location == -1)
            m_Location = location;
        else   {
            if( m_Location == location)
                return;
            throw new IllegalStateException("Location already set");
        }
    }

    @Override
    public String toString() {
        return  m_AminoAcid.getAbbreviation() + m_Location;
    }

    public boolean isDiSulphideBond() {
        return m_DiSulphideBond;
    }

    public void setDiSulphideBond(boolean diSulphideBond) {
        m_DiSulphideBond = diSulphideBond;
    }

    public boolean isSometimesMissedCleavage() {
        return m_SometimesMissedCleavage;
    }

    public void setSometimesMissedCleavage(boolean sometimesMissedCleavage) {
        m_SometimesMissedCleavage = sometimesMissedCleavage;
    }

    //
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//
//        AminoAcidAtLocation that = (AminoAcidAtLocation) o;
//
//        if (m_Location != that.m_Location) return false;
//        if (m_AminoAcid != that.m_AminoAcid) return false;
//        if (m_Chain != that.m_Chain) return false;
//
//        return true;
//    }
//
//    @Override
//    public int hashCode() {
//        int result = m_AminoAcid != null ? m_AminoAcid.hashCode() : 0;
//        result = 31 * result + m_Location;
//        result = 31 * result + m_Chain.hashCode();
//        return result;
//    }
}
