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
public class ThreeDModelCoverageAppletBuillder extends AbstractHtmlFragmentHolder {
    public static final ThreeDModelCoverageAppletBuillder[] EMPTY_ARRAY = {};

    private final PDBObject m_Model;
    private final ProteinFragmentationDescription m_PFD;
      private final ScriptWriter m_ScriptWriter = new ScriptWriter();

    public ThreeDModelCoverageAppletBuillder(final IHtmlFragmentHolder page, ProteinFragmentationDescription pfd ) {
        super(page);
         m_Model = pfd.getModel();
        m_PFD = pfd;
    }


    public PDBObject getModel() {
        return m_Model;
    }

    public ProteinFragmentationDescription getPFD() {
        return m_PFD;
    }

    @Override
    public void addStartText(final Appendable out, final Object... data) {
        try {
             out.append("<applet id=\"" + getUniqueId() + "\" name=\"flash\" code=\"JmolApplet\" archive=\"JmolApplet.jar\"\n" +
                    "        codebase=\"../..\"\n" +
                    "        width=\"924\" height=\"678\" align=\"center\" mayscript=\"true\">");
            out.append("<param name=\"bgcolor\" value=\"black\">");
            out.append("\n");
            out.append("<param name=\"progressbar\" value=\"true\">");
            out.append("\n");
            out.append("<param name=\"script\" value=\"\n");
            String script = m_ScriptWriter.writeScript(m_Model,getPFD().getAllCoverage());
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
