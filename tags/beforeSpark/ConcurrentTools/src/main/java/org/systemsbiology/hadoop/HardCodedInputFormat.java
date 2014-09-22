package org.systemsbiology.hadoop;

import org.apache.hadoop.fs.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.mapreduce.*;

import java.io.*;
import java.util.*;

/**
 * org.systemsbiology.hadoop.HardCodedInputFormat
 *
 * @author Steve Lewis
 * @date 31/05/13
 */
public class HardCodedInputFormat extends InputFormat<LongWritable, Text> {
    private static String[] g_TextToSend = {};

    public static String[] getTextToSend() {
        return g_TextToSend;
    }

    public static void setTextToSend(String text) {
        g_TextToSend = text.split("\n");
    }

    @Override
    public List<org.apache.hadoop.mapreduce.InputSplit> getSplits(JobContext context)
            throws IOException, InterruptedException {
        int numSplits = 1;
        if(g_TextToSend.length == 0)
            throw new IllegalStateException("Text has not been initialized"); // ToDo change
        ArrayList<InputSplit> splits = new ArrayList<InputSplit>(numSplits);
        splits.add(new HardCodedInputSplit());

        return splits;
    }


    @SuppressWarnings(value = "deprecated")
    public static class HardCodedInputSplit extends InputSplit implements
            org.apache.hadoop.mapred.InputSplit {

        public HardCodedInputSplit() {
        }


        public long getLength() {
            return 1;
        }

        public String[] getLocations() throws IOException {
            return new String[]{};
        }

        public void readFields(DataInput in) throws IOException {
        }

        public void write(DataOutput out) throws IOException {
        }


    }


    @Override
    public RecordReader<LongWritable, Text> createRecordReader(InputSplit inputSplit, TaskAttemptContext taskAttemptContext) throws IOException, InterruptedException {
        return new HardCodedTextReader();
    }

    /**
     * Custom RecordReader which returns the entire file as a
     * single m_Value with the name as a m_Key
     * Value is the entire file
     * Key is the file name
     */
    public class HardCodedTextReader extends RecordReader<LongWritable, Text> {

        private int m_Current;
        private LongWritable m_Key;
        private Text m_Value = null;
        private String m_CurrentLine;


        public void initialize(InputSplit genericSplit,
                               TaskAttemptContext context) throws IOException {

            Configuration job = context.getConfiguration();
            final Path file = new Path("/");

            // open the file and seek to the m_Start of the split
            FileSystem fs = file.getFileSystem(job);
            if(!(fs instanceof LocalFileSystem))
                throw new IllegalStateException("Hard Coded reader will NOT WORK on the cluster");

            m_Current = 0;
            if (m_Key == null) {
                m_Key = new LongWritable();
            }
            if (m_Value == null) {
                m_Value = new Text();
            }
            m_CurrentLine = getTextToSend()[m_Current++];
        }

        /**
         * look for a <scan tag then read until it closes
         *
         * @return true if there is data
         * @throws java.io.IOException
         */
        public boolean nextKeyValue() throws IOException {

            if (m_Current >= getTextToSend().length) {  // we are the the end of the split
                m_Key = null;
                m_Value = null;
                return false;
            }

            //            // advance to the start - probably done in initialize
            //            while (m_FileIn.getPos() < m_Start) {
            //                m_CurrentLine = m_Input.readLine();
            //            }
            // read more data

            m_Value.set(m_CurrentLine);


            // label = key

            m_Key.set(m_Current);
            m_CurrentLine = getTextToSend()[m_Current++];

            return true;
        }


        @Override
        public LongWritable getCurrentKey() {
            return m_Key;
        }

        @Override
        public Text getCurrentValue() {
            return m_Value;
        }

        @Override
        public float getProgress() throws IOException, InterruptedException {
            return (float) m_Current / getTextToSend().length;
        }

        @Override
        public void close() throws IOException {


        }
    }
}
