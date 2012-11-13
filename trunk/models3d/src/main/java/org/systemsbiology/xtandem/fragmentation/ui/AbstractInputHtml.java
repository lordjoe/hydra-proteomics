package org.systemsbiology.xtandem.fragmentation.ui;

import java.io.*;

/**
 * org.systemsbiology.xtandem.fragmentation.ui.HTMLPageBuillder
 * User: Steve
 * Date: 6/25/12
 */
public class AbstractInputHtml extends AbstractHtmlFragmentHolder {
    public static final AbstractInputHtml[] EMPTY_ARRAY = {};

    public static final String TAG = "input";

    private final HtmlInputTypes m_Type;
    public AbstractInputHtml(final HTMLFormBuillder page, HtmlInputTypes actionUrl) {
        super(page);
        m_Type = actionUrl;
     }


    public HtmlInputTypes getType() {
        return m_Type;
    }

    @Override
    public void addStartText(final Appendable out, final Object... data) {
        try {
            out.append("<" + TAG);
            out.append(" type=\"" + getType() + "\">");
            out.append("\n");
        }
        catch (IOException e) {
            throw new RuntimeException(e);

        }

    }

    @Override
    public void addEndText(final Appendable out, final Object... data) {
        try {
            out.append("</" + TAG + ">");
            out.append("\n");
        }
        catch (IOException e) {
            throw new RuntimeException(e);

        }

    }





}
