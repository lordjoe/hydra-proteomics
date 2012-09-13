package org.systemsbiology.jmol;

import org.systemsbiology.xtandem.*;
import org.systemsbiology.xtandem.peptide.*;

import java.util.*;

/**
 * org.systemsbiology.jmol.SequenceChainMap
 * User: steven
 * Date: 7/13/12
 */
public class SequenceChainMap  implements Comparable<SequenceChainMap> {
    public static final SequenceChainMap[] EMPTY_ARRAY = {};

    private final Protein m_Protein;
    private final int m_Pos;
    private final FastaAminoAcid m_AA;
    private final Map<ChainEnum,IAminoAcidAtLocation> m_ChainMappings = new HashMap<ChainEnum, IAminoAcidAtLocation>();

    public SequenceChainMap(Protein protein, int pos, FastaAminoAcid AA) {
        m_Protein = protein;
        m_Pos = pos;
        m_AA = AA;
    }

    public Protein getProtein() {
        return m_Protein;
    }

    public int getPos() {
        return m_Pos;
    }

    public FastaAminoAcid getAA() {
        return m_AA;
    }

    public IAminoAcidAtLocation getChainMapping(ChainEnum chain)
      {
         return m_ChainMappings.get(chain);
      }
    public AminoAcidAtLocation[] getChainMappings()
      {
         return m_ChainMappings.values().toArray(AminoAcidAtLocation.EMPTY_ARRAY);
      }

    public void addChainMapping(ChainEnum chain,IAminoAcidAtLocation added)
    {
        m_ChainMappings.put(chain,added);
    }


    @Override
    public int compareTo(SequenceChainMap o) {
        if(getPos() != o.getPos())
            return  getPos() < o.getPos() ? -1 : 1;
        return 0;
    }

    @Override
    public String toString() {
        return  getAA().toString()  + getPos()  ;
    }

}
