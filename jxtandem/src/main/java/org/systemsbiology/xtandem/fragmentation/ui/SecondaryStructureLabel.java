package org.systemsbiology.xtandem.fragmentation.ui;

import java.io.*;

/**
 * org.systemsbiology.xtandem.fragmentation.ui.SecondaryStructureLabel
 * User: steven
 * Date: 7/2/12
 */
public class SecondaryStructureLabel extends SVGRootBuilder {
    public static final SecondaryStructureLabel[] EMPTY_ARRAY = {};
    public static final int DEFAULT_WIDTH = 1100;
    public static final int DEFAULT_HEIGHT = 30;

    public static final int[] ITEM_WIDTH = { 160, 120, 80, 100, 140, 100 };


    public SecondaryStructureLabel(IHtmlFragmentHolder parent) {
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
            setHeight(30);
            indent(out);
            String wrapRect = "<rect style=\"stroke:black;fill:white\" transform=\"translate(0,0)\" width=\"" +
                    getWidth() + "\" " +
                    "  height=\""
                    + getHeight() +
                    "\"></rect>\n";
            out.append(wrapRect);
            String labelText = "        <g transform=\"translate(2,2)\">\n" +
                    "            <text style=\"fill:Black;\" text-anchor=\"left\" transform=\"translate(0,20)\">Secondary Structure</text>\n" +
                    "        </g>\n";
              indent(out);
            out.append(labelText);
            indent(out);
                 out.append("<g transform=\"translate(240,4)\">\n");
            int startx = 00;
            for (int i = 0; i < OneAminoAcidFragmentBuillder.SECONDARY_STRUCTURE_COLORS.length; i++) {
                String color =  OneAminoAcidFragmentBuillder.SECONDARY_STRUCTURE_COLORS[i];
                String coverageLabel =
                        "          <g transform=\"translate(" + startx + ",0)\">\n" +
                                "                <rect style=\"fill:" + color + ";\" transform=\"translate(0,0)\" width=\"20\" height=\"20\"></rect>\n" +
                                "                <text style=\"fill:Black;\" text-anchor=\"left\" transform=\"translate(30,20)\">" +
                                 OneAminoAcidFragmentBuillder.SECONDARY_STRUCTURE_TEXT[i] + "</text>\n" +
                                "            </g>\n";
                 out.append(coverageLabel);
                startx += ITEM_WIDTH[i];
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
