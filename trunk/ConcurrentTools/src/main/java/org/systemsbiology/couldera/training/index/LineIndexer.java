// (c) Copyright 2009 Cloudera, Inc.

package org.systemsbiology.couldera.training.index;

import java.io.IOException;

import org.apache.hadoop.conf.*;
import org.apache.hadoop.filecache.*;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.InputFormat;
import org.apache.hadoop.mapreduce.OutputFormat;
import org.apache.hadoop.mapreduce.lib.output.*;
import org.apache.hadoop.util.*;
import org.systemsbiology.common.*;
import  org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
/**
 * LineIndexer
 *
 * Creates an inverted org.systemsbiology.couldera.training.index over all the words in a document corpus, mapping
 * each observed word to a list of filename@offset locations where it occurs.
 *
 */
public class LineIndexer extends Configured implements Tool {

  // where to put the data in hdfs when we're done
  private static final String OUTPUT_PATH = "output";

  // where to read the data from.
  private static final String INPUT_PATH = "input";



  /** Driver for the actual MapReduce process */
  private void runJob(String [] args) throws IOException {
      try {
          Configuration conf = getConf();

          Job job = new Job(conf, "Gene Pipeline");

          conf = job.getConfiguration(); // NOTE JOB Copies the configuraton
          job.setJarByClass(getClass());

          Class<? extends InputFormat<?, ?>> aClass1 = job.getInputFormatClass();
          Class<? extends OutputFormat<?, ?>> aClass = job.getOutputFormatClass();

          FileInputFormat.addInputPath(job, new Path(args[0]));

          FileInputFormat.addInputPath(job, new Path(args[0]));
          FileOutputFormat.setOutputPath(job, new Path(args[1]));
          System.err.println("Args");
          for (int i = 0; i < args.length; i++) {
              String arg = args[i];
              System.err.println(" " + arg);
          }

          job.setMapperClass(LineIndexMapper.class);
          job.setReducerClass(LineIndexReducer.class);
          job.setCombinerClass(LineIndexReducer.class);

          job.setOutputKeyClass(Text.class);
          job.setOutputValueClass(Text.class);

          boolean ans = job.waitForCompletion(true);
       }
       catch (IOException e) {
           ExceptionUtilities.printFullStace(e);
           throw new RuntimeException(e);
       }
       catch (InterruptedException e) {
           ExceptionUtilities.printFullStace(e);
           throw new RuntimeException(e);
       }
       catch (ClassNotFoundException e) {
           ExceptionUtilities.printFullStace(e);
           throw new RuntimeException(e);
       }
       catch (RuntimeException e) {
           ExceptionUtilities.printFullStace(e);
           throw e;
       }
  }

  public int run(String [] args) throws IOException {
    runJob(args);
    return 0;
  }

  public static void main(String [] args) throws Exception {
    int ret = ToolRunner.run(new LineIndexer(), args);
    System.exit(ret);
  }
}

