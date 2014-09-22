package org.systemsbiology.xtandem.fragmentation.form;

/**
 * org.systemsbiology.xtandem.fragmentation.form.FormDataItem
 * User: steven
 * Date: 11/12/12
 */
public class FormDataItem {
    public static final FormDataItem[] EMPTY_ARRAY = {};

    private final String m_Name;
    private final boolean m_Required;
    private String m_Value;

    public FormDataItem(String name, boolean required) {
        m_Name = name;
        m_Required = required;
    }

    public FormDataItem(String name, boolean required,String value) {
        this(name,   required ) ;

    }

    public String getName() {
        return m_Name;
    }

    public boolean isRequired() {
        return m_Required;
    }

    public String getValue() {
        return m_Value;
    }

    public void setValue(String value) {
        m_Value = value;
    }

    /**
     * non-null is invalid reason
     * @return
     */
    public String isValid()
    {
        if(isRequired() && getValue() == null)
            return "Required Field " + getName() +  " not set";
        return null;  // OK
    }
}
