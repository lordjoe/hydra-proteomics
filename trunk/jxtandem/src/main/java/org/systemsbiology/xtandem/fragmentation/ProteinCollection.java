package org.systemsbiology.xtandem.fragmentation;

import com.lordjoe.utilities.*;
import org.systemsbiology.fasta.*;
import org.systemsbiology.xtandem.fragmentation.ui.*;
import org.systemsbiology.xtandem.peptide.*;
import org.systemsbiology.xtandem.taxonomy.*;

import java.io.*;
import java.util.*;

//import org.systemsbiology.xtandem.fragmentation.ui.*;

/**
 * org.systemsbiology.xtandem.fragmentation.ProteinCollection
 * User: Steve
 * Date: 6/21/12
 */
public class ProteinCollection implements IFastaHandler {
    public static final ProteinCollection[] EMPTY_ARRAY = {};

    //   public static final String SPECIAL_TEST_PROTEIN = "Q9Y6X3";
    public static final String[] SPECIAL_TEST_PROTEINS = {
            "O14773",
            "P29144",
            "Q9Y5R8",
            "O43617",
            "Q9Y296",
            "Q8IUR0",
            "Q96Q05",
            "Q9BW30",
            "Q9Y3C4",
            "P12270",
            "Q9ULW0",
            "Q9UI30",
            "O00300",
            "Q9Y2W1",
            "P62995",
            "O14545",
            "Q15628",
            "Q13114",
            "Q9BUZ4",
            "Q9Y4K3",
            "Q9UPV9",
            "Q9Y6X3",
    };

    public static final String[] MORE_PROTEINS = {
            "A8MT69", "O00139", "O00151", "O00154", "O00161", "O00244", "O00255", "O00267", "O00273", "O00299", "O00330", "O00401", "O00408", "O00411",
            "O00422", "O00425", "O00443", "O00506", "O00541", "O00571", "O00585", "O00746", "O00762", "O00764", "O14497", "O14519", "O14545", "O14556",
            "O14561", "O14593", "O14617", "O14684", "O14732", "O14733", "O14745", "O14757", "O14773", "O14776", "O14777", "O14893", "O14907", "O14920",
            "O14929", "O14933", "O14936", "O14965", "O14974", "O14976", "O14980", "O15020", "O15031", "O15047", "O15056", "O15085", "O15119", "O15305",
            "O15344", "O15379", "O15382", "O15514", "O15550", "O43148", "O43169", "O43172", "O43175", "O43251", "O43252", "O43278", "O43314", "O43324",
            "O43390", "O43395", "O43396", "O43405", "O43432", "O43447", "O43463", "O43488", "O43504", "O43566", "O43572", "O43615", "O43617", "O43663",
            "O43665", "O43707", "O43708", "O43719", "O43741", "O43747", "O43752", "O43765", "O43809", "O43813", "O43924", "O60218", "O60234", "O60256",
            "O60271", "O60333", "O60341", "O60343", "O60344", "O60462", "O60496", "O60504", "O60506", "O60547", "O60566", "O60573", "O60603", "O60658",
            "O60671", "O60674", "O60701", "O60711", "O60716", "O60763", "O60784", "O60869", "O60880", "O60885", "O60888", "O60907", "O60921", "O60930",
            "O60942", "O75112", "O75164", "O75223", "O75334", "O75340", "O75347", "O75351", "O75367", "O75368", "O75369", "O75391", "O75400", "O75436",
            "O75475", "O75521", "O75534", "O75569", "O75582", "O75608", "O75643", "O75688", "O75717", "O75792", "O75821", "O75832", "O75865", "O75874",
            "O75925", "O75940", "O75962", "O76003", "O76054", "O94768", "O94804", "O94813", "O94829", "O94855", "O94856", "O94925", "O95071", "O95149",
            "O95155", "O95166", "O95218", "O95292", "O95340", "O95400", "O95433", "O95453", "O95487", "O95684", "O95714", "O95747", "O95786", "O95822",
            "O95831", "O95861", "O95881", "O95989", "P00167", "P00338", "P00367", "P00387", "P00390", "P00441", "P00480", "P00488", "P00491", "P00492",
            "P00533", "P00558", "P00568", "P00749", "P00813", "P00918", "P01111", "P01112", "P02461", "P02462", "P02545", "P02549", "P02647", "P02649",
    };

    public static final Set<String> SPECIAL_ID_SET = new HashSet<String>(Arrays.asList(SPECIAL_TEST_PROTEINS));

    public static final String PROPERTY_FILE_NAME = "FragmentationStudy.properties";
    //
// properties describing protein fragmentation work
// files are delative to top directory
//
// fasta file for proteins
    public static final String FASTA_FILE_PROPERTY = "org.systemsbiology.proteimics.fastafile"; //uniprot-homo-sapiens.fasta
    // tsv file mappinn protein atlas id to uniprot id for interesting proteins
    public static final String UNIPROT_MAPPING_FILE_PROPERTY = "org.systemsbiology.proteimics.uniprot_mapping"; //mappingToUniprot.txt
    // directory where pdb models are stored
    public static final String PDB_DIRECTORY_PROPERTY = "org.systemsbiology.proteimics.pdb_model_directory"; //Models3d
    // directory where fragmentation ppeptides are stored
    public static final String FRAGMENTATION_DIRECTORY_PROPERTY = "org.systemsbiology.proteimics.fragmentation_directory"; //Fragments
    // tsv file with uniprotid and comma delimited list of models
    public static final String MODEL_LIST_PROPERTY = "org.systemsbiology.proteimics.available_3d_models"; //PdbModelList.txt


    private final Properties m_Properties = new Properties();
    private final Map<String, ProteinFragmentationDescription> m_UniProtIdToDescription = new HashMap<String, ProteinFragmentationDescription>();
    private final Map<String, Protein> m_Proteins = new HashMap<String, Protein>();
    private final Map<String, String> m_UniProtIdToPeptideAtlasId = new HashMap<String, String>();
    private final Map<String, File> m_UniProtIdToPDBFile = new HashMap<String, File>();

    @Override
    public void handleProtein(final String annotation, final String sequence) {
        String uniprotId = parseAnnotation(annotation);
        if (!isUniprotIdUsed(uniprotId))
            return;
        Protein prot = Protein.buildProtein(annotation, sequence, null);
        m_Proteins.put(uniprotId, prot);
        getProteinFragmentationDescription(uniprotId); // create as needed
    }

    protected String getProperty(String name) {
        guaranteeProperties();
        return m_Properties.getProperty(name);
    }

    protected File getFastaFile() {
        String name = getProperty(FASTA_FILE_PROPERTY);
        File ret = new File(name);
        if (!ret.exists() || !ret.isFile())
            throw new IllegalArgumentException("cannot find fasta file " + name);
        return ret;
    }

    protected File getUniprotTranslationFile() {
        String name = getProperty(UNIPROT_MAPPING_FILE_PROPERTY);
        File ret = new File(name);
        if (!ret.exists() || !ret.isFile())
            throw new IllegalArgumentException("cannot find UniprotTranslationFile file " + name);
        return ret;
    }

    protected File getPDBMappingFile() {
        String name = getProperty(MODEL_LIST_PROPERTY);
        File ret = new File(name);
        if (!ret.exists() || !ret.isFile())
            throw new IllegalArgumentException("cannot find PDBMappingFile file " + name);
        return ret;
    }

    public File getFragmentDirectory() {
        String name = getProperty(FRAGMENTATION_DIRECTORY_PROPERTY);
        File ret = new File(name);
        if (!ret.exists() || !ret.isDirectory())
            throw new IllegalArgumentException("cannot find fragmentation directory " + name);
        return ret;
    }

    public File getPDBDirectory() {
        String name = getProperty(PDB_DIRECTORY_PROPERTY);
        File ret = new File(name);
        if (!ret.exists() || !ret.isDirectory())
            throw new IllegalArgumentException("cannot find PDB directory " + name);
        return ret;
    }

    public String[] getProteinIds() {
        String[] strings = m_Proteins.keySet().toArray(new String[0]);
        Arrays.sort(strings);
        return strings;
    }


    public Protein getProtein(String id) {
        return m_Proteins.get(id);
    }


    public ProteinFragmentationDescription getProteinFragmentationDescription(String id) {
        ProteinFragmentationDescription ret = m_UniProtIdToDescription.get(id);
        if (ret == null) {
            ret = new ProteinFragmentationDescription(id, this);
            m_UniProtIdToDescription.put(id, ret);
            if (SPECIAL_ID_SET.contains(id))  // temporary for now
                ret.guaranteeFragments();
        }
        if (ret.getProtein() == null)
            ret.setProtein(getProtein(id));
        maybeAd3DModel(ret);
        return ret;
    }

    protected void maybeAd3DModel(ProteinFragmentationDescription ret) {
        Protein protein = ret.getProtein();
        String id = protein.getId();

    }


    public void loadProteins() {
        File f = getFastaFile();
        try {
            LineNumberReader rdr = new LineNumberReader(new FileReader(f));
            FastaStreamIterator fi = new FastaStreamIterator(rdr);
            while (fi.hasNext()) {
                FastaEntry fe = fi.next();
                handleProtein(fe.getAnnotation(), fe.getValue());

            }
        }
        catch (FileNotFoundException e) {
            throw new RuntimeException(e);

        }
    }

    protected void loadPeptideAtlasTranslationList() {
        File translations = this.getUniprotTranslationFile();
        String[] lines = FileUtilities.readInLines(translations);
        for (int i = 1; i < lines.length; i++) {
            String line = lines[i];
            String[] items = line.split("\t");
            m_UniProtIdToPeptideAtlasId.put(items[1], items[0]);
        }
    }

    protected void loadPDBTranslationList() {
        File translations = this.getPDBMappingFile();
        String[] lines = FileUtilities.readInLines(translations);
        for (int i = 1; i < lines.length; i++) {
            String line = lines[i];
            String[] items = line.split("\t");
            String modelid = items[1].split(",")[0];
            String uniProtId = items[0];
            File modelFile = new File("Models3d/" + modelid + ".pdb");
            if (!modelFile.exists()) {
                System.out.println("no file " + modelFile);
                continue;
            }
            m_UniProtIdToPDBFile.put(uniProtId, modelFile);
        }
    }

    public File getPDBModelFile(String uniprotId) {
        return m_UniProtIdToPDBFile.get(uniprotId);
    }

    public boolean isUniprotIdUsed(String id) {
        return m_UniProtIdToPeptideAtlasId.containsKey(id);
    }

    /**
     * return the uniprot id
     *
     * @param annotation
     * @return
     */
    private String parseAnnotation(final String annotation) {
        String[] items = annotation.split("\\|");

        return items[1];
    }

    protected void guaranteeProperties() {
        if (m_Properties.isEmpty())
            loadProperties();
    }

    protected void loadProperties() {
        File ret = new File(PROPERTY_FILE_NAME);
        if (!ret.exists() || !ret.isFile())
            throw new IllegalArgumentException("cannot find property file " + PROPERTY_FILE_NAME);
        try {
            m_Properties.load(new FileInputStream(ret));
        }
        catch (IOException e) {
            throw new RuntimeException(e);

        }
    }

    protected void loadData() {
        guaranteeProperties();
        loadPeptideAtlasTranslationList();
        loadPDBTranslationList();
        loadProteins();

    }

    // Only one amino acid - look at rectangle placement
    public static final String[] ONE_INTERESTING = {"O95292"};
    // Fragmented 3d model code if fine model is bad
    public static final String[] TWO_INTERESTING = {"Q9Y296"};
    // says 3d model but nothing shown
    public static final String[] THREE_INTERESTING = {"O43708",
 //           "O14933",
            "O14684" ,
  //          "O602182",
    };


    public static void main(String[] args) {
        ProteinCollection pc = new ProteinCollection();
        pc.loadData();
        String[] ids = pc.getProteinIds();
        //     ids = SPECIAL_TEST_PROTEINS; // use only a few
        ids = THREE_INTERESTING; // look at only one case
         ids = MORE_PROTEINS;
        ProteinCoveragePageBuilder pb = new ProteinCoveragePageBuilder(pc);
        pb.buildPages(ids);

    }

}
