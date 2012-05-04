package org.systemsbiology.hadoop;

import org.apache.hadoop.mapreduce.*;

import java.io.*;

/**
 * org.systemsbiology.hadoop.AbstractExecutableReducer
 * Root class for reducers that call an executable
 * User: steven
 * Date: Jul 30, 2010
 */
public abstract class AbstractExecutableReducer<KEYIN,VALUEIN,KEYOUT,VALUEOUT> extends Reducer<KEYIN,VALUEIN,KEYOUT,VALUEOUT> {
    public static final AbstractExecutableReducer[] EMPTY_ARRAY = {};

    /**
     * make sure that the executable exists
     */
    protected abstract void guaranteeExecutable( final Context context);


    /**
     * invoke the exe in some manner
     * @param key
     * @param context
     * @param otherArgs
     * @return
     */
    protected abstract String callExecutable(final KEYIN key,final VALUEIN value,final Context context, Object... otherArgs);

    /**
     * Called once at the start of the task.
     */
    @Override
    protected void setup(final Context context) throws IOException, InterruptedException {
        super.setup(context);    //To change body of overridden methods use File | Settings | File Templates.
        guaranteeExecutable(context);
    }

    /**
     *
     */
    @Override
    protected void reduce(final KEYIN key, final Iterable<VALUEIN> values, final Context context) throws IOException, InterruptedException {
       for(VALUEIN val : values) {
             callExecutable(key,val,context);
       }
    }
}
