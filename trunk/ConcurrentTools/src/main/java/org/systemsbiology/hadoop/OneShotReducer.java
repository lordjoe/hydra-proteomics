package org.systemsbiology.hadoop;

import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;

import java.io.*;
import java.util.*;

/**
 * org.systemsbiology.hadoop.OneShotReducer
 * Base class
 * User: steven
 * Date: Aug 16, 2010
 */
public abstract class OneShotReducer<KEYIN extends WritableComparable,VALUEIN extends Writable,KEYOUT extends WritableComparable,VALUEOUT extends Writable>  extends
        Reducer<KEYIN,VALUEIN,KEYOUT,VALUEOUT> {
    public static final OneShotReducer[] EMPTY_ARRAY = {};

    private boolean m_HasBeenCalled;
    @Override
      protected void reduce(final KEYIN key, final Iterable<VALUEIN> values, final Context context) throws IOException, InterruptedException {
          Iterator<VALUEIN> itr = values.iterator();
          while(itr.hasNext())  {
              if(m_HasBeenCalled)
                throw new IllegalStateException("This should only be called once"); // ToDo change
             VALUEIN value = itr.next();
              processOnce(context);
              m_HasBeenCalled = true;
           }
      }

    /**
     *  wrapper for write so we dont need to handle exceptions
     * @param key !null key
     * @param value !null value
     * @param context !null context
     * @throws RuntimeException  on error
     */
    public void safeWrite(KEYOUT key,VALUEOUT value, final Context context) throws RuntimeException {
          try {
              context.write(key,value);
          }
          catch (IOException e) {
              throw new RuntimeException(e);

          }
          catch (InterruptedException e) {
              throw new RuntimeException(e);

          }
      }

    protected abstract void processOnce( Context context);




}
