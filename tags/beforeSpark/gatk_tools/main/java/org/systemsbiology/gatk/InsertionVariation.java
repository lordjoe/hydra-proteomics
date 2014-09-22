package org.systemsbiology.gatk;

/**
 * org.systemsbiology.gatk.SNPVariation
 * User: steven
 * Date: 6/12/12
 */
public class InsertionVariation extends GeneVariant {
    public static final InsertionVariation[] EMPTY_ARRAY = {};

    private final DNABase[] m_Altered;
    private final DNABase m_Reference;

    public InsertionVariation(GeneLocation location, double score, DNABase reference, DNABase[] altered, String annotation) {
        super(VariantType.DEL, location,score, annotation);
        m_Reference = reference;
        m_Altered = altered;
    }

    public InsertionVariation(GeneLocation location, double score, DNABase reference, DNABase[] altered) {
        this(location,score, reference, altered, location.toString() + "|+" +    (altered.length - 1));

    }

    public DNABase[] getAltered() {
        return m_Altered;
    }

    public DNABase  getReference() {
        return m_Reference;
    }

    public int getLength() {
        return m_Altered.length;
    }

    @Override
    public GeneVariant asSample() {
        return new InsertionVariation(getLocation(),getScore(),getReference(),getAltered(),"100");
    }



    @Override
    public String toString() {
        return getLocation().toString()  + "|" + getReference() + "|-" +    getLength();

    }
}
