package org.systemsbiology.xtandem.hadoop;
/**
 * Licensed to the Apace Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import org.apache.hadoop.conf.*;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.input.*;
import org.apache.hadoop.util.*;

import java.io.*;
import java.util.*;

//import org.apache.hadoop.classification.InterfaceAudience;
//import org.apache.hadoop.classification.InterfaceStability;
//import org.apache.hadoop.mapred.FileInputFormat;
//import org.apache.hadoop.mapred.FileSplit;
//import org.apache.hadoop.mapred.JobConf;
//import org.apache.hadoop.mapred.LineRecordReader;


/**
 * NLineInputFormat which splits N lines of input as one split.
 * <p/>
 * In many "pleasantly" parallel applications, each process/mapper
 * processes the same input file (s), but with computations are
 * controlled by different parameters.(Referred to as "parameter sweeps").
 * One way to achieve this, is to specify a set of parameters
 * (one set per line) as input in a control file
 * (which is the input path to the map-reduce application,
 * where as the input dataset is specified
 * via a config variable in JobConf.).
 * <p/>
 * The NLineInputFormat can be used in such applications, that splits
 * the input file such that by default, one line is fed as
 * a value to one map task, and key is the offset.
 * i.e. (k,v) is (LongWritable, Text).
 * The location hints will span the whole mapred cluster.
 */
//@InterfaceAudience.Public
//@InterfaceStability.Stable
//Stable
public class NLineInputFormat extends FileInputFormat<LongWritable, Text> {
    public static final String LINES_PER_MAP =
            "mapreduce.input.lineinputformat.linespermap";

    public LineRecordReader createRecordReader(
            InputSplit genericSplit, TaskAttemptContext context)
            throws IOException {
        context.setStatus(genericSplit.toString());
        //    return new LineRecordReader(null, null);
        return new MyLineRecordReader();
    }

    /**
     * this class overrides the original LineNumber reader allowing you to add your own code
     */
    public class MyLineRecordReader extends LineRecordReader {

        public MyLineRecordReader() {
            super();
        }

        @Override
        public void initialize(final InputSplit genericSplit, final TaskAttemptContext context) throws IOException {
            super.initialize(genericSplit, context);    //To change body of overridden methods use File | Settings | File Templates.
        }

        @Override
        public boolean nextKeyValue() throws IOException {
            return super.nextKeyValue();    //To change body of overridden methods use File | Settings | File Templates.
        }

        @Override
        public LongWritable getCurrentKey() {
            return super.getCurrentKey();    //To change body of overridden methods use File | Settings | File Templates.
        }

        @Override
        public Text getCurrentValue() {
            return super.getCurrentValue();    //To change body of overridden methods use File | Settings | File Templates.
        }

        /**
         * Get the progress within the split
         */
        @Override
        public float getProgress() {
            return super.getProgress();    //To change body of overridden methods use File | Settings | File Templates.
        }

        @Override
        public void close() throws IOException {
            super.close();    //To change body of overridden methods use File | Settings | File Templates.
        }
    }

    /**
     * Logically splits the set of input files for the job, splits N lines
     * of the input as one split.
     *
     * @see FileInputFormat#getSplits(JobContext)
     */
    public List<InputSplit> getSplits(JobContext job)
            throws IOException {
        List<InputSplit> splits = new ArrayList<InputSplit>();
        int numLinesPerSplit = getNumLinesPerSplit(job);
        for (FileStatus status : listStatus(job)) {
            splits.addAll(getSplitsForFile(status,
                    job.getConfiguration(), numLinesPerSplit));
        }
        return splits;
    }

    public static List<FileSplit> getSplitsForFile(FileStatus status,
                                                   Configuration conf, int numLinesPerSplit) throws IOException {
        List<FileSplit> splits = new ArrayList<FileSplit>();
        Path fileName = status.getPath();
        /*if (status.isDirectory()) {
          throw new IOException("Not a file: " + fileName);
        }*/
        FileSystem fs = fileName.getFileSystem(conf);
        LineReader lr = null;
        try {
            FSDataInputStream in = fs.open(fileName);
            lr = new LineReader(in, conf);
            Text line = new Text();
            int numLines = 0;
            long begin = 0;
            long length = 0;
            int num = -1;
            while ((num = lr.readLine(line)) > 0) {
                numLines++;
                length += num;
                if (numLines == numLinesPerSplit) {
                    // NLineInputFormat uses LineRecordReader, which always reads
                    // (and consumes) at least one character out of its upper split
                    // boundary. So to make sure that each mapper gets N lines, we
                    // move back the upper split limits of each split
                    // by one character here.
                    if (begin == 0) {
                        splits.add(new FileSplit(fileName, begin, length - 1,
                                new String[]{}));
                    }
                    else {
                        splits.add(new FileSplit(fileName, begin - 1, length,
                                new String[]{}));
                    }
                    begin += length;
                    length = 0;
                    numLines = 0;
                }
            }
            if (numLines != 0) {
                splits.add(new FileSplit(fileName, begin, length, new String[]{}));
            }
        }
        finally {
            if (lr != null) {
                lr.close();
            }
        }
        return splits;
    }

    /**
     * Set the number of lines per split
     *
     * @param job      the job to modify
     * @param numLines the number of lines per split
     */
    public static void setNumLinesPerSplit(Job job, int numLines) {
        job.getConfiguration().setInt(LINES_PER_MAP, 10);
    }

    /**
     * Get the number of lines per split
     *
     * @param job the job
     * @return the number of lines per split
     */
    public static int getNumLinesPerSplit(JobContext job) {
        return job.getConfiguration().getInt(LINES_PER_MAP, 10);
    }

//@Override
//public org.apache.hadoop.mapred.RecordReader<LongWritable, Text> getRecordReader(
//		org.apache.hadoop.mapred.InputSplit genericSplit, JobConf conf, Reporter reporter)
//		throws IOException {
//	return null;
//}

}