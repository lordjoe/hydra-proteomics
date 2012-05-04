package org.systemsbiology.hadoop;

import com.lordjoe.utilities.*;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.input.*;
import org.apache.hadoop.mapreduce.lib.output.*;
import org.apache.hadoop.util.*;

import java.io.*;

/**
 * org.systemsbiology.hadoop.WholeFileTest
 */
public class WholeFileTest {


    public static enum NumberFilesSeen { AtMapper, AtReducer, AtSource };

    /**
     * Basically an identity Mapper but here for debugging
     */
    public static class FileMapper
            extends Mapper<Text, Text, Text, Text> {


        public void map(Text key, Text value, Context context
        ) throws IOException, InterruptedException {
            String keyStr = key.toString();
            String valueStr = value.toString();

//            String id = Util.textBetween(valueStr,"#ID = ","\n");
//            if(!id.equalsIgnoreCase(keyStr.replace(".dat","")))
//                throw new UnsupportedOperationException("Key is not id");

            context.getCounter(NumberFilesSeen.AtMapper).increment(1);
            context.write(key, value);
        }
    }

    public static class WholeFileReducer
            extends Reducer<Text, Text, Text, Text> {
        private IntWritable result = new IntWritable();

        public void reduce(Text key, Iterable<Text> values,
                           Context context
        ) throws IOException, InterruptedException {
            for (Text val : values) {
                String keyStr = key.toString();
                String valueStr = val.toString();
                context.write(key, val);
                context.getCounter(NumberFilesSeen.AtReducer).increment(1);
            }
        }
    }


    /**
     * Force loading of needed classes to make
     */
    public static final Class[] NEEDED =
            {
                    //                  org.apache.commons.logging.LogFactory.class,
                    org.apache.commons.cli.ParseException.class
            };


    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();
        Job job = new Job(conf, "WholeFiles");
        conf = job.getConfiguration(); // NOTE JOB Copies the configuraton
        long original = 0;

        job.setInputFormatClass(WholeFileInputFormat.class);
        job.setJarByClass(WholeFileTest.class);
        job.setMapperClass(FileMapper.class);
        //    job.setCombinerClass(IntSumReducer.class);
        job.setReducerClass(WholeFileReducer.class);

        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);


        if (otherArgs.length > 1) {
            String arg = otherArgs[0];
            File inputDir = new File(arg);
            File[] items = inputDir.listFiles();
             original = items.length;
             FileInputFormat.addInputPath(job, new Path(arg));
        }

        String athString = otherArgs[otherArgs.length - 1];
        File out = new File(athString);
        if (out.exists()) {
            FileUtilities.expungeDirectory(out);
            out.delete();
        }

        Path outputDir = new Path(athString);


        FileOutputFormat.setOutputPath(job, outputDir);


        boolean ans = job.waitForCompletion(true);
        long mapped = HadoopUtilities.getCounterValue(NumberFilesSeen.AtMapper, job);
        long reduced = HadoopUtilities.getCounterValue(NumberFilesSeen.AtReducer, job);

        if(original != mapped)
             throw new UnsupportedOperationException("Wrong number Mapped " + mapped + " not " + original);
        if(original != reduced)
            throw new UnsupportedOperationException("Wrong number Reduced " + reduced + " not " + original);
        int ret = ans ? 0 : 1;
        System.exit(ret);
    }
}