package org.systemsbiology.xtandem.fragmentation.ui;

import java.io.*;

/**
 * org.systemsbiology.xtandem.fragmentation.ui.HTMLPageBuillder
 * User: Steve
 * Date: 6/25/12
 */
public class HTMLPageBuillder   extends AbstractHtmlFragmentHolder {
    public static final HTMLPageBuillder[] EMPTY_ARRAY = {};

    public static final String META_INFO =
            "<meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\">";

    private final String m_Title;
    private final HTMLHeaderBuillder m_Header;
    private final HTMLBodyBuillder m_Body;
    private int m_IdIndex = 1;

    public HTMLPageBuillder(final String title) {
        super(null);
        m_Title = title;
        m_Header = new HTMLHeaderBuillder(this);
        m_Body = new HTMLBodyBuillder(this);
        HTMLHeaderBuillder header = getHeader();
        header.addString(META_INFO);
        header.addString("<title>" + getTitle() + "</title>");
    }

    @Override
    public HTMLPageBuillder getPage() {
        return this;
    }

    public String getTitle() {
        return m_Title;
    }

    public HTMLHeaderBuillder getHeader() {
        return m_Header;
    }

    public HTMLBodyBuillder getBody() {
        return m_Body;
    }

    @Override
    public String getUniqueId() {
        return "Item" + m_IdIndex++;
    }

    @Override
    public void addStartText(final Appendable out, final Object... data) {
        try {
            out.append("<html>");
            out.append("\n");
        }
        catch (IOException e) {
            throw new RuntimeException(e);

        }

    }

    @Override
    public void addEndText(final Appendable out, final Object... data) {
        try {
            out.append("</html>");
            out.append("\n");
        }
        catch (IOException e) {
            throw new RuntimeException(e);

        }

    }

    public String buildPage(final Object... data)
    {
        StringBuilder sb = new StringBuilder();
        appendFragment(sb, data);

        return sb.toString();
    }



}
