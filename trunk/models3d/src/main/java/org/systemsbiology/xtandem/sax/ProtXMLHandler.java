package org.systemsbiology.xtandem.sax;

import org.systemsbiology.jmol.*;
import org.systemsbiology.sax.*;
import org.systemsbiology.xtandem.*;
import org.systemsbiology.xtandem.bioml.sax.*;
import org.systemsbiology.xtandem.peptide.*;
import org.xml.sax.*;

import java.io.*;
import java.util.*;

/**
 * org.systemsbiology.xtandem.sax.ProtXMLHandler
 * User: steven
 * Date: 6/22/11
 */
public class ProtXMLHandler extends AbstractXTandemElementSaxHandler<ProteomicExperiment> implements ITopLevelSaxHandler {
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
            if (up != null) {
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


    public static Uniprot getUniprot(ProtXMLDetection det) {
        Uniprot ret = null;
        String name = det.getName();
          int proteinLength = det.getProteinLength();
        int non_decoy = 0;
        boolean containsExcluded = UniprotUtilities.containsExcludedFragment(name);
        if (!containsExcluded) {
            ret = Uniprot.getByQuery(name, proteinLength);
            if (ret != null)
                return ret;
            non_decoy++;
        }
        String[] alternateNames = det.getAlternateNames();
        for (int i = 0; i < alternateNames.length; i++) {
            String alternateName = alternateNames[i];
            containsExcluded = UniprotUtilities.containsExcludedFragment(alternateName);
            if (!name.toUpperCase().contains("DECOY"))
                non_decoy++;
            if (name.equals(alternateName))
                continue; // we have checked this
            ret = Uniprot.getByQuery(name, proteinLength);
            if (ret != null)
                return ret;

        }
        if(non_decoy == 0)
            return null; // igmore decoys
        // try 2
        if(!containsExcluded)     {
            ret = Uniprot.getByQuery(name, proteinLength);
             if (ret != null)
                 return ret;

        }

        for (int i = 0; i < alternateNames.length; i++) {
            String alternateName = alternateNames[i];
            ret = Uniprot.getByQuery(name, proteinLength);
            if (ret != null)
                return ret;

        }
        return ret; // found nothing
    }

    public static void main(String[] args) throws Exception {
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            ProtXMLHandler handler = new ProtXMLHandler();
            InputStream is = new FileInputStream(arg);
            XTandemUtilities.parseFile(is, handler, arg);
            ProteomicExperiment report = handler.getElementObject();



            int nFound = 0;
            int nNotFound = 0;
            ProtXMLDetection[] dets = report.getDetections();
            List<Uniprot> holder = new ArrayList<Uniprot>();

                for (int j = 0; j < dets.length; j++) {
                ProtXMLDetection det = dets[j];
                String name = det.getName();
                Uniprot up = getUniprot(det);
                if (up != null)    {
                       nFound++;
                }
                else
                    nNotFound++;

            }
            Uniprot[] found = new Uniprot[holder.size()];
             holder.toArray(found);
            for (int j = 0; j < found.length; j++) {
                Uniprot uniprot = found[j];
                BioJavaModel[] allModels = uniprot.getAllModels();
            }
            UniprotUtilities.saveStringtoUniprotIdFile();
            XTandemUtilities.breakHere();
        }

    }
}
