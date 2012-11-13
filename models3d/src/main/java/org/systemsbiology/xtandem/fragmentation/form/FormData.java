package org.systemsbiology.xtandem.fragmentation.form;

import java.util.*;

/**
 * org.systemsbiology.xtandem.fragmentation.form.FormData
 * User: steven
 * Date: 11/12/12
 */
public class FormData {
    public static final FormData[] EMPTY_ARRAY = {};

    private final String m_Name;
    private final Map<String, FormDataItem> m_Data = new HashMap<String, FormDataItem>();

    public FormData(String name) {
        m_Name = name;
    }

    public void addFormData(FormDataItem fd) {
        String name = fd.getName();
        if (m_Data.containsKey(name))
            throw new IllegalStateException("duplicate item " + name);
        m_Data.put(name, fd);
    }

    public FormDataItem getData(String name) {
        return m_Data.get(name);
    }





























































    public String[] getDataNames() {
        String[] ret = m_Data.keySet().toArray(new String[0]);
        Arrays.sort(ret);
        return ret;
    }

    public String isValid() {
        StringBuilder sb = new StringBuilder();

        for (FormDataItem fd : m_Data.values()) {
            String reason = fd.isValid();
            if (reason != null) {
                if (sb.length() > 0)
                    sb.append("\n");
                sb.append(reason);
            }

        }
        if (sb.length() > 0)
            return sb.toString();

        return null; // all ok
    }

}
