package com.lordjoe.lib.xml;
import java.util.Vector;


/**
* Class representing the Object passed to us by 3COM to set up 
* an item in a shopping cart
*/
public class ICCCItem 
{
    private String m_MFR;
    private String m_MSKU;
    private int m_QTY;
    
    public ICCCItem() {
    }
    
    
    public String getMFR() {
        return(m_MFR);
    }
    public void setMFR(String s) {
        m_MFR = s ;
    }
    public String getMSKU() {
        return(m_MSKU);
    }
    public void setMSKU(String s) {
        m_MSKU = s ;
    }
    public int getQTY() {
        return(m_QTY);
    }
    public void setQTY(int s) {
        m_QTY = s ;
    }
    
    
    
}
