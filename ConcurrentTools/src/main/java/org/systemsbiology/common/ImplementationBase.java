package org.systemsbiology.common;

import com.lordjoe.lib.xml.*;
import com.lordjoe.utilities.*;

import java.io.*;
import java.util.*;

/**
 * org.systemsbiology.common.ImplementationBase
 * written by Steve Lewis
 * on Apr 23, 2010
 */
public abstract class ImplementationBase implements ITagHandler, IXMLWriter
{
    public static final ImplementationBase[] EMPTY_ARRAY = {};
    public static final Class THIS_CLASS = ImplementationBase.class;


    // Fields are protected so super classes can alter limit
    protected String m_Name;
    protected String m_Description;

    public ImplementationBase() {
     }

    public String getName() {
        if(m_Name == null)
            return getClass().getSimpleName();
        return m_Name;
    }

    public void setName(String pName) {
        m_Name = pName;
    }

    public String getDescription() {
        return m_Description;
    }

    public void setDescription(String pDescription) {
        m_Description = pDescription;
    }


    /* (non-Javadoc)
    * @see com.lordjoe.lib.xml.ITagHandler#handleTag(java.lang.String, com.lordjoe.lib.xml.NameValue[])
    */

    public Object handleTag(String TagName, NameValue[] attributes) {
        return null;
    }


    /**
     * return the tag name for writing out XML
     *
     * @return
     */
    public String getTagName()
    {
        return getClass().getSimpleName();
    }


    public void writeXML() {
        String xml = getXMLString();
        String FileName = ClassUtilities.shortClassName(this);
        String id = getName();
        if (id != null)
            FileName += id;
        FileName += ".xml";
        FileUtilities.writeFile(FileName, xml);
        //  GeneralUtilities.printString(xml);
    }

    public String getXMLString() {
        return (getXMLString(false));
    }

    public String getXMLString(boolean expandFixed) {
        try {
            XMLPropertySet props = new XMLPropertySet();
            StringBuffer sb = new StringBuffer();
            appendXML(props, sb, 0, expandFixed);
            String xml = sb.toString();
            return (xml);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void appendXML(XMLPropertySet props, Appendable sb, int indent)  throws IOException {
        appendXML(props, sb, indent, false);
    }

    public void appendXML(XMLPropertySet props, Appendable sb, int indent, boolean expandFixed) throws IOException {
        props.setFixedEntitiesExpanded(expandFixed);
        String className = buildStartTag(props, sb, indent);
        appendProperties(props, sb, indent);
        sb.append(Util.indentString(indent));
        sb.append("</" + className + ">\n");
    }


    public void appendTagOnlyXML(XMLPropertySet props, Appendable sb, int indent,
                                 boolean expandFixed) throws IOException {
        props.setFixedEntitiesExpanded(expandFixed);
        buildClosedStartTag(props, sb, indent);
    }


    protected String buildStartTag(XMLPropertySet props, Appendable sb, int indent) throws IOException {
        String className = getTagName();
        sb.append(Util.indentString(indent));
        sb.append("<");
        sb.append(className);
        sb.append(" ");
        appendAttributes(props, sb);
        sb.append(" >\n");
        return (className);
    }

    protected void buildClosedStartTag(XMLPropertySet props, Appendable sb, int indent) throws IOException  {
        String className = getTagName();
        sb.append(Util.indentString(indent));
        sb.append("<");
        sb.append(className);
        sb.append(" ");
        appendAttributes(props, sb);
        sb.append(" />\n");
    }

    protected void appendAttributes(XMLPropertySet props, Appendable sb) throws IOException {
        addPropertyAttribute("name", props, sb);
    }

    protected void addPropertyAttribute(String name, XMLPropertySet props, Appendable sb) throws IOException  {
        addPropertyAttribute(this, name, props, sb);
    }


    public static void addPropertyAttribute(Object target, String name, XMLPropertySet props,
                                            Appendable sb) throws IOException {
        if (!props.isHandled(name)) {
            Object prop = ClassAnalyzer.getProperty(target, name);
            if (prop != null) {
                sb.append(" " + name + "=\"");
                String str = ImplementationUtilities.convertToString(prop);
                sb.append(str);
                sb.append("\" ");
            }
            else {
                ClassProperty classProperty = ClassAnalyzer.getClassProperty(target.getClass(), name);
                if (!classProperty.isNullable()) {
                    throw new IllegalStateException("Value for " + name + " in class " + target.getClass().getName() +
                            " can't be null");
                }
            }
        }
    }


    protected void appendProperties(XMLPropertySet props, Appendable sb, int indent) throws IOException  {
        if (props.isHandled(this))
            return;
        appendReflectiveProperties(props, sb, indent + 1);
        appendCollectionProperties(props, sb, indent + 1);
    }


    protected void appendReflectiveProperties(XMLPropertySet props, Appendable sb, int indent) throws IOException {
        String[] reflexiveProps = getReflectiveProperties();
        for (int i = 0; i < reflexiveProps.length; i++) {
            String reflexiveProp = reflexiveProps[i];
            appendReflectiveProperty(props, sb, indent, reflexiveProp);
        }
    }

    protected void appendCollectionProperties(XMLPropertySet props, Appendable sb, int indent) throws IOException {
        //GeneralUtilities.printString("Starting Collections " + this.getClass().getName() + " with length " + sb.length());

    }

    /**
     * return null if the object is in a valid state - otherwise return
     * an error
     * NOTE this version is made final to force overriding the protected
     * version below
     *
     * @return
     */
    public final String testValidation() {
        StringBuffer sb = new StringBuffer();
        validateState(sb);
        if (sb.length() > 0)
            return (sb.toString()); // problem
        return (null); // all OK
    }

    /**
     * add validation tests
     *
     * @param sb
     */
    protected void validateState(Appendable sb) {
        return;
    }


    protected final String[] getReflectiveProperties() {
        List holder = new ArrayList();
        accumulateReflectiveProperties(holder);
        String[] ret = new String[holder.size()];
        holder.toArray(ret);
        return (ret);
    }

    /**
     * Override this to handle more properties
     *
     * @param holder
     */
    protected void accumulateReflectiveProperties(List holder) {
        // nothing at this level
    }

    protected void appendPropertyAsTag(String propName, Object value, Appendable sb, int indent) throws IOException {
        if (value == null)
            return;
        String valueString = ImplementationUtilities.buildValueString(value);
        if (valueString.length() == 0)
            return;
        sb.append(Util.indentString(indent));
        sb.append("<");
        sb.append(propName);
        sb.append(">");
        sb.append(XMLUtil.toXMLString(valueString));
        sb.append("</");
        sb.append(propName);
        sb.append(">\n");

    }

    protected void appendReflectiveProperty(XMLPropertySet props, Appendable sb, int indent,
                                            String propName) throws IOException {
        Object value = ClassAnalyzer.getProperty(this, propName);
        if ("description".equals(propName)) {
            appendPropertyAsTag(propName, value, sb, indent);
            return;
        }
        sb.append(Util.indentString(indent));
        sb.append("<");
        sb.append(propName);
        sb.append(" ");
        sb.append(" value=\"");
        String val = ImplementationUtilities.buildValueString(value);
        sb.append(val);
        sb.append("\" />");
        sb.append("\n");
    }



    public boolean hasRequiredProperties(String[] props) {
        for (int i = 0; i < props.length; i++) {
            String prop = props[i];
            if (!hasRequiredProperty(prop))
                return (false);
        }
        return (true);
    }

    public boolean hasRequiredProperty(String prop) {
        Object test = ClassAnalyzer.getProperty(this, prop);
        return (validProperty(test));
    }

    /**
     * Test the valifity if a property value
     *
     * @param test posdsibly null value to test
     * @return
     */
    public static boolean validProperty(Object test) {
        if (test == null)
            return (false);
        if (test instanceof String) {
            return (!Util.isEmptyString((String) test));
        }
        if (test instanceof Integer) {
            return (((Integer) test).intValue() != Integer.MIN_VALUE);
        }
        if (test instanceof Date) {
            return (((Date) test).getTime() != 0);
        }
        return (true); // think of other tests later
    }

  
}
