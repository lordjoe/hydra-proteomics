package org.systemsbiology.asa;

/**
 * org.systemsbiology.asa.Matrix3d
 * User: Steve
 * Date: 7/11/12
 */
public class Matrix3d {
    public static final Matrix3d[] EMPTY_ARRAY = {};

    private final double[] m_Points = new double[9];

    public Matrix3d() {
    }


    public Matrix3d(Matrix3d a) {
        for (int i = 0; i < m_Points.length; i++) {
            m_Points[i] = a.m_Points[i];

        }
    }

    public double elem(int i,int j)
     {
         return m_Points[3 * i + j];
     }


    public void set_elem(int i,int j,double value)
     {
         m_Points[3 * i + j] = value;
     }

}
