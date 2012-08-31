package org.systemsbiology.igv;

import org.broad.igv.*;
import org.broad.igv.feature.*;
import org.broad.igv.feature.genome.*;
import org.broad.igv.ui.util.*;
import org.broad.igv.util.*;
import org.broad.tribble.*;

import java.io.*;
import java.util.*;

/**
 * org.systemsbiology.igv.VariantMapper
 * User: steven
 * Date: 8/31/12
 */
public class VariantMapper {
    public static final VariantMapper[] EMPTY_ARRAY = {};

    public static final String HUMAN_GENOME_PATH = "http://igvdata.broadinstitute.org/genomes/hg19.genome";
    private final String m_GenomePath;
    private final Genome m_Genome;
    private final Map<String, BasicFeature> m_IdToGene = new HashMap<String, BasicFeature>();
    private final Map<String, BasicFeature> m_NameToGenes = new HashMap<String, BasicFeature>();

    public VariantMapper(String genomePath) {
        m_GenomePath = genomePath;
        try {
            ProgressMonitor pm = null;
            Genome genome = GenomeManager.getInstance().loadGenome(genomePath, pm);
            m_Genome = genome;
        }
        catch (IOException e) {
            throw new RuntimeException(e);

        }
    }

    public Genome getGenome() {
        return m_Genome;
    }

    public Chromosome getChromosome(String name) {
        Genome g = getGenome();
        return g.getChromosome(name);
    }

    private List<Feature> readGenes(GenomeDescriptor genomeDescriptor, File archiveFile) throws IOException {
        InputStream geneStream = null;
        String geneFileName = genomeDescriptor.getGeneFileName();
        Genome genome = getGenome();
        try {
            geneStream = genomeDescriptor.getGeneStream();
            BufferedReader reader = geneStream == null ? null : new BufferedReader(new InputStreamReader(geneStream));
            FeatureParser parser;
            if (GFFParser.isGFF(geneFileName)) {
                parser = new GFFParser(geneFileName);
            }
            else {
                parser = AbstractFeatureParser.getInstanceFor(new ResourceLocator(geneFileName), genome);
            }
            List<Feature> genes = parser.loadFeatures(reader, genome);
            return genes;
        }
        finally {
            if (geneStream != null) geneStream.close();
        }
    }

    public String getGenomePath() {
        return m_GenomePath;
    }

    public BasicFeature getFeature(String id) {
        return m_IdToGene.get(id);
    }

    public BasicFeature getFeatureByName(String id) {
        return m_NameToGenes.get(id);
    }

    public String[] getIds( ) {
        String[] ret = m_IdToGene.keySet().toArray(new String[0]);
        Arrays.sort(ret);
        return ret;
    }

    public List<Feature> loadGenome() {
        try {
            String genomePath = getGenomePath();
            GenomeManager gm = GenomeManager.getInstance();
            File archiveFile = gm.getArchiveFile(genomePath);
            GenomeDescriptor genomeDescriptor = gm.parseGenomeArchiveFile(archiveFile);
            List<Feature> genes = readGenes(genomeDescriptor, archiveFile);
            for (Feature gene : genes) {
                BasicFeature bf = (BasicFeature)gene;
                m_IdToGene.put(bf.getIdentifier(), bf);
                m_NameToGenes.put(bf.getName(), bf);
                 String chr = gene.getChr();
                int start = gene.getStart();
                gene.getEnd();
            }
            return genes;
        }
        catch (IOException e) {
            throw new RuntimeException(e);

        }

    }


    public static void main(String[] args) {
        String path = HUMAN_GENOME_PATH;
        Globals.setHeadless(true);
        VariantMapper vm = new VariantMapper(path);
        List<Feature> genes = vm.loadGenome();
        for (Feature gene : genes) {
            String chr = gene.getChr();
            int start = gene.getStart();
            gene.getEnd();
        }

    }

}

