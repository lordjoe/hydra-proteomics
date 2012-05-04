package com.lordjoe.utilities;

/**
 * com.lordjoe.utilities.Point
 * User: steven
 * Date: 3/8/12
 */
public class Point {
    public static final Point[] EMPTY_ARRAY = {};

    public static final Point ZERO = new Point(0,0);

    private final double m_X;
    private final double m_Y;

    public Point(final double pX, final double pY) {
        m_X = pX;
        m_Y = pY;
    }

    public double getX() {
        return m_X;
    }

    public double getY() {
        return m_Y;
    }

    @Override
    public String toString() {
        return
                 "" + m_X +
                ", " + m_Y
                 ;
    }
}
