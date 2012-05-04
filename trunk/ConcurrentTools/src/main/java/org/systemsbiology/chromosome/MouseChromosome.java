package org.systemsbiology.chromosome;

/**
 * org.systemsbiology.chromosome.HumanChromosome
 * written by Steve Lewis
 * on Apr 7, 2010
 * NOTE Package private to force use of interface
 */
// Deliberately package private
enum MouseChromosome implements IChromosome
{

    chr1(1,201139347),
    //   chr1_random,
    chr2(2,185383055),
    //   chr2_random,
    chr3(3,162791785),
    //   chr3_random(0),
    chr4(4,158742729),
    //   chr4_random(0),
    chr5(5,155588011),
    //   chr5_random(0),
    chr6(6,152507384),
    //   chr6_random(0),
    chr7(7,155575051),
    //   chr7_random(0),
    chr8(8,134373655),
    //   chr8_random(0),
    chr9(9,126557702),
    //   chr9_random(0),
    chr10(10,132593128),
    //   chr10_random(0),
    chr11(11,124280741),
    //   chr11_random(0),
    chr12(12,123682688),
    chr13(13,122690006),
//    chr13_random(0),
    chr14(14,127698769),
    chr15(15,105564881),
//    chr15_random(0),
    chr16(16,100285540),
//    chr16_random(0),
    chr17(17,97178112),
//    chr17_random(0),
    chr18(18,92587479),
//    chr18_random(0),
    chr19(19,62569286),
//    chr19_random(0),
 //    chr22_random(0),
    chrX(20,169983308),
//    chrX_random(0),
    chrY(21,16220613),
    chrM(22,16631);

    public static final MouseChromosome[] STANDARD_CHROMOSOMES =
            {
                    chr1, chr2, chr3, chr4, chr5,
                    chr6, chr7, chr8, chr9, chr10,
                    chr11, chr12, chr13, chr14, chr15,
                    chr16, chr17, chr18, chr19,  chrX, chrY
            };

    public static final MouseChromosome[] EMPTY_ARRAY = {};
    public static final Class THIS_CLASS = MouseChromosome.class;

    private final int m_Length;
    private final short m_Index;
    private final String m_AlternateName;

    private MouseChromosome(int index,int pLength) {
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