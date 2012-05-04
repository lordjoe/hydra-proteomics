package org.systemsbiology.hadoop;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Partitioner;
import org.systemsbiology.chromosome.DefaultChromosome;
import org.systemsbiology.chromosome.IChromosome;
import org.systemsbiology.sam.AnalysisParameters;
import org.systemsbiology.sam.IAnalysisParameters;

import java.util.*;

/**
 * org.systemsbiology.hadoop.AsymetricPartitioner
 * Reserve a special reducer for special keys and use others
 * for 'ordinary' keys
 * written by Steve Lewis
 * on May 6, 2010
 */
public class AsymetricPartitioner extends Partitioner<Text, Text>
{
    public static final AsymetricPartitioner[] EMPTY_ARRAY = {};
    public static final Class THIS_CLASS = AsymetricPartitioner.class;

    private int m_SpecialPartitions = 1;
    private Set<String> m_SpecialKeys = new HashSet<String>();


    public void addSpecialKey(String added) {
        m_SpecialKeys.add(added);
    }


    public void removeSpecialKey(String removed) {
        m_SpecialKeys.remove(removed);
    }

    public String[] getSpecialKeys( ) {
        return m_SpecialKeys.toArray(new String[0]);
    }

     public int getSpecialPartitions() {
        return m_SpecialPartitions;
    }

    public void setSpecialPartitions(int specialPartitions) {
         m_SpecialPartitions = specialPartitions;
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

        final int special = getSpecialPartitions();
        String val = key.toString();
        if(m_SpecialKeys.contains(val))  {
            return 0; // dodo handle more special keys
        }
        int ordinaryPartitions =  numPartitions - special;
        if(ordinaryPartitions <= 0)
            ordinaryPartitions = 1;
        return special + (key.hashCode() % ordinaryPartitions); // to do make better

    }
}