
package com.lordjoe.lib.xml;

import com.lordjoe.utilities.*;


/**
* Setting a property failed
* com.lordjoe.lib.xml.PropertySetFailException
*/
public class PropertySetFailException extends RuntimeException
{
    public PropertySetFailException(String prop,Object value,Object Target,Throwable ex) {
        super(buildMessage(prop,value,Target.getClass()),ex);
    }
    
    protected static String buildMessage(String prop,Object value,Class TargetClass)
    {
        String message = "ClassAnalyzer failed to set the property " + prop  + 
            " to the value " + value + " in target class " + TargetClass.getName(); 
        return(message);
    }
}
