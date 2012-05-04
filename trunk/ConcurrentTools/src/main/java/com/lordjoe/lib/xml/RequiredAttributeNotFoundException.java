
package com.lordjoe.lib.xml;

import com.lordjoe.utilities.*;


/**
* An unknown property was requested
* com.lordjoe.lib.xml.UnknownPropertyException
*/
public class RequiredAttributeNotFoundException extends RuntimeException
{
    public RequiredAttributeNotFoundException(String needed,NameValue[] attributes) {
        super(buildMessage(needed, attributes));
    }
    
    protected static String buildMessage(String needed,NameValue[] attributes)
    {
        StringBuffer sb = new StringBuffer();
        sb.append("The required attribute \'" + needed + "\' was not found \n" +
                "Found attributes are: ");
        for (int i = 0; i < attributes.length; i++)
        {
            NameValue attribute = attributes[i];
            sb.append(attribute.getName() + "='" + attribute.getValue() + "', ");
        }
        return sb.toString();
    }
}
