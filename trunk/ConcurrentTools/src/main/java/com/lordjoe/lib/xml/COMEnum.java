package com.lordjoe.lib.xml;
import com.lordjoe.utilities.*;
import org.systemsbiology.sam.*;

import java.util.*;
import java.io.*;
/**{ class
@name SerializerParseException
@function thrown when XMLSerializer cannot parse
}*/ 
public class COMEnum  extends COMBase
{
    private NameValue[] m_items;
    
    public COMEnum() 
    {
        m_items = new NameValue[0];
    }
    
    public void addItem(String name,int value)
    {
        NameValue Added = new NameValue(name,new Integer(value));
        
        m_items = (NameValue[])Util.addToArray(m_items, Added);
    }
    
    /**
    * code to get parameter items
    * @return <Add Comment Here>
    * @see getitems
    */
    public NameValue[] getitems()
    {
        NameValue[] ret = new NameValue[m_items.length];
        System.arraycopy(m_items, 0, ret, 0, m_items.length);
        return(ret);
    }

    /**
    * code to add an object to items
    * @param added non-null item to add
    * @see removeitems
    */
    public void additems(NameValue added)
    {
        m_items = (NameValue[])Util.addToArray(m_items,added);
    }

    /**
    * code to add an object to items
    * @param added non-null item to add
    * @see additems
    */
    public void removeitems(NameValue removed)
    {
        m_items = (NameValue[])Util.removeFromArray(m_items,removed);
    }

    /**
    * return the number of elements in items
    * @return non-negative count
    */
    public int getitemsCount()
    {
        return(m_items.length);
    }

    /**
    * code get an iterator over items
    * @return  non-null iterator
    */
    public Iterator getitemsIterator()
    {
        return(Util.arrayIterator(m_items));
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
        if(TagName.equals("var")) {
            NameValue nv = XMLUtil.findRequiredNameValue("name", attributes);
            String name = nv.m_Value.toString();
            nv.m_Name = XMLUtil.TAG_HANDLED;
            nv = XMLUtil.findRequiredNameValue("value", attributes);
            String ValStr = nv.m_Value.toString();
            int value = 0;
            if(ValStr.startsWith("0x")) {
                value = Integer.parseInt(ValStr.substring(2),16);
               // System.out.println(name);
            }
            else {
                value = Integer.parseInt(ValStr);
            }
            nv.m_Name = XMLUtil.TAG_HANDLED;
            
            addItem(name,value);
            
            return(null);
        }
        if(TagName.equals("CDATA")) {
            attributes[0].m_Name = XMLUtil.TAG_HANDLED; // ignore
            return(null);
        }
        return(null);
    }
    
}
