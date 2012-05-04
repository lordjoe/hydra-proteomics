package org.systemsbiology.tcga;

/**
 * org.systemsbiology.tcga.TCGASampleType
 * written by Steve Lewis
 * on Apr 9, 2010
 */
public enum TCGASampleBaseType
{
    Normal,Tumor,Control,Other;

    public static final TCGASampleBaseType[] EMPTY_ARRAY = {};
    public static final Class THIS_CLASS = TCGASampleBaseType.class;

    public static TCGASampleBaseType fromString(String s)
    {
        if(s.startsWith("0"))
            return Tumor;
        if(s.startsWith("1"))
            return Normal;
        if(s.startsWith("2"))
             return Control;
       return Other;
    }


}