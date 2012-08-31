package org.systemsbiology.igv;

import org.broad.igv.feature.*;
import org.broad.igv.feature.genome.*;
import org.broad.igv.ui.util.*;

import java.io.*;

/**
 * org.systemsbiology.igv.VariantMapper
 * User: steven
 * Date: 8/31/12
 */
public class VariantMapper {
    public static final VariantMapper[] EMPTY_ARRAY = {};

    private final Genome m_Genome;

    public VariantMapper(Genome genome) {
        m_Genome = genome;
    }

    public Genome getGenome() {
        return m_Genome;
    }

    public Chromosome getChromosome(String name)
    {
        Genome g = getGenome();
        return g.getChromosome(name);
    }

    public static final String HUMAN_GENOME_PATH = "http://igvdata.broadinstitute.org/genomes/hg19.genome";

    public static void main(String[] args) {
        String path = HUMAN_GENOME_PATH;
        ProgressMonitor pm = null;
        try {
            Genome genome = GenomeManager.getInstance().loadGenome(path, pm);
            VariantMapper vm = new VariantMapper( genome);

        }
        catch (IOException e) {
            throw new RuntimeException(e);

        }
    }
}
