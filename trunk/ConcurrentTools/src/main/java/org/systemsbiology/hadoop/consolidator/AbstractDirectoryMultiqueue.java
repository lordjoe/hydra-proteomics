package org.systemsbiology.hadoop.consolidator;

import java.io.*;
import java.util.*;

/**
 * org.systemsbiology.hadoop.consolidator.DirectoryMultiqueue
 *   This code constructs a queue for every file in the directory
 * @author Steve Lewis
 * @date Nov 1, 2010
 */
public abstract class AbstractDirectoryMultiqueue<K> extends MultiQueue<K>
{
    public static AbstractDirectoryMultiqueue[] EMPTY_ARRAY = {};
    public static Class THIS_CLASS = AbstractDirectoryMultiqueue.class;

    public static class StringKeyComparator implements Comparator<String>,Serializable
    {
        @Override
        public int compare(String o1, String o2)
        {
            return getKey(o1).compareTo(getKey(o2));
        }

        public String getKey(String s)
        {
            return s.substring(0, s.indexOf("\t"));
        }
    }

    public AbstractDirectoryMultiqueue(File dir)
    {
        super();
        setComparator(buildComparator());
        for (File f : dir.listFiles()) {
            if (f.isDirectory())
                continue;
            final Queue<K> queue = buildFileQueue(f);
            addQueue(queue);
        }
    }

    /**
     * return an appropriate FIle based  Queue
     * @param input !null existing file frequently like part-r-00000
     * @return !null queue reading that file
     */
    public abstract Queue<K> buildFileQueue(File input);


    /**
     * return an appropriate Comparator for kaye read out of the file
     * @return !null Comparator
     */
    public abstract Comparator<K> buildComparator();


}

