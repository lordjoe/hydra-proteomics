package org.systemsbiology.xtandem.fragmentation.ui.form;

import org.systemsbiology.xtandem.fragmentation.ui.*;

import java.io.*;

/**
 * org.systemsbiology.xtandem.fragmentation.ui.HTMLPageBuillder
 * User: Steve
 * Date: 6/25/12
 */
public class AbstractInputHtml extends AbstractHtmlFragmentHolder {
    public static final AbstractInputHtml[] EMPTY_ARRAY = {};

    public static final String TAG = "input";

    private final String m_Name;
    private  String m_VisibleName;
    private String m_Value = "";
    private String m_Error = "";
    private final HtmlInputTypes m_Type;
    private boolean m_BreakAppendedAtEnd = true;

    public AbstractInputHtml(final HTMLFormBuillder page,String name, HtmlInputTypes actionUrl) {
        super(page);
        m_Type = actionUrl;
        m_Name = name;
        m_VisibleName= name;
     }

    public String getTag() {
        return TAG;
    }

    public String getName() {
        return m_Name;
    }

    public String getVisibleName() {
        return m_VisibleName;
    }

    public void setVisibleName(final String visibleName) {
        m_VisibleName = visibleName;
    }

    public HtmlInputTypes getType() {
        return m_Type;
    }


    public String getValue() {
        return m_Value;
    }

    public void setValue(final String value) {
        m_Value = value;
    }

    public String getError() {
        return m_Error;
    }

    public void setError( String error) {
        if(error == null)
            error = "";
        m_Error = error;
    }

    @Override
    public void addStartText(final Appendable out, final Object... data) {
        addNameText(  out,  data);
        addFieldText(out, data);
     }

    /**
     * add a name label before the field
     * @param out
     * @param data
     */
    protected void addNameText(final Appendable out, final Object... data) {
        try {
            if(getVisibleName() != null)
                 out.append(getVisibleName());
        }
        catch (IOException e) {
            throw new RuntimeException(e);

        }
    }
    protected void addFieldText(final Appendable out, final Object... data) {
          try {
               out.append("<" + getTag());
              out.append(" type=\"" + getType());
              out.append("\" ");
              out.append(" name=\"" + getName());
              out.append("\" ");

              addOtherAttributes(out, data);
              out.append(" >");
               out.append("\n");
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
    public void addOtherAttributes(Appendable out, Object[] data) {
    }

    @Override
    public void addEndText(final Appendable out, final Object... data) {
        try {
            out.append("</" + getTag() + ">");
            if(isBreakAppendedAtEnd())
                out.append("<br>");
             out.append("\n");
            if(getError().length() > 0)  {
                out.append(getError());
            }
        }
        catch (IOException e) {
            throw new RuntimeException(e);

        }

    }

    public boolean isBreakAppendedAtEnd() {
        return m_BreakAppendedAtEnd;
    }

    public void setBreakAppendedAtEnd(final boolean breakAppendedAtEnd) {
        m_BreakAppendedAtEnd = breakAppendedAtEnd;
    }
}
