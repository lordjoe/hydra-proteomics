package org.systemsbiology.xtandem.fragmentation.ui;

import java.io.*;

/**
 * org.systemsbiology.xtandem.fragmentation.ui.LiteralBuilder
 * User: Steve
 * Date: 6/25/12
 */
public class LiteralBuilder extends AbstractHtmlFragmentHolder {
    public static final LiteralBuilder[] EMPTY_ARRAY = {};

    private final String m_Text;
    public LiteralBuilder(final IHtmlFragmentHolder page, String tag) {
        super(page);
        m_Text = tag;
    }

    @Override
    public void addStartText(final Appendable out, final Object... data) {
        try {
            out.append( m_Text );
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
