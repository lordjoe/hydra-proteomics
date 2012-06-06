package org.systemsbiology.common;

import com.lordjoe.utilities.*;

/**
 * org.systemsbiology.common.FilterXMLLines
 *
 * @author Steve Lewis
 * @date 6/5/12
 */
public class FilterXMLLines
{
    public static FilterXMLLines[] EMPTY_ARRAY = {};
    public static Class THIS_CLASS = FilterXMLLines.class;

    /**
     * read all protein tags filtering to pick up the id following |ref|
     * @param args file fo filter
     */
    public static void main(String[] args)
    {
        final String[] strings = FileUtilities.readInLines(args[0]);
        for (int i = 0; i < strings.length; i++) {
            String string = strings[i].trim();
            if(!string.startsWith("<protein protein_name=\""))
                continue;
            int index1 = string.indexOf("ref|") + "ref|".length();
            int index2 = string.indexOf("|\"");
               String id = string.substring(index1,index2);
            System.out.println(id);
        }
    }
}
