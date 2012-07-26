package org.systemsbiology.asa;

import org.systemsbiology.jmol.*;

import java.util.*;

/**
 * org.systemsbiology.asa.AsaSubunit
 * User: steven
 * Date: 7/12/12
 */
public class AsaSubunit implements Comparable<AsaSubunit> {
    public static final AsaSubunit[] EMPTY_ARRAY = {};

    public static String buildSubUnitString(String chainId, String resType, int resNum) {
        return resType + resNum + ":" + chainId;
    }

    private final ChainEnum m_ChainId;
    private final String m_ResType;
    private final int m_Location;
    private final List<AsaAtom> m_Atoms = new ArrayList<AsaAtom>();

    public AsaSubunit(ChainEnum chainId, String resType, int resNum) {
        m_ChainId = chainId;
        m_ResType = resType;
        m_Location = resNum;
    }

    public boolean isAccessible() {
        if (m_Atoms.isEmpty())
            return false;
        for (AsaAtom atom : m_Atoms) {
            if (atom.isAccessible())
                return true;
        }
        return false;
    }

    @Override
    public int compareTo(AsaSubunit o) {
        if (getChainId().compareTo(o.getChainId()) != 0)
            return getChainId().compareTo(o.getChainId());
          if (getLocation() != o.getLocation())
            return getLocation() < o.getLocation() ? -1 : 1;
        return toString().compareTo(o.toString());
    }

    public double getAccessibleArea() {
        double ret = 0;
        for (AsaAtom atom : m_Atoms) {
            if (atom.isAccessible())
                ret += atom.getAccessibleArea();
        }
        return ret;
    }

    public AsaAtom[] getAtoms() {
        return m_Atoms.toArray(AsaAtom.EMPTY_ARRAY);
    }


    public void accumulateAtoms(Collection<AsaAtom> c) {
        c.addAll(m_Atoms);
    }

    public ChainEnum getChainId() {
        return m_ChainId;
    }

    public String getResType() {
        return m_ResType;
    }

    public int getLocation() {
        return m_Location;
    }

    @Override
    public String toString() {
        return buildSubUnitString(getChainId().toString(), getResType(), getLocation());
    }

    public void addAtom(AsaAtom added) {
        m_Atoms.add(added);
    }
}
