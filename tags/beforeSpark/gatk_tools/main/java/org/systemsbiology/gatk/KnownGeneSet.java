package org.systemsbiology.gatk;

import java.io.*;
import java.util.*;

/**
 * org.systemsbiology.gatk.KnownGeneSet
 * User: steven
 * Date: 6/18/12
 */
public class KnownGeneSet {
    public static final KnownGeneSet[] EMPTY_ARRAY = {};

    private Map<String,KnownGene> m_ProteinToGenes = new HashMap<String, KnownGene>();
    private Map<String,List<KnownGene>> m_ChromosomeToGenes = new HashMap<String,List<KnownGene>>();

    public void addKnownGene(KnownGene gene) {
        String chromosome = gene.getChromosome();
        List<KnownGene> knownGenes = m_ChromosomeToGenes.get(chromosome);
        if(knownGenes == null)  {
             knownGenes = new ArrayList<KnownGene>();
             m_ChromosomeToGenes.put(chromosome, knownGenes);
        }
        knownGenes.add(gene);
        String proteinId = gene.getProteinId();
        m_ProteinToGenes.put(proteinId, gene);
    }

    public KnownGene getGene(String protein)
    {
        return m_ProteinToGenes.get(protein);
    }


    public KnownGene getGene(GeneLocation loc)
    {
        String chromosome = loc.getChromosome();

        List<KnownGene> knownGenes = m_ChromosomeToGenes.get(chromosome);
        if(knownGenes == null)
            return null;
        for (KnownGene knownGene : knownGenes) {
              if(knownGene.containsLocation(loc))
                  return knownGene;
        }
        return null;
    }


    public KnownGene[] getGenes(GeneLocation loc) {
        List<KnownGene> holder = new ArrayList<KnownGene>();
        String chromosome = loc.getChromosome();

        List<KnownGene> knownGenes = m_ChromosomeToGenes.get(chromosome);
        if(knownGenes == null)
            return null;
        for (KnownGene knownGene : knownGenes) {
              if(knownGene.containsLocation(loc))
                  holder.add(knownGene);
        }

        KnownGene[] ret = new KnownGene[holder.size()];
        holder.toArray(ret);
        return ret;
    }


    public static void main(String[] args) {

        File f = new File(args[1]);
        InterectingSNP[] snps = InterectingSNP.readFromCSV(f);
         
        KnownGeneSet genes = new KnownGeneSet();
        String[] lines = GeneUtilities.readInLines(args[0]);
        KnownGene[] knownGenes = GeneUtilities.knownGeneFromLines(lines);
        for (int i = 0; i < knownGenes.length; i++) {
            KnownGene knownGene = knownGenes[i];
            genes.addKnownGene(knownGene);
        }
         for (int i = 0; i < snps.length; i++) {
            InterectingSNP snp = snps[i];
             GeneLocation loc = snp.getLoc();
             String uniprotId = snp.getUniprotId();
             KnownGene gene = genes.getGene(uniprotId);
             KnownGene[] gene2 = genes.getGenes(loc);
            if(gene != null)  {
                GeneInterval[] exons = gene.getExons();
                StringBuilder sb = new StringBuilder();

                for (int j = 0; j < exons.length; j++) {
                    GeneInterval exon = exons[j];
                    String exs = GeneUtilities.locationToGenome(exon);
                    sb.append(exs);
                }
                String genome = sb.toString();
                String sequence = GeneUtilities.genomeToAminoAcid(  genome);
                System.out.println(sequence);
            }
            else {
               gene = genes.getGene(loc);
            }
        }

    }
}
