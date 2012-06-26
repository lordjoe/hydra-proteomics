package org.systemsbiology.xtandem.fragmentation.ui;

import java.io.*;
import java.util.*;

/**
 * org.systemsbiology.xtandem.fragmentation.ui.HtmlFragmentHolder
 * User: Steve
 * Date: 6/25/12
 */
public abstract class AbstractHtmlFragmentHolder implements IHtmlFragmentHolder {
    public static final AbstractHtmlFragmentHolder[] EMPTY_ARRAY = {};

    public static void indent(final Appendable out,int n) {
        try {
            for (int i = 0; i < n; i++) {
                out.append("    ");
            }
        }
        catch (IOException e) {
            throw new RuntimeException(e);

        }
    }


    private final HTMLPageBuillder m_Page;
    private final List<IHtmlFragmentBuilder>  m_Builders = new ArrayList<IHtmlFragmentBuilder>();

    protected AbstractHtmlFragmentHolder(final HTMLPageBuillder page) {
        m_Page = page;
    }

    public HTMLPageBuillder getPage() {
        return m_Page;
    }


    public String getUniqueId() {
        return getPage().getUniqueId();
    }



    public abstract void addStartText(final Appendable out, final Object... data);

    public abstract void addEndText(final Appendable out, final Object... data);

    @Override
    public void addBuilder(final IHtmlFragmentBuilder added) {
        m_Builders.add(added);

    }

    @Override
    public void addString(final String added) {
        addBuilder(new HTMLTextHolder(added));

    }

    @Override
    public void appendFragment(final Appendable out, final Object... data) {
        addStartText( out, data);
        appendAllBuilders(out, data);
        addEndText( out, data);

    }

    protected void appendAllBuilders(final Appendable out, final Object[] data) {
        for(IHtmlFragmentBuilder builder : m_Builders)  {
           builder.appendFragment( out, data);
        }
    }
}
