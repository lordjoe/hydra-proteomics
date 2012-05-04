package org.systemsbiology.hadoop;

import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;
import org.systemsbiology.chromosome.*;
import org.systemsbiology.sam.*;

/**
 * org.systemsbiology.hadoop.ChromosomePartitioner
 * written by Steve Lewis
 * on May 6, 2010
 */
public class ChromosomePartitioner extends Partitioner<Text, Text>
{
    public static final ChromosomePartitioner[] EMPTY_ARRAY = {};
    public static final Class THIS_CLASS = ChromosomePartitioner.class;

    private IChromosome[] m_Chromosomes;

    public IChromosome[] getChromosomes() {
        if (m_Chromosomes == null)
            m_Chromosomes = buildChromosomes();
        return m_Chromosomes;
    }


    protected IChromosome[] buildChromosomes() {
        IAnalysisParameters ap = AnalysisParameters.getInstance();
        return ap.getDefaultChromosomes();
    }

    /**
     * Get the partition number for a given key (hence record) given the total
     * number of partitions i.e. number of reduce-tasks for the job.
     * <p/>
     * <p>Typically a hash function on a all or a subset of the key.</p>
     *
     * @param key           the key to be partioned.
     * @param value         the entry value.
     * @param numPartitions the total number of partitions.
     * @return the partition number for the <code>key</code>.
     */
    @Override
    public int getPartition(Text key, Text value, int numPartitions) {
        SAMRecordMapperKey srKey = new SAMRecordMapperKey(key.toString());
        int index = DefaultChromosome.chromosomeToIndex(srKey.getChromosome()) ;
        return index % numPartitions; // to do make better

    }
}
