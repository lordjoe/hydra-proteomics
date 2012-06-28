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
public class ThreeDModelAppletBuillder extends AbstractHtmlFragmentHolder {
    public static final ThreeDModelAppletBuillder[] EMPTY_ARRAY = {};

    private final ProteinFragmentationDescription m_Fragments;
    private final AminoAcidAtLocation[]  m_Locations;
    private final ProteinFragment m_Fragment;
    private final PDBObject m_Model;
    private final ScriptWriter m_ScriptWriter = new ScriptWriter();
    private final int m_Index;

    public ThreeDModelAppletBuillder(final IHtmlFragmentHolder page, ProteinFragmentationDescription pfd,ProteinFragment pf,AminoAcidAtLocation[]locs,int index) {
        super(page);
        m_Fragments = pfd;
        m_Model = pfd.getModel();
        m_Locations = locs;
        m_Fragment = pf ;
        m_Index = index;
    }


    public ProteinFragmentationDescription getFragments() {
        return m_Fragments;
    }

    public PDBObject getModel() {
        return m_Model;
    }

    @Override
    public void addStartText(final Appendable out, final Object... data) {
        try {
            out.append("<applet id=\"" + getUniqueId() + "\" name=\"flash\" code=\"JmolApplet\" archive=\"JmolApplet.jar\"\n" +
                    "        codebase=\"../..\"\n" +
                    "        width=\"500\" height=\"420\" align=\"center\" mayscript=\"true\">");
            out.append("<param name=\"bgcolor\" value=\"black\">");
            out.append("\n");
            out.append("<param name=\"progressbar\" value=\"true\">");
            out.append("\n");
              out.append("<param name=\"script\" value=\"\n");
            String script = m_ScriptWriter.writeScript(m_Model,m_Locations,m_Index);
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
