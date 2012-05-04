package com.lordjoe.lib.xml;


/**
 * Help class with static methods to set and get properties by name and 
 * access any exposed collections in an object
 * @author Steve Lewis
 * @since july 99
 */

public interface IObjectConverter
{
    /**
    * Convert a object to a specified class
    * @param in non-null Object to convert
    * @param DesiredClass non-null target class
    * @return possibly null return
    */
    public Object convertToDesiredClass(Object in,Class DesiredClass);
}