package org.systemsbiology.jmol;

import org.systemsbiology.xtandem.*;

import java.io.*;
import java.util.*;

/**
 * org.systemsbiology.jmol.PDBObject
 * User: steven
 * Date: 5/15/12
 */
public class PDBObject {
    public static final PDBObject[] EMPTY_ARRAY = {};

    private final List<AminoAcidAtLocation>  m_DisplayedAminoAcids = new ArrayList<AminoAcidAtLocation>();



    public void readFromReader(LineNumberReader rdr)  {
        try {
            AminoAcidAtLocation here = null;
            String line = rdr.readLine();
            while(line != null)   {
                AminoAcidAtLocation now = handleLine(line,here);
                if(now != null && !now.equals(here))
                    m_DisplayedAminoAcids.add(now);
                here = now;
                line = rdr.readLine();
            }
        }
        catch (IOException e) {
            throw new RuntimeException(e);

        }
    }

    private AminoAcidAtLocation handleLine(String line,AminoAcidAtLocation here) {
        if(line.startsWith("ATOM")) {
            return handleAtom(line,here);
        }
        return here;
    }

    private AminoAcidAtLocation handleAtom(String line, AminoAcidAtLocation here) {
        while(line.contains("  "))
            line = line.replace("  "," ");
         line = line.replace(" ","\t");
        String[] items = line.split("\t");
        FastaAminoAcid aa =  FastaAminoAcid.fromAbbreviation(items[3]);
        String chain = items[4];
        int position = Integer.parseInt(items[5]);
        AminoAcidAtLocation now = new AminoAcidAtLocation(aa,position);
        if(now.equals(here))
            return here;
        return now;
    }


    public AminoAcidAtLocation[] getAminoAcidsForSequence(String foundSequence) {
        FastaAminoAcid[] aas =  FastaAminoAcid.asAminoAcids(foundSequence);
        for (int i = 0; i < m_DisplayedAminoAcids.size() - aas.length; i++) {
            AminoAcidAtLocation aminoAcidAtLocation = m_DisplayedAminoAcids.get(i);
            if(aminoAcidAtLocation.getAminoAcid() == aas[0]) {
                 AminoAcidAtLocation[] ret = maybeBuildSequence(aas,i);
                if(ret != null)
                    return ret;
            }

        }
        return null;

    }

    private AminoAcidAtLocation[] maybeBuildSequence(FastaAminoAcid[] aas, int start) {
        List<AminoAcidAtLocation> holder = new ArrayList<AminoAcidAtLocation>();
        holder.add(m_DisplayedAminoAcids.get(start));
        for (int i = 1; i < aas.length; i++) {
            AminoAcidAtLocation aminoAcidAtLocation = m_DisplayedAminoAcids.get( start + i);
            if(aminoAcidAtLocation.getAminoAcid() == aas[i]) {
                 holder.add(aminoAcidAtLocation);
            }
            else {
                return null; // bad
            }
        }

        AminoAcidAtLocation[] ret = new AminoAcidAtLocation[holder.size()];
        holder.toArray(ret);
        return ret;
     }

    public static final String[] SOUGHT_SEQUENCES = {
            "HMDLCLTVVDR",
            "HVGSNLCLDSR",
            "HMDLCLTVVDRAPGSLIK",
            "VLTFLDSHCECNEHWLEPLLER",
            "HMDLCLTVVDRAPGSLIK",
     };

    public static void main(String[] args) throws Exception {
        PDBObject obj = new PDBObject();
        LineNumberReader rdr = new LineNumberReader(new FileReader(args[0]))  ;
        obj.readFromReader(rdr) ;
        List<AminoAcidAtLocation[]> holder = new ArrayList<AminoAcidAtLocation[]>();

        for (int i = 0; i < SOUGHT_SEQUENCES.length; i++) {
            String foundSequence = SOUGHT_SEQUENCES[i];
            AminoAcidAtLocation[] locs =  obj.getAminoAcidsForSequence( foundSequence);
            holder.add(locs);
        }
        AminoAcidAtLocation[][] ret = new AminoAcidAtLocation[holder.size()][];
        holder.toArray(ret);
        ScriptWriter sw  = new ScriptWriter();
        String script = sw.writeScript(obj, SOUGHT_SEQUENCES);
        System.out.println(script);
    }
}
