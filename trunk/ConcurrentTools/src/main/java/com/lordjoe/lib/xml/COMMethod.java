package com.lordjoe.lib.xml;
import com.lordjoe.utilities.*;
import java.util.*;
import java.io.*;
/**{ class
@name SerializerParseException
@function thrown when XMLSerializer cannot parse
}*/ 
public class COMMethod  implements  ITagHandler
{
    private String m_Name;
    private int    m_DispID;
    private String  m_Kind;
    private COMParam m_Return;
    private COMParam[] m_Params;
    
    public COMMethod() 
    {
        m_Params = new COMParam[0];
    }
    
    public boolean hasReturn()
    {
        return(m_Return != null && !m_Return.getType().equals("void"));
    }
    
    public int getNumberOptionalParams()
    {
        int ret = 0;
        for(int i = 0; i < m_Params.length; i++) {
            if(m_Params[i].isOptional())
                ret++;
        }
        return(ret);
    }
    
    /**
    * return true if the methos could be called with TestPar parameters
    * @param TestParams non-nugative int
    * @return as above
    */
    public boolean hasParamCount(int TestParams)
    {
        int NParams = getParamCount();
        if(TestParams > NParams)
            return(false);
        if(TestParams == NParams)
            return(true);
        return((NParams - getNumberOptionalParams()) <= TestParams);
    }
    
    /**
    * if the return is ambigouos re generate different method name and
    * add the real method to the editable wrapper so the 
    * developer can set the real return type
    */
    public boolean isReturnAmbiguous()
    {
        if(!hasReturn())
            return(false);
        if(m_Return.getType().equals("Object"))
            return(true);
        if(m_Return.getType().equals("Dispatch"))
            return(true);
        if(m_Return.getType().equals("IUnknown"))
            return(true);
        if(m_Return.getType().equals("IDispatch"))
            return(true);
        return(false);
    }
    
    
    /**
    * code to get parameter Name
    * @return <Add Comment Here>
    * @see getName
    */
    public String getName()
    {
        return(m_Name);
    }

    /**
    * code to set parameter Name
    * @param in <Add Comment Here>
    * @see getName
    */
    public void setName(String in)
    {
        m_Name = in;
    }

    /**
    * code to get parameter DispID
    * @return <Add Comment Here>
    * @see getDispID
    */
    public int getDispID()
    {
        return(m_DispID);
    }

    /**
    * code to set parameter DispID
    * @param in <Add Comment Here>
    * @see getDispID
    */
    public void setDispID(int in)
    {
        m_DispID = in;
    }

    /**
    * code to get parameter Kind
    * @return <Add Comment Here>
    * @see getKind
    */
    public String getKind()
    {
        return(m_Kind);
    }

    /**
    * code to set parameter Kind
    * @param in <Add Comment Here>
    * @see getKind
    */
    public void setKind(String in)
    {
        m_Kind = in;
    }

    /**
    * code to get parameter Return
    * @return <Add Comment Here>
    * @see getReturn
    */
    public COMParam getReturn()
    {
        return(m_Return);
    }

    /**
    * code to set parameter Return
    * @param in <Add Comment Here>
    * @see getReturn
    */
    public void setReturn(COMParam in)
    {
        m_Return = in;
    }

    /**
    * code to get parameter Params
    * @return <Add Comment Here>
    * @see getParams
    */
    public COMParam[] getParams()
    {
        COMParam[] ret = new COMParam[m_Params.length];
        System.arraycopy(m_Params, 0, ret, 0, ret.length);
        return(ret);
    }

    /**
    * code to add an object to Params
    * @param added non-null item to add
    * @see removeParams
    */
    public void addParam(COMParam added)
    {
        m_Params = (COMParam[])Util.addToArray(m_Params,added);
    }

    /**
    * code to add an object to Params
    * @param added non-null item to add
    * @see addParams
    */
    public void removeParam(COMParam removed)
    {
        m_Params = (COMParam[])Util.removeFromArray(m_Params,removed);
    }

    /**
    * return the number of elements in Params
    * @return non-negative count
    */
    public int getParamCount()
    {
        return(m_Params.length);
    }

    /**
    * code get an iterator over Params
    * @return  non-null iterator
    */
    public Iterator getParamIterator()
    {
        return(Util.arrayIterator(m_Params));
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
        if(TagName.equals("param")) {
            COMParam added = new COMParam();
            addParam(added);
            return(added);
        }
        if(TagName.equals("return")) {
            COMParam ret = new COMParam();
            setReturn(ret);
            return(ret);
        }
        if(TagName.equals("kind")) {
            NameValue nv = XMLUtil.findRequiredNameValue("value", attributes);
            String name = nv.m_Value.toString();
            nv.m_Name = XMLUtil.TAG_HANDLED;
            setKind(name);
            return(null);
        }
        return(null);
    }
    
}
