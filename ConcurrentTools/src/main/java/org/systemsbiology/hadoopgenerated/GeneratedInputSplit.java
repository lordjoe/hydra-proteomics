package org.systemsbiology.hadoopgenerated;

import java.io.IOException;
import java.io.DataInput;
import java.io.DataOutput;

import org.apache.hadoop.mapreduce.InputSplit;

/**
 * org.systemsbiology.hadoopgenerated.GeneratedInputSplit
 * see http://codedemigod.com/blog/?p=120
 *
 * @author Steve Lewis
 * @date Oct 10, 2010
 */
// NOTE implementing    org.apache.hadoop.mapred.InputSplit is CRITICAL for
// serialization - FileInputSplit does this
public class GeneratedInputSplit extends InputSplit implements org.apache.hadoop.mapred.InputSplit {
    public static GeneratedInputSplit[] EMPTY_ARRAY = {};
    public static Class THIS_CLASS = GeneratedInputSplit.class;


    private long m_StartNum;
    private long m_EndNum;

    GeneratedInputSplit() {
    }

    public GeneratedInputSplit(long p_Start, long p_End) {
        this.m_StartNum = p_Start;
        this.m_EndNum = p_End;
    }

    public long getLength() {
        return (this.m_EndNum - this.m_StartNum) * 8;
    }

    public String[] getLocations() throws IOException {
        return new String[]{};
    }

    public void readFields(DataInput in) throws IOException {
        this.m_StartNum = in.readLong();
        this.m_EndNum = in.readLong();
    }

    public void write(DataOutput out) throws IOException {
        out.writeLong(this.m_StartNum);
        out.writeLong(this.m_EndNum);
    }

    public long getStartNum() {
        return this.m_StartNum;
    }

    public long getEndNum() {

        return this.m_EndNum;
    }

}
