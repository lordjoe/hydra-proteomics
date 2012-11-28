package org.systemsbiology.xtandem.fragmentation.ui;

import java.io.*;

/**
 * org.systemsbiology.xtandem.fragmentation.ui.RefreshTagBuillder
 * User: steven
 * Date: 11/28/12
 */
public class RefreshTagBuillder  extends AbstractHtmlFragmentHolder {
    public static final RefreshTagBuillder[] EMPTY_ARRAY = {};

    private String m_Url;
    private int m_Wait;
    public RefreshTagBuillder(HTMLHeaderBuillder header, String url, int waitSec) {
        super(header);
    }

    @Override
    public void addStartText(Appendable out, Object... data) {
        try {
            out.append(" <meta http-equiv=\"Refresh\" content=\"" + m_Wait + "; URL=\"" + m_Url + "\">");
        }
        catch (IOException e) {
            throw new RuntimeException(e);

        }
    }

    @Override
    public void addEndText(Appendable out, Object... data) {

    }
}
