package org.systemsbiology.xtandem.sax;

import org.systemsbiology.jmol.*;
import org.systemsbiology.sax.*;
import org.systemsbiology.xtandem.*;
import org.systemsbiology.xtandem.bioml.sax.*;
import org.xml.sax.*;

import java.io.*;

/**
 * org.systemsbiology.xtandem.sax.JXTandemMassScoringHandler
 * User: steven
 * Date: 6/22/11
 */
public class ThreeDModelHandler extends AbstractXTandemElementSaxHandler<ThreeDModel> implements ITopLevelSaxHandler {
    public static final ThreeDModelHandler[] EMPTY_ARRAY = {};


    public static final String TAG = "dasalignment";


    public ThreeDModelHandler() {
        super(TAG, (DelegatingSaxHandler) null);
        setElementObject(new ThreeDModel());
    }

    @Override
    public void startElement(final String uri, final String localName, final String qName, final Attributes attributes) throws SAXException {
        if ("alignment".equals(qName)) {
            ThreeDModel elementObject = getElementObject();
            ISaxHandler handler = new ThreeDModelStructureHandler(this);
            getHandler().pushCurrentHandler(handler);
            handler.handleAttributes(uri, localName, qName, attributes);
            return;
          }
        super.startElement(uri, localName, qName, attributes);    //To change body of overridden methods use File | Settings | File Templates.
    }


    @Override
    public void endElement(final String elx, final String localName, final String el) throws SAXException {
         if ("alignment".equals(el)) {
             ISaxHandler ch = getHandler().popCurrentHandler();
             ThreeDModelStructureHandler handler = (ThreeDModelStructureHandler) ch;
             ThreeDModelStructure scan = handler.getElementObject();
             ThreeDModel model = getElementObject();
             model.addStructure(scan);
             String proteinId = scan.getProteinId();
             model.setProteinId(proteinId);
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
            ThreeDModelHandler handler = new ThreeDModelHandler();
            InputStream is = new FileInputStream(arg);
            XTandemUtilities.parseFile(is, handler, arg);
            ThreeDModel report = handler.getElementObject();
        }

    }
}
