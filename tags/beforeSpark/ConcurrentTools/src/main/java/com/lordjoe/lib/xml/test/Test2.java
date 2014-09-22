package com.lordjoe.lib.xml.test;

/**
 * com.lordjoe.lib.xml.test.Test2
 * @Author Steve Lewis smlewis@lordjoe.com
 */
public class Test2
{
    public static final Test2[] EMPTY_ARRAY = {};
    private String m_Fee;
    private String m_Fie;
    private transient String m_Foe;
    private transient String m_Fum;
    public Test2()
    {
    }

    public void init() {
        setFee("Fee");
        setFie("Fie");
        setFoe("Foe");
        setFum("Fum");
    }

    public String getFee() { return(m_Fee); }
    public void setFee(String in) { m_Fee = in; }

    public String getFie() { return(m_Fie); }
    public void setFie(String in) { m_Fie = in; }

    public String getFoe() { return(m_Foe); }
    public void setFoe(String in) { m_Foe = in; }

    public String getFum() { return(m_Fum); }
    public void setFum(String in) { m_Fum = in; }

}
