package org.systemsbiology.xtandem.fragmentation.ui;

import org.systemsbiology.jmol.*;
import org.systemsbiology.xtandem.*;
import org.systemsbiology.xtandem.fragmentation.*;
import org.systemsbiology.xtandem.peptide.*;
import org.w3c.css.sac.*;
import sun.awt.*;

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
  //                  "Purple",
 //                   "Green",
  //                  "Red",
            };

    public static final int SECONDARY_STRUCTURE_NOT_MODELED = 0;
    public static final int SECONDARY_STRUCTURE_MODELED = 1;
    public static final int SECONDARY_STRUCTURE_HELIX = 2;
    public static final int SECONDARY_STRUCTURE_SHEET = 3;
    public static final int SECONDARY_STRUCTURE_DISULPHIDE = 4;
    public static final int SECONDARY_STRUCTURE_MISSED_CLEAVAGE = 5;
    public static final int SECONDARY_STRUCTURE_TURN = 6;

    public static final String[] SECONDARY_STRUCTURE_COLORS =
            {
                    "#ffe0e0",  // SECONDARY_STRUCTURE_NOT_MODELED
                    "white",  //  SECONDARY_STRUCTURE_MODELED
                    "#d0d0ff",  //    SECONDARY_STRUCTURE_HELIX
                    "#d0ffff",  //  SECONDARY_STRUCTURE_SHEET
                    "#d0ffd0",   //SECONDARY_STRUCTURE_DISULPHIDE
                    "Lime",   //SECONDARY_STRUCTURE_MISSED_CLEAVAGE
                    "Orange",   //SECONDARY_STRUCTURE_TURN
            };


    public static final String[] SECONDARY_STRUCTURE_TEXT =
            {
                    "Not Modeled",  // SECONDARY_STRUCTURE_NOT_MODELED
                    "Modeled",  //  SECONDARY_STRUCTURE_MODELED
                    "Helix",  //    SECONDARY_STRUCTURE_HELIX
                    "Sheet",  //  SECONDARY_STRUCTURE_SHEET
                    "DiSulphide",   //SECONDARY_STRUCTURE_DISULPHIDE
                    "Missed Cleavage",   //SECONDARY_STRUCTURE_MISSED_CLEAVAGE
                    "Turn",   //SECONDARY_STRUCTURE_MISSED_CLEAVAGE
             };


    public static final String TAG = "text";

    private final String m_AminoAcid;
//    private final int m_Coverage;
    private final SequenceChainMap m_Mapping;
    private final IAminoAcidAtLocation  m_AAMapping;
//    private final boolean  m_MisssedCleavage;

    public OneAminoAcidFragmentBuillder(ProteinLineBuillder parent, int xpos, String aminoAcid, int coverage,
                                        SequenceChainMap mapping,boolean  misssedCleavage) {

        super(parent, TAG);
        m_AminoAcid = new String(aminoAcid);
//        m_Coverage = coverage;
        m_Mapping = mapping;
//        m_MisssedCleavage = misssedCleavage;
        if (mapping != null) {
            IAminoAcidAtLocation[] chainMappings = mapping.getChainMappings();
            if (chainMappings.length >  0) {
                m_AAMapping = chainMappings[0];
                 if(m_AAMapping instanceof ProteinAminoAcid)
                     ((ProteinAminoAcid)m_AAMapping).setDetected(coverage > 0);
            }
            else {
                m_AAMapping = null;
            }
        }
        else {
            m_AAMapping = null;
            throw new UnsupportedOperationException("Fix This"); // ToDo
        }
        setX(xpos);
        CoverageFragment cf = getParentOfType(CoverageFragment.class);
        setY(cf.getLineHeight());
        //     new OneAminoAcidTextBuillder(this,0,   aminoAcid,   coverage, mapping);
    }

    public String getAminoAcid() {
        IAminoAcidAtLocation aaMapping = getAAMapping();
        if(aaMapping == null)
            return m_AminoAcid;
        FastaAminoAcid aminoAcid = aaMapping.getAminoAcid();
        return aminoAcid.toString();
    }

    public int getCoverage() {
        IAminoAcidAtLocation aaMapping = getAAMapping();
        if(aaMapping == null)
            return 0;
        if(aaMapping.isDetected())
           return 1;
       else
           return 0;
    }

    public SequenceChainMap getMapping() {
        return m_Mapping;
    }

    public IAminoAcidAtLocation getAAMapping() {
        return m_AAMapping;
    }

    protected String getFillColor() {
        IAminoAcidAtLocation aaMapping = getAAMapping();
        if (aaMapping == null)
            return SECONDARY_STRUCTURE_COLORS[SECONDARY_STRUCTURE_NOT_MODELED];
        if (aaMapping.isPotentialCleavage() && aaMapping.isSometimesMissedCleavage())
               return SECONDARY_STRUCTURE_COLORS[SECONDARY_STRUCTURE_MISSED_CLEAVAGE];
        if (aaMapping.isDiSulphideBond())
              return SECONDARY_STRUCTURE_COLORS[SECONDARY_STRUCTURE_DISULPHIDE];
            UniprotFeatureType structure = aaMapping.getStructure();
        if (structure != null) {
            switch (structure) {
                case HELIX:
                    return SECONDARY_STRUCTURE_COLORS[SECONDARY_STRUCTURE_HELIX];
                case STRAND:
                    return SECONDARY_STRUCTURE_COLORS[SECONDARY_STRUCTURE_SHEET];
                case TURN:
                     return SECONDARY_STRUCTURE_COLORS[SECONDARY_STRUCTURE_TURN];

            }
        }
        return SECONDARY_STRUCTURE_COLORS[SECONDARY_STRUCTURE_MODELED];
    }

    public static final Offset RECT_OFFSET = new Offset(-10, -18);

    @Override
    public void addStartText(Appendable out, Object... data) {
        try {
            indent(out);
            IAminoAcidAtLocation aaMapping = getAAMapping();
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


        String fillColor = getFillColor();
        if(fillColor.contains(SECONDARY_STRUCTURE_COLORS[SECONDARY_STRUCTURE_DISULPHIDE]))
            fillColor = getFillColor(); // break here
        String coverageColor = getCoverageColor(textCoverage);
        sb.append(" style=\"fill:" + coverageColor +
            //    ";background-color:" + fillColor +
                ";\" ");
        sb.append(" text-anchor=\"middle\" ");
        sb.append(getTransformText());
        //     sb.append(" transform=\"translate(" + getX() + "," + getHeight() + ")\" ");
        return sb.toString();

    }

    @Override
    protected void appendAllBuilders(final Appendable out, final Object[] data) {
        try {
            String aminoAcid = getAminoAcid();
            if(aminoAcid != null)
                out.append(aminoAcid);
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
