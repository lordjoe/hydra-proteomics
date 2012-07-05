package org.systemsbiology.jmol;

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
        coverage = Math.min(coverage, MAX_COVERAGE);
        int cvalue = Math.min(255, 120 + ((136 * coverage) / MAX_COVERAGE));
        return "[" + cvalue + "," + cvalue + ",80]";
    }

    private final Map<AminoAcidAtLocation, Integer> m_UsedPositions = new HashMap<AminoAcidAtLocation, Integer>();

    public String writeScript(PDBObject original, String[] foundSequences) {
        m_UsedPositions.clear();
        StringBuilder sb = new StringBuilder();
        appendScriptHeader(original, sb);
        for (int i = 0; i < foundSequences.length; i++) {
            String foundSequence = foundSequences[i];
            AminoAcidAtLocation[] highlited = original.getAminoAcidsForSequence(foundSequence);
            appendScriptHilight(highlited, COLOR_NAMES[i & COLOR_NAMES.length], sb);

        }

        return sb.toString();
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

    public String getChainColor(ChainEnum chain, int index) {
        if (index == 0)
            return "";
        StringBuilder sb = new StringBuilder();
        sb.append("select {*:" + chain + "};color translucent" + TRANSLUCENT_COLORS[index % TRANSLUCENT_COLORS.length] + " white;");
        return sb.toString();
    }

    public String writeHideChainsScript(ProteinFragmentationDescription pfd ) {
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

        StringBuilder sb = new StringBuilder();
        appendScriptHeader(original, sb);
        for (int i = 0; i < coverage.length; i++) {
            short corg = coverage[i];
            if (corg > 0) {
                AminoAcidAtLocation aa = original.getAminoAcidAtLocation(i);
                if (aa != null) {
                    String coverageColor = getCoverageColor(corg);
                    appendScriptHilight(aa, coverageColor, sb);

                }

            }
        }

        return sb.toString();
    }

    public String writeScript(ProteinFragmentationDescription pfd, AminoAcidAtLocation[][] foundSequences) {
        PDBObject original = pfd.getModel();
        m_UsedPositions.clear();
        StringBuilder sb = new StringBuilder();
        appendScriptHeader(original, sb);
        String hiliteChains = writeHideChainsScript(  pfd );
        sb.append(hiliteChains);
        sb.append("\n");
        for (int i = 0; i < foundSequences.length; i++) {
            AminoAcidAtLocation[] highlited = foundSequences[i];
            appendScriptHilight(highlited, COLOR_NAMES[i % COLOR_NAMES.length], sb);

        }

        return sb.toString();
    }


    public String writeScript(ProteinFragmentationDescription pfd, AminoAcidAtLocation[] highlited, int index) {
        m_UsedPositions.clear();
        StringBuilder sb = new StringBuilder();
        PDBObject original = pfd.getModel();
        appendScriptHeader(original, sb);
        appendScriptHilight(highlited, COLOR_NAMES[index % COLOR_NAMES.length], sb);
        return sb.toString();
    }

    public int getHilight(AminoAcidAtLocation loc) {
        int ret = 1;
        Integer val = m_UsedPositions.get(loc);
        if (val != null) {
            ret = val + 1;
        }
        m_UsedPositions.put(loc, ret);
        return ret;
    }

    private void appendScriptHilight(AminoAcidAtLocation[] hilighted, String colorName, Appendable sb) {
        for (int i = 0; i < hilighted.length; i++) {
            AminoAcidAtLocation aa = hilighted[i];
            appendScriptHilight(aa, colorName, sb);
        }
    }

    private void appendScriptHilight(AminoAcidAtLocation aa, String colorName, Appendable sb) {
        if (aa.getLocation() == -1)
            return;
        int used = getHilight(aa);
        try {
            sb.append("select " + aa.toString() + ";");
            //   sb.append("color " + buildTranslucentString(used) + " " +  colorName);
            sb.append("color " + colorName);
            sb.append(";\n");
        }
        catch (IOException e) {
            throw new RuntimeException(e);

        }
    }

    private void appendScriptHeader(PDBObject original, Appendable sb) {
        try {
            File file = original.getFile();
            String fileName = file.getName();
            String parentName = file.getParentFile().getName();
            sb.append("load ../" + parentName + "/" + fileName);
            sb.append(";\n");
            sb.append("select all;color translucent[80,80,80] white");
            sb.append(";\n");
            sb.append("select all ;ribbon off");
            sb.append(";\n");
        }
        catch (IOException e) {
            throw new RuntimeException(e);

        }

    }

}
