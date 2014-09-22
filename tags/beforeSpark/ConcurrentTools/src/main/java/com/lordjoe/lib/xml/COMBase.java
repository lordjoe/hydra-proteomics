package com.lordjoe.lib.xml;
import com.lordjoe.utilities.*;
import java.util.*;
import java.io.*;
/**{ class
@name SerializerParseException
@function thrown when XMLSerializer cannot parse
}*/ 
public abstract class COMBase  implements  ITagHandler
{
    private String m_Name;
    
    public COMBase() 
    {
    }

    
    public void register(Map TheMap)
    {
        TheMap.put(getName(),this);
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
   
    
}
