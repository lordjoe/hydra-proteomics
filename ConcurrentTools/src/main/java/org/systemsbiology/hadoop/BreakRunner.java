package org.systemsbiology.hadoop;

import com.lordjoe.utilities.*;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.input.*;
import org.apache.hadoop.mapreduce.lib.output.*;
import org.apache.hadoop.util.*;
 import org.systemsbiology.common.*;
import org.systemsbiology.sam.*;

import java.io.*;
import java.util.*;

/**
 * org.systemsbiology.hadoop.BreakRunner
 *
 * @author Steve Lewis
 * @date May 11, 2010
 */
public class BreakRunner extends GenomeRunner {
    public static BreakRunner[] EMPTY_ARRAY = {};
    public static Class<BreakRunner> THIS_CLASS = BreakRunner.class;

    public static final Class DEFAULT_MAPPER = PartitioningMapper.class;
    public static final Class DEFAULT_REDUCER = PartitionReducer.class;
  //  public static final Class DEFAULT_COMBINER = PartitionReducer.class;
    public static final Class DEFAULT_PARTITIONER = ChromosomePartitioner.class;

    public BreakRunner() {
        setMapperClass(DEFAULT_MAPPER);
        setReducerClass(DEFAULT_REDUCER);
        // Combiners are not needed and a bad ides when the mapper and recucer  use different keys
      // setCombinerClass(DEFAULT_COMBINER);
    }



    public  String[] handleConfiguration(final String pArg, final Configuration pConf) {
        String[] lines;
        String configUrl = pConf.get(HadoopUtilities.CONF_KEY);
        if (configUrl == null)

        {
            System.err.println("Could not find config");
            configUrl = pArg;
        }

        if (configUrl.contains("="))
            configUrl = configUrl.split("=")[1];
        System.err.println("Config file is " + configUrl);
        lines = readConfigFileFS(configUrl);
//        System.err.println("\nConfiguration " + configUrl + "\n");
//        for (int i = 0; i < lines.length; i++) {
//            String line = lines[i];
//            System.err.println(line);
//        }
//        System.err.println("\n End Configuration\n");
        pConf.setStrings(HadoopUtilities.CONFIGURATION_KEY, lines);
        return lines;
    }



    /**
     * GenericOptionsParser is not doing its job so we add this
     *
     * @param conf !null conf
     * @param args arguments to process
     * @return !null unprocessed arguments
     */
    public static String[] internalProcessArguments(Configuration conf, String[] args) {
        List<String> holder = new ArrayList<String>();
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            if ("-D".equals(arg)) {
                String define = args[++i];
                processDefine(conf, define);
                continue;
            }
            if ("-filecache".equals(arg)) {
                String file = args[++i];
                if (true)
                    throw new UnsupportedOperationException("Fix This"); // ToDo
                continue;
            }

            holder.add(arg); // not handled
        }

        String[] ret = new String[holder.size()];
        holder.toArray(ret);
        return ret;
    }


    protected static void processDefine(Configuration conf, String arg) {
        String[] items = arg.split("=");
        conf.set(items[0].trim(), items[1].trim());

    }


    public int run(String[] args) throws Exception {
//        System.out.println("Hello.x World");
        String[] originalArgs = args;
        Configuration conf = getConf();
        args = new GenericOptionsParser(conf, args).getRemainingArgs();

        args =  internalProcessArguments(conf, args);

        String out = args[1];
        System.err.println("\nARGS" + "\n");
        for (int i = 0; i < args.length; i++) {
            String line = args[i];
            System.err.println(line);
        }
        String defaultConf = null;
        if(args.length > CONF_POSITION)
             defaultConf = args[CONF_POSITION];
        
        String[] lines = handleConfiguration(defaultConf, conf);

        SamConfigurer sc = SamConfigurer.getInstance();
        sc.configure(lines);

        String fileName = conf.get(HadoopUtilities.REPORT_KEY);
        System.err.println("Report file is " + fileName);
        if (fileName.contains("="))
            fileName = fileName.split("=")[1];

        String report = null;
        if (fileName.startsWith("s3n://"))

        {
            throw new UnsupportedOperationException("Move to awshadoop"); // ToDo
        //    report = AWSUtilities.readFileTextS3(fileName);
        }

        else

        {
            File reportFile = new File(fileName);
            if (reportFile.exists()) {
                report = FileUtilities.readInFile(reportFile);
                //  System.err.println(report);
            }
        }

        if (report != null)
        {
            conf.set(HadoopUtilities.STATISTICS_REPORT_KEY, report);
        }

        else

        {
            System.err.println("cannot find " + fileName);

        }
//        String fileSpec = args[IN_POSITION_START];
//        File[] inputs = CommonUtilities.getWildCardFiles(fileSpec);
//        System.err.println("Running Gene Job");
        String test = conf.get("tmpfiles");
        if (test == null)
            test = args[0];
        String[] files;
        if(test.contains(","))    {
            files = test.split(",");
        }
        else    {
            String[] data = { test.replace("File:", "")};
            files =  data;
        }


        runGenomeJob(conf, out, files, lines);

        return 0;
    }


    protected int runGenomeJob(Configuration conf, String out, String[] inputs, String[] configuration)
            throws IOException, InterruptedException, ClassNotFoundException {
        // cannot use an existing directory

        if (inputs.length == 0)
            return 1;
//
        String inputPath = inputs[0];

        EventTimer evt = new EventTimer();
        //      String inputPath = inp.getAbsolutePath();

        Job job = new Job(conf, "Gene Pipeline");
        conf = job.getConfiguration(); // NOTE JOB Copies the configuraton
        job.setJarByClass(getClass());

        Class<? extends InputFormat<?, ?>> aClass1 = job.getInputFormatClass();
        Class<? extends OutputFormat<?, ?>> aClass = job.getOutputFormatClass();
        

        conf.setClass("mapred.output.key.comparator.class", GenomicPartitionKeyComparator.class, WritableComparator.class);
        if (configuration != null) {
            System.err.println("================================ Setting configuration =================================");
            conf.setStrings(HadoopUtilities.CONFIGURATION_KEY, configuration);
            //     for (int i = 0; i < configuration.length; i++) {
            //         System.err.println(configuration[i]);
            //     }
        }
        conf.set(HadoopUtilities.INPUT_FILE_KEY, inputPath);


        /**
         * add a default header - this may be reset
         */
        conf.set(HadoopUtilities.HEADER_KEY.toString(), HadoopUtilities.DEFAULT_HEADER_TEXT);
        job.setJobName("Analysis");
        String inputName = buildJobName(inputPath);
        conf.set(HadoopUtilities.INPUT_NAME_KEY, inputName);

        // Limit number of records
        conf.setInt(HadoopUtilities.MAXRECORDS_KEY, HadoopUtilities.MAXIMUM_PARTITION_RECORDS);

        conf.setInt("mapred.tasktracker.map.tasks.maximum",4);
        conf.setInt("mapred.tasktracker.reduce.tasks.maximum",2);

        job.setJobName(inputName);

        Path outputDir = new Path(out);

        FileOutputFormat.setOutputPath(job, outputDir);

        for (int i = 0; i < inputs.length; i++) {
            String input = inputs[i];
            FileInputFormat.addInputPath(job, new Path(input));

        }

        FileInputFormat.setMinInputSplitSize(job, MIN_SPLIT_SIZE);

        final Class mapperClass = getMapperClass();
        System.out.println(" Mapper - " + mapperClass);
        job.setMapperClass(mapperClass);

        // Combiners are not needed and a bad ides when the mapper and recucer  use different keys
//        final Class combinerClass = getCombinerClass();
//            System.out.println(" combinerClass - " + combinerClass);
//             job.setCombinerClass(combinerClass);

        final Class reducerClass = getReducerClass();
        System.out.println(" Reducer - " + reducerClass);
        job.setReducerClass(reducerClass);

        // Try using compression
        // NOTE drop compression for development
       // FileOutputFormat.setCompressOutput(job, true);

//        Job reduceConf = new Job(false);
//          ChainReducer.setReducer(conf, reducerClass, LongWritable.class, Text.class,
//          Text.class, Text.class, true, reduceConf);
//
//
//
//        ChainReducer.addMapper(conf, CMap.class, Text.class, Text.class,
//          LongWritable.class, Text.class, false, null);


        // todo group
        // group and partition by the first int in the pair

        job.setPartitionerClass(GenomicKeyPartitioner.class);
        job.setMapOutputKeyClass(/* Text */ GenomePartitionKey.class);
        job.setOutputValueClass(Text.class);
        // Compare chromosome plus position
           job.setSortComparatorClass(GenomicPartitionKeyValueComparator.class);
        // compare window only
        // job.setSortComparatorClass(GenomicPartitionKeyComparator.class);
          job.setGroupingComparatorClass(GenomicPartitionKeyComparator.class);


//
//        //   job.setPartitionerClass(getPartitionerClass());
//        for (int i = 0; i < inputs.length; i++) {
//            File input = inputs[i];
//            inputPath = input.getAbsolutePath();
//            Path toProcess = new Path(inputPath);
//            FileInputFormat.addInputPath(job, toProcess);
//
//        }

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);

       
     //   job.setNumReduceTasks(DEFAULT_REDUCE_TASKS);


        try {
            boolean ans = job.waitForCompletion(true);
            int ret = ans ? 0 : 1;
            System.out.println("Processed " + /*inputName + */ " in " + evt);

            return ret;
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

    /**
     * All partitioned GZIP files start with
     * 
     */
    public static final String FIRST_NUMBER_PART = "00000001";

    @Override
    protected String buildJobName(String inputName) {

        inputName = inputName.replace(FIRST_NUMBER_PART, "");
        inputName = inputName.replace(".sam.gz", "");
        return inputName;
    }


    public static void main(String[] args) throws Exception {
    //    if (args.length > 0)
     //       FileUtils.deleteDirectory(new File(args[args.length - 1]));
        //   args = new String[]{"data/input", "data/output"};
        ToolRunner.run(new Configuration(), new BreakRunner(), args);
    }
}
