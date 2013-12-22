package com.lordjoe.utilities;

import javax.annotation.*;
import java.io.*;

/**
 * com.lordjoe.utilities.PictureRenamer
 * User: Steve
 * Date: 12/17/13
 */
public class PictureRenamer implements TypedVisitor<File>
{

    public static final PictureRenamer INSTANCE = new PictureRenamer();
    private PictureRenamer() {
    }

    /**
     * @param pFile interface implemented by the visitor pattern
     */
    @Override
    public void visit(@Nonnull final File pFile) {
        if(pFile.isDirectory())
            return;
        String name = pFile.getName();
        if(!name.endsWith(".JPG"))
            return;
        if(!name.startsWith("P10"))
             return;
        File parent = pFile.getParentFile();
        if(parent == null)
            return;
        String parentName = parent.getName();
        String lcPn = parentName.toLowerCase();
        if(lcPn.contains("selected"))
            return;
        if(lcPn.endsWith("_pana"))
            return;
        if(lcPn.endsWith("shared"))
             return;
         String newName = parentName + name.substring(4) ;
        pFile.renameTo(new File(parent,newName)) ;
    }


    private static void usage()
    {
        System.out.println("usage dir1 dir2 ...");
    }
    public static void main(String[] args) {
        if(args.length == 0)    {
            usage();
            return;
        }
        for (int i = 0; i < args.length; i++) {
            File f = new File(args[i]) ;
            if(f.exists())   {
                DirectoryVisitor dv = new DirectoryVisitor(f);
                dv.process(INSTANCE);
            }
            else {
                System.out.println("File " + f.getAbsolutePath() + " does not exist");
            }


        }
    }
}
