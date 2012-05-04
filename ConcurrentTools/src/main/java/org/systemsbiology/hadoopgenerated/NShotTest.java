package org.systemsbiology.hadoopgenerated;


import org.apache.hadoop.conf.*;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.input.*;
import org.apache.hadoop.mapreduce.lib.output.*;
import org.apache.hadoop.util.*;

import java.awt.*;
import java.io.*;
import java.text.*;
import java.util.*;

/**
 * org.systemsbiology.hadoopgenerated.NShotTest
 *
 * @author Steve Lewis
 * @date Oct 11, 2010
 */
public class NShotTest {
    public static NShotTest[] EMPTY_ARRAY = {};
    public static Class THIS_CLASS = NShotTest.class;

    public static final String  DATE_FORMAT = "MMMddHHmm";


    public static class Map extends Mapper<LongWritable, Text, Text, Text> {

        @Override
        protected void map(LongWritable key, Text value,
                           Context context) throws IOException, InterruptedException {

            // Add interesting code here
            context.write(new Text("foo" + key), new Text("bar" + key));
        }
    }

    public static void writeKeyValue(Reducer.Context context,Text key,Text value,String keyText,String valueStr)
    {
        try {
            key.set(keyText);
            value.set(valueStr);
            context.write(key, value);
        }
        catch (IOException e) {
            throw new RuntimeException(e);

        }
        catch (InterruptedException e) {
            throw new RuntimeException(e);

        }

    }

    public static class Reduce extends Reducer<Text, Text, Text, Text> {
        private boolean m_DateSent;

        /**
         * This method is called once for each key. Most applications will define
         * their reduce class by overriding this method. The default implementation
         * is an identity function.
         */
        @Override
        protected void reduce(Text key, Iterable<Text> values,
                              Context context)
                throws IOException, InterruptedException {
            if (!m_DateSent) {
                Text dkey = new Text("CreationDate");
                Text dValue = new Text();
                   SimpleDateFormat df = new SimpleDateFormat(DATE_FORMAT);
                String name = df.format(new Date());
                try {
                    df.parse(name);
                }
                catch (ParseException e) {
                    throw new RuntimeException(e);

                }

                writeKeyValue(context, dkey, dValue, "CreationDate", name);
                writeKeyValue(context, dkey,dValue,"user.dir",System.getProperty("user.dir"));
                writeKeyValue(context, dkey,dValue,"os.arch",System.getProperty("os.arch"));
                writeKeyValue(context, dkey,dValue,"os.name",System.getProperty("os.name"));



//                dkey.set("ip");
//                java.net.InetAddress addr = java.net.InetAddress.getLocalHost();
//                dValue.set(System.getProperty(addr.toString()));
//                context.write(dkey, dValue);

                m_DateSent = true;
            }
            Iterator<Text> itr = values.iterator();
            // Add interesting code here
            while (itr.hasNext()) {
                Text vCheck = itr.next();
                context.write(key, vCheck);
            }

        }


    }

    /**
     * { method
     *
     * @param DirectoryName <Add Comment Here>
     *                      }
     * @name expungeDirectory
     * @function delete a directory and all its contents
     */
    public static void expungeDirectory(String DirectoryName) {
        expungeDirectory(new File(DirectoryName));
    }

    /**
     * { method
     *
     * @param TheDir <Add Comment Here>
     *               }
     * @name expungeDirectory
     * @function delete a directory and all its contents
     */
    public static void expungeDirectory(File TheDir) {
        if (TheDir.exists()) {
            expungeDirectoryContents(TheDir);
            TheDir.delete();
        }

    }

    /**
     * { method
     *
     * @param DirectoryName <Add Comment Here>
     *                      }
     * @name expungeDirectory
     * @function delete a directory and all its contents
     */
    public static void expungeDirectoryContents(File TheDir) {
        String[] items = TheDir.list();
        for (int i = 0; i < items.length; i++) {
            File Test = new File(TheDir, items[i]);
            if (Test.isFile()) {
                Test.delete();
            }
            else {
                expungeDirectory(Test);
            }
        }

    }

    private static void expungeDirectory(final FileSystem fs, final Path src) {
        try {
            if (!fs.exists(src))
                return;
            // break these out
            if (fs.getFileStatus(src).isDir()) {
                boolean doneOK = fs.delete(src, true);
                doneOK = !fs.exists(src);
                return;
            }
            if (fs.isFile(src)) {
                boolean doneOK = fs.delete(src, false);
                return;
            }
        }
        catch (IOException e) {
            throw new RuntimeException(e);

        }

    }


    public static void runNShotTest(Configuration conf,String[] args)  throws Exception
    {
        AbstractNShotInputFormat.setNumberKeys(20);
        AbstractNShotInputFormat.setNumberSplits(5);

        String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();
//        if (otherArgs.length != 2) {
//            System.err.println("Usage: wordcount <in> <out>");
//            System.exit(2);
//        }
        Job job = new Job(conf, "Generated data");
          job.setJarByClass(NShotTest.class);

        job.setMapperClass(Map.class);
        job.setReducerClass(Reduce.class);


        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);


        job.setInputFormatClass(NShotInputFormat.class);
        job.setOutputFormatClass(TextOutputFormat.class);

        // added Slewis

        if (otherArgs.length > 1) {
            org.apache.hadoop.mapreduce.lib.input.FileInputFormat.addInputPath(job,
                    new Path(otherArgs[0]));
        }


        if(otherArgs.length > 1)    {
            FileInputFormat.addInputPath(job, new Path(otherArgs[0]));
        }

        String athString = otherArgs[otherArgs.length - 1];
        File out = new File(athString);
        if (out.exists()) {
             expungeDirectory(out);
            out.delete();
        }

        Path outputDir = new Path(athString);


         FileOutputFormat.setOutputPath(job, outputDir);


        boolean ans = job.waitForCompletion(true);
        int ret = ans ? 0 : 1;

    }

    public static void main(String[] args) throws Exception
    {
        Configuration conf = new Configuration();
         runNShotTest(  conf,  args);
    }
}


