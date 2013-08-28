package org.systemsbiology.xtandem.sax;

import org.systemsbiology.sax.*;
import org.systemsbiology.xtandem.bioml.sax.*;
import org.systemsbiology.xtandem.peptide.*;
import org.xml.sax.*;

/**
 * org.systemsbiology.xtandem.sax.JXTandemMassScoringHandler
 * User: steven
 * Date: 6/22/11
 */
public class ProtXMLProteinHandler extends AbstractXTandemElementSaxHandler<ProtXMLDetection> {
    public static final ProtXMLProteinHandler[] EMPTY_ARRAY = {};


    public static final String TAG = "protein_group";


    private String m_AllPeptides;

    public ProtXMLProteinHandler(IElementHandler pParent) {
        super(TAG, pParent);
        setElementObject(new ProtXMLDetection());
    }

    @Override
    public void startElement(final String uri, final String localName, final String qName, final Attributes attributes) throws SAXException {
        if ("peptide".equals(qName)) {
            handleAttributes(uri, localName, qName, attributes);
            setIgnoreTagContents(uri, localName, qName, attributes);
            return; // we handle this
        }
        if ("indistinguishable_protein".equals(qName)) {
            handleAttributes(uri, localName, qName, attributes);
            return; // we handle this
        }

        if ("protein".equals(qName)) {
            handleAttributes(uri, localName, qName, attributes);
            return; // we handle this
        }
        if ("parameter".equals(qName)) {
            handleAttributes(uri, localName, qName, attributes);
            return; // we handle this
        }
        if ("modification_info".equals(qName)) {
            return; // we handle this
        }
        if ("peptide_parent_protein".equals(qName)) {
            return; // we handle this
        }
        if ("annotation".equals(qName)) {
            ProtXMLDetection elementObject = getElementObject();
            elementObject.setAnnotation(attributes.getValue("protein_description"));
            return;
        }
        super.startElement(uri, localName, qName, attributes);    //To change body of overridden methods use File | Settings | File Templates.
    }


    /**
     * Divides the handling of a start element into handling the element and handling
     * Attributes
     * <p/>
     * <p>By default, do nothing.  Application writers may override this
     * method in a subclass to take specific actions at the start of
     * each element (such as allocating a new tree node or writing
     * output to a file).</p>
     *
     * @param uri        The Namespace URI, or the empty string if the
     *                   element has no Namespace URI or if Namespace
     *                   processing is not being performed.
     * @param localName  The local name (without prefix), or the
     *                   empty string if Namespace processing is not being
     *                   performed.
     * @param qName      The qualified name (with prefix), or the
     *                   empty string if qualified names are not available.
     * @param attributes The attributes attached to the element.  If
     *                   there are no attributes, it shall be an empty
     *                   Attributes object.
     * @throws org.xml.sax.SAXException Any SAX exception, possibly
     *                                  wrapping another exception.
     * @see org.xml.sax.ContentHandler#startElement
     */
    @Override
    public void handleAttributes(final String uri, final String localName, final String qName, final Attributes attributes) throws SAXException {
        super.handleAttributes(uri, localName, qName, attributes);
        ProtXMLDetection elementObject = getElementObject();
        if ("parameter".equals(qName)) {
            String name = attributes.getValue("name");
            if ("prot_length".equals(name)) {
                elementObject.setProteinLength(XTandemSaxUtilities.getRequiredIntegerAttribute("value", attributes));
               }
            return;
        }
        if ("protein_group".equals(qName)) {
            Double probability = XTandemSaxUtilities.getDoubleAttribute("probability", attributes, 0);
            elementObject.setProbability(probability);
            return;
        }
        if ("protein".equals(qName)) {
            String protein_name = XTandemSaxUtilities.getRequiredAttribute("protein_name", attributes);
            elementObject.setName(protein_name);
            Double percent_coverage = XTandemSaxUtilities.getDoubleAttribute("percent_coverage", attributes, 0.0);
            double fractionCoverage = percent_coverage / 100.0;
            elementObject.setFractionCoverage(fractionCoverage);
            m_AllPeptides = attributes.getValue("unique_stripped_peptides");

        }
        if ("indistinguishable_protein".equals(qName)) {     // todo maybe not
            String protein_name = XTandemSaxUtilities.getRequiredAttribute("protein_name", attributes);
            elementObject.addAlternateName(protein_name);
            //noinspection UnnecessaryReturnStatement
            return; // we handle this
        }
    }


    @Override
    public void endElement(final String elx, final String localName, final String el) throws SAXException {
        if ("protein".equals(el)) {
            buildProtein();
            return; // we handle this
        }

        if ("indistinguishable_protein".equals(el)) {
            return; // we handle this
        }
        if ("parameter".equals(el)) {
            return; // we handle this
        }
        if ("modification_info".equals(el)) {
            return; // we handle this
        }
        if ("peptide_parent_protein".equals(el)) {
            return; // we handle this
        }
        if ("annotation".equals(el)) {
            return; // we handle this
        }
        if ("indistinguishable_peptide".equals(el)) {
            return; // we handle this
        }
        if ("peptide".equals(el)) {
            ISaxHandler ch = getHandler().popCurrentHandler();   // stop ignoring
            return; // we handle this
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

    public String getAllPeptides() {
        return m_AllPeptides;
    }

    public void buildProtein() {
        ProtXMLDetection elementObject = getElementObject();
        String allPeptides = getAllPeptides();
        if (allPeptides == null || allPeptides.length() == 0)
            return; // no peptides

        String[] peptides = allPeptides.split("\\+");
        IPolypeptide[] polypeptides = new IPolypeptide[peptides.length];
        for (int i = 0; i < peptides.length; i++) {
            String peptide = peptides[i];
            polypeptides[i] = Polypeptide.fromString(peptide);
        }
        elementObject.setDetectedPeptides(polypeptides);
    }

}
