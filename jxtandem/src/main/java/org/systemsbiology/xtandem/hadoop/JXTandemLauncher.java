package org.systemsbiology.xtandem.hadoop;

import com.lordjoe.utilities.*;
 import org.apache.hadoop.conf.*;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.*;
import org.systemsbiology.common.*;
import org.systemsbiology.hadoop.*;
import org.systemsbiology.remotecontrol.*;
import org.systemsbiology.remotecontrol.LocalFileSystem;
import org.systemsbiology.xtandem.*;
import org.systemsbiology.xtandem.peptide.*;
import org.systemsbiology.xtandem.reporting.*;
import org.systemsbiology.xtandem.sax.*;
import org.systemsbiology.xtandem.taxonomy.*;

import java.io.*;
import java.util.*;
import java.util.prefs.*;

/**
 * org.systemsbiology.xtandem.hadoop.JXTandemLauncher
 * Launches a hadoop jon on the cluster
 * User: steven
 * Date: Jan 5, 2011
 * Singleton representing a JXTandem job -
 * This has the program main
 */
public class JXTandemLauncher implements IStreamOpener { //extends AbstractParameterHolder implements IParameterHolder {
    public static final JXTandemLauncher[] EMPTY_ARRAY = {};

    public static final String TURN_ON_SCAN_OUTPUT_PROPERTY = "org.systemsbiology.xtandem.SaveScansData";
    public static final int MAX_DISPLAY_LENGTH = 4 * 1000 * 1000;
    public static final int NUMBER_STAGES = 3;
    public static final boolean BUILD_DATABASE = true;

    private static boolean gReadScanFile;

    public static boolean isReadScanFile() {
        return gReadScanFile;
    }

    public static void setReadScanFile(final boolean pReadScanFile) {
        gReadScanFile = pReadScanFile;
    }

    private static boolean gSequenceFilesUsed = XTandemHadoopUtilities.USE_SEQUENCE_INTERMEDIATE_FILES;
    private static String gPassedJarFile;

    public static String getPassedJarFile() {
        return gPassedJarFile;
    }

    public static void setPassedJarFile(final String pPassedJarFile) {
        gPassedJarFile = pPassedJarFile;
    }

    public static boolean isSequenceFilesUsed() {
        return gSequenceFilesUsed;
    }

    public static void setSequenceFilesUsed(final boolean pSequenceFilesUsed) {
        gSequenceFilesUsed = pSequenceFilesUsed;
    }


    public static void logMessage(String message) {
        XTandemUtilities.outputLine(message);
    }

    private static boolean gDatabaseRebuildForced;
    private static boolean gDatabaseBuildOnly;

    public static boolean isDatabaseRebuildForced() {
        return gDatabaseRebuildForced;
    }

    public static void setDatabaseRebuildForced(final boolean pDatabaseRebuildForced) {
        gDatabaseRebuildForced = pDatabaseRebuildForced;
    }

    public static boolean isDatabaseBuildOnly() {
        return gDatabaseBuildOnly;
    }

    public static void setDatabaseBuildOnly(final boolean isTrue) {
        gDatabaseBuildOnly = isTrue;
        if (isTrue)
            setDatabaseRebuildForced(true); // make sure the database is built
    }

    protected static File getOutputFile(IMainData main, String key) {
        File ret = new File(main.getParameter(key));
        File parentFile = ret.getParentFile();
        if (parentFile != null) {
            if (!parentFile.exists())
                parentFile.mkdirs();
            else {
                if (parentFile.isFile())
                    throw new IllegalArgumentException("write output file into file  " + ret.getName());
            }
        }
        if ((parentFile != null && (!parentFile.exists() || !parentFile.canWrite())))
            throw new IllegalArgumentException("cannot access output file " + ret.getName());
        if (ret.exists() && !ret.canWrite())
            throw new IllegalArgumentException("cannot rewrite output file file " + ret.getName());

        return ret;
    }

    private HadoopTandemMain m_Application;

    private final JXTandemStatistics m_Statistics = new JXTandemStatistics();
    private String m_RemoteBaseDirectory;
    private String m_LocalBaseDirectory = System.getProperty("user.dir").replace("\\", "/");
    private Taxonomy m_Taxonomy;
    private String m_JarFile;
    private String m_OutputFileName;


    private final DelegatingFileStreamOpener m_Openers = new DelegatingFileStreamOpener();
    private int m_PassNumber;
    private boolean m_BuildJar = true;
    private final Map<String, String> m_PerformanceParameters = new HashMap<String, String>();
    private IFileSystem m_Accessor;

    // read from list path, default parameters as notes

    public JXTandemLauncher(final File pTaskFile) {

        setParameter("TaskFile", pTaskFile.getAbsolutePath());
        //    Protein.resetNextId();
        initOpeners();
        try {
            handleInputs(new FileInputStream(pTaskFile), pTaskFile.getName(), new Configuration());
        }
        catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public JXTandemLauncher(final InputStream is, String url, Configuration cfg) {
        setParameter("TaskFile", null);
        ///     Protein.resetNextId();
        initOpeners();

        handleInputs(is, url, cfg);

        this.m_RemoteBaseDirectory = getPassedBaseDirctory();
        if (isTaskLocal())
            this.m_LocalBaseDirectory = getRemoteBaseDirectory();
        //     if (gInstance != null)
        //        throw new IllegalStateException("Only one XTandemMain allowed");

    }

    /**
     * set a parameter value
     *
     * @param key   !null key
     * @param value !null value
     */
    public void setParameter(String key, String value) {
        getStatistics().setData(key, value);
    }

    /**
     * return a parameter configured in  default parameters
     *
     * @param key !null key
     * @return possibly null parameter
     */
    public String getParameter(String key) {
        return getStatistics().getData(key);
    }


    public JXTandemStatistics getStatistics() {
        return m_Statistics;
    }

    public long getTotalFragments() {
        String totalFragments = getParameter("TotalFragments");
        if (totalFragments == null)
            return 0;
        return Long.parseLong(totalFragments);
    }

    public void setPerformanceParameter(String key, String value) {
        m_PerformanceParameters.put(key, value);
    }

    /**
     * return a parameter configured in  default parameters
     *
     * @param key !null key
     * @return possibly null parameter
     */
    public String getPerformanceParameter(String key) {
        return m_PerformanceParameters.get(key);
    }

    /**
     * return a parameter configured in  rgw tandem
     *
     * @param key !null key
     * @return possibly null parameter
     */
    public String getTandemParameter(String key) {
        return getApplication().getParameter(key);
    }

    public String[] getPerformanceKeys() {
        String[] ret = m_PerformanceParameters.keySet().toArray(new String[0]);
        Arrays.sort(ret);
        return ret;
    }


    public boolean isBuildJar() {
        return m_BuildJar;
    }

    public void setBuildJar(final boolean pBuildJar) {
        m_BuildJar = pBuildJar;
    }

    public String getOutputLocationBase() {
        return getParameter("OutputLocationBase");
    }

    public void setOutputLocationBase(final String pOutputLocationBase) {
        setParameter("OutputLocationBase", pOutputLocationBase);
    }

    public String getOutputFileName() {
        if (m_OutputFileName == null)
            m_OutputFileName = BiomlReporter.buildDefaultFileName(getApplication());

        return m_OutputFileName;
    }

    /**
     * where output directories go
     *
     * @param passNumber job number - guarantees uniqueness
     * @return path name
     */
    public String getOutputLocation(int passNumber) {
        return getOutputLocationBase() + passNumber;
    }

    /**
     * where output directories go
     *
     * @return path name
     */
    public String getOutputLocation() {
        return getOutputLocation(getPassNumber());
    }

    public String getLastOutputLocation() {
        return getOutputLocation(getPassNumber() - 1);
    }

    public int getPassNumber() {
        return m_PassNumber;
    }

    public void setPassNumber(final int pPassNumber) {
        m_PassNumber = pPassNumber;
    }

    public void incrementPassNumber() {
        m_PassNumber++;
    }

    /**
     * add new ways to open files
     */
    protected void initOpeners() {
        addOpener(new FileStreamOpener());
        addOpener(new StreamOpeners.ResourceStreamOpener(XTandemUtilities.class));
        for (IStreamOpener opener : XTandemMain.getPreloadOpeners())
            addOpener(opener);
    }


    /**
     * open a file from a string
     *
     * @param fileName  string representing the file
     * @param otherData any other required data
     * @return possibly null stream
     */
    @Override
    public InputStream open(String fileName, Object... otherData) {
        return m_Openers.open(fileName, otherData);
    }

    public void addOpener(IStreamOpener opener) {
        m_Openers.addOpener(opener);
    }


    public ElapsedTimer getElapsed() {
        return getStatistics().getTotalTime();
    }

    public String getJarFile() {
        return m_JarFile;
    }

    public void setJarFile(final String pJarFile) {
        m_JarFile = pJarFile;
    }

    /**
     * parse the initial file and get run parameters
     *
     * @param is
     */
    public void handleInputs(final InputStream is, String url, Configuration cfg) {

        m_Application = HadoopTandemMain.getInstance(is, url, cfg);
        //    m_DefaultParameters = getTandemParameter( "list path, default parameters"); //, "default_input.xml");
        //    m_TaxonomyInfo = getTandemParameter( "list path, taxonomy information"); //, "taxonomy.xml");
        //    m_TaxonomyName = getTandemParameter("protein, taxon");
        //    m_SpectrumPath = getTandemParameter("spectrum, path"); //, "test_spectra.mgf");

        //    m_OutputPath = getOutputFile(m_Application, "output, path"); //, "output.xml");
        //  m_OutputResults = getTandemParameter("output, results");

        //     readDefaultParameters(m_Application);

        XTandemUtilities.validateParameters(getApplication());
        String value = m_Application.getParameter(TURN_ON_SCAN_OUTPUT_PROPERTY);

        if (value != null && "yes".equals(value))
            setReadScanFile(true);

        Properties properties = XTandemHadoopUtilities.getgHadoopProperties();
        for(String key : properties.stringPropertyNames())  {
            String val = properties.getProperty(key);
            m_Application.setParameter(key,val);
        }

    }

    /**
     * find the first protien with this sequwence and return the correcponding id
     *
     * @param sequence
     * @return
     */
    public String seqenceToID(String sequence) {
        return getTaxonomy().seqenceToID(sequence);
    }


    public File getTaskFile() {
        String taskFile = getParameter("TaskFile");
        if (taskFile == null)
            return null;
        return new File("TaskFile");
    }


    public String getTaxonomyInfo() {
        return getTandemParameter("list path, taxonomy information");
    }

    public String getSpectrumPath() {
        return getTandemParameter("spectrum, path");
    }


    public String getTaxonomyName() {
        return getTandemParameter("protein, taxon");
    }


    public Taxonomy getTaxonomy() {
        return m_Taxonomy;
    }

    public String getRemoteBaseDirectory() {
        if (m_RemoteBaseDirectory != null)
            return m_RemoteBaseDirectory;
        else
            return m_LocalBaseDirectory;
    }

    public void setRemoteBaseDirectory(final String pRemoteBaseDirectory) {
        m_RemoteBaseDirectory = pRemoteBaseDirectory;
        XTandemHadoopUtilities.setDefaultPath(pRemoteBaseDirectory);
    }

    public String getRemoteHost() {
        return getParameter("RemoteHost");
    }

    public void setRemoteHost(final String pRemoteHost) {
        setParameter("RemoteHost", pRemoteHost);
    }

    public int getRemoteHostPort() {
        String remoteHostPort = getParameter("RemoteHostPort");
        if (remoteHostPort == null)
            return 0;
        return Integer.parseInt(remoteHostPort);
    }

    public void setRemoteHostPort(final int pRemoteHostPort) {
        setParameter("RemoteHostPort", Integer.toString(pRemoteHostPort));
    }

    public String getParamsPath() {
        return getParameter("ParamsPath");
    }

    public String getTaskParamsPath() {
        String rbase = getRemoteBaseDirectory();
        String paramsPath = getParamsPath();
        if (rbase != null) {
            return rbase + "/" + new File(paramsPath).getName();

        }
        return paramsPath;
    }

    public void setParamsPath(String pParamsPath) {
        String rbase = getRemoteBaseDirectory();
        if (rbase != null) {
            pParamsPath = rbase + "/" + pParamsPath;

        }
        setParameter("ParamsPath", pParamsPath);
    }


    /*
    * modify checks the input parameters for known parameters that are use to modify
    * a protein sequence. these parameters are stored in the m_pScore member object's
    * msequenceutilities member object
    */


    public final String DEFAULT_SCORING_CLASS = "org.systemsbiology.xtandem.TandemScoringAlgorithm";


/*
* taxonomy uses the taxonomy information in the input XML file to load
* the  ProteinSequenceServer member object with file path names to the required
* sequence list files (FASTA format only in the initial release). If these
*/

    public void loadTaxonomy() {
        String strKey = "list path, taxonomy information";
        String path = getApplication().getParameter(strKey);
//        strKey = "protein, taxon";
//        String taxonomyName = getParameter(strKey);

        final String taxonomyName = getTaxonomyName();
        final String descriptiveFile = getTaxonomyInfo();
        m_Taxonomy = new Taxonomy(getApplication(), taxonomyName, descriptiveFile);

        TaxonHandler taxonHandler = new TaxonHandler(null, "peptide", taxonomyName);

        if (path != null) {
            InputStream is = open(path);
            String[] peptideFiles = XTandemUtilities.parseFile(is, taxonHandler, path);
            taxonHandler = new TaxonHandler(null, "saps", taxonomyName);
            is = open(path);
            String[] sapFiles = XTandemUtilities.parseFile(is, taxonHandler, path);

            // This step is called load annotation in XTandem
            taxonHandler = new TaxonHandler(null, "mods", taxonomyName);
            is = open(path);
            String[] annotationfiles = XTandemUtilities.parseFile(is, taxonHandler, path);

        }
        else {

        }


        //  long lReturn = m_svrSequences.load_file(strTaxonPath, strValue);
    }
//
//    /**
//     * read the parameters dscribed in the bioml file
//     * listed in "list path, default parameters"
//     * These look like
//     * <note>spectrum parameters</note>
//     * <note type="input" label="spectrum, fragment monoisotopic mass error">0.4</note>
//     * <note type="input" label="spectrum, parent monoisotopic mass error plus">100</note>
//     * <note type="input" label="spectrum, parent monoisotopic mass error minus">100</note>
//     * <note type="input" label="spectrum, parent monoisotopic mass isotope error">yes</note>
//     */
//    protected void readDefaultParameters(IMainData inputParameters) {
//        Map<String, String> map = XTandemUtilities.readNotes(m_DefaultParameters, this);
//        getParametersMap().putAll(map);
//        // parameters in the input file override parameters in the default file
//        getParametersMap().putAll(inputParameters);
//    }

    public static void usage() {
        XTandemUtilities.outputLine("Usage - JXTandem config=<confFile> params=<inputfile> <forceDatabaseRebuild> <buildDatabaseOnly>");
    }

    public static void usage2() {
        XTandemUtilities.outputLine("Usage - JXTandem <inputfile>");
    }

    public static void usage(String filename) {
        XTandemUtilities.outputLine("Usage - JXTandem <inputfile> cannot read file " + filename + " in directory " +
                System.getProperty("user.dir"));
    }

    public IFileSystem getAccessor() {
        return m_Accessor;
    }

    public void setAccessor(final IFileSystem pAccessor) {
        m_Accessor = pAccessor;
    }

    /**
     * @param hdfsPath
     * @param localFile
     * @return !null file
     * @throws IllegalArgumentException if the remote file does not exist
     */
    public File readRemoteFile(String hdfsPath, String localFile) throws IllegalArgumentException {
//        int p = getRemoteHostPort();
//        if (p <= 0)
//            throw new IllegalStateException("bad remote host port " + p);
//
        IFileSystem acc = getAccessor();
        if (!acc.exists(hdfsPath))
            throw new IllegalArgumentException("remote file " + hdfsPath + " does not exist");
        else
            XTandemUtilities.outputLine("Copying file " + hdfsPath + " to " + localFile);
        File out = new File(localFile);
        acc.copyFromFileSystem(hdfsPath, out);
        //      String remotepath = getOutputLocation(3) + "/" + fileName;
        return out;
    }

    public static final int SMALL_FILE_LENGTH = 1000000;


    protected int guaranteeRemoteDirectory(String baseDir, File file) {
        IFileSystem accessor = getAccessor();
        String remotepath = baseDir + "/" + file.getName();
        accessor.guaranteeDirectory(remotepath);
        File[] files = file.listFiles();
        int ret = 0;
        for (int i = 0; i < files.length; i++) {
            File file1 = files[i];
            if (file1.isFile()) {
                String path = remotepath + "/" + file1.getName();
                XTandemUtilities.outputLine("Writing to remote " + file1 + " " + file1.length() / 1000 + "kb");
                accessor.writeToFileSystem(path, file1);
                ret++;
            }
            else {
                ret += guaranteeRemoteDirectory(remotepath, file);
            }
        }
        return ret;
    }

    protected void handleCounters(IHadoopJob job) {
        Map<String, Long> counters;
        counters = job.getAllCounterValues();
        for (String key : counters.keySet()) {
            if (XTandemHadoopUtilities.isCounterHadoopInternal(key))
                continue;
            //Counter c = counters.get(key);
            String value = Long.toString(counters.get(key));
            setParameter(key, value);
        }
        //  XTandemHadoopUtilities.showAllCounters(counters);

    }

    public void runJobs(IHadoopController launcher) {

        XTandemUtilities.outputLine("Starting Job");
        guaranteeRemoteFiles();

        ElapsedTimer elapsed = getElapsed();

        JXTandemStatistics statistics = getStatistics();
        statistics.startJob("Total Scoring");
        // remember the input file foe a final report
        HadoopTandemMain application = getApplication();
        String parameter = application.getParameter("spectrum, path");
        if (parameter != null)
            statistics.setData("Input file", parameter);
        ITaxonomy taxonomy = application.getTaxonomy();
        String organism = taxonomy.getOrganism();
        if (organism != null)
            statistics.setData("Taxonomy database", organism);

        boolean buildDatabase = isDatabaseBuildRequired();
        IHadoopJob job = null;
        boolean ret;
        //  XTandemUtilities.outputLine("Temporarily reusing data directory");
        if (buildDatabase) {

            statistics.startJob("Build Database");
            statistics.startJob("SequenceFinder");
            job = buildJobSequenceFinder();

            ret = launcher.runJob(job);
            if (!ret)
                throw new IllegalStateException("SequenceFinder failed");
            handleCounters(job);
            elapsed.showElapsed("Finished SequenceFinder");
            statistics.endJob("SequenceFinder");
            elapsed.reset();
            statistics.startJob("MassFinder");
            job = buildJobMassFinder();
            ret = launcher.runJob(job);
            if (!ret)
                throw new IllegalStateException("MassFinder failed");

            handleCounters(job);


            IHadoopJob[] jobs = { job };

            elapsed.showElapsed("Finished MassFinder");
            saveDatabaseSizes(jobs);
            statistics.endJob("MassFinder");
            statistics.endJob("Build Database");
            if (isDatabaseBuildOnly()) {
                if ("true".equalsIgnoreCase(HadoopUtilities.getProperty(DELETE_OUTPUT_DIRECTORIES_PROPERTY)))
                    deleteRemoteIntermediateDirectories();
                return;

            }
            // return; // for now lets just get this part right
        }


        statistics.startJob("Scorer");
        job = buildJobPass1();

        ret = launcher.runJob(job);
        if (!ret)
            throw new IllegalStateException("Scorer job failed");

        statistics.endJob("Scorer");
        handleCounters(job);

        statistics.startJob("Score Combiner");
        elapsed.showElapsed("Finished Pass1");
        elapsed.reset();

        String jarString = getPassedJarFile();
        if (jarString == null)
            jarString = job.getJarFile();

        job = buildJobPass2();
        job.setJarFile(jarString);

        ret = launcher.runJob(job);
        if (!ret)
            throw new IllegalStateException("Score Combiner job failed");
        statistics.endJob("Score Combiner");

        handleCounters(job);


        elapsed.showElapsed("Finished Pass2");
        elapsed.reset();

        statistics.startJob("Consolidator");
        job = buildRemoteConsolidatorJob();
        job.setJarFile(jarString);

        ret = launcher.runJob(job);
        if (!ret)
            throw new IllegalStateException("Consolidator job failed");
        statistics.endJob("Consolidator");

        handleCounters(job);

        if ("true".equalsIgnoreCase(HadoopUtilities.getProperty(DELETE_OUTPUT_DIRECTORIES_PROPERTY)))
            deleteRemoteIntermediateDirectories();

        elapsed.showElapsed("Finished Consolidation");
        statistics.endJob("Total Scoring");

        // runJob(hc);
    }

    public static final int MAX_JOBS = 6;

    protected void deleteRemoteIntermediateDirectories() {
        IFileSystem accessor = getAccessor();
        for (int i = 0; i < MAX_JOBS; i++) {
            String outputLocation = getOutputLocation(i);
            accessor.expunge(outputLocation);
        }
    }

    protected void deleteLocalIntermediateDirectories() {
        for (int i = 0; i < MAX_JOBS; i++) {
            String outputLocation = getOutputLocation(i);
            FileUtilities.expungeDirectory(outputLocation);
        }
    }

    /**
     * copy files to hte file system designated
     *
     * @param pAccessor
     * @param pRbase
     */
    protected void guaranteeAccessibleFiles(final String pRbase) {
        IFileSystem pAccessor = getAccessor();

        XTandemUtilities.outputLine("Finding Remote Directory");
        pAccessor.guaranteeDirectory(pRbase);
        XTandemUtilities.outputLine("Writing Tandem Params");
        writeRemoteParamsFile(pRbase);

        //    guaranteeRemoteCopy(accessor, paramsPath);
        // we roll default parameters into params
        // guaranteeRemoteCopy(accessor, m_DefaultParameters);
        //    guaranteeRemoteCopy(accessor, m_TaxonomyInfo);

        XTandemUtilities.outputLine("Writing Taxonomy");
        writeRemoteTaxonomyFile(pAccessor, pRbase);

        // guaranteeRemoteCopy(accessor, m_TaxonomyName);
        XTandemUtilities.outputLine("Writing Spectral Data");
        String spectrumPath = getSpectrumPath();
        File spectrumFile = new File(spectrumPath);
        // maybe we need to copy to the remote system or maybe it iw already there
        if (spectrumFile.exists()) {
            guaranteeRemoteFilePath(spectrumFile, pRbase);
        }
        else {
            guaranteExistanceofRemoteFile(spectrumFile, pRbase, "Spectrum file ");
        }

        XTandemUtilities.outputLine("Writing Taxonomy database");
        String[] tfs = m_Taxonomy.getTaxomonyFiles();
        if (tfs != null) {
            for (int i = 0; i < tfs.length; i++) {
                String tf = tfs[i];
                File taxonomy = new File(tf);
                // maybe we need to copy to the remote system or maybe it iw already there
                if (taxonomy.exists()) {
                    guaranteeRemoteFilePath(taxonomy, pRbase);
                }
                else {
                    guaranteExistanceofRemoteFile(taxonomy, pRbase, "Taxonomy file ");
                }
            }
        }

        if (getPassedJarFile() != null) {

        }
    }

    protected int guaranteExistanceofRemoteFile(final File pFile1, final String pRemotePath, String message) {
        final IFileSystem accessor = getAccessor();
        if (pFile1.isDirectory()) {
            int ret = guaranteeRemoteDirectory(pRemotePath, pFile1);
            return ret;
        }
        String remotepath = pRemotePath + "/" + XTandemUtilities.asLocalFile(pFile1.getAbsolutePath());
        // if it is small then always make a copy
        if (accessor.exists(remotepath)) {
            return 2;
        }
        throw new IllegalStateException("message " + remotepath + "does not exist");
    }

    protected int guaranteeRemoteFilePath(final File pFile1, final String pRemotePath) {
        final IFileSystem accessor = getAccessor();
        if (pFile1.isDirectory()) {
            int ret = guaranteeRemoteDirectory(pRemotePath, pFile1);
            return ret;
        }
        if (!pFile1.canRead())
            return 0;

        long length = pFile1.length();

        String remotepath = pRemotePath + "/" + XTandemUtilities.asLocalFile(pFile1.getAbsolutePath());
        // if it is small then always make a copy
        if (length < SMALL_FILE_LENGTH) {
            accessor.deleteFile(remotepath);
            XTandemUtilities.outputLine("Writing to remote " + pFile1 + " " + pFile1.length() / 1000 + "kb");
            accessor.guaranteeFile(remotepath, pFile1);
            return 1;
        }
        else {
            if (accessor.exists(remotepath)) {
                long existingLength = accessor.fileLength(remotepath);
                // for very large data files we will tolerate a lot of error to avoid recopying them
                if (existingLength == length
                        || existingLength > (3 * 1000 * 1000 * 1000)      // todo HACK HACK if you have a gig or more do NOT recopy
                        ) {
                    return 1; // todo check md5 hash
                }
            }
            accessor.deleteFile(remotepath);
            accessor.guaranteeFile(remotepath, pFile1);
            return 1;
        }
    }

    protected void writeRemoteParamsFile(final String pRbase) {
        final IFileSystem pAccessor = getAccessor();
        String paramsPath = getParamsPath();
        File f = new File(paramsPath);
        String remotepath = pRbase + "/" + f.getName();
        OutputStream os = pAccessor.openFileForWrite(remotepath);
        PrintWriter out = new PrintWriter(new OutputStreamWriter(os));
        writeAdjustedParameters(out);
    }


    protected void writeRemoteTaxonomyFile(final IFileSystem pAccessor, final String pRbase) {
        String taxonomyPath = getTaxonomyInfo();
        if (taxonomyPath == null)
            throw new IllegalStateException("no taxonomy defined - add  list path, taxonomy information "); // ToDo change
        File f = new File(taxonomyPath);
        String remotepath = pRbase + "/" + f.getName();
        if (new File(remotepath).equals(f))
            return;
        OutputStream os = pAccessor.openFileForWrite(remotepath);
        PrintWriter out = new PrintWriter(new OutputStreamWriter(os));
        writeAdjustedTaxonomy(out);
    }

    /**
     * remap paths to where we put them in hdfs
     *
     * @param out
     */
    protected void writeAdjustedParameters(PrintWriter out) {
        out.println("<?xml version=\"1.0\"?>");
        out.println("<?xml-stylesheet type=\"text/xsl\" href=\"tandem-input-style.xsl\"?>");
        out.println("<bioml>");
        HadoopTandemMain application = this.getApplication();
        String[] parameterKeys = application.getParameterKeys();
        for (int i = 0; i < parameterKeys.length; i++) {
            String key = parameterKeys[i];
            String value = application.getParameter(key);
            if ("list path, default parameters".equals(key))
                continue; // plan to have all params in params file
            // convert all paths to local
            if (key.contains(" path")) {
                File f = new File(value);
                value = f.getName();
                String prefix = XTandemMain.getRequiredPathPrefix();
                if(prefix != null && !value.startsWith(prefix))
                     value =  prefix + value;
            }
            out.println("\t<note type=\"input\" label=\"" + key +
                    "\">" + value + "</note>");

        }
        out.println("</bioml>");
        out.close();
    }

    /**
     * remap paths to where we put them in hdfs
     *
     * @param out
     */
    protected void writeAdjustedTaxonomy(PrintWriter out) {
        String prefix = XTandemMain.getRequiredPathPrefix();
        out.println("<?xml version=\"1.0\"?>");
        out.println("<bioml label=\"x! taxon-to-file matching list\">");
        HadoopTandemMain app = this.getApplication();
        Taxonomy taxonomy = (Taxonomy) app.getTaxonomy();
        String taxonomyName = app.getParameter("protein, taxon");
        String[] taxomonyFiles = taxonomy.getTaxomonyFiles();
        out.println("     <taxon label=\"" + taxonomyName + "\">");
        for (int i = 0; i < taxomonyFiles.length; i++) {
            String s = taxomonyFiles[i];
            File f = new File(s);
            String value = f.getName();
            if(prefix != null && !value.startsWith(prefix))
                 value =  prefix + value;
             out.println("          <file format=\"peptide\" URL=\"" + value +
                    "\" />");

        }
        out.println("     </taxon>");
        out.println("</bioml>");
        out.close();
    }


    public void guaranteeRemoteFiles() {

        //    String host = RemoteUtilities.getHost(); // "192.168.244.128"; // "hadoop1";
        //    int port = RemoteUtilities.getPort();
        IFileSystem accessor = getAccessor(); //new HDFSAccessor(host, port);
        String rbase = getRemoteBaseDirectory();

        String udir = System.getProperty("user.dir");
        // running on local
        if (new File(udir).equals(new File(rbase)))
            return;
        guaranteeAccessibleFiles(rbase);
    }


    protected void saveDatabaseSizes(final IHadoopJob[] jobs) {
          Map<String, Long> counters;
        for (int i = 0; i < jobs.length; i++) {
            IHadoopJob job = jobs[i];
            if(!job.getMainClass().equals(JXTandemMassHandler.class.getName()))
                continue;
            counters = job.getAllCounterValues();
             String sizeText = buildSizeText(counters);
             HadoopTandemMain application = (HadoopTandemMain) getApplication();
             XTandemHadoopUtilities.writeDatabaseSizes(application, sizeText);

        }
     }

    protected String buildSizeText(final Map<String, Long> pCounters) {
        long total = 0;
        long max = 0;
        JXTandemStatistics statistics = getStatistics();
        StringBuilder sb = new StringBuilder();
        for (String key : pCounters.keySet()) {
            if (key.startsWith("M0") && key.endsWith(".peptide")) {
                sb.append(key.substring(1).replace(".peptide", ""));
               long value = pCounters.get(key);
                  total += value;
                max = Math.max(value, max);
                sb.append("\t" + value);
                sb.append("\n");
            }
        }
        statistics.setData("Total Fragments", Long.toString(total));
        statistics.setData("Max Mass Fragments", Long.toString(max));

        return sb.toString();
    }


    /**
     * read any cached database parameters
     *
     * @param context     !null context
     * @param application !null application
     * @return possibly null descripotion - null is unreadable
     */
    public DigesterDescription readDigesterDescription(HadoopTandemMain application) {
        try {
            String paramsFile = application.getDatabaseName() + ".params";
            Path dd = XTandemHadoopUtilities.getRelativePath(paramsFile);
            String hdfsPath = dd.toString();
            IFileSystem fs = getAccessor();
            if (!fs.exists(hdfsPath))
                return null;
            InputStream fsout = fs.openFileForRead(hdfsPath);
            DigesterDescription ret = new DigesterDescription(fsout);
            return ret;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;

        }
    }


    protected boolean isDatabaseBuildRequired() {
        if (isDatabaseRebuildForced())
            return true;
        HadoopTandemMain application = (HadoopTandemMain) getApplication();
        boolean buildDatabase;
        // Validate build parameters
         DigesterDescription dd = null;
        try {
            dd = readDigesterDescription(application);
        }
        catch (Exception e) {
            return true; // bad descriptor
        }
        // we have a database
        if (dd != null) {
            DigesterDescription desired = DigesterDescription.fromApplication(application);
            if (desired.equivalent(dd)) {
                buildDatabase = false;
            }
            else {
                buildDatabase = true;
                Path dpath = XTandemHadoopUtilities.getRelativePath(application.getDatabaseName());
                try {
                    FileSystem fileSystem = dpath.getFileSystem(application.getContext());
                    XTandemHadoopUtilities.expunge(dpath, fileSystem);
                }
                catch (IOException e) {
                    throw new RuntimeException(e);

                }
            }

        }
        else {
            buildDatabase = true;
        }
        Map<Integer, Integer> sizeMap = XTandemHadoopUtilities.guaranteeDatabaseSizes(application);
        if (sizeMap == null) {
            buildDatabase = true;
        }
        JXTandemStatistics statistics = getStatistics();
        long totalFragments = XTandemHadoopUtilities.sumDatabaseSizes(sizeMap);
        if (totalFragments < 1) {
            buildDatabase = true;
        }

        long MaxFragments = XTandemHadoopUtilities.maxDatabaseSizes(sizeMap);
        statistics.setData("Total Fragments", Long.toString(totalFragments));
        statistics.setData("Max Mass Fragments", Long.toString(MaxFragments));

        return buildDatabase;
    }


    public HadoopTandemMain getApplication() {
        return m_Application;
    }

    protected String getHostPrefix()
    {
        return "";
    }

    protected IHadoopJob buildJobSequenceFinder() {

        IMainData application = getApplication();
        Taxonomy taxonomy = (Taxonomy) application.getTaxonomy();
        String[] taxomonyFiles = taxonomy.getTaxomonyFiles();
        if (taxomonyFiles == null || taxomonyFiles.length == 0)
            throw new IllegalStateException("no taxonomy files defined");
        String spectumFile = taxomonyFiles[0];
        spectumFile = new File(spectumFile).getName();
        String remoteHost = getRemoteHost();
        int p = getRemoteHostPort();
        //         Taxonomy taxonomy = getTaxonomy();
//        String[] files = taxonomy.getTaxomonyFiles();
//        if(files != null && files.length > 0)
//            spectumFile = files[0];
        Class<JXTandemParser> mainClass = JXTandemParser.class;
        File taskFile = getTaskFile();
        String outputLocation = getOutputLocation();
        String taskParamsPath = getTaskParamsPath();
        String remoteBaseDirectory = getRemoteBaseDirectory();
        if(!spectumFile.startsWith(remoteBaseDirectory))
             spectumFile =  remoteBaseDirectory + "/"  + spectumFile;

        IHadoopJob job = HadoopJob.buildJob(
                mainClass,
                spectumFile,     // data on hdfs
                "jobs",      // jar location
                outputLocation,
                "-D",
                XTandemHadoopUtilities.PARAMS_KEY + "=" + taskParamsPath,
                "-D",
                XTandemHadoopUtilities.PATH_KEY + "=" + remoteBaseDirectory,
                "-D",
                XTandemHadoopUtilities.HOST_KEY + "=" + remoteHost,
                "-D",
                    XTandemHadoopUtilities.HOST_PORT_KEY + "=" + p,
                "-D",
                    XTandemHadoopUtilities.HOST_PREFIX_KEY + "=" + getHostPrefix()

//                  "-D",
//                  "org.systemsbiology.reportfile=YeastReports/yeastreport.xml"  // report file
        );
        String jarFile = job.getJarFile();
        setJarFile(jarFile);
        incrementPassNumber();
        return job;
    }


    protected IHadoopJob buildJobMassFinder() {

        // jar is same as parser
        HadoopJob.setJarRequired(false);

//         Taxonomy taxonomy = getTaxonomy();
//        String[] files = taxonomy.getTaxomonyFiles();
//        if(files != null && files.length > 0)
//            spectumFile = files[0];
        Class<JXTandemMassHandler> mainClass = JXTandemMassHandler.class;
        String remoteHost = getRemoteHost();
        int p = getRemoteHostPort();
        File taskFile = getTaskFile();
        String taskParamsPath = getTaskParamsPath();
        String remoteBaseDirectory = getRemoteBaseDirectory();
        String lastOutputLocation = getLastOutputLocation();
        String outputLocation = getOutputLocation();
        IHadoopJob job = HadoopJob.buildJob(
                mainClass,
                lastOutputLocation,     // data on hdfs
                "jobs",      // jar location
                outputLocation,
                "-D",
                XTandemHadoopUtilities.PARAMS_KEY + "=" + taskParamsPath,
                "-D",
                XTandemHadoopUtilities.PATH_KEY + "=" + remoteBaseDirectory,
                "-D",
                XTandemHadoopUtilities.HOST_KEY + "=" + remoteHost,
                "-D",
                XTandemHadoopUtilities.HOST_PORT_KEY + "=" + p

//                  "-D",
//                  "org.systemsbiology.reportfile=YeastReports/yeastreport.xml"  // report file
        );
        // reuse the only jar file
        job.setJarFile(getJarFile());

        incrementPassNumber();
        return job;
    }


    protected IHadoopJob buildJobPass1() {
        // guaranteeRemoteFiles();
        String spectumFile = getSpectrumPath();
        Class<JXTantemPass1Runner> mainClass = JXTantemPass1Runner.class;
        File taskFile = getTaskFile();
        boolean buildJar = isBuildJar();
        HadoopJob.setJarRequired(buildJar);
        String outputLocation = getOutputLocation();
        String remoteHost = getRemoteHost();
        String remoteBaseDirectory = getRemoteBaseDirectory();
        String spectrumFileName = spectumFile;
        //       if (new File(spectumFile).isAbsolute())
        spectrumFileName = new File(spectumFile).getName();
        String inputPath = getRemoteBaseDirectory() + "/" + spectrumFileName;
        String jobDirectory = "jobs";
        int p = getRemoteHostPort();
        //    if (p <= 0)
        //         throw new IllegalStateException("bad remote host port " + p);
        IHadoopJob job = HadoopJob.buildJob(
                mainClass,
                inputPath,     // data on hdfs
                jobDirectory,      // jar location
                outputLocation,
                "-D",
                XTandemHadoopUtilities.MAX_SCORED_PEPTIDES_KEY + "=" + XTandemHadoopUtilities.getMaxScoredPeptides(),
                "-D",
                XTandemHadoopUtilities.PARAMS_KEY + "=" + getTaskParamsPath(),
                "-D",
                XTandemHadoopUtilities.HOST_KEY + "=" + remoteHost,
                "-D",
                XTandemHadoopUtilities.HOST_PORT_KEY + "=" + p,
                "-D",
                XTandemHadoopUtilities.PATH_KEY + "=" + getRemoteBaseDirectory() //remoteBaseDirectory

//                  "-D",
//                  "org.systemsbiology.reportfile=YeastReports/yeastreport.xml"  // report file
        );

        if (getJarFile() != null)
            job.setJarFile(getJarFile());
        // reuse the only jar file
        // job.setJarFile(job.getJarFile());

        incrementPassNumber();
        return job;
    }

    protected IHadoopJob buildJobPass2() {
        //  guaranteeRemoteFiles();
        Class<JXTantemPass2Runner> mainClass = JXTantemPass2Runner.class;
        File taskFile = getTaskFile();
        int p = getRemoteHostPort();
        //  if (p <= 0)
        //      throw new IllegalStateException("bad remote host port " + p);

        // jar is same as pass1
        HadoopJob.setJarRequired(false);

        IHadoopJob job = HadoopJob.buildJob(
                mainClass,
                getLastOutputLocation(),
                getRemoteBaseDirectory() + "/jobs",      // jar location
                getOutputLocation(),
                "-D",
                XTandemHadoopUtilities.PARAMS_KEY + "=" + getTaskParamsPath(),
                "-D",
                XTandemHadoopUtilities.HOST_KEY + "=" + getRemoteHost(),
                "-D",
                XTandemHadoopUtilities.HOST_PORT_KEY + "=" + p,
                "-D",
                XTandemHadoopUtilities.PATH_KEY + "=" + getRemoteBaseDirectory()

//                  "-D",
//                  "org.systemsbiology.reportfile=YeastReports/yeastreport.xml"  // report file
        );

        // reuse the only jar file
        job.setJarFile(getJarFile());
        incrementPassNumber();

        return job;
    }

    protected IHadoopJob buildRemoteConsolidatorJob() {
        Class<JXTantemConsolidator> mainClass = JXTantemConsolidator.class;

        // jar is same as pass1
        HadoopJob.setJarRequired(false);

        String s = getRemoteHost();
        int p = getRemoteHostPort();
        //  if (p <= 0)
        //      throw new IllegalStateException("bad remote host port " + p);
        String outputFileName = getOutputFileName();
        IHadoopJob job = HadoopJob.buildJob(
                mainClass,
                getLastOutputLocation(),
                getRemoteBaseDirectory() + "/jobs",      // jar location
                getOutputLocation(),     // data on hdfs
                //          getOutputLocation(3),
                "-D",
                XTandemHadoopUtilities.PARAMS_KEY + "=" + getTaskParamsPath(),
                "-D",
                XTandemHadoopUtilities.HOST_KEY + "=" + s,
                "-D",
                XTandemHadoopUtilities.HOST_PORT_KEY + "=" + p,
                "-D",
                XTandemHadoopUtilities.PATH_KEY + "=" + getRemoteBaseDirectory(),
                "-D",
                BiomlReporter.FORCED_OUTPUT_NAME_PARAMETER + "=" + outputFileName

//                  "-D",
//                  "org.systemsbiology.reportfile=YeastReports/yeastreport.xml"  // report file
        );

        // reuse the only jar file
        job.setJarFile(getJarFile());

        incrementPassNumber();
        return job;
    }


    public void expungeLocalDirectories() {
        for (int i = 0; i < NUMBER_STAGES; i++) {
            expungeLocalDirectory(i);

        }
    }

    protected void expungeLocalDirectory(final int index) {
        String outputLocation = getOutputLocation(index + 1);
        FileUtilities.expungeDirectory(outputLocation);
    }

    // handled in the job
//    public void expungeRemoteDirectories() {
//        String host = RemoteUtilities.getHost();
//        HDFSAccessor accessor = new HDFSAccessor(host);
//        for (int i = 0; i < NUMBER_STAGES; i++) {
//            expungeRemoteDirectory(accessor,i);
//
//        }
//    }

    protected void expungeRemoteDirectory(IFileSystem accessor, final int index) {
        String outputLocation = getOutputLocation(index + 1);
        accessor.expunge(outputLocation);

    }

    private static String gParamsFile;
    private static String gPassedBaseDirctory = System.getProperty("user.dir");
    private static boolean gTaskIsLocal = true;

    public static String getParamsFile() {
        return gParamsFile;
    }


    public static boolean isTaskLocal() {
        return gTaskIsLocal;
    }


    public static String getPassedBaseDirctory() {
        if (gPassedBaseDirctory == null)
            return null;
        return gPassedBaseDirctory.replace("\\", "/");
    }

    // params=tandem.params   remoteBaseDirectory=/user/howdah/JXTandem/data/largeSample

    protected static void handleArguments(String[] args) {
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            handleArgument(arg);
        }
    }

    protected static void handleArgument(final String pArg) {
        if (pArg.startsWith("params=")) {
            gParamsFile = pArg.substring("params=".length());
            try {
                String paramsFile = getParamsFile();
                InputStream is = buildInputStream(paramsFile);
                if (is == null)
                    throw new IllegalStateException("params file " + gParamsFile + " does not exist");
                is.close();
            }
            catch (IOException e) {
                throw new RuntimeException(e);

            }
            return;
        }
        if (pArg.startsWith("jar=")) {
            setPassedJarFile(pArg.substring("jar=".length()));
            return;
        }
        if (pArg.startsWith("config=")) {
            String cnfigfile = pArg.substring("config=".length());
            handleConfigurationFile(cnfigfile);
            return;
        }
        if (pArg.toLowerCase().equals("forcedatabaserebuild")) {
            setDatabaseRebuildForced(true);
            return;
        }
        if (pArg.toLowerCase().equals("builddatabaseonly")) {
            setDatabaseBuildOnly(true);
            return;
        }
        if (pArg.startsWith("remoteBaseDirectory=")) {
            gPassedBaseDirctory = pArg.substring("remoteBaseDirectory=".length());
            return;
        }

        else {
            throw new IllegalArgumentException("wrong argument provided: " + pArg);
        }


    }

    protected static void handleConfigurationFile(final String pCnfigfile) {
        InputStream describedStream = XTandemUtilities.getDescribedStream(pCnfigfile);

        Properties props = new Properties();
        try {
            props.load(describedStream);
        }
        catch (IOException e) {
            throw new RuntimeException(e);

        }
        for (String property : props.stringPropertyNames()) {
            String value = props.getProperty(property);
            handleValue(property, value);
        }
    }

    public static final String PARAMS_PROPERTY = "params";
    public static final String REMOTE_HOST_PROPERTY = "remoteHost";
    public static final String REMOTE_PORT_PROPERTY = "remotePort";
    public static final String REMOTE_USER_PROPERTY = "remoteUser";
    public static final String REMOTE_JOBTRACKER_PROPERTY = "remoteJobTracker";
    public static final String REMOTE_ENCRYPTED_PASSWORD_PROPERTY = "encryptedRemotePassword";
    public static final String REMOTE_PLAINTEXT_PASSWORD_PROPERTY = "plainTextRemotePassword";
    public static final String REMOTEDIRECTORY_PROPERTY = "remoteBaseDirectory";
    public static final String MAX_PEPTIDES_PROPERTY = "maxPeptideFragmentsPerReducer";
    public static final String MAX_SPLIT_SIZE_PROPERTY = "maxSplitSize";
    public static final String MAX_REDUCE_TASKS_PROPERTY = "maxReduceTasks";
    public static final String DELETE_OUTPUT_DIRECTORIES_PROPERTY = "deleteOutputDirectories";
    public static final String COMPRESS_INTERMEDIATE_FILES_PROPERTY = "compressIntermediateFiles";
    public static final String MAX_CKUSTER_MEMORY_PROPERTY = "maxClusterMemory";

    protected static void handleValue(final String pProperty, String pValue) {

        if (pProperty.startsWith("DEFINE_")) {
            String prop = pProperty.substring("DEFINE_".length());
            XTandemHadoopUtilities.setHadoopProperty(prop, pValue);
            return;
        }
        if (PARAMS_PROPERTY.equals(pProperty)) {
            gParamsFile = pValue;
            return;
        }
        if (REMOTE_HOST_PROPERTY.equals(pProperty)) {
            Preferences prefs = Preferences.userNodeForPackage(RemoteUtilities.class);
            prefs.put("host", pValue);
            return;
        }
        if (REMOTE_PORT_PROPERTY.equals(pProperty)) {
            Preferences prefs = Preferences.userNodeForPackage(RemoteUtilities.class);
            prefs.put("port", pValue);
            return;
        }
        if (REMOTE_USER_PROPERTY.equals(pProperty)) {
            Preferences prefs = Preferences.userNodeForPackage(RemoteUtilities.class);
            prefs.put("user", pValue);
            return;
        }
        if (REMOTE_JOBTRACKER_PROPERTY.equals(pProperty)) {
            Preferences prefs = Preferences.userNodeForPackage(RemoteUtilities.class);
            prefs.put("jobtracker", pValue);
            return;
        }
        if (REMOTE_ENCRYPTED_PASSWORD_PROPERTY.equals(pProperty)) {
            Preferences prefs = Preferences.userNodeForPackage(RemoteUtilities.class);
            prefs.put("password", pValue);
            return;
        }
        if (REMOTE_PLAINTEXT_PASSWORD_PROPERTY.equals(pProperty)) {
            Preferences prefs = Preferences.userNodeForPackage(RemoteUtilities.class);
            String encrypted = Encrypt.encryptString(pValue);
            prefs.put("password", encrypted);
            return;
        }
        if (REMOTEDIRECTORY_PROPERTY.equals(pProperty)) {
            String baseDir = pValue;
            gTaskIsLocal = false;
            if (pValue.startsWith("File://")) {
                gTaskIsLocal = true;
                baseDir = pValue.replace("File://", "");
            }
            if (pValue.endsWith("<LOCAL_DIRECTORY>")) {
                String local = new File(System.getProperty("user.dir")).getName();
                baseDir = baseDir.replace("<LOCAL_DIRECTORY>", local);
            }

            gPassedBaseDirctory = baseDir;
            return;
        }
        if (MAX_SPLIT_SIZE_PROPERTY.equals(pProperty)) {
            int value = Integer.parseInt(pValue);
            XTandemHadoopUtilities.setMaxSplitSize(value);
            return;
        }
        if (MAX_PEPTIDES_PROPERTY.equals(pProperty)) {
            int value = Integer.parseInt(pValue);
            XTandemHadoopUtilities.setMaxScoredPeptides(value);
            return;
        }
        if (MAX_REDUCE_TASKS_PROPERTY.equals(pProperty)) {
            int value = Integer.parseInt(pValue);
            XTandemHadoopUtilities.setMaxReduceTasks(value);
            return;
        }
        if (MAX_CKUSTER_MEMORY_PROPERTY.equals(pProperty)) {
            HadoopUtilities.setProperty(MAX_CKUSTER_MEMORY_PROPERTY, pValue);
            return;
        }
        if (MAX_CKUSTER_MEMORY_PROPERTY.equals(pProperty)) {
            HadoopUtilities.setProperty(MAX_CKUSTER_MEMORY_PROPERTY, pValue);
            return;
        }
        if (DELETE_OUTPUT_DIRECTORIES_PROPERTY.equals(pProperty)) {
            HadoopUtilities.setProperty(DELETE_OUTPUT_DIRECTORIES_PROPERTY, pValue);
            return;
        }
        if (COMPRESS_INTERMEDIATE_FILES_PROPERTY.equals(pProperty)) {
            setSequenceFilesUsed("true".equalsIgnoreCase(pValue));
            return;
        }

        throw new UnsupportedOperationException("Property " + pProperty + " with value " + pValue + " Not handled");
    }

    public static InputStream buildInputStream(String paramsFile) {
        XTandemUtilities.outputLine("reading params file " + paramsFile);
        if (paramsFile.startsWith("res://"))
            return XTandemUtilities.getDescribedStream(paramsFile);

        File test = new File(paramsFile);
        if (!test.exists()) {
            XTandemUtilities.outputLine("  params file does not exist " + test.getAbsolutePath());
            return null;
        }
        if (!test.canRead()) {
            XTandemUtilities.outputLine("  params file cannot be read " + test.getAbsolutePath());
            return null;
        }
        return XTandemUtilities.getDescribedStream(paramsFile);


    }

    // Call with
    // params=tandem.params remoteHost=Glados remoteBaseDirectory=/user/howdah/JXTandem/data/largeSample

    public static void main(String[] args) {
        if (args.length == 0) {
            usage();
            return;
        }
        if ("params=".equals(args[1])) {
            usage2();
            return;
        }

        ElapsedTimer total = new ElapsedTimer();


        try {
            handleArguments(args);
            String passedBaseDirctory = getPassedBaseDirctory();
            boolean isRemote = !isTaskLocal();
            boolean isAmazon = passedBaseDirctory.startsWith("s3n://");
            Configuration cfg = new Configuration();
            if (isRemote) {
                if (isAmazon) {
                    cfg.set("fs.default.name", "s3n://");
                }
                else {
                    cfg.set("fs.default.name", "hdfs://" + RemoteUtilities.getHost() + ":" + RemoteUtilities.getPort());

                }
            }
            else {
                cfg.set("fs.default.name", "file:///");    // use local

            }

            String paramsFile = getParamsFile();
            InputStream is = buildInputStream(paramsFile);
            if (is == null) {
                File test = new File(paramsFile);
                XTandemUtilities.outputLine("CANNOT RUN BECAUSE CANNOT READ PARAMS FILE " + test.getAbsolutePath());
                return;
            }
            JXTandemLauncher main = new JXTandemLauncher(is, paramsFile, cfg);

            if (getPassedJarFile() != null) {   // start with a jar file
                main.setJarFile(getPassedJarFile());
                main.setBuildJar(false);
            }
            ElapsedTimer elapsed = main.getElapsed();
            IHadoopController launcher = null;

            if (isRemote) {
                if (isAmazon) {
                    Object[] cargs = { "MyJobName"};
                    Class lClass = Class.forName("org.systemsbiology.aws.AWSMapReduceLauncher");
                    launcher = (IHadoopController )lClass.newInstance( );
                }
                else {
                    String host = RemoteUtilities.getHost();
                    int port = RemoteUtilities.getPort();
                    main.setRemoteHost(host);
                    main.setRemoteHostPort(port);
                    // make sure directory exists
                    IFileSystem access = new HDFSAccessor(host, port);
                    main.setAccessor(access);
                    String user = RemoteUtilities.getUser(); // "training";  //
                    String password = RemoteUtilities.getPassword(); // "training";  //
                    RemoteSession rs = new RemoteSession(host, user, password);
                    rs.setConnected(true);
                    launcher = new RemoteHadoopController(rs);

                }
                main.setRemoteBaseDirectory(passedBaseDirctory);

            }
            else {
                IFileSystem access = new LocalFileSystem(new File(main.getRemoteBaseDirectory()));
                main.setAccessor(access);
                main.setBuildJar(false);
                HadoopJob.setJarRequired(false);
                launcher = new LocalHadoopController();

            }
            IFileSystem accessor = main.getAccessor();
            accessor.guaranteeDirectory(passedBaseDirctory);

            main.setParamsPath(paramsFile);


            main.loadTaxonomy();
            main.setPassNumber(1);
            String outFile = main.getOutputFileName();


            String defaultBasePath = passedBaseDirctory; //RemoteUtilities.getDefaultPath() + "/JXTandem/JXTandemOutput";
            accessor.guaranteeDirectory(defaultBasePath);
            main.setOutputLocationBase(defaultBasePath + "/OutputData");
            if (isRemote) {
                // make sure directory exists
                // we will kill output directories to guarantee empty
                HDFSUtilities.setOutputDirectoriesPrecleared(true);
                //   main.expungeRemoteDirectories();
                elapsed.showElapsed("Finished Setup");
            }
            else {
                main.expungeLocalDirectories();

            }
            main.runJobs(launcher);

            //   XTandemDebugging.loadXTandemValues("log1.txt");

            File f = null;
            if (!isDatabaseBuildOnly() && passedBaseDirctory != null) {
                String hdfsPath = passedBaseDirctory + "/" + XTandemUtilities.asLocalFile(outFile);
                //       String asLocal = XTandemUtilities.asLocalFile("/user/howdah/JXTandem/data/SmallSample/yeast_orfs_all_REV01_short.2011_11_325_10_35_19.t.xml");
                //       String hdfsPathEric = passedBaseDirctory + "/" + "yeast_orfs_all_REV01_short.2011_11_325_10_35_19.t.xml";


                try {
                    //          f = main.readRemoteFile(hdfsPathEric, outFile);
                    f = main.readRemoteFile(hdfsPath, outFile);
                    if (f.length() < MAX_DISPLAY_LENGTH) {
                        String s = FileUtilities.readInFile(f);
                        XTandemUtilities.outputLine(s);
                    }
                    // read in the larger scans file
                    if (isReadScanFile()) {  // todo add a way to turn this on
                        IFileSystem acc = main.getAccessor();
                        if (acc.exists(hdfsPath)) {
                            outFile += ".scans";
                            f = main.readRemoteFile(hdfsPath, outFile);
                            XTandemUtilities.outputLine("Created output file " + f.getAbsolutePath());
                        }
                    }
                    HadoopTandemMain application = main.getApplication();
                    if ("yes".equals(application.getParameter(XTandemUtilities.WRITING_PEPXML_PROPERTY))) {
                        ITandemScoringAlgorithm[] algorithms = application.getAlgorithms();
                        for (int i = 0; i < algorithms.length; i++) {
                            ITandemScoringAlgorithm algorithm = algorithms[i];
                            String fileName = hdfsPath + "." + algorithm.getName() + ".pep.xml";
                            String outFile2 = main.getOutputFileName() + "." + algorithm.getName() + ".pep.xml";
                            main.readRemoteFile(fileName, outFile2);
                        }

                    }

                }
                catch (IllegalArgumentException e) {
                    XTandemUtilities.outputLine("Cannot copy remote file " + hdfsPath + " to local file " + outFile +
                            " because " + e.getMessage());
                    e.printStackTrace();
                    throw e;

                }
            }


            XTandemUtilities.outputLine("Fragment Database Size " + main.getTotalFragments());

            main.getElapsed().showElapsed("Capture Output", System.out);

            XTandemUtilities.outputLine();
            XTandemUtilities.outputLine();
            JXTandemStatistics stats = main.getStatistics();
            XTandemUtilities.outputLine(stats.toString());
            total.showElapsed("Total Time", System.out);

            // I think hadoop has launched some threads so we can shut down now
        }
        catch (Throwable e) {
            e.printStackTrace();
            if (e != e.getCause() && e.getCause() != null) {
                while (e != e.getCause() && e.getCause() != null) {
                    e = e.getCause();
                }
                XTandemUtilities.outputLine(e.getMessage());
                StackTraceElement[] stackTrace = e.getStackTrace();
                for (int i = 0; i < stackTrace.length; i++) {
                    StackTraceElement se = stackTrace[i];
                    XTandemUtilities.outputLine(se.toString());
                }
            }
        }
        finally {
            System.exit(0);
        }
    }


}
