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
            ProteinFragmentationDescription pfd = getPFD();
            String script = m_ScriptWriter.writeScript(pfd,locations);
            String coveragescript = m_ScriptWriter.writeScript(pfd, pfd.getAllCoverage());
            script = script.replace("\n","\\\n");
            coveragescript = coveragescript.replace("\n","\\\n");

            out.append("      <script type=\"text/javascript\">\n");
            out.append("    jmol_id = \"" + ProteinCoveragePageBuilder.JMOL_APPLET_ID + "\";") ;
             out.append("    loadText = \'" +  ScriptWriter.getLoadText(pfd ) + "\';\n");
            out.append("    showAminoAcids = \'" + script + "\';\n");
            out.append("    showCoverage = \'" + coveragescript + "\';\n");
            out.append("     window.defaultloadscript = showAminoAcids;\n");
                        out.append("    jmolInitialize(\"../../\");\n");
            out.append("    jmolApplet([\"924\",\"678\"], loadText + defaultloadscript,jmol_id);\n");
            out.append("\tfunction setAndScript(btn, obj, target) {\n" +
                    "        // Entire array object is provided as 2nd argument.\n" +
                    "        window.defaultloadscript = obj[1];\n" +
                    "        jmolScript(window.defaultloadscript,jmol_id);\n" +
                    "    }\n" +
                    "\tfunction scriptOnly(btn, obj, target) {\n" +
                    "          jmolScript(window.defaultloadscript,jmol_id);\n" +
                    "    }\n");
            out.append(" \tjmolBr();\n");
            String view =   m_ScriptWriter.buildCoverageAminoAcidSelector( );
            out.append(view );
            out.append(" \tjmolBr();\n");
            String chechBoxes = m_ScriptWriter.buildChainsCheckBoxes(  pfd);
          out.append(chechBoxes );
            out.append("\n");
            out.append("</script>\n");
//            out.append("<applet id=\"" + getUniqueId() + "\" name=\"flash\" code=\"JmolApplet\" archive=\"JmolApplet.jar\"\n" +
//                    "        codebase=\"../..\"\n" +
//                    "        width=\"924\" height=\"678\" align=\"center\" mayscript=\"true\">");
//            out.append("<param name=\"bgcolor\" value=\"black\">");
//            out.append("\n");
//            out.append("<param name=\"progressbar\" value=\"true\">");
//            out.append("\n");
//            out.append("<param name=\"script\" value=\"\n");
//             out.append(script);
//            out.append("  \">\n");

        }
        catch (IOException e) {
            throw new RuntimeException(e);

        }
    }

    @Override
    public void addEndText(final Appendable out, final Object... data) {
        try {
         //   out.append("</applet>");
             out.append("\n");
        }
        catch (IOException e) {
            throw new RuntimeException(e);

        }

    }


}
