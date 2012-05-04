package org.systemsbiology.common;

import java.io.*;
import java.util.*;

/**
 * org.systemsbiology.common.CommonUtilities
 * written by Steve Lewis
 * on Apr 20, 2010
 */
public class ExceptionUtilities {
    public static final ExceptionUtilities[] EMPTY_ARRAY = {};
    public static final Class THIS_CLASS = ExceptionUtilities.class;

    // Do NOT build

    private ExceptionUtilities() {
    }


    /**
     * return the cause of the exception which really caused the problem
     *
     * @param start
     * @return
     */
    public static String[] getFullStack(Throwable start) {
        List<String> holder = new ArrayList<String>();
        appendFullStace(start, holder);
        String[] ret = new String[holder.size()];
        holder.toArray(ret);
        return ret;
    }

    /**
     * return the cause of the exception which really caused the problem
     *
     * @param start
     * @return
     */
    protected static void appendFullStace(Throwable start, List<String> holder) {
        holder.add(start.getMessage());
        final StackTraceElement[] stackTraceElements = start.getStackTrace();
        for (int i = 0; i < stackTraceElements.length; i++) {
            StackTraceElement st = stackTraceElements[i];
            holder.add(st.toString());
        }
         Throwable next = getUltimateCause(start);
        if (next == null || next == start)
            return;
        appendFullStace(next, holder);

    }

    /**
     * return the cause of the exception which really caused the problem
     *
     * @param start
     * @return
     */
    public static void printFullStace(Throwable start) {
        printFullStace(start, System.out);
    }

    /**
     * return the cause of the exception which really caused the problem
     *
     * @param start
     * @return
     */
    public static void printFullStace(Throwable start, Appendable app) {
        try {
            String[] elts = getFullStack(start);
            for (int i = 0; i < elts.length; i++) {
                String elt = elts[i];
                app.append(elt + "\n");
            }
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * return the cause of the exception which really caused the problem
     *
     * @param start
     * @return
     */
    public static Throwable getUltimateCause(Throwable start) {
        final Throwable cause = start.getCause();
        if (cause == null || cause == start)
            return start;
        return getUltimateCause(cause);
    }


    /**
     * return a Runtime Exception caused by start - returning start if it is
     * a RuntimeException
     *
     * @param start non-null Exception
     * @return non-null RuntimeException
     */
    public static RuntimeException getRuntimeException(Throwable start) {
        if (start instanceof RuntimeException)
            return (RuntimeException)start;
        return new RuntimeException(start);
    }


}