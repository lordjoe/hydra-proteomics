package org.systemsbiology.hadoop;

import com.lordjoe.utilities.*;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;

import java.io.*;
import java.util.*;

/**
 * org.systemsbiology.hadoop.AbstractParameterizedMapper
 *
 * @author Steve Lewis
 * @date 8/19/13
 */
public class AbstractParameterizedMapper<T> extends Mapper<T, Text, Text, Text> {
    public static final boolean WRITING_PARAMETERS = true;

    private ISetableParameterHolder m_Application;
    private final Text m_OnlyKey = new Text();
    private final Text m_OnlyValue = new Text();
    private long m_MinimumFreeMemory = Long.MAX_VALUE;
    private final ElapsedTimer m_Elapsed = new ElapsedTimer();
    private Mapper.Context m_Context;

    @SuppressWarnings("UnusedDeclaration")
    public final Mapper.Context getContext() {
        return m_Context;
    }

    @Override
    protected void setup(final Mapper<T, Text, Text, Text>.Context context) throws IOException, InterruptedException {
        //noinspection unchecked
        super.setup(context);

        m_Context = context;
        // read configuration lines
        Configuration conf = context.getConfiguration();


        // This allows non-hadoop code to report progress
        ProgressManager.INSTANCE.addProgressHandler(new HadoopProgressManager(context));

        // debugging code to show my keys
        //noinspection SimplifiableIfStatement,ConstantIfStatement
        if (false) {
            Iterator<Map.Entry<String, String>> iterator = conf.iterator();
            //noinspection WhileLoopReplaceableByForEach
            while (iterator.hasNext()) {
                Map.Entry<String, String> next = iterator.next();
                String key = next.getKey();
                //   if(key.startsWith("org.systemsbiology")) {
                System.err.println(key + "=" + next.getValue());
                //`   }
            }
            System.err.println("Done showing my keys");

        }


//        String defaultPath = conf.get(XTandemHadoopUtilities.PATH_KEY);
//        XTandemHadoopUtilities.setDefaultPath(defaultPath);
//        String forcePathPrefix = conf.get(XTandemHadoopUtilities.FORCE_PATH_PREFIX_KEY);
//        XTandemMain.setRequiredPathPrefix(forcePathPrefix);


        IAnalysisParameters ap = AnalysisParameters.getInstance();
        ap.setJobName(context.getJobName());


        // m_Factory.setValidationStringency(SAMFileReader.ValidationStringency.LENIENT)

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
        JobSizeEnum jobSize = m_Application.getEnumParameter(HadoopUtilities.JOB_SIZE_PROPERTY, JobSizeEnum.class, JobSizeEnum.Medium);
        HadoopUtilities.setHadoopProperty(HadoopUtilities.JOB_SIZE_PROPERTY, jobSize.toString());

    }

    @SuppressWarnings("UnusedDeclaration")
    protected long setMinimalFree() {
        long freemem = Runtime.getRuntime().freeMemory();
        setMinimumFreeMemory(Math.min(freemem, getMinimumFreeMemory()));
        return freemem;
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

    public final ISetableParameterHolder getApplication() {
        return m_Application;
    }

    private final Text getOnlyKey() {
        return m_OnlyKey;
    }

    private final Text getOnlyValue() {
        return m_OnlyValue;
    }

    /**
     * Called once at the end of the task.
     */
    @Override
    protected void cleanup(final Mapper.Context context) throws IOException, InterruptedException {
        //noinspection unchecked
        super.cleanup(context);
    }

    protected void writeKeyValue(String key, String value, Context context) {
        Text onlyKey = getOnlyKey();
        onlyKey.set(key);
        Text onlyValue = getOnlyValue();
        onlyValue.set(value);
        try {
            context.write(onlyKey, onlyValue);
        } catch (IOException e) {
            throw new UnsupportedOperationException(e);
        } catch (InterruptedException e) {
            throw new UnsupportedOperationException(e);
        }
    }

}
