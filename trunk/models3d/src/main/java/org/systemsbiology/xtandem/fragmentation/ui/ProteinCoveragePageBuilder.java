package org.systemsbiology.xtandem.fragmentation.ui;

import com.lordjoe.utilities.*;
import org.systemsbiology.asa.*;
import org.systemsbiology.jmol.*;
import org.systemsbiology.xtandem.fragmentation.*;
import org.systemsbiology.xtandem.peptide.*;

import java.io.*;
import java.util.*;

/**
 * org.systemsbiology.xtandem.fragmentation.ui.ProteinCoveragePageBuilder
 * User: Steve
 * Date: 6/28/12
 */
public class ProteinCoveragePageBuilder {
    public static final ProteinCoveragePageBuilder[] EMPTY_ARRAY = {};

    public static final int MAX_COVERAGE = 8;
    public static final int INDEX_ROW_LENGTH = 6;


    public static final String HOME_PAGE = "IndexGood.html";
    public static final String JMOL_APPLET_ID = "JMol";

    private final ProteinCollection m_Proteins;
    private final int[] m_TotalCoverage = new int[MAX_COVERAGE];
    private final double[] m_AverageCoverage = new double[MAX_COVERAGE];

    public ProteinCoveragePageBuilder(final ProteinCollection proteins) {
        m_Proteins = proteins;
    }

    public ProteinCollection getProteins() {
        return m_Proteins;
    }

    public int[] getTotalCoverage() {
        return m_TotalCoverage;
    }

    public double[] getAverageCoverage() {
        if (m_AverageCoverage[0] == 0) {
            int sum = 0;
            for (int i = 0; i < m_TotalCoverage.length; i++) {
                sum += m_TotalCoverage[i];
            }
            for (int i = 0; i < m_TotalCoverage.length; i++) {
                m_AverageCoverage[i] = m_TotalCoverage[i] / (double) sum;
            }
        }
        return m_AverageCoverage;
    }

    public void showCoverage(String id) {
        ProteinCollection pc = getProteins();
        ProteinFragmentationDescription pfd = pc.getProteinFragmentationDescription(id);
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
            if (thisCoverage > 0)
                totalCoverage++;
            if (thisCoverage != lastCoverage) {
                if (lastIndex != -1) {
                    System.out.println("coverage " + lastCoverage + " " + lastIndex + "-" + i);
                }
                lastIndex = i;
                lastCoverage = thisCoverage;
            }
        }
        System.out.println("Fraction Coverage " + String.format("%5.3f", pfd.getFractionalCoverage()));

    }


    public void buildPages(final String[] ids) {
        List<String> holder = new ArrayList<String>();
        List<String> idholder = new ArrayList<String>();

        ProteinCollection pc = getProteins();
        String next = null;
        String prev = null;
        for (int i = 0; i < ids.length; i++) {
            if (i < ids.length - 1)
                next = ids[i + 1];
            else
                next = null;
            String id = ids[i];
            if(id == null)
                continue;
            System.out.println(id);
            String page = showCoveragePage(id, prev, next);

            if (page != null) {
                holder.add(page);
                idholder.add(id);
                prev = id;
            }
        }
        String[] pages = new String[holder.size()];
        holder.toArray(pages);
        String[] idsUsed = new String[idholder.size()];
        idholder.toArray(idsUsed);
        buildIndexPage(idsUsed, pages);
    }

    static int bad_models = 0;

    protected String showCoveragePage(String id, String prev, String next) {
        ProteinCollection proteins = getProteins();
        ProteinFragmentationDescription pfd = proteins.getProteinFragmentationDescription(id);
        int[] coverageLevels = pfd.getStatistics().getCoverageStatistics();
        for (int i = 0; i < Math.min(MAX_COVERAGE, coverageLevels.length); i++) {
            int coverageLevel = coverageLevels[i];
            m_TotalCoverage[i] += coverageLevel;
        }
        PDBObject model = null;
        File model3d = proteins.getPDBModelFile(id);
        if (model3d != null) {
            try {
                model = new PDBObject(model3d, pfd.getProtein());
                pfd.setModel(model);

            }
            catch (NumberFormatException e) {
                System.out.println("problem " + id + " " + e.getMessage());
                System.out.println("Bad Models " + bad_models++);
            }
            catch (IllegalStateException e) {
                if (!e.getMessage().startsWith("bad location"))
                    throw new RuntimeException(e);
                else
                    System.out.println("problem " + id + " " + e.getMessage());
            }
            catch (IllegalArgumentException e) {
                e.printStackTrace();
                if (!e.getMessage().startsWith("Bad amino acid abbreviation"))
                    throw new RuntimeException(e);
                else
                    System.out.println("problem " + id + " " + e.getMessage());
            }
        }

        HTMLPageBuillder pb = new HTMLPageBuillder("Coverage for " + id);
        HTMLBodyBuillder body = pb.getBody();
        HTMLHeaderBuillder header = pb.getHeader();
        Protein protein = pfd.getProtein();
        body.addString("<a href=\"../" + HOME_PAGE + "\" >Home</a>\n");
        header.addString("<script src=\"../Jmol.js\" type=\"text/javascript\"></script> <!-- REQUIRED -->\n");
        if (prev != null)
            body.addString("<a href=\"" + prev + ".html\" >Prev</a>\n");
        if (next != null)
            body.addString("<a href=\"" + next + ".html\" >Next</a>\n");

        body.addString("<h1>" + pb.getTitle() + "</h1>\n");
        body.addString("<h3>" + protein.getAnnotation() + "</h3>\n");
        String chainstr = "";
        if (model == null) {
            new HTMLHeaderHolder(body, "No 3D Model Found", 1);
        }
        else {
            ChainEnum[] chains = model.getChains();
            if (chains.length > 1) {
                StringBuilder sb = buildChainsString(chains);
                chainstr = sb.toString();
            }
        }

        ProteinFragment[] fragments = pfd.getFragments();
        double fractionalCoverage = pfd.getFractionalCoverage();
        if (fractionalCoverage == 0)
            return null;
        int coveragePercent = (int) (100 * fractionalCoverage);
        body.addString("<h2>Coverage % " + coveragePercent +
                " Number Fragments " + fragments.length +
                chainstr +
                "</h2>\n");

        if (model != null) {
            Asa.calculate_asa(model);
            ThreeDModelBuillder tm = new ThreeDModelBuillder(body, pfd);
//
//         }
//         else {
//             new HTMLHeaderHolder(body, "No 3D Model Found", 1);
        }

        new CoverageColorsLabel(body);
        if(model != null)  {
            new SingleTagBuillder(body, "p");
            new SecondaryStructureLabel(body);
        }

        new SingleTagBuillder(body, "p");

        CoverageFragment cf = new CoverageFragment(body, pfd);

        SingleTagBuillder st = new SingleTagBuillder(body, "p");


        String page = pb.buildPage();
        String fileName = "pages/" + id + ".html";
        new File("pages").mkdirs();
        FileUtilities.writeFile(fileName, page);
        return fileName;
    }

    protected static StringBuilder buildChainsString(ChainEnum[] chains) {
        StringBuilder sb = new StringBuilder();
        sb.append(" Chains ");
        for (int i = 0; i < chains.length; i++) {
            ChainEnum chain = chains[i];
            if (i > 0)
                sb.append(",");
            sb.append(chain);
        }
        return sb;
    }


    protected String buildIndexPage(String[] ids, String[] pages) {
        ProteinCollection proteins = getProteins();
        HTMLPageBuillder pb = new HTMLPageBuillder("Protein Coverage ");
        HTMLBodyBuillder body = pb.getBody();

        List<ProteinFragmentationDescription> idWith3dModel = new ArrayList<ProteinFragmentationDescription>();
        List<ProteinFragmentationDescription> idWithout3dModel = new ArrayList<ProteinFragmentationDescription>();
        List<String> pageWith3dModel = new ArrayList<String>();
        List<String> pageWithout3dModel = new ArrayList<String>();
        List<ProteinFragmentationDescription> pfdWith3dModel = new ArrayList<ProteinFragmentationDescription>();

        for (int i = 0; i < ids.length; i++) {
            String id = ids[i];
            String page = pages[i];
            ProteinFragmentationDescription pd = proteins.getProteinFragmentationDescription(id);
            if (pd.getModel() != null) {
                idWith3dModel.add(pd);
                pageWith3dModel.add(page);
            }
            else {
                idWithout3dModel.add(pd);
                pageWithout3dModel.add(page);

            }
        }
        String[] empty = {};
        ProteinFragmentationDescription[] emptyPd = {};
        if (!idWith3dModel.isEmpty()) {
            body.addString("<h1>Proteins With 3D Models</h1>\n");
            body.addString("<h3>UniprotID Coverage% Peptides Modeled/Total Peptides  [Number Chains]</h3>\n");
            new ReferenceTableBuillder(body, idWith3dModel.toArray(emptyPd), pageWith3dModel.toArray(empty), INDEX_ROW_LENGTH);
        }
        if (!idWithout3dModel.isEmpty()) {
            body.addString("<h1>Proteins Without 3D Models</h1>");
            new ReferenceTableBuillder(body, idWithout3dModel.toArray(emptyPd), pageWithout3dModel.toArray(empty), INDEX_ROW_LENGTH);
        }
        String page = pb.buildPage();
        String fileName = HOME_PAGE;
        FileUtilities.writeFile(fileName, page);
        Collections.sort(pfdWith3dModel, ProteinFragmentationDescription.INTERESTING_COMPARATOR);
        for (Iterator<ProteinFragmentationDescription> iterator = pfdWith3dModel.iterator(); iterator.hasNext(); ) {
            ProteinFragmentationDescription next = iterator.next();
            System.out.println(next.getUniprotId() + " " + String.format("%5.3f", next.getFractionalCoverage()) + " " +
                    next.getStatistics().getPartitionStatistics(2));
        }
        double[] averagecoverage = getAverageCoverage();
        for (int i = 0; i < averagecoverage.length; i++) {
            double v = averagecoverage[i];
            System.out.print(Integer.toString(i) + " " + String.format("%5.3f", v) + ",");
        }
        System.out.println();
        return fileName;
    }


}
