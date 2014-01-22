package com.lordjoe.algorithms;

import com.sun.istack.internal.*;

import java.util.*;

/**
 * com.lordjoe.algorithms.AveragedSet
 * track the average of a number of measures associated with objects
 * User: Steve
 * Date: 1/21/14
 * a set that counts the number of times an item is added
 */
public class AveragedSet<T> {

    private Map<T, NamedCountedSum> m_Counts = new HashMap<T, NamedCountedSum>();

    public void clear() {
        m_Counts.clear();
    }


    public void add(@NotNull T added,double value) {
        if (m_Counts.containsKey(added))
               m_Counts.get(added).add(added, value);
         else
               m_Counts.put(added,new NamedCountedSum(added,value));
    }

    /**
     * return the count for any item
     * @param key
     * @return  count or 0 of nit fount
     */
    public int getCount(@NotNull T key) {
        if (m_Counts.containsKey(key))
            return m_Counts.get(key).getCount();
        else
            return 0;

    }
    /**
      * return the count for any item
      * @param key
      * @return  count or 0 of nit fount
      */
     public double getAverage(@NotNull T key) {
         if (m_Counts.containsKey(key))
             return m_Counts.get(key).getAverage();
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
             int test = m_Counts.get(t).getCount();
            if(test >= minCount)
                ret.add(t);
        }
        return (List<T>)ret;
     }

    /**
     * return all items with counts at least x
     * @param minCount
     * @return
     */
    public @NotNull  List<T> getItemsWithAverageGreater(double minAverage) {
        List ret = new ArrayList() ;
        for (T t : m_Counts.keySet()) {
            double test = m_Counts.get(t).getAverage();
            if(test >= minAverage)
                ret.add(t);
        }
        return (List<T>)ret;
     }

    public static class NamedCountedSum<T> implements Comparable<NamedCountedSum> {
        private final T name;
        private int count;
        private double sum;

        public NamedCountedSum(final T pName,double value) {
            name = pName;
            count = 1;
            sum = value;
        } 
        

        public T getName() {
            return name;
        }

        public int getCount() {
            return count;
        }

        public double getSum() {
            return sum;
        }
        public double getAverage() {
               if(count > 0)
                   return sum / count;
               else
                   return 0;
            }

        public void add(@NotNull T added,double value) {
            if(!added.equals(name))
                throw new IllegalArgumentException("adding the wrong item ");
            count++;
            sum += value;
          }

        @Override
        public String toString() {
            return name.toString() + "-"   + String.format("%10.2f",getAverage());
        }

        /**
         * sort descending average then decsanding count then name
         * @param o
         * @return
         */
        @Override
        public int compareTo(final NamedCountedSum o) {
            int ret = Double.compare(o.getAverage(),getAverage()) ; // highest average first
            if(ret != 0)
                return ret;
            if(getCount() != o.getCount())
                return getCount() > o.getCount() ? -1 : 1;
            return getName().toString().compareTo(o.getName().toString());
        }
    }

}
