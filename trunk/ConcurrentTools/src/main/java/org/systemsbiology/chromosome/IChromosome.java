package org.systemsbiology.chromosome;

/**
 * org.systemsbiology.chromosome.IChromosome
 * written by Steve Lewis
 * on Apr 8, 2010
 */
public interface IChromosome 
{
    public static final IChromosome[] EMPTY_ARRAY = {};
    public static final Class THIS_CLASS = IChromosome.class;

    /**
     * singleton marker for a NullChromosome
     */
    public static final IChromosome NULL_CHROMOSOME = new NullChromosome();

    public String getAlternateName() ;

     public int getLength();

    public short getIndex();

    /**
     * place holder when no chromosome is available
     */
    public static class NullChromosome implements IChromosome {
        private NullChromosome() {}

        public String getAlternateName() {
            return null;
        }

        public int getLength() {
            return 0;
        }

        public int compareTo(IChromosome o) {
            if(o ==this )  return 0;
            return toString().compareTo(o.toString());
         }

        @Override
        public short getIndex() {
            return Short.MAX_VALUE;
        }
    }




}