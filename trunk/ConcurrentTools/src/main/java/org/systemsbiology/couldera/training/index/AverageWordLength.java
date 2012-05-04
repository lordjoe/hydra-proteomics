package org.systemsbiology.couldera.training.index;

import java.io.*;
import java.util.Iterator;
import java.util.StringTokenizer;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapred.FileOutputFormat;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;


/**
 * org.systemsbiology.couldera.training.index.AverageWordLength
 *
 * @author Steve Lewis
 * @date Jul 1, 2010
 */
public class AverageWordLength
{
    public static AverageWordLength[] EMPTY_ARRAY = {};
    public static Class THIS_CLASS = AverageWordLength.class;

    public static class MapClass extends MapReduceBase implements
            Mapper<LongWritable, Text, Text, IntWritable>
    {

        private IntWritable len = new IntWritable(1);
        private Text word = new Text();

        public void map(LongWritable key, Text value, OutputCollector<Text, IntWritable> output,
                        Reporter reporter) throws IOException
        {
            String line = value.toString();
            StringTokenizer itr = new StringTokenizer(line);

            while (itr.hasMoreTokens()) {
                String tok = itr.nextToken();
                word.set(tok.substring(0, 1));
                len.set(tok.length());
                output.collect(word, len);
            }
        }
    }

    public static class WordLenValue implements Writable
    {
        private int m_Leangth;
        private int m_Count;

        public int getLength()
        {
            return m_Leangth;
        }

        public int getCount()
        {
            return m_Count;
        }

        public void setLength(int v)
        {
            m_Leangth = v;
        }

        public void setCount(int v)
        {
            m_Count = v;
        }

        @Override
        public void readFields(DataInput in) throws IOException
        {
            m_Leangth = in.readInt();
            m_Count = in.readInt();
        }

        @Override
        public void write(DataOutput out) throws IOException
        {
            out.writeInt(m_Leangth);
            out.writeInt(m_Count);

        }
    }

    public static class Reduce extends MapReduceBase implements
            Reducer<Text, IntWritable, Text, FloatWritable>
    {

        private FloatWritable avg = new FloatWritable();

        public void reduce(Text key, Iterator<IntWritable> values,
                           OutputCollector<Text, FloatWritable> output, Reporter reporter)
                throws IOException
        {
            int sum = 0;
            int cnt = 0;
            while (values.hasNext()) {
                sum += values.next().get();
                cnt++;
            }

            avg.set((float) sum / cnt);
            output.collect(key, avg);
        }
    }

    public static void main(String[] args) throws Exception
    {
        if (args.length != 2) {
            System.out.println("usage: [input] [output]");
            System.exit(-1);
        }

        String inputPath = args[0];
        String outputPath = args[1];

        JobConf conf = new JobConf(AverageWordLength.class);
        conf.setJobName("AverageWordLength");

        conf.setMapOutputKeyClass(Text.class);
        conf.setMapOutputValueClass(IntWritable.class);
        conf.setOutputKeyClass(Text.class);
        conf.setOutputValueClass(FloatWritable.class);

        conf.setMapperClass(MapClass.class);
        conf.setReducerClass(Reduce.class);
        conf.setCombinerClass(Reduce.class);

        FileInputFormat.setInputPaths(conf, new Path(inputPath));
        FileOutputFormat.setOutputPath(conf, new Path(outputPath));

        // delete the output directory if it exists already
        FileSystem.get(conf).delete(new Path(outputPath), true);

        JobClient.runJob(conf);
    }
}

