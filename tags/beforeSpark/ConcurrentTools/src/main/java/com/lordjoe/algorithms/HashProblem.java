package com.lordjoe.algorithms;

import java.util.*;

/**
 * com.lordjoe.algorithms.HashProblem
 * User: Steve
 * Date: 4/18/12
 */
public class HashProblem {
    public static final HashProblem[] EMPTY_ARRAY = {};

    public static final Integer[] TEST_INTS = {231552, 234756, 596873, 648219, 726312, 981237, 988331, 1277361, 1283379};

    private final Set<Integer> m_Values = new HashSet<Integer>();

    public boolean[] isSumPresent() {
        boolean[] ret = new boolean[TEST_INTS.length];
        for (Integer test : m_Values) {
            for (int i = 0; i < TEST_INTS.length; i++) {
                if (ret[i])
                    continue;   // already found
                int restOfSum = (int) TEST_INTS[i] - test;
                if (m_Values.contains(restOfSum)) {
                    ret[i] = true;
                }
            }
        }
        return ret;
    }

    public void loadValues(int[] inp) {
        for (int i = 0; i < inp.length; i++) {
            m_Values.add(inp[i]);

        }
    }


    public static void main(String[] args) {
        HashProblem hp = new HashProblem();
        int[] test = Inversions.readArray("HashInt.txt");
        hp.loadValues(test);
        boolean[] soln = hp.isSumPresent();
        for (int i = 0; i < soln.length; i++) {
            boolean b = soln[i];
            System.out.print(b ? 1 : 0);
        }
        System.out.println();
    }
}
