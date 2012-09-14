package org.systemsbiology.xtandem.fragmentation.ui;

import java.io.*;

/**
 * org.systemsbiology.xtandem.fragmentation.ui.CoverageColorsLabel
 * User: steven
 * Date: 7/2/12
 */
public class HelixandTurnLabel extends SVGRootBuilder {
    public static final HelixandTurnLabel[] EMPTY_ARRAY = {};
    public static final int DEFAULT_WIDTH = 1000;
    public static final int DEFAULT_HEIGHT = 30;

    public HelixandTurnLabel(IHtmlFragmentHolder parent) {
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
                    "            <text style=\"fill:Black;\" text-anchor=\"left\" transform=\"translate(0,20)\">Helix</text>\n" +
                    "                <rect style=\"fill:" + OneAminoAcidFragmentBuillder.SECONDARY_STRUCTURE_COLORS[OneAminoAcidFragmentBuillder.SECONDARY_STRUCTURE_HELIX] +
                       ";;stroke-width:3;stroke:black;\" transform=\"translate(80,2)\" width=\"20\" height=\"20\"></rect>\n" +
                       "        </g>\n";
              indent(out);
            out.append(labelText);
            indent(out);
       //     out.append("<g transform=\"translate(" + xtrans + ",4)\">\n");

  //          String color = OneAminoAcidFragmentBuillder.SECONDARY_STRUCTURE_COLORS[OneAminoAcidFragmentBuillder.SECONDARY_STRUCTURE_HELIX];
//            String coverageLabel =
//                    "          <g transform=\"translate(" + (40) + ",0)\">\n" +
//                            "                <rect style=\"fill:" + color + ";\" transform=\"translate(0,0)\" width=\"20\" height=\"20\"></rect>\n" +
//                            "                <text style=\"fill:Black;\" text-anchor=\"middle\" transform=\"translate(30,20)\">" +
//                               "</text>\n" +
//                            " </g>\n";
//            out.append(coverageLabel);

            indent(out);
            int xtrans  = 160;
            labelText = "        <g transform=\"translate( " + xtrans + ",2)\">\n" +
                    "            <text style=\"fill:Black;\" text-anchor=\"left\" transform=\"translate(0,20)\">Sheet</text>\n" +
                    "                <rect style=\"fill:" + OneAminoAcidFragmentBuillder.SECONDARY_STRUCTURE_COLORS[OneAminoAcidFragmentBuillder.SECONDARY_STRUCTURE_SHEET] +
                    ";;stroke-width:3;stroke:black;\" transform=\"translate(80,2)\" width=\"20\" height=\"20\"></rect>\n" +
                    "        </g>\n";
            out.append(labelText);
            xtrans += 160;
            labelText = "        <g transform=\"translate( " + xtrans + ",2)\">\n" +
                    "            <text style=\"fill:Black;\" text-anchor=\"left\" transform=\"translate(0,20)\">Turn</text>\n" +
                    "                <rect style=\"fill:" + OneAminoAcidFragmentBuillder.SECONDARY_STRUCTURE_COLORS[OneAminoAcidFragmentBuillder.SECONDARY_STRUCTURE_TURN] +
                    ";;stroke-width:3;stroke:black;\" transform=\"translate(80,2)\" width=\"20\" height=\"20\"></rect>\n" +
                    "        </g>\n";
            indent(out);
            out.append(labelText);
            indent(out);
            out.append("        </g>\n");

            //          out.append("       </rect>\n");
        }
        catch (IOException e) {
            throw new RuntimeException(e);

        }

    }

}
