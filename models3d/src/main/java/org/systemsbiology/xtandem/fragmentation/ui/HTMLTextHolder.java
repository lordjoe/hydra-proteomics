package org.systemsbiology.xtandem.fragmentation.ui;

import java.io.*;

/**
 * org.systemsbiology.xtandem.fragmentation.ui.HTMLTextHolder
 * User: Steve
 * Date: 6/25/12
 */
public class HTMLTextHolder implements IHtmlFragmentBuilder {
    public static final HTMLTextHolder[] EMPTY_ARRAY = {};

    private final IHtmlFragmentHolder m_Parent;
    private final String m_Text;

    public HTMLTextHolder(IHtmlFragmentHolder parent,final String text) {
        m_Text = text;
        m_Parent = parent;
        if(parent instanceof AbstractHtmlFragmentHolder)
             ((AbstractHtmlFragmentHolder)parent).addBuilder(this);
     }


    public IHtmlFragmentHolder getParent() {
        return m_Parent;
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
