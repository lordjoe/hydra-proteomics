package com.lordjoe.lib.xml;
import com.lordjoe.utilities.*;
import java.util.*;
import java.io.*;
/**{ class
@name SerializerParseException
@function thrown when XMLSerializer cannot parse
}*/ 
public class COMCOClass extends COMBase
{
    private String m_IID;
    private Set m_items;
    
    public COMCOClass() 
    {
        m_items = new HashSet();
    }
    
    public void register(Map TheMap)
    {
        super.register(TheMap);
        String[] items = getitems();
        for(int i = 0; i < items.length; i++)
            TheMap.put(items[i],this);
    }
    
    public void addItem(String name)
    {
        m_items.add(name);
    }
    

    /**
    * code to get parameter IID
    * @return <Add Comment Here>
    * @see getIID
    */
    public String getIID()
    {
        return(m_IID);
    }

    /**
    * code to set parameter IID
    * @param in <Add Comment Here>
    * @see getIID
    */
    public void setIID(String in)
    {
        m_IID = in;
    }

    /**
    * code to get parameter items
    * @return <Add Comment Here>
    * @see getitems
    */
    public String[] getitems()
    {
        String[] ret = new String[m_items.size()];
        m_items.toArray(ret);
        return(ret);
    }
    
    /** 
    * This returns the object which will handle the tag - the handler
    * may return itself or create a sub-object to manage the tag
    * @param TagName non-null name of the tag
    * @param attributes non-null array of name-value pairs
    * @return possibly null handler
    */
    public Object handleTag(String TagName,NameValue[] attributes)
    {
        if(TagName.equals("typedoc")) {
            NameValue nv = XMLUtil.findRequiredNameValue("doc", attributes);
            String name = nv.m_Value.toString();
            nv.m_Name = XMLUtil.TAG_HANDLED;
            addItem(name);
            return(null);
        }
        return(null);
    }
    
}
