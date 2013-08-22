package org.systemsbiology.hadoop;

import com.lordjoe.utilities.*;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.util.*;

import java.io.*;

/**
 * org.systemsbiology.hadoop.AbstractParameterizedReducer
 *
 * @author Steve Lewis
 * @date 8/22/13
 */

public abstract class AbstractParameterizedReducer extends Reducer<Text, Text, Text, Text> {

    public static final boolean WRITING_PARAMETERS = true;

    private ISetableParameterHolder m_Application;
    private final Text m_OnlyKey = new Text();
    private final Text m_OnlyValue = new Text();
    private boolean m_AllSpecialKeysHandled;
    private long m_MinimumFreeMemory = Long.MAX_VALUE;
    private final ElapsedTimer m_Elapsed = new ElapsedTimer();
    private Context m_Context;

    @SuppressWarnings("UnusedDeclaration")
    public boolean isAllSpecialKeysHandled() {
        return m_AllSpecialKeysHandled;
    }

    public void setAllSpecialKeysHandled(final boolean pAllSpecialKeysHandled) {
        m_AllSpecialKeysHandled = pAllSpecialKeysHandled;
    }


    public ElapsedTimer getElapsed() {
        return m_Elapsed;
    }

    public long getMinimumFreeMemory() {
        return m_MinimumFreeMemory;
    }

    public void setMinimumFreeMemory(final long pMinimumFreeMemory) {
        m_MinimumFreeMemory = pMinimumFreeMemory;
    }


    @SuppressWarnings("UnusedDeclaration")
    protected long setMinimalFree() {
        long freemem = Runtime.getRuntime().freeMemory();
        setMinimumFreeMemory(Math.min(freemem, getMinimumFreeMemory()));
        return freemem;
    }

    @SuppressWarnings("UnusedDeclaration")
    public final Context getContext() {
        return m_Context;
    }

    @Override
    protected void setup(final Context context) throws IOException, InterruptedException {
        super.setup(context);
        m_Context = context;
        // read configuration lines
        //noinspection UnusedDeclaration
        Configuration conf = context.getConfiguration();

        IAnalysisParameters ap = AnalysisParameters.getInstance();
        ap.setJobName(context.getJobName());


//        String defaultPath = conf.get(HadoopUtilities.PATH_KEY);
//        HadoopUtilities.setDefaultPath(defaultPath);


        // sneaky trick to extract the version
        String version = VersionInfo.getVersion();
        context.getCounter("Performance", "Version-" + version).increment(1);
        // sneaky trick to extract the user
        String uname = System.getProperty("user.name");
        context.getCounter("Performance", "User-" + uname).increment(1);



//        // sometimes we need to add a prefix to a file
//        String forcePathPrefix = conf.get(XTandemHadoopUtilities.FORCE_PATH_PREFIX_KEY);
//        XTandemMain.setRequiredPathPrefix(forcePathPrefix);
//        // m_Factory.setValidationStringency(SAMFileReader.ValidationStringency.LENIENT)

        m_Application = HadoopUtilities.loadFromContext(context);

        if (WRITING_PARAMETERS) {
            ISetableParameterHolder application = getApplication();


            String[] keys = application.getParameterKeys();
            for (String key : keys) {
                if (key.startsWith("org.")) {
                    System.err.println(key + " = " + application.getParameter(key));
                }
            }
        }



    }


    public final ISetableParameterHolder getApplication() {
        return m_Application;
    }

    public final Text getOnlyKey() {
        return m_OnlyKey;
    }

    public final Text getOnlyValue() {
        return m_OnlyValue;
    }


    @SuppressWarnings("UnusedDeclaration")
    protected boolean isKeySpecial(String s) {
        return s.startsWith("#");
    }

    @Override
    public void reduce(Text key, Iterable<Text> values,
                       Context context) throws IOException, InterruptedException {
        // keys starting with # come BEFORE ALL other keys
        String sequence = key.toString();
        // these are special and will ALL behandled FIRST
        if (sequence.startsWith("#"))
            reduceSpecial(key, values, context);
        else {
            // we will get NO MORE special keys
            setAllSpecialKeysHandled(true);
            reduceNormal(key, values, context);
        }
    }

    /**
     * Called once at the end of the task.
     */
    @Override
    protected void cleanup(final Context context) throws IOException, InterruptedException {
        super.cleanup(context);
    }

    protected abstract void reduceNormal(Text key, Iterable<Text> values,
                                         Context context) throws IOException, InterruptedException;

    @SuppressWarnings("UnusedParameters")
    protected void reduceSpecial(Text key, Iterable<Text> values,
                                 Context context) throws IOException, InterruptedException {
        // Handle special early keys
    }
}
