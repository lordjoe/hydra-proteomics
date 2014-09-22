package org.systemsbiology.asa;

import javax.vecmath.*;
import java.util.*;

/**
 * org.systemsbiology.asa.Point3d
 * User: Steve
 * Date: 7/11/12
 */
public class Point3d extends Vector3d {
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

  
    public Point3d(final double px, final double py, final double pz) {
        super(px, py ,pz);
    }

    public Point3d(Point3d pz) {
       this(pz.x,pz.y,pz.z);
    }
 

    public double distance(Point3d o) {
        double sum = 0;
        double del = x - o.x;
        sum += del * del;
        del = y - o.y;
        sum += del * del;
        del = z - o.z;
        sum += del * del;
        return Math.sqrt(sum);
    }

 


    public Point3d add(Point3d added) {
        return new Point3d(x + added.x, y + added.y, z+ added.z);
    }

    public Point3d subtract(Point3d added) {
        return new Point3d(x - added.x, y - added.y, z- added.z);
    }

    public double dot(Point3d added) {
        return  x * added.x + y * added.y + z* added.z;
    }




    public Point3d copy(double added) {
        return new Point3d(x  , y , z );
    }



    public boolean eq(Point3d added) {
        if (!isSmall(Math.abs(x - added.x)))
            return false;
        if (!isSmall(Math.abs(y - added.y)))
            return false;
        if (!isSmall(Math.abs(z- added.z)))
            return false;

        return true;
    }

}
