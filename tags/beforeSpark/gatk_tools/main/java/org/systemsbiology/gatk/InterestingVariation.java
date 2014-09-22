package org.systemsbiology.gatk;

import java.io.*;
import java.util.*;

/**
 * org.systemsbiology.gatk.InterestingVariation
 * User: steven
 * Date: 7/23/12
 */
public class InterestingVariation {
    public static final InterestingVariation[] EMPTY_ARRAY = {};

    public static InterestingVariation[] readInterestingVariants(File test) {
        String[] lines = GeneUtilities.readInLines(test);
        List<InterestingVariation> holder = new ArrayList<InterestingVariation>();
        for (int i = 1; i < lines.length; i++) {
            String line = lines[i];
            InterestingVariation iv = new InterestingVariation(line);
            holder.add(iv);
        }
        InterestingVariation[] ret = new InterestingVariation[holder.size()];
        holder.toArray(ret);
        return ret;
    }


    private final String m_Subject;
    private final GeneLocation m_Location;
    private final String m_Gene;
    private DNABase m_OldBase;
    private DNABase m_NewBase;

    public InterestingVariation(GeneLocation location, String subject, String gene) {
        m_Location = location;
        m_Subject = subject;
        m_Gene = gene;
    }

    public InterestingVariation(String line) {
        String[] items = line.split(",");
        m_Subject = items[0];
        String chromosome = items[1];
        Long loc = new Long(items[2]);
        m_Location = new GeneLocation(chromosome, loc);
        m_Gene = items[3];

        if (items.length > 4)
            m_OldBase = DNABase.valueOf(items[4]);

        if (items.length > 5)
            m_NewBase = DNABase.valueOf(items[5]);
    }


    public GeneLocation getLocation() {
        return m_Location;
    }

    public String getGene() {
        return m_Gene;
    }

    public DNABase getOldBase() {
        return m_OldBase;
    }

    public DNABase getNewBase() {
        return m_NewBase;
    }

    public String getSubject() {
        return m_Subject;
    }

    public void setOldBase(DNABase oldBase) {
        m_OldBase = oldBase;
    }

    public void setNewBase(DNABase newBase) {
        m_NewBase = newBase;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(
                m_Subject + "," +
                        m_Location.getChromosome() + "," +
                        m_Location.getLocation() + "," +
                        m_Gene);
        if (getOldBase() != null) {
            sb.append("," + getOldBase() + "," + getNewBase());
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        InterestingVariation that = (InterestingVariation) o;

        if (m_Location != null ? !m_Location.equals(that.m_Location) : that.m_Location != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return m_Location != null ? m_Location.hashCode() : 0;
    }

    public static void main(String[] args) {
        File test = new File(args[0]);
        InterestingVariation[] interestingVariations = readInterestingVariants(test);
        for (int i = 0; i < interestingVariations.length; i++) {
            InterestingVariation interestingVariation = interestingVariations[i];

        }
        return;
    }

}
