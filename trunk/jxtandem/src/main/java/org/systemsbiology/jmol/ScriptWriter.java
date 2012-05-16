package org.systemsbiology.jmol;

import org.systemsbiology.xtandem.hadoop.*;

import java.util.*;

/**
 * org.systemsbiology.jmol.ScriptWriter
 * User: steven
 * Date: 5/15/12
 */
public class ScriptWriter {
    public static final ScriptWriter[] EMPTY_ARRAY = {};

    public static final String[] COLOR_NAMES = {"red", "yellow", "orange", "green", "blue"};

    public static String buildTranslucentString(int used)
    {
        int val = Math.min(128 * used,255);
        StringBuilder sb = new StringBuilder();
        sb.append("translucent[" + val + "," + val + "," + val + "]");
        return sb.toString();
    }

    private final Map<AminoAcidAtLocation,Integer> m_UsedPositions = new HashMap<AminoAcidAtLocation,Integer>();

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

    public int getHilight(AminoAcidAtLocation loc)  {
        int ret = 1;
        Integer val = m_UsedPositions.get(loc);
        if(val != null)  {
            ret = val + 1;
        }
        m_UsedPositions.put(loc,ret);
        return ret;
    }

    private void appendScriptHilight(AminoAcidAtLocation[] hilighted, String colorName, StringBuilder sb) {
        for (int i = 0; i < hilighted.length; i++) {
            AminoAcidAtLocation aa = hilighted[i];
            appendScriptHilight(aa, colorName, sb);
        }
    }

    private void appendScriptHilight(AminoAcidAtLocation aa, String colorName, StringBuilder sb) {
        int used = getHilight(aa);
        sb.append("select " + aa.toString() + ";");
     //   sb.append("color " + buildTranslucentString(used) + " " +  colorName);
        sb.append("color " +    colorName);
          sb.append("\n");
    }

    private void appendScriptHeader(PDBObject original, StringBuilder sb) {
        sb.append("select all;color translucent[80,80,80] white");
        sb.append("\n");
        sb.append("select all ;ribbon off");
          sb.append("\n");

    }

}
