package org.systemsbiology.xtandem.fragmentation.ui;

import com.lordjoe.utilities.*;
import org.systemsbiology.jmol.*;
import org.systemsbiology.xtandem.fragmentation.*;

import java.io.*;
import java.util.*;

/**
 * org.systemsbiology.xtandem.fragmentation.ui.ProteinCoveragePageBuilder
 * User: Steve
 * Date: 6/28/12
 */
public class ProteinCoveragePageTracker {
    public static final ProteinCoveragePageTracker[] EMPTY_ARRAY = {};

    public static final int MAX_COVERAGE = 8;
    public static final int INDEX_ROW_LENGTH = 10;


    public static final String HOME_PAGE = "IndexGood.html";

    public static final FilenameFilter HTML_FILTER = new FileUtilities.EndsWithFilter(".html");


    private final File m_PageDir;
    private final Set<File> m_PageFiles = new HashSet<File>();
    private final Set<File> m_PageFilesWithModel = new HashSet<File>();
    private final Set<File> m_PageFilesWithoutModel = new HashSet<File>();
    private boolean m_PagesRead;

    public ProteinCoveragePageTracker(final File baseDir) {
        m_PageDir = baseDir;
        File[] files = m_PageDir.listFiles(HTML_FILTER);
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            processPage(file);
        }
        setPagesRead(true);
    }

    protected void processPage(File file) {
        String[] lines = FileUtilities.readNLines(file, 3);
        if (!isAnnotated(lines)) {
            annotateFile(file);
            lines = FileUtilities.readNLines(file, 3);
        }
        handleAnnotation(file, lines[1]);
    }

    private void handleAnnotation(File file, String line) {
        String model = extractText(line, "Model=");
        int coverage = Integer.parseInt(extractText(line, "Coverage="));
        if (model != null)
            m_PageFilesWithModel.add(file);
        else
            m_PageFilesWithoutModel.add(file);

    }

    private String extractText(String line, String model) {
        int index = line.indexOf(model);
        if (index < 0)
            return null;

        int fromIndex = index + model.length();
        int end = line.indexOf(" ", fromIndex);
        return line.substring(fromIndex, end);

    }

    public static final String LINE_1 = "<!doctype html>";
    public static final String LINE_2_START = "<!-- ANNOTATION ";
    public static final String EMPTY_ANNOTATION = "<!-- ANNOTATION  -->";

    private boolean isAnnotated(String[] lines) {

        if (lines.length < 3)
            return false;
        if (!LINE_1.equals(lines[0]))
            return false;
        if (lines[1].equals(EMPTY_ANNOTATION))
            return false;
        if (!lines[1].startsWith(LINE_2_START))
            return true;
        // todo add other tests
        return true;
    }

    protected void annotateFile(File file) {
        String[] lines = FileUtilities.readInLines(file);
        StringBuilder sb = new StringBuilder();

        sb.append(LINE_1);
        sb.append("\n");
        String annotation = buildAnnotation(file, lines);
        sb.append(annotation);
        sb.append("\n");

        int i = 0;
        if (lines[0].equals(LINE_1))
            i = 2; // readding anotation
        for (; i < lines.length; i++) {
            String line = lines[i];
            sb.append(line);
            sb.append("\n");

        }

        String text = sb.toString();
        FileUtilities.writeFile(file, text);
    }

    protected String buildAnnotation(File file, String[] lines) {
        StringBuilder sb = new StringBuilder();
        sb.append(LINE_2_START);

        String model = getModel(lines);
        if (model != null) {
            sb.append(" Model=" + model);
        }
        sb.append(" Coverage=" + getCoverage(lines));
        sb.append(" -->");
        return sb.toString();
    }

    public static final String COVERAGE_START = "<h2>Coverage % ";

    protected int getCoverage(String[] lines) {
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            int index = line.indexOf(COVERAGE_START);
            if (index > -1) {
                int startIndex = index + COVERAGE_START.length();
                return Integer.parseInt(line.substring(startIndex, startIndex + 2).trim());
            }
        }
        return 0;
    }


    public static final String MODEL_START = "<h1>Using Model ";
    public static final String NO_MODEL_TEXT = "<h1>No 3D Model Found</h1>";

    protected String getModel(String[] lines) {
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            if (NO_MODEL_TEXT.equals(line))
                return null;
            int index = line.indexOf(MODEL_START);
            if (index > -1) {
                return extractText(line, MODEL_START);
            }
        }
        return null;
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

    protected void addIndexes(HTMLBodyBuillder body)
    {
        guaranteePages();

           List<String> pageWith3dModel = new ArrayList<String>();
           List<String> pageWithout3dModel = new ArrayList<String>();
           List<ProteinFragmentationDescription> pfdWith3dModel = new ArrayList<ProteinFragmentationDescription>();

           String[] empty = {};
           ProteinFragmentationDescription[] emptyPd = {};
           if (!m_PageFilesWithModel.isEmpty()) {
               body.addString("<h1>Proteins With 3D Models</h1>\n");
               List<String> holder = new ArrayList<String>();
               List<String> holder2 = new ArrayList<String>();
               File[] files = m_PageFilesWithModel.toArray(new File[0]);
               Arrays.sort(files);
               for (int i = 0; i < files.length; i++) {
                   File file = files[i];
                   String name = file.getName().replace(".html", "");
                   holder.add(name);
                   holder2.add("pages/" + file.getName());

               }

               String[] NameWith3dModel = new String[holder.size()];
               holder.toArray(NameWith3dModel);
               String[] UrlWith3dModel = new String[holder2.size()];
               holder2.toArray(UrlWith3dModel);
               new StringTableBuillder(body, NameWith3dModel, UrlWith3dModel, INDEX_ROW_LENGTH);
           }
           if (!m_PageFilesWithoutModel.isEmpty()) {
               body.addString("<h1>Proteins Without 3D Models</h1>");
               List<String> holder = new ArrayList<String>();
               List<String> holder2 = new ArrayList<String>();
               File[] files = m_PageFilesWithoutModel.toArray(new File[0]);
               Arrays.sort(files);
               for (int i = 0; i < files.length; i++) {
                   File file = files[i];
                   String name = file.getName().replace(".html", "");
                   holder.add(name);
                   holder2.add("pages/" + file.getName());

               }

               String[] NameWith3dModel = new String[holder.size()];
               holder.toArray(NameWith3dModel);
               String[] UrlWith3dModel = new String[holder2.size()];
               holder2.toArray(UrlWith3dModel);
               new StringTableBuillder(body, NameWith3dModel, UrlWith3dModel, INDEX_ROW_LENGTH);
           }

    }

    protected void buildIndexPage(String fileName,HTMLPageBuillder pb) {

           String page = pb.buildPage();
        FileUtilities.writeFile(fileName, page);
//        Collections.sort(pfdWith3dModel, ProteinFragmentationDescription.INTERESTING_COMPARATOR);
//        for (Iterator<ProteinFragmentationDescription> iterator = pfdWith3dModel.iterator(); iterator.hasNext(); ) {
//            ProteinFragmentationDescription next = iterator.next();
//            System.out.println(next.getUniprotId() + " " + String.format("%5.3f", next.getFractionalCoverage()) + " " +
//                    next.getStatistics().getPartitionStatistics(2));
//        }
//        System.out.println();
     }

    public Set<File> getPageFiles() {
        return m_PageFiles;
    }


    public boolean isPagesRead() {
        return m_PagesRead;
    }

    public void setPagesRead(boolean pagesRead) {
        m_PagesRead = pagesRead;
    }


    protected void guaranteePages() {
        if (isPagesRead())
            return;

        setPagesRead(true);
    }


    public static void main(String[] args) {
        ProteinCoveragePageTracker pt = new ProteinCoveragePageTracker(new File("pages"));
        HTMLPageBuillder pb = new HTMLPageBuillder("Protein Coverage ");
        HTMLBodyBuillder body = pb.getBody();
        pt.addIndexes(body);
        pt.buildIndexPage("IndexGood.html",pb);
    }


}
