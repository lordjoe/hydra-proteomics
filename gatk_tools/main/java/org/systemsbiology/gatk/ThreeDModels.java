package org.systemsbiology.gatk;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 * org.systemsbiology.gatk.ThreeDModels
 * User: steven
 * Date: 6/20/12
 */
public class ThreeDModels {
    public static final ThreeDModels[] EMPTY_ARRAY = {};

    public static final String PDB_DOWNLOAD_URL_START = "http://www.pdb.org/pdb/download/downloadFile.do?fileFormat=pdb&compression=NO&structureId=";

    public static String[] readModelXml(File models) {
        try {
            String[] ids = GeneUtilities.readInLines(models);
            PrintWriter out = new PrintWriter(new FileWriter("PdbModels.txt"));
            List<String> holder = new ArrayList<String>();
            for (int i = 1; i < ids.length; i++) {
                String id = ids[i];
                String[] items = id.split("\t");
                String xml = readPDBXML(items[1]);
                if (xml != null) {
                    holder.add(xml);
                    System.out.println("found " + (holder.size()) + " in " + i);
                    out.println(xml);
                    out.println("<EndModel/>\n");
                  }

            }

            out.close();
            String[] ret = new String[holder.size()];
            holder.toArray(ret);
            return ret;
        }
        catch (IOException e) {
            throw new RuntimeException(e);

        }

    }

    private static String readPDBXML(String item) {
        try {
            String urlStr = "http://www.pdb.org/pdb/rest/das/pdb_uniprot_mapping/alignment?query=" + item;
            URL url = new URL(urlStr);
            String[] lines = GeneUtilities.readInLines(url);
            if (lines.length == 0)
                return null;
            if (lines[0].contains("Bad reference (403)"))
                return null;
            if (!lines[0].startsWith("<?xml"))
                return null;
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < lines.length; i++) {
                String line = lines[i];
                sb.append(line + "\n");
            }
            String s = sb.toString();
            return s;
        }
        catch (MalformedURLException e) {
            throw new RuntimeException(e);

        }
    }

    public static void main(String[] args) {
        File models = new File(args[0]);
        if (!models.exists())
            throw new IllegalArgumentException("model file " + args[0] + " does not exxist");
        String[] xmls = readModelXml(models);
        for (int i = 0; i < xmls.length; i++) {
            String xml = xmls[i];

        }
    }

}
