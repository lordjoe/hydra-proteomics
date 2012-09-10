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
            return new Uniprot(split1);
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
    private final Set<IPolypeptide> m_Theoretical = new HashSet<IPolypeptide>();
    private final Set<String> m_TheoreticalSequences = new HashSet<String>();
    private boolean m_BadPeptide;
    private Sequence m_Sequence;

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

        m_Protein = Protein.buildProtein(annotation, sequence, "");
        buildTheoreticalPeptides(PeptideBondDigester.getDigester("Trypsin"));
        int seqLength = sequence.length();
        FastaAminoAcid[] fastaAminoAcids = FastaAminoAcid.asAminoAcids(sequence);
        m_AminoAcids = new ProteinAminoAcid[fastaAminoAcids.length];
        for (int i = 0; i < fastaAminoAcids.length; i++) {
            m_AminoAcids[i] = new ProteinAminoAcid(fastaAminoAcids[i], i);

        }

    }

    protected void buildTheoreticalPeptides(PeptideBondDigester trypsin) {
        IPolypeptide[] digest = trypsin.digest(getProtein());
        for (int i = 0; i < digest.length; i++) {
            IPolypeptide pp = digest[i];
            if (isPeptideAcceptable(pp)) {
                m_Theoretical.add(pp);
                m_TheoreticalSequences.add(pp.getSequence());
            }
        }
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

    public void setSequence(Sequence sequence) {
        m_Sequence = sequence;
        Feature[] fts = SwissProt.getAnalyzedFeatures(sequence);
        for (int i = 0; i < fts.length; i++) {
            Feature ft = fts[i];
            handleFeature(ft);
        }
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
                throw new IllegalStateException("never get here");
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
        int min = location.getMin();
        int max = location.getMin();
        ProteinAminoAcid[] aminoAcids = getAminoAcids();
        for (int i = min; i < max; i++) {
            ProteinAminoAcid aminoAcid = aminoAcids[i];
            aminoAcid.addFeature(type);
        }
    }

    protected void handleTurn(Feature ft) {
        UniprotFeatureType type = SwissProt.getFeatureType(ft);
        if (type != UniprotFeatureType.TURN)
            throw new IllegalArgumentException("must have type " + UniprotFeatureType.TURN);
        throw new UnsupportedOperationException("Fix This"); // ToDo
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

    public Protein getProtein() {
        return m_Protein;
    }

    public String[] getModels() {
        return m_Models;
    }

    public File getModelDirectory() {
        return m_ModelDirectory;
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
        BioJavaModel bestModel = getBestModel();
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
        return m_BestModel;
    }


    public void addFoundPeptide(IPolypeptide peptide) {
        m_Detected.add(peptide);
        String pst = peptide.getSequence();
        Protein protein = getProtein();
        String id = protein.getId();
        String prost = protein.getSequence();
        int index = prost.indexOf(pst);
        if (index == -1) {
            //  System.out.println("BAD PEPTIDE " + pst + "  NOT IN " + id);
            setBadPeptide(true);
            return;
            // throw new IllegalStateException("sequence needs ot occur once " + pst);
        }
        while (index > -1) {
            for (int i = index; i < (index + pst.length()); i++) {
                m_AminoAcids[i].setDetected(true);
            }
            index = prost.indexOf(pst, index + 1);
        }
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
        BioJavaModel best = null;
        int sequencelen = getProtein().getSequenceLength();
        for (int i = 0; i < mdls.length; i++) {
            BioJavaModel mdl = mdls[i];
            int fitLength = mdl.getFitLength();
            double fraction = mdl.resolveChains();
            if (fraction > bestFit) {
                bestFit = fraction;
                best = mdl;
            }
            bestFit = Math.max(bestFit, fraction);
        }
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

        }

        clearModels();
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
            for (int j = 0; j < peptides.length; j++) {
                FoundPeptide peptide = peptides[j];
                IPolypeptide peptide1 = peptide.getPeptide();
                pfd.addFragment(protein, peptide1, j);
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


    public static Uniprot[] findUnterestingUniprots(File sp, Map<String, Uniprot> idToUniprot) {
        Sequence[] ret = SwissProt.readInterestingSequences(sp);
        Map<String, Uniprot> testUniProts = new HashMap<String, Uniprot>();
        for (int i = 0; i < ret.length; i++) {
            Sequence sequence = ret[i];
            String name = sequence.getName();
            Uniprot pt = idToUniprot.get(name);
            if (pt == null || pt.isBadPeptide()) {
                System.out.println("Bad id " + name);
                continue;
            }
            pt.setSequence(sequence);
            testUniProts.put(name, pt);

        }
        return idToUniprot.values().toArray(Uniprot.EMPTY_ARRAY);
    }


    public static void main(String[] args) throws IOException {
        // downloadUniprots(args[0]);
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
            System.out.println(pt.getId() + " " + String.format("%6.3f", coverage));

        }

        Uniprot[] interesting = findUnterestingUniprots(sp, idToUniprot);

        for (int i = 0; i < interesting.length; i++) {
            Uniprot uniprot = interesting[i];

        }
        System.out.println("Total " + pts.length + " bad " + nBad + " modeled " + interesting.length);
        //     buildPages(inp);
        // getUniptotsFromCommaSeparated(args);

    }


}
