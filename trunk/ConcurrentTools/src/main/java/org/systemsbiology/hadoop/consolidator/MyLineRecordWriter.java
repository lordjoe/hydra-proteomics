package org.systemsbiology.hadoop.consolidator;

import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.*;

import java.io.*;

/**
 * org.systemsbiology.hadoop.consolidator.MyLineRecordWriter
 *   copy of TextOutputFormat LineRecordWriter explsed as a high level class
 * 
 * @author Steve Lewis
 * @date Nov 9, 2010
 */
public class MyLineRecordWriter<K, V>
  implements RecordWriter<K, V>
  {
  private static final String utf8 = "UTF-8";
  private static final byte[] newline;
  static {
    try {
      newline = "\n".getBytes(utf8);
    } catch (UnsupportedEncodingException uee) {
      throw new IllegalArgumentException("can't find " + utf8 + " encoding");
    }
  }

  protected DataOutputStream out;
  private final byte[] keyValueSeparator;

  public MyLineRecordWriter(DataOutputStream out, String keyValueSeparator) {
    this.out = out;
    try {
      this.keyValueSeparator = keyValueSeparator.getBytes(utf8);
    } catch (UnsupportedEncodingException uee) {
      throw new IllegalArgumentException("can't find " + utf8 + " encoding");
    }
  }

  public MyLineRecordWriter(DataOutputStream out) {
    this(out, "\t");
  }

  /**
   * Write the object to the byte stream, handling Text as a special
   * case.
   * @param o the object to print
   * @throws IOException if the write throws, we pass it on
   */
  private void writeObject(Object o) throws IOException {
    if (o instanceof Text) {
      Text to = (Text) o;
      out.write(to.getBytes(), 0, to.getLength());
    } else {
      out.write(o.toString().getBytes(utf8));
    }
  }

  public synchronized void write(K key, V value)
    throws IOException {

    boolean nullKey = key == null || key instanceof NullWritable;
    boolean nullValue = value == null || value instanceof NullWritable;
    if (nullKey && nullValue) {
      return;
    }
    if (!nullKey) {
      writeObject(key);
    }
    if (!(nullKey || nullValue)) {
      out.write(keyValueSeparator);
    }
    if (!nullValue) {
      writeObject(value);
    }
    out.write(newline);
  }

  public synchronized void close(Reporter reporter) throws IOException {
    out.close();
  }
}
