package org.systemsbiology.xtandem.fragmentation.ui;

import java.io.*;

/**
 * org.systemsbiology.xtandem.fragmentation.ui.HTMLPageBuillder
 * User: Steve
 * Date: 6/25/12
 */
public class ReferenceTableBuillder extends AbstractHtmlFragmentHolder {
    public static final ReferenceTableBuillder[] EMPTY_ARRAY = {};

    private final String[] m_Texts;
    private final String[] m_References;
    private final int m_RowLength;
    public ReferenceTableBuillder(final IHtmlFragmentHolder page, String[] text, String[] refs,int rowLength) {
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
                String text = m_Texts[i];
                out.append("<td>");
                  out.append("<a href=\"" + ref + "\">" + text + "</a>\n");
                  out.append("</td>");
                  if(col++ > getRowLength()) {
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
