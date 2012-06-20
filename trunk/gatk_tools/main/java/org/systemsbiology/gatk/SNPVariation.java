package org.systemsbiology.gatk;

/**
 * org.systemsbiology.gatk.SNPVariation
 * User: steven
 * Date: 6/12/12
 */
public class SNPVariation extends GeneVariant {
    public static final SNPVariation[] EMPTY_ARRAY = {};

    private final DNABase m_Reference;
    private final DNABase m_Altered;

    public SNPVariation(  GeneLocation location,double score,DNABase reference, DNABase altered, String annotation) {
        super(VariantType.SNP, location,score, annotation);
        m_Reference = reference;
        m_Altered = altered;
    }

    public SNPVariation(  GeneLocation location,double score, DNABase reference, DNABase altered) {
        this(location,score, reference, altered, location.toString() + "|" + reference + "->" + altered);

    }

    public DNABase getReference() {
        return m_Reference;
    }

    public DNABase getAltered() {
        return m_Altered;
    }

    @Override
    public boolean equivalent(GeneVariant o) {
          if(!super.equivalent(  o))
              return false;
          SNPVariation sv = (SNPVariation)o;
          if(getAltered() != sv.getAltered())
              return false;
          return true;
      }



    @Override
    public String toString() {
        return getLocation().toString()  + "|" + getReference() + "->" + getAltered();

    }
}
