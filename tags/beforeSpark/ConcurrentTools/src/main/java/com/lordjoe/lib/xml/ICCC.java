
package com.lordjoe.lib.xml;
import java.util.Vector;
import java.util.Enumeration;
import com.lordjoe.lib.xml.NameValue;
import com.lordjoe.utilities.*;

/**
* Class representing the Object passed to us by 3COM to set up 
* a shopping cart
*/
public class ICCC  implements INameable,ITagHandler
{
    private String m_version;
    private String m_Name;
    private String m_Country;
    private String m_ID;
    private Vector m_Products;
    
    public ICCC() 
    {
        m_Products = new Vector();
    }
    
    public void addProduct(ICCCItem TheItem)
    {
        m_Products.addElement(TheItem);
    }
    public void removeProduct(ICCCItem TheItem)
    {
        m_Products.removeElement(TheItem);
    }
    
    public Enumeration enumerateProduct()
    {
        return(m_Products.elements());
    }
    
    public int getProductCount()
    {
        return(m_Products.size());
    }
    
    public String getVersion() {
        return(m_version);
    }
    public void setVersion(String s) {
        m_version = s ;
    }
    
    public String getName() {
        return(m_Name);
    }
    public void setName(String s) {
        m_Name = s ;
    }
    
    public String getCountry() {
        return(m_Country);
    }
    public void setCountry(String s) {
        m_Country = s ;
    }
    
    public String getID() {
        return(m_ID);
    }
    public void setID(String s) {
        m_ID = s ;
    }
    
    /** 
    * This returns the object which will handle the tag - the handler
    * may return itself or create a sub-object to manage the tag
    * @param TagName non-null name of the tag
    * @param attributes non-null array of name-value pairs
    * @return possibly null handler
    */
    public Object handleTag(String TagName,NameValue[] attributes)
    {
        Object ret = null;
        if(TagName.equalsIgnoreCase("TRANSFER_SHOPPING_LIST"))
            ret = this;
        if(TagName.equalsIgnoreCase("SENDER"))
            ret = this;
        if(TagName.equalsIgnoreCase("TRANSACTION"))
            ret = this;
        if(TagName.equalsIgnoreCase("SHOPPER"))
            ret = this;
        if(TagName.equalsIgnoreCase("PRODUCT_TSL")) {
            ICCCItem TheItem = new ICCCItem();
            addProduct(TheItem);
            ret = TheItem;
        }
        if(ret != null) 
            ClassAnalyzer.setAttributes(ret,attributes);
            
       return(null);
    }
    
    
    
}
