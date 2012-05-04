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
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.io.SequenceFile.*;
import org.apache.hadoop.io.compress.*;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.output.*;
import org.apache.hadoop.util.*;

import java.io.*;

/** An {@link org.apache.hadoop.mapreduce.OutputFormat} that writes {@link org.apache.hadoop.io.SequenceFile}s. */
public class MapFileOutputFormat<K   extends WritableComparable,V   extends Writable > extends SequenceFileOutputFormat<K, V>
{

  public RecordWriter<K, V>
         getRecordWriter(TaskAttemptContext context
                         ) throws IOException, InterruptedException {
    Configuration conf = context.getConfiguration();

    CompressionCodec codec = null;
    CompressionType compressionType = CompressionType.NONE;
    if (getCompressOutput(context)) {
      // find the kind of compression to do
      compressionType = getOutputCompressionType(context);

      // find the right codec
      Class<?> codecClass = getOutputCompressorClass(context,
                                                     DefaultCodec.class);
      codec = (CompressionCodec)
        ReflectionUtils.newInstance(codecClass, conf);
    }
    // get the path of the temporary output file
    Path file = getDefaultWorkFile(context, "");
    FileSystem fs = file.getFileSystem(conf);
      //Configuration conf, FileSystem fs, String dirName,    Class<? extends WritableComparable> keyClass, Class valClass)
      String name = file.toString();
      final MapFile.Writer out =
      new MapFile.Writer(  conf,fs, name,
              (Class<? extends WritableComparable>) context.getOutputKeyClass(),
                                context.getOutputValueClass() );

    return new RecordWriter<K, V>() {

        public void write(K key, V value)
          throws IOException {

          out.append(key, value);
        }

        public void close(TaskAttemptContext context) throws IOException {
          out.close();
        }
      };
  }



}

