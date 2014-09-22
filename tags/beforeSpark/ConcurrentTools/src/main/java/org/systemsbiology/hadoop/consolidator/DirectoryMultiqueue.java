package org.systemsbiology.hadoop.consolidator;

import java.io.*;
import java.util.*;

/**
 * org.systemsbiology.hadoop.consolidator.DirectoryMultiqueue
 * This code constructs a queue for every file in the directory
 *
 * @author Steve Lewis
 * @date Nov 1, 2010
 */
public class DirectoryMultiqueue extends AbstractDirectoryMultiqueue<String> {
    public static DirectoryMultiqueue[] EMPTY_ARRAY = {};
    public static Class THIS_CLASS = DirectoryMultiqueue.class;

    public static class StringKeyComparator implements Comparator<String>, Serializable {
        @Override
        public int compare(String o1, String o2) {
            return getKey(o1).compareTo(getKey(o2));
        }

        public String getKey(String s) {
            return s.substring(0, s.indexOf("\t"));
        }
    }

    public DirectoryMultiqueue(File dir) {
        super(dir);
    }

    /**
     * return an appropriate FIle based  Queue
     *
     * @param input !null existing file frequently like part-r-00000
     * @return !null queue reading that file
     */
    @Override
    public Queue<String> buildFileQueue(File input) {
        return new TextFileReaderQueue(input);
    }

    /**
     * return an appropriate Comparator for kaye read out of the file
     *
     * @return !null Comparator
     */
    @Override
    public Comparator<String> buildComparator() {
        return new StringKeyComparator();
    }

    public static void main(String[] args) throws IOException {
        String filename = "E:\\ConcurrentTools\\StatisticalWordCount";
        DirectoryMultiqueue mq = new DirectoryMultiqueue(new File(filename));
        File consolidated = new File("Consolidated.txt");

        PrintWriter out = new PrintWriter(new FileWriter(consolidated));
        String s = mq.poll();
        while (s != null) {
            out.println(s);
            s = mq.poll();
        }
        out.close();
    }

}

