package org.systemsbiology.xtandem.fragmentation.ui.form;

import java.io.*;

/**
 * org.systemsbiology.xtandem.fragmentation.ui.form.TextAreaBuilder
 * User: steven
 * Date: 11/16/12
 */
public class TextAreaBuilder extends AbstractInputHtml {
    public static final TextAreaBuilder[] EMPTY_ARRAY = {};

    public static final String TAG = "textarea";

    private final int m_Rows;
    private final int m_Cols;
    public TextAreaBuilder(HTMLFormBuillder page, String name,int rows,int cols) {
        super(page, name, HtmlInputTypes.textarea);
        m_Rows = rows;
        m_Cols = cols;
    }

    public int getRows() {
        return m_Rows;
    }

    public int getCols() {
        return m_Cols;
    }

    @Override
    public String getTag() {
        return TAG;
    }

    protected void addNameText(final Appendable out, final Object... data) {
         try {
             if(getName() != null) {
                 out.append(getName());
                 if(getError() != null)
                     out.append(getError());
                 out.append("<br>");
              }
         }
         catch (IOException e) {
             throw new RuntimeException(e);

         }
    }

    @Override
    public void addStartText(final Appendable out, final Object... data) {
        super.addStartText(out, data);
        try {
            if(getValue() != null)
               out.append(getValue());
        }
        catch (IOException e) {
            throw new RuntimeException(e);

        }

    }

    /**
     * overtide to set other attributes
     * @param out
     * @param data
     */
    @Override
    public void addOtherAttributes(Appendable out, Object[] data) {
        try {
            out.append(" rows=\"" + getRows() + "\" ");
            out.append(" cols=\"" + getCols() + "\" ");
         }
        catch (IOException e) {
            throw new RuntimeException(e);

        }

    }





}
