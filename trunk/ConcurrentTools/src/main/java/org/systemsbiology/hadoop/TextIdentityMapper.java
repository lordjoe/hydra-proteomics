package org.systemsbiology.hadoop;

import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;

import java.io.*;

/**
 * org.systemsbiology.hadoop.TextIdentityMapper
 * Simple Identity mapper class for Text Key and Value
 * User: steven
 * Date: Aug 16, 2010
 */
public class TextIdentityMapper extends Mapper<Text,Text,Text,Text> {
        @Override
        protected void map(final Text key, final Text value, final Context context) throws IOException, InterruptedException {
            context.write(key,value);
        }
    }
