package org.systemsbiology.chromosome;

import java.util.*;

/**
 * org.systemsbiology.chromosome.DefaultChromosome
 * written by Steve Lewis
 * on Apr 13, 2010
 */
public class DefaultChromosome {
    public static final DefaultChromosome[] EMPTY_ARRAY = {};
    public static final Class THIS_CLASS = DefaultChromosome.class;
    public static final String HG18 = "hg18";
    public static final String HG19 = "hg19";
    public static final String YEAST = "yeast";
    public static final String HUMAN = "human";
    public static final String S288C = "s288c";
    public static final String MM9 = "mm9";

    private static String gDefaultChromosomeSetName = S288C; //  HG18; //
    private static final Map<String, IChromosome> gStringToChromosome =
            new HashMap<String, IChromosome>();

    private static IChromosome[] gDefaultChromosomeSet =     YeastChromosome.STANDARD_CHROMOSOMES; // HumanChromosome.STANDARD_CHROMOSOMES;  // 

    public static IChromosome[] getDefaultChromosomeSet() {
        return gDefaultChromosomeSet;
    }

    public static String getDefaultChromosomeSetName() {
        return gDefaultChromosomeSetName;
    }

    public static void setDefaultChromosomeSet(String name) {
        setDefaultChromosomeSet(getChromosomeSet(name));
        gDefaultChromosomeSetName = name;
        return;
    }


    public static IChromosome[] getChromosomeSet(String name) {

        if (HG18.equalsIgnoreCase(name)) {
            return HumanChromosome.STANDARD_CHROMOSOMES;
        }
        if (HG19.equalsIgnoreCase(name)) {
            return HumanChromosome.STANDARD_CHROMOSOMES;
        }
        if (HUMAN.equalsIgnoreCase(name)) {
            return HumanChromosome.STANDARD_CHROMOSOMES;
        }
        if (YEAST.equalsIgnoreCase(name)) {
            return YeastChromosome.STANDARD_CHROMOSOMES;
        }
        if (S288C.equalsIgnoreCase(name)) {
            return YeastChromosome.STANDARD_CHROMOSOMES;
        }
        if ("Mouse".equalsIgnoreCase(name)) {
            return MouseChromosome.STANDARD_CHROMOSOMES;
        }
        if (MM9.equalsIgnoreCase(name)) {
            return MouseChromosome.STANDARD_CHROMOSOMES;
        }
        throw new UnsupportedOperationException("Chromosome set " + name + " not understood");
    }


    public static void setDefaultChromosomeSet(IChromosome[] pDefaultChromosomeSet) {
        if (gDefaultChromosomeSet == pDefaultChromosomeSet)
            return;
        gStringToChromosome.clear();
        gDefaultChromosomeSet = pDefaultChromosomeSet;
    }

    public static IChromosome parseChromosome(String name) {
        // big cheat to allow classes to look like keys
        if (name.contains("."))
            return getTextChromosome(name);

        if (gStringToChromosome.size() == 0) {
            for (int i = 0; i < gDefaultChromosomeSet.length; i++) {
                IChromosome c = gDefaultChromosomeSet[i];
                gStringToChromosome.put(c.toString().toLowerCase(), c);

            }
        }
        IChromosome iChromosome = gStringToChromosome.get(name.toLowerCase());
        if(iChromosome == null) {
            iChromosome = resetChromosomeSet(name);
        }
        return iChromosome;
    }

    /**
     * force use of proper chromosome set
     * todo MAKE BETTER!
     * @param s
     * @return
     */
    public static IChromosome resetChromosomeSet(String s)
    {
        IChromosome ret = null;
        try {
           ret = HumanChromosome.valueOf(s);
            gStringToChromosome.clear();
           DefaultChromosome.setDefaultChromosomeSet("human");
            return ret;
        }
        catch (Exception e)  {}
        try {
           ret = YeastChromosome.valueOf(s);
            gStringToChromosome.clear();
            DefaultChromosome.setDefaultChromosomeSet("yeast");
            return ret;
        }
        catch (Exception e)  {}
         return null; // give up
    }

    /**
     * turn a chromosome into in org.systemsbiology.couldera.training.index - used in
     * Partitioners
     *
     * @param chr
     * @return
     */
    public static int chromosomeToIndex(IChromosome chr) {
        if (chr == null)
            return 0;
        if (chr instanceof HumanChromosome) {
            switch ((HumanChromosome) chr) {
                case chr1:
                    return 1;
                case chr2:
                    return 2;
                case chr3:
                    return 3;
                case chr4:
                    return 4;
                case chr5:
                    return 5;
                case chr6:
                    return 6;
                case chr7:
                    return 7;
                case chr8:
                    return 8;
                case chr9:
                    return 9;
                case chr10:
                    return 10;
                case chr11:
                    return 11;
                case chr12:
                    return 12;
                case chr13:
                    return 13;
                case chr14:
                    return 14;
                case chr15:
                    return 15;
                case chr16:
                    return 16;
                case chr17:
                    return 17;
                case chr18:
                    return 18;
                case chr19:
                    return 19;
                case chr20:
                    return 20;
                case chr21:
                    return 21;
                case chr22:
                    return 22;
                case chrX:
                    return 23;
                case chrY:
                    return 24;
                case chrM:
                    return 25;
                default:
                    return 0;
            }
        }
        if (chr instanceof MouseChromosome) {
            switch ((MouseChromosome) chr) {
                case chr1:
                    return 1;
                case chr2:
                    return 2;
                case chr3:
                    return 3;
                case chr4:
                    return 4;
                case chr5:
                    return 5;
                case chr6:
                    return 6;
                case chr7:
                    return 7;
                case chr8:
                    return 8;
                case chr9:
                    return 9;
                case chr10:
                    return 10;
                case chr11:
                    return 11;
                case chr12:
                    return 12;
                case chr13:
                    return 13;
                case chr14:
                    return 14;
                case chr15:
                    return 15;
                case chr16:
                    return 16;
                case chr17:
                    return 17;
                case chr18:
                    return 18;
                case chr19:
                    return 19;
                  case chrX:
                    return 23;
                case chrY:
                    return 24;
                case chrM:
                    return 25;
                default:
                    return 0;
            }
        }
        if (chr instanceof YeastChromosome) {
            switch ((YeastChromosome) chr) {
                case chrI:
                    return 1;
                case chrII:
                    return 2;
                case chrIII:
                    return 3;
                case chrIV:
                    return 4;
                case chrV:
                    return 5;
                case chrVI:
                    return 6;
                case chrVII:
                    return 7;
                case chrVIII:
                    return 8;
                case chrIX:
                    return 9;
                case chrX:
                    return 10;
                case chrXI:
                    return 11;
                case chrXII:
                    return 12;
                case chrXIII:
                    return 13;
                case chrXIV:
                    return 14;
                case chrXV:
                    return 15;
                case chrXVI:
                    return 16;
                default:
                    return 0;
            }
        }
        // Huh ?????
        return 0;
    }

    private static Map<String, TextChromosome> gStringChromosomes = new HashMap<String, TextChromosome>();

    protected static TextChromosome getTextChromosome(String s) {
        synchronized (gStringChromosomes) {
            TextChromosome ret = gStringChromosomes.get(s);
            if (ret == null) {
                ret = new TextChromosome(s);
                gStringChromosomes.put(s, ret);
            }
            return ret;
        }
    }

    private static class TextChromosome implements IChromosome, Comparable<TextChromosome> {
        private final String m_Value;

        private TextChromosome(final String pValue) {
            m_Value = pValue;
        }

        @Override
        public String toString() {
            return getValue();
        }

        @Override
        public String getAlternateName() {
            return null;
        }

        @Override
        public int getLength() {
            return 0;
        }

        public String getValue() {
            return m_Value;
        }

        @Override
        public short getIndex() {
            return 0;
        }

        @Override
        public int compareTo(final TextChromosome o) {
            return getValue().compareTo(o.getValue());
        }
    }

}
