package com.lordjoe.algorithms;

import com.google.common.io.*;

import java.io.*;
import java.lang.reflect.*;
import java.util.*;

/**
 * com.lordjoe.algorithms.Automation
 * Runs a given class' main multiple times with different argument lines
 * User: Steve
 * Date: 2/6/14
 */
public class Automation {


    public static void usage() {
        System.out.println("Usage <full main class> <command lines.txt>");
    }


    private static void runMain(final Method pMain, final String pLine) throws Exception {
        if(pLine.startsWith("#"))
            return; // ignore line starting with # - allowing comments and turing runs on and off
        String[] args = parseArgsLine(pLine);
        Object[] argSet = { args };
        System.out.println("== run with " + pLine);
        pMain.invoke(null,argSet);
    }

    private static String[] parseArgsLine(final String pLine) {
        if(pLine.contains("\""))
            throw new UnsupportedOperationException("Add code to handle quoted arguments"); // ToDo

        String[] split = pLine.split(" ") ;
        List<String> holder = new ArrayList<String>();

        for (String s : split) {
            s = s.trim();
            if(s.length() > 0)
                holder.add(s);
        }
        String[] ret = new String[holder.size()];
        holder.toArray(ret);
        return ret;
    }


    public static final Class[] MAIN_ARGS = { String[].class };


    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            usage();
            return;
        }

        Class mainClass = Class.forName(args[0]) ;
        Method main = mainClass.getDeclaredMethod("main",MAIN_ARGS);

        LineReader rdr = new LineReader(new FileReader(args[1]));
        String line = rdr.readLine();
        while(line != null)    {
            runMain(main,line);
            line = rdr.readLine();
        }


    }

}
