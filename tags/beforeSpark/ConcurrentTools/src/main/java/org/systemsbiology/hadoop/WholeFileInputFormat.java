/**
 * Licensed to the Apache Software Foundation (ASF) under one
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

package org.systemsbiology.hadoop;


import org.apache.hadoop.conf.*;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.io.*;
import org.apache.hadoop.io.compress.*;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.input.*;
import org.apache.hadoop.util.*;

import java.io.*;


/**
 * Splitter that reads a whole file as a single record
 * This is useful when you have a large number of files
 * each of which is a complete unit - for example XML Documents
 */
public class WholeFileInputFormat extends FileInputFormat<Text, Text> {

    @Override
    public RecordReader<Text, Text> createRecordReader(InputSplit split,
                       TaskAttemptContext context) {
        return new MyWholeFileReader();
    }

    @Override
    protected boolean isSplitable(JobContext context, Path file) {
        return false;
    }

    /**
     * Custom RecordReader which returns the entire file as a
     * single value with the name as a key
     * Value is the entire file
     * Key is the file name
     */
    public static class MyWholeFileReader extends RecordReader<Text, Text> {

        private CompressionCodecFactory compressionCodecs = null;
        private long start;
        private long end;
        private LineReader in;
        private Text key = null;
        private Text value = null;
        private Text buffer = new Text();

        public void initialize(InputSplit genericSplit,
                               TaskAttemptContext context) throws IOException {
            FileSplit split = (FileSplit) genericSplit;
            Configuration job = context.getConfiguration();
            start = split.getStart();
            end = start + split.getLength();
            final Path file = split.getPath();
            compressionCodecs = new CompressionCodecFactory(job);
            final CompressionCodec codec = compressionCodecs.getCodec(file);

            // open the file and seek to the start of the split
            FileSystem fs = file.getFileSystem(job);
            FSDataInputStream fileIn = fs.open(split.getPath());
            if (codec != null) {
                in = new LineReader(codec.createInputStream(fileIn), job);
                end = Long.MAX_VALUE;
            }
            else {
                in = new LineReader(fileIn, job);
            }
            if (key == null) {
                key = new Text();
            }
            key.set(split.getPath().getName());
            if (value == null) {
                value = new Text();
            }

        }

        public boolean nextKeyValue() throws IOException {
            int newSize = 0;
            StringBuilder sb = new StringBuilder();
            newSize = in.readLine(buffer);
            while (newSize > 0) {
                String str = buffer.toString();
                sb.append(str);
                sb.append("\n");
                newSize = in.readLine(buffer);
            }

            String s = sb.toString();
            value.set(s);

            if (sb.length() == 0) {
                key = null;
                value = null;
                return false;
            }
            else {
                return true;
            }
        }

        @Override
        public Text getCurrentKey() {
            return key;
        }

        @Override
        public Text getCurrentValue() {
            return value;
        }

        /**
         * Get the progress within the split
         */
        public float getProgress() {
            return 0.0f;
        }

        public synchronized void close() throws IOException {
            if (in != null) {
                in.close();
            }
        }
    }

}