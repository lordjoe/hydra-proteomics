package org.systemsbiology.xtandem.fragmentation.ui;

import java.io.*;

/**
 * org.systemsbiology.xtandem.fragmentation.ui.HTMLTextHolder
 * User: Steve
 * Date: 6/25/12
 */
public class HTMLHeaderHolder implements IHtmlFragmentBuilder {
    public static final HTMLHeaderHolder[] EMPTY_ARRAY = {};

    private final IHtmlFragmentHolder m_Parent;
    private final String m_Text;
    private final int m_Header;

    public HTMLHeaderHolder(IHtmlFragmentHolder parent, final String text,int header) {
        m_Text = text;
        m_Parent = parent;
        m_Header = header;
        if(parent instanceof AbstractHtmlFragmentHolder)
             ((AbstractHtmlFragmentHolder)parent).addBuilder(this);
     }


    public String getText() {
        return m_Text;
    }

    public int getHeader() {
        return m_Header;
    }

    @Override
    public void appendFragment(final Appendable out, final Object... data) {
        try {
            out.append("<h" + getHeader() + ">" +  getText() + "</h" + getHeader() + ">");
            out.append("\n");
         }
        catch (IOException e) {
            throw new RuntimeException(e);

        }

    }
}
