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

    private final Map<ProteinFragment, IAminoAcidAtLocation[]> m_FragmentLocations;
    private final ProteinFragmentationDescription m_PFD;
    private final ScriptWriter m_ScriptWriter = new ScriptWriter();

    public ThreeDModelCompositeAppletBuillder(final IHtmlFragmentHolder page, ProteinFragmentationDescription pfd, final Map<ProteinFragment, IAminoAcidAtLocation[]> fragments) {
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
            List<IAminoAcidAtLocation[]> holder = new ArrayList<IAminoAcidAtLocation[]>();
            for (ProteinFragment pf : m_FragmentLocations.keySet()) {
                holder.add(m_FragmentLocations.get(pf));
            }

            IAminoAcidAtLocation[][] locations = new IAminoAcidAtLocation[holder.size()][];
            holder.toArray(locations);
            ProteinFragmentationDescription pfd = getPFD();
            String hilightText = m_ScriptWriter.writeHilightText(pfd);
            String scriptx = m_ScriptWriter.writeScript(pfd);
            String coveragescript = m_ScriptWriter.writeScript(pfd, pfd.getAllCoverage());
            String solventScript = m_ScriptWriter.writeSolventAccessScript(pfd );
            String solventAtomicScript = m_ScriptWriter.writeSolventAtomicAccessScript(pfd );
            String hydrophobicityScript = m_ScriptWriter.writeHydrophobicityScript(pfd )  ;
                //    script = script.replace("\n","\\\n");
            //     coveragescript = coveragescript.replace("\n","\\\n");

            out.append("      <script type=\"text/javascript\">\n");
            out.append("    var hideChains = [];\n");
            out.append("    var ribbons = 'off';\n");
            out.append("    jmol_id = \"" + ProteinCoveragePageBuilder.JMOL_APPLET_ID + "\";");
            out.append("    loadText = \'" + ScriptWriter.getLoadText(pfd) + "\';\n");
            out.append(hilightText + "\n");
            out.append(scriptx + "\n");
            out.append("    showAminoAcids = \'select all;color translucent[80,80,80] white;\' + fragments.join(\' \'); \n");
            out.append("    showSolventAtomicAccess = \'" + solventAtomicScript + "\';\n");
            out.append("    showHydrophobicity = \'" + hydrophobicityScript + "\';\n");
            out.append("    showSolventAccess = \'" + solventScript + "\';\n");
                out.append("    showCoverage = \'" + coveragescript + "\';\n");
               out.append("    window.defaultloadscript = showAminoAcids;\n");
            // force use of signed jar
            out.append("    jmolInitialize(\"../../\",\"JMolAppletSigned.jar\");\n");
            out.append("    jmolApplet([\"924\",\"678\"], loadText + \'select all;color translucent[80,80,80] white;select all ;wireframe on;spacefill 30%;ribbon off;\' + window.defaultloadscript,jmol_id);\n");
            out.append(
                    "\tfunction runScript( ) {\n" +
                            "          if('off' == ribbons) \n" +
                            "                jmolScript(\'select all;wireframe on;spacefill 30%;\',jmol_id);\n" +
                            "        script = \'select all;color translucent[80,80,80] white;\\\n" +
                            "          select all ;ribbon \' + ribbons + \';\'  +  \n" +
                            "         window.defaultloadscript  +  hideChains.join(\' \');\n" +
                            "          jmolScript(script,jmol_id);\n" +
                            "    }\n" +
                            "\tfunction setAndScript(btn, obj, target) {\n" +
                            "        // Entire array object is provided as 2nd argument.\n" +
                            "        window.defaultloadscript = obj[1];\n" +
                            "          runScript();\n" +
                            "    }\n" +
                            "\tfunction scriptOnly(btn, obj, target) {\n" +
                            "          runScript();\n" +
                            "    }\n" +
                            "\tfunction hideChain(btn, obj, target) {\n" +
                            "          hideChains[obj[1]] = \'\';\n" +
                            "          runScript();\n" +
                            "    }\n" +
                            "\tfunction showChain(btn, obj, target) {\n" +
                            "          hideChains[obj[1]] = obj[2];\n" +
                            "          runScript();\n" +
                            "    }\n" +
                            "\tfunction changeRibbon(btn, obj, target) {\n" +
                            "          ribbons = obj[1];\n" +
                            "          if('off' == ribbons) \n" +
                            "                jmolScript(\'select all;wireframe on;spacefill 30%;\',jmol_id);\n" +
                            "          runScript();\n" +
                            "    }\n" +
                            "\n");

            out.append(" \tjmolBr();\n");
            String view = m_ScriptWriter.buildCoverageAminoAcidSelector();
            out.append(view);
            out.append(" \tjmolBr();\n");
            String chechBoxes = m_ScriptWriter.buildChainsCheckBoxes(pfd);
            out.append(chechBoxes);
            out.append(" \tjmolBr();\n");
            out.append("jmolCheckbox([changeRibbon,\'only\'],[changeRibbon,\'off\'],\'Ribbons\');\n");
            out.append(" \tjmolBr();\n");
            String menu = m_ScriptWriter.buildFragmentSelectMenu(pfd);
            out.append(menu);
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
