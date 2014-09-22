package org.systemsbiology.hadoop;

import org.apache.hadoop.mapreduce.*;

import java.io.*;

/**
 * org.systemsbiology.hadoop.AbstractExecutableMapper
 * Root class for mapper that call an executable
 * all the real work is in the Call Executable method
 * User: steven
 * Date: Jul 30, 2010
 */
public abstract class AbstractExecutableMapper<KEYIN,VALUEIN,KEYOUT,VALUEOUT> extends Mapper<KEYIN,VALUEIN,KEYOUT,VALUEOUT> {
    public static final AbstractExecutableMapper[] EMPTY_ARRAY = {};

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
    protected void map(KEYIN key, VALUEIN value, org.apache.hadoop.mapreduce.Mapper<KEYIN,VALUEIN,KEYOUT,VALUEOUT>.Context context) throws java.io.IOException, java.lang.InterruptedException
    {
         callExecutable(key,value,context);
    }
}
