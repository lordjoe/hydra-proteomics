package org.proteios.io;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;

/**
 * Defines an xml tag that is readable using a XMLReader and TagFactory.
 * 
 * @author gregory
 */
public abstract class XMLTag
{
	protected XMLTag parent;
	/**
	 * Allowed child tags, used by the {@link XMLReaderUtil} to match a child
	 */
	private List<XMLTag> allowed = new ArrayList<XMLTag>();
	/**
	 * Stores child tags. See
	 * {@link XMLReaderUtil#parse(XMLStreamReader, TagFactory, XMLTag, XMLTag)}
	 */
	private List<XMLTag> children = null;


	/**
	 * Add a child tag
	 * 
	 * @param child
	 */
	public void addChild(XMLTag child)
	{
		if (children == null)
			children = new ArrayList<XMLTag>();
		children.add(child);
	}


	/**
	 * Adds one or more xml tags that are allowed as child tags. Use this method
	 * to build a tree of tags that should be read.
	 * 
	 * @param tags
	 * @return true, just as the Add method in a Collection
	 */
	public boolean allow(XMLTag... tags)
	{
		for (XMLTag t : tags)
			allowed.add(t);
		return true;
	}


	/**
	 * @return iterator of allowed tags
	 */
	public Iterator<XMLTag> allowed()
	{
		return allowed.iterator();
	}


	/**
	 * Overwrites the current list of children with a new list.
	 * 
	 * @param children the list of child tags to set
	 */
	public void setChildren(List<XMLTag> children)
	{
		this.children = children;
	}


	/**
	 * @return iterator of child tags, null if there are no children
	 */
	public Iterator<XMLTag> children()
	{
		if (children != null)
			return children.iterator();
		return null;
	}


	/**
	 * Constructor which creates an XMLTag without a parent
	 */
	XMLTag()
	{
		this.parent = null;
	}


	/**
	 * Constructs an XMLTag with the given parent, may be null which is the same
	 * as an empty constructor.
	 * 
	 * @param parent XMLTag of this XMLTag
	 */
	XMLTag(XMLTag parent)
	{
		this.parent = parent;
	}


	/**
	 * This method returns true if the class name matches the start tagname.
	 * Character casing is ignored. Eg. a class named Body that extends the
	 * XMLTag will match tags <body> and <BODY>. This method will first check if
	 * the parser is at currently at the begining of a tag.
	 * 
	 * @param parser used to read an xml stream
	 * @param name tag name to match
	 * @return true if the tag matches, false otherwise
	 */
	public boolean matchStartTag(XMLStreamReader parser, String name)
	{
		if (parser.getEventType() == XMLStreamConstants.START_ELEMENT)
		{
			String tagName = parser.getLocalName().toUpperCase();
			return tagName.equals(name);
		}
		return false;
	}


	/**
	 * This method returns true if the class name matches the end tagname.
	 * Character casing is ignored. Eg. a class named Html that extends the
	 * XMLTag will match tags </html> and </HTML>. This method will first check
	 * if the parser is at currently at the end of a tag.
	 * 
	 * @param parser used to read an xml stream
	 * @param name tag name to match
	 * @return true if the classname matches the end tag name
	 */
	public boolean matchEndTag(XMLStreamReader parser, String name)
	{
		if (parser.getEventType() == XMLStreamConstants.END_ELEMENT)
		{
			String tagName = parser.getLocalName().toUpperCase();
			return tagName.equals(name);
		}
		return false;
	}


	/**
	 * Matches an attribute with the specified name and value. This method will
	 * first check if the parser is at currently at the begining of a tag.
	 * 
	 * @param parser used to read an xml stream
	 * @param name of the attribute to match
	 * @param value of the attribute to match
	 * @return true if the attribute matches, false otherwise
	 */
	public boolean matchAttribute(XMLStreamReader parser, String name,
			String value)
	{
		if (parser != null && name != null && value != null)
		{
			if (parser.getEventType() == XMLStreamConstants.START_ELEMENT)
			{
				String attributeValue = parser.getAttributeValue(null, name);
				return value.equals(attributeValue);
			}
		}
		return false;
	}


	/**
	 * @see java.lang.Object#toString()
	 * @return a string representation of this tag and its children
	 */
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("\n ");
		// indent tag
		XMLTag p = parent;
		while (p != null)
		{
			sb.append(" ");
			p = p.parent;
		}
		sb.append(this.asString());
		if (children != null)
		{
			for (XMLTag child : children)
				sb.append(child);
			sb.append("");
		}
		return sb.toString();
	}


	/**
	 * @return a String looking like <TAG a=".." b="..">
	 */
	public String asString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append('<');
		sb.append(this.getClass().getSimpleName());
		String attributes = attributes();
		if (attributes != null)
		{
			sb.append(" ");
			sb.append(attributes);
		}
		sb.append('>');
		return sb.toString();
	}


	/**
	 * Override this method to create an attribute string
	 * 
	 * @return a string looking like a=".." b=".." or null if tag has no
	 *         attributes
	 */
	public String attributes()
	{
		return null;
	}


	/**
	 * Simplifies building of attribute strings
	 * 
	 * @param a list of label, value, label, value,...
	 * @return String label="value", label="value"
	 */
	protected String build(Object... a)
	{
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < a.length; i = i + 2)
		{
			Object label = a[i];
			Object val = a[i + 1];
			if (val != null)
			{
				if (sb.length() > 0)
					sb.append(", ");
				sb.append(label);
				sb.append("=\"");
				sb.append(val);
			}
		}
		return sb.toString();
	}


	/**
	 * Implement this method to match a specific tag. If you only need to match
	 * the name of the tag you can extend the {@link Tag} class.
	 * 
	 * @param parser that reads an xml stream
	 * @param parent the current parent xml tag
	 * @return true if the start tag matches, false otherwise
	 */
	public abstract boolean matchStart(XMLStreamReader parser, XMLTag parent);


	/**
	 * Implement this method to match a specific end tag. If you only use the
	 * name of a class you can extend the {@link Tag} class..
	 * 
	 * @param parser that reads an xml stream
	 * @param parent the current parent xml tag
	 * @return true if the end tag matches, false otherwise
	 */
	public abstract boolean matchEnd(XMLStreamReader parser, XMLTag parent);
}