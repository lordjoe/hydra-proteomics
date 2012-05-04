// (c) Copyright 2009 Cloudera, Inc.

package org.systemsbiology.couldera.training.index;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.input.*;

/**
 * LineIndexMapper
 * <p/>
 * Maps each observed word in a line to a (filename@offset) string.
 */
public class LineIndexMapper extends Mapper<LongWritable, Text, Text, Text>
{
    public LineIndexMapper()
    {
    }

    private static final Text OUT_KEY = new Text();
    private static final Text WORD_KEY = new Text();

    protected void map(
            LongWritable key,
            Text value,
            Context context) throws IOException, InterruptedException
    {
        FileSplit fileSplit = (FileSplit) context.getInputSplit();
        String fileName = fileSplit.getPath().getName();
        OUT_KEY.set(fileName + "@" + key.get());
        String[] items = value.toString().split(" ");
        for (int i = 0; i < items.length; i++) {
            String s = cleanWord(items[i]);
            if (s.length() > 0) {
                WORD_KEY.set(s);
                context.write(WORD_KEY, OUT_KEY);
            }
        }
        // TODO - Put your code here.
    }


    public static String cleanWord(String s)
    {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (Character.isLetterOrDigit(c))
                sb.append(Character.toUpperCase(c));
        }
        return sb.toString();

    }

    /**
     * Closes this stream and releases any system resources associated
     * with it. If the stream is already closed then invoking this
     * method has no effect.
     *
     * @throws java.io.IOException if an I/O error occurs
     */
    public void close() throws IOException
    {
    }

 
}

