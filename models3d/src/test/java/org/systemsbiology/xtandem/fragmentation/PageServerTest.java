package org.systemsbiology.xtandem.fragmentation;

import com.lordjoe.utilities.*;
import org.junit.*;
import org.systemsbiology.jmol.*;
import org.systemsbiology.uniprot.*;
import org.systemsbiology.xtandem.fragmentation.server.*;
import org.systemsbiology.xtandem.fragmentation.ui.*;
import org.systemsbiology.xtandem.peptide.*;

import java.io.*;
import java.util.*;

/**
 * org.systemsbiology.xtandem.fragmentation.PageServerTest
 * User: steven
 * Date: 1/3/13
 */
public class PageServerTest {

    public static final Random RND = new Random();

    public static final String[] IDS =
            {
                    //    "Strong response:",
                    "IFIT3,Q01629",
                    "CASP7,P55210",
                    "TXNRD1,Q16881",
                    "OAS1,P00973",
                    "OAS3,Q9Y6K5",
                    "THBS1,P07996",
                    "PNPT1,Q8TCS8",
                    "NMI,Q13287",
                    "IFIH1,Q9BYX4",
                    "Mx1,P20591",
                    "PLSCR1,O15162",
                    "RTP4,Q96DX8",
                    "SPP1,P10451",
                    "PLK2,Q9NYY3",
                    "UBD,O15205",
                    "DDX58,O95786",
                    "DNAJA1,P31689",
                    "TRIM14,Q14142",
                    //            ",",
                    //            "Weak response:,",
//                    "IFITM2,Q01629",
//                    "ADAR,H0YCK3",
//                    "IFITM3,Q01629",
//                    "MICAL2,F5H8E8",
//                    "NFKBIA,P25963",
//                    "TNFRSF12A,  Q9NP84",
//                    "ITGAV,P06756",
//                    "STAT1,P42224",
//                    "SKIL,P12757",
//                    "TMEM2,Q9UHN6",
//                    "FMR1,Q06787",
//                    "SFPQ,P23246"
            };

    public static String getPageString(String uiprotId) {
        ProteinDatabase pd = ProteinDatabase.getInstance();
        Protein protein = pd.getProtein(uiprotId);
        if (protein == null) {
            throw new UnsupportedOperationException("Fix This"); // ToDo
        }
        String sequence = protein.getSequence();

        String fragments = generateFragments(protein);
        FoundPeptide[] peptides = PageServer.fragmentsToFoundPeptides(protein, fragments);
        ProteinFragmentationDescription pfd = new ProteinFragmentationDescription(uiprotId, null, protein, peptides);
        Uniprot up = Uniprot.getUniprot(uiprotId);
        BioJavaModel bestModel = up.getBestModel();

        if (bestModel != null) {

            File file = bestModel.getFile();
            pfd.setModelFile(file);
        }


        return ProteinCoveragePageBuilder.buildFragmentDescriptionPage(uiprotId, null, null, pfd);
    }



    private static String generateFragments(Protein protein) {
        PeptideBondDigester dig = PeptideBondDigester.TRYPSIN1;   // allow one cleavage

        IPolypeptide[] digest = dig.digest(protein);
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < digest.length; i++) {
            IPolypeptide pp = digest[i];
            if(RND.nextInt(3) > 0)      // use every third
                continue;
            if (sb.length() > 0)
                sb.append("\n");
            sb.append(pp.getSequence());
        }

        return sb.toString();
    }


    public static final String[] ID_WITH_MODEL =
            {
         //           "O95786",
                    "P55210",
       //             "Q16881",
       //             "Q8TCS8",
       //             "P20591",
            };

    @Before
    public void setUpDirectories()
    {
        File pages = new File("pages");
        pages.mkdirs();
        File models = new File("Models3D");
        models.mkdirs();

    }

    /**
     * build a group of pages of interest to Kathie with models
     * @throws Exception
     */
    @Test
    public void testPageWithModelBuild() throws Exception {
        Uniprot.setDownloadModels(true);

        File pages = new File("pages");
        for (int i = 0; i < ID_WITH_MODEL.length; i++) {
            String id  = ID_WITH_MODEL[i];
            String page = getPageString(id);

        }


    }

    /**
     * build pages for all proteint of interest to kathy walters
     * @throws Exception
     */
   @Test
    public void testPageBuild() throws Exception {
        Uniprot.setDownloadModels(true);
        ProteinDatabase pd = ProteinDatabase.getInstance();   // preload
        File pages = new File("pages");

        for (int i = 0; i < IDS.length; i++) {
            String idx = IDS[i];
            String[] items = idx.split(",");

            String id = items[1];
            String page = getPageString(id);

        }


    }


}
