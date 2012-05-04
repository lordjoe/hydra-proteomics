package org.systemsbiology.hadoop;

import com.lordjoe.utilities.*;
import net.sf.samtools.*;
import net.sf.samtools.util.*;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.filecache.*;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.fs.FileSystem;
 import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;


import java.io.*;
import java.util.*;

/**
 * org.systemsbiology.hadoop.HadoopUtilities
 * written by Steve Lewis
 * on May 4, 2010
 */
public class HadoopUtilities {
    public static final HadoopUtilities[] EMPTY_ARRAY = {};
    public static final Class THIS_CLASS = HadoopUtilities.class;


    public static final String GROUP_NAME = "Genome";

    public static final Text HEADER_KEY = new Text("<SAM HEADER>");

    public static final String DEFAULT_HEADER_TEXT = "@HD\tVN:1.0\tSO:unsorted";

    public static final String SYSBIO_KEY_BASE = "org.systemsbiology";
    public static final String CONFIGURATION_KEY = SYSBIO_KEY_BASE + ".configuration";
    public static final String INPUT_FILE_KEY = SYSBIO_KEY_BASE + ".inputFile";
    public static final String INPUT_NAME_KEY = SYSBIO_KEY_BASE + ".inputName";
    public static final String STEP_NAME_KEY = SYSBIO_KEY_BASE + ".StepName";
    public static final String UUID_KEY = SYSBIO_KEY_BASE + ".UUID";
    public static final String JOB_ID_NAME_KEY = SYSBIO_KEY_BASE + ".JobId";

    public static final String CONF_KEY = SYSBIO_KEY_BASE + ".configfile";
    public static final String REPORT_KEY = SYSBIO_KEY_BASE + ".reportfile"; // "Report";
    public static final int IN_POSITION_START = 4;
    public static final String JAR_PROPERTY = "org.systemsbiology.tandem.hadoop.PrebuiltJar";

    public static final int DEFAULT_REDUCE_TASKS = 14;
    // in development to speed up
    public static String gReuseJar; //"Mar231041_0.jar";   //  null; //


    private static Properties gRemoteProperties = new Properties();

    public static void setProperty(String key, String value) {
        gRemoteProperties.setProperty(key, value);
    }

    public static String getProperty(String key) {
        return gRemoteProperties.getProperty(key);
    }

    public static String getReuseJar() {
        return gReuseJar;
    }

    public static void setReuseJar(final String pReuseJar) {
        gReuseJar = pReuseJar;
    }

    private static IConfigureFileSystem gFileSystemConfigurer = IConfigureFileSystem.NULL_CONFIGURE_FILE_SYSTEM;

    /**
     * return the configurer
     *
     * @return !null configurer
     */
    public static synchronized IConfigureFileSystem getFileSystemConfigurer() {
        if (gFileSystemConfigurer == null)
            gFileSystemConfigurer = IConfigureFileSystem.NULL_CONFIGURE_FILE_SYSTEM;
        return gFileSystemConfigurer;
    }

    public static synchronized void setFileSystemConfigurer(final IConfigureFileSystem pFileSystemConfigurer) {
        gFileSystemConfigurer = pFileSystemConfigurer;
    }

    // limit to stop memory from beign exceeded
    public static final String MAXRECORDS_KEY = SYSBIO_KEY_BASE + ".max_records_per_partition";
    public static final int MAXIMUM_PARTITION_RECORDS = 18000;


    public static final String STATISTICS_REPORT_KEY = SYSBIO_KEY_BASE + ".statistics";

    private static SAMFileReader.ValidationStringency gValidationStringency =
            SAMFileReader.ValidationStringency.LENIENT;

    private HadoopUtilities() {
    } // do not construct


    /**
     * return all cached local files
     *
     * @param context
     * @return
     */
    public static File[] getDistributedCacheFiles(final TaskInputOutputContext context) {
        try {
            Configuration configuration = context.getConfiguration();

            LocalFileSystem system = FileSystem.getLocal(configuration);
            Path[] files = DistributedCache.getLocalCacheFiles(configuration);
            List<File> holder = new ArrayList<File>();

            if (files != null) {
                for (int i = 0; i < files.length; i++) {
                    Path file = files[i];
                    File file1 = system.pathToFile(file);
                    holder.add(file1);
                }
            }


            File[] ret = new File[holder.size()];
            holder.toArray(ret);
            return ret;
        }
        catch (IOException e) {
            throw new RuntimeException(e);

        }

    }


    public static long getCounterValue(Enum val, Job job) {
        try {
            Counters counters = job.getCounters();
            Counter counter = counters.findCounter(val);
            return counter.getValue();
        }
//        catch (InterruptedException e) {
//               throw new RuntimeException(e);
//
//           }
        catch (IOException e) {
            throw new RuntimeException(e);

        }

    }
//
//    public static String[] readConfigFileFS(String pArg) {
//        //   if (pArg.startsWith("s3n://"))
//        //       return AWSUtilities.readConfigFileS3(pArg);
//
//        File configuration = new File(pArg);
//        String[] pLines = null;
//        boolean exists = configuration.exists();
//        System.err.println("File " + pArg + " exists " + exists);
//        boolean isFile = configuration.isFile();
//        System.err.println("File " + pArg + " isFile " + isFile);
//        if (exists && isFile) {
//            List<String> holder = null;
//            String line = null;
//            try {
//                System.err.println("File " + pArg + " ready to open");
//                LineNumberReader nr = new LineNumberReader(new FileReader(pArg));
//                System.err.println("File " + pArg + "   open");
//                holder = new ArrayList<String>();
//                line = nr.readLine();
//                while (line != null) {
//                    System.err.println(line);
//                    holder.add(line);
//                    line = nr.readLine();
//                }
//            }
//            catch (IOException e) {
//                e.printStackTrace(System.err);
//                throw new RuntimeException(e);
//
//            }
//            String[] ret = new String[holder.size()];
//            pLines = holder.toArray(ret);
//            for (int i = 0; i < pLines.length; i++) {
//                line = pLines[i];
//                //   System.err.println("line " + i + " " + line);
//                // tell the system about the chromosome set
//                if (line.startsWith("Organism")) {
//                    String[] items = line.split(" ");
//                    if (items.length > 1) {
//                        String org = items[1];
//                        DefaultChromosome.setDefaultChromosomeSet(org);
//                    }
//                }
//            }
//
//        }
//        return pLines;
//    }
//

    public static void setCounterValue(Enum val, long newValue, Job job) {
        try {
            Counters counters = job.getCounters();
            Counter counter = counters.findCounter(val);
            long value = counter.getValue();
            counter.increment(newValue - value);
        }
//        catch (InterruptedException e) {
//              throw new RuntimeException(e);
//
//          }
        catch (IOException e) {
            throw new RuntimeException(e);

        }

    }


    public static boolean isStandAlone() {
        if (!isWindows())
            return false;
        return true; // todo add more tests
    }

    public static boolean isLinux() {
        String os = System.getProperty("os.name").toLowerCase();
        return os.contains("linux");
    }

    public static boolean isWindows() {
        return !isLinux();
    }

    public static void writeResourceAsFile(Class cls, String resourceName, LocalFileSystem localFs, String dstFile) {
        InputStream inp = cls.getResourceAsStream(resourceName);
        writeStreamAsFile(localFs, dstFile, inp);
    }

    public static void writeStreamAsFile(final LocalFileSystem localFs, final String dstFile, final InputStream pInp) {
        Path path = new Path(dstFile);
        try {
            FSDataOutputStream outStream = localFs.create(path);
            copyFile(pInp, outStream);
        }
        catch (IOException e) {
            throw new RuntimeException(e);

        }
    }

    public static void writeStringAsFile(final LocalFileSystem localFs, final String dstFile, final String s) {
        ByteArrayInputStream inp = new ByteArrayInputStream(s.getBytes());
        writeStreamAsFile(localFs, dstFile, inp);
    }

    /**
     * { method
     *
     * @param dst destination file name
     * @param src source file name
     * @return true for success
     *         }
     * @name copyFile
     * @function copy file named src into new file named dst
     */
    public static boolean copyFile(InputStream inp, FSDataOutputStream outStream) {
        int bufsize = 1024;
        try {
            // failure - no data

            int bytesRead = 0;
            byte[] buffer = new byte[bufsize];
            while ((bytesRead = inp.read(buffer, 0, bufsize)) != -1) {
                outStream.write(buffer, 0, bytesRead);
            }
            return true;
        }
        catch (IOException ex) {
            return (false);
        }
        finally {
            FileUtilities.guaranteeClosed(inp);
            FileUtilities.guaranteeClosed(outStream);
        }

    }


    private static void foo() throws Exception {
        Path p = new Path("/my/path");
        Configuration conf = new Configuration();
        FileSystem fs = FileSystem.get(conf);
        FileStatus[] fileStats = fs.listStatus(p);
        for (int i = 0; i < fileStats.length; i++) {
            Path f = fileStats[i].getPath();// do something interesting}
        }
    }

//    public void writeToFileSystem()
//    {
//        Configuration conf = new Configuration();
//        FileSystem fs = FileSystem.get(conf);
//        Path p = new Path("/my/path/foo");
//        FSDataOutputStream out = fs.create(path, false);
//// write some raw bytes
//        out.write(getBytes());
//// write an int
//        out.writeInt(getInt());
//        ...
//        out.close();
//    }
//
//    /**
//     * fill in the Sequence data when it is not available
//     *
//     * @param header
//     */
//    public static void addDefaultHeaderSequence( SAMFileHeader header) {
//        IChromosome[] chromosomes = DefaultChromosome.getDefaultChromosomeSet();
//        for (int i = 0; i < chromosomes.length; i++) {
//            IChromosome chr = chromosomes[i];
//            SAMSequenceRecord rec = new SAMSequenceRecord(chr.toString(), chr.getLength());
//            rec.setAssembly(DefaultChromosome.getDefaultChromosomeSetName());
//            header.addSequence(rec);
//        }
//    }
//
//    public static void readConfigFile(Configuration pConf, String pConfigFile) {
//        File configuration = new File(pConfigFile);
//        if (configuration.exists() && configuration.isFile()) {
//            String[] lines = FileUtilities.readInAllLines(configuration);
//            for (int i = 0; i < lines.length; i++) {
//                String line = lines[i];
//                // tell the system about the chromosome set
//                if (line.startsWith("Organism")) {
//                    String[] items = line.split(" ");
//                    if (items.length > 1)
//                        DefaultChromosome.setDefaultChromosomeSet(items[1]);
//                }
//            }
//            pConf.setStrings(HadoopUtilities.CONFIGURATION_KEY, lines);
//
//        }
//    }

    public static final Text ONLY_KEY = new Text(); // avoid garbage collection



    public static SAMFileReader.ValidationStringency getValidationStringency() {
        return gValidationStringency;
    }

    public static void setValidationStringency(
            SAMFileReader.ValidationStringency pValidationStringency) {
        gValidationStringency = pValidationStringency;
    }

    /**
     * read a SAM Header from a Text String
     *
     * @param text input text
     * @return SAMFileHeader
     */
    public static SAMFileHeader headerFromText(String text) {
        LineReader sr = new StringLineReader(text);
        final SAMTextHeaderCodec headerCodec = new SAMTextHeaderCodec();
        headerCodec.setValidationStringency(getValidationStringency());
        SAMFileHeader ret = headerCodec.decode(sr, null);
        return ret;

    }

    public static final Text ONLY_KEY1 = new Text(); // avoid garbage collection
    public static final Text ONLY_VALUE = new Text(); // avoid garbage collection

    /**
     * wrapper around text output
     *
     * @param ctx     !null context
     * @param key
     * @param message
     */
    public static void writeText(TaskInputOutputContext ctx, String key, String message) {
        try {
            ONLY_KEY1.set(key);
            ONLY_VALUE.set(message);
            ctx.write(ONLY_KEY1, ONLY_VALUE);
        }
        catch (IOException e) {
            throw new RuntimeException(e);

        }
        catch (InterruptedException e) {
            throw new RuntimeException(e);

        }

    }

//    /**
//     * real all records from a sam or BAM file
//     * DONT TRY ON A GIGABYTE FILE!!!
//     *
//     * @param fileName file to read
//     * @return !null array of records
//     */
//    public static IExtendedSamRecord[] readFile(String fileName) {
//        return readFile(new File(fileName));
//
//    }
//
//    /**
//     * real all records from a sam or BAM file
//     * DONT TRY ON A GIGABYTE FILE!!!
//     *
//     * @param fileName file to read
//     * @return !null array of records
//     */
//    public static SamRecord[] readFile(File fileName) {
//
//        List<IExtendedSamRecord> holder = new ArrayList<IExtendedSamRecord>();
//
//        final SAMFileReader inputSam = new SAMFileReader(fileName);
//        inputSam.setValidationStringency(getValidationStringency());
//        BamStatistics stat = new BamStatistics();
//        for (final SAMRecord samRecord : inputSam) {
//            IExtendedSamRecord rec = new ExtendedSamRecord(samRecord);
//            holder.add(rec);
//        }
//
//        inputSam.close();
//        IExtendedSamRecord[] ret = new IExtendedSamRecord[holder.size()];
//        holder.toArray(ret);
//        return ret;
//
//    }

    public static final String FIELD_SEPARATOR = "\t";

    /**
     * turn a SAM header into text
     *
     * @param header
     * @return
     */
    public static String buildHeaderText(final SAMFileHeader header) {
        final StringWriter headerTextBuffer = new StringWriter();
        new SAMTextHeaderCodec().encode(headerTextBuffer, header);
        final String headerText = headerTextBuffer.toString();
        return headerText;
    }
//
//    private static final TextTagCodec tagCodec = new TextTagCodec();
//    private static final SAMTagUtil tagUtil = new SAMTagUtil();

    public static enum KeepAliveEnum {
        KeepAlive;
    }

    ;

    public static enum MapFailureCount {
        FailureCount;
    }

    ;

    public static enum ReduceFailureCount {
        FailureCount;
    }

    ;

    /**
     * a certain number of failures are allowed in a mapper
     * note that one has occured
     *
     * @param ctx         !null context
     * @param id          count id
     * @param maxFailures throw an exception after this
     * @throws TaskFailException
     */
    public static void addFailure(TaskInputOutputContext cts, Throwable ex, Enum id, int maxFailures) {
        Counter counter = cts.getCounter(KeepAliveEnum.KeepAlive);
        int value = (int) counter.getValue();
        if (value > maxFailures)
            throw new TaskFailException(value, ex);
        ex.printStackTrace(System.err);
        counter.increment(1);

    }

    public static class TaskFailException extends RuntimeException {
        /**
         * Constructs a new runtime exception with <code>null</code> as its
         * detail message.  The cause is not initialized, and may subsequently be
         * initialized by a call to {@link #initCause}.
         */
        public TaskFailException(int numberFailures, Throwable ex) {
            super("Failed after " + numberFailures + " failures", ex);
        }
    }

    public static final int DEFAULT_KEEP_ALIVE_TIME = 10000; // 10 sec
    private static int gKeepAliveTimeMillisec = DEFAULT_KEEP_ALIVE_TIME;

    public static int getKeepAliveTimeMillisec() {
        return gKeepAliveTimeMillisec;
    }

    public static void setKeepAliveTimeMillisec(int pKeepAliveTimeMillisec) {
        gKeepAliveTimeMillisec = pKeepAliveTimeMillisec;
    }

    public static long getLastKeepAlive() {
        return gLastKeepAlive;
    }

    public static void setLastKeepAlive(long pLastKeepAlive) {
        gLastKeepAlive = pLastKeepAlive;
    }

    public static long gLastKeepAlive = System.currentTimeMillis();

    public static final ThreadLocal<TaskInputOutputContext> gCurrentContext = new ThreadLocal<TaskInputOutputContext>();

    public static void setCurrentContext(TaskInputOutputContext ctx) {
        gCurrentContext.set(ctx);
    }

    public static TaskInputOutputContext getCurrentContext() {
        return gCurrentContext.get();
    }

    /**
     * Set a counter to keep job from terminating
     *
     * @param cts !null task to tickle
     */
    public static void keepAlive() {
        keepAlive(getCurrentContext());
    }

    /**
     * /**
     * Set a counter to keep job from terminating
     *
     * @param cts !null task to tickle
     */
    public static void keepAlive(TaskInputOutputContext cts) {
        long now = System.currentTimeMillis();
        if (now < getLastKeepAlive() + getKeepAliveTimeMillisec())
            return;
        final Counter counter = cts.getCounter(KeepAliveEnum.KeepAlive);
        counter.increment(1);
        setLastKeepAlive(now);
    }

    /**
     * Writes the record to disk.  Sort order has been taken care of by the time
     * this method is called.
     *
     * @param alignment
     */
    public static String buildAlignmentText(final SAMRecord alignment) {
        StringBuilder sb = new StringBuilder();
        sb.append(alignment.getReadName());
        sb.append(FIELD_SEPARATOR);
        sb.append(Integer.toString(alignment.getFlags()));
        sb.append(FIELD_SEPARATOR);
        sb.append(alignment.getReferenceName());
        sb.append(FIELD_SEPARATOR);
        sb.append(Integer.toString(alignment.getAlignmentStart()));
        sb.append(FIELD_SEPARATOR);
        sb.append(Integer.toString(alignment.getMappingQuality()));
        sb.append(FIELD_SEPARATOR);
        sb.append(alignment.getCigarString());
        sb.append(FIELD_SEPARATOR);

        //  == is OK here because these strings are interned
        if (alignment.getReferenceName().equals(alignment.getMateReferenceName()) &&
                !SAMRecord.NO_ALIGNMENT_REFERENCE_NAME.equals(alignment.getReferenceName())) {
            sb.append("=");
        }
        else {
            sb.append(alignment.getMateReferenceName());
        }
        sb.append(FIELD_SEPARATOR);
        sb.append(Integer.toString(alignment.getMateAlignmentStart()));
        sb.append(FIELD_SEPARATOR);
        sb.append(Integer.toString(alignment.getInferredInsertSize()));
        sb.append(FIELD_SEPARATOR);
        sb.append(alignment.getReadString());
        sb.append(FIELD_SEPARATOR);
        sb.append(alignment.getBaseQualityString());
        if(true)
            throw new UnsupportedOperationException("Fix This"); // ToDo
//        if (alignment.getBinaryAttributes() != null) {  // todo fix
//            for (final SAMBinaryTagAndValue attribute : alignment.getBinaryAttributes()) {
//                sb.append(FIELD_SEPARATOR);
//                sb.append(tagCodec.encode(tagUtil.makeStringTag(attribute.tag), attribute.value));
//            }
//        }
//
        return sb.toString();
    }

}
