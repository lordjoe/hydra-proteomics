package org.systemsbiology.hadoop.consolidator;

import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;

import java.io.*;
import java.util.*;

/**
 * org.systemsbiology.hadoop.consolidator.AbstractConsolidator
 *  A consolidator takes a Hadoop directory with a number of
 *  part-r-00000 , part-r-00001 and generates usable output
 * @author Steve Lewis
 * @date Oct 23, 2010
 */
public class AbstractConsolidator
{
    public static AbstractConsolidator[] EMPTY_ARRAY = {};
    public static Class THIS_CLASS = AbstractConsolidator.class;

    public static class TokenizerMapper
             extends Mapper<Object, Text, Text, IntWritable>
        {

         private final static IntWritable one = new IntWritable(1);
         private Text word = new Text();

         public void map(Object key, Text value, Context context
         ) throws IOException, InterruptedException {
             StringTokenizer itr = new StringTokenizer(value.toString());
             while (itr.hasMoreTokens()) {
                 String s = itr.nextToken().toUpperCase();
                 s = dropNonLetters(s);
                 if (s.length() > 0) {
                     word.set(s);
                     context.write(word, one);
                 }
             }
         }
     }

     public static String dropNonLetters(String s) {
         StringBuilder sb = new StringBuilder();
         for (int i = 0; i < s.length(); i++) {
             char c = s.charAt(i);
             if (Character.isLetter(c))
                 sb.append(c);
         }

         return sb.toString();
     }

     public static class IntSumReducer
             extends Reducer<Text, IntWritable, Text, IntWritable> {
         private IntWritable result = new IntWritable();

         public void reduce(Text key, Iterable<IntWritable> values,
                            Context context
         ) throws IOException, InterruptedException {
             int sum = 0;
             for (IntWritable val : values) {
                 sum += val.get();
             }
             result.set(sum);
             context.write(new Text(key.toString()), result);
         }
     }

    
}
