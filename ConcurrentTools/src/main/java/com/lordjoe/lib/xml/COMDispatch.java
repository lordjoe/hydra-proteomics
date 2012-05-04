package com.lordjoe.lib.xml;
import com.lordjoe.utilities.*;
import java.util.*;
import java.io.*;
/**{ class
@name SerializerParseException
@function thrown when XMLSerializer cannot parse
}*/ 
public class COMDispatch  extends COMBase
{
    private List m_Methods;
    private String m_EntryInfo;
    private String m_IID;
    private boolean m_TestedForCollection;
    private boolean m_Collection;
    private boolean m_ItemCollection;
    private boolean m_NextCollection;
    
    public COMDispatch() 
    {
        m_Methods = new ArrayList();
    }
    
    public void addMethod(COMMethod added)
    {
       m_Methods.add(added);
       m_TestedForCollection = false;
    }
    
    public COMMethod[] getMethods()
    {
       COMMethod[] ret = new COMMethod[m_Methods.size()];
       m_Methods.toArray(ret);
       return(ret);
    }
    
    public boolean isCollection()
    {
        if(!m_TestedForCollection) {
            testForCollection();
        }
        return(m_Collection);
            
    }
    
    public boolean isItemCollection()
    {
        if(!m_TestedForCollection) {
            testForCollection();
        }
        return(m_ItemCollection);
            
    }
    
    public boolean isNextCollection()
    {
        if(!m_TestedForCollection) {
            testForCollection();
        }
        return(m_NextCollection);
            
    }
    
    protected void testForCollection()
    {
        m_TestedForCollection = true;
        m_Collection = false;
        COMMethod[] methods =  getMethods();
        boolean hasGetNext = false;
        boolean hasGetFirst = false;
        boolean hasGetLast = false;
        boolean hasGetCount = false;
        boolean hasItem = false;
        for(int i = 0; i < methods.length; i++) {
            COMMethod test = methods[i];
            String TestName = test.getName();
            if(TestName.equals("GetFirst") && !hasGetFirst) {
                 hasGetFirst = test.hasParamCount(0);
            }
            if(TestName.equals("GetLast") && !hasGetLast) {
                 hasGetLast = test.hasParamCount(0);
            }
            if(TestName.equals("GetNext") && !hasGetNext) {
                 hasGetNext = test.hasParamCount(0);
            }
            if(TestName.equals("Count") && !hasGetCount) {
                 hasGetCount = test.hasParamCount(0);
            }
            if(TestName.equals("Item") && !hasItem) {
                 hasItem = test.hasParamCount(1);
            }
        }
        if(hasGetNext && hasGetFirst && hasGetLast && hasGetCount) {
            m_Collection = true;
            m_NextCollection = true;
        }
        if(hasItem && hasGetCount){
            m_Collection = true;
            m_ItemCollection = true;
        }
        
    }
    

    /**
    * code to get parameter EntryInfo
    * @return <Add Comment Here>
    * @see getEntryInfo
    */
    public String getEntryInfo()
    {
        return(m_EntryInfo);
    }

    /**
    * code to set parameter EntryInfo
    * @param in <Add Comment Here>
    * @see getEntryInfo
    */
    public void setEntryInfo(String in)
    {
        m_EntryInfo = in;
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
    * This returns the object which will handle the tag - the handler
    * may return itself or create a sub-object to manage the tag
    * @param TagName non-null name of the tag
    * @param attributes non-null array of name-value pairs
    * @return possibly null handler
    */
    public Object handleTag(String TagName,NameValue[] attributes)
    {
        if(TagName.equals("funcdesc")) {
            COMMethod added = new COMMethod();
            addMethod(added);
            return(added);
        }
        /*
        * Here we build a function from a dispatch property
        */
        if(TagName.equals("vardesc")) {
            COMMethod added = new COMMethod();
            added.setKind("IDispatch.PROPERTYGET");
            
            NameValue nv = XMLUtil.findRequiredNameValue("name", attributes);
            String name = nv.m_Value.toString();
            nv.m_Name = XMLUtil.TAG_HANDLED;
            added.setName(name);
            
            nv = XMLUtil.findRequiredNameValue("id", attributes);
            String idString = nv.m_Value.toString();
            int id = Integer.parseInt(idString);
            nv.m_Name = XMLUtil.TAG_HANDLED;
            added.setDispID(id);
            
            COMParam ret = new COMParam();
            nv = XMLUtil.findRequiredNameValue("type", attributes);
            String TypeString = nv.m_Value.toString();
            nv.m_Name = XMLUtil.TAG_HANDLED;
            ret.setType(TypeString);
            
            nv = XMLUtil.findRequiredNameValue("com_type", attributes);
            String ComString = nv.m_Value.toString();
            nv.m_Name = XMLUtil.TAG_HANDLED;
            ret.setCom_Type(ComString);
            added.setReturn(ret);
            
            addMethod(added);
            // Add a set method for each property (not all will work)
            COMMethod add_set = new COMMethod();
            add_set.setKind("IDispatch.PROPERTYPUT");
            add_set.setName(name);
            add_set.setDispID(id);
            
            COMParam in = new COMParam();
            in.setName("in");
            in.setType(TypeString);
            in.setCom_Type(ComString);
            add_set.addParam(in);
            addMethod(add_set);
           
            return(null);
        }
        return(null);
    }
    
}
