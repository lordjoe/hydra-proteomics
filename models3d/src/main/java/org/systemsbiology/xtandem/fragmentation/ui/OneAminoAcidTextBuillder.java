package org.systemsbiology.xtandem.fragmentation.ui;

import org.systemsbiology.jmol.*;

import java.io.*;

/**
 * org.systemsbiology.xtandem.fragmentation.ui.HTMLPageBuillder
 * User: Steve
 * Date: 6/25/12
 */
public class OneAminoAcidTextBuillder extends SVGFragmentBuilder {
    public static final OneAminoAcidTextBuillder[] EMPTY_ARRAY = {};
    public static final String[] COVERAGE_LEVEL_COLORS =
            {
                    "Black",
                    "Cyan",
                    "Blue",
                    "Green",
                    "Red",
            };


    public static final String TAG = "text";

    private final String m_AminoAcid;
    private final int m_Coverage;
    private final SequenceChainMap m_Mapping;
    private final IAminoAcidAtLocation m_AAMapping;

    public OneAminoAcidTextBuillder(OneAminoAcidFragmentBuillder parent, int xpos, String aminoAcid, int coverage,
                                    SequenceChainMap mapping) {

        super(  parent, TAG);
        m_AminoAcid = new String(aminoAcid);
        m_Coverage = coverage;
        m_Mapping = mapping;
        if(mapping != null) {
            IAminoAcidAtLocation[] chainMappings = mapping.getChainMappings();
            if(chainMappings.length > 0)   {
                 m_AAMapping = chainMappings[0];
            }
            else {
                m_AAMapping = null;
             }
         }
        else {
           m_AAMapping = null;
        }
        setX(xpos);
        CoverageFragment cf = getParentOfType(CoverageFragment.class);
        setY(cf.getLineHeight());
    }

    public String getAminoAcid() {
        return m_AminoAcid;
    }

    public int getCoverage() {
        return m_Coverage;
    }


    protected String getCoverageColor(int index) {
        if (index <= 0)
            return COVERAGE_LEVEL_COLORS[0];
        if (index >= COVERAGE_LEVEL_COLORS.length)
            return COVERAGE_LEVEL_COLORS[COVERAGE_LEVEL_COLORS.length - 1];
        return COVERAGE_LEVEL_COLORS[index];
    }

    @Override
    protected String getTagAttributes() {
        StringBuilder sb = new StringBuilder(super.getTagAttributes());
        int textCoverage = getCoverage();

        sb.append(" style=\"fill:" + getCoverageColor(textCoverage) + ";\" ");
             sb.append(" text-anchor=\"middle\" "  );
 //        sb.append(getTransformText());
    //     sb.append(" transform=\"translate(" + getX() + "," + getHeight() + ")\" ");
        return sb.toString();

    }

    @Override
    protected void appendAllBuilders(final Appendable out, final Object[] data) {
        try {
            out.append(getAminoAcid());
        }
        catch (IOException e) {
            throw new RuntimeException(e);

        }
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
