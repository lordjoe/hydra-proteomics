package com.lordjoe.algorithms;

import com.sun.istack.internal.*;

import java.util.*;

/**
 * com.lordjoe.algorithms.CountedSet
 * User: Steve
 * Date: 1/21/14
 * a set that counts the number of times an item is added
 */
public class CountedSet<T> {

    private Map<T, Integer> m_Counts = new HashMap<T, Integer>();

    public void clear() {
        m_Counts.clear();
    }


    public void add(@NotNull T added) {
        int count = getCount(added);
        m_Counts.put(added, count + 1);
    }

    /**
     * return the count for any item
     * @param key
     * @return  count or 0 of nit fount
     */
    public int getCount(@NotNull T key) {
        if (m_Counts.containsKey(key))
            return m_Counts.get(key);
        else
            return 0;

    }

    /**
     * return all items with counts at least x
     * @param minCount
     * @return
     */
    public @NotNull  List<T> getItemsWithCount(int minCount) {
        List ret = new ArrayList() ;
        for (T t : m_Counts.keySet()) {
             int test = m_Counts.get(t);
            if(test >= minCount)
                ret.add(t);
        }
        return (List<T>)ret;
     }

}
