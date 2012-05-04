package org.systemsbiology.hadoop.consolidator;

import com.lordjoe.utilities.*;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.lib.*;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.input.*;
import org.apache.hadoop.mapreduce.lib.output.*;
import org.apache.hadoop.util.*;

import java.io.*;

/**
 * org.systemsbiology.hadoop.consolidator.WordCountConsolidator
 *
 * @author Steve Lewis
 * @date Oct 23, 2010
 */
public class WordCountConsolidator extends SingleOutputTextConsolidator
{
    public static WordCountConsolidator[] EMPTY_ARRAY = {};
    public static  final Class THIS_CLASS = WordCountConsolidator.class;

    public static final String SINGLE_FILE_OUT_KEY =
            "org.systemsbiology.hadoop.consolidator.SingleOutputTextConsolidator.Path";
    public static final String FILESYSTEM_KEY =
            "org.systemsbiology.hadoop.consolidator.SingleOutputTextConsolidator.FileSystem";


     public static class WordCountConsolidatorMapper
            extends Mapper<LongWritable, Text, Text, LongWritable>

    {

        private Text m_Out = new Text();
        private LongWritable m_Count = new LongWritable();

        @Override
        protected void setup(Context context)
                throws IOException, InterruptedException
        {
            super.setup(context);

        }

        @Override
        protected void map(LongWritable key, Text value,
                           Context context)
                throws IOException, InterruptedException
        {
             String[] items = value.toString().split("\t");
            m_Out.set(items[0]);
            m_Count.set(Integer.parseInt(items[1]));
             context.write(m_Out,m_Count);

        }

        @Override
        protected void cleanup(Context context)
                throws IOException, InterruptedException
        {
            super.cleanup(context);

        }
    }


    public static class WordCountReducer
            extends Reducer<Text, LongWritable, Text, Text>
    {
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
            m_OutputFileSystem = ConsolidatorUtilities.buildFileSystem(fileSystem, context);
            m_OutStream = m_OutputFileSystem.create(m_OutputPath);
            m_Out = new PrintWriter(m_OutStream);
        }

        public void reduce(Text key, Iterable<Text> values,
                           Context context
        ) throws IOException, InterruptedException
        {
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

    public static void main(String[] args) throws Exception {
         Configuration conf = new Configuration();
         String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();
//        if (otherArgs.length != 2) {
//            System.err.println("Usage: wordcount <in> <out>");
//            System.exit(2);
//        }
         Job job = new Job(conf, "word count consolidator");
         conf = job.getConfiguration(); // NOTE JOB Copies the configuraton
         job.setJarByClass(THIS_CLASS);
         job.setMapperClass(WordCountConsolidatorMapper.class);
          job.setReducerClass(WordCountReducer.class);

         job.setMapOutputKeyClass(Text.class);
         job.setMapOutputValueClass(LongWritable.class);
         job.setOutputKeyClass(Text.class);
         job.setOutputValueClass(Text.class);



         // added Slewis
         job.setNumReduceTasks(1);

         if(otherArgs.length > 1)    {
             FileInputFormat.addInputPath(job, new Path(otherArgs[0]));
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
         int ret = ans ? 0 : 1;
         System.exit(ret);
     }



}
