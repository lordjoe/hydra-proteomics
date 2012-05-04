package org.systemsbiology.fasta;

import org.systemsbiology.chromosome.*;

import java.io.*;
import java.net.*;

/**
 * org.systemsbiology.fasta.FASTAUtilities
 * User: steven
 * Date: May 24, 2010
 */
public class FASTAUtilities {
    public static final FASTAUtilities[] EMPTY_ARRAY = {};

    public static final int MAX_LINE_LENGTH = 80;

    public static String buildFasta(String comment, String sequence) {
        StringBuilder sb = new StringBuilder();
        try {
            accumulateLines(0, comment, ">", sb);
            accumulateLines(0, sequence, null, sb);
        }
        catch (IOException e) {
            throw new RuntimeException(e);

        }
        return sb.toString();
    }

    public static void writeFasta(OutputStream sout, String comment, String sequence) {
        PrintWriter out = new PrintWriter(new OutputStreamWriter(sout));
        writeFasta(out, comment, sequence);
    }

    public static void writeFasta(PrintWriter out, String comment, String sequence) {
        try {
            accumulateLines(0, comment, ">", out);
            accumulateLines(0, sequence, null, out);
        }
        catch (IOException e) {
            throw new RuntimeException(e);

        }
    }

    protected static void accumulateLines(int start, String data, String prefix, Appendable sb) throws IOException {
        if (prefix != null)
            sb.append(prefix);
        if (data.length() < start + MAX_LINE_LENGTH) {
            sb.append(data.substring(start));
            sb.append("\n");
        }
        else {
            int end = start + MAX_LINE_LENGTH;
            sb.append(data.substring(start, end));
            sb.append("\n");
            accumulateLines(end, data, prefix, sb);
        }
    }

    public static String getChromosomeSection(IChromosome chr, int start, int end) {
        String url = buildURI(chr, start, end);
        return doGet(url);
    }


    public static final int MAX_SEQUENCE_LENGTH = 10000000;
    protected static String buildURI(IChromosome chr, int start, int end) {
        StringBuilder sb = new StringBuilder();

        if(end <= start)
            throw new IllegalArgumentException("end must be >= start");
        if(start < 0)
            throw new IllegalArgumentException("start must be >= 0 ");
        if(end > chr.getLength())
            throw new IllegalArgumentException("end must be <= chromosome length " + chr.getLength());

        if(end - start > MAX_SEQUENCE_LENGTH)
            throw new IllegalArgumentException("Google app engine has a maximim download length of 10 million not  " + (end - start));


        sb.append(chr);
        sb.append("/");
        sb.append(start);
        sb.append("/");
        sb.append(end);
        sb.append("/");
        sb.append("sequence");

        return sb.toString();

    }


    public static String getGenome(IChromosome chr) {
        Class chrClass = chr.getClass();
        String className = chrClass.getSimpleName();
        if("HumanChromosome".equals(className) )
            return "hg18";
        if("YeastChromosome".equals(className) )
            return "yeast";
        if("MouseChromosome".equals(className) )
            return "mm9";
        throw new IllegalArgumentException("cannot map chromosome " + className); // ToDo change
    }


    public static String doGet(String uri) {
        try {
            URL url = new URL("https://addama-systemsbiology-public.appspot.com/" + uri);
            URLConnection uc = url.openConnection();
            uc.setRequestProperty("x-addama-apikey", "60667408-363a-45a5-b771-42a8e4ecc0a7");
            uc.connect();
            return getContent(uc.getInputStream());
        }
        catch (IOException e) {
            throw new RuntimeException(e);

        }
    }

    public static String getContent(InputStream inputStream) throws IOException {
        StringBuilder builder = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        String line = "";
        while (line != null) {
            line = reader.readLine();
            if (line != null) {
                builder.append(line);
            }
        }
        return builder.toString();
    }

    public static void main(String[] args) {
        
    }

}
