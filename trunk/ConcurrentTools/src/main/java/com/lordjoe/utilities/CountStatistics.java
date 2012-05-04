package com.lordjoe.utilities;

import org.systemsbiology.common.*;

import java.io.*;
import java.util.*;

/**
 * com.lordjoe.utilities.CountStatistics
 * an object to count members at integer values
 * User: Steve
 * Date: Apr 11, 2011
 */
public class CountStatistics {
    public static final CountStatistics[] EMPTY_ARRAY = {};

    private final int[] m_HeldAsArray;
    private int m_OtherValues;
    private int m_TotalMembers;
    private final Map<Integer, Integer> m_HeldAsMap;

    /**
     * this version only holds the array values
     *
     * @param maxHeldAsArray
     */
    public CountStatistics(int maxHeldAsArray) {
        this(maxHeldAsArray, false);
    }

    /**
     * this version can hold other values as a map
     *
     * @param maxHeldAsArray  - mac to hold in array
     * @param holdOthersAsMap if true others are held in a map
     */
    public CountStatistics(int maxHeldAsArray, boolean holdOthersAsMap) {
        m_HeldAsArray = new int[maxHeldAsArray];
        if (holdOthersAsMap)
            m_HeldAsMap = new HashMap<Integer, Integer>();
        else
            m_HeldAsMap = null;
    }

    public void show(Appendable adder, int binWidth) {
        try {
            adder.append("Max value " + getMaxCount() + "\n");
            adder.append("Max bin " + getMaxBin(binWidth) + "\n");
            for (int i = 0; i < m_HeldAsArray.length; i += binWidth) {
                int holds = getBinValue(i,binWidth);
                if(holds > 0)
                     adder.append(Integer.toString(i) + ":" +  holds + "\n");

            }
        }
        catch (IOException e) {
            throw new RuntimeException(e);

        }
    }

    public int getMaxCount() {

        int max = Integer.MIN_VALUE;
        for (int i = 0; i < m_HeldAsArray.length; i++) {
            int test = m_HeldAsArray[i];
            if (test > max)
                max = test;

        }
        return max;
    }


    public int getBinValue(int start, int width) {

        int testsum = 0;
        for (int j = 0; j < width; j++) {
            testsum += m_HeldAsArray[start + j];

        }

        return testsum;
    }


    public int getMaxBin(int width) {

        int max = Integer.MIN_VALUE;
        for (int i = 0; i < m_HeldAsArray.length - width; i++) {
            int testsum = getBinValue(i,   width) ;
              if (testsum > max)
                max = testsum;

        }
        return max;
    }

    /**
     * add an item at that value
     *
     * @param value positive integer value
     */
    public synchronized void addItem(int value) {
        m_TotalMembers++;
        if(m_HeldAsArray.length == 5000 && value == 147)
             CommonUtilities.breakHere();

        if (value < m_HeldAsArray.length) {
            m_HeldAsArray[value]++;
        }
        else {
            if (m_HeldAsMap == null) {
                m_OtherValues++;
                if(m_HeldAsArray.length == 5000 )
                     CommonUtilities.breakHere();
            }
            else {
                if (m_HeldAsMap.containsKey(value)) {
                    int current = m_HeldAsMap.get(value) + 1;
                    m_HeldAsMap.put(value, current);
                }
                else {
                    m_HeldAsMap.put(value, 1);

                }
            }
        }
    }

    public int getTotalMembers() {
        return m_TotalMembers;
    }

    public synchronized int getCountAtValue(int value) {
        if (value < m_HeldAsArray.length) {
            return m_HeldAsArray[value];
        }
        else {
            if (m_HeldAsMap == null) {
                throw new IllegalArgumentException("value exceeds bins");
            }
            else {
                if (m_HeldAsMap.containsKey(value)) {
                    return m_HeldAsMap.get(value);
                }
                else {
                    return 0;

                }
            }
        }

    }

    public boolean isLargeCountKept() {
        return m_HeldAsMap != null;
    }

    public int getMaxArrayCounts() {
        return m_HeldAsArray.length;
    }

    /**
     * get the values in the array - these may be all values or bot
     *
     * @return
     */
    public int[] getValuesAsArray() {
        int[] ret = new int[getMaxArrayCounts()];
        System.arraycopy(m_HeldAsArray, 0, ret, 0, getMaxArrayCounts());
        return ret;
    }

    public int getOtherValues() {
        return m_OtherValues;
    }
}
