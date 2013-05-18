package org.systemsbiology.hadoop;


import org.apache.hadoop.conf.*;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.input.*;
import org.apache.hadoop.mapreduce.lib.output.*;
import org.apache.hadoop.util.*;

//import org.systemsbiology.remotecontrol.*;

import java.io.*;
import java.util.*;

/**
 *  org.systemsbiology.hadoop.CapitalWordCount
 */
public class CapitalWordCount  extends ConfiguredJobRunner implements IJobRunner {

 
    public static class TokenizerMapper
            extends Mapper<Object, Text, Text, IntWritable> {

        private final static IntWritable one = new IntWritable(1);
        private Text word = new Text();

        public void map(Object key, Text value, Context context
        ) throws IOException, InterruptedException {
            StringTokenizer itr = new StringTokenizer(value.toString());
            while (itr.hasMoreTokens()) {
                String s = itr.nextToken().toUpperCase();
                s = dropNonLetters(s);
                if (s.length() > 0) {
                    word.set(s);
                    context.write(word, one);
                }
            }
        }
    }

    public static String dropNonLetters(String s) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (Character.isLetter(c))
                sb.append(c);
        }

        return sb.toString();
    }

    public static class IntSumReducer
            extends Reducer<Text, IntWritable, Text, IntWritable> {
        private IntWritable result = new IntWritable();

        public void reduce(Text key, Iterable<IntWritable> values,
                           Context context
        ) throws IOException, InterruptedException {
            int sum = 0;
            for (IntWritable val : values) {
                sum += val.get();
            }
            result.set(sum);
            context.write(new Text(key.toString()), result);
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
//    /**
//     * Force loading of needed classes to make
//     */
//    public static final Class[] NEEDED =
//            {
//  //                  org.apache.commons.logging.LogFactory.class,
//                    org.apache.commons.cli.ParseException.class
//            };
//

    /**
     * kill a directory and all contents
     * @param src
     * @param fs
     * @return
     */
        public static  boolean expunge(Path src,FileSystem fs) {


          try {
              if (!fs.exists(src))
                  return true;
              // break these out
              if (fs.getFileStatus(src).isDir()) {
                  boolean doneOK = fs.delete(src, true);
                  doneOK = !fs.exists(src);
                  return doneOK;
              }
              if (fs.isFile(src)) {
                  boolean doneOK = fs.delete(src, false);
                  return doneOK;
              }
              throw new IllegalStateException("should be file of directory if it exists");
          }
          catch (IOException e) {
              throw new RuntimeException(e);
          }

      }


    public  int runJob(Configuration conf,String[] args) throws Exception {
           String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();
//        if (otherArgs.length != 2) {
//            System.err.println("Usage: wordcount <in> <out>");
//            System.exit(2);
//        }
        Job job = new Job(conf, "word count");
        conf = job.getConfiguration(); // NOTE JOB Copies the configuraton
        job.setJarByClass(CapitalWordCount.class);
        job.setMapperClass(TokenizerMapper.class);
         job.setCombinerClass(IntSumReducer.class);
        job.setReducerClass(IntSumReducer.class);
        
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(IntWritable.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);


        
        // added Slewis
        job.setNumReduceTasks(2); // cheaper on amazon HadoopUtilities.DEFAULT_REDUCE_TASKS);
    //    job.setPartitionerClass(MyPartitioner.class);

        if(otherArgs.length > 1)    {
            FileInputFormat.addInputPath(job, new Path(otherArgs[0]));
        }

        // you must pass the output directory as the last argument
        String athString = otherArgs[otherArgs.length - 1];
        File out = new File(athString);
//        if (out.exists()) {
//            FileUtilities.expungeDirectory(out);
//            out.delete();
//        }

        Path outputDir = new Path(athString);

        FileSystem fileSystem = outputDir.getFileSystem(conf);
        expunge(outputDir,fileSystem);    // make sure thia does not exist
        FileOutputFormat.setOutputPath(job, outputDir);


        boolean ans = job.waitForCompletion(true);
        int ret = ans ? 0 : 1;
        return ret;
    }


    /**
     * Execute the command with the given arguments.
     *
     * @param args command specific arguments.
     * @return exit code.
     * @throws Exception
     */
    @Override
    public int run(final String[] args) throws Exception {
        Configuration conf = getConf();
        if(conf == null)
            conf = HDFSAccessor.getSharedConfiguration();
        //      conf.set(BamHadoopUtilities.CONF_KEY,"config/MotifLocator.config");
        return runJob(conf, args);
    }


    public static void main(String[] args) throws Exception {
        ToolRunner.run(new CapitalWordCount(), args);
    }
}