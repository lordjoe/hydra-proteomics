/*
 $Id: XMLImportUtil.java 3207 2009-04-09 06:48:11Z gregory $

 Copyright (C) 2006 Gregory Vincic, Olle Mansson
 Copyright (C) 2007 Gregory Vincic

 This file is part of Proteios.
 Available at http://www.proteios.org/

 Proteios is free software; you can redistribute it and/or modify it
 under the terms of the GNU General Public License as published by
 the Free Software Foundation; either version 2 of the License, or
 (at your option) any later version.

 Proteios is distributed in the hope that it will be useful, but
 WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program; if not, write to the Free Software
 Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 02111-1307, USA.
 */
package org.proteios.io;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

/**
 * This class supports simple extraction of data from XML tags.
 * 
 * Used e.g. to parse the attribute data in a start tag
 * in an XML file.
 * 
 * @author Olle
 * @version 2.0
 * @proteios.modified $Date: 2009-04-08 23:48:11 -0700 (Wed, 08 Apr 2009) $
 */
public class XMLImportUtil
{
	/**
	 * Seeks for namespace data.
	 *
	 * This method extracts namespace value 'namespacePrefix' for
	 * searchString "Prefix" and 'namespaceURI' for searchString "URI"
	 * from XML start elements <...  xmlns:namespacePrefix="namespaceURI"  ...>.
	 *
	 * @param searchStr String A string indicating what to look for.
	 * @param parser XMLStreamReader instance.
	 * @return String with namespace data for first namespace item, if found, else (String)null.
	 * @throws XMLStreamException If there is an XML Stream related error
	 */
	public static String seekNamespace(String searchStr, XMLStreamReader parser) throws XMLStreamException
	{
		/*
		 * Get namespace data.
		 */
		for (int i = 0; i < parser.getNamespaceCount(); i++) {
			String namespacePrefix = parser.getNamespacePrefix(i);
			String namespaceURI = parser.getNamespaceURI(i);

			if (searchStr.equals("Prefix"))
			{
				return namespacePrefix;
			}
			else if (searchStr.equals("URI"))
			{
				return namespaceURI;
			}
		}
		/*
		 * Namespace data not found, return null
		 */
		return null;
	}

	/**
	 * Seeks for value of attribute.
	 *
	 * This method extracts attribute value 'attrValue' for
	 * attribute name 'attrName' from XML start elements
	 * of type: <...  attrName="attrValue"  ...>
	 *
	 * @param searchStr String with attribute name to look for.
	 * @param parser XMLStreamReader instance.
	 * @return String with value for searched attribute name,
	 * if found, else (String)null.
	 * @throws XMLStreamException If there is an XML Stream related error
	 */
	public static String seekAttribute(String searchStr, XMLStreamReader parser) throws XMLStreamException
	{
		/*
		 * Get attributes
		 */
		for (int i = 0; i < parser.getAttributeCount(); i++) {
			//String attrPrefix = parser.getAttributePrefix(i);
			//String attrNamespace = parser.getAttributeNamespace(i);
			String attrName = parser.getAttributeLocalName(i);
			String attrValue = parser.getAttributeValue(i);

			if (attrName.equals(searchStr)) {
				return attrValue;
			}
		}
		/*
		 * String not found, return null
		 */
		return null;
	}

	/**
	 * Seeks for value of attribute from name-value pairs.
	 * 
	 * This method extracts attribute value 'attrValue' for
	 * attribute name 'attrName' from XML start elements
	 * of type: <...  name="attrName" value="attrValue"  ...>
	 *
	 * @param searchStr String with attribute name to look for.
	 * @param parser XMLStreamReader instance.
	 * @return String with value for searched attribute name,
	 * if found, else (String)null.
	 * @throws XMLStreamException If there is an XML Stream related error
	 */
	public static String seekAttributeFromNameValuePair(String searchStr, XMLStreamReader parser) throws XMLStreamException
	{
		/*
		 * Get attributes
		 */
		int nAttr = parser.getAttributeCount();
		for (int i = 0; i < nAttr; i++) {
			//String attrPrefix = parser.getAttributePrefix(i);
			//String attrNamespace = parser.getAttributeNamespace(i);
			String attrName = parser.getAttributeLocalName(i);
			String attrValue = parser.getAttributeValue(i);

			if (attrName.equals("name") && attrValue.equals(searchStr)) {
				if (i < (nAttr - 1)) {
					/*
					 * Get next attribute.
					 */
					attrName = parser.getAttributeLocalName(i+1);
					attrValue = parser.getAttributeValue(i+1);
					if (attrName.equals("value")) {
						return attrValue;
					}
				}
			}
		}
		/*
		 * Attribute not found, return null
		 */
		return null;
	}

	/**
	 * Fetches a specific "name" part from attributes,
	 * where the name attribute index is given (starting from 0).
	 * 
	 * @param index int with index of name attribute.
	 * @param parser XMLStreamReader instance.
	 * @return String with value of attribute name,
	 * if found, else (String)null.
	 * @throws XMLStreamException If there is an XML Stream related error
	 */
	public static String fetchNameAttribute(int index, XMLStreamReader parser) throws XMLStreamException
	{
		/*
		 * Get attributes
		 * Count all attributes whose name is literally "name",
		 * starting with 0, and return the corresponding
		 * value string if the count equals the index argument.
		 */
		int nameIndex = -1;
		int nAttr = parser.getAttributeCount();
		for (int i = 0; i < nAttr; i++) {
			//String attrPrefix = parser.getAttributePrefix(i);
			//String attrNamespace = parser.getAttributeNamespace(i);
			String attrName = parser.getAttributeLocalName(i);
			String attrValue = parser.getAttributeValue(i);

			if (attrName.equals("name")) {
				nameIndex++;
			}
			if (nameIndex == index) {
				return attrValue;
			}
		}
		/*
		 * Attribute not found, return null
		 */
		return null;
	}

}
