package org.systemsbiology.hadoop.consolidator;

import org.apache.hadoop.fs.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;

import java.io.*;

/**
 * org.systemsbiology.hadoop.consolidator.SingleOutputTextConsolidator
 *
 * @author Steve Lewis
 * @date Oct 23, 2010
 */
public class SingleOutputTextConsolidator extends AbstractConsolidator
{
    public static SingleOutputTextConsolidator[] EMPTY_ARRAY = {};
    public static Class THIS_CLASS = SingleOutputTextConsolidator.class;

    public static final String SINGLE_FILE_OUT_KEY =
               "org.systemsbiology.hadoop.consolidator.SingleOutputTextConsolidator.Path";
    public static final String FILESYSTEM_KEY =
               "org.systemsbiology.hadoop.consolidator.SingleOutputTextConsolidator.FileSystem";



         public static class SingleOutputPartitioner extends Partitioner<Text, IntWritable>
         {
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
         public int getPartition(Text key, IntWritable value, int numPartitions) {

             if(numPartitions > 1)
                throw new IllegalStateException("Use only one reducer");
             return 0;
         }
     }


    public static class SingleTextReducer
            extends Reducer<Text, Text, Text, Text> {
        private Path m_OutputPath;
        private FileSystem m_OutputFileSystem;
        private FSDataOutputStream m_OutStream;
        private PrintWriter m_Out;

        @Override
        protected void setup(Context context)
                throws IOException, InterruptedException
        {
            super.setup(context);
            String outputStr = context.getConfiguration().get(SINGLE_FILE_OUT_KEY);
            m_OutputPath = new Path(outputStr);
            String fileSystem = context.getConfiguration().get(FILESYSTEM_KEY);
            m_OutputFileSystem = ConsolidatorUtilities.buildFileSystem(fileSystem,context);
            m_OutStream = m_OutputFileSystem.create(m_OutputPath);
            m_Out = new PrintWriter(m_OutStream);
        }

        public void reduce(Text key, Iterable<Text> values,
                           Context context
        ) throws IOException, InterruptedException {
            int sum = 0;
            for (Text val : values) {
                m_Out.println(val);
            }
         }

        @Override
        protected void cleanup(Context context)
                throws IOException, InterruptedException
        {
            super.cleanup(context);
            m_Out.close();

        }
    }


     public static class MyPartitioner extends Partitioner<Text, IntWritable> {
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
         public int getPartition(Text key, IntWritable value, int numPartitions) {

             String s = key.toString();
             if (s.length() == 0)
                 return 0;
             char c = s.charAt(0);
             int letter = Character.toUpperCase(c) - 'A';
             if (letter < 0 || letter > 26)
                 return 0;
             return letter % numPartitions;
         }
     }

}
