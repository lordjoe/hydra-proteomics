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


    private String m_UniqueId;
    private final IHtmlFragmentHolder m_Parent;
    private final List<IHtmlFragmentBuilder>  m_Builders = new ArrayList<IHtmlFragmentBuilder>();

    protected AbstractHtmlFragmentHolder(final IHtmlFragmentHolder parent) {
        m_Parent = parent;
        if(parent instanceof AbstractHtmlFragmentHolder)
            ((AbstractHtmlFragmentHolder)parent).addBuilder(this);
    }

    @Override
    public IHtmlFragmentHolder getParent() {
        return m_Parent;
    }

    @Override
    public HTMLPageBuillder getPage() {
        return getParent().getPage();
    }


      public String getUniqueId() {
        if(m_UniqueId == null)   {
            m_UniqueId = getPage().getUniqueId();
        }
        return m_UniqueId;
    }


    public abstract void addStartText(final Appendable out, final Object... data);

    public abstract void addEndText(final Appendable out, final Object... data);

   // @Override
    protected void addBuilder(final IHtmlFragmentBuilder added) {
        m_Builders.add(added);

    }

    @Override
    public void addString(final String added) {
         new HTMLTextHolder(this,added);

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
