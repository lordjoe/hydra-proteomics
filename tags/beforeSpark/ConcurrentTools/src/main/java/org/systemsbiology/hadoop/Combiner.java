package org.systemsbiology.hadoop;

import org.apache.hadoop.fs.*;

/**
 * org.systemsbiology.hadoop.Combiner
 *
 * @author Steve Lewis
 * @date Oct 18, 2010
 */
public class Combiner
{
    public static Combiner[] EMPTY_ARRAY = {};
    public static Class THIS_CLASS = Combiner.class;

    private Path m_SoucePath;

    public Combiner(Path pSoucePath)
    {
        m_SoucePath = pSoucePath;
    }

    public Path getSoucePath()
    {
        return m_SoucePath;
    }

    public void setSoucePath(Path pSoucePath)
    {
        m_SoucePath = pSoucePath;
    }

    public void combine()
    {
        final Path path = getSoucePath();
         
    }
}
