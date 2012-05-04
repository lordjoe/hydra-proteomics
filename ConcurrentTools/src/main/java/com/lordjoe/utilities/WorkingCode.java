package com.lordjoe.utilities;

import java.util.*;

/**
 * com.lordjoe.utilities.WorkingCode
 * whenever I want a little code to work with
 * User: steven
 * Date: 4/11/12
 */
public class WorkingCode {
    public static final WorkingCode[] EMPTY_ARRAY = {};


    public static void makeGrp(String fileName, int groupSize, String outFileName) {
        String[] lines = FileUtilities.readInLines(fileName);
        List<String> holder = new ArrayList<String>();
        int index = 0;
        int setindex = 1;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i].trim() ;
            if(line.startsWith("#"))
                continue;

            if (index == 0) {
                String setName = "Gene Set" + setindex++;
                sb.append(setName + "\t" + setName);
            }
            sb.append("\t" + line.trim()  );
            if (index++ > groupSize) {
                index = 0;
                String e = sb.toString();
                holder.add(e);
                sb.setLength(0);
            }
        }
        if (sb.length() > 0) {
             String e = sb.toString();
            holder.add(e);
         }

        String[] ret = new String[holder.size()];
        holder.toArray(ret);
        FileUtilities.writeFileLines(outFileName, ret);
    }

    public static void makeOneGrp(String fileName,  String outFileName) {
        String[] lines = FileUtilities.readInLines(fileName);
        List<String> holder = new ArrayList<String>();
        int index = 0;
          String grpName = fileName.substring(0,fileName.length() - 4); // grop extension
        StringBuilder sb = new StringBuilder();
        String setName = grpName;
        sb.append(setName + "\t" + setName);
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i].trim() ;
            if(line.startsWith("#"))
                continue;

               sb.append("\t" + line.trim()  );
          }
        if (sb.length() > 0) {
             String e = sb.toString();
            holder.add(e);
         }

        String[] ret = new String[holder.size()];
        holder.toArray(ret);
        FileUtilities.writeFileLines(outFileName, ret);
    }

    public static void main(String[] args) {
        makeOneGrp(args[0],  "LiverGenes.gmt");
    //    makeGrp(args[0], 6, "LiverGenes.gmt");
     }
}
