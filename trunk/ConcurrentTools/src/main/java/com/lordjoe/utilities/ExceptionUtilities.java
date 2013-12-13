package com.lordjoe.utilities;

import java.io.*;

/**
 * com.lordjoe.utilities.ExceptionUtilities
 * User: Steve
 * Date: 12/13/13
 */
public class ExceptionUtilities {

    public static void printAllStacks(Throwable t) {
        printAllStacks(t, System.out);
    }


    public static void printAllStacks(Throwable t, Appendable out) {

        try {
            StackTraceElement[] stackTrace = t.getStackTrace();
            for (int i = 0; i < stackTrace.length; i++) {
                StackTraceElement trace = stackTrace[i];
                out.append("\tat " + trace + "\n");

            }

            Throwable next = t.getCause();
            if(next != null && next != t)
                printAllStacks(next,   out) ;
        }
        catch (IOException e) {
            throw new RuntimeException(e);

        }
    }

}
