package org.systemsbiology.fasta;

import java.io.*;
import java.util.*;
import java.util.zip.*;

/**
 * org.systemsbiology.fasta.FastaStreamReader
 * A class to inerate through Fasta files and find entries
 * User: Steve
 * Date: 1/30/12
 */
public class FastaStreamReader {
    public static final FastaStreamReader[] EMPTY_ARRAY = {};

    public static FastaEntry[] getEntriesContaining(File inFile, String[] values, boolean unique) {
        return getEntriesContaining(inFile, new HashSet<String>(Arrays.asList(values)), unique);
    }

    public static FastaEntry[] getEntriesContaining(File inFile, Set<String> values, boolean unique) {
        InputStream is = buildInputStream(inFile);
        Iterator<FastaEntry> iter = new FastaStreamIterator(is);
        Set<FastaEntry> holder = new HashSet<FastaEntry>();
        long numberTested = 0;
        // if unique remove after found
        Set<String> removed = new HashSet<String>();
        while (iter.hasNext()) {
            FastaEntry test = iter.next();
            // show progress
            if(++numberTested %1000 == 0)   {
                System.out.print(".");
               if( numberTested % 50000 == 0)   {
                   System.out.println();
               }
            }

            for (String s : values) {
                if (test.getValue().contains(s)) {
                    holder.add(test);
                    if (unique) {
                        removed.add(s);
                    }
                }
            }
            // got one for each value and we are unique - return
            if (!removed.isEmpty()) {
                values.removeAll(removed);
                removed.clear();
                if (values.isEmpty())
                    break;
            }
        }
        FastaEntry[] ret = new FastaEntry[holder.size()];
        holder.toArray(ret);
        return ret;
    }

    public static InputStream buildInputStream(final File pInFile) {
        try {
            FileInputStream in = new FileInputStream(pInFile);
            if (pInFile.getName().endsWith(".gz"))
                return new GZIPInputStream(in);
            if (pInFile.getName().endsWith(".zip"))
                return new ZipInputStream(in);
            return in;
        }
        catch (IOException e) {
            throw new RuntimeException(e);

        }
    }


    public static final String[] SEARCHED_VALUES =
            {
                    "ESSRDKDNG",
                    "EPSASAHR",
                    "EPSYV",
                    "ESGPR",
                    "ESASSG",
                    "CLCGAPGCK",
                    "EGGGGR",
                    "QDGKDGGAS",
                    "CPQAGM",
                    "CGGGTC",
                    "CAQGGHS",
                    "QASSGSS",

            };

    public static void main(String[] args) throws Exception
    {
        if(args.length < 2)   {
            System.out.println("usage <fastaFile> <output file>");
            return;
        }
        File inp = new File(args[0]);
        File out = new File(args[1]);

        FastaEntry[] entries = getEntriesContaining(inp,SEARCHED_VALUES,true);
        PrintWriter outW = new PrintWriter(out);
        for (int i = 0; i < entries.length; i++) {
            FastaEntry entry = entries[i];
            outW.println(entry);
        }
        outW.close();
     }
}


