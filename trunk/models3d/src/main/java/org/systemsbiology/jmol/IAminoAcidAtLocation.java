package org.systemsbiology.jmol;

import org.systemsbiology.xtandem.*;
import org.systemsbiology.xtandem.fragmentation.*;
import org.systemsbiology.xtandem.peptide.*;

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

    public void setDiSulphideBond(boolean doit);

    public boolean isPotentialCleavage();

    public boolean isDetected();

    public boolean isSometimesMissedCleavage();

    public UniprotFeatureType getStructure();

    public void setStructure(final UniprotFeatureType structure);

    public ProteinAminoAcid getProteinAminoAcid();

    public void setLocation(final int location);

}
