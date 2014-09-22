package org.systemsbiology.hadoop;

import org.apache.hadoop.conf.*;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.input.*;
import org.apache.hadoop.mapreduce.lib.output.*;
import org.apache.hadoop.util.*;
import org.systemsbiology.remotecontrol.*;

import java.io.*;

/**
 * org.systemsbiology.hadoop.ConfiguredJobRunner
 * User: steven
 * Date: 9/26/11
 */
public class WrappedToolRunner extends ConfiguredJobRunner implements IJobRunner {

    public static final WrappedToolRunner[] EMPTY_ARRAY = {};

    private final Tool m_Tool;

    public WrappedToolRunner(Tool job) {

        m_Tool = job;
    }

    public Tool getTool() {
        return m_Tool;
    }

    @Override
    public int runJob(Configuration conf, String[] args) throws Exception {
        Tool tool = getTool();
        String oldFs = conf.get("fs.defaultFS");
        String host = RemoteUtilities.getHost();
        int port = RemoteUtilities.getPort();
         final String userDir = "/user/" + RemoteUtilities.getUser();
        conf.set("fs.defaultFS", "hdfs://" + host + ":" + port + userDir);

        tool.setConf(conf);
         return run(args);
    }

    @Override
    public int run(String[] args) throws Exception {
        Tool tool = getTool();
         return tool.run(args);
    }

//
//    @Override
//    public  int runJob(Configuration conf,String[] args) throws Exception {
//           String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();
////        if (otherArgs.length != 2) {
////            System.err.println("Usage: wordcount <in> <out>");
////            System.exit(2);
////        }
//        Job job = new Job(conf, "word count");
//        conf = job.getConfiguration(); // NOTE JOB Copies the configuraton
//        job.setJarByClass(CapitalWordCount.class);
//        job.setMapperClass(TokenizerMapper.class);
//         job.setCombinerClass(IntSumReducer.class);
//        job.setReducerClass(IntSumReducer.class);
//
//        job.setMapOutputKeyClass(Text.class);
//        job.setMapOutputValueClass(IntWritable.class);
//        job.setOutputKeyClass(Text.class);
//        job.setOutputValueClass(IntWritable.class);
//
//
//
//        // added Slewis
//        job.setNumReduceTasks(2); // cheaper on amazon HadoopUtilities.DEFAULT_REDUCE_TASKS);
//    //    job.setPartitionerClass(MyPartitioner.class);
//
//        if(otherArgs.length > 1)    {
//            FileInputFormat.addInputPath(job, new Path(otherArgs[0]));
//        }
//
//        // you must pass the output directory as the last argument
//        String athString = otherArgs[otherArgs.length - 1];
//        File out = new File(athString);
////        if (out.exists()) {
////            FileUtilities.expungeDirectory(out);
////            out.delete();
////        }
//
//        Path outputDir = new Path(athString);
//
//        FileSystem fileSystem = outputDir.getFileSystem(conf);
//        expunge(outputDir,fileSystem);    // make sure thia does not exist
//        FileOutputFormat.setOutputPath(job, outputDir);
//
//
//        boolean ans = job.waitForCompletion(true);
//        int ret = ans ? 0 : 1;
//        return ret;
//    }


}
