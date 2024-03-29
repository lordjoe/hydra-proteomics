package org.systemsbiology.gatk;


import net.sf.samtools.*;

import java.io.*;
import java.util.*;

/**
 * org.systemsbiology.gatk.SamToolsRunner
 * User: Steve
 * Date: 6/1/12
 */
public class SamToolsRunner {
    public static final SamToolsRunner[] EMPTY_ARRAY = {};


    public static File[] includeFilesWithEnding(File[] in, String ending) {
        List<File> holder = new ArrayList<File>();
        for (int i = 0; i < in.length; i++) {
            File file = in[i];
            if (!file.getName().endsWith(ending))
                continue;
            holder.add(file);
        }
        File[] ret = new File[holder.size()];
        holder.toArray(ret);
        return ret;
    }


    public static File[] excludeFilesWithEnding(File[] in, String ending) {
        List<File> holder = new ArrayList<File>();
        for (int i = 0; i < in.length; i++) {
            File file = in[i];
            if (file.getName().endsWith(ending))
                continue;
            holder.add(file);
        }
        File[] ret = new File[holder.size()];
        holder.toArray(ret);
        return ret;
    }

    public static File[] excludeFilesWithoutEnding(File[] in, String ending) {
        List<File> holder = new ArrayList<File>();
        for (int i = 0; i < in.length; i++) {
            File file = in[i];
            if (!file.getName().endsWith(ending))
                continue;
            holder.add(file);
        }
        File[] ret = new File[holder.size()];
        holder.toArray(ret);
        return ret;
    }

    public static File[] excludeFilesWithStart(File[] in, String ending) {
        List<File> holder = new ArrayList<File>();
        for (int i = 0; i < in.length; i++) {
            File file = in[i];
            if (file.getName().startsWith(ending))
                continue;
            holder.add(file);
        }
        File[] ret = new File[holder.size()];
        holder.toArray(ret);
        return ret;
    }

    public static File[] excludeFilesWithoutStart(File[] in, String ending) {
        List<File> holder = new ArrayList<File>();
        for (int i = 0; i < in.length; i++) {
            File file = in[i];
            if (!file.getName().startsWith(ending))
                continue;
            holder.add(file);
        }
        File[] ret = new File[holder.size()];
        holder.toArray(ret);
        return ret;
    }

    public static File[] getFilesWithExtension(File dir, String extension) {
        List<File> holder = new ArrayList<File>();
        String realExtension = "." + extension;
        addFilesWithRealExtension(holder, dir, realExtension);

        File[] ret = new File[holder.size()];
        holder.toArray(ret);
        return ret;
    }

    private static void addFilesWithRealExtension(final List<File> holder, final File dir, final String realExtension) {
        File[] test = dir.listFiles();
        if (test == null)
            return;
        for (int i = 0; i < test.length; i++) {
            File file = test[i];
            if (file.isFile()) {
                String name = file.getName();
                if (name.endsWith(realExtension)) {
                    holder.add(file);
                    continue;
                }
            }
            if (file.isDirectory())
                addFilesWithRealExtension(holder, file, realExtension);
        }

    }

    public static void addInputArguments(List<String> holder, File in) {
        holder.add("I=" + in.getAbsolutePath() + "");
    }


//    public static void addIOutputArgumentsX(List<String> holder, File in, String addedExtension) {
//        File out = addedExtension(in, addedExtension);
//        holder.add("O=" + out.getAbsolutePath());
//    }

    private static File addedExtensionX(final File in, final String addedExtension) {
        String name = in.getName();
        int index = name.lastIndexOf(".");
        String newName = name.substring(0, index) + "." + addedExtension + name.substring(index);
        if (in.getParentFile() != null)
            return new File(in.getParentFile(), newName);
        else
            return new File(newName);
    }


    private static File buildTmp(final File in) {
        File inParent = in.getParentFile();
        if (inParent == null)
            return new File("tmp.bam");
        File file = new File(inParent, "tmp.bam");
        return file;
    }

    private static File buildIndex(final File in) {
        File inParent = in.getParentFile();
        String indexName = in.getName().replace(".bam",".bai");
        if (inParent == null)
            return new File(indexName);
        File file = new File(inParent, indexName);
        return file;
    }

    /**
     * rename tmp to to in then kill in
     *
     * @param in
     * @return in - now the contents of tmp
     */
    private static File handleTmpRename(final File in) {
        if (!in.exists())
          throw new IllegalStateException("in file not found");
        File index = buildIndex(in);
        if (!index.exists())
           throw new IllegalStateException(" index file not found");
        File tmp = buildTmp(in);
        File tmpindex = buildIndex(tmp);
          if (!tmp.exists())
           throw new IllegalStateException("tmp file not found");
        if (!tmpindex.exists())
           throw new IllegalStateException("tmp index file not found");


        File inParent = in.getParentFile();
        File rename = new File(  "tmpSaver.bam");
        File renameIndex= new File(  "tmpSaver.bai");
         if (inParent != null)  {
             rename = new File(inParent, "tmpSaver.bam");
             renameIndex = new File(inParent, "tmpSaver.bai");
           }
        rename.delete();
        renameIndex.delete();

        if(!index.renameTo(renameIndex)) // rename index first it will be problematic
              throw new IllegalStateException("cannot rename " + index);
        if(!in.renameTo(rename))
             throw new IllegalStateException("cannot rename " + in);

        if(!tmp.renameTo(in))
                throw new IllegalStateException("cannot rename " + tmp);
        if(! tmpindex.renameTo(index))
                throw new IllegalStateException("cannot rename " + tmpindex);


        rename.delete();
        renameIndex.delete();

        return in;
    }

    public static void addInputAndOutputArguments(List<String> holder, File in) { //}, String addedExtension) {
        addInputArguments(holder, in);
        //       addIOutputArguments(holder, in, addedExtension);
        File out = buildTmp(in);
        holder.add("O=" + out.getAbsolutePath());
    }

    private static File runReorderSam(final File in) {
        try {
            Class cls = Class.forName("net.sf.picard.sam.ReorderSam");
            MainRunner mr = new MainRunner(cls);
            String addedExtension = "Reordered";
            File output = buildTmp(in); // addedExtension(in, addedExtension);
            String[] Mainargs = buildReorderSamArguments(in, addedExtension);
            mr.runMain(Mainargs);
            output = handleTmpRename(in);

            return in;
        }
        catch (ClassNotFoundException e) {
            throw new RuntimeException(e);

        }
    }

    public static String[] buildReorderSamArguments(File in, String addedExtension) {
        List<String> holder = new ArrayList<String>();
        addInputAndOutputArguments(holder, in); //, addedExtension);
        String name = in.getName();
        String fastaFile = "e:/resources/hg19.fa";   // todo make generic
        if (name.toLowerCase().contains("mouse"))
            fastaFile = "e:/resources/mmu9.fa";   // todo make generic

        holder.add("REFERENCE=" + fastaFile);
//        @Option(shortName="S", doc="If true, then allows only a partial overlap of the BAM contigs with the new reference " +
//                                    "sequence contigs.  By default, this tool requires a corresponding contig in the new " +
//                                    "reference for each read contig")
        holder.add("S=true");
//        @Option(shortName="U", doc="If true, then permits mapping from a read contig to a new reference contig with the " +
//                                   "same name but a different length.  Highly dangerous, only use if you know what you " +
//                                   "are doing.")
        holder.add("U=true");
        holder.add("CREATE_INDEX=true");
        holder.add("VALIDATION_STRINGENCY=SILENT");

        String[] ret = new String[holder.size()];
        holder.toArray(ret);
        return ret;
    }


    private static File runSortSam(final File in) {
        try {
            Class cls = Class.forName("net.sf.picard.sam.SortSam");
            MainRunner mr = new MainRunner(cls);
            String addedExtension = "sortedByPos";
            if(in.getName().contains(addedExtension))
                return in;
            String[] Mainargs = buildSortSamArguments(in, addedExtension);
            mr.runMain(Mainargs);
            File output = handleTmpRename(in);
            return output;
        }
        catch (ClassNotFoundException e) {
            throw new RuntimeException(e);

        }
    }

    public static String[] buildSortSamArguments(File in, String addedExtension) {
        List<String> holder = new ArrayList<String>();
        addInputAndOutputArguments(holder, in); //, addedExtension);
        holder.add("SO=coordinate");
        holder.add("VALIDATION_STRINGENCY=SILENT");

        String[] ret = new String[holder.size()];
        holder.toArray(ret);
        return ret;
    }

    private static File runCleanSam(final File in) {
        try {
            Class cls = Class.forName("net.sf.picard.sam.CleanSam");
            MainRunner mr = new MainRunner(cls);
            String addedExtension = "Cleaned";
            String[] Mainargs = buildCleanSamArguments(in, addedExtension);
            mr.runMain(Mainargs);
            File output = handleTmpRename(in);
            return output;
        }
        catch (ClassNotFoundException e) {
            throw new RuntimeException(e);

        }
    }

    public static String[] buildCleanSamArguments(File in, String addedExtension) {
        List<String> holder = new ArrayList<String>();
        addInputAndOutputArguments(holder, in); //, addedExtension);
        holder.add("CREATE_INDEX=true");
        holder.add("VALIDATION_STRINGENCY=SILENT");

        String[] ret = new String[holder.size()];
        holder.toArray(ret);
        return ret;
    }

    private static File runAddOrReplaceReadGroups(final File in, final String id) {
        try {
            Class cls = Class.forName("net.sf.picard.sam.AddOrReplaceReadGroups");
            MainRunner mr = new MainRunner(cls);
            String addedExtension = "Grouped";
            String[] Mainargs = buildAddReadGroupArguments(in, addedExtension, id);
            mr.runMain(Mainargs);
            File output = handleTmpRename(in);
            return output;
        }
        catch (ClassNotFoundException e) {
            throw new RuntimeException(e);

        }
    }

    public static String[] buildAddReadGroupArguments(File in, String addedExtension, String id) {
        List<String> holder = new ArrayList<String>();
        addInputAndOutputArguments(holder, in); //, addedExtension);


        holder.add("SORT_ORDER=coordinate");
        holder.add("VALIDATION_STRINGENCY=SILENT");
        holder.add("CREATE_INDEX=true");
        holder.add("RGPL=illumina");
        holder.add("RGLB=bar");
        holder.add("RGID=" + id);
        holder.add("RGPU=pu" + id);
        holder.add("RGSM=sample" + id);

        String[] ret = new String[holder.size()];
        holder.toArray(ret);
        return ret;
    }


    protected static File[] sortFiles(final File[] candidates) {
        List<File> outputs1 = new ArrayList<File>();
        for (int i = 0; i < candidates.length; i++) {
            File candidate = candidates[i];
            File output = runSortSam(candidate);
            outputs1.add(output);
        }
        File[] out = new File[outputs1.size()];
        outputs1.toArray(out);
        return out;
    }


    protected static File[] findVariants(final File[] candidates, final File intervals) {
        List<File> outputs = new ArrayList<File>();
        for (int i = 0; i < candidates.length; i++) {
            File candidate = candidates[i];
            File output = runFindVariants(candidate, intervals);
            outputs.add(output);
        }
        File[] out = new File[outputs.size()];
        outputs.toArray(out);
        return out;
    }

    public static String[] buildCountLocusArguments(File in, SAMFileWriter out, InterestingVariation[] genes, File intervals) {
        List<String> holder = new ArrayList<String>();

        CopyInterestingReadsWalker.setLocations(genes);
        CopyInterestingReadsWalker.setWriter(out);

        holder.add("-I");
        holder.add(in.getAbsolutePath());

        holder.add("-R");
        holder.add(GeneUtilities.getReferenceFile().getAbsolutePath());
        holder.add("-T");
        holder.add("CopyInterestingReads");
        holder.add("-L");
        holder.add(intervals.getAbsolutePath());

        String[] ret = new String[holder.size()];
        holder.toArray(ret);
        return ret;
    }


    public static void runCountLoci(final File in, SAMFileWriter out, InterestingVariation[] genes, File intervals) {
        GATKRunner mr = new GATKRunner();
        String[] Mainargs = buildCountLocusArguments(in, out, genes, intervals);
        mr.runMain(Mainargs);
    }


    public static void runFindInMouse(final File in, InterestingVariation[] vars, Map<String, SAMRecord> interestingRecords, Map<String, SAMRecord> allRecords) {
        GATKRunner mr = new GATKRunner();
        String[] Mainargs = buildFindInMouse(in, vars, interestingRecords, allRecords);
        mr.runMain(Mainargs);
    }


    public static String[] buildFindInMouse(File in, InterestingVariation[] vars, Map<String, SAMRecord> interestingRecords, Map<String, SAMRecord> allRecords) {
        List<String> holder = new ArrayList<String>();

        FindInAlternateSpeciesWalker.setInterestingReads(interestingRecords.keySet());
        holder.add("-I");
        holder.add(in.getAbsolutePath());

        holder.add("-R");
        String absolutePath = GeneUtilities.getReferenceFile().getAbsolutePath();
        holder.add(absolutePath);
        holder.add("-T");
        holder.add("FindInAlternateSpecies");
        holder.add("-U");
        holder.add("ALLOW_SEQ_DICT_INCOMPATIBILITY");

        String[] ret = new String[holder.size()];
        holder.toArray(ret);
        return ret;
    }


    public static Map<String, SAMRecord> readSamFile(final File samFile) {
        Map<String, SAMRecord> ret = new HashMap<String, SAMRecord>();
        SAMFileReader rdr = new SAMFileReader(samFile);
        rdr.setValidationStringency(SAMFileReader.ValidationStringency.SILENT);
        SAMRecordIterator iterator = rdr.iterator();
        while (iterator.hasNext()) {
            SAMRecord next = iterator.next();
            String readString = next.getReadString();
            ret.put(readString, next);
        }
        return ret;
    }

    public static String[] buildReadSamFileArguments(File in) {
        List<String> holder = new ArrayList<String>();


        holder.add("-I");
        holder.add(in.getAbsolutePath());

        holder.add("-R");
        holder.add(GeneUtilities.getReferenceFile().getAbsolutePath());
        holder.add("-T");
        holder.add("GatherReads");

        String[] ret = new String[holder.size()];
        holder.toArray(ret);
        return ret;
    }


    private static File runFindVariants(final File in, final File intervals) {
        GATKRunner mr = new GATKRunner();
        String addedExtension = "Grouped";

        String fileName = in.getName();
        fileName = fileName.substring(0, fileName.indexOf(".")) + ".vcf";
        File out = new File(fileName);
        if (in.getParentFile() != null)
            out = new File(in.getParentFile(), fileName);

        String[] Mainargs = buildFindVariantsArguments(in, out, intervals);
        mr.runMain(Mainargs);
        return out;
    }

    public static String[] buildFindVariantsArguments(File in, File out, File intervals) {
        List<String> holder = new ArrayList<String>();

        holder.add("-I");
        holder.add(in.getAbsolutePath());

        holder.add("-o");
        holder.add(out.getAbsolutePath());
        holder.add("-l");
        holder.add("INFO");
        holder.add("--genotype_likelihoods_model");
        holder.add("BOTH");
        holder.add("-R");
        holder.add(GeneUtilities.getReferenceFile().getAbsolutePath());
        holder.add("-U");
        holder.add("ALLOW_SEQ_DICT_INCOMPATIBILITY");
        holder.add("-T");
        holder.add("UnifiedGenotyper");
        holder.add("--output_mode");
        holder.add("EMIT_VARIANTS_ONLY");
        holder.add("-stand_call_conf");
        holder.add(GATKRunner.DEFAULT_MIMIMUM_SCORE);
        holder.add("-stand_emit_conf");
        holder.add(GATKRunner.DEFAULT_MIMIMUM_SCORE);
        holder.add("-mbq");
        holder.add(Integer.toString(GATKRunner.DEFAULT_MINIMUM_BASE_QUALITY_SCORE));
        holder.add("-nt");
        holder.add(Integer.toString(GATKRunner.DEFAULT_NUMBER_THREADS));
        holder.add("-L");
        holder.add(intervals.getAbsolutePath());

        String[] ret = new String[holder.size()];
        holder.toArray(ret);
        return ret;
    }


    protected static File[] addReadGroups(final File[] candidates) {
        List<File> outputs = new ArrayList<File>();
        for (int i = 0; i < candidates.length; i++) {
            File candidate = candidates[i];
            File parentFile = candidate.getParentFile();
            String id = "1";
            if (parentFile != null && !".".equals(parentFile.getName()))
                id = parentFile.getName();
            File output = runAddOrReplaceReadGroups(candidate, id);
            outputs.add(output);
        }
        File[] out = new File[outputs.size()];
        outputs.toArray(out);
        return out;
    }


    protected static File[] reorderFiles(final File[] candidates) {
        List<File> outputs = new ArrayList<File>();
        for (int i = 0; i < candidates.length; i++) {
            File candidate = candidates[i];
            File output = runReorderSam(candidate);
            outputs.add(output);
        }
        File[] out = new File[outputs.size()];
        outputs.toArray(out);
        return out;
    }

    protected static File[] cleanFiles(final File[] candidates) {
        List<File> outputs = new ArrayList<File>();
        for (int i = 0; i < candidates.length; i++) {
            File candidate = candidates[i];
            File output = runCleanSam(candidate);
            outputs.add(output);
        }
        File[] out = new File[outputs.size()];
        outputs.toArray(out);
        return out;
    }

    protected static File[] fixBamFiles(File[] candidates) {
        candidates = addReadGroups(candidates);
        candidates = reorderFiles(candidates);
        return candidates;
    }



    public static File[] runBamFixPipeline(File[] candidates) {
        candidates = sortFiles(candidates);

        candidates = reorderFiles(candidates);
        candidates = fixBamFiles(candidates);
        candidates = cleanFiles(candidates);
        return candidates;
    }


    public static void main(String[] args) throws Exception {
        File dir = new File(System.getProperty("user.dir"));
        dir =  dir.getAbsoluteFile();
        File[] candidates = getFilesWithExtension(dir, "bam");
        candidates = excludeFilesWithoutEnding(candidates, ".sortedByPos.bam");
        candidates = excludeFilesWithStart(candidates, "HCV");
//        candidates = excludeFilesWithoutStart(candidates, "human");
//        candidates = excludeFilesWithEnding(candidates, "Grouped.Sorted.bam");
//        candidates = excludeFilesWithEnding(candidates, "Sorted.Grouped.Sorted.Grouped");
        candidates = excludeFilesWithoutStart(candidates, "mouse");

        candidates = runBamFixPipeline(candidates);

//        candidates = findVariants(candidates, new File("LiverGenes.intervals"));
//        for (int i = 0; i < candidates.length; i++) {
//            File candidate = candidates[i];
//
//        }
    }


}
