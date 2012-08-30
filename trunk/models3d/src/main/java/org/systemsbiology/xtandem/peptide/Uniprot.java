package org.systemsbiology.xtandem.peptide;

import com.lordjoe.utilities.*;
import org.systemsbiology.jmol.*;
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
    private final String[] m_Models;
    private final Map<String, BioJavaModel> m_IdToMpdel = new HashMap<String, BioJavaModel>();
    private final File m_ModelDirectory = new File("Models3D");
    private BioJavaModel m_BestModel;

    public Uniprot(String line) {
        this(line.split("\t"));
    }

    public Uniprot(String[] split1) {
        final String annotation = split1[0];
        final String sequence = split1[2].replace(" ", "");
        m_Protein = Protein.buildProtein(annotation, sequence, "");
        String pdbs = split1[1].trim();
        if (pdbs.length() == 0)
            m_Models = new String[0];
        else {
            m_Models = pdbs.split(";");
        }
        if (!m_ModelDirectory.isDirectory())
            throw new IllegalStateException("Model directory " + m_ModelDirectory + " does not exist");
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
            mdl.readFile(new File(mdr, id + ".pdb"));
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
        if(bestModel != null)
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


    public static Uniprot[] readUniprots() {
        String[] lines = FileUtilities.readInLines("Origene.tsv");
        List<Uniprot> holder = new ArrayList<Uniprot>();
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            Uniprot up = new Uniprot(line);
            holder.add(up);
        }
        Uniprot[] ret = new Uniprot[holder.size()];
        holder.toArray(ret);
        return ret;

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

    public static final int MAX_BUILT_PAGES =  100000;

    public static void buildModelPages(Uniprot[] pts) {
        Protein[] proteins = new Protein[pts.length];
        String[] ids = new String[pts.length];
          ProteinCollection pc = new ProteinCollection();
        FoundPeptides fps = FoundPeptides.readFoundPeptides(new File("OrigenePeptides.tsv"));
        File pdbDirectory = pc.getPDBDirectory();
        int count = 0;
        for (int i = 0; i < ids.length; i++) {
            Uniprot pt = pts[i];
            Protein protein = pt.getProtein();
            proteins[i] = protein;
            String id = protein.getId();
            FoundPeptide[] peptides = fps.getPeptides(id);
            ProteinFragmentationDescription pfd = new ProteinFragmentationDescription( id, pc,protein, peptides);
            for (int j = 0; j < peptides.length; j++) {
                FoundPeptide peptide = peptides[j];
                IPolypeptide peptide1 = peptide.getPeptide();
                pfd.addFragment( protein,    peptide1, j);
            }
            pc.addProteinFragmentationDescription(pfd);
            ids[i] = id;
            BioJavaModel bestModel = pt.getBestModel();
             File added = new File(pdbDirectory, bestModel.getPdbCode() + ".pdb");
            pc.addPDBModelFile(id, added);
              if(count++ > MAX_BUILT_PAGES  )
               break;

        }
        ProteinCoveragePageBuilder pb = new ProteinCoveragePageBuilder(pc);
        pb.buildPages(ids);

    }


    public static final int GOOD = 0;
    public static final int BAD = 1;
    public static final int NONE = 2;

    public static void main(String[] args) throws IOException {
        // downloadUniprots(args[0]);
        Uniprot[] pts = readUniprots();
        Uniprot[] gm = getGoodModels(pts);
        buildModelPages(gm);
    }


}
