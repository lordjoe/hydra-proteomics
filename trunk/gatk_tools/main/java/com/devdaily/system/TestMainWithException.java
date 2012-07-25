package com.devdaily.system;

/**
 * com.devdaily.system.TestMain
 * Test class with a simple main   that tests the effects of a main throwing an exception
  * User: steven
 * Date: 7/25/12
 */
public class TestMainWithException {
    public static final TestMainWithException[] EMPTY_ARRAY = {};

    public static void main(String[] args) {
        System.out.println("Arguments");
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            System.out.println(arg);

        }
        throw new IllegalStateException("This version is supposed to throw an exception");
    }
}
