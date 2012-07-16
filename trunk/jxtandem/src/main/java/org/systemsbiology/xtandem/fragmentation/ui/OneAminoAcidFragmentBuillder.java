package org.systemsbiology.xtandem.fragmentation.ui;

import org.systemsbiology.jmol.*;

import java.io.*;

/**
 * org.systemsbiology.xtandem.fragmentation.ui.HTMLPageBuillder
 * User: Steve
 * Date: 6/25/12
 */
public class OneAminoAcidFragmentBuillder extends SVGFragmentBuilder {
    public static final OneAminoAcidFragmentBuillder[] EMPTY_ARRAY = {};
    public static final String[] COVERAGE_LEVEL_COLORS =
            {
                    "Black",
                    "Blue",
                    "Purple",
                    "Green",
                    "Red",
            };

    public static final int SECONDARY_STRUCTURE_NOT_MODELED = 0;
    public static final int SECONDARY_STRUCTURE_MODELED = 1;
    public static final int SECONDARY_STRUCTURE_HELIX = 2;
    public static final int SECONDARY_STRUCTURE_SHEET = 3;
    public static final int SECONDARY_STRUCTURE_DISULPHIDE = 4;
    public static final int SECONDARY_STRUCTURE_MISSED_CLEAVAGE = 5;

    public static final String[] SECONDARY_STRUCTURE_COLORS =
            {
                    "#d0d0d0",  // SECONDARY_STRUCTURE_NOT_MODELED
                    "#ffe0e0",  //  SECONDARY_STRUCTURE_MODELED
                    "#d0d0ff",  //    SECONDARY_STRUCTURE_HELIX
                    "#d0ffff",  //  SECONDARY_STRUCTURE_SHEET
                    "#d0ffd0",   //SECONDARY_STRUCTURE_DISULPHIDE
                    "Lime",   //SECONDARY_STRUCTURE_MISSED_CLEAVAGE
            };


    public static final String[] SECONDARY_STRUCTURE_TEXT =
            {
                    "Not Modeled",  // SECONDARY_STRUCTURE_NOT_MODELED
                    "Modeled",  //  SECONDARY_STRUCTURE_MODELED
                    "Helix",  //    SECONDARY_STRUCTURE_HELIX
                    "Sheet",  //  SECONDARY_STRUCTURE_SHEET
                    "DiSulphide",   //SECONDARY_STRUCTURE_DISULPHIDE
                    "Missed Cleavage",   //SECONDARY_STRUCTURE_MISSED_CLEAVAGE
            };


    public static final String TAG = "text";

    private final String m_AminoAcid;
    private final int m_Coverage;
    private final SequenceChainMap m_Mapping;
    private final AminoAcidAtLocation m_AAMapping;

    public OneAminoAcidFragmentBuillder(ProteinLineBuillder parent, int xpos, String aminoAcid, int coverage,
                                        SequenceChainMap mapping) {

        super(parent, TAG);
        m_AminoAcid = new String(aminoAcid);
        m_Coverage = coverage;
        m_Mapping = mapping;
        if (mapping != null) {
            AminoAcidAtLocation[] chainMappings = mapping.getChainMappings();
            if (chainMappings.length > 0) {
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
        //     new OneAminoAcidTextBuillder(this,0,   aminoAcid,   coverage, mapping);
    }

    public String getAminoAcid() {
        return m_AminoAcid;
    }

    public int getCoverage() {
        return m_Coverage;
    }

    public SequenceChainMap getMapping() {
        return m_Mapping;
    }

    public AminoAcidAtLocation getAAMapping() {
        return m_AAMapping;
    }

    protected String getFillColor() {
        AminoAcidAtLocation aaMapping = getAAMapping();
        if (aaMapping == null)
            return SECONDARY_STRUCTURE_COLORS[SECONDARY_STRUCTURE_NOT_MODELED];
        if (aaMapping.isDiSulphideBond())
            return SECONDARY_STRUCTURE_COLORS[SECONDARY_STRUCTURE_DISULPHIDE];
        SecondaryStructure structure = aaMapping.getStructure();
        if (structure != null) {
            switch (structure) {
                case HELIX:
                    return SECONDARY_STRUCTURE_COLORS[SECONDARY_STRUCTURE_HELIX];
                case SHEET:
                    return SECONDARY_STRUCTURE_COLORS[SECONDARY_STRUCTURE_SHEET];

            }
        }
        return SECONDARY_STRUCTURE_COLORS[SECONDARY_STRUCTURE_MODELED];
    }

    public static final Offset RECT_OFFSET = new Offset(-10, -18);

    @Override
    public void addStartText(Appendable out, Object... data) {
        try {
            indent(out);
            out.append("<rect ");
            out.append(" style=\"fill:" + getFillColor() + ";\" ");
            out.append("width=\"" + CoverageFragment.AMINO_ACID_WIDTH + "\" height=\"" + CoverageFragment.AMINO_ACID_HEIGHT + "\" ");
            Offset offset = getOffset();
            offset = offset.add(RECT_OFFSET);
            out.append(" transform=\"translate(" + offset.toString() + ")\" ");
            out.append(getTransformText());
            out.append(" ");
            out.append("/>\n");
        }
        catch (IOException e) {
            throw new RuntimeException(e);

        }

        super.addStartText(out, data);
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


        sb.append(" style=\"fill:" + getCoverageColor(textCoverage) + ";background-color:" + getFillColor() + ";\" ");
        sb.append(" text-anchor=\"middle\" ");
        sb.append(getTransformText());
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
    protected boolean isTagOnSeparateLine() {
        return false;
    }
}
