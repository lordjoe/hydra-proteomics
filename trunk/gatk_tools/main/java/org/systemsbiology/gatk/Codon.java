package org.systemsbiology.gatk;

/**
 * org.systemsbiology.gatk.Codin
 * User: steven
 * Date: 6/18/12
 */
public enum Codon {
    TTT("F"),
    TTC("F"),
    TTA("L"),
    TTG("L"),
    TCT("S"),
    TCC("S"),
    TCA("S"),
    TCG("S"),
    TAT("Y"),
    TAC("Y"),
    TAA(null),    //            TAA end
    TAG(null),   //            TAG end
    TGT("C"),
    TGC("C"),
    TGA(null),  //            TGA end
    TGG("W"),
    CTT("L"),
    CTC("L"),
    CTA("L"),
    CTG("L"),
    CCT("P"),
    CCC("P"),
    CCA("P"),
    CCG("P"),
    CAT("H"),
    CAC("H"),
    CAA("Q"),
    CAG("Q"),
    CGT("R"),
    CGC("R"),
    CGA("R"),
    CGG("R"),
    ATT("I"),
    ATC("I"),
    ATA("I"),
    ATG("M"),
    ACT("T"),
    ACC("T"),
    ACA("T"),
    ACG("T"),
    AAT("N"),
    AAC("N"),
    AAA("K"),
    AAG("K"),
    AGT("S"),
    AGC("S"),
    AGA("R"),
    AGG("R"),
    GTT("V"),
    GTC("V"),
    GTA("V"),
    GTG("V"),
    GCT("A"),
    GCC("A"),
    GCA("A"),
    GCG("A"),
    GAT("D"),
    GAC("D"),
    GAA("E"),
    GAG("E"),
    GGT("G"),
    GGC("G"),
    GGA("G"),
    GGG("G");
    
    public static final Codon[] EMPTY_ARRAY = {};
    private final String m_AminoAcid;

    Codon(String aminoAcid) {
        m_AminoAcid = aminoAcid;
    }

    public String getAminoAcid() {
        return m_AminoAcid;
    }

    public boolean isStop() {
        if(getAminoAcid() != null)
            return false;
         return true;
    }

    public boolean isStart() {
        return ATG == this;
    }
}
