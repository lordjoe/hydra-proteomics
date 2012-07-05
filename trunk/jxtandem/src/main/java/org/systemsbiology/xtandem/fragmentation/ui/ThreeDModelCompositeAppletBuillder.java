package org.systemsbiology.xtandem.fragmentation.ui;

import org.systemsbiology.jmol.*;
import org.systemsbiology.xtandem.fragmentation.*;

import java.io.*;
import java.util.*;

/**
 * org.systemsbiology.xtandem.fragmentation.ui.HTMLPageBuillder
 * User: Steve
 * Date: 6/25/12
 */
public class ThreeDModelCompositeAppletBuillder extends AbstractHtmlFragmentHolder {
    public static final ThreeDModelCompositeAppletBuillder[] EMPTY_ARRAY = {};

    private final Map<ProteinFragment, AminoAcidAtLocation[]> m_FragmentLocations;
    private final ProteinFragmentationDescription m_PFD;
    private final ScriptWriter m_ScriptWriter = new ScriptWriter();

    public ThreeDModelCompositeAppletBuillder(final IHtmlFragmentHolder page, ProteinFragmentationDescription pfd, final Map<ProteinFragment, AminoAcidAtLocation[]> fragments) {
        super(page);
        m_FragmentLocations = fragments;
        m_PFD = pfd;
     }

    public ProteinFragmentationDescription getPFD() {
        return m_PFD;
    }

    public PDBObject getModel() {
        return getPFD().getModel();
    }

    @Override
    public void addStartText(final Appendable out, final Object... data) {
        try {
            List<AminoAcidAtLocation[]> holder = new ArrayList<AminoAcidAtLocation[]>();
            for (ProteinFragment pf : m_FragmentLocations.keySet()) {
                holder.add(m_FragmentLocations.get(pf));
            }

            AminoAcidAtLocation[][] locations = new AminoAcidAtLocation[holder.size()][];
            holder.toArray(locations);
            out.append("<applet id=\"" + getUniqueId() + "\" name=\"flash\" code=\"JmolApplet\" archive=\"JmolApplet.jar\"\n" +
                    "        codebase=\"../..\"\n" +
                    "        width=\"924\" height=\"678\" align=\"center\" mayscript=\"true\">");
            out.append("<param name=\"bgcolor\" value=\"black\">");
            out.append("\n");
            out.append("<param name=\"progressbar\" value=\"true\">");
            out.append("\n");
            out.append("<param name=\"script\" value=\"\n");
            String script = m_ScriptWriter.writeScript(getPFD(),locations);
            out.append(script);
            out.append("  \">\n");

        }
        catch (IOException e) {
            throw new RuntimeException(e);

        }
    }

    @Override
    public void addEndText(final Appendable out, final Object... data) {
        try {
            out.append("</applet>");
            out.append("\n");
        }
        catch (IOException e) {
            throw new RuntimeException(e);

        }

    }


}
