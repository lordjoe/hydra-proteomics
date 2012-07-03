package org.systemsbiology.xtandem.fragmentation.ui;

import com.lordjoe.utilities.*;
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

    private final ProteinCollection m_Proteins;

    public ProteinCoveragePageBuilder(final ProteinCollection proteins) {
        m_Proteins = proteins;
    }

    public ProteinCollection getProteins() {
        return m_Proteins;
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
        for (int i = 0; i < ids.length; i++) {
            String id = ids[i];
            String page = showCoveragePage(id);
            if (page != null) {
                holder.add(page);
                idholder.add(id);
            }
        }
        String[] pages = new String[holder.size()];
        holder.toArray(pages);
        String[] idsUsed = new String[idholder.size()];
        idholder.toArray(idsUsed);
        buildIndexPage(idsUsed, pages);
    }

    static int bad_models = 0;

    protected String showCoveragePage(String id) {
        ProteinCollection proteins = getProteins();
        ProteinFragmentationDescription pfd = proteins.getProteinFragmentationDescription(id);
        PDBObject model = null;
        File model3d = proteins.getPDBModelFile(id);
        if (model3d != null) {
            try {
                model = new PDBObject(model3d,pfd.getProtein());
                pfd.setModel(model);
            }
            catch (NumberFormatException e) {
                System.out.println("problem " + id + " " + e.getMessage());
                System.out.println("Bad Models " + bad_models++);
            }
            catch (IllegalArgumentException e) {
                if (!e.getMessage().startsWith("Bad amino acid abbreviation"))
                    throw new RuntimeException(e);
                else
                    System.out.println("problem " + id + " " + e.getMessage());
            }
        }

        HTMLPageBuillder pb = new HTMLPageBuillder("Coverage for " + id);
        HTMLBodyBuillder body = pb.getBody();
        Protein protein = pfd.getProtein();
        body.addString("<a href=\"../Index.html\" >Home</a>\n");
        body.addString("<h1>" + pb.getTitle() + "</h1>\n");
        body.addString("<h3>" + protein.getAnnotation() + "</h3>\n");
        if (model == null) {
            new HTMLHeaderHolder(body, "No 3D Model Found", 1);
        }

        double fractionalCoverage = pfd.getFractionalCoverage();
        if (fractionalCoverage == 0)
            return null;
        int coveragePercent = (int) (100 * fractionalCoverage);
        body.addString("<h2>Coverage % " + coveragePercent + "</h2>\n");

        new CoverageColorsLabel(body);
        new SingleTagBuillder(body, "p");

        CoverageFragment cf = new CoverageFragment(body, pfd);

        SingleTagBuillder st = new SingleTagBuillder(body, "p");

        if (model != null) {
            ThreeDModelBuillder tm = new ThreeDModelBuillder(body, pfd);

        }
        else {
            new HTMLHeaderHolder(body, "No 3D Model Found", 1);
        }

        String page = pb.buildPage();
        String fileName = "pages/" + id + ".html";
        new File("pages").mkdirs();
        FileUtilities.writeFile(fileName, page);
        return fileName;
    }

    protected String buildIndexPage(String[] ids, String[] pages) {
        ProteinCollection proteins = getProteins();
        HTMLPageBuillder pb = new HTMLPageBuillder("Protein Coverage ");
        HTMLBodyBuillder body = pb.getBody();

        List<String> idWith3dModel = new ArrayList<String>();
        List<String> idWithout3dModel = new ArrayList<String>();
        List<String> pageWith3dModel = new ArrayList<String>();
        List<String> pageWithout3dModel = new ArrayList<String>();

        for (int i = 0; i < ids.length; i++) {
            String id = ids[i];
            String page = pages[i];
            ProteinFragmentationDescription pd = proteins.getProteinFragmentationDescription(id);
            if (pd.getModel() != null) {
                idWith3dModel.add(id);
                pageWith3dModel.add(page);
            }
            else {
                idWithout3dModel.add(id);
                pageWithout3dModel.add(page);

            }
        }
        String[] empty = {};
        if (!idWith3dModel.isEmpty()) {
            body.addString("<h1>Proteins With 3D Models</h1>");
            new ReferenceTableBuillder(body, idWith3dModel.toArray(empty), pageWith3dModel.toArray(empty), 12);
        }
        if (!idWithout3dModel.isEmpty()) {
            body.addString("<h1>Proteins Without 3D Models</h1>");
            new ReferenceTableBuillder(body, idWithout3dModel.toArray(empty), pageWithout3dModel.toArray(empty), 12);
        }
        String page = pb.buildPage();
        String fileName = "Index.html";
        FileUtilities.writeFile(fileName, page);
        return fileName;
    }


}
