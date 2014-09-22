package org.systemsbiology.uniprot;

import org.systemsbiology.xtandem.peptide.*;

import java.io.*;
import java.util.*;

/**
 * org.systemsbiology.uniprot.ProteinDatabase
 * User: Steve
 * Date: 11/21/12
 */
public class ProteinDatabase {
    public static final ProteinDatabase[] EMPTY_ARRAY = {};

    private static ProteinDatabase gInstance;

    public static synchronized ProteinDatabase getInstance() {
        if (gInstance == null)
            gInstance = new ProteinDatabase();
        return gInstance;
    }

    //  public static final String DATABASE_FILE = "uniprot/uniprot_sprot.fasta";
    public static final String DATABASE_FILEX = "uniprot-homo-sapiens.fasta";  // todo revert


    public static File getHomeDirectory(String base, String defaultDir) {
        File f = new File(base);
        if (!f.exists()) {
            String udir = System.getProperty("user.dir");
            File home = new File(udir);
            home = new File(home, defaultDir);
            f = new File(home, base);
        }
        if (!f.exists()) {
            String uHome = System.getProperty("user.home");
            File home = new File(uHome);
            home = new File(home, defaultDir);
            f = new File(home, base);
        }
        if (!f.exists()) {
            f.mkdirs();
        }
        return f;
    }


    public static File getHomeFile(String base, String defaultDir) {
        File f = new File(base);
        if (!f.exists()) {
            String udir = System.getProperty("user.dir");
            File home = new File(udir);
            home = new File(home, defaultDir);
            f = new File(home, base);
        }
        if (!f.exists()) {
            String uHome = System.getProperty("user.home");
            File home = new File(uHome);
            home = new File(home, defaultDir);
            f = new File(home, base);
        }
        if (!f.exists()) {
            throw new IllegalStateException("cannot file file " + base);
        }
        return f;
    }

    /**
     * read the interestig fasta file
     *
     * @return
     */
    public static Reader getDatabaseReader() {
        File f = getHomeFile(DATABASE_FILEX, "Spaghetti");
        if (!f.exists()) {
            throw new IllegalStateException("database file does not exist");
        }
        try {
            return new FileReader(f);
        }
        catch (FileNotFoundException e) {
            throw new RuntimeException(e);

        }
    }

    private final Map<String, Protein> m_IdToSequence = new HashMap<String, Protein>();

    private ProteinDatabase() {
    }

    protected void guaranteeDatabasePopulated() {
        if (!m_IdToSequence.isEmpty())
            return;
        populateDatabase();
    }

    public Protein getProtein(String id) {
        guaranteeDatabasePopulated();
        return m_IdToSequence.get(id);
    }


    public String[] getIds() {
        guaranteeDatabasePopulated();
        String[] ret = m_IdToSequence.keySet().toArray(new String[0]);
        Arrays.sort(ret);
        return ret;
    }

    public static final int NUMBER_FASTA_SPLITS = 64; // make it easy to search for sequences
    public static final int START_ID = 4;

    protected void populateDatabase() {
        try {
            LineNumberReader rdr = new LineNumberReader(getDatabaseReader());
            String line = rdr.readLine();
            StringBuilder sb = new StringBuilder();

            String sequence = sb.toString();
            String id = null;
            String annotation = null;
            while (line != null) {
                if (line.startsWith(">")) {
                    if (sb.length() > 0) {
                        Protein p = Protein.buildProtein(id, annotation, sb.toString(), DATABASE_FILEX);
                        m_IdToSequence.put(id, p);
                        id = null;
                        annotation = null;
                        sb.setLength(0);
                    }
                    annotation = line;
                    int endId = line.indexOf("|", START_ID + 1);
                    id = line.substring(START_ID, endId).trim();
                }
                else {
                    sb.append(line);
                }
                line = rdr.readLine();
            }
            if (sb.length() > 0) {
                Protein p = Protein.buildProtein(id, annotation, sb.toString(), DATABASE_FILEX);
                m_IdToSequence.put(id, p);
                id = null;
                annotation = null;
                sb.setLength(0);
            }

        }
        catch (IOException e) {
            throw new RuntimeException(e);

        }


    }

    protected void writeSample(Appendable out, Uniprot up) {
        try {
            out.append(up.getId());
            out.append("\t");
            IProtein protein = up.getProtein();
            String sequence = protein.getSequence();

            String[] fragments = makeFragments(sequence);
            for (int i = 0; i < fragments.length; i++) {
                String fragment = fragments[i];
                if (i > 0)
                    out.append(",");
                out.append(fragment);

            }

            out.append("\t");
            String[] models = up.getModels();
            for (int i = 0; i < models.length; i++) {
                String model = models[i];
                if (i > 0)
                    out.append(",");
                out.append(model);

            }
            out.append("\n");

        }
        catch (IOException e) {
            throw new RuntimeException(e);

        }

    }

    public static final int MINIMUM_SAMPLES = 3;
    public static final int MAXIMUM_SAMPLES = 30;
    public static final int MINIMUM_LENGTH = 7;
    public static final int MAXIMUM_LENGTH = 20;

    public static final Random RND = new Random();

    protected static String[] makeFragments(String s) {
        int nsamples = MINIMUM_SAMPLES + RND.nextInt(MAXIMUM_SAMPLES);
        List<String> holder = new ArrayList<String>();
        while (holder.size() < nsamples) {
            holder.add(makeFragment(s));
        }

        String[] ret = new String[holder.size()];
        holder.toArray(ret);
        return ret;
    }

    protected static String makeFragment(String s) {
        int len = MINIMUM_LENGTH + RND.nextInt(MAXIMUM_LENGTH - MINIMUM_LENGTH);
        int start = RND.nextInt(s.length() - len);
        return s.substring(start, len + start);

    }

    public static String toRealId(String in) {
        int index = in.indexOf("-");
        if (index >= 0) {
            in = in.substring(0, index);
        }
        return in;

    }

    public static final int MAX_SAMPLES = 30;

    public static void main(String[] args) throws Exception {
        ProteinDatabase pd = ProteinDatabase.getInstance();
        String[] ids = pd.getIds();
        PrintWriter out = new PrintWriter(new FileWriter("samples_withmodels.txt"));
        int nSamples = 0;
        for (int i = 0; i < ids.length; i++) {
            String id = toRealId(ids[i]);
            Uniprot up = Uniprot.buildUniprot(id);
            if (up == null)
                continue;
            if (up.getModels().length > 0) {
                pd.writeSample(out, up);
                out.flush();
                if (nSamples++ > MAX_SAMPLES) {
                    out.close();
                    return;
                }
            }
        }
        Protein p = pd.getProtein("O00154");
        if (p == null)
            throw new IllegalStateException("problem"); // ToDo change
        p = pd.getProtein("O00154");
        if (p == null)
            throw new IllegalStateException("problem"); // ToDo change
        p = pd.getProtein("O00625");
        if (p == null)
            throw new IllegalStateException("problem"); // ToDo change
        p = pd.getProtein("A2RUC4");
        if (p == null)
            throw new IllegalStateException("problem"); // ToDo change
    }

}
