package org.systemsbiology.xtandem.hadoop;

import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.*;
import org.systemsbiology.hadoop.*;
import org.systemsbiology.xtandem.reporting.*;
import org.systemsbiology.xtandem.scoring.*;

import java.io.*;

/**
 * org.systemsbiology.xtandem.hadoop.BoimlWritingMapper
 * Mapper used in the consolidation task which generates output xml fragments to unburden the reducer
 * User: steven
 * Date: 3/7/11
 */
public class BoimlWritingMapper extends AbstractTandemMapper<Text> {
    public static final BoimlWritingMapper[] EMPTY_ARRAY = {};


    private BiomlReporter m_Reporter;

    public BoimlWritingMapper() {
    }

    @Override
    protected void setup(final Context context) throws IOException, InterruptedException {
        super.setup(context);
        // read configuration lines
        Configuration conf = context.getConfiguration();

        IAnalysisParameters ap = AnalysisParameters.getInstance();
        ap.setJobName(context.getJobName());

        // m_Factory.setValidationStringency(SAMFileReader.ValidationStringency.LENIENT)

        //      getApplication().loadTaxonomy();
        m_Reporter = new BiomlReporter(getApplication(), null);

    }

    public BiomlReporter getReporter() {
        return m_Reporter;
    }

     /**
     * map each scanned score to the key
     *
     * @param key
     * @param value
     * @param context
     * @throws java.io.IOException
     * @throws InterruptedException
     */
    @Override
    protected void map(final Text key, final Text value, final Context context) throws IOException, InterruptedException {
        String text = value.toString();
        //   text = XTandemHadoopUtilities.cleanXML(text);
 //       ScoredScan scan = XTandemHadoopUtilities.readScoredScan(text, getApplication());
        MultiScorer multiScorer = XTandemHadoopUtilities.readMultiScoredScan(text, getApplication());
        ScoredScan scan = (ScoredScan)multiScorer.getScoredScans()[0];

        String id = scan.getKey(); // key holds charge


        final Text onlyKey = getOnlyKey();
        onlyKey.set(id);

        BiomlReporter reporter = getReporter();
        StringWriter sw = new StringWriter();
        PrintWriter out = new PrintWriter(sw);
        reporter.writeScanScores(scan, out, 1);



        Text onlyValue = getOnlyValue();
        onlyValue.set(sw.toString() + "\f" + text);    /// form feed separated
        context.write(onlyKey, onlyValue);

        context.getCounter("Performance", "TotalScoringScans").increment(1);
        getApplication().clearRetainedData();

    }


    @Override
    protected void cleanup(final Context context) throws IOException, InterruptedException {
        super.cleanup(context);    //To change body of overridden methods use File | Settings | File Templates.
    }
}
