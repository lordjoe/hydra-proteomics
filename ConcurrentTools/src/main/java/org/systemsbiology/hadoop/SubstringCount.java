package org.systemsbiology.hadoop;


import org.apache.hadoop.conf.*;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.input.*;
import org.apache.hadoop.mapreduce.lib.output.*;
import org.apache.hadoop.util.*;

import java.io.*;
import java.util.*;
 /**
 * org.systemsbiology.hadoop.SubstringGenerator
  *
  * This illustrates an issue we are having where a mapper generating a much larger volume of
  * data ans number of records times out even though the code is small, simple and fast
  *
  * NOTE!!! as written the program will generate a 4GB file in hdfs with good input data -
  * this is done only if the file does not exist but may take several hours. It will only be
  * done once. After that the failure is fairly fast
  *
 * What this will do is count  unique Substrings of lines of length
 * between MIN_SUBSTRING_LENGTH and MAX_SUBSTRING_LENGTH by generatin all
 * substrings and  then using the word could algorithm
 * What is interesting is that the number and volume or writes in the
  * map phase is MUCH larger than the number of reads and the volume of read data
  *
  * The example is artificial but similar the some real BioInformatics problems -
  *  for example finding all substrings in a gemome can be important for the design of
  *  microarrays.
  *
  *  While the real problem is more complex - the problem is that
  *  when the input file is large enough the mappers time out failing to report after
  *  600 sec. There is nothing slow in any of the application code and nothing I
 */
public class SubstringCount extends Configured implements Tool , IJobRunner {


    public static final long ONE_MEG = 1024 * 1024;
    public static final long ONE_GIG = 1024 * ONE_MEG;
    public static final int LINE_LENGTH = 100;
    public static final Random RND = new Random();

   // NOTE - edit this line to be a sensible location in the current file system
    public static final String INPUT_FILE_PATH = "RandomLines.txt";
   // NOTE - edit this line to be a sensible location in the current file system
    public static final String OUTPUT_FILE_PATH = "output";
     // NOTE - edit this line to be the input file size - 4 * ONE_GIG should be large but not a problem
    public static final long DESIRED_LENGTH = 4 * ONE_GIG;
    // NOTE - limits on substring length
    public static final int MINIMUM_LENGTH = 5;
    public static final int MAXIMUM_LENGTH = 32;


    /**
     * create an input file to read
     * @param fs !null file system
     * @param p  !null path
     * @throws IOException om error
     */
    public static void guaranteeInputFile(FileSystem fs, Path p) throws IOException {
        if (fs.isFile(p)) {
            FileStatus fileStatus = fs.getFileStatus(p);
            if (fileStatus.getLen() >= DESIRED_LENGTH)
                return;
        }
        FSDataOutputStream open = fs.create(p);
        PrintStream ps = new PrintStream(open);
         long showInterval = DESIRED_LENGTH  / 100;
        for (long i = 0; i < DESIRED_LENGTH; i += LINE_LENGTH) {
            writeRandomLine(ps, LINE_LENGTH);
            // show progress
            if(i % showInterval == 0)  {
                System.err.print(".");

            }
        }
        System.err.println("");
        ps.close();
    }

    /**
     * write a line with a random string of capital letters
     *
     * @param pPs         -  output
     * @param pLineLength length of the line
     */
    public static void writeRandomLine(final PrintStream pPs, final int pLineLength) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < pLineLength; i++) {
            char c = (char) ('A' + RND.nextInt(26));
            sb.append(c);

        }
        String s = sb.toString();
        pPs.println(s);
    }


    private Job m_Job;

    /**
     * Construct a Configured.
     */
    public SubstringCount() {
    }


    /**
     * after the job is run return the Job
     * @return  the job
     */
    public Job getJob() {
        return m_Job;
    }

    public void setJob(final Job pJob) {
        m_Job = pJob;
    }


    /**
     * similar to the Word Count mapper but one line generates a lot more output
     */
    public static class SubStringMapper
            extends Mapper<Object, Text, Text, IntWritable> {

        /**
         * generate a array of substrings
         *
         * @param inp       input long string
         * @param minLength minimum substring length
         * @param maxLength maximum substring length
         * @return !null array of strings
         */
        public static String[] generateSubStrings(String inp, int minLength, int maxLength) {
           // guarantee no more than 100 characters
            if(inp.length() > 100)
                      inp = inp.substring(0,100);
            List<String> holder = new ArrayList<String>();
            for (int start = 0; start < inp.length() - minLength; start++) {
                for (int end = start + minLength; end < Math.min(inp.length(), start + maxLength); end++) {
                    try {
                        holder.add(inp.substring(start, end));
                    }
                    catch (Exception e) {
                        throw new RuntimeException(e);

                    }
                }
            }

            String[] ret = new String[holder.size()];
            holder.toArray(ret);
             return ret;
        }

        private final IntWritable one = new IntWritable(1);
        private final Text word = new Text();

        /**
         * Like word count except the words are all substrings of the input data
         * This leads to a large increase
         * @param key  irrelevant
         * @param value  one read line
         * @param context  !null context
            */
        public void map(Object key, Text value, Context context
        ) throws IOException, InterruptedException {
            String inp = value.toString();
            // The written data is hundreds of times the input data
            String[] data = generateSubStrings(inp, MINIMUM_LENGTH, MAXIMUM_LENGTH);
            for (int i = 0; i < data.length; i++) {
                String s = data[i];
                word.set(s);
                context.write(word, one);
            }
        }
    }

    /**
     * Essentially the same reducer used by word count
     */
    public static class IntSumReducer
            extends Reducer<Text, IntWritable, Text, IntWritable> {
        private final IntWritable result = new IntWritable();

        public void reduce(Text key, Iterable<IntWritable> values,
                           Context context
        ) throws IOException, InterruptedException {
            int sum = 0;
            for (IntWritable val : values) {
                sum += val.get();
            }
            result.set(sum);
            context.write(key, result);
        }
    }


    /**
     * kill a directory and all contents
     * useful to make sure the output directory is empty
     * @param src !null path of a directory
     * @param fs !null file system
     * @return  true on success
     */
    public static boolean expunge(Path src, FileSystem fs) {


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

     /**
      * this implementation of run allows the program to start with a Configuration which may
      * have been filled by other code
      * @param conf !null conf
      * @param args  !null arguments
      * @return 0 on success
      * @throws Exception  on error
      */
    public int runJob(Configuration conf, String[] args) throws Exception {
        String[] realArgs = new String[2];

        String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();

        if(otherArgs.length > 0)
             realArgs[0] = otherArgs[0];
         else
             realArgs[0] = INPUT_FILE_PATH;
        if(otherArgs.length > 1)
             realArgs[1] = otherArgs[1];
         else
             realArgs[1] = OUTPUT_FILE_PATH;

        Path InputPath = new Path(realArgs[0]);
        Path outputDir =  new Path(realArgs[1]);

         Job job = new Job(conf, "Substring Generator");
        conf = job.getConfiguration(); // NOTE JOB Copies the configuraton
        job.setJarByClass(SubstringCount.class);
        job.setMapperClass(SubStringMapper.class);
     //   job.setCombinerClass(IntSumReducer.class);
        job.setReducerClass(IntSumReducer.class);

        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(IntWritable.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

         job.setNumReduceTasks(72); // todo size for your cluster
        FileInputFormat.addInputPath(job,InputPath);


        FileSystem fileSystem = outputDir.getFileSystem(conf);
        expunge(outputDir, fileSystem);    // make sure thia does not exist
        FileOutputFormat.setOutputPath(job, outputDir);

        // Now create something for the job to read
        guaranteeInputFile(fileSystem, InputPath);


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
             conf = new Configuration();

        return runJob(conf, args);
    }

    /**
     *
     * @param args  args[0] is the path to a file to be created in the FileSystem
     * args[1] is the path to an output directory in the file system - the contents WILL be deleted
     * @throws Exception  on error
     */
    public static void main(String[] args) throws Exception {
        ToolRunner.run(new SubstringCount(), args);
    }
}