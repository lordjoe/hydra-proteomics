package com.lordjoe.lib.xml;

/**
 * com.lordjoe.lib.xml.IAttributeBuilder
 *  @see ClassAnalyzer.registerBuilder
 * @author Steve Lewis
 * @date Dec 18, 2007
 */
public interface IAttributeBuilder {
    public static IAttributeBuilder[] EMPTY_ARRAY = {};
    public static Class THIS_CLASS = IAttributeBuilder.class;

    /**
     * build a attribute to add t a class
     * @param AttributeName non-null name of the attribute
     * @param analysis non-null analysis of the attribute
     * @return possibly null attribute
     */
    public Object buildAttribute(String AttributeName, ClassAnalysis analysis);
}
