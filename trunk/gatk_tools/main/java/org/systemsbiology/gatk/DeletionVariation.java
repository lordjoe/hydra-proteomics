package org.systemsbiology.gatk;

/**
 * org.systemsbiology.gatk.SNPVariation
 * User: steven
 * Date: 6/12/12
 */
public class DeletionVariation extends GeneVariant {
    public static final DeletionVariation[] EMPTY_ARRAY = {};

    private final DNABase[] m_Reference;
    private final DNABase m_Altered;

    public DeletionVariation(GeneLocation location, double score,DNABase[] reference,DNABase altered, String annotation) {
        super(VariantType.DEL, location,score, annotation);
        m_Reference = reference;
        m_Altered = altered;
    }

    public DeletionVariation(GeneLocation location, double score,DNABase[] reference,DNABase altered) {
        this(location,score, reference, altered, location.toString() + "|-" +    reference.length);

    }

    public DNABase getAltered() {
        return m_Altered;
    }

    public DNABase[] getReference() {
        return m_Reference;
    }

    public int getLength() {
        return m_Reference.length;
    }


    @Override
    public String toString() {
        return getLocation().toString()  + "|" + getReference() + "|-" +    getLength();

    }
}
