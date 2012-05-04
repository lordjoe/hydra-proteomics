package org.systemsbiology.fasta;

import net.sf.picard.reference.*;
import org.systemsbiology.chromosome.*;
import org.systemsbiology.sam.*;

import java.io.*;
import java.util.*;

/**
 * org.systemsbiology.fasta.ChromosomeSequence
 * reads a chromosome sequence when the FAST files are present as a resource
 * TODO - makeit smart enough to handle compressed files
 * User: steven
 * Date: May 24, 2010
 */
public class ChromosomeSequence {
    public static final ChromosomeSequence[] EMPTY_ARRAY = {};
    public static final int ROW_LENGTH = 80;
    public static final int BITS_PER_BASE = 4;

    private static IFASTADataSource gDataSource;

    public static IFASTADataSource getDataSource() {
        return gDataSource;
    }

    public static void setDataSource(final IFASTADataSource pDataSource) {
        gDataSource = pDataSource;
    }

    private static final Map<IChromosome, ChromosomeSequence> gSequences = new HashMap<IChromosome, ChromosomeSequence>();

    public static ChromosomeSequence getSequence(IChromosome chr) {
        ChromosomeSequence ret = gSequences.get(chr);
        if (ret == null) {
            ret = new ChromosomeSequence(chr);
            gSequences.put(chr,ret);
        }
        return ret;
    }

    private IChromosome m_Chromosome;
    private FastaSequenceResource m_SequenceResource;
    private ReferenceSequence m_Sequence;

    private ChromosomeSequence(IChromosome chr) {
        m_Chromosome = chr;
    }

    public IChromosome getChromosome() {
        return m_Chromosome;
    }

//    protected void populate()
//    {
//         IFASTADataSource ds = getDataSource();
//        final InputStream fastaData = ds.getFASTAData(getChromosome());
//         LineNumberReader lr = new LineNumberReader(new InputStreamReader(new BufferedInputStream(fastaData)));
//        try {
//            String line = lr.readLine();
//            while(line != null)    {
//                if(line.startsWith(">"))
//                    continue;
//                 addLineRow(line);
//                 line = lr.readLine();
//            }
//            lr.close();
//        }
//        catch (IOException e) {
//             throw new RuntimeException(e);
//
//        }
//    }
//
//    protected void addLineRow(final String pLine) {
//        BitSet bs = new BitSet(pLine.length() * BITS_PER_BASE);
//        for (int i = 0; i < pLine.length(); i++) {
//            char c = pLine.charAt(i);
//            int org.systemsbiology.couldera.training.index = BITS_PER_BASE * 1;
//            switch(c) {
//                case 'a' :
//                case 'A' :
//                    bs.set(org.systemsbiology.couldera.training.index + 2,true);
//                    bs.set(org.systemsbiology.couldera.training.index + 3,true);
//                    bs.set(org.systemsbiology.couldera.training.index,false);
//                    bs.set(org.systemsbiology.couldera.training.index + 1,false);
//                    break;
//                case 'c' :
//                case 'C' :
//                    bs.set(org.systemsbiology.couldera.training.index + 2,true);
//                    bs.set(org.systemsbiology.couldera.training.index + 3,true);
//                    bs.set(org.systemsbiology.couldera.training.index,true);
//                    bs.set(org.systemsbiology.couldera.training.index + 1,false);
//                    break;
//                case 'g' :
//                case 'G' :
//                    bs.set(org.systemsbiology.couldera.training.index + 2,true);
//                    bs.set(org.systemsbiology.couldera.training.index + 3,true);
//                    bs.set(org.systemsbiology.couldera.training.index,false);
//                    bs.set(org.systemsbiology.couldera.training.index + 1,true);
//                    break;
//                case 't' :
//                case 'T' :
//                    bs.set(org.systemsbiology.couldera.training.index + 2,true);
//                    bs.set(org.systemsbiology.couldera.training.index + 3,true);
//                    bs.set(org.systemsbiology.couldera.training.index,true);
//                    bs.set(org.systemsbiology.couldera.training.index + 1,true);
//                    break;
//                default:
//                    throw new UnsupportedOperationException("Unknown FASTA character"); // ToDo
//               }
//
//       }
//    }


    public boolean hasSequence(String resource) {
        final Class<? extends ChromosomeSequence> aClass = getClass();
        final ClassLoader loader = aClass.getClassLoader();
        //     final InputStream in1 = loader.getResourceAsStream(s);
        final InputStream in1 = aClass.getResourceAsStream(resource);
        if (in1 == null)
            return false;
        return true;
    }


    public ReferenceSequence getSequence() {
        if (m_Sequence == null) {
            IFASTADataSource source = getDataSource();
            IChromosome iChromosome = getChromosome();
            final String s = source.getFASTAResource(iChromosome);
            if (hasSequence(s)) {
                m_SequenceResource = new FastaSequenceResource(s);
                m_Sequence = m_SequenceResource.nextSequence();
            }
        }
        return m_Sequence;
    }

    public DNABase getBase(int position) {
        final ReferenceSequence rs = getSequence();
        if(rs == null)
            return null;
        final byte[] bss = rs.getBases();
        if(position >= bss.length)
            return null;
        byte test = bss[position];
        switch (test) {
            case (byte) 'A':
                return DNABase.A;
            case (byte) 'C':
                return DNABase.C;
            case (byte) 'G':
                return DNABase.G;
            case (byte) 'T':
                return DNABase.T;
            default:
                return null;
        }
    }

    public DNABase[] getBases(int position, int length) {
        final ReferenceSequence rs = getSequence();
        if(rs == null)
            return null;
        DNABase[] ret = new DNABase[length];
        for (int i = 0; i < length; i++) {
            ret[i] = getBase(position + i);

        }
        return ret;
    }


    public static void main(String[] args) {
        DefaultChromosome.setDefaultChromosomeSet("Yeast");
        setDataSource(new ResourceDataSource("/genomes/sacCer2/"));
        final IChromosome[] chromosomes = DefaultChromosome.getDefaultChromosomeSet();
        final IChromosome ch0 = chromosomes[0];
        ChromosomeSequence seq = getSequence(ch0);
        for (int i = 0; i < 20; i++) {
            DNABase base = seq.getBase(i);

        }

        final DNABase[] bases = seq.getBases(20000, 100);
        String str = DNABase.asString(bases);
        System.out.println(str);
    }

}
