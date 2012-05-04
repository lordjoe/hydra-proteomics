
package com.lordjoe.lib.xml;

import com.lordjoe.utilities.*;


/**
* An unknown property was requested
* com.lordjoe.lib.xml.UnknownPropertyException
*/
public class UnknownClassPropertyException extends RuntimeException
{
    public UnknownClassPropertyException(String unknown,Class TargetClass) {
        super(buildMessage(unknown, TargetClass));
    }
    
    protected static String buildMessage(String unknown,Class TargetClass)
    {
        String message = "The Property " + unknown  + " is not understood!\n" +
        "Known Properties of class " + TargetClass.getName() + " are:\n";
        ClassProperty[] props = ClassAnalyzer.getProperties(TargetClass);
         String[] PropNames = new String[props.length];
        for(int i = 0; i < PropNames.length; i++)
            PropNames[i] = props[i].getName();
        Util.sort(PropNames);
        message += Util.buildListString(PropNames,1);
        return(message);
    }
}
