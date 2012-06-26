package org.systemsbiology.gatk;


import java.util.*;

/**
 * org.systemsbiology.gatk.KnownGene
 * User: steven
 * Date: 6/18/12
 */
public class KnownGene implements Comparable<KnownGene> {
    public static final KnownGene[] EMPTY_ARRAY = {};

    private final String m_Name;
    private final String m_ProteinId;
    private String m_Chromosome;
    private int m_Start = Integer.MAX_VALUE;
    private final List<GeneInterval> m_Exons = new ArrayList<GeneInterval>();

    public KnownGene(String name, String proteinId) {
        m_Name = name;
        m_ProteinId = proteinId;
    }

    public KnownGene(String line) {
        String[] items = line.split("\t");
        int index = 0;
        String name = items[index++]; // varchar(255) NOT NULL default '',
        String chrom = items[index++]; // varchar(255) NOT NULL default '',
        String strand = items[index++]; // char(1) NOT NULL default '',
        String txStart = items[index++]; // int(10) unsigned NOT NULL default '0',
        String txEnd = items[index++]; // int(10) unsigned NOT NULL default '0',
        String cdsStart = items[index++]; // int(10) unsigned NOT NULL default '0',
        String cdsEnd = items[index++]; // int(10) unsigned NOT NULL default '0',
        String exonCount = items[index++]; // int(10) unsigned NOT NULL default '0',
        String exonStarts = items[index++]; // longblob NOT NULL,
        String exonEnds = items[index++]; // longblob NOT NULL,
        String proteinID = items[index++]; // varchar(40) NOT NULL default '',
        String alignID = items[index++]; // varchar(255) NOT NULL default '',

        m_Name = name;
        m_ProteinId = proteinID;
        int numberExons = Integer.parseInt(exonCount);
        String[] starts = exonStarts.split(",");
        String[] ends = exonEnds.split(",");
        for (int i = 0; i < numberExons; i++) {
            int start = Integer.parseInt(starts[i]);
            int end = Integer.parseInt(ends[i]);
            GeneInterval gi = new GeneInterval(chrom, start, end);
            addExon(gi);
        }

    }

    public void addExon(GeneInterval added) {
        m_Exons.add(added);
        if(m_Chromosome == null)
            m_Chromosome = added.getChromosome();
        m_Start = Math.min(added.getStart(),m_Start);
    }

    public GeneInterval[] getExons() {
        return m_Exons.toArray(GeneInterval.EMPTY_ARRAY);
    }

    public GeneInterval getExon(GeneLocation loc) {
         for(GeneInterval test : m_Exons)   {
             if(test.isWithin(loc))
                 return test;
         }
        return null;
    }

    public GeneLocation getReadFrameStart(GeneLocation loc) {
         GeneInterval gi = getExon(  loc);
        if(gi == null)
            return null;
        int start = gi.getStart();
        while(start < loc.getLocation() - 3)
            start++;

        return new GeneLocation(loc.getChromosome(),start);
    }

    public Codon getCodon(String chronosomeData,GeneLocation loc)   {
        throw new UnsupportedOperationException("Fix This"); // ToDo
    }

    public String getName() {
        return m_Name;
    }

    public String getProteinId() {
        return m_ProteinId;
    }

    public String getChromosome() {
        return m_Chromosome;
    }

    public int getStart() {
        return m_Start;
    }

    /**
     * sort first bu chromosome order
     * @param o
     * @return
     */
    @Override
    public int compareTo(KnownGene o) {
        if(this == o)
            return 0;
        int ret = getChromosome().compareTo(o.getChromosome());
        if(ret != 0)
            return ret;
        int start = getStart();
        int ostart = o.getStart();
          if(start != ostart)
              return start < ostart ? -1 : 1;
        return getName().compareTo(o.getName());
    }

    public static void main(String[] args) {
        String[] lines = GeneUtilities.readInLines(args[0]);
        KnownGene[] knownGenes = GeneUtilities.knownGeneFromLines(lines);
        for (int i = 0; i < knownGenes.length; i++) {
            KnownGene knownGene = knownGenes[i];
            
        }

    }
}
