package org.systemsbiology.xtandem.fragmentation.ui;

import java.io.*;

/**
 * org.systemsbiology.xtandem.fragmentation.ui.SVGFragmentBuilder
 * User: Steve
 * Date: 6/26/12
 */
public abstract class SVGFragmentBuilder extends AbstractHtmlFragmentHolder {
    public static final SVGFragmentBuilder[] EMPTY_ARRAY = {};


    private final int m_Indent;
    private final String m_Tag;
    private Integer m_Index;
    private Integer m_X;
    private Integer m_Y;
    private Integer m_Width;
    private Integer m_Height;
    private String m_PopupText;

    protected SVGFragmentBuilder(IHtmlFragmentHolder parent, final String tag) {
        super(parent);
        m_Tag = tag;
        SVGFragmentBuilder svgParent = getSVGParent();
        if (svgParent != null) {
            m_Indent = svgParent.getIndent() + 1;
        }
        else {
            m_Indent = 1;

        }
    }

    public String getTag() {
        return m_Tag;
    }

    public SVGFragmentBuilder getSVGParent() {
        IHtmlFragmentHolder parent = getParent();
        if (parent instanceof SVGFragmentBuilder) {
            return (SVGFragmentBuilder) parent;
        }
        return null;
    }

    public String getPopupText() {
        return m_PopupText;
    }

    public void setPopupText(final String popupText) {
        m_PopupText = popupText;
    }

    public int getX() {
        return m_X;
    }

    public int getY() {
        return m_Y;
    }

    public Integer getWidth() {
        return m_Width;
    }

    public Integer getHeight() {
        return m_Height;
    }

    public int getIndent() {
        return m_Indent;
    }

    public void setX(final Integer x) {
        m_X = x;
    }

    public void setY(final Integer y) {
        m_Y = y;
    }

    public void setWidth(final Integer width) {
        m_Width = width;
    }

    public void setHeight(final Integer height) {
        m_Height = height;
    }


    public final Integer getIndex() {
        return m_Index;
    }

    public void setIndex(final Integer index) {
        m_Index = index;
    }

    protected void indent(final Appendable out) {
        try {
            for (int i = 0; i < getIndent(); i++) {
                out.append("    ");
            }
        }
        catch (IOException e) {
            throw new RuntimeException(e);

        }
    }

    protected boolean isTagOnSeparateLine() {
        return true;
    }


    @Override
    public void addBuilder(final IHtmlFragmentBuilder added) {
        super.addBuilder(added);
        if (added instanceof SVGFragmentBuilder) {
            SVGFragmentBuilder addedSVG = (SVGFragmentBuilder) added;
            String tooltip = addedSVG.getPopupText();
            if (tooltip != null) {
                ToolTipTextBuillder tb = new ToolTipTextBuillder(this, addedSVG);
            }

        }
    }

    /**
     * override if you do not want the start tag on a new line
     *
     * @return
     */
    protected String getTagAttributes() {
        StringBuilder sb = new StringBuilder();
        sb.append("id=\"" + getUniqueId() + "\" ");
        return sb.toString();
    }

    @Override
    protected void appendAllBuilders(final Appendable out, final Object[] data) {
        super.appendAllBuilders(out, data);
    }

    /**
     * do not override = override getTagAttributes() and
     * getTagStartEndCharacter() for whether to end in a newline
     *
     * @param out
     * @param data
     */
    @Override
    public final void addStartText(final Appendable out, final Object... data) {
        try {
            indent(out);
            out.append("<");
            out.append(getTag());
            out.append(" ");
            out.append(getTagAttributes());
            out.append(" ");
            out.append(">");
            if (isTagOnSeparateLine())
                out.append("\n");
        }
        catch (IOException e) {
            throw new RuntimeException(e);

        }

    }

    @Override
    public final void addEndText(final Appendable out, final Object... data) {
        try {
            if (isTagOnSeparateLine())
                indent(out);
            out.append("</");
            out.append(getTag());
            out.append(">\n");
        }
        catch (IOException e) {
            throw new RuntimeException(e);

        }

    }
}
