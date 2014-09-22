package org.systemsbiology.hadoop;

import com.lordjoe.utilities.*;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.input.*;
import org.apache.hadoop.mapreduce.lib.output.*;
import org.apache.hadoop.util.*;
import org.systemsbiology.remotecontrol.*;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 * org.systemsbiology.hadoop.HadoopProbe
 * This class is word count refitted to instrument a
 * hadoop cluster
 */
public class HadoopProbe extends Configured implements Tool
{

    public static interface ContextRunner {
        public void run(TaskInputOutputContext context);
    }

    private static List<ContextRunner> m_Probes = new ArrayList<ContextRunner>();


    public static void addProbe(ContextRunner added) {
        m_Probes.add(added);
    }


    public static void removeProbe(ContextRunner removed) {
        m_Probes.remove(removed);
    }

    public static ContextRunner[] getProbess() {
        return m_Probes.toArray(new ContextRunner[0]);
    }

    /**
     * run then forgive errors
     */
    public static void runProbes(TaskInputOutputContext context) {
        for (ContextRunner r : getProbess()) {
            try {
                r.run(context);
            }
            catch (Exception e) {
                String txt = Util.getExceptionStack(e);
                IntWritable result = new IntWritable(1);
                Text key = new Text(txt);
                try {
                    context.write(key, result);
                }
                catch (IOException e1) {
                    throw new RuntimeException(e1);

                }
                catch (InterruptedException e1) {
                    throw new RuntimeException(e1);

                }

            }
        }
    }


    public static class TokenizerMapper
            extends Mapper<Object, Text, Text, IntWritable> {

        private final static IntWritable one = new IntWritable(1);
        private Text word = new Text();

        public void map(Object key, Text value, Context context
        ) throws IOException, InterruptedException {
            StringTokenizer itr = new StringTokenizer(value.toString());
            while (itr.hasMoreTokens()) {
                String s = itr.nextToken().toLowerCase();
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

        static {
            addProbe(new ReadDataSource());
            addProbe(new SendIPAddress());
            addProbe(new ListMyProperties());

        }

        private boolean m_FirstTime = true;

        public boolean isFirstTime() {
            return m_FirstTime;
        }

        public void setFirstTime(final boolean pFirstTime) {
            m_FirstTime = pFirstTime;
        }

        public void reduce(Text key, Iterable<IntWritable> values,
                           Context context
        ) throws IOException, InterruptedException {
            if (isFirstTime()) {
                HadoopProbe.runProbes(context);
                setFirstTime(false);
            }
            int sum = 0;
            for (IntWritable val : values) {
                sum += val.get();
            }
            result.set(sum);
            context.write(key, result);
        }
    }

    public static class MyPartitioner extends Partitioner<Text, IntWritable> {
        /**
         * Get the partition number for a given key (hence record) given the total
         * number of partitions i.e. number of reduce-tasks for the job.
         * <p/>
         * <p>Typically a hash function on a all or a subset of the key.</p>
         *
         * @param key           the key to be partioned.
         * @param value         the entry value.
         * @param numPartitions the total number of partitions.
         * @return the partition number for the <code>key</code>.
         */
        @Override
        public int getPartition(Text key, IntWritable value, int numPartitions) {

            String s = key.toString();
            if (s.length() == 0)
                return 0;
            char c = s.charAt(0);
            int letter = Character.toLowerCase(c) - 'a';
            if (letter < 0 || letter > 26)
                return 0;
            return letter % numPartitions;
        }
    }


    /**
     * Force loading of needed classes to make
     */
    public static final Class[] NEEDED =
            {
   //                 org.apache.commons.logging.LogFactory.class,
                    org.apache.commons.cli.ParseException.class
            };


    public static class ReadDataSource implements ContextRunner {
        /**
         * test the ability to read a specific file on hdfs
         *
         * @param context
         */
        public void run(TaskInputOutputContext context) {
            try {
                String[] Strings = HDFSUtilities.getHDFSLines(context, "/home/training/hdfsprobes/FeeFieFoe.properties");
                IntWritable result = new IntWritable(1);
                for (int i = 0; i < Strings.length; i++) {
                    String s = Strings[i];
                    Text key = new Text(s);
                    context.write(key, result);

                }
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
            catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }
    }


    public static class SendIPAddress implements ContextRunner {


        public void run(TaskInputOutputContext context) {
            try {
                InetAddress thisIp = InetAddress.getLocalHost();
                IntWritable result = new IntWritable(1);
                Text key = new Text(thisIp.getHostAddress());
                context.write(key, result);
                //      key = new Text( getMACAddress( thisIp) );
                //      context.write(key, result);
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
            catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

    }

    public static class ListMyProperties implements ContextRunner {


        public void run(TaskInputOutputContext context) {
            try {
                IntWritable result = new IntWritable(1);
                Configuration cong = context.getConfiguration();
                Iterator<Map.Entry<String, String>> itr = cong.iterator();
                while (itr.hasNext()) {
                    Map.Entry<String, String> se = itr.next();
                    if (se.getKey().startsWith(HadoopUtilities.SYSBIO_KEY_BASE)) {
                        Text key = new Text(se.getKey() + "=" + se.getValue());
                        context.write(key, result);
                    }
                }
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
            catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

    }

    public int run(String[] args) throws Exception {

        Configuration conf = getConf();
          // use the hadoop file system
        //  conf.set("fs.default.name","hdfs://127.0.0.1:9000/");

        String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();
//        if (otherArgs.length != 2) {
//            System.err.println("Usage: wordcount <in> <out>");
//            System.exit(2);
//        }
        Job job = new Job(conf, "HadoopProbe");
        conf = job.getConfiguration(); // NOTE JOB Copies the configuraton
        job.setJarByClass(HadoopProbe.class);
        job.setMapperClass(TokenizerMapper.class);
 //       job.setCombinerClass(IntSumReducer.class);
        job.setReducerClass(IntSumReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        // added Slewis
        job.setNumReduceTasks(4);
        job.setPartitionerClass(MyPartitioner.class);

        if (otherArgs.length > 1) {
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

        Configuration cnf = job.getConfiguration();

        // test atrings passed to instances
        cnf.setStrings(HadoopUtilities.SYSBIO_KEY_BASE + ".strings","fee","fie","foe","fum");
        cnf.set(HadoopUtilities.SYSBIO_KEY_BASE + ".item","Message in a bottle");
        cnf.set(HadoopUtilities.SYSBIO_KEY_BASE + ".item2","FUBAR");
   
        boolean ans = job.waitForCompletion(true);
        int ret = ans ? 0 : 1;
        return ret;
    }

    public static void main(String[] args) throws Exception {
        //   FileUtils.deleteDirectory(new File("data/output"));
        //   args = new String[]{"data/input", "data/output"};
        ToolRunner.run(new Configuration(),new HadoopProbe(), args);
    }

}