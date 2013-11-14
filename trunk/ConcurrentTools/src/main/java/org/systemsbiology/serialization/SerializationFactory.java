package org.systemsbiology.serialization;

import java.util.*;

/**
 * org.systemsbiology.serialization.SerializationFactory
 * written by Steve Lewis
 * on Apr 9, 2010
 */
public class SerializationFactory implements ISerializerFactory {
    public static final SerializationFactory[] EMPTY_ARRAY = {};
    public static final Class THIS_CLASS = SerializationFactory.class;


    private static ISerializerFactory gInstance;

    public static synchronized ISerializerFactory getInstance() {
        if (gInstance == null)
            gInstance = new SerializationFactory(); // default implementation
        return gInstance;
    }

    public static synchronized void setInstance(ISerializerFactory pInstance) {
        if (gInstance != null)
            throw new UnsupportedOperationException("Can only set singleton once");
        gInstance = pInstance;
    }

    private List<ISerializer> m_Serializers = new ArrayList<ISerializer>();

    private SerializationFactory() {

    }

    public void register(ISerializer ser) {
        synchronized (m_Serializers) {
            if (!m_Serializers.contains(ser))
                m_Serializers.add(ser);
        }
    }

    public ISerializer getMatchingSerializer(String fileName) {
        synchronized (m_Serializers) {
            for (ISerializer test : m_Serializers) {
                if (test.canHandle(fileName))
                    return test;

            }
            return null;
        }
    }

    public Object deserialize(String name) {
        ISerializer ser = getMatchingSerializer(name);
        if (ser != null)
            return ser.Deserialize(name);
        else
            return null;
    }

    public boolean serialize(String name, Object target) {
        ISerializer ser = getMatchingSerializer(name);
        if (ser != null) {
            ser.SerializeTo(target, name);
            return true;
        } else
            return false;
    }
}
