package org.systemsbiology.gatk;

import com.sun.jndi.toolkit.url.*;
import net.sf.picard.reference.*;
import net.sf.samtools.*;
import org.broadinstitute.sting.gatk.contexts.*;
import org.broadinstitute.sting.gatk.datasources.reference.*;
import org.broadinstitute.sting.utils.*;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.zip.*;

/**
 * PACKAGE_NAME.org.systemsbiology.gatk.GeneUtilities
 * some useful static methods
 * User: Steve
 * Date: 6/14/12
 */
public class GeneUtilities {
    public static final GeneUtilities[] EMPTY_ARRAY = {};


    private static File gReferenceFile;
    private static SAMSequenceDictionary gReferenceDictionary;

    public static File getReferenceFile() {
        return gReferenceFile;
    }

    public static void setReferenceFile(File refFile) {
        ReferenceDataSource rds = new ReferenceDataSource(refFile);
        IndexedFastaSequenceFile reference = rds.getReference();
        SAMSequenceDictionary sd = reference.getSequenceDictionary();
        setReferenceDictionary(sd);
        gReferenceFile = refFile;
    }

    public static SAMSequenceDictionary getReferenceDictionary() {
        return gReferenceDictionary;
    }

    private static void setReferenceDictionary(SAMSequenceDictionary referenceDictionary) {
        gReferenceDictionary = referenceDictionary;
    }

    public static void guaranteeHeader(SAMFileWriter writer, ReferenceContext ref) {
        SAMFileHeader fileHeader = writer.getFileHeader();
        SAMSequenceDictionary sd = fileHeader.getSequenceDictionary();
        if (sd.getSequences().isEmpty()) {
            GenomeLocParser gp = ref.getGenomeLocParser();
        }
    }

    /**
     * { method
     *
     * @param FileName the file name
     * @return - array or strings - null if file empty or does not exist
     *         }
     * @name readInLines
     * @function reads all the data in a file into an array of strings - one per line
     */
    public static String[] readInLines(String TheFile) {
        return readInLines(new File(TheFile));
    }

    public static String[] readInLines(File TheFile) {
        try {
            if(TheFile.getName().endsWith(".gz"))
                return readInLines(new InputStreamReader(new GZIPInputStream(new FileInputStream(TheFile))));
              else
                return readInLines(new FileReader(TheFile));
        }
        catch (FileNotFoundException e) {
            throw new RuntimeException(e);

        }
        catch (IOException e) {
            throw new RuntimeException(e);

        }
    }


    public static String extractQuotedText(String xml, String beforeQuote) {
        int loc = xml.indexOf(beforeQuote);
        if (loc == -1)
            return null;
        loc++;
        int end = xml.indexOf("\"", loc + 1);
        return xml.substring(loc, end);
    }


    public static String[] extractQuotedTextItems(String xml, String beforeQuote) {
        List<String> holder = new ArrayList<String>();
        int end = 0;
        int loc = xml.indexOf(beforeQuote, end);
        while (loc != -1) {
            loc++;
            end = xml.indexOf("\"", loc + 1);
            if (end > -1)
                holder.add(xml.substring(loc, end));
        }
        String[] ret = new String[holder.size()];
        holder.toArray(ret);
        return ret;
    }


    public static String[] readInLines(URL url) {
        InputStream inp = null;
        try {
            inp = url.openStream();
            return readInLines(inp);
        }
        catch (IOException e) {
            throw new RuntimeException(e);

        }
        finally {
            if (inp != null) {
                try {
                    inp.close();
                }
                catch (IOException e) {

                }
            }
        }
    }


    public static String[] readInLines(Reader in) {
        try {
            LineNumberReader r = new LineNumberReader(in);
            List<String> holder = new ArrayList<String>();
            String s = r.readLine();
            while (s != null) {
                holder.add(s);
                s = r.readLine();
            }
            String[] ret = holder.toArray(new String[0]);
            r.close();
            return (ret);
        }
        catch (IOException ex) {
            throw new IllegalStateException(ex.getMessage());
        }
    }

    public static String[] readInLines(InputStream inp) {
        return readInLines(new InputStreamReader(inp));
    }


    public static GeneVariant[] variantsFromLines(String[] lines) {
        List<GeneVariant> holder = new ArrayList<GeneVariant>();
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            GeneVariant e = variantFromLine(line);
            if (e != null)
                holder.add(e);
        }
        GeneVariant[] ret = new GeneVariant[holder.size()];
        holder.toArray(ret);
        return ret;
    }

    public static GeneVariant variantFromLine(String line) {
        if (line.startsWith("#"))
            return null;
        String[] items = line.split("\t");
        String chromosome = items[0];
        long start = Long.parseLong(items[1]);
        GeneLocation loc = new GeneLocation(chromosome, start);
        String original = items[3];
        String changedTo = items[4];
        double score = Double.parseDouble(items[5]);
        String annotation = items[7];
        if (original.length() == 1) {
            if (changedTo.length() == 1) {
                return new SNPVariation(loc, score, DNABase.valueOf(original), DNABase.valueOf(changedTo), annotation);
            }
            else {
                if (original.contains(",")) //deal with  	A,AT
                    original = changedTo.substring(0, 1);
                return new InsertionVariation(loc, score, DNABase.valueOf(original), DNABase.fromString(changedTo), annotation);
            }
        }
        else {
            if (changedTo.contains(",")) //deal with  ATT	A,AT
                changedTo = changedTo.substring(0, 1);
            return new DeletionVariation(loc, score, DNABase.fromString(original), DNABase.valueOf(changedTo), annotation);
        }

    }


    public static MapToGeneRegion readGeneMap(final String geneFile) {
        return readGeneMap(new File(geneFile));
    }

    /**
     * fins all varaints common in the list
     *
     * @param samples
     * @param numberCases
     * @return
     */
    public static GeneVariant[] commonVariants(GeneVariant[] samples, int numberCases) {
        Arrays.sort(samples);
        if (numberCases == 1)
            return samples;
        //     System.out.println("Looking for common in " + samples.length);
        int commonCount = 0;
        if (samples.length == 0)
            return GeneVariant.EMPTY_ARRAY;
        GeneVariant lastSample = null;
        List<GeneVariant> holder = new ArrayList<GeneVariant>();
        for (int i = 0; i < samples.length; i++) {
            GeneVariant sample = samples[i];
            if (lastSample != null && lastSample.equivalent(sample)) {
                commonCount++;
                if (commonCount >= numberCases) {
                    holder.add(lastSample);
                    lastSample = null;
                }
            }
            else {
                commonCount = 1;
                lastSample = sample.asSample();
            }
        }

        GeneVariant[] ret = new GeneVariant[holder.size()];
        holder.toArray(ret);
        return ret;
    }

    public static MapToGeneRegion readGeneMap(final File geneFile) {
        String[] lines = GeneUtilities.readInLines(geneFile);
        GeneRegion[] regions = regionsFromLines(lines);
        return new MapToGeneRegion(regions);
    }


    public static GeneRegion[] regionsFromLines(String[] lines) {
        List<GeneRegion> holder = new ArrayList<GeneRegion>();
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            holder.add(regionFromLine(line));
        }
        GeneRegion[] ret = new GeneRegion[holder.size()];
        holder.toArray(ret);
        return ret;
    }


    /**
     * linf like THBS1	chr15:39873279-39889668
     *
     * @param line
     * @return
     */
    public static GeneRegion regionFromLine(String line) {
        String[] items = line.split("\t");
        String gene = items[0];
        return new GeneRegion(items[1], items[0]);
    }

    public static String genomeToAminoAcid(String genome)
    {
        StringBuilder sb = new StringBuilder();

         for(int i = 0; i < genome.length() - 2; i += 3)   {
            String cdn = genome.substring(i,i + 3);
            Codon codon = Codon.valueOf(cdn);
            String aa = codon.getAminoAcid();
            if(aa == null)
                 aa = codon.getAminoAcid(); // break here
             sb.append(aa);
        }
        return sb.toString();
     }

    public static KnownGene[] knownGeneFromLines(String[] lines) {
        List<KnownGene> holder = new ArrayList<KnownGene>();
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i].trim();
            if (line.startsWith("#"))
                continue;
            if (line.length() == 0)
                continue;
            KnownGene gene = new KnownGene(line);
            holder.add(gene);
        }
        KnownGene[] ret = new KnownGene[holder.size()];
        holder.toArray(ret);
        return ret;
    }

    public static String locationToGenome(GeneInterval loc) {
        String fullGenome = chromosomeToGenome( loc.getChromosome());
        String me = fullGenome.substring(loc.getStart(),loc.getEnd());
        StringBuilder sb = new StringBuilder(me); // force creat new string so large buffer not held
        return sb.toString();

    }

    private static final Map<String, String> gLastGenome = new HashMap<String, String>();

    public static String chromosomeToGenome(String chromosome) {
        String ret = gLastGenome.get(chromosome);
        if (ret != null)
            return ret;
        ret = readChromosome(chromosome);
        gLastGenome.clear();
        gLastGenome.put(chromosome, ret);
        return ret;

    }

    public static final String RESOURCE_LOC = "e:/resources/";

    private static String readChromosome(String chromosome) {
        File f = new File (RESOURCE_LOC  + chromosome + ".fa.gz");
        String[] strings = GeneUtilities.readInLines(f);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < strings.length; i++) {
            String string = strings[i];
            if(string.startsWith(">"))
                continue;
            sb.append(string);
        }
        return sb.toString();
     }
}
