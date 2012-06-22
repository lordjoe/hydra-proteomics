package org.systemsbiology.xtandem.sax;

import org.systemsbiology.jmol.*;
import org.systemsbiology.xtandem.*;
import org.xml.sax.*;

import java.io.*;

/**
 * org.systemsbiology.xtandem.sax.JXTandemMassScoringHandler
 * User: steven
 * Date: 6/22/11
 */
public class ThreeDModelStructureHandler extends AbstractElementSaxHandler<ThreeDModelStructure>  {
    public static final ThreeDModelStructureHandler[] EMPTY_ARRAY = {};


    public static final String TAG = "alignment";


    private String m_Property;

    public ThreeDModelStructureHandler( IElementHandler pParent) {
        super(TAG, pParent);
        setElementObject(new ThreeDModelStructure());
     }

    @Override
    public void startElement(final String uri, final String localName, final String qName, final Attributes attributes) throws SAXException {
        if ("alignObject".equals(qName)) {
            String type = attributes.getValue("type");
            if ("STRUCTURE".equals(type)) {
                ThreeDModelStructure elementObject = getElementObject();
                String id = attributes.getValue("dbAccessionId");
                 elementObject.setAccessionId(id);
                return;
            }
            if ("PROTEIN".equals(type)) {
                ThreeDModelStructure elementObject = getElementObject();
                String id = attributes.getValue("dbAccessionId");
                elementObject.setProteinId(id);

            }
        }
        if ("alignObjectDetail".equals(qName)) {
             handleAlignObjectDetail(uri, localName, qName, attributes);
             return;
        }
        if ("block".equals(qName)) {
            return;
        }
        if ("segment".equals(qName)) {
              return;
        }
         super.startElement(uri, localName, qName, attributes);    //To change body of overridden methods use File | Settings | File Templates.
    }

      public void handleAlignObjectDetail(final String uri, final String localName, final String qName, final Attributes attributes) throws SAXException {
          clearIncludedText();
           m_Property  = attributes.getValue("property");
       }





    @Override
    public void endElement(final String elx, final String localName, final String el) throws SAXException {
        if ("alignObject".equals(el)) {
            String added =  getIncludedText().trim();
            if(added.length() == 0)
                return;
            clearIncludedText();
             ThreeDModelStructure elementObject = getElementObject();
             elementObject.addProperty(m_Property,added);
            return;
        }
         if ("alignObjectDetail".equals(el)) {
               return;
           }
        if ("block".equals(el)) {
               return;
           }
        if ("segment".equals(el)) {
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


}
