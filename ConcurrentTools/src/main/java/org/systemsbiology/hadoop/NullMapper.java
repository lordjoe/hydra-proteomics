package org.systemsbiology.hadoop;


 import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;

import java.io.*;

/**
 * org.systemsbiology.hadoop.NullMapper
 * well it seems Mapper does this but NullMapper makes it clearer
 * User: Steve
 * Date: 9/13/11
 */
public class NullMapper<T  extends Writable> extends Mapper<T , Text, Text, Text> {
    public static final NullMapper[] EMPTY_ARRAY = {};

    private final Text m_OnlyKey = new Text();
    /**
     * Called once for each key/value pair in the input split. Most applications
     * should override this, but the default is the identity function.
     */
    @Override
    protected void map(final T key, final Text value, final Context context) throws IOException, InterruptedException {
        if(key instanceof Text) {
            context.write((Text)key, value);
        }
        else {
            m_OnlyKey.set(key.toString());
            context.write(m_OnlyKey, value);
        }
      }
}
