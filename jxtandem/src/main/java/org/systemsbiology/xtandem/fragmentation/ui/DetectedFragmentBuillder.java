package org.systemsbiology.xtandem.fragmentation.ui;

import org.systemsbiology.xtandem.fragmentation.*;

import java.io.*;

/**
 * org.systemsbiology.xtandem.fragmentation.ui.HTMLPageBuillder
 * User: Steve
 * Date: 6/25/12
 */
public class DetectedFragmentBuillder extends SVGFragmentBuilder {
    public static final DetectedFragmentBuillder[] EMPTY_ARRAY = {};

    public static final String TAG = "g";

    private final ProteinFragmentLine m_Line;
    private final ProteinFragment m_Fragment;

    public DetectedFragmentBuillder( SVGFragmentBuilder parent, ProteinFragment frag, final ProteinFragmentLine line, final int index) {
        super( parent, TAG);
         m_Line = line;
        m_Fragment = frag;
        setIndex(index);
          int lineStart = frag.getStartLocation();
        int fragmentLineStart = line.getStart();
        int startAA = Math.max(lineStart, fragmentLineStart);

        int lineAminoAcid = startAA - fragmentLineStart; //  - lineStart;
        String sequence = frag.getSequence();
//        if(sequence.startsWith("GCHESC"))
//               sequence = frag.getSequence(); // break here

        int fragmentLength = sequence.length();
        int lineLength = fragmentLength;
        if (fragmentLineStart > lineStart)
            lineLength -= (fragmentLineStart - lineStart);
        lineLength = Math.min(CoverageFragment.AMINO_ACID_LINE_WIDTH - lineAminoAcid + 1, lineLength);
          String coverageColor = CoverageFragment.COVERAGE_COLORS[getIndex() % CoverageFragment.COVERAGE_COLORS.length];
        RectFragmentBuillder rf = new RectFragmentBuillder( this,coverageColor);
        rf.setWidth(lineLength * CoverageFragment.AMINO_ACID_WIDTH);
        rf.setHeight(CoverageFragment.RECTANGLE_HEIGHT);
        rf.setX(lineAminoAcid * CoverageFragment.AMINO_ACID_WIDTH + (CoverageFragment.AMINO_ACID_WIDTH / 2));

        int displayIndex = CoverageFragment.MAX_COVERAGE_DEPTH  - (index % CoverageFragment.MAX_COVERAGE_DEPTH) - 1;
        if(displayIndex >= 4)
               displayIndex = 3; //CoverageFragment.MAX_COVERAGE_DEPTH  - (index % CoverageFragment.MAX_COVERAGE_DEPTH) - 1; // break here
        rf.setY( displayIndex * CoverageFragment.RECTANGLE_HEIGHT);
        rf.setPopupText(sequence);
     }


    public ProteinFragmentLine getLine() {
        return m_Line;
    }


}
