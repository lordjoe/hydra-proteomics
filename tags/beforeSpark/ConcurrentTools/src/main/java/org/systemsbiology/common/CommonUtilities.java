package org.systemsbiology.common;

import java.io.*;
import java.util.*;

/**
 * org.systemsbiology.common.CommonUtilities
 * written by Steve Lewis
 * on Apr 20, 2010
 */
public class CommonUtilities {
    public static final CommonUtilities[] EMPTY_ARRAY = {};
    public static final Class THIS_CLASS = CommonUtilities.class;

    public static final long[] POWERS_OF_2 = {
            1L,
            2L,
            2L * 2L,
            2L * 2L * 2L,
            2L * 2L * 2L * 2L,     // 4
            2L * 2L * 2L * 2L * 2L,
            2L * 2L * 2L * 2L * 2L * 2L,
            2L * 2L * 2L * 2L * 2L * 2L * 2L,
            2L * 2L * 2L * 2L * 2L * 2L * 2L * 2L,  // 8
            256L * 2L,
            256L * 2L * 2L,
            256L * 2L * 2L * 2L,
            256L * 2L * 2L * 2L * 2L,     // 12
            256L * 2L * 2L * 2L * 2L * 2L,
            256L * 2L * 2L * 2L * 2L * 2L * 2L,
            256L * 2L * 2L * 2L * 2L * 2L * 2L * 2L,
            256L * 2L * 2L * 2L * 2L * 2L * 2L * 2L * 2L,  // 16

             256L * 256L * 2L,
            256L * 256L * 2L * 2L,
            256L * 256L * 2L * 2L * 2L,
            256L * 256L * 2L * 2L * 2L * 2L,     // 20
            256L * 256L * 2L * 2L * 2L * 2L * 2L,
            256L * 256L * 2L * 2L * 2L * 2L * 2L * 2L,
            256L * 256L * 2L * 2L * 2L * 2L * 2L * 2L * 2L,
            256L * 256L * 2L * 2L * 2L * 2L * 2L * 2L * 2L * 2L,  // 24

            256L * 256L * 256 * 2L,
            256L * 256L * 256 * 2L * 2L,
            256L * 256L * 256 * 2L * 2L * 2L,
            256L * 256L * 256 * 2L * 2L * 2L * 2L,     // 28
            256L * 256L * 256 * 2L * 2L * 2L * 2L * 2L,
            256L * 256L * 256 * 2L * 2L * 2L * 2L * 2L * 2L,
            256L * 256L * 256 * 2L * 2L * 2L * 2L * 2L * 2L * 2L,
            256L * 256L * 256 * 2L * 2L * 2L * 2L * 2L * 2L * 2L * 2L,  // 32

             256L * 256L * 256 * 256L * 2L,
            256L * 256L * 256 * 256L * 2L * 2L,
            256L * 256L * 256 * 256L * 2L * 2L * 2L,
            256L * 256L * 256 * 256L * 2L * 2L * 2L * 2L,     // 36
            256L * 256L * 256 * 256L * 2L * 2L * 2L * 2L * 2L,
            256L * 256L * 256 * 256L * 2L * 2L * 2L * 2L * 2L * 2L,
            256L * 256L * 256 * 256L * 2L * 2L * 2L * 2L * 2L * 2L * 2L,
            256L * 256L * 256 * 256L * 2L * 2L * 2L * 2L * 2L * 2L * 2L * 2L,  // 40
            256L * 256L * 256 * 256L * 256L * 2L,
            256L * 256L * 256 * 256L * 256L * 2L * 2L,
            256L * 256L * 256 * 256L * 256L * 2L * 2L * 2L,
            256L * 256L * 256 * 256L * 256L * 2L * 2L * 2L * 2L,     // 44
            256L * 256L * 256 * 256L * 256L * 2L * 2L * 2L * 2L * 2L,
            256L * 256L * 256 * 256L * 256L * 2L * 2L * 2L * 2L * 2L * 2L,
            256L * 256L * 256 * 256L * 256L * 2L * 2L * 2L * 2L * 2L * 2L * 2L,
            256L * 256L * 256 * 256L * 256L * 2L * 2L * 2L * 2L * 2L * 2L * 2L * 2L,  // 48
            256L * 256L * 256 * 256L * 256L * 256L * 2L,
            256L * 256L * 256 * 256L * 256L * 256L * 2L * 2L,
            256L * 256L * 256 * 256L * 256L * 256L * 2L * 2L * 2L,
            256L * 256L * 256 * 256L * 256L * 256L * 2L * 2L * 2L * 2L,     // 52
            256L * 256L * 256 * 256L * 256L * 256L * 2L * 2L * 2L * 2L * 2L,
            256L * 256L * 256 * 256L * 256L * 256L * 2L * 2L * 2L * 2L * 2L * 2L,
            256L * 256L * 256 * 256L * 256L * 256L * 2L * 2L * 2L * 2L * 2L * 2L * 2L,
            256L * 256L * 256 * 256L * 256L * 256L * 2L * 2L * 2L * 2L * 2L * 2L * 2L * 2L,  // 56
            256L * 256L * 256 * 256L * 256L * 256L * 256 * 2L,
            256L * 256L * 256 * 256L * 256L * 256L * 256 * 2L * 2L,
            256L * 256L * 256 * 256L * 256L * 256L * 256 * 2L * 2L * 2L,
            256L * 256L * 256 * 256L * 256L * 256L * 256 * 2L * 2L * 2L * 2L,     // 60
            256L * 256L * 256 * 256L * 256L * 256L * 256 * 2L * 2L * 2L * 2L * 2L,
            256L * 256L * 256 * 256L * 256L * 256L * 256 * 2L * 2L * 2L * 2L * 2L * 2L,

    };
    // Do NOT build

    private CommonUtilities() {
    }

    public static File getUserDirectory()
    {
         return new File(getUserDir());
    }


    public static String getUserDir()
    {
         return System.getProperty("user.dir");
    }

    public static long shiftBy(long n, int shift) {
        return POWERS_OF_2[shift] * n;
    }


    public static final String TEMP_STRING = "___XXX___";
    public static final String TEXT_FOR_STAR = "___STAR___";

    /**
     * get all files matches a wild card like /foo/bar/yeast0*.sam.gz
     *
     * @param pFileSpec
     * @return
     */
    public static File[] getWildCardFiles(String pFileSpec) {
        List<File> holder = new ArrayList<File>();
        // Sometimes it is hard to pass *
        pFileSpec = pFileSpec.replace(TEXT_FOR_STAR, "*");


        if (pFileSpec.contains("*")) {
            File bar = new File(pFileSpec.replace("*", TEMP_STRING));
            File dir = bar.getParentFile();
            String spec = bar.getName();
            spec = spec.replace(".", "\\.");    // *)&)(^&(&^(*& regex
            spec = spec.replace(TEMP_STRING, ".*");
            String[] items = dir.list();
            for (int i = 0; i < items.length; i++) {
                String item = items[i];
                if (item.startsWith("."))
                    continue;
                if (item.matches(spec))
                    holder.add(new File(dir, item));

            }

        }
        else {
            holder.add(new File(pFileSpec));
        }
        File[] ret = new File[holder.size()];
        holder.toArray(ret);
        return ret;
    }

    /**
     * open a file output stream without throwing a *%^&$%&$%% checked exception
     *
     * @param f non-null file
     * @return the stream
     */
    public static FileOutputStream openFile(File f) {
        try {
            return new FileOutputStream(f);
        }
        catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static void breakHere() {
        // todo You want to stop
    }

    public static int compare(int i1, int i2) {
        if (i1 == i2)
            return 0;
        return i1 < i2 ? -1 : 1;
    }

    public static int compare(short i1, short i2) {
        if (i1 == i2)
            return 0;
        return i1 < i2 ? -1 : 1;
    }

    public static int compare(long i1, long i2) {
        if (i1 == i2)
            return 0;
        return i1 < i2 ? -1 : 1;
    }

    public static int compare(double i1, double i2) {
        if (i1 == i2)
            return 0;
        return i1 < i2 ? -1 : 1;
    }

    public static int compare(float i1, float i2) {
        if (i1 == i2)
            return 0;
        return i1 < i2 ? -1 : 1;
    }

    public static int compare(byte i1, byte i2) {
        if (i1 == i2)
            return 0;
        return i1 < i2 ? -1 : 1;
    }


    public static double getMedian(double[] values) {
        int len = values.length;
        if (len == 0)
            return 0;
        int len2 = len / 2;
        Arrays.sort(values);
        if (len % 2 == 1)
            return values[len2];
        else
            return values[len2 - 1] + values[len2];

    }

    public static final double[][] RATIOS =
            {
                    {},
                    {1.0 / 2},
                    {1.0 / 3, 2.0 / 3},
                    {1.0 / 4, 2.0 / 4, 3.0 / 4},
                    {1.0 / 5, 2.0 / 5, 3.0 / 5, 4.0 / 5},
                    {1.0 / 6, 2.0 / 6, 3.0 / 6, 4.0 / 6, 5.0 / 6},
                    {1.0 / 7, 2.0 / 7, 3.0 / 7, 4.0 / 7, 5.0 / 7},
            };

    /**
     * return the curs for ntimes
     *
     * @param n ntile - 1
     * @return the cuts
     */
    protected static double[] getRatios(int n) {
        if (n < RATIOS.length)
            return RATIOS[n];
        double[] ret = new double[n];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = i / (double) n;
        }
        return ret;
    }

    /**
     * break an array of
     *
     * @param values
     * @param n      number of times - return n - 1 values
     * @return
     */
    public static double[] getNTile(double[] values, int n) {
        int len = values.length;
        if (len == 0)
            throw new IllegalArgumentException("Cannot have ntile for arrays of length 0");
        //  Arrays.sort(values);
        double[] ratios = getRatios(n - 1);
        double[] ret = new double[n - 1];
        for (int i = 0; i < ret.length; i++) {
            double nr = len * ratios[i];
            int index1 = (int) nr;
            double remainder = nr - index1;
            if (remainder == 0 || index1 >= values.length - 1) {
                ret[i] = values[index1];
            }
            else {
                double v1 = values[index1] * (1.0 - remainder);
                double v2 = values[index1 + 1] * remainder;
                ret[i] = v1 + v2;
            }

        }
        return ret;

    }

}
