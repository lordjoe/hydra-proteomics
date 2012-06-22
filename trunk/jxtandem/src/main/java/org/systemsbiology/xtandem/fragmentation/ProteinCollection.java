package org.systemsbiology.xtandem.fragmentation;

import com.lordjoe.utilities.*;
import org.systemsbiology.fasta.*;
import org.systemsbiology.xtandem.peptide.*;
import org.systemsbiology.xtandem.taxonomy.*;

import java.io.*;
import java.util.*;

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

    public static final Set<String>  SPECIAL_ID_SET = new HashSet<String>(Arrays.asList(SPECIAL_TEST_PROTEINS));

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

    @Override
    public void handleProtein(final String annotation, final String sequence) {
        String uniprotId = parseAnnotation(annotation);
        if(!isUniprotIdUsed(uniprotId))
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
            ret = new ProteinFragmentationDescription(id,this);
            m_UniProtIdToDescription.put(id, ret);
            if(SPECIAL_ID_SET.contains(id))  // temporary for now
                ret.guaranteeFragments( );
         }
        if (ret.getProtein() == null)
            ret.setProtein(getProtein(id));
        return ret;
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

    protected void loadPeptideAtlasTranslationList()
    {
        File translations = this.getUniprotTranslationFile();
        String[] lines = FileUtilities.readInLines(translations);
        for (int i = 1; i < lines.length; i++) {
            String line = lines[i];
            String[] items = line.split("\t");
            m_UniProtIdToPeptideAtlasId.put(items[1],items[0]);
        }
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
        loadProteins();

    }

    protected void showCoverage(String id){
        ProteinFragmentationDescription pfd =  getProteinFragmentationDescription(id);
         Protein prot = pfd.getProtein();
         ProteinFragment[] fragments = pfd.getFragments();
//         for (int i = 0; i < fragments.length; i++) {
//             ProteinFragment fragment = fragments[i];
//             int start = fragment.getStartLocation();
//             System.out.println(fragment + " -> " + start);
//                int[] starts = fragment.getStartLocations();
//             if(starts.length > 1)   {
//                 for (int j = 0; j < starts.length; j++) {
//                     int start1 = starts[j];
//                     System.out.println(start1);
//                 }
//             }
//
//         }
         short[] allCoverage = pfd.getAllCoverage();
         int lastCoverage = -1;
         int lastIndex = -1;
         int totalCoverage = 0;
         for (int i = 0; i < allCoverage.length; i++) {
             int thisCoverage = allCoverage[i];
             if(thisCoverage > 0)
                 totalCoverage++;
             if(thisCoverage != lastCoverage) {
                 if(lastIndex != -1) {
                     System.out.println("coverage " + lastCoverage + " " + lastIndex + "-" + i);
                 }
                 lastIndex = i;
                 lastCoverage = thisCoverage;
             }
         }
         System.out.println("Fraction Coverage " + String.format("%5.3f",pfd.getFractionalCoverage()));

    }

    public static void main(String[] args) {
        ProteinCollection pc = new ProteinCollection();
        pc.loadData();
        String[] ids = pc.getProteinIds();
        for (int i = 0; i < SPECIAL_TEST_PROTEINS.length; i++) {
            String id = SPECIAL_TEST_PROTEINS[i];
            pc.showCoverage(id);
        }

    }

}
