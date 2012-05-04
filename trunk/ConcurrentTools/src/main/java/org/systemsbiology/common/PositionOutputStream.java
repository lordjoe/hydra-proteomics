package org.systemsbiology.common;

import java.io.*;

/**
 * org.systemsbiology.common.PositionInputStream
 * Stream which can deliver position
 * written by Steve Lewis
 * on Apr 15, 2010
 * @see http://stackoverflow.com/questions/240294/given-a-java-inputstream-how-can-i-determine-the-current-offset-in-the-stream
 */

public final class PositionOutputStream extends FilterOutputStream
{
    public static final PositionOutputStream[] EMPTY_ARRAY = {};
    public static final Class THIS_CLASS = PositionOutputStream.class;



  private long pos = 0;

  private long mark = 0;

  public PositionOutputStream(OutputStream in)
  {
    super(in);
  }

  /**
   * <p>Get the stream position.</p>
   *
   * <p>Eventually, the position will roll over to a negative number.
   * Reading 1 Tb per second, this would occur after approximately three
   * months. Applications should account for this possibility in their
   * design.</p>
   *
   * @return the current stream position.
   */
  public synchronized long getPosition()
  {
    return pos;
  }


    @Override
    public void write(int i) throws IOException {
        super.write(i);
        pos += 4;
    }

    @Override
    public void write(byte[] pBytes) throws IOException {
        super.write(pBytes);
        pos += pBytes.length;
    }

    @Override
    public void write(byte[] pBytes, int i, int i1) throws IOException {
        super.write(pBytes, i, i1);
        pos += i1;
     }




}