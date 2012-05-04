package com.lordjoe.utilities;

import java.io.*;
import java.util.*;

/**
 * com.lordjoe.utilities.FileUnpacker
 *
 * @author Steve Lewis
 * @date Aug 19, 2007
 */
public class FileUnpacker
{
    public static FileUnpacker[] EMPTY_ARRAY = {};
    public static Class THIS_CLASS = FileUnpacker.class;

    private static void unpackDir(File pFile)
    {
        int index = 100;
        String name = pFile.getName();
        String[] fileNames = FileUtilities.getAllFilesWithExtension(pFile, "mp3");
        for (int i = 0; i < fileNames.length; i++) {
            String fileName = fileNames[i];
            File test = new File(fileName);
            if(test.isFile()) {
                File dest = new File(pFile,name + index++ + ".mp3");
                FileUtilities.copyFile(test,dest);
                System.out.println(test);
            }
        }
    }


    private static boolean isUnPackRequired(File pFile)
    {
        String[] strs = pFile.list();
        if(strs == null)
            return false;
        int nSubdirs = 0;
        for (int i = 0; i < strs.length; i++) {
            String str = strs[i];
            File test = new File(pFile,str);
            if(test.isDirectory())   {
                String name = test.getName().toLowerCase();
                if("cd1".equalsIgnoreCase(name))
                     return true;
                if("d01".equalsIgnoreCase(name))
                     return true;
                if(name.contains("disc 01"))
                   return true;
                if(name.contains("cd 01"))
                   return true;
               }
                nSubdirs++;
        }
        return false;
        //return nSubdirs > 1;

    }

    protected static File[] getCandidateFiles(String base)
    {
        File baseDir = new File(base);
        String[] strs = baseDir.list();
        List<File> holder = new ArrayList<File>();
        for (int i = 0; i < strs.length; i++) {
            String str = strs[i];
            File test = new File(baseDir,str);
            if(isUnPackRequired(test))
                holder.add(test);
       }
        File[] ret = new File[holder.size()];
        holder.toArray(ret);
        return ret;

    }

    protected static void usage()
    {
        System.out.println("Usage baseDir");
    }
    
    public static void main(String[] args)
    {
        if(args.length < 1)  {
            usage();
            return;
        }
        File[] files = getCandidateFiles(args[0]);
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            unpackDir(file);
        }
    }

}
