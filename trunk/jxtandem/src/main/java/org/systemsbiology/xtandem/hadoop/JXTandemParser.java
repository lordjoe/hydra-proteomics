package org.systemsbiology.xtandem.hadoop;


import org.apache.hadoop.conf.*;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.*;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.Counters;
import org.apache.hadoop.mapreduce.lib.input.*;
import org.apache.hadoop.mapreduce.lib.output.*;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.*;
import org.apache.openjpa.persistence.criteria.*;
import org.systemsbiology.hadoop.*;
import org.systemsbiology.xtandem.*;
import org.systemsbiology.xtandem.peptide.*;

import javax.imageio.*;
import java.io.*;
import java.util.*;


/**
 * org.systemsbiology.xtandem.hadoop.JXTandemParser
 */
public class JXTandemParser extends ConfiguredJobRunner implements IJobRunner {

    public static final int MAX_TEST_PROTEINS = 0; // 2000;
    public static final int REPORT_INTERVAL_PROTEINS = 10000; // 2000;



    public static class ProteinMapper extends AbstractTandemMapper<Text> {

        private FastaHadoopLoader m_Loader;
        private int m_Proteins;
        private int m_ProteinsReported;

        public FastaHadoopLoader getLoader() {
            return m_Loader;
        }


        public void incrementNumberMappedProteins(Context context) {
            Counter counter = context.getCounter("Parser", "TotalProteins");
            counter.increment(1);
        }

        public void incrementNumberDecoysProteins(Context context) {
            Counter counter = context.getCounter("Parser", "TotalDecoyProteins");
            counter.increment(1);
        }

        /**
         * Called once at the beginning of the task.
         */
        @Override
        protected void setup(final Context context) throws IOException, InterruptedException {
            super.setup(context);
            HadoopTandemMain application = getApplication();
            //    application.loadTaxonomy();
            m_Loader = new FastaHadoopLoader(application);

            long freemem = setMinimalFree();
            XTandemUtilities.outputLine("Free Memory " + String.format("%7.2fmb", freemem / 1000000.0) +
                    " minimum " + String.format("%7.2fmb", getMinimumFreeMemory() / 1000000.0));
        }

        @Override
        public void map(Text key, Text value, Context context
        ) throws IOException, InterruptedException {

            String label = key.toString();
            label = XTandemUtilities.conditionProteinLabel(label);
            String sequence = value.toString();
            // drop terminating *
            if(sequence.endsWith("*"))
                sequence = sequence.substring(0,sequence.length() - 1);

            if (label.toUpperCase().contains("DECOY"))
                incrementNumberDecoysProteins(context);
            incrementNumberMappedProteins(context);
            FastaHadoopLoader loader = getLoader();
            loader.handleProtein(label, sequence, context);
            //  context.write(key, value);

            if (m_Proteins++ % REPORT_INTERVAL_PROTEINS == 0) {
                showStatistics();
              }
        }

        private void showStatistics() {
            ElapsedTimer elapsed = getElapsed();
            elapsed.showElapsed("Processed " + (m_Proteins - m_ProteinsReported) + " proteins at " + XTandemUtilities.nowTimeString() +
                    " total " + m_Proteins);
            // how much timeis in my code
            m_ProteinsReported = m_Proteins;

            long freemem = setMinimalFree();
            XTandemUtilities.outputLine("Free Memory " + String.format("%7.2fmb", freemem / 1000000.0) +
                    " minimum " + String.format("%7.2fmb", getMinimumFreeMemory() / 1000000.0));
            elapsed.reset();
        }

        /**
         * Called once at the end of the task.
         */
        @Override
        protected void cleanup(final Context context) throws IOException, InterruptedException {
            super.cleanup(context);    //To change body of overridden methods use File | Settings | File Templates.
            System.err.println("cleanup up Parser map");
            FastaHadoopLoader loader = getLoader();
            long numberPeptides = loader.getFragmentIndex();
            Counter counter = context.getCounter("Parser", "NumberFragments");
            counter.increment(numberPeptides);
            System.err.println("cleanup up Parser map done");
        }
    }

    /**
     * special class to combine fragments
     */
    public static class FragmentCombiner extends AbstractTandemReducer {


        public void reduceNormal(Text key, Iterable<Text> values,
                                 Context context) throws IOException, InterruptedException {

           String sequence = key.toString();
            StringBuilder sb = new StringBuilder();
            for (Text val : values) {
                if (sb.length() > 0)
                    sb.append(";");
                sb.append(val.toString());
            }
            Text onlyValue = getOnlyValue();
            onlyValue.set(sb.toString());
            context.write(key, onlyValue);
        }
    }

    public static class ProteinReducer extends AbstractTandemReducer {

        private long m_NumberUniquePeptides;

        public void incrementNumberReducedFragments(Context context) {
            Counter counter = context.getCounter("Parser", "NumberUniqueFragments");
            counter.increment(1);
            m_NumberUniquePeptides++;
          }
        /**
         * Called once at the beginning of the task.
         */
        @Override
        protected void setup(final Context context) throws IOException, InterruptedException {
            super.setup(context);
            System.err.println("Setting up Parser reduce");
        }


        public void reduceNormal(Text key, Iterable<Text> values,
                                 Context context) throws IOException, InterruptedException {

            String sequence = key.toString();
            int totalDuplicates = 0;

            //  System.err.println("handling sequence " + sequence);

            IPolypeptide pp = Polypeptide.fromString(sequence);

//Only XScore DMQYFE  832.318
//Only XScore EGNELYK  834.399
//Only XScore EKDNTDE  832.332
//Only XScore IQPTRMS  832.435
//Only XScore LIVKYLS  835.529
//
//            String rawSequence = pp.getSequence();
//            if (
//                    "DMQYFE".equals(rawSequence) ||
//                            "EGNELYK".equals(rawSequence) ||
//                            "EKDNTDE".equals(rawSequence) ||
//                            "IQPTRMS".equals(rawSequence) ||
//                            "LIVKYLS".equals(rawSequence)
//                    )
//                XTandemUtilities.breakHere();
//
            if(sequence.startsWith("AVLEFTPETPSPLIGILENK"))
                 XTandemUtilities.breakHere();

            StringBuilder sb = new StringBuilder();
            // should work even if we use a combiner
            for (Text val : values) {
                totalDuplicates++;
                if (sb.length() > 0)
                    sb.append(";");
                String str = val.toString();
                sb.append(str);
            }
            String proteins = sb.toString();

            HadoopTandemMain application = getApplication();
            MassType massType = application.getMassType();
            String keytr;
            Text onlyKey = getOnlyKey();
            Text onlyValue = getOnlyValue();
            switch (massType) {
                case monoisotopic:

                    double mMass = pp.getMatchingMass();
                    int monomass = XTandemUtilities.getDefaultConverter().asInteger(mMass);
                    keytr = String.format("%06d", monomass);
                    onlyKey.set(keytr);
                    onlyValue.set(sequence + "," + String.format("%10.4f", mMass) + "," + monomass + "," + proteins);
                    break;
                case average:
                    double aMass = pp.getMatchingMass();
                    int avgmass = XTandemUtilities.getDefaultConverter().asInteger(aMass);
                    keytr = String.format("%06d", avgmass);
                    onlyKey.set(keytr);
                    onlyValue.set(sequence + "," + aMass + "," + avgmass + "," + proteins);
                    break;

            }
            incrementNumberReducedFragments(context);
             context.write(onlyKey, onlyValue);
            if (m_NumberUniquePeptides % REPORT_INTERVAL_PROTEINS == 0) {
                showReduceStatistics();
            }
            // bin number of duplicates to see if a combiner will help
            Counter counter = context.getCounter("Parser", String.format("DuplicatesOfSize%04d", totalDuplicates));
            counter.increment(1);
        }


        private void showReduceStatistics() {
            ElapsedTimer elapsed = getElapsed();
            elapsed.showElapsed("Processed " + m_NumberUniquePeptides + " peptides at " + XTandemUtilities.nowTimeString());
            // how much timeis in my code

            long freemem = setMinimalFree();
            XTandemUtilities.outputLine("Free Memory " + String.format("%7.2fmb", freemem / 1000000.0) +
                    " minimum " + String.format("%7.2fmb", getMinimumFreeMemory() / 1000000.0));
            elapsed.reset();
        }


        /**
         * Called once at the end of the task.
         */
        @Override
        protected void cleanup(final Context context) throws IOException, InterruptedException {
            System.err.println("Handled " + m_NumberUniquePeptides + "Unique peptides");
            super.cleanup(context);
        }

    }

    /**
     * Execute the command with the given arguments.
     *
     * @param args command specific arguments.
     * @return exit code.
     * @throws Exception
     */
    public int runJob(Configuration conf, final String[] args) {
        if (args.length == 0)
            throw new IllegalStateException("needs a file name");

        try {
            GenericOptionsParser gp = new GenericOptionsParser(conf, args);

            String[] otherArgs = gp.getRemainingArgs();
            // GenericOptionsParser  stops at the first non-define
            otherArgs = XTandemHadoopUtilities.internalProcessArguments(conf, args);
//        if (otherArgs.length != 2) {
//            System.err.println("Usage: wordcount <in> <out>");
//            System.exit(2);
//        }


            Job job = new Job(conf, "Fasta Format");
            setJob(job);
            conf = job.getConfiguration(); // NOTE JOB Copies the configuraton
            // make default settings
            XTandemHadoopUtilities.setDefaultConfigurationArguments(conf);

            // force the splitter to use more mappers
            XTandemHadoopUtilities.addMoreMappers(conf);

            String params = conf.get(XTandemHadoopUtilities.PARAMS_KEY);
            if (params == null)
                conf.set(XTandemHadoopUtilities.PARAMS_KEY, otherArgs[0]);
            job.setJarByClass(JXTandemParser.class);
            job.setInputFormatClass(FastaInputFormat.class);
            job.setMapperClass(ProteinMapper.class);
            job.setReducerClass(ProteinReducer.class);
            // try to reduce duplicates in a combiner
            // is the combiner a bad idea???
            //job.setCombinerClass(FragmentCombiner.class);

            job.setMapOutputKeyClass(Text.class);
            job.setMapOutputValueClass(Text.class);
            job.setOutputKeyClass(Text.class);
            job.setOutputValueClass(Text.class);

            if (JXTandemLauncher.isSequenceFilesUsed())
                job.setOutputFormatClass(SequenceFileOutputFormat.class);
            else
                job.setOutputFormatClass(TextOutputFormat.class);


            // Do not set reduce tasks - ue whatever cores are available
            // this does not work just set a number for now
            XTandemHadoopUtilities.setRecommendedMaxReducers(job);

            if (otherArgs.length > 1) {
                for (int i = 0; i < otherArgs.length - 1; i++) {
                    String inputFile = otherArgs[i];
                    String remoteDirectory = conf.get(XTandemHadoopUtilities.PATH_KEY);
                    if (remoteDirectory != null && !inputFile.startsWith(remoteDirectory))
                        inputFile = remoteDirectory + "/" + inputFile;

                    XTandemHadoopUtilities.setInputPath(  job, inputFile);
                }
            }

            // you must pass the output directory as the last argument
            String athString = otherArgs[otherArgs.length - 1];


            if(athString.startsWith("s3n://"))
               athString = athString.substring(athString.lastIndexOf("s3n://"));
            Path outputDir = new Path(athString);
            System.err.println("Output path Parser  " + athString);

            FileSystem fileSystem = outputDir.getFileSystem(conf);
            XTandemHadoopUtilities.expunge(outputDir, fileSystem);    // make sure thia does not exist
            FileOutputFormat.setOutputPath(job, outputDir);


            boolean ans = job.waitForCompletion(true);
            if(ans)
                XTandemHadoopUtilities.saveCounters(fileSystem,  XTandemHadoopUtilities.buildCounterFileName(this,conf),job);


            int ret = ans ? 0 : 1;

            //    if (numberMapped != numberReduced)
            //       throw new IllegalStateException("problem"); // ToDo change

            if (!ans)
                throw new IllegalStateException("Job Failed");


            return ret;
        }
        catch (IOException e) {
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
        //      conf.set(BamHadoopUtilities.CONF_KEY,"config/MotifLocator.config");
        return runJob(conf, args);
    }


    public static void main(String[] args) throws Exception {
        ToolRunner.run(new JXTandemParser(), args);
    }
}