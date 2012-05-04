package org.systemsbiology.chromosome;

/**
 * org.systemsbiology.chromosome.HumanChromosome
 * written by Steve Lewis
 * on Apr 7, 2010
 * NOTE Package private to force use of interface
 */
 enum YeastChromosome implements IChromosome
{


    chrI(1,230208),     // 230208, 813178, 316617, 1531918, 576869,

    //   chr1_random,
    chrII(2,813178),
    //   chr2_random,
    chrIII(3,316617),
    //   chr3_random(0),
    chrIV(4,1531918),
    //   chr4_random(0),
    chrV(5,576869),
    //   chr5_random(0),
    chrVI(6,270148),         //  270148, 1090947, 562643, 439885, 745745,

    //   chr6_random(0),
    chrVII(7,71090947),
    //   chr7_random(0),
    chrVIII(8,562643),
    //   chr8_random(0),
    chrIX(9,439885),
    //   chr9_random(0),
    chrX(10,745745),
    //   chr10_random(0),
    chrXI(11,666454),              //  666454, 1078175, 924429, 784333, 1091289, 948062

    //   chr11_random(0),
    chrXII(12,1078175),
    chrXIII(13,924429),
//    chr13_random(0),
    chrXIV(14,784333),

    chrXV(15,1091289),

    chrXVI(16,948062);

    // All files string
    // chrI.sam  chrII.sam  chrIII.sam  chrIV.sam  chrV.sam  chrVI.sam  chrVII.sam  chrVIII.sam  chrIX.sam  chrX.sam  chrXI.sam  chrXII.sam  chrXIII.sam  chrXIV.sam  chrXV.sam  chrXVI.sam
    public static final YeastChromosome[] STANDARD_CHROMOSOMES =
            {
                    chrI, chrII, chrIII, chrIV, chrV,
                    chrVI, chrVII, chrVIII, chrIX, chrX,
                    chrXI, chrXII, chrXIII, chrXIV, chrXV, chrXVI

            };

    public static final YeastChromosome[] EMPTY_ARRAY = {};
    public static final Class THIS_CLASS = YeastChromosome.class;

    private final int m_Length;
    private final short m_Index;
    private final String m_AlternateName;

    private YeastChromosome(int index,int pLength) {
        m_Length = pLength;
        m_Index = (short)index;
        m_AlternateName = toString();
    }

    public String getAlternateName() {
        return m_AlternateName;
    }

    public int getLength() {
        return m_Length;
    }

    public short getIndex() {
        return m_Index;
    }
}