package org.systemsbiology.sam;

import com.lordjoe.utilities.*;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.input.*;
import org.apache.hadoop.mapreduce.lib.output.*;
import org.apache.hadoop.util.*;
import org.systemsbiology.chromosome.*;

import java.io.*;
import java.util.*;

/**
 *  org.systemsbiology.sam.SamSort
 */
public class SamSort
{

    public static class TokenizerMapper
            extends Mapper<Object, Text, Text, Text> {

        private Text ONLY_WORD = new Text();

        public void map(Object key, Text value, Context context
        ) throws IOException, InterruptedException {
            String val = value.toString();
            if(val.startsWith("@"))
                return; // ignore header for now
            String[] fields = val.split("\t");
            if(fields.length < 4)
                return;
            SortableChromosome sc = SortableChromosome.getSortableChromosome(fields[2]);
            if(sc == null)
                return;
            String KeyStr = sc.toString() + " " + fields[3];
            ONLY_WORD.set(KeyStr);
           context.write(ONLY_WORD,value);
       }
    }


    public static class SimpleReducer
            extends Reducer<Text, Text, NullWritable, Text> {

        public void reduce(Text key, Iterable<Text> values,
                           Context context
        ) throws IOException, InterruptedException {

            NullWritable keyw = NullWritable.get();
             Iterator<Text> vi = values.iterator();
            while(vi.hasNext()) {
                context.write(keyw, vi.next());
            }
        }
    }

//    public static class MyPartitioner extends Partitioner<Text, IntWritable> {
//        /**
//         * Get the partition number for a given key (hence record) given the total
//         * number of partitions i.e. number of reduce-tasks for the job.
//         * <p/>
//         * <p>Typically a hash function on a all or a subset of the key.</p>
//         *
//         * @param key           the key to be partioned.
//         * @param value         the entry value.
//         * @param numPartitions the total number of partitions.
//         * @return the partition number for the <code>key</code>.
//         */
//        @Override
//        public int getPartition(Text key, IntWritable value, int numPartitions) {
//
//            String s = key.toString();
//            if (s.length() == 0)
//                return 0;
//            char c = s.charAt(0);
//            int letter = Character.toUpperCase(c) - 'A';
//            if (letter < 0 || letter > 26)
//                return 0;
//            return letter % numPartitions;
//        }
//    }
//

    /**
     * Force loading of needed classes to make
     */
    public static final Class[] NEEDED =
            {
//                    org.apache.commons.logging.LogFactory.class,
                    org.apache.commons.cli.ParseException.class
            };



    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();
//        if (otherArgs.length != 2) {
//            System.err.println("Usage: wordcount <in> <out>");
//            System.exit(2);
//        }
        Job job = new Job(conf, "Sam Sort");
        conf = job.getConfiguration(); // NOTE JOB Copies the configuraton
        job.setJarByClass(SamSort.class);
        job.setMapperClass(TokenizerMapper.class);
    //    job.setCombinerClass(IntSumReducer.class);
        job.setReducerClass(SimpleReducer.class);
        
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);
        job.setOutputKeyClass(NullWritable.class);
        job.setOutputValueClass(Text.class);

        job.setInputFormatClass(SequenceFileInputFormat.class);
        job.setNumReduceTasks(1);
        // added Slewis
       //   job.setPartitionerClass(MyPartitioner.class);

        if(otherArgs.length > 1)    {
            final String arg = otherArgs[0];
            FileInputFormat.addInputPath(job, new Path(arg));
        }
     //   FileInputFormat.setInputPathFilter(job,BamFileFilter.class);

        String pathString = otherArgs[otherArgs.length - 1];
        File out = new File(pathString);
        if (out.exists()) {
            FileUtilities.expungeDirectory(out);
            out.delete();
        }

        Path outputDir = new Path(pathString);


        FileOutputFormat.setOutputPath(job, outputDir);


        boolean ans = job.waitForCompletion(true);
        int ret = ans ? 0 : 1;
        System.exit(ret);
    }
}