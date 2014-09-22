package com.lordjoe.lib.xml;

/**
* interface saying the object can be converted to a string and back 
* use only for non-composit object which can be represented as an XML attribute
*/
public interface OIStringConverter
{
    /** 
        convert the object into a String which can be used to reset the state
        @return non-null String representing the current state-  
    */
    public String toDataString();
    /** 
        convert the object from a String which can be used to reset the state
        @param in non-null String representing the current state  
    */
    public void fromDataString(String in);
// end class OIStringConverter
}
