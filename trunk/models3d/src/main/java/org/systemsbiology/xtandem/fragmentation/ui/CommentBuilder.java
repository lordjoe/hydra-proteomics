package org.systemsbiology.xtandem.fragmentation.ui;

import java.io.*;

/**
 * org.systemsbiology.xtandem.fragmentation.ui.CommentBuilder
 * insert an html comment
 * User: Steve
 * Date: 6/25/12
 */
public class CommentBuilder extends AbstractHtmlFragmentHolder {
    public static final CommentBuilder[] EMPTY_ARRAY = {};

    private final String m_Text;
    public CommentBuilder(final IHtmlFragmentHolder page, String text) {
        super(page);
        m_Text = text;
    }

    @Override
    public void addStartText(final Appendable out, final Object... data) {
        try {
            out.append("<!-- " + m_Text + " -->");
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
