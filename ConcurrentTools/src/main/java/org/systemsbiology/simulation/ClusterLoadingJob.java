package org.systemsbiology.simulation;

import com.lordjoe.utilities.*;
import net.sf.samtools.*;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.io.compress.*;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.input.*;
import org.apache.hadoop.mapreduce.lib.output.*;
import org.apache.hadoop.util.*;
import org.systemsbiology.hadoop.*;
import org.systemsbiology.partitioning.*;
import org.systemsbiology.remotecontrol.*;

import java.io.*;

/**
 * org.systemsbiology.simulation.ClusterLoadingJob
 */
public class ClusterLoadingJob
{

    public static final String NUMBER_RECORDS_KEY = "org.systemsbiology.numberrecords";
    public static final String OUTPUT_BUCKET_KEY = "org.systemsbiology.bucket";

    public static final String DEFAULT_BUCKET = "sim";
    public static final int DEFAULT_NUMBER_RECORDS = 20000000; //20 * SimulatedPartitioner.ONE_MILLION;


    public static class TokenizerMapper
            extends Mapper<Object, Text, IntWritable, Text>
    {

        public static final int NUMBER_KEYS_TO_HIT_ALL_REDUCERS = 1000;
        private final Text ONLY_TEXT = new Text();
        private final IntWritable ONLY_INT = new IntWritable(1);
        private Text word = new Text();

        private int m_EmmittedKeys;


        public void map(Object key, Text value, Context context
        ) throws IOException, InterruptedException
        {
            while (m_EmmittedKeys < NUMBER_KEYS_TO_HIT_ALL_REDUCERS) {
                ONLY_INT.set(m_EmmittedKeys);
                ONLY_TEXT.set(Integer.toString(m_EmmittedKeys++));
                context.write(ONLY_INT, ONLY_TEXT);
            }
         }
    }

    public static class DataGenerator
            extends Reducer<IntWritable, Text, Text, Text>
    {
        private IntWritable result = new IntWritable();
        public final Text ONLY_KEY = new Text();
        public final Text ONLY_VALUE = new Text();

        private SimulatedPartitioner m_Partitioner;
        private HadoopStreamFactory m_Factory;
        private int m_DataToGenerate;
        private int m_Index;

        public HadoopStreamFactory getFactory()
        {
            return m_Factory;
        }

        public void setFactory(HadoopStreamFactory pFactory)
        {
            m_Factory = pFactory;
        }

        @Override
        protected void setup(Context context)
                throws IOException, InterruptedException
        {
            super.setup(context);

            Configuration c = context.getConfiguration();
            m_Index = context.getTaskAttemptID().getId();
            m_DataToGenerate = c.getInt(NUMBER_RECORDS_KEY, DEFAULT_NUMBER_RECORDS);
            setFactory(new HadoopStreamFactory(c));
            m_Partitioner = new SimulatedPartitioner();
            m_Partitioner.guaranteeSampleRecords();
            m_Partitioner.setPairIdentifier(m_Index << 20);
            if (m_DataToGenerate == 0)
                 return;
             SequenceFile.Writer writer = buildSequenceWriter(context);
             for (; m_DataToGenerate > 0; m_DataToGenerate--) {
                 generateRecord(writer);
                 if (m_DataToGenerate % 10000 == 0) {
                     System.out.println("generated " + m_DataToGenerate);
                     // increment a counter so the job isnt killed
                     HadoopUtilities.keepAlive(context);
                 }
             }
             writer.close();
        }

        public SimulatedPartitioner getPartitioner()
        {
            return m_Partitioner;
        }

        public void reduce(Text key, Iterable<IntWritable> values,
                           Context context
        ) throws IOException, InterruptedException
        {
         }

        private void generateRecord(SequenceFile.Writer pWriter)
        {
            try {
                SimulatedPartitioner sp = getPartitioner();
                SAMRecord[] records = sp.buildRecordPair();
                ONLY_KEY.set(records[0].getReadName());
                for (int i = 0; i < records.length; i++) {
                    SAMRecord record = records[i];
                    ONLY_VALUE.set(record.format());
                    pWriter.append(ONLY_KEY, ONLY_VALUE);
                }
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }

        }

        protected SequenceFile.Writer buildSequenceWriter(Context context)
        {

            Configuration c = context.getConfiguration();
            String dir = c.get(OUTPUT_BUCKET_KEY, DEFAULT_BUCKET);
            FileSystem fs = HDFSUtilities.getFileSystem(context);
            final Path home = fs.getHomeDirectory();
            Path dirPath = new Path(home, dir);

            try {
                SequenceFile.CompressionType compressionType = SequenceFile.CompressionType.BLOCK;
                CompressionCodec codec = new BZip2Codec(); //new GzipCodec();
                fs.mkdirs(dirPath);
                int index = 1;
                Path path1 = new Path(dirPath, "data" + index++ + "_" + getFactory().getDistinguisher() + ".seq");

                while(fs.exists(path1)) {
                    path1 = new Path(dirPath, "data" + index++ + "_" + getFactory().getDistinguisher() + ".seq");
                }
                String pathStr = path1.toUri().toString();  // just to look for debugging

                SequenceFile.Writer writer = SequenceFile.createWriter(fs, c, path1, Text.class,
                        Text.class, compressionType, codec);

                return writer;
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }

        }

    }


    public static void main(String[] args) throws Exception
    {
        String fileName = DEFAULT_BUCKET;
        if (args.length > 0)
            fileName = args[0];

        int NRecords = DEFAULT_NUMBER_RECORDS;
        if (args.length > 1)
            NRecords = Integer.parseInt(args[1]);

        Configuration conf = new Configuration();
        String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();
//        if (otherArgs.length != 2) {
//            System.err.println("Usage: wordcount <in> <out>");
//            System.exit(2);
//        }
        Job job = new Job(conf, "Cluster Loader");
        conf = job.getConfiguration(); // NOTE JOB Copies the configuraton
        job.setJarByClass(ClusterLoadingJob.class);
        job.setMapperClass(TokenizerMapper.class);
        //    job.setCombinerClass(IntSumReducer.class);
        job.setReducerClass(DataGenerator.class);

        job.setMapOutputKeyClass(IntWritable.class);
        job.setMapOutputValueClass(Text.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);

        job.setNumReduceTasks(20);

        conf.setInt(NUMBER_RECORDS_KEY, NRecords);
        conf.set(OUTPUT_BUCKET_KEY, fileName);

        if (otherArgs.length > 1) {
            FileInputFormat.addInputPath(job, new Path(otherArgs[0]));
        }
        else {
            FileInputFormat.addInputPath(job, new Path("/user/jpatterson/input/moby"));

        }

        String athString = "simout";
        if (otherArgs.length > 1)
            athString = otherArgs[otherArgs.length - 1];
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