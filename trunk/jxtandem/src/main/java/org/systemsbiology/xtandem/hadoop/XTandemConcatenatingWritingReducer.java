package org.systemsbiology.xtandem.hadoop;

import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.*;
import org.systemsbiology.sam.*;
import org.systemsbiology.xtandem.*;
import org.systemsbiology.xtandem.pepxml.*;
import org.systemsbiology.xtandem.reporting.*;
import org.systemsbiology.xtandem.scoring.*;

import java.io.*;
import java.util.*;

/**
 * org.systemsbiology.xtandem.hadoop.XTandemXMLWritingReducer
 * This is designed to be the only reducer and will write the XML file written by
 * XTanden - the input is scored Scans will read text from iput and append headers
 * User: steven
 * Date: 3/7/11
 */
public class XTandemConcatenatingWritingReducer extends AbstractTandemReducer {
    public static final XTandemConcatenatingWritingReducer[] EMPTY_ARRAY = {};

    private boolean m_WriteScans;
    private boolean m_WritePepXML;

    private PrintWriter m_Writer; // todo add code to turn this on
    private PrintWriter m_ScansWriter;
    private BiomlReporter m_Reporter;
    private PepXMLWriter[] m_pepXMLWriter;
    private PrintWriter[] m_PepXmlsOutWriter;


    public XTandemConcatenatingWritingReducer() {
    }


    public PrintWriter getWriter() {
        return m_Writer;
    }

    public BiomlReporter getReporter() {
        return m_Reporter;
    }

    public boolean isWritePepXML() {
        return m_WritePepXML;
    }

    public void setWritePepXML(final boolean pWritePepXML) {
        m_WritePepXML = pWritePepXML;
    }

    public PepXMLWriter getPepXMLWriter(int index) {
         return m_pepXMLWriter[index];
     }

    public PrintWriter getPepXmlsOutWriter(int index) {
        return m_PepXmlsOutWriter[index];
    }

    public PrintWriter getScansWriter() {
        return m_ScansWriter;
    }

    public void setScansWriter(final PrintWriter pScansWriter) {
        m_ScansWriter = pScansWriter;
    }

    public boolean isWriteScans() {
        return m_WriteScans;
    }

    public void setWriteScans(final boolean pWriteScans) {
        m_WriteScans = pWriteScans;
    }

    @Override
    protected void setup(final Context context) throws IOException, InterruptedException {
        super.setup(context);

        getElapsed().reset(); // start the clock
        ScoredScan.setReportExpectedValue(true);

        // read configuration lines
        Configuration conf = context.getConfiguration();


        IAnalysisParameters ap = AnalysisParameters.getInstance();
        ap.setJobName(context.getJobName());


        HadoopTandemMain application = getApplication();
        if ("yes".equals(application.getParameter(JXTandemLauncher.TURN_ON_SCAN_OUTPUT_PROPERTY)))
            setWriteScans(true);

        String fileName = conf.get(BiomlReporter.FORCED_OUTPUT_NAME_PARAMETER);
        if (fileName != null) {
            if (fileName.contains(":") || fileName.charAt(0) != '/') {
                String path = conf.get(XTandemHadoopUtilities.PATH_KEY);

                fileName = path + "/" + fileName;
            }
            application.setParameter(BiomlReporter.FORCED_OUTPUT_NAME_PARAMETER, fileName);
        }
        else {
            String paramsFile = XTandemHadoopUtilities.buildOutputFileName(context, application);
            System.err.println("Writing output to file " + paramsFile);
        }

        m_Writer = XTandemHadoopUtilities.buildWriter(context, application);
        if (isWriteScans()) {
            m_ScansWriter = XTandemHadoopUtilities.buildWriter(context, application, ".scans");
            m_ScansWriter.println("<scans>");
        }
        m_Reporter = new BiomlReporter(application, null);

        m_Reporter.writeHeader(getWriter(), 0);

        if ("yes".equals(application.getParameter(XTandemUtilities.WRITING_PEPXML_PROPERTY))) {
            m_WritePepXML = true;
              ITandemScoringAlgorithm[] algorithms = application.getAlgorithms();
            m_pepXMLWriter = new PepXMLWriter[algorithms.length];
            m_PepXmlsOutWriter = new PrintWriter[algorithms.length];
              for (int i = 0; i <  algorithms.length  ; i++) {
                ITandemScoringAlgorithm algorithm = algorithms[i];
                m_PepXmlsOutWriter[i] = XTandemHadoopUtilities.buildWriter(context, application, "." + algorithm.getName() + ".pep.xml");
                 m_pepXMLWriter[i] = new PepXMLWriter(application);
                String spectrumPath = application.getParameter("spectrum, path");
                getPepXMLWriter(i).writePepXMLHeader(spectrumPath, getPepXmlsOutWriter(i));

            }

        }

    }


    protected void reduceNormal(final Text key, final Iterable<Text> values, final Context context)
            throws IOException, InterruptedException {
        String keyStr = key.toString().trim();
        String scanXML = null;
        String id = "";
        try {
// Debug stuff
            id = keyStr; // key includes charge

            final HadoopTandemMain app = getApplication();
            //        final Scorer scorer = app.getScoreRunner();
            Iterator<Text> textIterator = values.iterator();
            Text first = textIterator.next();
            String rawText = first.toString();

            String[] split = rawText.split("\f");
            String biomlText = split[0];
            scanXML = split[1];
            // write raw it has more scoring
            if (m_ScansWriter != null)
                m_ScansWriter.println(scanXML);


            if (getWriter() != null)
                getWriter().println(biomlText);

            if (isWritePepXML()) {
                MultiScorer multiScorer = XTandemHadoopUtilities.readMultiScoredScan(scanXML, getApplication());
                IScoredScan[] scoredScans = multiScorer.getScoredScans();
                // todo support multiple
                for (int i = 0; i <  scoredScans.length ; i++) {
                    IScoredScan scan = scoredScans[i];
                    getPepXMLWriter(i).writePepXML(scan, getPepXmlsOutWriter(i));

                }
                ScoredScan scan = (ScoredScan) scoredScans[0];
            }
            app.clearRetainedData();

        }
        catch (NumberFormatException e) {
            Text onlyKey = getOnlyKey();
            Text onlyValue = getOnlyValue();
            onlyKey.set(keyStr);
            if (scanXML != null) {
                String message = e.getClass().getName() + " " + e.getMessage() + " " + scanXML;
                onlyValue.set(message);
                context.write(onlyKey, onlyValue);
            }
            else {
                String message = e.getClass().getName() + " " + e.getMessage();
                onlyValue.set(message);
                context.write(onlyKey, onlyValue);

            }
            return; // todo why would this happen
        }
        catch (RuntimeException e) {
            Text onlyKey = getOnlyKey();
            Text onlyValue = getOnlyValue();
            if (scanXML != null) {
                String message = e.getClass().getName() + " " + e.getMessage() + " " + scanXML;
                onlyValue.set(message);
                context.write(onlyKey, onlyValue);
                throw e;
            }
            else {
                String message = e.getClass().getName() + " " + e.getMessage();
                onlyValue.set(message);
                context.write(onlyKey, onlyValue);
                throw e;
            }
        }
        catch (Exception e) {
            Text onlyKey = getOnlyKey();
            Text onlyValue = getOnlyValue();
            if (scanXML != null) {
                String message = e.getClass().getName() + " " + e.getMessage() + " " + scanXML;
                onlyValue.set(message);
                context.write(onlyKey, onlyValue);
                throw new RuntimeException(message);
            }
            else {
                String message = e.getClass().getName() + " " + e.getMessage();
                onlyValue.set(message);
                context.write(onlyKey, onlyValue);
                throw new RuntimeException(message);
            }
        }


    }


    @Override
    protected void cleanup(final Context context) throws IOException, InterruptedException {
        if (m_Writer != null) {
            m_Reporter.writeReportEnd(m_Writer);
            m_Writer.close();
            m_Writer = null;

        }

        if (m_ScansWriter != null) {
            m_ScansWriter.println("</scans>");
            m_ScansWriter.close();
            m_ScansWriter = null;
        }

        if (m_PepXmlsOutWriter != null) {
            for (int i = 0; i < m_PepXmlsOutWriter.length; i++) {
                PrintWriter pepXmlsOutWriter = getPepXmlsOutWriter(i);
                getPepXMLWriter(i).writePepXMLFooter(pepXmlsOutWriter);
                pepXmlsOutWriter.close();

            }
            m_PepXmlsOutWriter = null;
        }

        System.err.println(getElapsed().formatElapsed("Finished with Consolidation"));

        super.cleanup(context);
    }
}
