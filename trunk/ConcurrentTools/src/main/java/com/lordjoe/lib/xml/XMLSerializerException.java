package com.lordjoe.lib.xml;
import com.lordjoe.utilities.*;

import java.util.*;

/**{ class
@name SerializerParseException
@function thrown when XMLSerializer cannot parse
}*/ 
public class XMLSerializerException extends RuntimeException
{
    public XMLSerializerException(Exception ex) {
        super(ex);
    }

    public XMLSerializerException(Exception ex, List tagStack, List objectStack)
    {
        super(buildErrorMessage(ex, tagStack, objectStack),ex);
    }
    
    protected static String buildErrorMessage(Exception ex, List eleStack, List objectStack) 
    {
        String[] elements = new String[eleStack.size()];
        eleStack.toArray(elements);
        Object[] handlers = new Object[objectStack.size()];
        objectStack.toArray(handlers);
        StringBuffer sb = new StringBuffer(256);
	sb.append("Error processing XML at:\n");
        for(int i = 0; i < elements.length; i++) {
            sb.append("     Element: " + elements[i]);
            if(handlers.length > i )
                sb.append(" Handler: " + handlers[i].getClass().getName());
           sb.append("\n");
        }
	sb.append("\n");
        return(sb.toString());
        
    }
    
}
