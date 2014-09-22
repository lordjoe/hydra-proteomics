package com.lordjoe.lib.xml;

import com.lordjoe.lib.xml.*;

import java.io.*;

/**
 * com.lordjoe.lib.xml.IXMLWriter
 * @author smlewis
 * Date:  Sep 17, 2002
 */
public interface IXMLWriter
 {
    public static final IXMLWriter[] EMPTY_ARRAY = {};

    public String getXMLString();

    public void appendXML(XMLPropertySet props,Appendable sb,int indent) throws IOException;
 }