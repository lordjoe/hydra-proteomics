package org.systemsbiology.hadoop;


import org.apache.hadoop.conf.*;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.input.*;
import org.apache.hadoop.mapreduce.lib.output.*;
import org.apache.hadoop.util.*;

import java.io.*;

/**
 * org.systemsbiology.hadoop.SubstringFinder
 * a very simple job similar to the task of creating a database for protein fragments
 * The first job reads a set of strings similar to a fasta file and finde all unique substrings of
 * length 5 - 15
 * The next job assigns a mass to each substring - assume the string is a set of amino acids and
 * writes one file for each mass (a varient can add to hbase but I do noit want to assume hbase is present
 *
 * The reason the code is interesting is
 * 1) A smallish input file generates a lot of IO unlike typical tasks where the big data is present on input
 * 2) There are two jobs run in succession
 * 3) The last reducer writes to HDFS (or with changes to HBase creating a permanant record
 *
 */
public class SubstringFinder extends ConfiguredJobRunner implements IJobRunner {

    public static final int MINIMUM_SUBSTRING = 5;
    public static final int MAXIMUM_SUBSTRING = 16;

    /**
     * if the String ia a protein it will return the mass as the
     *   sum oa amino acid masses
     * @param s  !null string
     * @return  mass as a set os amino acids
     */
    public static double findMass(String s) {
        double mass = 0;
        for (int i = 0; i < s.length(); i++)
            mass += findCharMass(s.charAt(i));
        return mass;
    }

     /**
      *
      *
      *  */
    public static double findCharMass(char c) {
        switch (Character.toUpperCase(c)) {
            case 'A':
                return 71.0788;
            case 'B':
                return 114.1038;    // Same as N
            case 'C':
                return 103.1388;
            case 'D':
                return 115.0886;
            case 'E':
                return 129.1155;
            case 'F':
                return 147.1766;
            case 'G':
                return 57.0519;
            case 'H':
                return 137.1411;
            case 'I':
                return 113.1594;
            case 'J':
                return 0.0;
            case 'K':
                return 128.1741;
            case 'L':
                return 113.1594;
            case 'M':
                return 131.1926;
            case 'N':
                return 114.1038;
            case 'O':
                return 114.1038;    // Same as N
            case 'P':
                return 97.1167;
            case 'Q':
                return 128.1307;
            case 'R':
                return 156.1875;
            case 'S':
                return 87.0782;
            case 'T':
                return 101.1051;
         //   case 'U':
         //       return 0.0;
            case 'V':
                return 99.1326;
            case 'W':
                return 186.2132;
            case 'X':
                return 113.1594;
            case 'Y':
                return 163.1760;
            case 'Z':
                return 128.1307;
            default:
                return 100;
        }
    }

    public static final Text gOnlyKey = new Text();
    public static final Text gOnlyValue = new Text();
     public static void propagateSubStrings(String word, String key, TaskInputOutputContext ctx) {
        int length = word.length();


        for (int c = 0; c < length - MINIMUM_SUBSTRING; c++) {
            int maxi = Math.min(length - c, MAXIMUM_SUBSTRING);
            for (int i = MINIMUM_SUBSTRING; i < maxi; i++) {
                String sub = word.substring(c, c + i);
                int length1 = sub.length();
                if (length1 >= MINIMUM_SUBSTRING && length1 < MAXIMUM_SUBSTRING)    {
                    gOnlyKey.set(sub);
                    gOnlyValue.set(key);
                    try {
                        ctx.write(gOnlyKey,gOnlyValue);
                    }
                    catch (IOException e) {
                        throw new RuntimeException(e);

                    }
                    catch (InterruptedException e) {
                        throw new RuntimeException(e);

                    }
                }
                else
                    throw new UnsupportedOperationException("Bad substring " + sub);
            }
        }
    }


    public static class SubStringsMapper
            extends Mapper<Object, Text, Text, Text> {

        private final static Text one = new Text();
        private Text word = new Text();

        public void map(Object key, Text value, Context context
        ) throws IOException, InterruptedException {
            String inString = value.toString();
            propagateSubStrings(inString, key.toString(), context);
        }
    }


    public static class DuplicateEliminatorReducer
            extends Reducer<Text, Text, Text, Text> {
        private Text result = new Text();

        public void reduce(Text key, Iterable<Text> values,
                           Context context
        ) throws IOException, InterruptedException {
            int sum = 0;
            StringBuilder sb = new StringBuilder();


            for (Text val : values) {
                if (sb.length() > 0)
                    sb.append(",");
                sb.append(val.toString());
            }
            result.set(sb.toString());
            context.write(key, result);
        }
    }



    public static class MassMapper
            extends Mapper<Text, Text, Text, Text> {

        private final static Text onlyKey = new Text();
        private Text onlyValue = new Text();

        public void map(Text key, Text value, Context context
        ) throws IOException, InterruptedException {
            String proteinStr = value.toString();
            String[] split = proteinStr.split("\t");
            String protein = key.toString();
            double mass = findMass(protein);
            String massKey = Integer.toString((int)(100 * mass));  // round to nearest hundredth
            onlyKey.set(massKey);
            onlyValue.set(protein);
            context.write(onlyKey,onlyValue); // mass is key string is value
        }
    }


    public static class KeyWritingReducer
            extends Reducer<Text, Text, Text, Text> {
        private Text onlyValue = new Text();

        public void reduce(Text key, Iterable<Text> values,
                           Context context
        ) throws IOException, InterruptedException {
              StringBuilder sb = new StringBuilder();

            String massStr = key.toString();

            // Append values ina comma delimited list
            for (Text val : values) {
                if (sb.length() > 0)
                    sb.append(",");
                sb.append(val.toString());
            }
            String valueListStr = sb.toString();
            onlyValue.set(valueListStr);
             context.write(key,onlyValue);
        }
    }

//
//    /**
//     * Force loading of needed classes to make
//     */
//    public static final Class[] NEEDED =
//            {
//  //                  org.apache.commons.logging.LogFactory.class,
//                    org.apache.commons.cli.ParseException.class
//            };
//

    /**
     * kill a directory and all contents
     *
     * @param src
     * @param fs
     * @return
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


    public int runJob(Configuration confx, String[] args) throws Exception {
        String[] otherArgs = new GenericOptionsParser(confx, args).getRemainingArgs();
//        if (otherArgs.length != 2) {
//            System.err.println("Usage: wordcount <in> <out>");
//            System.exit(2);
//        }
        Job job = new Job(confx, "Substrings");
        Configuration conf = job.getConfiguration(); // NOTE JOB Copies the configuraton
        job.setJarByClass(SubstringFinder.class);
        job.setMapperClass(SubStringsMapper.class);
        job.setCombinerClass(DuplicateEliminatorReducer.class);
        job.setReducerClass(DuplicateEliminatorReducer.class);
        job.setInputFormatClass(FastaInputFormat.class);
        job.setOutputFormatClass( SequenceFileOutputFormat.class);

        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);


          job.setNumReduceTasks(HadoopUtilities.DEFAULT_REDUCE_TASKS);

        Path inputPath = new Path(otherArgs[0]);
        if (otherArgs.length > 1) {
            FileInputFormat.addInputPath(job, inputPath);
        }

        // you must pass the output directory as the last argument
        String athString = otherArgs[otherArgs.length - 1];
        File out = new File(athString);
//        if (out.exists()) {
//            FileUtilities.expungeDirectory(out);
//            out.delete();
//        }

        Path outputDir = new Path(athString);

        FileSystem fileSystem = outputDir.getFileSystem(confx);
        expunge(outputDir, fileSystem);    // make sure thia does not exist
        FileOutputFormat.setOutputPath(job, outputDir);

       // added Slewis
         job.setNumReduceTasks(HadoopUtilities.DEFAULT_REDUCE_TASKS);

        boolean ans = job .waitForCompletion(true);
        if(!ans )
            throw new IllegalStateException("job 1 failed");

        Job job2 = new Job(confx, "MassMapping");
        Configuration configuration = job2.getConfiguration();// NOTE JOB Copies the configuraton
        job2.setJarByClass(MassMapper.class);
         job2.setMapperClass(MassMapper.class);
         job2.setCombinerClass(DuplicateEliminatorReducer.class);
         job2.setReducerClass(KeyWritingReducer.class);
        job2.setInputFormatClass(SequenceFileInputFormat.class);

         job2.setMapOutputKeyClass(Text.class);
         job2.setMapOutputValueClass(Text.class);
         job2.setOutputKeyClass(Text.class);
         job2.setOutputValueClass(Text.class);

         FileInputFormat.addInputPath(job2, outputDir);

        Path outputDir2 = new Path(athString + "2");
        expunge(outputDir2, fileSystem);    // make sure thia does not exist
        FileOutputFormat.setOutputPath(job2, outputDir2);

         // added Slewis
         job2.setNumReduceTasks(HadoopUtilities.DEFAULT_REDUCE_TASKS);

          ans = job2.waitForCompletion(true);
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
             conf = HDFSAccessor.getSharedConfiguration();
        return runJob(conf, args);
    }

    public static final String LITTLE_TEST =
            "ABCDEFHIJKLMNOPQRSTUVWXYZABCDEFHIJKLMNOPQRSTUVWXYZ";

    public static final String BIG_TEST =
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Curabitur interdum lectus vitae ante rutrum faucibus. " +
                    "Vivamus consequat felis nec nisl eleifend at sollicitudin nisl mollis. Donec at ultricies lacus. Maecenas" +
                    " laoreet neque in lectus luctus sed porttitor lectus hendrerit. Aliquam vitae eros tortor, id sagittis mi." +
                    " Duis vulputate odio ut neque faucibus tristique. Aliquam ultricies scelerisque risus at pulvinar. Donec at " +
                    "arcu leo. Proin pretium interdum fringilla. Vivamus metus nulla, pulvinar at adipiscing non, tempus et odio. " +
                    "Aliquam malesuada est nec ipsum faucibus sit amet tincidunt nibh interdum. Sed tempus pretium ipsum a aliquam." +
                    " Curabitur dapibus orci a sapien ornare lobortis. Phasellus molestie, nibh sit amet venenatis ultrices, quam " +
                    "orci hendrerit nisi, tempus vulputate ante elit eu libero. Donec commodo consectetur mi et porttitor.";


    public static void main(String[] args) throws Exception {
       // propagateSubStrings(LITTLE_TEST, "", null);

          ToolRunner.run(new SubstringFinder(), args);
    }
}