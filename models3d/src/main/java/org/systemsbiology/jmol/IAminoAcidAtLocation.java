package org.systemsbiology.jmol;

import org.systemsbiology.xtandem.*;

/**
 * org.systemsbiology.jmol.IAminoAcidAtLocation
 * User: steven
 * Date: 9/12/12
 */
public interface IAminoAcidAtLocation {
    public static final IAminoAcidAtLocation[] EMPTY_ARRAY = {};

    public ChainEnum getChain();

    public FastaAminoAcid getAminoAcid();

    public int getLocation();

    public boolean isDiSulphideBond();

    public boolean isPotentialCleavage();

    public boolean isDetected();

    public boolean isSometimesMissedCleavage();
}
