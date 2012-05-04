package com.lordjoe.lib.xml;

//import org.xml.sax.*;
//import org.w3c.dom.*;

import java.net.URL;
import java.util.*;
import java.lang.reflect.*;
import javax.xml.parsers.*;
import java.io.*;

import com.lordjoe.utilities.*;
import org.w3c.dom.*;
import org.xml.sax.*;
import org.xml.sax.helpers.*;

import java.text.*;

/*
 * com.lordjoe.lib.xml.DocumentReportElement
 * @author smlewis
 * Date: Jan 16, 2003
 */

public class DocumentReportElement
{
    public static final Class THIS_CLASS = DocumentReportElement.class;
    public static final DocumentReportElement[] EMPTY_ARRAY = {};

    public static final int MAX_DISPLAYED_ITEMS = 15;

    private boolean m_Leaf;
    private boolean m_Attribute;
    private String m_Name;
    private Set m_Values;
    private Map m_SubElements;

    public DocumentReportElement()
    {
        m_Values = new HashSet();
        m_SubElements = new HashMap();

    }

    public DocumentReportElement(Element builder)
    {
        this();
        setName(builder.getTagName());
        handleElements(builder);
    }

    public DocumentReportElement(Document builder)
    {
        this(builder.getDocumentElement());
    }

    public void addCorrespondingElement(Document added)
    {
        Element top = added.getDocumentElement();
        addCorrespondingElement(top);

    }

    public void addCorrespondingElement(Element added)
    {
        NodeList items = added.getChildNodes();
        for (int i = 0; i < items.getLength(); i++)
        {
            Node test = items.item(i);
            if (test.getNodeType() == Node.ELEMENT_NODE)
            {
                Element realElement = ((Element) test);
                if (realElement.getTagName().equals(getName()))
                    addCorrespondingElement(realElement); // move to sub items
                else
                    addSubElement(realElement);
                continue;
            }
        }
    }

    public void addDocumentElement(DocumentReportElement added)
    {
        String addName = added.getName();
        Object item = m_SubElements.get(addName);
        if (item != null)
            throw new IllegalStateException("report " + added.getName() + " already exists");
        m_SubElements.put(addName, added);
    //    System.out.println("Adding " + addName + " to " + getName());
    }

    public boolean isLeaf()
    {
        return m_Leaf;
    }

    public void setLeaf(boolean leaf)
    {
        m_Leaf = leaf;
    }

    public String getName()
    {
        return m_Name;
    }

    public void setName(String name)
    {
        m_Name = name;
    }

    public String[] getValues()
    {
        String[] ret = new String[m_Values.size()];
        m_Values.toArray(ret);
        Arrays.sort(ret);
        return (ret);
    }

    public void addValue(String added)
    {
        String name = getName();
        if (!m_Values.contains(added))
            m_Values.add(added);
    }

    public DocumentReportElement[] getSubElements()
    {
        DocumentReportElement[] ret = new DocumentReportElement[m_SubElements.size()];
        m_SubElements.values().toArray(ret);
        return (ret);
    }

    public void setSubElements(Map subElements)
    {
        m_SubElements = subElements;
    }

    public DocumentReportElement getSubElement(String name)
    {
        return ((DocumentReportElement) m_SubElements.get(name));
    }

    protected void handleElements(Element base)
    {
        String name = getName();
        NamedNodeMap attrs = base.getAttributes();
        for (int i = 0; i < attrs.getLength(); i++)
         {
             Node test = attrs.item(i);
             addAttribute(test);
         }


        NodeList items = base.getChildNodes();
        for (int i = 0; i < items.getLength(); i++)
        {
            Node test = items.item(i);
            int type = test.getNodeType();
            String value = test.getNodeValue();
            if(!Util.isEmptyString(value))
                value = value.trim();
            switch (type)
            {
                case Node.ELEMENT_NODE:
                    addSubElement((Element) test);
                    break;
                case Node.TEXT_NODE:
                    if (Util.isEmptyString(value))
                        break;
                    addValue(value);
                    //           setLeaf(true);
                    break;
                case Node.CDATA_SECTION_NODE:
                    if (Util.isEmptyString(value))
                        break;
                    addValue(value);
                    //    setLeaf(true);
                    break;

                case Node.ATTRIBUTE_NODE:
                    addAttribute(test);
                    break;
                default :
                    test = null;
            }
        }
    }

    protected void addAttribute(Node base)
    {
        String name = base.getNodeName();
        if (name.equals("TYPE"))
            name = "TYPE";
        String value = base.getNodeValue();
        DocumentReportElement val = getSubElement(name);
        if (val != null)
        {
            val.addValue(value);
            return;
        }
        val = new DocumentReportElement();
        val.setName(name);
        val.setLeaf(true);
        val.addValue(value);
        addDocumentElement(val);
    }

    protected void addSubElement(Element base)
    {
        String name = base.getTagName();
        DocumentReportElement val = getSubElement(name);
        if (val != null)
        {
            val.handleElements(base);
            return;
        }
        val = new DocumentReportElement(base);
        addDocumentElement(val);
    }

    public String generateXML()
    {
        StringBuffer sb = new StringBuffer();
        generateXML(sb, 0);
        return (sb.toString());
    }

    public void generateXML(StringBuffer sb, int indent)
    {
        System.out.println("Generating XML for " + getName());
        sb.append(Util.indentString(indent));
        sb.append("<Item name=\"" + getName() + "\" >\n");

        handleValues(sb, indent + 1);
        if (!isLeaf())
            handleSubElements(sb, indent + 1);
        sb.append(Util.indentString(indent));
        sb.append("</Item>\n");
    }

    public void handleValues(StringBuffer sb, int indent)
    {
        String[] vals = getValues();
        if (vals.length == 0)
            return;
        sb.append(Util.indentString(indent));
        sb.append("<values>");
        if (vals.length > MAX_DISPLAYED_ITEMS)
        {
            sb.append("total of " + vals.length + " values");
        }
        else
        {
            for (int i = 0; i < vals.length; i++)
            {
                String val = vals[i];
                sb.append(val);
                if (i < vals.length - 1)
                    sb.append(",");
            }
        }
        sb.append("</values>\n");
    }

    public void handleSubElements(StringBuffer sb, int indent)
    {
        DocumentReportElement[] items = getSubElements();
        if (items.length == 0)
            return;
        sb.append(Util.indentString(indent));
        sb.append("<subElements>\n");
        for (int i = 0; i < items.length; i++)
        {
            DocumentReportElement val = items[i];
            String name = val.getName();
            val.generateXML(sb, indent + 1);
        }
        sb.append(Util.indentString(indent));
        sb.append("</subElements>\n");
    }

   

}