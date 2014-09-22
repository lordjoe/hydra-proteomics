package org.systemsbiology.jmol;

import org.systemsbiology.asa.*;
import org.systemsbiology.xtandem.*;
import org.systemsbiology.xtandem.fragmentation.*;

import java.io.*;
import java.util.*;

/**
 * org.systemsbiology.jmol.ScriptWriter
 * User: steven
 * Date: 5/15/12
 */
public class ScriptWriter {
    public static final ScriptWriter[] EMPTY_ARRAY = {};

    public static final String[] DEFAULT_COLOR_NAMES = {"red", "yellow", "orange", "green", "blue"};

    public static String[] COLOR_NAMES = DEFAULT_COLOR_NAMES;

    public static String buildTranslucentString(int used) {
        int val = Math.min(128 * used, 255);
        StringBuilder sb = new StringBuilder();
        sb.append("translucent[" + val + "," + val + "," + val + "]");
        return sb.toString();
    }

    public static final int MAX_COVERAGE = 6;

    public static String getCoverageColor(int coverage) {
        // changes to be 1 or more

        coverage = Math.min(coverage, MAX_COVERAGE);
        int cvalue = 165;
        if(coverage > 1)   {
            cvalue = 220;
        }
 //       int cvalue = Math.min(255, 120 + ((136 * coverage) / MAX_COVERAGE));
        return "[" + cvalue + "," + cvalue + ",80]";
    }

    private final Map<IAminoAcidAtLocation, Integer> m_UsedPositions = new HashMap<IAminoAcidAtLocation, Integer>();

    public String writeScript(PDBObject original, String[] foundSequences) {
        m_UsedPositions.clear();
        StringBuilder sb = new StringBuilder();
        boolean quoteNewLines = false;
        appendScriptHeader(original, quoteNewLines, sb);
        for (int i = 0; i < foundSequences.length; i++) {
            String foundSequence = foundSequences[i];
            IAminoAcidAtLocation[] highlited = original.getAminoAcidsForSequence(foundSequence);
            appendScriptHilight(highlited, COLOR_NAMES[i & COLOR_NAMES.length], sb);
            sb.append("\n");
        }

        return sb.toString();
    }


    public static String buildFragmentSelectMenu(ProteinFragmentationDescription pfd) {
        Map<ProteinFragment, IAminoAcidAtLocation[]> aminoAcidLocations = pfd.getAminoAcidLocations();
        ProteinFragment[] proteinFragments = aminoAcidLocations.keySet().toArray(ProteinFragment.EMPTY_ARRAY);
        Arrays.sort(proteinFragments);
        StringBuilder sb = new StringBuilder();
        //     sb.append("jmolHtml(\"<h3>Dim Chain</h3>\");\n");
        sb.append("jmolMenu([\n");
        sb.append("[ [setAndScript,showAminoAcids ], \"all\", true]");
        for (ProteinFragment pf : proteinFragments) {
            String select = pf.getSequence();
            if (select.length() > 13)
                select = select.substring(0, 10) + "...";
            sb.append(",\n");
            //            sb.append("[[SetFragment" + pf.getIndex() + "], \"" + select + "\"]");
            sb.append("[[setAndScript,fragments[" + (1 + pf.getIndex()) + "]],\"" + select + "\"]");
        }
        //   sb.append( "],12,\'FragmentsMenu\',\"Select Fragments\");\n");
        sb.append("\n] );\n");
        return sb.toString();
    }


    public static String buildFragmentFunctionStrings(ProteinFragmentationDescription pfd) {
        Map<ProteinFragment, IAminoAcidAtLocation[]> aminoAcidLocations = pfd.getAminoAcidLocations();
        ProteinFragment[] proteinFragments = aminoAcidLocations.keySet().toArray(ProteinFragment.EMPTY_ARRAY);
        Arrays.sort(proteinFragments);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < proteinFragments.length; i++) {
            ProteinFragment proteinFragment = proteinFragments[i];

        }
        return sb.toString();
    }


    public static String buildChainsCheckBoxes(ProteinFragmentationDescription pfd) {
        PDBObject pdbObject = pfd.getModel();
        ChainEnum[] chains = pdbObject.getChains();
        StringBuilder sb = new StringBuilder();
        if (chains.length < 2)
            return sb.toString();

        //     sb.append("jmolHtml(\"<h3>Dim Chain</h3>\");\n");
        for (int i = 0; i < chains.length; i++) {
            ChainEnum chain = chains[i];
            String color = ScriptWriter.getHideChainCommand(chain, i);

            sb.append("jmolCheckbox([hideChain," + (i + 1) + "],[showChain," + (i + 1) + ",\'" + color + "\'],\"Chain " + chain + "\",\"checked\");\n");
        }
        return sb.toString();
    }

    public static String buildCoverageAminoAcidSelector() {
        StringBuilder sb = new StringBuilder();
        sb.append("jmolRadioGroup([\n" +
                "\n" +
                "     [ [setAndScript,showCoverage], \"Coverage\"] ,\n" +
       //         "     [ [setAndScript,showSolventAccess], \"Solvent Access\"] ,\n" +
                "     [ [setAndScript,showSolventAtomicAccess], \"Solvent Atomic Access\"] ,\n" +
                "     [ [setAndScript,showHydrophobicity], \"Hydrophobicity\"] ,\n" +
                " \t  [ [setAndScript,showAminoAcids] , \"Peptides\",  \"checked\"] \n" +
                "  \t]);");
        return sb.toString();
    }

    protected static StringBuilder buildChainsCheckBoxes(ChainEnum[] chains) {
        StringBuilder sb = new StringBuilder();
        sb.append("Dim Chain");
        for (int i = 0; i < chains.length; i++) {
            ChainEnum chain = chains[i];
            String color = ScriptWriter.getChainColor(chain, i);
            sb.append("jmolCheckbox(\"" + color + "\" , currentScript," + chain);
            if (i > 0)
                sb.append(",");
            sb.append(chain);
        }
        return sb;
    }


    public static final String[] TRANSLUCENT_COLORS =
            {
                    "[100,0,0]",
                    "[0,100,0]",
                    "[0,0,100]",
                    "[60,60,0]",
                    "[ 0,60,60]",
                    "[60,0,60]",
            };

    public static String getChainColor(ChainEnum chain, int index) {
        if (index == 0)
            return "";
        StringBuilder sb = new StringBuilder();
        sb.append("select {*:" + chain + "};color translucent" + TRANSLUCENT_COLORS[index % TRANSLUCENT_COLORS.length] + " white;");
        return sb.toString();
    }

    public static String getHideChainCommand(ChainEnum chain, int index) {
        StringBuilder sb = new StringBuilder();
        sb.append("select {*:" + chain + "};color translucent" + TRANSLUCENT_COLORS[index % TRANSLUCENT_COLORS.length] + " white;");
        return sb.toString();
    }

    public String writeHideChainsScript(ProteinFragmentationDescription pfd) {
        m_UsedPositions.clear();
        PDBObject original = pfd.getModel();

        StringBuilder sb = new StringBuilder();
        int index = 0;
        ChainEnum[] chains = pfd.getModel().getChains();
        for (int i = 0; i < chains.length; i++) {
            ChainEnum chain = chains[i];
            String line = getChainColor(chain, index++);
            if (line.length() > 0) {
                sb.append(line);
                sb.append("\n");
            }
        }
        return sb.toString();
    }


    public String writeScript(ProteinFragmentationDescription pfd, short[] coverage) {
        m_UsedPositions.clear();
        PDBObject original = pfd.getModel();
        ProteinSubunit[] subUnits = original.getSubUnits();
        StringBuilder sb = new StringBuilder();
        boolean quoteNewLines = true;
        appendScriptHeader(original, quoteNewLines, sb);
        for (int i = 0; i < coverage.length; i++) {
            short corg = coverage[i];
            if (corg > 0) {
                SequenceChainMap mapping = original.getMapping(i);
                IAminoAcidAtLocation[] chainMappings = mapping.getChainMappings();
                for (int j = 0; j < chainMappings.length; j++) {
                    IAminoAcidAtLocation aa = chainMappings[j];
                    String coverageColor = getCoverageColor(corg);
                    if (appendScriptHilight(aa, coverageColor, sb)) ;
                    sb.append("\\\n");

                }
//                for (int j = 0; j < subUnits.length; j++) {
//                    ProteinSubunit subUnit = subUnits[j];
//                    AminoAcidAtLocation aa = subUnit.getAminoAcidAtLocation(i);
//                      if (aa != null) {
//                          String coverageColor = getCoverageColor(corg);
//                          if(appendScriptHilight(aa, coverageColor, sb));
//                              sb.append("\\\n");
//                          break; // only for one subunit???
//                      }
//
//                }
            }
        }

        return sb.toString();
    }

    public String writeSolventAccessScript(ProteinFragmentationDescription pfd) {
        if(true)
            throw new UnsupportedOperationException("This is not very useful use writeSolventAtomicAccessScript");
        m_UsedPositions.clear();
        PDBObject original = pfd.getModel();
        StringBuilder sb = new StringBuilder();
        boolean quoteNewLines = true;
        appendScriptHeader(original, quoteNewLines, sb);
        sb.append("select all;color translucent[0,0,50];");
        sb.append("wireframe off;spacefill 100%;");
        sb.append("select water;color translucent[0,0,50];");
        AsaSubunit[] su = original.getAccessibleSubunits();
        for (int i = 0; i < su.length; i++) {
            AsaSubunit corg = su[i];
            if (corg instanceof IAminoAcidAtLocation) {
                AminoAcidAtLocation aa = (AminoAcidAtLocation) corg;
                if (!aa.isAccessible()) {
                    appendScriptHilight(aa, "red", sb);
                    sb.append("\\\n");
                }
            }
        }

        return sb.toString();
    }

    public static final String[] HYDROPHOBIC_COLOR_STRINGS =
            {
//                    "#FF0000", "#FF1010", "#FF2020", "#FF3030", "#FF4040", "#FF5050",
//                    "#FF6060", "#FF7070", "#FF8080", "#FF9090", "3FFA0A0", "#FFB0B0",
//                    "#FFC0C0", "#FFD0D0", "#FFE0E0", "#FFFFFF", "#E0E0FF", "#D0D0FF",
//                    "#C0C0FF", "#B0B0FF", "#A0A0FF", "#9090FF", "#8080FF", "#7070FF",
//                    "#6060FF", "#5050FF", "#4040FF", "#3030FF", "#2020FF", "31010FF",
//                    "#0000FF"
                    "FF0000", "FF1010", "FF2020", "FF3030", "FF4040", "FF5050",
                    "FF6060", "FF7070", "FF8080", "FF9090", "3FFA0A0","FFB0B0",
                    "FFC0C0", "FFD0D0", "FFE0E0", "FFFFFF", "E0E0FF", "D0D0FF",
                    "C0C0FF", "B0B0FF", "A0A0FF", "9090FF", "8080FF", "7070FF",
                    "6060FF", "5050FF", "4040FF", "3030FF", "2020FF", "3101FF",
                    "0000FF"

            };
    public static final int NUMBER_HYDROPHOBIC_COLORS = HYDROPHOBIC_COLOR_STRINGS.length;

    public static String hydrophobicityColor(FastaAminoAcid aa) {
        if(FastaAminoAcid.UNKNOWN == aa)
            return "translucent[0,0,50]";
        int hydrophobicity = FastaAminoAcid.hydroPhobicityRank(aa,NUMBER_HYDROPHOBIC_COLORS);
        if(hydrophobicity == -1)
            return "translucent[0,0,50]";
        String s = HYDROPHOBIC_COLOR_STRINGS[NUMBER_HYDROPHOBIC_COLORS - hydrophobicity - 1];
        int red = Integer.parseInt(s.substring(0,2),16);
        int green = Integer.parseInt(s.substring(2,4),16);
        int blue = Integer.parseInt(s.substring(4,6),16);

        return "[" + red + "," + green +"," + blue + "]";
    }


    public String writeHydrophobicityScript(ProteinFragmentationDescription pfd) {
        PDBObject original = pfd.getModel();
        StringBuilder sb = new StringBuilder();
        boolean quoteNewLines = true;
        appendScriptHeader(original, quoteNewLines, sb);
        AsaSubunit[] su = original.getAccessibleSubunits();
        for (int i = 0; i < su.length; i++) {
            AsaSubunit corg = su[i];
            if (corg instanceof IAminoAcidAtLocation) {
                IAminoAcidAtLocation aa = (IAminoAcidAtLocation) corg;
                if (aa.isSometimesMissedCleavage())
                    appendScriptHilight(aa, "green", sb);
                else
                    appendScriptHilight(aa, hydrophobicityColor(aa.getAminoAcid()), sb);
                sb.append("\\\n");
            }
        }

        return sb.toString();
    }


    public String writeSolventAtomicAccessScript(ProteinFragmentationDescription pfd) {
        m_UsedPositions.clear();
        PDBObject original = pfd.getModel();

        StringBuilder sb = new StringBuilder();
        boolean quoteNewLines = true;
        appendScriptHeader(original, quoteNewLines, sb);
        sb.append("select all;color translucent[0,0,50];");
        sb.append("wireframe off;spacefill 100%;");
        sb.append("select water;color translucent[0,0,50];");
        AsaAtom[] atoms = original.getAtoms();
        for (int i = 0; i < atoms.length; i++) {
            AsaAtom atom = atoms[i];
            if (!atom.isAccessible()) {
                sb.append("select atomno = " + atom.getNum() + ";color red;");
                sb.append("\\\n");
            }

        }
        return sb.toString();
    }


    public String writeHilightText(ProteinFragmentationDescription pfd) {
        Map<ProteinFragment, IAminoAcidAtLocation[]> aminoAcidLocations = pfd.getAminoAcidLocations();
        ProteinFragment[] proteinFragments = aminoAcidLocations.keySet().toArray(ProteinFragment.EMPTY_ARRAY);
        Arrays.sort(proteinFragments);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < proteinFragments.length; i++) {
            ProteinFragment pf = proteinFragments[i];
            sb.append("HilightFragment" + (pf.getIndex() + 1) + "=\'\\\n");
            IAminoAcidAtLocation[] highlited = aminoAcidLocations.get(pf);
            String colorName = COLOR_NAMES[i % COLOR_NAMES.length];
            for (int j = 0; j < highlited.length; j++) {
                IAminoAcidAtLocation aa = highlited[j];
                appendScriptHilight(aa, colorName, sb);
                sb.append("\\\n");   // app end to javascript string

            }
            sb.append("\';\n");

        }
        return sb.toString();
    }


    public String writeScript(ProteinFragmentationDescription pfd) {
        Map<ProteinFragment, IAminoAcidAtLocation[]> aminoAcidLocations = pfd.getAminoAcidLocations();
        ProteinFragment[] proteinFragments = aminoAcidLocations.keySet().toArray(ProteinFragment.EMPTY_ARRAY);
        Arrays.sort(proteinFragments);
        StringBuilder sb = new StringBuilder();
        sb.append("var fragments = [];\n");
        for (int i = 0; i < proteinFragments.length; i++) {
            ProteinFragment pf = proteinFragments[i];
//            if (i > 0)
//                sb.append("+");
            int index = pf.getIndex() + 1;
            sb.append("fragments[" + index + "] = HilightFragment" + index + ";\n");
        }
        //
//        PDBObject original = pfd.getModel();
//        m_UsedPositions.clear();
//        appendScriptHeader(original, sb);
//        String hiliteChains = writeHideChainsScript(pfd);
//        sb.append(hiliteChains);
//        sb.append("\n");
//        for (int i = 0; i < foundSequences.length; i++) {
//            AminoAcidAtLocation[] highlited = foundSequences[i];
//            appendScriptHilight(highlited, COLOR_NAMES[i % COLOR_NAMES.length], sb);
//
//        }

        return sb.toString();
    }


    public String writeScript(ProteinFragmentationDescription pfd, IAminoAcidAtLocation[] highlited, int index) {
        m_UsedPositions.clear();
        StringBuilder sb = new StringBuilder();
        PDBObject original = pfd.getModel();
        boolean quoteNewLines = false;
        appendScriptHeader(original, quoteNewLines, sb);
        appendScriptHilight(highlited, COLOR_NAMES[index % COLOR_NAMES.length], sb);
        return sb.toString();
    }

    public int getHilight(IAminoAcidAtLocation loc) {
        int ret = 1;
        Integer val = m_UsedPositions.get(loc);
        if (val != null) {
            ret = val + 1;
        }
        m_UsedPositions.put(loc, ret);
        return ret;
    }

    private void appendScriptHilight(IAminoAcidAtLocation[] hilighted, String colorName, Appendable sb) {
        for (int i = 0; i < hilighted.length; i++) {
            IAminoAcidAtLocation aa = hilighted[i];
            appendScriptHilight(aa, colorName, sb);
            try {
                sb.append("\n");
            }
            catch (IOException e) {
                throw new RuntimeException(e);

            }
        }
    }

    private boolean appendScriptHilight(IAminoAcidAtLocation aa, String colorName, Appendable sb) {
        if (aa.getLocation() == -1)
            return false;
        int used = getHilight(aa);
        try {
            sb.append("select " + aa.toString() + ";");
            //   sb.append("color " + buildTranslucentString(used) + " " +  colorName);
            sb.append("color " + colorName);
            sb.append(";");
            return true;
            //    sb.append(";\n");
        }
        catch (IOException e) {
            throw new RuntimeException(e);

        }
    }

    public static String getLoadText(ProteinFragmentationDescription pfd) {
        StringBuilder sb = new StringBuilder();
        PDBObject original = pfd.getModel();
        File file = original.getFile();
        String fileName = file.getName();
        String parentName = file.getParentFile().getName();
        sb.append("load ../" + parentName + "/" + fileName);
        sb.append(";");
        return sb.toString();

    }

    private void appendScriptHeader(PDBObject original, boolean quoteNewLines, Appendable sb) {
        try {
            File file = original.getFile();
            String fileName = file.getName();
            String parentName = file.getParentFile().getName();
            //   sb.append("load ../" + parentName + "/" + fileName);
            //   sb.append(";\n");
            sb.append("select all;color translucent[80,80,80] white;");
            if (quoteNewLines)
                sb.append("\\");
            sb.append("\n");
//            sb.append("select all ;ribbon off;wireframe on;spacefill 30%;");
//            if (quoteNewLines)
//                sb.append("\\");
//            sb.append("\n");
        }
        catch (IOException e) {
            throw new RuntimeException(e);

        }

    }

}
