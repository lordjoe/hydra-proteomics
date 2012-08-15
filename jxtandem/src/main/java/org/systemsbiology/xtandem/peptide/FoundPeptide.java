package org.systemsbiology.xtandem.peptide;

/**
* org.systemsbiology.xtandem.peptide.FoundPeptide
* User: Steve
* Date: 8/15/12
*/
public class FoundPeptide {
    private final IPolypeptide m_Peptide;
    private final String m_ProteinId;
    private final int m_Charge;

    public FoundPeptide(final IPolypeptide peptide, final String proteinId, int charge) {
        m_Peptide = peptide;
        m_ProteinId = proteinId;
        m_Charge = charge;
    }

    public FoundPeptide(final String peptide, final String proteinId) {
          this(ParseConcensus.toPolyPeptide(peptide),proteinId, ParseConcensus.toCharge(peptide));
      }

    public IPolypeptide getPeptide() {
        return m_Peptide;
    }

    public String getProteinId() {
        return m_ProteinId;
    }

    public int getCharge() {
        return m_Charge;
    }

    @Override
    public String toString() {
        return
                 m_Peptide.toString() +  "\t"
                  + m_ProteinId + "\t"
                  + m_Charge
                 ;
    }
}
