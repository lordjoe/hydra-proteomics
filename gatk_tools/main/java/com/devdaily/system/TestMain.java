package com.devdaily.system;

/**
 * com.devdaily.system.TestMain
 * Test class with a simple main
 * User: steven
 * Date: 7/25/12
 */
public class TestMain {
    public static final TestMain[] EMPTY_ARRAY = {};

    public static void main(String[] args) {
        System.out.println("Arguments");
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            System.out.println(arg);

        }
    }

}
