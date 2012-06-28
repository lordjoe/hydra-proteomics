package org.systemsbiology.xtandem.fragmentation.ui;

import org.systemsbiology.xtandem.fragmentation.*;

import java.io.*;

/**
 * org.systemsbiology.xtandem.fragmentation.ui.HTMLPageBuillder
 * User: Steve
 * Date: 6/25/12
 */
public class ProteinLineBuillder extends SVGFragmentBuilder {
    public static final ProteinLineBuillder[] EMPTY_ARRAY = {};

    public static final String TAG = "g";

    private final ProteinFragmentLine m_Line;
    private Integer m_LineHeight;

    public ProteinLineBuillder(  SVGFragmentBuilder parent, final ProteinFragmentLine line) {
        super(  parent, TAG);
        m_Line = line;
         buildAminoAcidBuilders();
    }

    protected void buildAminoAcidBuilders() {
        ProteinFragmentLine line = getLine();
        int start = line.getStart();
        ProteinFragmentationDescription fragments = line.getFragments();
        short[] allCoverage = fragments.getAllCoverage();
    //    int height = getLineHeight();
        String sequence = line.getSequence();
        for (int i = 0; i < sequence.length(); i++) {
            String c = sequence.substring(i, i + 1);
            int xPosition = CoverageFragment.AMINO_ACID_WIDTH * (i + 1);
            short textCoverage = allCoverage[start + i];
            OneAminoAcidFragmentBuillder aa = new OneAminoAcidFragmentBuillder(  this, xPosition, c, textCoverage);
          }

    }

    public ProteinFragmentLine getLine() {
        return m_Line;
    }

//
//    public int getLineHeight() {
//         if(m_LineHeight == null)
//             m_LineHeight = buildLineHeight();
//        return m_LineHeight;
//    }
//
//
//    public int buildLineHeight() {
//        int coverageDepth = 0;
//        ProteinFragmentLine line = getLine();
//        ProteinFragmentationDescription fragments = line.getFragments();
//        short[] allCoverage = fragments.getAllCoverage();
//        for (int i = 0; i < allCoverage.length; i++) {
//            short i1 = allCoverage[i];
//            coverageDepth = Math.max(coverageDepth, Math.min(i1, CoverageFragment.MAX_COVERAGE_DEPTH));
//        }
//        return CoverageFragment.AMINO_ACID_HEIGHT + coverageDepth * CoverageFragment.RECTANGLE_HEIGHT;
//    }
//

}
