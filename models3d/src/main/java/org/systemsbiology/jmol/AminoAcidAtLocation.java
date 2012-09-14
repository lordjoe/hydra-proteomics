package org.systemsbiology.jmol;

import org.systemsbiology.asa.*;
import org.systemsbiology.xtandem.*;
import org.systemsbiology.xtandem.fragmentation.*;
import org.systemsbiology.xtandem.peptide.*;

import java.util.*;

/**
 * org.systemsbiology.jmol.AminoAcidAtLocation
 * User: steven
 * Date: 5/15/12
 */
public class AminoAcidAtLocation extends AsaSubunit implements IAminoAcidAtLocation {
    public static final AminoAcidAtLocation[] EMPTY_ARRAY = {};

    /**
     * convert locations to a sequence
     * @param locs !null locs
     * @return   represented sequence
     */
    public static ChainEnum[] toChains(IAminoAcidAtLocation[] locs)
    {
        Set<ChainEnum> holder = new HashSet<ChainEnum>();

        for (int i = 0; i < locs.length; i++) {
            IAminoAcidAtLocation loc = locs[i];
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
    public static String toSequence(IAminoAcidAtLocation[] locs)
    {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < locs.length; i++) {
            IAminoAcidAtLocation loc = locs[i];
            sb.append(loc.getAminoAcid().toString());
        }
        return sb.toString();
    }
//
//    private final FastaAminoAcid m_AminoAcid;
//    private int m_Location = -1;
//    private SecondaryStructure m_Structure = SecondaryStructure.NONE;
//    private final ChainEnum m_Chain;
//    private boolean m_DiSulphideBond;
//    private boolean m_PotentialCleavage;
//    private boolean m_Detected;
//    private boolean m_SometimesMissedCleavage;

    private final ProteinAminoAcid m_ProteinAminoAcid;
    public AminoAcidAtLocation(ChainEnum chain,ProteinAminoAcid pa) {
        super(chain,pa.getAminoAcid().toString(),0);
        m_ProteinAminoAcid  = pa;
    }
//
//    public AminoAcidAtLocation(FastaAminoAcid aminoAcid, int location) {
//        this(aminoAcid,location,ChainEnum.A);
//    }


    public boolean isPotentialCleavage() {
        return m_ProteinAminoAcid.isPotentialCleavage();
    }



    public boolean isDetected() {
        return  m_ProteinAminoAcid.isDetected();
    }


    public UniprotFeatureType getStructure() {
        return  m_ProteinAminoAcid.getStructure();
    }

    public void setStructure(final UniprotFeatureType structure) {
        m_ProteinAminoAcid.setStructure(structure);
    }

    @Override
    public ChainEnum getChain() {
        return m_ProteinAminoAcid.getChain();
    }

    @Override
    public FastaAminoAcid getAminoAcid() {
        return  m_ProteinAminoAcid.getAminoAcid();
    }

    @Override
    public int getLocation() {
        return m_ProteinAminoAcid.getLocation();
    }

    public void setLocation(int location) {
        if(getLocation() == -1)
            m_ProteinAminoAcid.setLocation(location);
        else   {
            if( getLocation() == location)
                return;
            throw new IllegalStateException("Location already set");
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getAminoAcid().getAbbreviation());
        sb.append(Integer.toString(getLocation())) ;
        ChainEnum chain = getChain();
        if(chain != null)
            sb.append(":" + chain) ;
        return sb.toString();
       }

    @Override
    public boolean isDiSulphideBond() {
        return   m_ProteinAminoAcid.isDiSulphideBond();
    }

    public void setDiSulphideBond(boolean doIt)  {
        ProteinAminoAcid pa = getProteinAminoAcid();
        if(doIt) {
            if(!pa.hasFeature(UniprotFeatureType.DISULFID))
                pa.addFeature(UniprotFeatureType.DISULFID);
        }
        else {
            pa.removeFeature(UniprotFeatureType.DISULFID);
        }
    }



    @Override
    public boolean isSometimesMissedCleavage() {
        return   m_ProteinAminoAcid.isSometimesMissedCleavage();
    }

    public ProteinAminoAcid getProteinAminoAcid() {
        return m_ProteinAminoAcid;
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
