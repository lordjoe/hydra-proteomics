package org.systemsbiology.chromosome;

/**
 * org.systemsbiology.chromosome.HumanChromosome
 * written by Steve Lewis
 * on Apr 7, 2010
 * NOTE Package private to force use of interface
 */
// Deliberately package private
enum HumanChromosome implements IChromosome
{

    chr1(1,247249719),
    //   chr1_random,
    chr2(2,242951149),
    //   chr2_random,
    chr3(3,199501827),
    //   chr3_random(0),
    chr4(4,191273063),
    //   chr4_random(0),
    chr5(5,180857866),
    //   chr5_random(0),
    chr6(6,170899992),
    //   chr6_random(0),
    chr7(7,158821424),
    //   chr7_random(0),
    chr8(8,146274826),
    //   chr8_random(0),
    chr9(9,140273252),
    //   chr9_random(0),
    chr10(10,135374737),
    //   chr10_random(0),
    chr11(11,134452384),
    //   chr11_random(0),
    chr12(12,132349534),
    chr13(13,114142980),
//    chr13_random(0),
    chr14(14,106368585),
    chr15(15,100338915),
//    chr15_random(0),
    chr16(16,88827254),
//    chr16_random(0),
    chr17(17,78774742),
//    chr17_random(0),
    chr18(18,76117153),
//    chr18_random(0),
    chr19(19,63811651),
//    chr19_random(0),
    chr20(20,62435964),
    chr21(21,46944323),
//    chr21_random(0),
    chr22(22,49691432),
//    chr22_random(0),
    chrX(23,154913754),
//    chrX_random(0),
    chrY(24,57772954),
    chrM(25,16550);

    public static final HumanChromosome[] STANDARD_CHROMOSOMES =
            {
                    chr1, chr2, chr3, chr4, chr5,
                    chr6, chr7, chr8, chr9, chr10,
                    chr11, chr12, chr13, chr14, chr15,
                    chr16, chr17, chr18, chr19, chr20,
                    chr21,  chr22, chrX, chrY
            };

    public static final HumanChromosome[] EMPTY_ARRAY = {};
    public static final Class THIS_CLASS = HumanChromosome.class;

    private final int m_Length;
    private final short m_Index;
    private final String m_AlternateName;

    private HumanChromosome(int index,int pLength) {
        m_Index = (short)index;
        m_Length = pLength;
        m_AlternateName = toString().replace("chr", "hs");
    }

    public String getAlternateName() {
        return m_AlternateName;
    }

    public int getLength() {
        return m_Length;
    }

    @Override
    public short getIndex() {
        return m_Index;
    }




}