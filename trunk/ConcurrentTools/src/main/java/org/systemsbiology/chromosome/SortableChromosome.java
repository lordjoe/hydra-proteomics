package org.systemsbiology.chromosome;

import java.util.*;

/**
 * org.systemsbiology.chromosome.SortableChromosome
 * Special version of   IChromosome guaranteed to sort as strings
 * written by Steve Lewis
 * on Apr 7, 2010
 * NOTE Package private to force use of interface
 */
// Deliberately package private
public enum SortableChromosome implements IChromosome
{

    chr01(1,247249719),
    //   chr1_random,
    chr02(2,242951149),
    //   chr2_random,
    chr03(3,199501827),
    //   chr3_random(0),
    chr04(4,191273063),
    //   chr4_random(0),
    chr05(5,180857866),
    //   chr5_random(0),
    chr06(6,170899992),
    //   chr6_random(0),
    chr07(7,158821424),
    //   chr7_random(0),
    chr08(8,146274826),
    //   chr8_random(0),
    chr09(9,140273252),
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

    public static final SortableChromosome[] STANDARD_CHROMOSOMES =
            {
                    chr01, chr02, chr03, chr04, chr05,
                    chr06, chr07, chr08, chr09, chr10,
                    chr11, chr12, chr13, chr14, chr15,
                    chr16, chr17, chr18, chr19, chr20,
                    chr21,  chr22, chrX, chrY
            };

    public static final SortableChromosome[] EMPTY_ARRAY = {};
    public static final Class THIS_CLASS = SortableChromosome.class;

    private final int m_Length;
    private final short m_Index;
    private final String m_AlternateName;

    private SortableChromosome(int index,int pLength) {
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

    public static SortableChromosome getSortableChromosome(IChromosome chr)
    {
        if(chr instanceof SortableChromosome)
            return (SortableChromosome)chr;
        return getSortableChromosome(chr.toString());
    }

    public static SortableChromosome getSortableChromosome(String chr)
    {
         Map<String, SortableChromosome> map = getMap();
        return map.get(chr);
    }

    private static Map<String,SortableChromosome>  gStringToItem ;

    private static Map<String,SortableChromosome> getMap()
    {
        if(gStringToItem == null)  {
            buildMap();
        }
        return gStringToItem;
    }

    private static void buildMap()
    {
        Map<String,SortableChromosome> ret = new HashMap<String,SortableChromosome>();
        ret.put("chr1",chr01);
        ret.put("chrI",chr01);

        ret.put("chr2",chr02);
        ret.put("chrII",chr02);

        ret.put("chr3",chr03);
        ret.put("chrIII",chr03);

        ret.put("chr4",chr04);
        ret.put("chrIV",chr04);

        ret.put("chr5",chr05);
        ret.put("chrV",chr05);

          ret.put("chr6",chr06);
        ret.put("chrVI",chr06);

        ret.put("chr7",chr07);
        ret.put("chrVII",chr07);

        ret.put("chr8",chr08);
        ret.put("chrVIII",chr08);

        ret.put("chr92",chr09);
        ret.put("chrIX",chr02);

        ret.put("chrX",chr10);
        ret.put("chrXI",chr11);
        ret.put("chrXII",chr12);
        ret.put("chrXIII",chr13);
        ret.put("chrXIV",chr14);
        ret.put("chrXV",chr15);
        ret.put("chrXVI",chr16);

        gStringToItem = ret;

    }

}