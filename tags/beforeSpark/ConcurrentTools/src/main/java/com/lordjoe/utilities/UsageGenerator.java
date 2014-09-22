package com.lordjoe.utilities;

/**
 * com.lordjoe.utilities.UsageGenerator
 * User: steven
 * Date: 11/6/12
 */
public class UsageGenerator {
    public static final UsageGenerator[] EMPTY_ARRAY = {};


    public static void showUsage(String sample,  String... argdescription)
    {
        String ustring = "usage  " + sample;
        System.out.println(ustring);
        for (int i = 0; i < argdescription.length; i++) {
            String s = argdescription[i];
            System.out.println(s);
         }
        System.out.println(ustring);
    }

}
