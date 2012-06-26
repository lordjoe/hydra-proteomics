package org.systemsbiology.xtandem.fragmentation.ui;

import java.io.*;

/**
 * org.systemsbiology.xtandem.fragmentation.ui.HTMLPageBuillder
 * User: Steve
 * Date: 6/25/12
 */
public class ToolTipTextBuillder extends SVGFragmentBuilder {
    public static final ToolTipTextBuillder[] EMPTY_ARRAY = {};

    public static final String DO_MOUSEOVER_TEXT =
            "<set attributeName=\"visibility\" from=\"hidden\" to=\"visible\" begin=\"%MY_ID%.mouseover\" end=\"%MY_ID%.mouseout\"/>\n";
    public static final int POPUP_FONT_SIZE = 30;

    public static final String TAG = "text";

    private final SVGFragmentBuilder m_Target;

    public ToolTipTextBuillder(final HTMLPageBuillder page, SVGFragmentBuilder parent, SVGFragmentBuilder target) {

        super(page, parent, TAG);
        m_Target = target;
    }

    public SVGFragmentBuilder getTarget() {
        return m_Target;
    }

    @Override
    protected String getTagAttributes() {
        StringBuilder sb = new StringBuilder(super.getTagAttributes());
        SVGFragmentBuilder target = getTarget();
        sb.append(" x=\"" + (target.getX() + target.getWidth() / 2) + "\"");
        sb.append(" y=\"" + target.getY() + "\"");
        sb.append(" font-size=\"" + POPUP_FONT_SIZE + "\"");
        sb.append(" fill=\"black\" ");
        sb.append(" stroke=\"black\" ");
        sb.append(" stroke-width=\".5px\" ");
        sb.append(" visibility=\"hidden\" ");
        return sb.toString();

    }

    @Override
    protected boolean isTagOnSeparateLine() {
        return false;
    }

    @Override
    protected void appendAllBuilders(final Appendable out, final Object[] data) {
        super.appendAllBuilders(out, data);
        SVGFragmentBuilder target = getTarget();
        String mouseOver = DO_MOUSEOVER_TEXT.replace("%MY_ID%", target.getUniqueId());
        try {
            out.append(target.getPopupText());
            out.append("\n");
            indent(out);
            out.append(mouseOver);
        }
        catch (IOException e) {
            throw new RuntimeException(e);

        }
    }
}
