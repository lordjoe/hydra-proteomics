package org.systemsbiology.asa;

import java.util.*;

/**
 * org.systemsbiology.asa.Point3d
 * User: Steve
 * Date: 7/11/12
 */
public class Point3d {
    public static final Point3d[] EMPTY_ARRAY = {};
    public static final Random RND = new Random();

    public static final double RANDOM_VECTOR_FACTOR = 100;

    /**
     * rqandom value +/- 100
     * @return
     */
    private static double random_uniform(double min,double max)
    {
        return (RND.nextDouble() * (max - min)) + min;
    }

    public static Point3d randomPoint()
    {
        return new Point3d(random_uniform(-RANDOM_VECTOR_FACTOR,RANDOM_VECTOR_FACTOR),
                random_uniform(-RANDOM_VECTOR_FACTOR,RANDOM_VECTOR_FACTOR),
                random_uniform(-RANDOM_VECTOR_FACTOR,RANDOM_VECTOR_FACTOR));
    }

    public static double random_angle()
    {
        return random_uniform(-Math.PI / 2,Math.PI / 2);
    }

    public static final double RAD2DEG = 180.0 / Math.PI;
    public static final double DEG2RAD = Math.PI / 180.0;
    public static final double SMALL = 1E-6;

    /**
     * @param a
     * @return
     */
    public static boolean isSmall(double a) {
        return a < SMALL;
    }

    private final double m_X;
    private final double m_Y;
    private final double m_Z;

    public Point3d(final double x, final double y, final double z) {
        m_X = x;
        m_Y = y;
        m_Z = z;
    }

    public Point3d(Point3d z) {
        m_X = z.getX();
        m_Y = z.getY();
        m_Z = z.getZ();
    }

    public double getX() {
        return m_X;
    }

    public double getY() {
        return m_Y;
    }

    public double getZ() {
        return m_Z;
    }

    public double distance(Point3d o) {
        double sum = 0;
        double del = getX() - o.getX();
        sum += del * del;
        del = getY() - o.getY();
        sum += del * del;
        del = getZ() - o.getZ();
        sum += del * del;
        return Math.sqrt(sum);
    }

    public double length() {
        return distance(this);
    }


    public Point3d add(Point3d added) {
        return new Point3d(getX() + added.getX(), getY() + added.getY(), getZ() + added.getZ());
    }

    public Point3d subtract(Point3d added) {
        return new Point3d(getX() - added.getX(), getY() - added.getY(), getZ() - added.getZ());
    }

    public double dot(Point3d added) {
        return  getX() * added.getX() + getY() * added.getY() + getZ() * added.getZ();
    }


    public Point3d scale(double added) {
        return new Point3d(getX() * added , getY() * added, getZ()  * added);
    }


    public Point3d copy(double added) {
        return new Point3d(getX()  , getY() , getZ()  );
    }


    public Point3d normalize( ) {
        return scale(1.0 / length());
    }



    public Point3d negate() {
        return new Point3d(-getX(), -getY(), -getZ());
    }

    public boolean eq(Point3d added) {
        if (!isSmall(Math.abs(getX() - added.getX())))
            return false;
        if (!isSmall(Math.abs(getY() - added.getY())))
            return false;
        if (!isSmall(Math.abs(getZ() - added.getZ())))
            return false;

        return true;
    }

}
