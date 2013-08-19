package org.systemsbiology.hadoopgenerated;
import java.io.*;
import java.util.*;

import org.apache.hadoop.conf.*;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.*;

 import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
 import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.*;

/**
 * org.systemsbiology.hadoop.GeneratedMapper
 *
 * @author Steve Lewis
 * @date Oct 10, 2010
 */


public class GeneratedMapper
{


    public static class Map extends Mapper<LongWritable,LongWritable,LongWritable, LongWritable>
    {
        private final static LongWritable truePrime = new LongWritable(0);
        private final static LongWritable falsePrime = new LongWritable(1);

        @Override
        protected void map(LongWritable key, LongWritable value,
                     Context context) throws IOException, InterruptedException {
            long suspectPrimeValue = value.get();
            boolean isComposite = false;

            for(long i = 2; i < suspectPrimeValue - 1 ; i++)
            {
                if(suspectPrimeValue % i == 0)
                {
                    context.write(falsePrime, value);
                    isComposite = true;
                    break;
                }
            }

            if(!isComposite)
            {
                context.write(truePrime, value);
            }
            System.out.println(suspectPrimeValue);
        }
    }

    public static class Reduce extends Reducer<LongWritable, LongWritable, LongWritable, LongWritable>
    {

        /**
         * This method is called once for each key. Most applications will define
         * their reduce class by overriding this method. The default implementation
         * is an identity function.
         */
        @Override
        protected void reduce(LongWritable key, Iterable<LongWritable> values,
                              Context context)
                throws IOException, InterruptedException
        {
            Iterator<LongWritable>   itr = values.iterator();
            while(itr.hasNext())
              {
                  LongWritable vCheck = itr.next();
                  context.write(key,vCheck);
              }

        }

  
    }

//    public static void main(String[] args) throws Exception
//    {
//        JobConf conf = new JobConf(GeneratedMapper.class);
//        conf.setJobName("primetest");
//
//        conf.setOutputKeyClass(BooleanWritable.class);
//        conf.setOutputValueClass(LongWritable.class);
//
//        conf.setMapperClass(Map.class);
//        conf.setCombinerClass(Reduce.class);
//        conf.setReducerClass(Reduce.class);
//
//        conf.setInputFormat(GeneratedDataInputFormat.class);
//        conf.setOutputFormat(TextOutputFormat.class);
//
//        conf.setOutputPath(new Path(args[0]));
//
//        JobClient.runJob(conf);
//    }

    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();
//        if (otherArgs.length != 2) {
//            System.err.println("Usage: wordcount <in> <out>");
//            System.exit(2);
//        }
        Job job = new Job(conf, "Generated data");
        job.setJarByClass(GeneratedMapper.class);

        job.setMapperClass(Map.class);
        job.setReducerClass(Reduce.class);


        job.setMapOutputKeyClass(LongWritable.class);
        job.setMapOutputValueClass(LongWritable.class);

        job.setOutputKeyClass(LongWritable.class);
        job.setOutputValueClass(LongWritable.class);


        job.setInputFormatClass(GeneratedDataInputFormat.class);
        job.setOutputFormatClass(TextOutputFormat.class);

        // added Slewis

         if(otherArgs.length > 1)    {
            org.apache.hadoop.mapreduce.lib.input.FileInputFormat.addInputPath(job, new Path(otherArgs[0]));
        }

        // make sure output does not exist
        int index = 1;
         String s = "e:/foo";
        while(new File(s).exists()) {
            s = s + index++;
        }
        Path outputDir = new Path(s);


        FileOutputFormat.setOutputPath(job, outputDir);


        boolean ans = job.waitForCompletion(true);
        int ret = ans ? 0 : 1;
        System.exit(ret);
    }

}