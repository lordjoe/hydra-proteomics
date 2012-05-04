package org.systemsbiology.xtandem.sax;

import com.lordjoe.utilities.*;
import org.systemsbiology.xtandem.*;
import org.systemsbiology.xtandem.bioml.sax.*;
import org.systemsbiology.xtandem.scoring.*;
import org.systemsbiology.xtandem.testing.*;
import org.xml.sax.*;
import sun.reflect.*;

import javax.persistence.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.*;

/**
 * org.systemsbiology.xtandem.sax.XTandemScoringHandler
 * User: steven
 * Date: 6/22/11
 */
public class XTandemScoringHandler extends AbstractElementSaxHandler<List<ScoredScan>> implements ITopLevelSaxHandler {
    public static final XTandemScoringHandler[] EMPTY_ARRAY = {};


    public static final String TAG = "bioml";

    private ScanScoringReport m_Report;
    private final Map<String,ScoredScan> m_Scans = new  HashMap<String,ScoredScan>();

    public XTandemScoringHandler( ScoringProcesstype type ) {
        super(TAG, (DelegatingSaxHandler) null);
        m_Report = new ScanScoringReport(ScoringProcesstype.XTandem);
        setElementObject(new ArrayList<ScoredScan>());
    }

    public XTandemScoringHandler(  ) {
        this(ScoringProcesstype.XTandem);
    }

    public ScanScoringReport getReport() {
        return m_Report;
    }

    public void setReport(final ScanScoringReport report) {
        m_Report = report;
    }

    @Override
    public void startElement(final String uri, final String localName, final String qName, final Attributes attributes) throws SAXException {
        if ("group".equals(qName)) {
             BiomlScanReportHandler handler = new BiomlScanReportHandler(this);
            getHandler().pushCurrentHandler(handler);
            handler.handleAttributes(uri, localName, qName, attributes);
            return;
        }
        if ("dot_product".equals(qName)) {
            List<ScoredScan> elementObject = getElementObject();
            DotProductScoringHandler handler = new DotProductScoringHandler(this, getReport());
            getHandler().pushCurrentHandler(handler);
            handler.handleAttributes(uri, localName, qName, attributes);
            return;
        }
        if ("total".equals(qName)) {
            return;
        }
        if ("file".equals(qName)) {
            return;
        }

         super.startElement(uri, localName, qName, attributes);    //To change body of overridden methods use File | Settings | File Templates.
    }


    @Override
    public void endElement(final String elx, final String localName, final String el) throws SAXException {
        if ("dot_product".equals(el)) {
            ISaxHandler handler1 = getHandler().popCurrentHandler();
            if (handler1 instanceof DotProductScoringHandler) {
                DotProductScoringHandler handler = (DotProductScoringHandler) handler1;
            }
            return;
        }
        if ("group".equals(el)) {
            ISaxHandler iSaxHandler = getHandler().popCurrentHandler();
             BiomlScanReportHandler handler =  (BiomlScanReportHandler) iSaxHandler;
            ScoredScan scan = handler.getElementObject();

             List<ScoredScan> report = getElementObject();
              report.add(scan);
            return;
        }
        
        if ("total".equals(el)) {
            return;
        }
        if ("file".equals(el)) {
            return;
        }
         super.endElement(elx, localName, el);    //To change body of overridden methods use File | Settings | File Templates.
    }

    /**
     * finish handling and set up the enclosed object
     * Usually called when the end tag is seen
     */
    @Override
    public void finishProcessing() {
        ScanScoringReport report = getReport();
        List<ScoredScan> scans  = getElementObject();
        for (ScoredScan scan : scans)   {
            String id = scan.getId();
            if(id != null)   {
                if(m_Scans.containsKey(id))   {
                     possiblyReplaceBestScanatId(id,scan);
                }
                else {
                    m_Scans.put(id  ,scan);
                }
            }
            else  {
                showBadScan(scan);
            }
        }
    }

    protected void possiblyReplaceBestScanatId(final String id, final ScoredScan scan) {
         ScoredScan current = m_Scans.get(id);
        ISpectralMatch currentBest = current.getBestMatch();
        if(currentBest == null)  {
             m_Scans.put(id,scan);
             return;
         }
        ISpectralMatch scanBest = scan.getBestMatch();
        if(scanBest == null)  {
               return;
        }
         double score = currentBest.getHyperScore();
         double scanScore =  scanBest.getHyperScore();
         if(scanScore > score)
              m_Scans.put(id,scan);
    }

    /**
     * who knows how we got here
     * @param scan
     */
    private void showBadScan(final ScoredScan scan) {

        if (false) {
            throw new UnsupportedOperationException("Fix This"); // ToDo
        }


    }

    public Map<String, ScoredScan> getScans() {
        return m_Scans;
    }

    public static void main(String[] args) throws Exception {
        String xTandemFile = null;

        if (args.length > 0)
            xTandemFile = args[0];
        else
            xTandemFile = FileUtilities.getLatestFileWithExtension(".t.txt").getName();
        XTandemScoringHandler handler = new XTandemScoringHandler();
        InputStream is = new FileInputStream(xTandemFile);
        XTandemUtilities.parseFile(is, handler, xTandemFile);
        List<ScoredScan> elementObject = handler.getElementObject();
        ScanScoringReport report = handler.getReport();
        if(true)
            throw new UnsupportedOperationException("Need to patch report"); // ToDo

        if (!report.equivalent(report))
            throw new IllegalStateException("problem"); // ToDo change
        int totalScores = report.getTotalScoreCount();
        IScanScoring[] scanScoring = report.getScanScoring();


        for (int j = 0; j < scanScoring.length; j++) {
            IScanScoring scoring = scanScoring[j];
            XTandemUtilities.outputLine("Scored " + scoring.getId());
            ITheoreticalScoring[] tss = scoring.getScorings();
            TheoreticalScoring.sortByKScore(tss);
            if (tss.length  > 0) {
                 XTandemUtilities.outputLine("Scored   " + tss[0] + " best Score " + tss[0].getTotalKScore());
               }
            if (tss.length  > 1) {
                 XTandemUtilities.outputLine("Scored Next   " + tss[1] + " next best Score " + tss[1].getTotalKScore());
               }
            }


    }
}