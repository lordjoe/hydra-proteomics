package org.systemsbiology.xtandem.fragmentation.ui;

import java.io.*;

/**
 * org.systemsbiology.xtandem.fragmentation.ui.CoverageColorsLabel
 * User: steven
 * Date: 7/2/12
 */
public class CoverageColorsLabel extends SVGRootBuilder {
    public static final CoverageColorsLabel[] EMPTY_ARRAY = {};
    public static final int DEFAULT_WIDTH = 400;
     public static final int DEFAULT_HEIGHT = 30;

    public CoverageColorsLabel(IHtmlFragmentHolder parent) {
        super(parent);
        setHeight(DEFAULT_HEIGHT);
        setWidth(DEFAULT_WIDTH);
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
            setWidth(DEFAULT_WIDTH);
            setHeight(DEFAULT_HEIGHT);
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
            int numberColors = OneAminoAcidFragmentBuillder.COVERAGE_LEVEL_COLORS.length;
            int xtrans = 250;
            indent(out);
                 out.append("<g transform=\"translate(" + xtrans + ",4)\">\n");

                for (int i = 0; i < numberColors; i++) {
                String color =  OneAminoAcidFragmentBuillder.COVERAGE_LEVEL_COLORS[i];
                String coverageLabel =
                        "          <g transform=\"translate(" + (  50 * i) + ",0)\">\n" +
                                "                <rect style=\"fill:" + color + ";\" transform=\"translate(0,0)\" width=\"20\" height=\"20\"></rect>\n" +
                                "                <text style=\"fill:Black;\" text-anchor=\"middle\" transform=\"translate(30,20)\">" +
                                                       i + "</text>\n" +
                                "            </g>\n";
                 out.append(coverageLabel);
            }
//            out.append("        </g>\n");
//            indent(out);
//             xtrans +=  40 + 40 * numberColors;
//            labelText = "        <g transform=\"translate( " + xtrans +  ",2)\">\n" +
//                     "            <text style=\"fill:Black;\" text-anchor=\"left\" transform=\"translate(0,20)\">Missed Cleavages</text>\n" +
//                     "                <rect style=\"fill:" +  OneAminoAcidFragmentBuillder.SECONDARY_STRUCTURE_COLORS[OneAminoAcidFragmentBuillder.SECONDARY_STRUCTURE_MISSED_CLEAVAGE] +
//                    ";;stroke-width:3;stroke:black;\" transform=\"translate(210,2)\" width=\"20\" height=\"20\"></rect>\n" +
//                       "        </g>\n";
//            out.append(labelText);
//            xtrans  += 250;
//            labelText = "        <g transform=\"translate( " + xtrans +  ",2)\">\n" +
//                     "            <text style=\"fill:Black;\" text-anchor=\"left\" transform=\"translate(0,20)\">DiSulphide</text>\n" +
//                     "                <rect style=\"fill:" +  OneAminoAcidFragmentBuillder.SECONDARY_STRUCTURE_COLORS[OneAminoAcidFragmentBuillder.SECONDARY_STRUCTURE_DISULPHIDE] +
//                    ";;stroke-width:3;stroke:black;\" transform=\"translate(160,2)\" width=\"20\" height=\"20\"></rect>\n" +
//                       "        </g>\n";
//               indent(out);
//             out.append(labelText);
//            indent(out);
//            out.append("        </g>\n");

      //          out.append("       </rect>\n");
           }
        catch (IOException e) {
            throw new RuntimeException(e);

        }

    }

}
