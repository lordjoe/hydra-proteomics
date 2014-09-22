package org.systemsbiology.hadoop;


import org.apache.hadoop.conf.*;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.output.*;

import java.io.*;
import java.util.*;

/**
 * org.systemsbiology.hadoop.OneKeyValueInputFormat
 * This si designed to return exactly One Kay Value pair
 * - The Key is aclled "KEY" and the Value is "Value"
 * The class is designed to force Map Reduce to run once and is
 * specificlly designed for testing environmental issues
 * User: steven
 * Date: Aug 16, 2010
 */
public class OneKeyValueInputFormat extends InputFormat<Text, Text>{
    public static final OneKeyValueInputFormat[] EMPTY_ARRAY = {};

    private static class NullInputSplit extends InputSplit implements Writable {
         @Override
         public long getLength() throws IOException, InterruptedException {
             return 1;
         }

         @Override
         public String[] getLocations() throws IOException, InterruptedException {
             String[] ret =  { ""};
             return ret;
         }

        /**
         * Serialize the fields of this object to <code>out</code>.
         *
         * @param out <code>DataOuput</code> to serialize this object into.
         * @throws java.io.IOException
         */
        @Override
        public void write(final DataOutput out) throws IOException {

        }

        /**
         * Deserialize the fields of this object from <code>in</code>.
         * <p/>
         * <p>For efficiency, implementations should attempt to re-use storage in the
         * existing object where possible.</p>
         *
         * @param in <code>DataInput</code> to deseriablize this object from.
         * @throws java.io.IOException
         */
        @Override
        public void readFields(final DataInput in) throws IOException {

        }
    }

    /**
     * Retuens exactly one Key Value Pair
     */
    private static class OneRecordReader extends RecordReader<Text, Text>  {

        private int m_NRecords;

        public int getNRecords() {
            return m_NRecords;
        }

        public void setNRecords(final int pNRecords) {
            m_NRecords = pNRecords;
        }

        @Override
        public void initialize(final InputSplit pInputSplit, final TaskAttemptContext pTaskAttemptContext) throws IOException, InterruptedException {

        }

        @Override
        public boolean nextKeyValue() throws IOException, InterruptedException {
            return m_NRecords++ == 0;
        }

        @Override
        public Text getCurrentKey() throws IOException, InterruptedException {
            return new Text("Key");
        }

        @Override
        public Text getCurrentValue() throws IOException, InterruptedException {
            return new Text("Value");
         }

        @Override
        public float getProgress() throws IOException, InterruptedException {
            return 0;
        }

        @Override
        public void close() throws IOException {

        }
    }
      @Override
    public List<InputSplit> getSplits(final JobContext pJobContext) throws IOException, InterruptedException {
          List<InputSplit> holder = new ArrayList<InputSplit>();
          holder.add(new NullInputSplit()) ;
          return holder;
    }

    @Override
    public RecordReader<Text, Text> createRecordReader(final InputSplit pInputSplit, final TaskAttemptContext pTaskAttemptContext) throws IOException, InterruptedException {
        return new OneRecordReader();
    }


    private static class TestIdentityMapper extends Mapper<Text,Text,Text,Text>   {
        @Override
        protected void map(final Text key, final Text value, final Context context) throws IOException, InterruptedException {
            context.write(key,value);
        }
    }

    private static class TestCountReducer extends OneShotReducer<Text,Text,Text,Text>   {
        private int m_NumberKeys = 1;

        @Override
        protected void processOnce(final Context context) {
            // report number of keys - should be only one
            safeWrite(new Text("number_calls"),new Text(Integer.toString(m_NumberKeys++)),context);
        }

      }

    public static Job buildOneShotJob(String name,Class reducer) throws IOException {
          Configuration conf = new Configuration();
          Job job = new Job(conf, name);
         job.setInputFormatClass(OneKeyValueInputFormat.class);
         conf = job.getConfiguration(); // NOTE JOB Copies the configuraton
         job.setJarByClass(OneKeyValueInputFormat.class);
         job.setMapperClass(TestIdentityMapper.class);

         job.setNumReduceTasks(1);

         job.setMapOutputKeyClass(Text.class);
         job.setMapOutputValueClass(Text.class);
         job.setOutputKeyClass(Text.class);
         job.setOutputValueClass(Text.class);


         // added Slewis
         job.setNumReduceTasks(1);

        job.setReducerClass(reducer);
        job.setJobName(reducer.getSimpleName());
         return job;
     }



    public static void main(String[] args) throws Exception{
        Job job = buildOneShotJob( "One Key",TestCountReducer.class);
 
         job.setNumReduceTasks(30);

         Path outputDir = new Path("TestJob");

         FileOutputFormat.setOutputPath(job, outputDir);


         boolean ans = job.waitForCompletion(true);
         int ret = ans ? 0 : 1;
         System.exit(ret);

    }
}
