package org.systemsbiology.xtandem.fragmentation.ui;

import java.io.*;

/**
 * org.systemsbiology.xtandem.fragmentation.ui.HTMLPageBuillder
 * User: Steve
 * Date: 6/25/12
 */
public class HTMLHeaderBuillder extends AbstractHtmlFragmentHolder {
    public static final HTMLHeaderBuillder[] EMPTY_ARRAY = {};

    public HTMLHeaderBuillder(final HTMLPageBuillder page) {
        super(page);
    }

    @Override
    public void addStartText(final Appendable out, final Object... data) {
        try {
            out.append("<head>");
            out.append("\n");
        }
        catch (IOException e) {
            throw new RuntimeException(e);

        }

    }

    @Override
    public void addEndText(final Appendable out, final Object... data) {
        try {
            out.append("</head>");
            out.append("\n");
        }
        catch (IOException e) {
            throw new RuntimeException(e);

        }

    }





}
