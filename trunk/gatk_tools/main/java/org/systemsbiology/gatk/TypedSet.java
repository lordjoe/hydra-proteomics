package org.systemsbiology.gatk;

import java.util.*;

/**
 * org.systemsbiology.gatk.TypedSet
 * Sample of Java implementation of code from the Substitution paper
 * User: Steve
 * Date: 6/23/12
 */
public class TypedSet<T> {
    public static final TypedSet[] EMPTY_ARRAY = {};

    private final Class m_HeldClass;
    private final Set<T> m_Holder = new HashSet<T>();

    public TypedSet(T[] memberClass) {
        m_HeldClass = memberClass.getClass().getComponentType();
    }

    public Class getHeldClass() {
        return m_HeldClass;
    }

    public void add(T added)  {
        if(!getHeldClass().isInstance(added))
            throw new IllegalArgumentException("object of type " + added.getClass() +
                    " is not an instance of " + getHeldClass());
        m_Holder.add(added);
    }


    public void delete(T deleted)  {
         m_Holder.remove(deleted);
    }

    public boolean isMember(T test)  {
         return m_Holder.contains(test);
    }

    /**
     * Who knows what this is
     */
    public static class PesistentObject {

    }

    /**
     * blank inplementation of  ThirdPartyPersistentSet
     */
    public static class ThirdPartyPersistentSet {
        public void add(PesistentObject added)  {
            throw new UnsupportedOperationException("Fix This");
             }


         public void delete(PesistentObject deleted)  {
             throw new UnsupportedOperationException("Fix This");
         }

         public boolean isMember(PesistentObject test)  {
             throw new UnsupportedOperationException("Fix This");
         }

    }

    /**
     * implementation of TypedSet using third party container of third party object
     * @param <T>
     */
    public static class PersistentSet<T extends TypedSet.PesistentObject> extends TypedSet<T>
    {
        private final  ThirdPartyPersistentSet m_PersistentSet = new ThirdPartyPersistentSet();
        public PersistentSet( ) {
            super((T[])new PesistentObject[0]);   // the cast is pretty harmless
        }


        public void add(T added)  {
             m_PersistentSet.add(added);
        }


        public void delete(T deleted)  {
             m_PersistentSet.delete(deleted);
        }

        public boolean isMember(T test)  {
             return m_PersistentSet.isMember(test);
        }
    }

 }
