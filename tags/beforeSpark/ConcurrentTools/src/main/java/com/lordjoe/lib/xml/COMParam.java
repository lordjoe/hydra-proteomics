package com.lordjoe.lib.xml;
import com.lordjoe.utilities.*;
import java.util.*;
import java.io.*;
/**
* Class representing a method parameter or return
* @authos Steve Lewis
*/ 
public class COMParam  implements ITagHandler
{
    public static final String USER_DEF_PREFIX = "VT_USERDEFINED:";
    private String m_Name;
    private String m_Com_Type;
    private String m_Type;
    private int m_Flags;
    
    public COMParam() 
    {
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
    * code to get parameter Flags
    * @return <Add Comment Here>
    * @see getFlags
    */
    public int getFlags()
    {
        return(m_Flags);
    }
    
    public boolean isOptional()
    {
        if((m_Flags & 0x10) == 0)
            return(false);
        return(true);
    }

    /**
    * code to set parameter Flags
    * @param in <Add Comment Here>
    * @see getFlags
    */
    public void setFlags(int in)
    {
        m_Flags = in;
    }

    /**
    * code to get parameter Com_Type
    * @return <Add Comment Here>
    * @see getCom_Type
    */
    public String getCom_Type()
    {
        return(m_Com_Type);
    }

    /**
    * code to set parameter Com_Type
    * @param in <Add Comment Here>
    * @see getCom_Type
    */
    public void setCom_Type(String in)
    {
        m_Com_Type = in;
    }

    /**
    * code to get parameter Type
    * @return <Add Comment Here>
    * @see getType
    */
    public String getType()
    {
        return(m_Type);
    }
    
    public boolean isTypeEnum()
    {
        if(!getType().equals("int"))
            return(false);
        if(!getCom_Type().startsWith(USER_DEF_PREFIX))
            return(false);
        return(true);
    }

    /**
    * code to set parameter Type
    * @param in <Add Comment Here>
    * @see getType
    */
    public void setType(String in)
    {
        m_Type = in;
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
        return(null);
    }
    
}
