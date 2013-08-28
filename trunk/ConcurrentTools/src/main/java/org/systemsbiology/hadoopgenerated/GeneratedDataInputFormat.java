package org.systemsbiology.hadoopgenerated;

import java.io.*;
import java.util.*;

import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;

import org.apache.hadoop.mapreduce.InputSplit;


import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.Reporter;
import org.apache.hadoop.mapreduce.RecordReader;

/**
 * org.systemsbiology.hadoopgenerated.GeneratedDataInputFormat
 *   see http://codedemigod.com/blog/?p=120
 * @author Steve Lewis
 * @date Oct 10, 2010
 */
public class GeneratedDataInputFormat extends InputFormat<LongWritable, LongWritable>  
{
    public static GeneratedDataInputFormat[] EMPTY_ARRAY = {};
    public static Class THIS_CLASS = GeneratedDataInputFormat.class;

    /**
     * Logically split the set of input files for the job.
     * <p/>
     * <p>Each {@link org.apache.hadoop.mapreduce.InputSplit} is then assigned to an individual {@link org.apache.hadoop.mapreduce.Mapper}
     * for processing.</p>
     * <p/>
     * <p><i>Note</i>: The split is a <i>logical</i> split of the inputs and the
     * input files are not physically split into chunks. For e.g. a split could
     * be <i>&lt;input-file-path, start, offset&gt;</i> tuple. The InputFormat
     * also creates the {@link org.apache.hadoop.mapreduce.RecordReader} to read the {@link org.apache.hadoop.mapreduce.InputSplit}.
     *
     * @param context job configuration.
     * @return an array of {@link org.apache.hadoop.mapreduce.InputSplit}s for the job.
     */
    @Override
    public List<org.apache.hadoop.mapreduce.InputSplit> getSplits(JobContext context)
            throws IOException, InterruptedException
    {
         int numSplits = 5;

        long startingNumber = 2; //from
         long endingNumber = 265; // to, exclusive

         long numbersInSplit = (long)Math.floor((endingNumber - startingNumber)/numSplits);
         long startingNumberInSplit = startingNumber;
         long endingNumberInSplit = startingNumberInSplit + numbersInSplit;
         long remainderInLastSplit = (endingNumber - startingNumber) - numSplits*numbersInSplit;

         ArrayList<InputSplit> splits = new ArrayList<InputSplit>(numSplits);

         for(int i = 0; i < numSplits - 1; i++)
         {
             splits.add(new GeneratedInputSplit(startingNumberInSplit, endingNumberInSplit));
             startingNumberInSplit = endingNumberInSplit;
             endingNumberInSplit = startingNumberInSplit + numbersInSplit;
         }

         //add last split, with remainder if any
         splits.add(new GeneratedInputSplit(startingNumberInSplit, endingNumberInSplit + remainderInLastSplit));

         return splits;
     }



    /**
     * Create a record reader for a given split. The framework will call
     * {@link org.apache.hadoop.mapreduce.RecordReader#initialize(org.apache.hadoop.mapreduce.InputSplit, org.apache.hadoop.mapreduce.TaskAttemptContext)} before
     * the split is used.
     *
     * @param split   the split to be read
     * @param context the information about the task
     * @return a new record reader
     * @throws java.io.IOException
     * @throws InterruptedException
     */
    @Override
    public RecordReader<LongWritable, LongWritable> createRecordReader(
            org.apache.hadoop.mapreduce.InputSplit split,
            TaskAttemptContext context)
            throws IOException, InterruptedException
    {
        return new GeneratedDataRecordReader((GeneratedInputSplit)split);
    }

 @SuppressWarnings("UnusedDeclaration")
    public RecordReader<LongWritable, LongWritable> getRecordReader(InputSplit split, JobConf job, Reporter reporter)
    {
        return new GeneratedDataRecordReader((GeneratedInputSplit)split);
    }



}
