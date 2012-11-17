package org.systemsbiology.xtandem.fragmentation.ui;

import com.lordjoe.utilities.*;
import org.systemsbiology.xtandem.fragmentation.ui.form.*;

import java.io.*;
import java.util.*;

/**
 * org.systemsbiology.xtandem.fragmentation.ui.FragmentQueryPage
 * User: steven
 * Date: 11/16/12
 */
public class FragmentQueryPage {
    public static final FragmentQueryPage[] EMPTY_ARRAY = {};

    private final HTMLPageBuillder m_Page = new HTMLPageBuillder("Proteins");
    private PageChooser m_PageChooser;
    private ProteinForm m_ProteinForm;
    private final File m_PageDir;

    public FragmentQueryPage() {
        makePageBuilder();
        m_PageDir = new File(FileUtilities.getUserDirectory(), "pages");
    }



    public HTMLPageBuillder getPage() {
        return m_Page;
    }

    public HTMLBodyBuillder getPageBody() {
        return getPage().getBody();
    }

    public File getPageDir() {
        return m_PageDir;
    }

    public void makePageBuilder() {
        getPageBody().clear();
        makeProteinForm();
        makePageChooser();
    }

    private void makeProteinForm() {
        m_ProteinForm = new  ProteinForm(getPageBody()) ;
    }

    private void makePageChooser() {
        File[] allFiles = FileUtilities.getAllFiles(getPageDir());
        List<File> holder = new ArrayList<File>();
        for (int i = 0; i < allFiles.length; i++) {
            File allFile = allFiles[i];
            if (allFile.getName().endsWith(".html"))
                holder.add(allFile);
        }
        File[] ret = new File[holder.size()];
        holder.toArray(ret);
        m_PageChooser = new PageChooser(getPageBody(), ret);
    }

    private class ProteinForm extends HTMLFormBuillder {

        public ProteinForm(HTMLBodyBuillder parent) {
            super(parent, "/svlt");
            new AbstractInputHtml(this,"Uniprot Id",HtmlInputTypes.text);
            new TextAreaBuilder(this,"Fragments",10,60);
            addSubmitButton();
         }

    }

    public static final int MAX_COLS = 5;

    private class PageChooser extends AbstractHtmlFragmentHolder {
        private final File[] m_Files;

        private PageChooser(IHtmlFragmentHolder parent, File[] files) {
            super(parent);
            m_Files = files;
        }

        @Override
        public void addStartText(Appendable out, Object... data) {
            try {
                out.append("<table>\n");
            }
            catch (IOException e) {
                throw new RuntimeException(e);

            }
        }

        @Override
        protected void appendAllBuilders(Appendable out, Object[] data) {
            super.appendAllBuilders(out, data);
            try {
                int row = 0;
                int col = 0;
                for (int i = 0; i < m_Files.length; i++) {
                    if (col++ == 0) {
                        out.append("<tr>");
                    }
                    out.append("<td>");
                    File o = m_Files[i];
                    String name = o.getName();
                    String hname = name.replace(".html", "");
                    out.append("<a href=\"pages/" + name + ">" + hname + "</a>");

                    out.append("</td>\n");

                    if (col >= MAX_COLS) {
                        out.append("</tr>\n");
                        col = 0;
                        row++;
                    }
                }
            }
            catch (IOException e) {
                throw new RuntimeException(e);

            }
        }

        @Override
        public void addEndText(Appendable out, Object... data) {
            try {
                out.append("</table>\n");
            }
            catch (IOException e) {
                throw new RuntimeException(e);

            }

        }
    }


}
