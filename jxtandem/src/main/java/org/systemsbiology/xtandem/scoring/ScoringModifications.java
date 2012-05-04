package org.systemsbiology.xtandem.scoring;

import org.systemsbiology.xtandem.*;
import org.systemsbiology.xtandem.peptide.*;

import java.util.*;

/**
 * org.systemsbiology.xtandem.scoring.ScoringModifications
 *
 * @author Steve Lewis
 * @date Jan 11, 2011
 */
public class ScoringModifications {
    public static ScoringModifications[] EMPTY_ARRAY = {};
    public static Class THIS_CLASS = ScoringModifications.class;

    private float m_CTerminalModificationMass;
    private float m_NTerminalModificationMass;
    private float m_CTerminalCharge;
    private float m_NTerminalCharge;
    private String[] m_ResidueModifications;
    private String m_ResiduePotentialModifications;
    private PeptideModification[] m_Modifications;

    public ScoringModifications(IParameterHolder holder) {
        m_NTerminalModificationMass = holder.getFloatParameter(
                "protein, N-terminal residue modification mass", 0);
        m_CTerminalModificationMass = holder.getFloatParameter(
                "protein, C-terminal residue modification mass", 0);
        m_NTerminalCharge = holder.getFloatParameter("protein, cleavage N-terminal mass change", 0);
        m_CTerminalCharge = holder.getFloatParameter("protein, cleavage C-terminal mass change", 0);

        m_ResidueModifications = holder.getIndexedParameters("residue, modification mass");

        m_ResiduePotentialModifications = holder.getParameter("residue, potential modification mass");

        buildModifications();
    }

    protected void buildModifications() {
        if (m_Modifications != null && m_Modifications.length > 0)
            return;

        List<PeptideModification> holder = new ArrayList<PeptideModification>();

        if (m_ResiduePotentialModifications != null && m_ResiduePotentialModifications.length() > 0) {
            PeptideModification[] peptideModifications = PeptideModification.fromListString(m_ResiduePotentialModifications, PeptideModificationRestriction.Global, false);
            holder.addAll(Arrays.asList(peptideModifications));
        }
        holder.add(PeptideModification.CYSTEIN_MODIFICATION);

        m_Modifications = new PeptideModification[holder.size()];
        holder.toArray(m_Modifications);
    }


    public PeptideModification[] getModifications() {
        return m_Modifications;
    }

    public float getCTerminalModificationMass() {
        return m_CTerminalModificationMass;
    }

    public float getCTerminalCharge() {
        return m_CTerminalCharge;
    }

    public float getNTerminalModificationMass() {
        return m_NTerminalModificationMass;
    }

    public float getNTerminalCharge() {
        return m_NTerminalCharge;
    }

    public String[] getResidueModifications() {
        return m_ResidueModifications;
    }

    public String getResiduePotentialModifications() {
        return m_ResiduePotentialModifications;
    }
}
