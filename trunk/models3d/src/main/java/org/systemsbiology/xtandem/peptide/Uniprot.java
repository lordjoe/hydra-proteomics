package org.systemsbiology.xtandem.peptide;

import com.lordjoe.utilities.*;
import org.biojava.bio.seq.*;
import org.biojava.bio.symbol.*;
import org.systemsbiology.jmol.*;
import org.systemsbiology.xtandem.*;
import org.systemsbiology.xtandem.fragmentation.*;
import org.systemsbiology.xtandem.fragmentation.ui.*;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.logging.*;

/**
 * org.systemsbiology.xtandem.peptide.Uniprot
 * User: steven
 * Date: 8/16/12
 */
public class Uniprot {
    public static final Uniprot[] EMPTY_ARRAY = {};

    public static final double GOOD_FIT = 0.3;
    private static final String UNIPROT_SERVER = "http://www.uniprot.org/";
    private static final Logger LOG = Logger.getAnonymousLogger();

    public static final double MINIMUM_FRAGMENT_MASS = 600;
    public static final double MAXIMUM_FRAGMENT_MASS = 5000;
    public static final double MINIMUM_FRAGMENT_LENGTH = 6;
    public static final double MAXIMUM_FRAGMENT_LENGTH = 50;

    public static final ParameterNameValue[] EMPTY_PARAMETER_VALUES = new ParameterNameValue[0];

    public static final Comparator<Feature> BY_POSITION = new FeaturePositionComparator();
    public static final Comparator<Location> LOC_BY_POSITION = new LocationPositionComparator();


    public static Sequence getBioJavaSequence(String uiprotId) {
        String s = getSwissProtAnnotation(uiprotId);
        if (s == null || s.length() == 0)
            return null;
        Sequence ret = SwissProt.parseSequence(s);
        return ret;

    }

    public static String retrieveSequence(String uiprotId) {
        String location = UNIPROT_SERVER + "uniprot/" + uiprotId + ".fasta";
        try {
            String ret = retrieveData(location);
            if (ret.length() == 0)
                return null; // not found

            if (ret.startsWith(">")) {
                String[] items = ret.split("\n");
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < items.length; i++) {
                    String item = items[i];
                    if (item.startsWith(">"))
                        continue;
                    sb.append(item);
                }
                return sb.toString();
            }
            if (ret.contains("<h2 class=\"error\">404 Not Found</h2>"))
                return null; // not fount
            throw new IllegalStateException("bad uniprot response");
        }
        catch (IOException e) {
            return null;

        }
        catch (InterruptedException e) {
            return null;

        }
    }

    /**
     * choose lowest location
     */
    private static class FeaturePositionComparator implements Comparator<Feature> {
        @Override
        public int compare(Feature o1, Feature o2) {
            if (o1 == o2)
                return 0;
            Location l1 = o1.getLocation();
            Location l2 = o2.getLocation();
            int ret = LOC_BY_POSITION.compare(l1, l2);
            if (ret != 0)
                return ret;
            return o1.getType().compareTo(o2.getType());
        }
    }

    /**
     * choose lowest location
     */
    private static class LocationPositionComparator implements Comparator<Location> {
        @Override
        public int compare(Location o1, Location o2) {
            if (o1 == o2)
                return 0;
            Integer mn1 = o1.getMin();
            Integer mn2 = o2.getMin();
            int ret = mn1.compareTo(mn2);
            if (ret != 0)
                return ret;
            Integer mx1 = o1.getMax();
            Integer mx2 = o2.getMax();
            return mx1.compareTo(mx2);

        }
    }


    protected static String run(String tool, ParameterNameValue[] params)
            throws Exception {
        String location = buildLocation(tool, params);
        return retrieveData(location);
    }

    protected static String run(String tool, Collection<ParameterNameValue> params)
            throws Exception {
        String location = buildLocation(tool, params);
        return retrieveData(location);
    }

    public static String getSwissProtAnnotation(String uniprotid) {
        try {
            String location = buildLocation("uniprot/" + uniprotid + ".txt", EMPTY_PARAMETER_VALUES);
            return retrieveData(location);
        }
        catch (IOException e) {
            throw new RuntimeException(e);

        }
        catch (InterruptedException e) {
            throw new RuntimeException(e);

        }

    }

    protected static String retrieveDataX(String location) throws IOException, InterruptedException {
        StringBuilder builder = new StringBuilder();
        URL url = new URL(location);
        LOG.info("Submitting...");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        HttpURLConnection.setFollowRedirects(true);
        conn.setDoInput(true);
        conn.connect();

        int status = conn.getResponseCode();
        while (true) {
            int wait = 0;
            String header = conn.getHeaderField("Retry-After");
            if (header != null)
                wait = Integer.valueOf(header);
            if (wait == 0)
                break;
            LOG.info("Waiting (" + wait + ")...");
            conn.disconnect();
            Thread.sleep(wait * 1000);
            conn = (HttpURLConnection) new URL(location).openConnection();
            conn.setDoInput(true);
            conn.connect();
            status = conn.getResponseCode();
        }
        if (status == HttpURLConnection.HTTP_OK) {
            LOG.info("Got a OK reply");

            InputStream reader = conn.getInputStream();
            URLConnection.guessContentTypeFromStream(reader);
            int a = 0;
            while ((a = reader.read()) != -1) {
                builder.append((char) a);
            }
        }
        else
            LOG.severe("Failed, got " + conn.getResponseMessage() + " for "
                    + location);
        conn.disconnect();
        return builder.toString();
    }


    protected static String retrieveData(String location) throws IOException, InterruptedException {
        StringBuilder builder = new StringBuilder();
        URL url = new URL(location);
        LOG.info("Submitting...");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        HttpURLConnection.setFollowRedirects(true);
        conn.setDoInput(true);
        conn.connect();

        int status = conn.getResponseCode();
        while (true) {
            int wait = 0;
            String header = conn.getHeaderField("Retry-After");
            if (header != null)
                wait = Integer.valueOf(header);
            if (wait == 0)
                break;
            LOG.info("Waiting (" + wait + ")...");
            conn.disconnect();
            Thread.sleep(wait * 1000);
            conn = (HttpURLConnection) new URL(location).openConnection();
            conn.setDoInput(true);
            conn.connect();
            status = conn.getResponseCode();
        }
        if (status == HttpURLConnection.HTTP_OK) {
            LOG.info("Got a OK reply");

            InputStream reader = conn.getInputStream();
            LineNumberReader inp = new LineNumberReader(new InputStreamReader(reader));
            String line = inp.readLine();
            while (line != null) {
                builder.append(line);
                builder.append("\n");
                line = inp.readLine();
            }
        }
        else
            LOG.severe("Failed, got " + conn.getResponseMessage() + " for "
                    + location);
        conn.disconnect();
        return builder.toString();
    }

    private static String buildLocation(String tool, ParameterNameValue[] params) {
        StringBuilder locationBuilder = new StringBuilder(UNIPROT_SERVER + tool);
        if (params == null || params.length == 0)
            return locationBuilder.toString();
        locationBuilder.append("/?");
        boolean first = true;
        for (ParameterNameValue pv : params) {
            if (!first)
                locationBuilder.append('&');
            locationBuilder.append(pv.name).append('=').append(pv.value);
            first = false;
        }
        return locationBuilder.toString();
    }

    private static String buildLocation(String tool, Collection<ParameterNameValue> params) {
        StringBuilder locationBuilder = new StringBuilder(UNIPROT_SERVER + tool + "/?");
        boolean first = true;
        for (ParameterNameValue pv : params) {
            if (!first)
                locationBuilder.append('&');
            locationBuilder.append(pv.name).append('=').append(pv.value);
            first = false;
        }
        return locationBuilder.toString();
    }

    private static class ParameterNameValue {
        private final String name;
        private final String value;

        public ParameterNameValue(String name, String value)
                throws UnsupportedEncodingException {
            this.name = name;
            this.value = value;
            //          this.name = URLEncoder.encode(name, "UTF-8");
            //           this.value = URLEncoder.encode(value, "UTF-8");
        }
    }


    public static Uniprot getUniprot(String accession) {
        try {
            String s = run("uniprot", new ParameterNameValue[]{
                    //           new ParameterNameValue("format", "tab"),
                    new ParameterNameValue("query",
                            "accession:" + accession + "&format=tab&columns=id,database(PDB),sequence"),
            });
            final String[] split = s.split("\n");
            if (split.length != 2)
                return null;
            if (!split[0].startsWith("Entry"))
                return null;
            final String query = split[1];
            final String[] split1 = query.split("\t");
            if (split1.length != 3)
                return null;
            Uniprot ret = new Uniprot(split1);
            Sequence sq = getBioJavaSequence(accession);
            if (sq != null)
                ret.setSequence(sq);
            return ret;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


    private final Protein m_Protein;
    private final ProteinAminoAcid[] m_AminoAcids;
    private final String[] m_Models;

    private final Map<String, BioJavaModel> m_IdToMpdel = new HashMap<String, BioJavaModel>();
    private final File m_ModelDirectory = new File("Models3D");
    private BioJavaModel m_BestModel;
    private final Set<IPolypeptide> m_Detected = new HashSet<IPolypeptide>();
    private final Set<FoundPeptide> m_Found = new HashSet<FoundPeptide>();
    private final Set<IPolypeptide> m_Theoretical = new HashSet<IPolypeptide>();
    private final Set<String> m_TheoreticalSequences = new HashSet<String>();
    private boolean m_BadPeptide;
    private Sequence m_Sequence;
    private Feature[] m_InterestingFeatures;
    private boolean m_Analyzed;

    public Uniprot(String line) {
        this(line.split("\t"));
    }

    public Uniprot(String[] split1) {
        final String annotation = split1[0];
        final String sequence = split1[2].replace(" ", "");
        String pdbs = split1[1].trim();
        if (pdbs.length() == 0)
            m_Models = new String[0];
        else {
            m_Models = pdbs.split(";");
        }
        if (!m_ModelDirectory.isDirectory())
            throw new IllegalStateException("Model directory " + m_ModelDirectory + " does not exist");

        m_Protein = Protein.buildProtein(annotation, annotation, sequence, "");
        buildTheoreticalPeptides(PeptideBondDigester.getDigester("Trypsin"));
        int seqLength = sequence.length();
        FastaAminoAcid[] fastaAminoAcids = FastaAminoAcid.asAminoAcids(sequence);
        m_AminoAcids = new ProteinAminoAcid[fastaAminoAcids.length];
        for (int i = 0; i < fastaAminoAcids.length; i++) {
            FastaAminoAcid aa = fastaAminoAcids[i];
            m_AminoAcids[i] = new ProteinAminoAcid(aa, i);
            // cleavage code - not for lastt
            if (i == fastaAminoAcids.length - 1)
                continue;
            if (aa == FastaAminoAcid.K || aa == FastaAminoAcid.R) {
                if (fastaAminoAcids[i + 1] != FastaAminoAcid.P)
                    m_AminoAcids[i].setPotentialCleavage(true);
            }

        }

    }

    private static int gTotalUnacceptable;
    private static int gTotalPrptides;

    protected void buildTheoreticalPeptides(PeptideBondDigester trypsin) {
        IPolypeptide[] digest = trypsin.digest(getProtein());
        int numberUnacceptable = 0;
        for (int i = 0; i < digest.length; i++) {
            IPolypeptide pp = digest[i];
            if (isPeptideAcceptable(pp)) {
                m_Theoretical.add(pp);
                m_TheoreticalSequences.add(pp.getSequence());
            }
            else {
                numberUnacceptable++;
            }
        }
        gTotalPrptides += digest.length;
        gTotalUnacceptable += numberUnacceptable;
    }

    public boolean isBadPeptide() {
        return m_BadPeptide;
    }

    public void setBadPeptide(boolean badPeptide) {
        m_BadPeptide = badPeptide;
    }

    public Sequence getSequence() {
        return m_Sequence;
    }

    public boolean isAnalyzed() {
        return m_Analyzed;
    }

    public void setSequence(Sequence sequence) {
        m_Sequence = sequence;
        Feature[] fts = SwissProt.getAnalyzedFeatures(sequence);
        List<Feature> holder = new ArrayList<Feature>();

        for (int i = 0; i < fts.length; i++) {
            Feature ft = fts[i];
            holder.add(ft);
            handleFeature(ft);
        }
        m_InterestingFeatures = new Feature[holder.size()];
        holder.toArray(m_InterestingFeatures);
        Arrays.sort(m_InterestingFeatures, BY_POSITION);
    }

    protected void handleFeature(Feature ft) {
        UniprotFeatureType type = SwissProt.getFeatureType(ft);
        switch (type) {
            case TRANSMEM:
            case STRAND:
            case TURN:
            case HELIX:
                handleFeature(ft, type);
                break;
            default:
                handleFeature(ft, type);
                //        throw new IllegalStateException("never get here");
        }
    }

    protected void handleFeature(Feature ft, UniprotFeatureType expected) {
        UniprotFeatureType type = SwissProt.getFeatureType(ft);

        if (type != expected)
            throw new IllegalArgumentException("must have type " + expected);
        setFeatureAtLocation(ft, type);
    }


    private void setFeatureAtLocation(Feature ft, UniprotFeatureType type) {
        Location location = ft.getLocation();
        int min = location.getMin() - 1;
        int max = location.getMax();
        ProteinAminoAcid[] aminoAcids = getAminoAcids();
        switch (type) {
            case DISULFID:
                ProteinAminoAcid mnaa = aminoAcids[min];
                if (mnaa.getAminoAcid() != FastaAminoAcid.C)
                    throw new IllegalStateException("should be cys");
                mnaa.addFeature(type);
                ProteinAminoAcid mxaa = aminoAcids[max - 1];
                if (mxaa.getAminoAcid() != FastaAminoAcid.C)
                    throw new IllegalStateException("should be cys");
                mxaa.addFeature(type);
                break;
            case ZN_FING:
                aminoAcids[min].addFeature(type);
                aminoAcids[max - 1].addFeature(type);
                break;
            default:
                for (int i = min; i < max; i++) {
                    if (!location.contains(i))
                        continue;
                    ProteinAminoAcid aminoAcid = aminoAcids[i];
                    aminoAcid.addFeature(type);
                }
        }
    }

    protected boolean isPeptideAcceptable(IPolypeptide pp) {
        int length = pp.getSequenceLength();
        if (length < MINIMUM_FRAGMENT_LENGTH)
            return false;
        if (length > MAXIMUM_FRAGMENT_LENGTH)
            return false;
        return true;
    }

    protected String getDelimitedModels() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < m_Models.length; i++) {
            if (i > 0)
                sb.append(";");
            sb.append(m_Models[i]);

        }
        return sb.toString();
    }

    public boolean isDetected(IPolypeptide pp) {
        return m_Detected.contains(pp);
    }

    public boolean isAminoAcidDetected(int index) {
        return m_AminoAcids[index].isDetected();
    }

    public FastaAminoAcid getAnimoAcid(int index) {
        return m_AminoAcids[index].getAminoAcid();
    }

    public ProteinAminoAcid[] getAminoAcids() {
        return m_AminoAcids;
    }


    public int getDetectedCount() {
        int sum = 0;
        ProteinAminoAcid[] aminoAcids = getAminoAcids();
        for (int i = 0; i < aminoAcids.length; i++) {
            boolean d = aminoAcids[i].isDetected();
            if (d)
                sum++;
        }
        return sum;
    }

    public int getFirstDetected() {
        ProteinAminoAcid[] aminoAcids = getAminoAcids();
        for (int i = 0; i < aminoAcids.length; i++) {
            boolean d = aminoAcids[i].isDetected();
            if (d)
                return i;
        }
        return -1;
    }

    public int getLastDetected() {
        ProteinAminoAcid[] aminoAcids = getAminoAcids();
        for (int i = aminoAcids.length - 1; i >= 0; i--) {
            boolean d = aminoAcids[i].isDetected();
            if (d)
                return i;
        }
        return -1;
    }

    public IPolypeptide[] getTheoretical() {
        IPolypeptide[] iPolypeptides = m_Theoretical.toArray(IPolypeptide.EMPTY_ARRAY);
        Arrays.sort(iPolypeptides, Polypeptide.STRING_COMPARATOR);
        return iPolypeptides;

    }

    public IPolypeptide[] getDetected() {
        IPolypeptide[] iPolypeptides = m_Detected.toArray(IPolypeptide.EMPTY_ARRAY);
        Arrays.sort(iPolypeptides, Polypeptide.STRING_COMPARATOR);
        return iPolypeptides;

    }


    public FoundPeptide[] getFound() {
        FoundPeptide[] iPolypeptides = m_Found.toArray(FoundPeptide.EMPTY_ARRAY);
        return iPolypeptides;

    }

    public Protein getProtein() {
        return m_Protein;
    }

    public String[] getModels() {
        return m_Models;
    }

    public File getModelDirectory() {
        return m_ModelDirectory;
    }

    public static boolean gDownloadModels = false;

    /**
     * if true unknown models will be downloaded
     *
     * @return
     */
    public static boolean isDownloadModels() {
        return gDownloadModels;
    }

    /**
     * if true unknown models will be downloaded
     *
     * @return
     */
    public static void setDownloadModels(final boolean downloadModels) {
        gDownloadModels = downloadModels;
    }

    /**
     * @param id
     * @return
     * @throws IllegalArgumentException
     */
    public BioJavaModel getModel(String id) throws IllegalArgumentException {
        BioJavaModel mdl = m_IdToMpdel.get(id);
        if (mdl == null) {
            mdl = new BioJavaModel(getProtein());
            File mdr = getModelDirectory();
            File f = new File(mdr, id + ".pdb");
            if (f.exists()) {
                mdl.readFile(f);
            }
            else {
                if (!isDownloadModels())
                    return null;

                f = ThreeDModel.downLoad3DModel(mdr, id);
                if (f.exists()) {
                    mdl.readFile(f);
                }

            }
            m_IdToMpdel.put(id, mdl);
        }
        return mdl;
    }

    @Override
    public String toString() {
        return
                m_Protein.getId() + "\t" +
                        getDelimitedModels() + "\t" +
                        m_Protein.getSequence();


    }

    public static int gDidReanalysis;

    /**
     * Walk through onr bad case
     */
    public void reanalyze() {
        if (gDidReanalysis > 1)
            return;
        gDidReanalysis++;
        int[] statistics = new int[3];
        analyze(statistics);   // break here
    }

    public boolean isGoodFit() {
        int sequencelen = getProtein().getSequenceLength();
        BioJavaModel bestModel = internalGettBestModel();
        if (bestModel != null)
            return true;

        BioJavaModel[] mdls = getAllModels();

        for (int i = 0; i < mdls.length; i++) {
            BioJavaModel mdl = mdls[i];
            int fitLength = mdl.getFitLength();
            if (fitLength > sequencelen / 2)
                return true;
            if (fitLength > 50)
                return true;
        }
        return false;
    }


    public BioJavaModel[] getAllModels() {
        List<BioJavaModel> holder = new ArrayList<BioJavaModel>();
        final String[] models = getModels();
        for (int j = 0; j < models.length; j++) {
            String model = models[j];
            try {
                BioJavaModel mdl = getModel(model);
                if (mdl != null)
                    holder.add(mdl);
            }
            catch (IllegalArgumentException e) {
                // forgive
            }
//                ThreeDModel.downLoad3DModel(model);
//
        }

        BioJavaModel[] ret = new BioJavaModel[holder.size()];
        holder.toArray(ret);
        return ret;
    }

    public BioJavaModel getBestModel() {
        if (!isAnalyzed())
            analyze();
        return internalGettBestModel();
    }

    protected BioJavaModel internalGettBestModel() {
        return m_BestModel;
    }


    public void addFoundPeptide(IPolypeptide peptide) {
        m_Detected.add(peptide);
        String pst = peptide.getSequence();
        Protein protein = getProtein();
        String id = protein.getId();
        m_Found.add(new FoundPeptide(peptide, id, 0));
        String prost = protein.getSequence();
        int index = prost.indexOf(pst);
        if (index == -1) {
            //  System.out.println("BAD PEPTIDE " + pst + "  NOT IN " + id);
            setBadPeptide(true);
            return;
            // throw new IllegalStateException("sequence needs ot occur once " + pst);
        }
        while (index > -1) {
            int endIndex = index + pst.length();
            if (index > 0 && m_AminoAcids[index - 1].isPotentialCleavage())
                m_AminoAcids[index - 1].setObservedCleavage(true);

            for (int i = index; i < endIndex; i++) {
                ProteinAminoAcid aa = m_AminoAcids[i];
                aa.setDetected(true);
                // ignore missed near start or end
                if (aa.isPotentialCleavage() && i > index + 1) {
                    if (i < endIndex - 1) {
                        if (i < endIndex - 2) {
                            aa.setMissedCleavage(true);
                            //                    System.out.println(peptide.getSequence());
                        }
                    }
                    else
                        aa.setObservedCleavage(true);
                }
            }
            index = prost.indexOf(pst, index + 1);
        }
    }

    public int[] analyze() {
        final int[] statistics = new int[3];
        analyze(statistics);
        return statistics;
    }

    public void analyze(final int[] statistics) {
        Protein protein = getProtein();
        String id = protein.getId();
        final String[] models = getModels();
        if (models.length == 0) {
            statistics[NONE]++;
            return;
        }
        double bestFit = 0;
        BioJavaModel[] mdls = getAllModels();
        if (mdls.length == 0) {
            clearModels();
            return;
        }
        BioJavaModel best = null;
        int sequencelen = getProtein().getSequenceLength();
        for (int i = 0; i < mdls.length; i++) {
            BioJavaModel mdl = mdls[i];
            if (mdl == null)
                continue;
            int fitLength = mdl.getFitLength();
            double fraction = mdl.resolveChains();
            if (fraction > bestFit) {
                bestFit = fraction;
                best = mdl;
            }
            bestFit = Math.max(bestFit, fraction);
        }
        m_BestModel = best;
        if (bestFit > GOOD_FIT || isGoodFit()) {
            statistics[GOOD]++;
            m_BestModel = best;

            System.out.println("" + protein.getId() + " matches " +
                    String.format("%5.3f", bestFit) + " of " + protein.getSequenceLength()
            );
            if (bestFit < 1.05 * GOOD_FIT)
                reanalyze();
        }
        else {
            statistics[BAD]++;
            m_BestModel = null;

        }

        clearModels();
        m_Analyzed = true;
    }


    public void clearModels() {
        m_IdToMpdel.clear();
    }

    public String getId() {
        return getProtein().getId();
    }


    public static Map<String, Uniprot> readUniprots(File inp) {
        String[] lines = FileUtilities.readInLines(inp);
        Map<String, Uniprot> holder = new HashMap<String, Uniprot>();
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            Uniprot up = new Uniprot(line);
            Protein protein = up.getProtein();
            holder.put(up.getId(), up);
        }
        return holder;

    }

    private static void downloadUniprots(String pArg) throws IOException {
        File inp = new File("GoodProteins.txt");
        if (!inp.exists())
            throw new IllegalArgumentException("input file " + pArg + " does not exist");


        File outf = new File("Origene.tsv");
        PrintWriter out = new PrintWriter(new FileWriter(outf));
        String[] proteins = FileUtilities.readInLines(inp);
        for (String id : proteins) {
            Uniprot up = getUniprot(id);
            if (up != null) {
                final String x = up.toString();
                out.println(x);

            }
            else {
                System.err.println("Cannot find " + id);
            }
        }
        out.close();
    }

    public static Uniprot[] writeProteins(String[] proteins, PrintWriter out) {

        List<Uniprot> holder = new ArrayList<Uniprot>();

        for (String id : proteins) {
            Uniprot up = getUniprot(id);
            if (up != null) {
                final String x = up.toString();
                out.println(x);
                holder.add(up);
            }
            else {
                System.err.println("Cannot find " + id);
            }
        }
        out.close();
        Uniprot[] ret = new Uniprot[holder.size()];
        holder.toArray(ret);
        return ret;
    }

    public static Uniprot[] writeProteins(String[] proteins, File outf) {

        try {
            PrintWriter out = new PrintWriter(new FileWriter(outf));
            return writeProteins(proteins, out);
        }
        catch (IOException e) {
            throw new RuntimeException(e);

        }
    }

    public static Uniprot[] getGoodModels(Uniprot[] pts) {
        int[] statistics = new int[3];
        List<Uniprot> holder = new ArrayList<Uniprot>();

        for (int i = 0; i < pts.length; i++) {
            Uniprot pt = pts[i];
            pt.analyze(statistics);
            if (pt.isGoodFit())
                holder.add(pt);
        }
        Uniprot[] ret = new Uniprot[holder.size()];
        holder.toArray(ret);
        System.out.println(
                "good " + statistics[GOOD] +
                        " bad " + statistics[BAD] +
                        " none " + statistics[NONE]
        );
        return ret;
    }

    public static final int MAX_BUILT_PAGES = 100000;

    public static void buildModelPages(Uniprot[] pts, File inp) {
        FoundPeptides fps = null;
        if (inp != null)
            fps = FoundPeptides.readFoundPeptides(inp);
        Protein[] proteins = new Protein[pts.length];
        String[] ids = new String[pts.length];
        ProteinCollection pc = new ProteinCollection();
        File pdbDirectory = pc.getPDBDirectory();
        int count = 0;
        for (int i = 0; i < ids.length; i++) {
            Uniprot pt = pts[i];
            Protein protein = pt.getProtein();
            proteins[i] = protein;
            String id = protein.getId();
            FoundPeptide[] peptides = FoundPeptide.EMPTY_ARRAY;
            if (fps != null)
                peptides = fps.getPeptides(id);

            ProteinFragmentationDescription pfd = new ProteinFragmentationDescription(id, pc, protein, peptides);
            int index = 0;
            for (int j = 0; j < peptides.length; j++) {
                FoundPeptide peptide = peptides[j];
                IPolypeptide peptide1 = peptide.getPeptide();
                index = pfd.addFragment(protein, peptide1, index);
            }
            pc.addProteinFragmentationDescription(pfd);
            ids[i] = id;
            BioJavaModel bestModel = pt.getBestModel();
            if (bestModel != null) {
                File added = new File(pdbDirectory, bestModel.getPdbCode() + ".pdb");
                pc.addPDBModelFile(id, added);

            }
            if (count++ > MAX_BUILT_PAGES)
                break;

        }
        ProteinCoveragePageBuilder pb = new ProteinCoveragePageBuilder(pc);
        pb.buildPages(ids);

    }

    private static void buildPages(File inp) {
        Map<String, Uniprot> stringUniprotMap = readUniprots(inp);
        Uniprot[] pts = stringUniprotMap.values().toArray(Uniprot.EMPTY_ARRAY);
        Uniprot[] gm = getGoodModels(pts);
//        buildModelPages(gm,inp);
        buildModelPages(pts, null);
    }

    public static Uniprot buildUniprot(String id) {
        Uniprot up = getUniprot(id);

        return up;
    }


    private static void getUniptotsFromCommaSeparated(String[] args) {
        File outf = new File(args[0]);
        String id = args[1];
        String[] items = id.split(",");
        Uniprot[] ret = writeProteins(items, outf);
        for (int i = 0; i < ret.length; i++) {
            Uniprot uniprot = ret[i];

        }
    }


    public static final String HEPC_IDS =
            "H0YCK3,Q01629,F5H8E8.G3V3I4,Q9NP84,P06756,P42224,P12757,P31689,Q9UHN6" +
                    ",Q06787,P23246,O14879,P55210,Q01629,Q9Y6K5,P25963,Q8WYK2,Q03169" +
                    ",P07996,Q13287,Q13287,P06756,Q9BYX4,P42224,O15162,Q96DX8,P10451" +
                    ",Q9NYY3,P47928,O15205,O95786,P31689,Q9UHN6,Q14142,P12757 ";

    public static final int GOOD = 0;
    public static final int BAD = 1;
    public static final int NONE = 2;


    public static Map<String, Uniprot> findUnterestingUniprots(File sp, Map<String, Uniprot> idToUniprot) {
        Sequence[] ret = SwissProt.readInterestingSequences(sp);
        Map<String, Uniprot> testUniProts = new HashMap<String, Uniprot>();
        for (int i = 0; i < ret.length; i++) {
            Sequence sequence = ret[i];
            String name = sequence.getName();
            Uniprot pt = idToUniprot.get(name);
            if (pt == null || pt.isBadPeptide()) {
                //           System.out.println("Bad id " + name);
                continue;
            }
            pt.setSequence(sequence);
            testUniProts.put(name, pt);

        }
        return testUniProts;
        //    return idToUniprot.values().toArray(Uniprot.EMPTY_ARRAY);
    }

    public boolean hasFeature(UniprotFeatureType ft) {
        for (int i = 0; i < m_AminoAcids.length; i++) {
            ProteinAminoAcid aminoAcid = m_AminoAcids[i];
            if (aminoAcid.hasFeature(ft))
                return true;
        }
        return false;
    }

    public int countFeature(UniprotFeatureType ft) {
        int count = 0;
        for (int i = 0; i < m_AminoAcids.length; i++) {
            ProteinAminoAcid aminoAcid = m_AminoAcids[i];
            if (aminoAcid.hasFeature(ft))
                count++;
        }
        return count;
    }

    public int countObservedCleavageFeature(UniprotFeatureType ft) {
        int count = 0;
        for (int i = 0; i < m_AminoAcids.length; i++) {
            ProteinAminoAcid aminoAcid = m_AminoAcids[i];
            if (!aminoAcid.isObservedCleavage())
                continue;
            if (aminoAcid.hasFeature(ft))
                count++;
        }
        return count;
    }

    public int countPotentialCleavageFeature(UniprotFeatureType ft) {
        int count = 0;
        for (int i = 0; i < m_AminoAcids.length; i++) {
            ProteinAminoAcid aminoAcid = m_AminoAcids[i];
            if (!aminoAcid.isPotentialCleavage())
                continue;
            if (aminoAcid.hasFeature(ft))
                count++;
        }
        return count;
    }

    public int countDetectedFeature(UniprotFeatureType ft) {
        int count = 0;
        for (int i = 0; i < m_AminoAcids.length; i++) {
            ProteinAminoAcid aminoAcid = m_AminoAcids[i];
            if (!aminoAcid.isDetected())
                continue;
            if (aminoAcid.hasFeature(ft))
                count++;
        }
        return count;
    }

    public int countDetected() {
        int count = 0;
        for (int i = 0; i < m_AminoAcids.length; i++) {
            ProteinAminoAcid aminoAcid = m_AminoAcids[i];
            if (!aminoAcid.isDetected())
                continue;
            count++;
        }
        return count;
    }

    public int countObservedCleavages() {
        int count = 0;
        for (int i = 0; i < m_AminoAcids.length; i++) {
            ProteinAminoAcid aminoAcid = m_AminoAcids[i];
            if (!aminoAcid.isObservedCleavage())
                continue;
            count++;
        }
        return count;
    }

    public int countMissedCleavages() {
        int count = 0;
        for (int i = 0; i < m_AminoAcids.length; i++) {
            ProteinAminoAcid aminoAcid = m_AminoAcids[i];
            if (!aminoAcid.isMissedCleavage())
                continue;
            count++;
        }
        return count;
    }

    public int countPotentialCleavages() {
        int count = 0;
        for (int i = 0; i < m_AminoAcids.length; i++) {
            ProteinAminoAcid aminoAcid = m_AminoAcids[i];
            if (!aminoAcid.isPotentialCleavage())
                continue;
            count++;
        }
        return count;
    }

    public int getDetectedPeptideCount() {
        return m_Detected.size();
    }

    public int getTheoreticalPeptideCount() {
        return m_Theoretical.size();
    }


    public int getSequenceLength() {
        return getProtein().getSequenceLength();
    }

    public static Map<UniprotFeatureType, FisherTable> handleFeatures(Uniprot[] pts) {
        UniprotFeatureType[] fts = SwissProt.ANALYZED_FEATURES;
        Map<UniprotFeatureType, FisherTable> ret = new HashMap<UniprotFeatureType, FisherTable>();
        for (int i = 0; i < fts.length; i++) {
            UniprotFeatureType ft = fts[i];
            FisherTable fisherTable = handleFeatures(pts, ft);
            ret.put(ft, fisherTable);
        }
        return ret;
    }

    public static Map<UniprotFeatureType, FisherTable> handleObservedCleavages(Uniprot[] pts) {
        UniprotFeatureType[] fts = SwissProt.ANALYZED_FEATURES;
        Map<UniprotFeatureType, FisherTable> ret = new HashMap<UniprotFeatureType, FisherTable>();
        for (int i = 0; i < fts.length; i++) {
            UniprotFeatureType ft = fts[i];
            FisherTable fisherTable = handleObservedCleavages(pts, ft);
            ret.put(ft, fisherTable);
        }
        return ret;
    }

    public static Map<UniprotFeatureType, FisherTable> handleMissedCleavages(Uniprot[] pts) {
        UniprotFeatureType[] fts = SwissProt.ANALYZED_FEATURES;
        Map<UniprotFeatureType, FisherTable> ret = new HashMap<UniprotFeatureType, FisherTable>();
        for (int i = 0; i < fts.length; i++) {
            UniprotFeatureType ft = fts[i];
            FisherTable fisherTable = handleMissedCleavages(pts, ft);
            ret.put(ft, fisherTable);
        }
        return ret;
    }

    protected void addToTable(UniprotFeatureType ft, FisherTable tb) {
        int ftDetected = 0;
        int ftNotDetected = 0;
        int notFtDetected = 0;
        int notFtNotDetected = 0;

        for (int i = 0; i < m_AminoAcids.length; i++) {
            ProteinAminoAcid aminoAcid = m_AminoAcids[i];
            if (aminoAcid.hasFeature(ft)) {
                if (aminoAcid.isDetected()) {
                    ftDetected++;
                }
                else {
                    ftNotDetected++;
                }

            }
            else {
                if (aminoAcid.isDetected()) {
                    notFtDetected++;
                }
                else {
                    notFtNotDetected++;
                }

            }
        }
        tb.addTo(notFtNotDetected, notFtDetected, ftNotDetected, ftDetected);

    }

    protected void addObservedCleavagesToTable(UniprotFeatureType ft, FisherTable tb) {
        int ftDetected = 0;
        int ftNotDetected = 0;
        int notFtDetected = 0;
        int notFtNotDetected = 0;

        for (int i = 0; i < m_AminoAcids.length; i++) {
            ProteinAminoAcid aminoAcid = m_AminoAcids[i];
            if (!aminoAcid.isPotentialCleavage())
                continue;
            if (aminoAcid.hasFeature(ft)) {
                if (aminoAcid.isObservedCleavage()) {
                    ftDetected++;
                }
                else {
                    ftNotDetected++;
                }

            }
            else {
                if (aminoAcid.isObservedCleavage()) {
                    notFtDetected++;
                }
                else {
                    notFtNotDetected++;
                }

            }
        }
        tb.addTo(notFtNotDetected, notFtDetected, ftNotDetected, ftDetected);

    }


    protected void addMissedCleavagesToTable(UniprotFeatureType ft, FisherTable tb) {
        int ftDetected = 0;
        int ftNotDetected = 0;
        int notFtDetected = 0;
        int notFtNotDetected = 0;

        for (int i = 0; i < m_AminoAcids.length; i++) {
            ProteinAminoAcid aminoAcid = m_AminoAcids[i];
            if (!aminoAcid.isPotentialCleavage())
                continue;
            if (aminoAcid.hasFeature(ft)) {
                if (aminoAcid.isMissedCleavage()) {
                    ftDetected++;
                }
                else {
                    ftNotDetected++;
                }

            }
            else {
                if (aminoAcid.isMissedCleavage()) {
                    notFtDetected++;
                }
                else {
                    notFtNotDetected++;
                }

            }
        }
        tb.addTo(notFtNotDetected, notFtDetected, ftNotDetected, ftDetected);

    }

    private static FisherTable handleFeatures(Uniprot[] pts, UniprotFeatureType ft) {
        int numberProteinsShowing = 0;
        List<Double> featureDetected = new ArrayList<Double>();
        List<Double> totalDetected = new ArrayList<Double>();

        FisherTable ret = new FisherTable();

        for (int i = 0; i < pts.length; i++) {
            Uniprot pt = pts[i];
            //          if(!pt.hasFeature(ft)) continue;


            int seen = pt.countFeature(ft);
            int dc = pt.countDetected();
            if (dc == 0 || seen == 0)
                continue;
            numberProteinsShowing++;
            int detected = pt.countDetectedFeature(ft);
            double frac = (double) detected / (double) seen;
            featureDetected.add(frac);

            int sequenceLength = pt.getSequenceLength();
            double total = (double) dc / (double) sequenceLength;
            totalDetected.add(total);

            pt.addToTable(ft, ret);


        }
        Double[] fd = new Double[featureDetected.size()];
        featureDetected.toArray(fd);

        Double[] td = new Double[totalDetected.size()];
        totalDetected.toArray(td);

//        System.out.println(ft);
//        for (int i = 0; i < td.length; i++) {
//            Double tdb = td[i];
//            Double fdb = fd[i];
//            System.out.println(String.format("%6.3f", fdb) + " of " + String.format("%6.3f", tdb));
//        }
        return ret;
    }


    private static FisherTable handleObservedCleavages(Uniprot[] pts, UniprotFeatureType ft) {

        FisherTable ret = new FisherTable();

        for (int i = 0; i < pts.length; i++) {
            Uniprot pt = pts[i];
            //        if(!pt.hasFeature(ft)) continue;

            pt.addObservedCleavagesToTable(ft, ret);
        }
        return ret;
    }

    private static FisherTable handleMissedCleavages(Uniprot[] pts, UniprotFeatureType ft) {

        FisherTable ret = new FisherTable();

        for (int i = 0; i < pts.length; i++) {
            Uniprot pt = pts[i];
            //          if(!pt.hasFeature(ft)) continue;

            pt.addMissedCleavagesToTable(ft, ret);
        }
        return ret;
    }

    private static void showFeatures(Map<UniprotFeatureType, FisherTable> mpx) {
        UniprotFeatureType[] fts = SwissProt.ANALYZED_FEATURES;
        for (int i = 0; i < fts.length; i++) {
            UniprotFeatureType ft = fts[i];
            FisherTable fisherTable = mpx.get(ft);
            double prob = fisherTable.getProbability();
            double total = fisherTable.sum();
            if (total == 0)
                return;
            int[] values = fisherTable.getValues();
            double detected = (values[FisherTable.NOT_PRESENT_DECTECTED] + values[FisherTable.PRESENT_DECTECTED]) / total;
            double pFeature = (double) (values[FisherTable.PRESENT_NOT_DECTECTED] + values[FisherTable.PRESENT_DECTECTED]);
            if (pFeature == 0)
                return;
            double fdetected = (values[FisherTable.PRESENT_DECTECTED]) / pFeature;

            System.out.println(ft.toString() +
                    " prob " + String.format("%6.1e", prob) +
                    " fdetect " + String.format("%6.3f", fdetected) +
                    " detect " + String.format("%6.3f", detected) +
                    " number " + (int) total +
                    " detected " + values[FisherTable.PRESENT_DECTECTED]


            );

        }
    }

    public static final String SAMPLE = "java Uniprot" + "  Origene.tsv GoodPeptides.txt  OrigeneData.dat";

    public static void main(String[] args) throws IOException {
        // downloadUniprots(args[0]);


        if (args.length < 3) {
            UsageGenerator.showUsage(SAMPLE,
                    "proteins <tab delimited uniptotid\tsequence>",
                    "peptides <tab delimited uniptotid\tsequence>",
                    "sp <todo fix>"

            );
            return;
        }

        File inp = new File(args[0]);
        FileUtilities.guaranteeExistingFile(inp);

        File peptides = new File(args[1]);
        FileUtilities.guaranteeExistingFile(peptides);

        File sp = new File(args[2]);
        FileUtilities.guaranteeExistingFile(sp);


        int nBad = 0;

        FoundPeptides fps = FoundPeptides.readFoundPeptides(peptides);
        Map<String, Uniprot> idToUniprot = readUniprots(inp);
        Uniprot[] pts = idToUniprot.values().toArray(Uniprot.EMPTY_ARRAY);
        for (int i = 0; i < pts.length; i++) {
            Uniprot pt = pts[i];
            Protein protein = pt.getProtein();
            String id = protein.getId();
            FoundPeptide[] peptides1 = fps.getPeptides(id);
            for (int j = 0; j < peptides1.length; j++) {
                FoundPeptide fp = peptides1[j];
                pt.addFoundPeptide(fp.getPeptide());
            }
            int len = protein.getSequenceLength();
            int nd = pt.getDetectedCount();
            int fd = pt.getFirstDetected();
            int ld = pt.getLastDetected();
            if (pt.isBadPeptide()) {
                nBad++;
                idToUniprot.remove(pt.getId());
                continue;
            }
            double coverage = (double) nd / (double) len;
            //    System.out.println(pt.getId() + " " + String.format("%6.3f", coverage));

        }

        Map<String, Uniprot> unterestingUniprots = findUnterestingUniprots(sp, idToUniprot);
        Uniprot[] interesting = unterestingUniprots.values().toArray(Uniprot.EMPTY_ARRAY);
        ;

        System.out.println("Features");
        Map<UniprotFeatureType, FisherTable> mpx = handleFeatures(interesting);
        showFeatures(mpx);

        System.out.println("Observed Cleavages");
        Map<UniprotFeatureType, FisherTable> obsCleave = handleObservedCleavages(interesting);
        showFeatures(obsCleave);

        System.out.println("Missed Cleavages");
        Map<UniprotFeatureType, FisherTable> missCleave = handleMissedCleavages(interesting);
        showFeatures(missCleave);


        for (int i = 0; i < interesting.length; i++) {
            Uniprot uniprot = interesting[i];

        }
        System.out.println("Total " + pts.length + " bad " + nBad + " modeled " + interesting.length);
        //     buildPages(inp);
        // getUniptotsFromCommaSeparated(args);

    }


}
