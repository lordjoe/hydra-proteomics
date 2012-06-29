package org.systemsbiology.xtandem.fragmentation.ui;

/**
 * org.systemsbiology.xtandem.fragmentation.ui.Offset
 * immutable representation of a point interpreted as an offset from 0
 * User: Steve
 * Date: 6/29/12
 */
public class Offset implements Comparable<Offset> {
    public static final Offset[] EMPTY_ARRAY = {};

    public static final Offset ZERO_OFFSET = new Offset(0,0);

    private final int m_X;
    private final int m_Y;

    public Offset(final int x, final int y) {
        m_X = x;
        m_Y = y;
    }

    public boolean isZero()
    {
        return getX() == 0 && getY() == 0;
    }

    public int getX() {
        return m_X;
    }

    public int getY() {
        return m_Y;
    }

    public Offset add(Offset addto)
    {
        return new Offset(getX() + addto.getX(),getY() + addto.getY());
    }

    public Offset subtract(Offset addto)
    {
        return new Offset(getX() - addto.getX(),getY() - addto.getY());
    }

    public Offset setX(int x)
    {
        return new Offset(x,getY() );
    }

    public Offset setY(int y)
    {
        return new Offset( getX(),y );
    }

    @Override
    public String toString() {
        return  "" + m_X +
                "," + m_Y;
    }


    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final Offset offset = (Offset) o;

        if (m_X != offset.m_X) return false;
        if (m_Y != offset.m_Y) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = m_X;
        result = 31 * result + m_Y;
        return result;
    }

    /**
     * sort by x then y
     * @param o
     * @return
     */
    @Override
    public int compareTo(final Offset o) {
        int x = getX();
        int ox = o.getX();
        if(x != ox)
            return x < ox ? -1 : 1;
        int y = getY();
        int oy = o.getY();
        if(y != oy)
            return y < oy ? -1 : 1;
        return 0;
     }
}
