package org.systemsbiology.serialization;

import java.io.*;

/**
 * org.systemsbiology.serialization.ISerializer
 * written by Steve Lewis
 * on Apr 9, 2010
 */
public interface ISerializer
{
    public static final ISerializer[] EMPTY_ARRAY = {};
    public static final Class THIS_CLASS = ISerializer.class;

    public void Serialize(Object target, OutputStream out);

    public void SerializeTo(Object target,String name);

    public Object Deserialize(String name);

    /**
     * return true if this is the name if a serialization we can handle
     * frequently this is done by checking the extension
     * @param name name - think file name but could be part of a URL
     * @return true if it matches - usually this is an extension match
     */

    public boolean canHandle(String name);


}