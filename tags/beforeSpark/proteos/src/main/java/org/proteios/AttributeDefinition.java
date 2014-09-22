/*
 $Id: code_templates.xml 1916 2007-08-31 09:54:00Z jari $
 
 Copyright (C) 2006, 2007 Gregory Vincic
 
 Files are copyright by their respective authors. The contributions to
 files where copyright is not explicitly stated can be traced with the
 source code revision system.
 
 This file is part of Proteios.
 Available at http://www.proteios.org/
 
 Proteios is free software; you can redistribute it and/or
 modify it under the terms of the GNU General Public License
 as published by the Free Software Foundation; either version 2
 of the License, or (at your option) any later version.
 
 Proteios is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.
 
 You should have received a copy of the GNU General Public License
 along with this program; if not, write to the Free Software
 Foundation, Inc., 59 Temple Place - Suite 330,
 Boston, MA  02111-1307, USA.
 */
package org.proteios;

//import org.proteios.props.AttributeReader;

/**
 * An attribute is defined by it's set and get methods. This class defines such
 * attributes. E.g. a String attribute 'name' would be defined as
 * 
 * <pre>
 * AttributeDefinition nameA = new AttributeDefinition(String.class, &quot;Name&quot;);
 * </pre>
 * 
 * a boolean attribute would look like
 * 
 * <pre>
 * AttributeDefinition hiddenA = new AttributeDefinition(Boolean.class, &quot;Hidden&quot;,
 * 	&quot;is&quot;, &quot;set&quot;);
 * </pre>
 * 
 * meaning that the attribute Hidden is accessible through isHidden() and
 * setHidden(Boolean) methods.
 * 
 * @author gregory
 * @see ClassDescriptor
 */
public class AttributeDefinition
{
	/**
	 * The class this attribute is part of, cannot be null.
	 */
	private Class describedClass;
	/**
	 * Defines the value type, cannot be null.
	 */
	private Class type;
	/**
	 * Key identifying this attribute.
	 */
	private String key;
	/**
	 * Prefix used to identifying the method reading this attribute. E.g. for
	 * the String attribute 'Name' the prefix would be 'get' resulting in the
	 * method getName
	 */
	private String readPrefix = null;
	private String writePrefix = null;
	/**
	 * Reads an attribute from the value type of this attribute definition
	 */
//	private AttributeReader reader = null;


//	/**
//	 * Creates a method key where read prefix is 'get' and write prefix is 'set'
//	 *
//	 * @param describedClass
//	 * @param type of this attribute value
//	 * @param key identifying this attribute
//	 */
//	public AttributeDefinition(Class describedClass, Class type, String key)
//	{
//		this.describedClass = describedClass;
//		this.type = type;
//		this.key = key;
//		this.readPrefix = "get";
//		this.writePrefix = "set";
//	}
//
//
//	/**
//	 * Creates a method key where read prefix is 'get' and write prefix is 'set'
//	 * with an attribute reader that reads a value from this attribute type.
//	 *
//	 * @param describedClass
//	 * @param type of this attribute value
//	 * @param key identifying this attribute
//	 * @param reader the reader that reads an attribute from this attribute e.g.
//	 *        the name of a Nameable type
//	 */
//	public AttributeDefinition(Class describedClass, Class type, String key,
//			AttributeReader reader)
//	{
//		this.describedClass = describedClass;
//		this.type = type;
//		this.key = key;
//		this.readPrefix = "get";
//		this.writePrefix = "set";
//		this.reader = reader;
//	}


	/**
	 * @param describedClass
	 * @param type of this attribute value
	 * @param key identifying this attribute
	 * @param readPrefix e.g. 'get' or 'is' for boolean values
	 * @param writePrefix e.g. 'set'
	 */
	public AttributeDefinition(Class describedClass, Class type, String key,
			String readPrefix, String writePrefix)
	{
		this.describedClass = describedClass;
		this.type = type;
		this.key = key;
		this.readPrefix = readPrefix;
		this.writePrefix = writePrefix;
	}


	/**
	 * @return the key identifying this attribute
	 */
	public String getKey()
	{
		return key;
	}


	/**
	 * @return concatenated string of readPrefix and key
	 */
	public String getReadMethodName()
	{
		return readPrefix + getKey();
	}


	/**
	 * @return concatenated string of writePrefix and key
	 */
	public String getWriteMethodName()
	{
		return writePrefix + getKey();
	}


	public Class getAttributeType()
	{
		return type;
	}


	/**
	 * A defined attribute is equal to another if the keys are the same.
	 */
	@Override
	public boolean equals(Object obj)
	{
		AttributeDefinition atr = (AttributeDefinition) obj;
		return atr.getKey().equals(getKey());
	}


	@Override
	public int hashCode()
	{
		return getKey().hashCode();
	}


	@Override
	public String toString()
	{
		return getKey();
	}


	public Class getDescribedClass()
	{
		return describedClass;
	}


//	public AttributeReader getReader()
//	{
//		return reader;
//	}
}
