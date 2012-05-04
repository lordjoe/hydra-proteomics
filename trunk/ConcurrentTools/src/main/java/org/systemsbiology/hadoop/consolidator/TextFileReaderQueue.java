package org.systemsbiology.hadoop.consolidator;

import java.io.*;

/**
 * org.systemsbiology.hadoop.consolidator.TextFileReaderQueue
 *  implement a pop only queue whose elements are lines in a text file
 * @author Steve Lewis
 * @date Nov 1, 2010
 */
public class TextFileReaderQueue extends UnimplementedQueue<String>
{
    public static TextFileReaderQueue[] EMPTY_ARRAY = {};
    public static Class THIS_CLASS = TextFileReaderQueue.class;

    private LineNumberReader m_Reader;
    private String m_CurrentLine;

    public TextFileReaderQueue(File inp)
    {
        try {
            m_Reader = new LineNumberReader(new FileReader(inp));
            getNextLine();
        }
        catch (FileNotFoundException e) {
            m_Reader = null;
        }

    }

    protected void getNextLine()
    {
        try {
            m_CurrentLine = m_Reader.readLine();
            if (m_CurrentLine == null)
                m_Reader = null;
        }
        catch (IOException e) {
            m_CurrentLine = null;
            m_Reader = null;
        }
    }

    /**
     * Retrieves and removes the head of this queue,
     * or returns <tt>null</tt> if this queue is empty.
     *
     * @return the head of this queue, or <tt>null</tt> if this queue is empty
     */
    @Override
    public String poll()
    {
        if (m_Reader == null)
            return null;
        String ret = m_CurrentLine;
        getNextLine();
        return ret;
    }

    /**
     * Retrieves, but does not remove, the head of this queue,
     * or returns <tt>null</tt> if this queue is empty.
     *
     * @return the head of this queue, or <tt>null</tt> if this queue is empty
     */
    @Override
    public String peek()
    {
        if (m_Reader == null)
            return null;
        return m_CurrentLine;

    }

    /**
     * Returns <tt>true</tt> if this collection contains no elements.
     *
     * @return <tt>true</tt> if this collection contains no elements
     */
    @Override
    public boolean isEmpty()
    {
        if (m_Reader == null)
            return true;
        if (m_CurrentLine == null)
            return true;
        return false;

    }
}
