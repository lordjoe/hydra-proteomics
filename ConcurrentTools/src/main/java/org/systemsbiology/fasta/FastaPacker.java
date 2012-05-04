package org.systemsbiology.fasta;

import java.io.*;
import java.util.*;

/**
 * org.systemsbiology.fasta.FastaPacker
 * User: steven
 * Date: Jun 21, 2010
 */
public class FastaPacker {
    public static final FastaPacker[] EMPTY_ARRAY = {};

    /**
     * remap Fasta File as A clooection of bytes
     *
     * @param fastaFile
     * @param packedFile
     */
    public static void packFastaFile(File fastaFile, File packedFile) {
        try {
            LineNumberReader reader = new LineNumberReader(new FileReader(fastaFile));
            FileOutputStream fout = new FileOutputStream(packedFile);
            BufferedOutputStream out = new BufferedOutputStream(fout);
            String line = reader.readLine();
            while (line != null) {
                if (!line.startsWith(">")) {
                    byte[] bytes = new byte[line.length()];
                    for (int i = 0; i < bytes.length; i++) {
                        bytes[i] = (byte) line.charAt(i);
                    }
                    out.write(bytes);
                }
                line = reader.readLine();
            }
            reader.close();
            out.flush();
            out.close();
        }
        catch (IOException e) {
            throw new RuntimeException(e);

        }
    }

    /**
     * remap Fasta File as A clooection of bytes
     *
     * @param fastaFile
     * @param packedFile
     */
    public static void packFastaDirectory(File fastaDirectory, File packedFileDirectory) {
        packedFileDirectory.mkdirs();
        File[] files = fastaDirectory.listFiles();
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            String name = file.getName();
            if (!name.endsWith(".fa"))
                continue;
            File outFile = new File(packedFileDirectory, name.replace(".fa", ".fabytes"));
            packFastaFile(file, outFile);
            System.out.println("Packed file " + name);
        }
    }


    /**
     * read bytes out of a Fasta file
     *
     * @param fastaFile
     * @param packedFile
     */


    /**
     * read bytes out of a Fasta file
     *
     * @param fastaFile
     * @param start
     * @param end
     * @return
     */
    public static String getFastaData(File fastaFile, int start, int end) {
        try {
            LineNumberReader reader = new LineNumberReader(new FileReader(fastaFile));
            String line = reader.readLine();
            int bytesRead = 0;
            StringBuilder sb = new StringBuilder();

            while (line != null) {
                if (!line.startsWith(">")) {
                    int l = line.length();
                    if (start > bytesRead + 1) {
                        bytesRead += l;
                        continue;
                    }
                    for (int i = 0; i < l; i++) {
                        if (bytesRead > end) {
                            reader.close();
                            return sb.toString();
                        }
                        if (bytesRead++ >= start)
                            sb.append(line.charAt(i));
                    }
                }
                line = reader.readLine();
            }
            reader.close();
            return sb.toString();
        }
        catch (IOException e) {
            throw new RuntimeException(e);

        }
    }


    /**
     * read bytes out of a Fasta file
     *
     * @param fastaFile
     * @param start
     * @param end
     * @return
     */
    public static int countFastaProteins(File fastaFile) {
        List<Long> millions = new ArrayList<Long>();
        try {
            int nProteins = 0;
            LineNumberReader reader = new LineNumberReader(new FileReader(fastaFile));
            long lastPos = 0;
            String line = reader.readLine();
            int bytesRead = 0;
            StringBuilder sb = new StringBuilder();
            while (line != null) {
                if (line.startsWith(">")) {
                    nProteins++;
                    if (nProteins % 10000 == 0)
                        System.out.print(".");
                    if (nProteins % 1000000 == 0) {
                        millions.add(lastPos);
                        System.out.println(".");
                    }
                }
                lastPos += line.length() + 1;
                line = reader.readLine();
            }
            reader.close();
            for (long l : millions)
                System.out.println("" + l);
            return nProteins;
        }
        catch (IOException e) {
            throw new RuntimeException(e);

        }
    }


    /**
     * read bytes out of a Fasta file
     *
     * @param fastaFile
     * @param start
     * @param end
     * @return
     */
    public static String getPackedFastaData(File fastaFile, int start, int end) {
        try {
            end = Math.min(end, (int) fastaFile.length());
            FileInputStream inp = new FileInputStream(fastaFile);
            inp.skip(start);
            byte[] buffer = new byte[4096];
            StringBuilder sb = new StringBuilder();

            int nread = inp.read(buffer);
            while (nread > 0) {
                for (int i = 0; i < nread; i++) {
                    sb.append((char) buffer[i]);

                }
                nread = inp.read(buffer);
            }
            inp.close();
            return sb.toString();
        }
        catch (IOException e) {
            throw new RuntimeException(e);

        }
    }

    /**
      * read bytes out of a Fasta file
      *
      * @param fastaFile
      * @param start
      * @param end
      * @return
      */
     public static int countFastaProteins(File fastaFile,long pos) {
           try {
             int nProteins = 0;
             LineNumberReader reader = new LineNumberReader(new FileReader(fastaFile));
             reader.skip(pos);
             String line = reader.readLine();
             StringBuilder sb = new StringBuilder();
             while (line != null) {
                 if (line.startsWith(">")) {
                     nProteins++;
                     if (nProteins % 10000 == 0)
                         System.out.print(".");
                     if (nProteins % 1000000 == 0) {
                         return nProteins;
                     }
                 }
                 line = reader.readLine();
             }
             reader.close();
              return nProteins;
         }
         catch (IOException e) {
             throw new RuntimeException(e);

         }
     }


    public static long[] POSITIONS = {
            0,
            841673486L,
            1509437181L,
            2001391685L,
            2551958198L,
            3110879735L,
            3665078640L,
            4230541107L,
            4763740592L,
            5324931371L,
            5864776792L,
            6397136652L,
            6942467748L,
            7465152015L,
            7981571000L,
            8484347520L,
            9000141063L,
    };

    public static void main(String[] args) {

        String source = "I:/ForSteven/giantFasta/nr.fasta";
        for (int i = 0; i < POSITIONS.length; i++) {
            int number = countFastaProteins(new File(source),POSITIONS[i]);
            System.out.println("\nfount " + number);

        }
        int number = countFastaProteins(new File(source));
        System.out.println("fount " + number);
//        source = "H:\\Madhu\\mouse_motifscanning";
//        String dest = "E:/PackedGenome/mm9";
//        if(args.length > 0)
//            source = args[0];
//        if(args.length > 1)
//            dest = args[1];
//
//        packFastaDirectory(new File(source), new File(dest));

    }


}
