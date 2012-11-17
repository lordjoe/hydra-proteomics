package org.systemsbiology.xtandem.fragmentation.ui.form;

import org.systemsbiology.xtandem.fragmentation.ui.*;

import java.io.*;

/**
 * org.systemsbiology.xtandem.fragmentation.ui.HTMLPageBuillder
 * User: Steve
 * Date: 6/25/12
 */
public class HTMLFormBuillder extends AbstractHtmlFragmentHolder {
    public static final HTMLFormBuillder[] EMPTY_ARRAY = {};

    public static final String TAG = "form";

    private final String m_ActionUrl;
    public HTMLFormBuillder(final HTMLBodyBuillder  page, String actionUrl) {
        super(page);
        m_ActionUrl = actionUrl;
        addSubmitButton();
    }

    protected void addSubmitButton() {
       new SubmitButtonBuilder(this);
    }

    public String getActionUrl() {
        return m_ActionUrl;
    }

    @Override
    public void addStartText(final Appendable out, final Object... data) {
        try {
            out.append("<" + TAG);
            out.append(" action=\"" + getActionUrl() + "\">");
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
