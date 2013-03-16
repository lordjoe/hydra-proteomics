package org.systemsbiology.xtandem.sax;

import org.systemsbiology.xtandem.*;
import org.systemsbiology.xtandem.peptide.*;
import org.xml.sax.*;

import java.io.*;

/**
 * org.systemsbiology.xtandem.sax.ProtXMLHandler
 * User: steven
 * Date: 6/22/11
 */
public class ProtXMLHandler extends AbstractElementSaxHandler<ProteomicExperiment> implements ITopLevelSaxHandler {
    public static final ProtXMLHandler[] EMPTY_ARRAY = {};


    public static final String TAG = "protein_summary";


    public ProtXMLHandler() {
        super(TAG, (DelegatingSaxHandler) null);
        setElementObject(new ProteomicExperiment());
    }


    @Override
    public void startElement(final String uri, final String localName, final String qName, final Attributes attributes) throws SAXException {
        if ("protein_summary_header".equals(qName)) {
            setIgnoreTagContents(uri, localName, qName, attributes);
            return;
        }
        if ("dataset_derivation".equals(qName)) {
             setIgnoreTagContents(uri, localName, qName, attributes);
             return;
         }
         if ("protein_group".equals(qName)) {  // we are really interested in the protein tag
            ProtXMLProteinHandler handler = new ProtXMLProteinHandler(this);
            getHandler().pushCurrentHandler(handler);
            handler.handleAttributes(uri, localName, qName, attributes);
            return;
        }
    }


    @Override
    public void endElement(final String elx, final String localName, final String el) throws SAXException {
        if ("protein_summary_header".equals(el)) {
            ISaxHandler ch = getHandler().popCurrentHandler();
            return;
        }
        if ("dataset_derivation".equals(el)) {
            ISaxHandler ch = getHandler().popCurrentHandler();
            return;
        }
        if ("protein_group".equals(el)) {  // we are really interested in the protein tag
            ISaxHandler ch = getHandler().popCurrentHandler();
            ProtXMLProteinHandler handler = (ProtXMLProteinHandler) ch;
            ProtXMLDetection up = handler.getElementObject();
            if(up != null)    {
                ProteomicExperiment exp = getElementObject();
                exp.addDetection(up);

            }
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

    }

    public static void main(String[] args) throws Exception {
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            ProtXMLHandler handler = new ProtXMLHandler();
            InputStream is = new FileInputStream(arg);
            XTandemUtilities.parseFile(is, handler, arg);
            ProteomicExperiment report = handler.getElementObject();

            ProtXMLDetection[] dets = report.getDetections();
              for (int j = 0; j < dets.length; j++) {
                  ProtXMLDetection det = dets[j];
                  String name = det.getName();

            }
        }

    }
}
