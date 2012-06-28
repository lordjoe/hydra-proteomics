package org.systemsbiology.xtandem.fragmentation.ui;

import org.systemsbiology.xtandem.*;
import org.systemsbiology.xtandem.fragmentation.*;

import java.io.*;

/**
 * org.systemsbiology.xtandem.fragmentation.ui.AminoAcidTextLine
 * User: Steve
 * Date: 6/25/12
 */
public class AminoAcidTextLine extends SVGFragmentBuilder {
    public static final AminoAcidTextLine[] EMPTY_ARRAY = {};


    public static final String TAG = "g";

     private final ProteinFragmentLine m_LineFragment;

    public AminoAcidTextLine( SVGFragmentBuilder parent, ProteinFragmentLine coverageFragment,int index) {
        super( parent, TAG);
        setIndex(index);
        m_LineFragment = coverageFragment;
         new ProteinLineBuillder( this,m_LineFragment) ;
     }


    public ProteinFragmentLine getLineFragment() {
        return m_LineFragment;
    }



    protected int getLineHeight()
    {
         return ((CoverageFragment)getParent()).getLineHeight();
//        int coverageDepth = 0;
//          ProteinFragmentationDescription fragments = getLineFragment().getFragments();
//         short[] allCoverage = fragments.getAllCoverage();
//        for (int i = 0; i < allCoverage.length; i++) {
//            short i1 = allCoverage[i];
//            coverageDepth = Math.max(coverageDepth,Math.min(i1,CoverageFragment.MAX_COVERAGE_DEPTH));
//        }
//        return CoverageFragment.AMINO_ACID_HEIGHT + ( 1 + coverageDepth)  * ( 2 + CoverageFragment.RECTANGLE_HEIGHT);
    }

    @Override
    protected String getTagAttributes() {
        StringBuilder sb = new StringBuilder(super.getTagAttributes());
        sb.append("  transform=\"translate(0," +
                    ( getLineHeight() * getIndex()) + ")\" ");
        return sb.toString();
      }

//    @Override
//    public void addStartText(final Appendable out, final Object... data) {
//        try {
//            indent(out,2);
//            out.append("<g id=\"" + getUniqueId() + "\"  transform=\"translate(0," +
//                    ( getLineHeight() * getIndex()) + ")\" >\n");
//        }
//        catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//
//    }
//
//    @Override
//    public void addEndText(final Appendable out, final Object... data) {
//        try {
//            indent(out,2);
//            out.append("</g>\n");
//        }
//        catch (IOException e) {
//            throw new RuntimeException(e);
//
//        }
//    }

}
