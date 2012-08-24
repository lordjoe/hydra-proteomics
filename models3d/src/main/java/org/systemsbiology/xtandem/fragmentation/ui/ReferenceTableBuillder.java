package org.systemsbiology.xtandem.fragmentation.ui;

import org.systemsbiology.jmol.*;
import org.systemsbiology.xtandem.fragmentation.*;

import java.io.*;

/**
 * org.systemsbiology.xtandem.fragmentation.ui.HTMLPageBuillder
 * User: Steve
 * Date: 6/25/12
 */
public class ReferenceTableBuillder extends AbstractHtmlFragmentHolder {
    public static final ReferenceTableBuillder[] EMPTY_ARRAY = {};

    private final ProteinFragmentationDescription[] m_Texts;
    private final String[] m_References;
    private final int m_RowLength;
    public ReferenceTableBuillder(final IHtmlFragmentHolder page, ProteinFragmentationDescription[] text, String[] refs,int rowLength) {
        super(page);
        m_References = refs;
        m_Texts = text;
        m_RowLength = rowLength;
      }

    public String[] getReferences() {
        return m_References;
    }


    public int getRowLength() {
        return m_RowLength;
    }

    @Override
    protected void appendAllBuilders(Appendable out, Object[] data) {
        int col = 0;
        try {
             out.append("<tr>");
            String[] references = getReferences();
            for (int i = 0; i < references.length; i++) {
                String ref = references[i];
                ProteinFragmentationDescription pd = m_Texts[i];
                out.append("<td>");
                int coveragePercent = (int) (100 * pd.getFractionalCoverage());
                String modeled = "";
                PDBObject model = pd.getModel();
                if(model != null)   {
                   int nf =  pd.getFragments().length;
                   int nm = pd.getAminoAcidLocations().size();
                   int nchains = model.getChains().length;
                   modeled = " "  + nm + "/" + nf + "[" + nchains + "]";
                }

                out.append("<a href=\"" + ref + "\">" + pd.getUniprotId() + " " +  coveragePercent + "% " +  modeled +

                        "</a>\n");
                  out.append("</td>");
                  if(++col  >= getRowLength()) {
                      col = 0;
                      out.append("</tr>\n");
                      out.append("<tr>\n");

                  }

            }
             out.append("</tr>\n");
             out.append("\n");
         }
         catch (IOException e) {
             throw new RuntimeException(e);

         }
     }

    @Override
    public void addStartText(final Appendable out, final Object... data) {
        try {
            out.append("<table>");
            out.append("\n");
        }
        catch (IOException e) {
            throw new RuntimeException(e);

        }

    }

    @Override
    public void addEndText(final Appendable out, final Object... data) {
        try {
             out.append("</table>");
             out.append("\n");
         }
         catch (IOException e) {
             throw new RuntimeException(e);

         }
       }





}
