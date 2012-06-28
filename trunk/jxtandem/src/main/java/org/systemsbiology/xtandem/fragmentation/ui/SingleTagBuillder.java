package org.systemsbiology.xtandem.fragmentation.ui;

import java.io.*;

/**
 * org.systemsbiology.xtandem.fragmentation.ui.HTMLPageBuillder
 * User: Steve
 * Date: 6/25/12
 */
public class SingleTagBuillder extends AbstractHtmlFragmentHolder {
    public static final SingleTagBuillder[] EMPTY_ARRAY = {};

    private final String m_Tag;
    public SingleTagBuillder(final IHtmlFragmentHolder page,String tag) {
        super(page);
        m_Tag = tag;
    }

    @Override
    public void addStartText(final Appendable out, final Object... data) {
        try {
            out.append("<" + m_Tag + "/>");
            out.append("\n");
        }
        catch (IOException e) {
            throw new RuntimeException(e);

        }

    }

    @Override
    public void addEndText(final Appendable out, final Object... data) {
       }





}
