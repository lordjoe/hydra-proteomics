package org.systemsbiology.hadoop;

import org.apache.hadoop.mapreduce.*;

import java.io.*;

/**
 * org.systemsbiology.hadoop.StringRecordWriter
 * User: steven
 * Date: Jun 23, 2010
 */
public class StringRecordWriter<K,V> extends RecordWriter<K,V> {
    public static final StringRecordWriter[] EMPTY_ARRAY = {};

    private final StringBuilder m_Text = new StringBuilder();


    public String getText()
    {
        return m_Text.toString();
    }


    public void cleat()
    {
         m_Text.setLength(0);
    }

     /**
     * Writes a key/value pair.
     *
     * @param key   the key to write.
     * @param value the value to write.
     * @throws java.io.IOException
     */
    @Override
    public void write(final K key, final V value) throws IOException {
        m_Text.append(key.toString()+ "\t" + value.toString());
    }

    /**
     * Close this <code>RecordWriter</code> to future operations.
     *
     * @param context the context of the task
     * @throws java.io.IOException
     */
    @Override
    public void close(final TaskAttemptContext context) throws IOException, InterruptedException {

    }
}
