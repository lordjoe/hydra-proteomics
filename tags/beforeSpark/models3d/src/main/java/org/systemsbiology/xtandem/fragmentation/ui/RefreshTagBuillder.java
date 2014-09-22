package org.systemsbiology.xtandem.fragmentation.ui;

import java.io.*;

/**
 * org.systemsbiology.xtandem.fragmentation.ui.HTMLPageBuillder
 * User: Steve
 * Date: 6/25/12
 */
public class RefreshTagBuillder extends AbstractHtmlFragmentHolder {
    public static final RefreshTagBuillder[] EMPTY_ARRAY = {};

    private final String m_URL;
    private final int  m_Wait;
    public RefreshTagBuillder(final HTMLHeaderBuillder parent, String tag,int wait) {
        super(parent);
        m_URL = tag;
        m_Wait = wait;
    }

    public String getURL() {
        return m_URL;
    }

    public int getWait() {
        return m_Wait;
    }

    @Override
    public void addStartText(final Appendable out, final Object... data) {
        try {
            out.append("<meta http-equiv=\"refresh\" content=\"" + getWait() + ";URL='" + getURL() + "'\">");
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
