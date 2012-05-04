package org.systemsbiology.serialization;

/**
 * org.systemsbiology.serialization.ISerializerFactory
 * written by Steve Lewis
 * on Apr 9, 2010
 */
public interface ISerializerFactory
{
    public static final ISerializerFactory[] EMPTY_ARRAY = {};
    public static final Class THIS_CLASS = ISerializerFactory.class;


    public void register(ISerializer ser);

    public ISerializer getMatchingSerializer(String fileName);

    public Object deserialize(String name);

    public boolean serialize(String name,Object target);
}