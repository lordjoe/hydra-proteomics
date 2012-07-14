package org.systemsbiology.jmol;

import org.systemsbiology.xtandem.*;

/**
 * org.systemsbiology.jmol.ChainPosition
 * User: steven
 * Date: 7/13/12
 */
public class ChainPosition implements Comparable<ChainPosition> {
    public static final ChainPosition[] EMPTY_ARRAY = {};

    private final ChainEnum m_Chain;
    private final int m_Pos;
    private final FastaAminoAcid m_AA;

    public ChainPosition(ChainEnum chain, int pos, FastaAminoAcid AA) {
        m_Chain = chain;
        m_Pos = pos;
        m_AA = AA;
    }

    public ChainEnum getChain() {
        return m_Chain;
    }

    public int getPos() {
        return m_Pos;
    }

    public FastaAminoAcid getAA() {
        return m_AA;
    }

    @Override
    public int compareTo(ChainPosition o) {
        if(getChain() != o.getChain())
            return getChain().compareTo(o.getChain());
        if(getPos() != o.getPos())
            return  getPos() < o.getPos() ? -1 : 1;
        return 0;
    }

    @Override
    public String toString() {
        return  getAA().toString()  + getPos() + ":" + getChain().toString();
    }
}
