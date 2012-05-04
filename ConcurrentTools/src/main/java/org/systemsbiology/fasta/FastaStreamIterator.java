package org.systemsbiology.fasta;

import java.io.*;
import java.util.*;

/**
* org.systemsbiology.fasta.FastaStreamIterator
* User: Steve
* Date: 1/30/12
*/
public class FastaStreamIterator implements Iterator<FastaEntry> {
    private LineNumberReader m_Reader;
    private String m_CurrentLine;
    private FastaEntry m_Next;

    public FastaStreamIterator(LineNumberReader rdr) {
        m_Reader = rdr;
        m_Next = readNext();
    }

    public FastaStreamIterator(InputStream inp) {
        this(new LineNumberReader(new InputStreamReader(inp)));
    }

    /**
     * Returns {@code true} if the iteration has more elements.
     * (In other words, returns {@code true} if {@link #next} would
     * return an element rather than throwing an exception.)
     *
     * @return {@code true} if the iteration has more elements
     */
    @Override
    public boolean hasNext() {
        return m_Next != null;
    }

    /**
     * Returns the next element in the iteration.
     *
     * @return the next element in the iteration
     * @throws java.util.NoSuchElementException
     *          if the iteration has no more elements
     */
    @Override
    public FastaEntry next() {
        if (m_Next == null)
            throw new NoSuchElementException();
        FastaEntry ret = m_Next;
        m_Next = readNext();
        return ret;
    }

    protected FastaEntry readNext() {
        if (m_Reader == null)
            return null;
        try {
            if (m_CurrentLine == null)
                m_CurrentLine = m_Reader.readLine();
            if (m_CurrentLine == null)
                return null;
            while (!m_CurrentLine.startsWith(">")) {
                m_CurrentLine = m_Reader.readLine();
                if (m_CurrentLine == null)
                    return null;
            }
            String annotation = m_CurrentLine.substring(1);

            m_CurrentLine = m_Reader.readLine();
            if (m_CurrentLine == null)
                return null;
            StringBuilder sb = new StringBuilder();
            while (!m_CurrentLine.startsWith(">")) {
                sb.append(m_CurrentLine);
                m_CurrentLine = m_Reader.readLine();
                if (m_CurrentLine == null) {
                    m_Reader = null;
                    return null;
                }
            }

            String value = sb.toString();


            return new FastaEntry(annotation, value);
        }
        catch (IOException e) {
            throw new RuntimeException(e);

        }
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("Cannot remove");
    }
}
