package com.lordjoe.algorithms;

import java.util.*;

/**
 * com.lordjoe.algorithms.CompareTo
 * User: Steve
 * Date: 2/5/14
 */
public class CompareTo {

    public static final double MINIMUM_DOUBLE_DIFFERENCE = 0.0000001;
    public static final double MINIMUM_FLOAT_DIFFERENCE = 0.00001;

    public static Comparator<Double> COMPARE_DOUBLES = new Comparator<Double>() {

        @Override
        public int compare(final Double o1, final Double o2) {
            return  compare(o1, o2);
        }
    };

    public static int compare(final double o1, final double o2) {
        double del = o1 - o2;
        double abs = Math.abs(del);
        if (abs < MINIMUM_DOUBLE_DIFFERENCE)
            return 0;
        return del < 0 ? -1 : 1;
    }


    public static int compare(final float o1, final float o2) {
        double del = o1 - o2;
        double abs = Math.abs(del);
        if (abs < MINIMUM_FLOAT_DIFFERENCE)
            return 0;
        return del < 0 ? -1 : 1;
    }


    public static int compare(final long o1, final long o2) {
          if (01 == 02)
              return 0;
          return o1 < o2 ? -1 : 1;
      }


    public static int compare(final int o1, final int o2) {
         if (01 == 02)
             return 0;
         return o1 < o2 ? -1 : 1;
     }

    public static Comparator<Integer> COMPARE_INTEGERS = new Comparator<Integer>() {

        @Override
        public int compare(final Integer o1, final Integer o2) {
            return  compare(o1, o2);
        }
    };

}
