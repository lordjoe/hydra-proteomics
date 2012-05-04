package org.systemsbiology.xtandem.hadoop;

import org.apache.hadoop.conf.*;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.input.*;
import org.apache.hadoop.mapreduce.lib.output.*;
import org.apache.hadoop.util.*;
import org.systemsbiology.hadoop.*;

import java.io.*;

/**
 * org.systemsbiology.xtandem.hadoop.JXTantemConsolidator
 * Last job in hadoop cascade - this writes an output file
 * <p/>
 * as stand alone from largeSample use the followiing
 * JXTandemOutput2 JXTandemOutput3  -D org.systemsbiology.xtandem.params=tandem.params -D org.systemsbiology.xtandem.hdfs.host=Glados -D org.systemsbiology.xtandem.hdfs.basepath=/user/howdah/JXTandem/data/largeSample
 * User: steven
 * Date: 3/7/11
 */
public class JXTantemConsolidator extends ConfiguredJobRunner implements IJobRunner {
    public static final JXTantemConsolidator[] EMPTY_ARRAY = {};


    public String[] handleConfiguration(final String pArg, final Configuration pConf) {
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
        lines = HadoopUtilities.readConfigFileFS(configUrl);
        pConf.setStrings(HadoopUtilities.CONFIGURATION_KEY, lines);
        return lines;
    }

    public int runJob(Configuration conf, final String[] args) throws Exception {
        try {
            String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();
            // GenericOptionsParser stops after the first non-argument
            otherArgs = XTandemHadoopUtilities.handleGenericInputs(conf, otherArgs);
            Job job = new Job(conf, "JXTandemConsolidator");
            setJob(job);
            conf = job.getConfiguration(); // NOTE JOB Copies the configuraton
            // make default settings
            XTandemHadoopUtilities.setDefaultConfigurationArguments(conf);
            long original = 0;

            conf = job.getConfiguration(); // maybe we make a copy


            if (JXTandemLauncher.isSequenceFilesUsed()) {
                job.setInputFormatClass(SequenceFileInputFormat.class);
            }
            else {
                job.setInputFormatClass(ScoredScanXMLInputFormat.class);
            }
            job.setOutputFormatClass(TextOutputFormat.class);

            job.setJarByClass(JXTantemConsolidator.class);

            // Move the work of generating the text into the mappers
            boolean buildBoimlInMapper = true;
            if (buildBoimlInMapper) {
                job.setMapperClass(BoimlWritingMapper.class);
                job.setReducerClass(XTandemConcatenatingWritingReducer.class);
            }
            else {
                job.setMapperClass(ScanScoreMapper.class);
                job.setReducerClass(XTandemXMLWritingReducer.class);
            }


            job.setMapOutputKeyClass(Text.class);
            job.setMapOutputValueClass(Text.class);

            job.setOutputKeyClass(Text.class);
            job.setOutputValueClass(Text.class);

            // this is important - we are generating a single output file
            job.setNumReduceTasks(1);

            XTandemHadoopUtilities.setInputArguments(otherArgs, job);

            String athString = otherArgs[otherArgs.length - 1];
            if(athString.startsWith("s3n://"))
                athString = athString.substring(athString.lastIndexOf("s3n://"));
             Path outputDir = new Path(athString);
             System.err.println("Consolidator Output path " + athString);
            XTandemHadoopUtilities.setOutputDirecctory(outputDir, job);
            FileSystem fileSystem = outputDir.getFileSystem(conf);

              boolean ans = job.waitForCompletion(true);
            if(ans)
                   XTandemHadoopUtilities.saveCounters(fileSystem,  XTandemHadoopUtilities.buildCounterFileName(this,conf),job);
            int ret = ans ? 0 : 1;
            if (!ans)
                throw new IllegalStateException("Job Failed");
            return ret;
        }
        catch (IOException e) {
            throw new RuntimeException(e);

        }
        catch (IllegalStateException e) {
            throw new RuntimeException(e);

        }
        catch (InterruptedException e) {
            throw new RuntimeException(e);

        }
        catch (ClassNotFoundException e) {
            throw new RuntimeException(e);

        }
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
        Configuration conf = new Configuration();
        return runJob(conf, args);
    }

    public static void main(String[] args) throws Exception {
        int res = ToolRunner.run(new Configuration(), new JXTantemConsolidator(), args);
        //      conf.set(BamHadoopUtilities.CONF_KEY,"config/MotifLocator.config");
    }
}
