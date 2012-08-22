package org.systemsbiology.gatk;

import java.io.*;
import java.util.*;

/**
 * org.systemsbiology.gatk.InterectingSNP
 * User: steven
 * Date: 8/22/12
 */
public class InterectingSNP {
    public static final InterectingSNP[] EMPTY_ARRAY = {};

    public static InterectingSNP[] readFromCSV(File f) {
        String[] lines = GeneUtilities.readInLines(f);
        List<InterectingSNP> holder = new ArrayList<InterectingSNP>();
        // start at 1 to drop header
        for (int i = 1; i < lines.length; i++) {
            String line = lines[i];
            if(line.length() < 10 ||!line.contains(","))
                continue;
            holder.add(new InterectingSNP(line));
        }
        InterectingSNP[] ret = new InterectingSNP[holder.size()];
        holder.toArray(ret);
        return ret;
    }

    private final String m_Subject;
    private final String m_UniprotId;
    private final GeneLocation m_Loc;
    private final DNABase m_Reference;
    private final DNABase m_Altered;

    public InterectingSNP(String line) {
        String[] split = line.split(",");
        m_Subject = split[0];
        m_Loc = new GeneLocation(split[1] + ":" + split[2]);
        m_UniprotId = split[4];
        if (split.length < 7 || split[5].isEmpty() || split[6].isEmpty()) {
            m_Reference = null;
            m_Altered = null;
        }
        else {
            m_Reference = DNABase.valueOf(split[5].substring(0, 1));
            m_Altered = DNABase.valueOf(split[6].substring(0, 1));

        }
    }

    public String getSubject() {
        return m_Subject;
    }

    public DNABase getAltered() {
        return m_Altered;
    }

    public DNABase getReference() {
        return m_Reference;
    }

    public GeneLocation getLoc() {
        return m_Loc;
    }

    public String getUniprotId() {
        return m_UniprotId;
    }
}
