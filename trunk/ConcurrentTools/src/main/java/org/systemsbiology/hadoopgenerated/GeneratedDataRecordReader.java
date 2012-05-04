package org.systemsbiology.hadoopgenerated;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapreduce.*;

import java.io.*;

/**
 * org.systemsbiology.hadoopgenerated.GeneratedDataRecordReader
 *
 * @author Steve Lewis
 * @date Oct 10, 2010
 */
public class GeneratedDataRecordReader extends RecordReader<LongWritable, LongWritable>
{
    public static GeneratedDataRecordReader[] EMPTY_ARRAY = {};
    public static Class THIS_CLASS = GeneratedDataRecordReader.class;

    private long m_End;
    private long m_Index;
    private long m_Start;

    public GeneratedDataRecordReader(GeneratedInputSplit split)
    {
        this.m_End = split.getEndNum();
        this.m_Index = split.getStartNum(); //index at starting number of split
        this.m_Start = split.getStartNum();
    }


    public void close(){}

    public float getProgress()
    {
        if(this.m_Index  == this.m_End)
        {
            return 0.0f;
        }
        else
        {
            return Math.min(1.0f, (this.m_Index - this.m_Start) / (float)(this.m_End - this.m_Start));
        }
    }

    public long getPos()
    {
        return this.m_End - this.m_Index;
    }

    public boolean next(LongWritable key, LongWritable value)
    {
        if(this.m_Index < this.m_End)
        {
            key.set(this.m_Index);
            value.set(this.m_Index);
            this.m_Index++;

            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * Called once at initialization.
     *
     * @param split   the split that defines the range of records to read
     * @param context the information about the task
     * @throws java.io.IOException
     * @throws InterruptedException
     */
    @Override
    public void initialize(InputSplit split, TaskAttemptContext context)
            throws IOException, InterruptedException
    {
    //    if (true) throw new UnsupportedOperationException("Fix This");

    }

    /**
     * Read the next key, value pair.
     *
     * @return true if a key/value pair was read
     * @throws java.io.IOException
     * @throws InterruptedException
     */
    @Override
    public boolean nextKeyValue() throws IOException, InterruptedException
    {
        if(this.m_Index < this.m_End)
        {
             this.m_Index++;

            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * Get the current key
     *
     * @return the current key or null if there is no current key
     * @throws java.io.IOException
     * @throws InterruptedException
     */
    @Override
    public LongWritable getCurrentKey() throws IOException, InterruptedException
    {
        return new LongWritable(m_Index - 1);
    }

    /**
     * Get the current value.
     *
     * @return the object that was read
     * @throws java.io.IOException
     * @throws InterruptedException
     */
    @Override
    public LongWritable getCurrentValue() throws IOException, InterruptedException
    {
        return new LongWritable(m_Index - 1);
     }
}
