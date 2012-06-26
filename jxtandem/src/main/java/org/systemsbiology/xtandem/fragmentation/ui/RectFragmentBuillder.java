package org.systemsbiology.xtandem.fragmentation.ui;

/**
 * org.systemsbiology.xtandem.fragmentation.ui.HTMLPageBuillder
 * User: Steve
 * Date: 6/25/12
 */
public class RectFragmentBuillder extends SVGFragmentBuilder {
    public static final RectFragmentBuillder[] EMPTY_ARRAY = {};

    public static final String TAG = "rect";

    private final String m_CoverageColor;

    public RectFragmentBuillder(final HTMLPageBuillder page, SVGFragmentBuilder parent, String coverageColor) {

        super(page, parent, TAG);
        m_CoverageColor = coverageColor;
    }

    public String getCoverageColor() {
        return m_CoverageColor;
    }

    @Override
    protected String getTagAttributes() {
        StringBuilder sb = new StringBuilder(super.getTagAttributes());
        sb.append(" style=\"fill:" + getCoverageColor() + ";\" ");
        sb.append(" transform=\"translate(" + getX() + "," + getY() + ")\" ");
        sb.append(" width=\"" + getWidth() + "\"");
        sb.append(" height=\"" + getHeight() + "\"");
        return sb.toString();

    }

    /**
     * this version keels the object on one line
     */
    @Override
    protected boolean isTagOnSeparateLine()
    {
        return false;
    }

}
