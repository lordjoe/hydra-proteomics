package org.systemsbiology.common;

/**
 * org.systemsbiology.common.IStringConverter
 * @author Steve Lewis
 * @date Jun 2, 2006
 */
public interface IStringConverter<T>
{
    public static final IStringConverter[] EMPTY_ARRAY = {};

    public String convertToString(T obj);

    public T convertFromString(String str);

    public boolean isApplicable(Class test);

}
