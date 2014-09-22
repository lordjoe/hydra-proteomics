package com.lordjoe.utilities;

import java.io.*;
import java.util.*;

/**
 * com.lordjoe.utilities.DirectoryUtilities
 *   Common static methods for dealing with Directories
 * @author Steve Lewis
 * @date 8/26/13
 */
public class DirectoryUtilities {

    public static List<String> commonPaths(File... directories)
    {
         if(directories.length < 2)
            return new ArrayList<String>();   // no point in looking for a single directory
        Set<String> uncommonPaths = new HashSet<String>();
        Set<String> commonPaths = new HashSet<String>();

        //noinspection ForLoopReplaceableByForEach
        for (int i = 0; i < directories.length; i++) {
            File directory = directories[i];
            processPaths(directory,uncommonPaths,commonPaths);
        }


        List<String> ret = new ArrayList<String>(commonPaths);
        Collections.sort(ret);
        return ret;
    }

    protected static void processPaths(File directory, Set<String> uncommonPaths, Set<String> commonPaths) {
        if(!directory.exists() ||!directory.isDirectory())
            throw new IllegalArgumentException("no directory " + directory);

        File[] files = directory.listFiles();
        if(files == null)
            return;
        for (File file : files) {
            processPaths("",file, uncommonPaths, commonPaths);
        }

    }



    protected static void processPaths(@SuppressWarnings("UnusedParameters") String path,File child, Set<String> uncommonPaths, Set<String> commonPaths) {
        String name = child.getName();
        String newPath;

        newPath =   name;
         if(child.isFile()) {
             if(uncommonPaths.contains(newPath))
                 commonPaths.add(newPath);
             else
                 uncommonPaths.add(newPath);
         }
           File[] files = child.listFiles();
        if(files == null)
            return;
        for (File file : files) {
              processPaths(newPath,file, uncommonPaths, commonPaths);
         }


    }


    protected static void usage()
    {
        System.out.println("Usage <directories> ");
       }

    public static void main(String[] args) {
         if(args.length == 0)      {
             usage();
             return;
         }
        File[] directories = new File[args.length];
        for (int i = 0; i < directories.length; i++) {
            directories[i] = new File(args[i]);

        }
        List<String> common =  commonPaths(directories);
        for (String s : common) {
            if(s.endsWith(".java"))
                System.out.println(s);
        }
    }

}
