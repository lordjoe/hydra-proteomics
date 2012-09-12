package org.systemsbiology.xtandem.peptide;

/**
 * org.systemsbiology.xtandem.peptide.FisherTable
 * User: steven
 * see http://research.microsoft.com/en-us/um/redmond/projects/mscompbio/fisherexacttest/details.aspx
 * Date: 9/11/12
 */
public class FisherTable {
    public static final FisherTable[] EMPTY_ARRAY = {};

    public static final int NOT_PRESENT_NOT_DECTECTED = 0;
    public static final int NOT_PRESENT_DECTECTED = 1;
    public static final int PRESENT_NOT_DECTECTED = 2;
    public static final int  PRESENT_DECTECTED = 3;

    private final int[] m_Values = new int[4];

    public FisherTable() {
    }

    public FisherTable(int a, int b, int c, int d) {
        this();
        addTo(a, b, c, d);
    }

    public int sum() {
        int ret = 0;
        for (int i = 0; i < m_Values.length; i++) {
            ret += m_Values[i];
        }
        return ret;
    }

    public double getProbability() {
        FisherExact fe = new FisherExact(sum() + 20);

         double p = fe.getP(m_Values[0], m_Values[1], m_Values[2], m_Values[3]);
        return p;
    }


    public void addTo(int np_nd, int np_d, int p_nd, int pd) {
        int index = 0;
        m_Values[NOT_PRESENT_NOT_DECTECTED] += np_nd;
        m_Values[NOT_PRESENT_DECTECTED] += np_d;
        m_Values[PRESENT_NOT_DECTECTED] += p_nd;
        m_Values[PRESENT_DECTECTED] += pd;
    }

    public int[] getValues() {
        return m_Values;
    }
}
