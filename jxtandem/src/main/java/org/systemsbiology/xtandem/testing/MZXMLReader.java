package org.systemsbiology.xtandem.testing;

import org.systemsbiology.hadoop.*;
import org.systemsbiology.xtandem.*;
import org.systemsbiology.xtandem.hadoop.*;

import java.io.*;

/**
 * org.systemsbiology.xtandem.testing.MZXMLReader
 * basic timing to parse am mzxml file EXACTLY like a mapper
 * User: steven
 * Date: 10/25/11
 */
public class MZXMLReader {
    public static final MZXMLReader[] EMPTY_ARRAY = {};

    private static int gNestedTotalScans;
    private static int gTotalScans;
    private static ElapsedTimer gTimer = new ElapsedTimer();

    private static void processFile(final File pFile) throws IOException {
        String name = pFile.getAbsolutePath();
        XTandemUtilities.outputLine(name);
        InputStream is = XTandemUtilities.getDescribedStream(name);
        processStream(is);

    }

    private static void processStream(final InputStream pIs) throws IOException {
        StringBuilder sb = new StringBuilder();

        LineNumberReader rdr = new LineNumberReader(new InputStreamReader(pIs));
        String line = rdr.readLine();
        int scanLevel = 0;
        while (line != null) {
            if (line.contains("<scan")) {
                scanLevel++;
                break;
            }
            line = rdr.readLine();
        }
        do {

            line = rdr.readLine();
            sb.append(line);
            sb.append("\n");
            if (line.contains("<scan")) {
                gTotalScans++;
                scanLevel++;
            }
            if (line.contains("</scan")) {
                scanLevel--;
                handleScan(sb, scanLevel);
            }
        }
        while (!line.contains("</msRun>"));
    }

    private static void handleLocalFile(final String pArg) throws IOException {
        File indir = new File(pArg);
        if (indir.isDirectory()) {
            File[] files = indir.listFiles();
            for (int i = 0; i < files.length; i++) {
                File file = files[i];
                processFile(file);
            }
        }
        else {
            processFile(indir);

        }
        gTimer.showElapsed("Handled " + gTotalScans + " nested " + gNestedTotalScans);
    }


    private static void handleHDFSFile(final String pArg) throws IOException {
        HDFSAccessor fs = new HDFSAccessor();

        if (fs.isDirectory(pArg)) {
            throw new UnsupportedOperationException("Fix This"); // ToDo
        }
        else {
            String name = pArg;
            XTandemUtilities.outputLine(name);
            InputStream is = fs.openFileForRead(name);
            processStream(is);
        }
        gTimer.showElapsed("Handled " + gTotalScans + " nested " + gNestedTotalScans);
    }


    private static void handleScan(final StringBuilder pSb, final int pScanLevel) {
        String scn = pSb.toString().trim();
        if (scn.startsWith("<scan")) {
            RawPeptideScan scan = XTandemHadoopUtilities.readScan(scn,null);
            gTotalScans++;
        }
        else {
            gNestedTotalScans++;
        }
        pSb.setLength(0);
    }


    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        String arg = args[0];
        if (arg.startsWith("hdfs:/")) {
             handleHDFSFile(arg.substring("hdfs:/".length()));
        }
        else {
            handleLocalFile(arg);
        }
    }

}
