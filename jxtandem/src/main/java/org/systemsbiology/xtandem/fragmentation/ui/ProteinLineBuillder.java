package org.systemsbiology.xtandem.fragmentation.ui;

import org.systemsbiology.xtandem.fragmentation.*;

import java.io.*;
import java.util.*;

/**
 * org.systemsbiology.xtandem.fragmentation.ui.HTMLPageBuillder
 * User: Steve
 * Date: 6/25/12
 */
public class ProteinLineBuillder extends AbstractHtmlFragmentHolder {
    public static final ProteinLineBuillder[] EMPTY_ARRAY = {};

    public static final String[] COVERAGE_LEVEL_COLORS =
              {
                      "Black",
                      "Cyan",
                      "Blue",
                       "Green",
                      "Red",
                };



    private final ProteinFragmentLine m_Line;

    public ProteinLineBuillder(final HTMLPageBuillder page, final ProteinFragmentLine line) {
        super(page);
        m_Line = line;
    }

    public ProteinFragmentLine getLine() {
        return m_Line;
    }

    @Override
    public void addStartText(final Appendable out, final Object... data) {
        try {
            indent(out, 3);
            out.append("<g id=\"" + getUniqueId() + "\" >\n");
        }
        catch (IOException e) {
            throw new RuntimeException(e);
       }
    }

    protected String getCoverageColor(int index)
    {
        if(index <= 0 )
            return COVERAGE_LEVEL_COLORS[0];
        if(index >= COVERAGE_LEVEL_COLORS.length)
            return COVERAGE_LEVEL_COLORS[COVERAGE_LEVEL_COLORS.length - 1];
        return COVERAGE_LEVEL_COLORS[index];
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


     ProteinFragment[] getDetectedFragments() {
         ProteinFragmentLine line = getLine();
         int start = line.getStart();
         int end = line.getEnd();
         ProteinFragmentationDescription pfd = line.getFragments();
         ProteinFragment[] fragments = pfd.getFragments();
         List<ProteinFragment> holder = new ArrayList<ProteinFragment>();
         for (int i = 0; i < fragments.length; i++) {
             ProteinFragment fragment = fragments[i];
             int fStart = fragment.getStartLocation();
             int fEnd = fragment.getSequence().length();
             if(fStart > end)
                 continue;
             if(fEnd < start)
                 continue;
             holder.add(fragment);
         }
         ProteinFragment[] ret = new ProteinFragment[holder.size()];
         holder.toArray(ret);
         return ret;

     }

    @Override
    protected void appendAllBuilders(final Appendable out, final Object[] data) {
        try {

            ProteinFragmentLine line = getLine();
            int start = line.getStart();
            ProteinFragmentationDescription fragments = line.getFragments();
            short[] allCoverage = fragments.getAllCoverage();
            int height = getLineHeight();
            String sequence = line.getSequence();
            super.appendAllBuilders(out, data);
            for (int i = 0; i < sequence.length(); i++) {
                char c = sequence.charAt(i);
                indent(out,4);
                out.append("<text id=\"" + getUniqueId() +
                        "\" style=\"fill:" +getCoverageColor(allCoverage[start + i]) +   ";\" " +
                        " transform=\"translate(" + (CoverageFragment.AMINO_ACID_WIDTH * (i + 1)) + "," + height + ")" +
                        "\" >");
                //  <text id="1" transform="translate(20,20)">A</text>
                out.append(c);
                out.append("</text>");
                out.append("\n");
            }
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
