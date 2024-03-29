package org.systemsbiology.xtandem.fragmentation.ui;

/**
 * org.systemsbiology.xtandem.fragmentation.ui.AminoAcidTextLine
 * User: Steve
 * Date: 6/25/12
 */
public class AminoAcidTextLine extends SVGFragmentBuilder {
    public static final AminoAcidTextLine[] EMPTY_ARRAY = {};


    public static final String TAG = "g";

    private final ProteinFragmentLine m_LineFragment;

    public AminoAcidTextLine(SVGFragmentBuilder parent, ProteinFragmentLine coverageFragment, int index) {
        super(parent, TAG);
        setIndex(index);
        m_LineFragment = coverageFragment;
        setY(getLineHeight() * getIndex());
        new ProteinLineBuillder(this, m_LineFragment);
    }


    public ProteinFragmentLine getLineFragment() {
        return m_LineFragment;
    }


    protected int getLineHeight() {
        return ((CoverageFragment) getParent()).getLineHeight();
    }

    @Override
    protected String getTagAttributes() {
        StringBuilder sb = new StringBuilder(super.getTagAttributes());
        sb.append(getTransformText());
        //   sb.append("  transform=\"translate(0," +
        //               ( getLineHeight() * getIndex()) + ")\" ");
        return sb.toString();
    }

    @Override
    protected void appendAllBuilders(Appendable out, Object[] data) {
        super.appendAllBuilders(out, data);    //To change body of overridden methods use File | Settings | File Templates.
    }
}
