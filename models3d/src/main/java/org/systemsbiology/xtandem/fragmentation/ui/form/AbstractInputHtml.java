package org.systemsbiology.xtandem.fragmentation.ui.form;

import org.systemsbiology.xtandem.fragmentation.ui.*;

import java.io.*;

/**
 * org.systemsbiology.xtandem.fragmentation.ui.HTMLPageBuillder
 * User: Steve
 * Date: 6/25/12
 */
public class AbstractInputHtml extends AbstractHtmlFragmentHolder {
    public static final AbstractInputHtml[] EMPTY_ARRAY = {};

    public static final String TAG = "input";

    private final String m_Name;
    private final HtmlInputTypes m_Type;
    public AbstractInputHtml(final HTMLFormBuillder page,String name, HtmlInputTypes actionUrl) {
        super(page);
        m_Type = actionUrl;
        m_Name = name;
     }

    public String getTag() {
        return TAG;
    }

    public String getName() {
        return m_Name;
    }

    public HtmlInputTypes getType() {
        return m_Type;
    }

    @Override
    public void addStartText(final Appendable out, final Object... data) {
        try {
            out.append("<" + getTag());
            out.append(" type=\"" + getType());
            addOtherAttributes( out,  data);
            out.append("\">");
            out.append("\n");
        }
        catch (IOException e) {
            throw new RuntimeException(e);

        }

    }

    /**
     * overtide to set other attributes
     * @param out
     * @param data
     */
    public void addOtherAttributes(Appendable out, Object[] data) {
    }

    @Override
    public void addEndText(final Appendable out, final Object... data) {
        try {
            out.append("</" + getTag() + ">");
            out.append("\n");
        }
        catch (IOException e) {
            throw new RuntimeException(e);

        }

    }





}
