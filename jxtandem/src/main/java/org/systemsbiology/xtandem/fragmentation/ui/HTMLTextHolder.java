package org.systemsbiology.xtandem.fragmentation.ui;

import java.io.*;

/**
 * org.systemsbiology.xtandem.fragmentation.ui.HTMLTextHolder
 * User: Steve
 * Date: 6/25/12
 */
public class HTMLTextHolder implements IHtmlFragmentBuilder {
    public static final HTMLTextHolder[] EMPTY_ARRAY = {};

    private final String m_Text;

    public HTMLTextHolder(final String text) {
        m_Text = text;
    }


    public String getText() {
        return m_Text;
    }

    @Override
    public void appendFragment(final Appendable out, final Object... data) {
        try {
            out.append( getText());
            out.append("\n");
         }
        catch (IOException e) {
            throw new RuntimeException(e);

        }

    }
}
