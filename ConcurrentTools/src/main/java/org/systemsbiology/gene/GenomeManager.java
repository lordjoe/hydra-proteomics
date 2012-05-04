package org.systemsbiology.gene;

import java.io.*;
import java.util.*;

/**
 * org.systemsbiology.gene.GenomeManager
 * written by Steve Lewis
 * on Apr 12, 2010
 */
public class GenomeManager
{
    public static final GenomeManager[] EMPTY_ARRAY = {};
    public static final Class THIS_CLASS = GenomeManager.class;

    private static File gGemonDirectory = new File("genomes");

    public static File getGemonDirectory() {
        return gGemonDirectory;
    }

    public static void setGemonDirectory(File pGemonDirectory) {
        gGemonDirectory = pGemonDirectory;
    }

    private static GenomeManager gInstance;

    public static synchronized GenomeManager getInstance() {
        if(gInstance == null)
            gInstance = new GenomeManager();
        return gInstance;
    }

    public static void setInstance(GenomeManager pInstance) {
        if(gInstance != null)
            throw new IllegalStateException("Manager can be set only once");
        gInstance = pInstance;
    }
    private final Map<ReferenceGenome,GeneSet> m_Genomes =
             new HashMap<ReferenceGenome,GeneSet>();

    private GenomeManager() {}

     public GeneSet getGeneSet(ReferenceGenome pGenome) {
         synchronized (m_Genomes) {
             GeneSet ret = m_Genomes.get(pGenome);
             if(ret == null)
                 ret = buildGeneSet(pGenome);
             return ret;
         }
     }

    protected GeneSet buildGeneSet(ReferenceGenome pGenome) {
        GeneSet ret = new GeneSet(pGenome);
        String s = pGenome.toString() + ".genome.gz";
        String s1 = "/genomes/" + s;
        InputStream asStream = getClass().getResourceAsStream(s1);
     //   File fil = new File(getGemonDirectory(), s);
     //   if(!fil.exists())
      //      throw new IllegalStateException("Gene File " + fil.getAbsolutePath() + " Does not exits") ;
        ret.loadFromStream(asStream);
        return ret;

    }

    protected void addGeneListing(GeneSet added) {
         m_Genomes.put(added.getGenome(),added);
      }

     public GeneSet findGenome(ReferenceGenome name)   {
         return m_Genomes.get(name);
     }

     public static final int NUMBER_TESTS = 32;
    protected static void testGeneSet(GeneSet gs)
    {
        for (int i = 0; i < NUMBER_TESTS; i++) {
            String test = CancerGenes.randomCancerGene();
            IGeneListing lst = gs.findByName(test);
            if(lst == null)  {
                System.out.println("Gene " + test + " not found");
            }
            else {
                System.out.println("Gene " + test + "  FOUND");

            }

        }
    }

    public static void main(String[] args) {
        GenomeManager gm = GenomeManager.getInstance();
        for(ReferenceGenome ref : ReferenceGenome.values())  {
            GeneSet gs = gm.getGeneSet(ref);
            testGeneSet( gs);
        }
    }


}
