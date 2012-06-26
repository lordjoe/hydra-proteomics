package org.systemsbiology.xtandem.fragmentation.ui;

import org.systemsbiology.xtandem.fragmentation.*;
import org.systemsbiology.xtandem.peptide.*;

import java.io.*;

/**
 * org.systemsbiology.xtandem.fragmentation.ui.CoverageFragment
 * User: Steve
 * Date: 6/25/12
 */
public class CoverageFragment extends SVGFragmentBuilder {
    public static final CoverageFragment[] EMPTY_ARRAY = {};

    public static final String TAG = "svg";


    public static final String[] COVERAGE_COLORS =
            {
                    "Red",
                    "Lime",
                    "Blue",
                    "Aqua",
                    "Fuchsia",
                    "Yellow",
                    "Salmon",
                    "Brown",
            };

    public static final int AMINO_ACID_WIDTH = 20;
    public static final int AMINO_ACID_HEIGHT = 20;
    public static final int RECTANGLE_HEIGHT = 10;

    public static final int MAX_COVERAGE_DEPTH = 5;

    public static final int AMINO_ACID_LINE_WIDTH = 60;


    public static final String CSS_TEXT =
            "  <style type='text/css'>\n" +
                    "    svg {\n" +
                    "    fill: none;\n" +
                    "    stroke: none;\n" +
                    "    }\n" +
                    "    svg text {\n" +
                    "        font-style: normal; \n" +
                    "        font-variant: normal; \n" +
                    "        font-weight: normal; \n" +
                    "        font-size: 18pt; \n" +
                    "        line-height: normal; \n" +
                    "        font-family: helvetica;\n" +
            //        "        stroke:black;\n" +
                    "        fill:black;\n" +
                    "        text-anchor: middle;\n" +
                    "        dy:.35em;\n" +
                    "    } \n" +
                    "    svg rect {\n" +
                    "        fill:red;\n" +
                    "        stroke: black;\n" +
                    "        stroke-width:.5px;\n" +
                    "    }\n" +
                    "  </style>\n";


    private final ProteinFragmentationDescription m_Fragments;
    private final ProteinFragmentLine[] m_Lines;

    public CoverageFragment(final HTMLPageBuillder page, ProteinFragmentationDescription fragments) {
        super(page,null,TAG);
        page.getHeader().addString(CSS_TEXT);
        m_Fragments = fragments;
        setWidth((AMINO_ACID_LINE_WIDTH + 1) * AMINO_ACID_WIDTH);
        Protein protein = fragments.getProtein();
        HTMLBodyBuillder body = page.getBody();
        body.addString("<h1>" + page.getTitle() + "</h1>\n");
        body.addString("<h3>" + protein.getAnnotation() + "</h3>\n");
        m_Lines = ProteinFragmentLine.generateLines(fragments);
        ProteinFragment[] fragments1 = fragments.getFragments();
        for (int i = 0; i < m_Lines.length; i++) {
            ProteinFragmentLine coverageFragment = m_Lines[i];
            AminoAcidTextLine aminoAcidTextLine = new AminoAcidTextLine(page,this, coverageFragment, i);
            addBuilder(aminoAcidTextLine);
            int displapedFragments = 0;
            for (int j = 0; j < fragments1.length; j++) {
                ProteinFragment test = fragments1[j];
                if(coverageFragment.containsFragent(test)) {
                    aminoAcidTextLine.addBuilder(new DetectedFragmentBuillder(page, this, test, coverageFragment, ++displapedFragments));
                 }
            }
        }
    }

    protected int getLineHeight()
    {
        int coverageDepth = 0;
          ProteinFragmentationDescription fragments = getFragments();
         short[] allCoverage = fragments.getAllCoverage();
        for (int i = 0; i < allCoverage.length; i++) {
            short i1 = allCoverage[i];
            coverageDepth = Math.max(coverageDepth,Math.min(i1,CoverageFragment.MAX_COVERAGE_DEPTH));
        }
        return CoverageFragment.AMINO_ACID_HEIGHT + coverageDepth * CoverageFragment.RECTANGLE_HEIGHT;
    }


    public int getNumberLines()
    {
        return getLines().length;
    }
    public ProteinFragmentationDescription getFragments() {
        return m_Fragments;
    }

    @Override
    public Integer getHeight() {
        if(super.getHeight() == null)
            setHeight(getLineHeight() * getNumberLines());
         return super.getHeight() ;
    }


    public ProteinFragmentLine[] getLines() {
        return m_Lines;
    }

    @Override
    protected String getTagAttributes() {
        StringBuilder sb = new StringBuilder(super.getTagAttributes());
        sb.append(" width =\"" + getWidth() + "\" height=\"" + getHeight() + "\"");
        return sb.toString();
      }






}
