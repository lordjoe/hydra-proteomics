package org.systemsbiology.xtandem.fragmentation.ui;

import java.io.*;

/**
 * org.systemsbiology.xtandem.fragmentation.ui.CoverageColorsLabel
 * User: steven
 * Date: 7/2/12
 */
public class CoverageColorsLabel extends SVGRootBuilder {
    public static final CoverageColorsLabel[] EMPTY_ARRAY = {};

    public CoverageColorsLabel(IHtmlFragmentHolder parent) {
        super(parent);
        setHeight(30);
        setWidth(500);
    }

    @Override
    protected String getTagAttributes() {
        StringBuilder sb = new StringBuilder(super.getTagAttributes());
           sb.append(" width=\"" + getWidth() + "\"");
          sb.append(" height=\"" + getHeight() + "\"");
          return sb.toString();
      }

    @Override
    protected final void appendAllBuilders(final Appendable out, final Object[] data) {
        super.appendAllBuilders(out, data);
        try {
            setWidth(500);
            setHeight(30);
            indent(out);
            String wrapRect = "<rect style=\"stroke:black;fill:white\" transform=\"translate(0,0)\" width=\"" +
                    getWidth() + "\" " +
                    "  height=\""
                    + getHeight() +
                    "\"></rect>\n";
            out.append(wrapRect);
            String labelText = "        <g transform=\"translate(2,2)\">\n" +
                    "            <text style=\"fill:Black;\" text-anchor=\"left\" transform=\"translate(0,20)\">Detected Coverage</text>\n" +
                    "        </g>\n";
              indent(out);
            out.append(labelText);
            indent(out);
                 out.append("<g transform=\"translate(300,4)\">\n");
            for (int i = 0; i < OneAminoAcidFragmentBuillder.COVERAGE_LEVEL_COLORS.length; i++) {
                String color =  OneAminoAcidFragmentBuillder.COVERAGE_LEVEL_COLORS[i];
                String coverageLabel =
                        "          <g transform=\"translate(" + (40 * i) + ",0)\">\n" +
                                "                <rect style=\"fill:" + color + ";\" transform=\"translate(0,0)\" width=\"20\" height=\"20\"></rect>\n" +
                                "                <text style=\"fill:Black;\" text-anchor=\"middle\" transform=\"translate(30,20)\">" + i + "</text>\n" +
                                "            </g>\n";
                 out.append(coverageLabel);
            }
            indent(out);
             out.append("        </g>\n");
    //          out.append("       </rect>\n");
           }
        catch (IOException e) {
            throw new RuntimeException(e);

        }

    }

}
