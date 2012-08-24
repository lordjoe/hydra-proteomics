package org.systemsbiology.xtandem.fragmentation.ui;

import org.systemsbiology.xtandem.fragmentation.*;

/**
 * org.systemsbiology.xtandem.fragmentation.ui.HTMLPageBuillder
 * User: Steve
 * Date: 6/25/12
 */
public class RectFragmentBuillder extends SVGFragmentBuilder {
    public static final RectFragmentBuillder[] EMPTY_ARRAY = {};

    public static final String TAG = "rect";

    private final String m_CoverageColor;

    public RectFragmentBuillder( SVGFragmentBuilder parent, String coverageColor) {

        super(  parent, TAG);
        m_CoverageColor = coverageColor;
    }

    public String getCoverageColor() {
        return m_CoverageColor;
    }

    @Override
    protected String getTagAttributes() {
        StringBuilder sb = new StringBuilder(super.getTagAttributes());
        DetectedFragmentBuillder fp = getParentOfType(DetectedFragmentBuillder.class);
        ProteinFragment fragment = fp.getFragment();
        sb.append(getTransformText());
 //       sb.append("  fill=\"" + getCoverageColor() + "\" ");
        if(fragment.hasMissedCleavages())
            sb.append(" style=\"fill:"   + getCoverageColor() + ";stroke-width:3;stroke:black\" ");
        else {
            sb.append(" style=\"fill:"   + getCoverageColor() + ";stroke-width:0.5px;stroke:black\" ");

        }
     //    sb.append(" transform=\"translate(" + getX() + "," + getY() + ")\" ");
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
