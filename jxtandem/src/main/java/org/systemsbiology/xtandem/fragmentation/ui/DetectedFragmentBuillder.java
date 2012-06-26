package org.systemsbiology.xtandem.fragmentation.ui;

import org.systemsbiology.xtandem.fragmentation.*;

import java.io.*;
import java.util.*;

/**
 * org.systemsbiology.xtandem.fragmentation.ui.HTMLPageBuillder
 * User: Steve
 * Date: 6/25/12
 */
public class DetectedFragmentBuillder extends AbstractHtmlFragmentHolder {
    public static final DetectedFragmentBuillder[] EMPTY_ARRAY = {};




    private final ProteinFragmentLine m_Line;
    private final ProteinFragment m_Fragment;
    private final  int m_Index;
    private final int m_Top;
    private final int m_Left;
     private final  int m_Width;
    private final int m_Height = CoverageFragment.RECTANGLE_HEIGHT;

    public DetectedFragmentBuillder(final HTMLPageBuillder page,ProteinFragment frag, final ProteinFragmentLine line, final int index ) {
        super(page);
        m_Line = line;
        m_Fragment = frag;
        m_Index = index;
        m_Top =  (index % CoverageFragment.MAX_COVERAGE_DEPTH) * CoverageFragment.RECTANGLE_HEIGHT ;
        int lineStart = frag.getStartLocation();
        int fragmentLineStart = line.getStart();
        int startAA = Math.max(lineStart, fragmentLineStart);

        int lineAminoAcid = startAA - fragmentLineStart; //  - lineStart;
        String sequence = frag.getSequence();
//        if(sequence.startsWith("GCHESC"))
//               sequence = frag.getSequence(); // break here

        int fragmentLength = sequence.length();
        int lineLength =   fragmentLength;
        if(fragmentLineStart > lineStart)
            lineLength -= (fragmentLineStart - lineStart);
         lineLength = Math.min(CoverageFragment.AMINO_ACID_LINE_WIDTH - lineAminoAcid + 1 , lineLength);
        m_Left = lineAminoAcid *  CoverageFragment.AMINO_ACID_WIDTH + (CoverageFragment.AMINO_ACID_WIDTH / 2);
        m_Width = lineLength  *  CoverageFragment.AMINO_ACID_WIDTH;
    }

    public int getTop() {
        return m_Top;
    }

    public int getLeft() {
        return m_Left;
    }

    public int getWidth() {
        return m_Width;
    }

    public int getHeight() {
        return m_Height;
    }

    public ProteinFragmentLine getLine() {
        return m_Line;
    }

    public int getIndex() {
        return m_Index;
    }

    @Override
    public void addStartText(final Appendable out, final Object... data) {
        try {
            indent(out, 3);
            out.append("<g id=\"" + getUniqueId() +
            //        "  transform=\"translate(0," +   getTop() +  ")"   +
                    "\" >\n");
        }
        catch (IOException e) {
            throw new RuntimeException(e);
       }
    }


    protected int getLineHeight()
    {
        int coverageDepth = 0;
        ProteinFragmentLine line = getLine();
        ProteinFragmentationDescription fragments = line.getFragments();
         short[] allCoverage = fragments.getAllCoverage();
        for (int i = 0; i < allCoverage.length; i++) {
            short i1 = allCoverage[i];
            coverageDepth = Math.max(coverageDepth, Math.min(i1, CoverageFragment.MAX_COVERAGE_DEPTH));
        }
        return CoverageFragment.AMINO_ACID_HEIGHT + coverageDepth * CoverageFragment.RECTANGLE_HEIGHT;
    }


    @Override
    protected void appendAllBuilders(final Appendable out, final Object[] data) {
        try {
            indent(out,5);
            out.append(" <rect transform=\"translate(" + getLeft() + "," +   getTop() +  ")\" " +
                     " width=\"" + getWidth() + "\" height=\"" + getHeight() +
                    "\" style=\"fill:" + CoverageFragment.COVERAGE_COLORS[getIndex() % CoverageFragment.COVERAGE_COLORS.length]  +   ";\" " +

                    " </rect>\n");
          }
        catch (IOException e) {
            throw new RuntimeException(e);

        }
    }


    @Override
    public void addEndText(final Appendable out, final Object... data) {
        try {
            indent(out, 3);
            out.append("</g>");
            out.append("\n");
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


}
