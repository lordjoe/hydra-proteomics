package org.systemsbiology.jmol;

import java.util.*;

/**
* org.systemsbiology.jmol.ThreeDModelStructure
* User: Steve
* Date: 6/21/12
*/
public class ThreeDModelStructure
{
    private String m_ProteinId;
    private  String m_AccessionId;
    private final Properties m_Properties = new Properties();

    public ThreeDModelStructure( ) {
      }


    public void addProperty(String id,String value)   {
         m_Properties.setProperty(id, value);

    }

    public String getProperty(String id)   {
         return m_Properties.getProperty(id);

    }


    public String getAccessionId() {
        return m_AccessionId;
    }

    public String getProteinId() {
        return m_ProteinId;
    }

    public void setAccessionId(final String accessionId) {
        m_AccessionId = accessionId;
    }

    public void setProteinId(final String proteinId) {
        m_ProteinId = proteinId;
    }
}
