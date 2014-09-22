/*
 $Id: XMLReaderUtil.java 3207 2009-04-09 06:48:11Z gregory $

 Copyright (C) 2007 Gregory Vincic, Olle Mansson

 Files are copyright by their respective authors. The contributions to
 files where copyright is not explicitly stated can be traced with the
 source code revision system.

 This file is part of Proteios.
 Available at http://www.proteios.org/

 Proteios-2.x is free software; you can redistribute it and/or
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
package org.proteios.io;

import java.util.Iterator;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

/**
 * Use this class to simplify lookup and positioning of an XMLStreamReader
 * 
 * @author gregory
 */
public class XMLReaderUtil
{
	/**
	 * Find the next element matching template tag and adds it as a child to the
	 * parent.
	 * 
	 * @param parser XMLStreamReader
	 * @param factory used to create tags
	 * @param template XMLTag to match
	 * @param parent optional parent tag
	 * @return a new tag matching the template or null if none is found
	 */
	public XMLTag parse(XMLStreamReader parser, TagFactory factory,
			XMLTag template, XMLTag parent)
			throws XMLStreamException
	{
		XMLTag tag = null;
		if (gotoTag(parser, template))
		{
			tag = factory.create(template.getClass().cast(template));
			for (int i = parser.next(); i != XMLStreamConstants.END_DOCUMENT; i = parser
				.next())
			{
				if (template.matchEnd(parser, parent))
				{
					factory.done(tag);
					break;
				}
				Iterator<XMLTag> allowed = template.allowed();
				while (allowed.hasNext())
				{
					XMLTag childTemplate = allowed.next();
					if (childTemplate.matchStart(parser, tag))
					{
						XMLTag child = parse(parser, factory, childTemplate,
							tag);
						if (tag != null && child != null)
						{
							tag.addChild(child);
							child.parent = tag;
						}
						break;
					}
				}
			}
		}
		return tag;
	}


	/**
	 * Positions the parser to start tag matching the given template. Starts by
	 * looking at the current position. Note that the parser will be moved to
	 * the end of the stream if no matching tag is found.
	 * 
	 * @param parser to position
	 * @param template XMLTag to match
	 * @return true if one is found, false otherwise
	 */
	public boolean gotoTag(XMLStreamReader parser, XMLTag template)
	{
		try
		{
			for (int event = parser.getEventType(); event != XMLStreamConstants.END_DOCUMENT; event = parser
				.next())
			{
				if (template.matchStart(parser, null))
					return true;
			}
		}
		catch (XMLStreamException e)
		{
			e.printStackTrace();
		}
		return false;
	}


	/**
	 * Positions the parser to the tag named tagName.
	 * 
	 * @param parser to position
	 * @param tagName in UPPERCASE
	 * @return true if one is found, false otherwise
	 */
	public boolean gotoTag(XMLStreamReader parser, String tagName)
	{
		try
		{
			for (int event = parser.next(); event != XMLStreamConstants.END_DOCUMENT; event = parser
				.next())
			{
				if (event == XMLStreamConstants.START_ELEMENT)
				{
					String tag = parser.getLocalName().toUpperCase();
					if (tag.equals(tagName))
					{
						return true;
					}
				}
			}
		}
		catch (XMLStreamException e)
		{
			e.printStackTrace();
		}
		return false;
	}


	/**
	 * If attrIndex and attrValue are both null only the tagName will be
	 * searched for
	 * 
	 * @param parser xml reader to position
	 * @param tagName in UPPERCASE
	 * @param attrIndex attribute index to match
	 * @param attrValue value of attribute to match
	 * @return true if one is found, false otherwise
	 */
	public boolean gotoTag(XMLStreamReader parser, String tagName,
			Integer attrIndex, String attrValue)
	{
		try
		{
			for (int event = parser.next(); event != XMLStreamConstants.END_DOCUMENT; event = parser
				.next())
			{
				if (event == XMLStreamConstants.START_ELEMENT)
				{
					String tag = parser.getLocalName().toUpperCase();
					if (tag.equals(tagName))
					{
						String attributeValue = parser
							.getAttributeValue(attrIndex);
						if (attributeValue != null && attributeValue
							.equals(attrValue))
						{
							return true;
						}
					}
				}
			}
		}
		catch (XMLStreamException e)
		{
			e.printStackTrace();
		}
		return false;
	}


	/**
	 * @param parser
	 * @param tagName in UPPERCASE
	 * @param attrName attribute name to match
	 * @param attrValue value of attribute to match
	 * @return true if one is found, false otherwise
	 */
	public boolean gotoTag(XMLStreamReader parser, String tagName,
			String attrName, String attrValue)
	{
		try
		{
			for (int event = parser.next(); event != XMLStreamConstants.END_DOCUMENT; event = parser
				.next())
			{
				if (event == XMLStreamConstants.START_ELEMENT)
				{
					String tag = parser.getLocalName().toUpperCase();
					if (tag.equals(tagName))
					{
						String attributeValue = parser.getAttributeValue(null,
							attrName);
						if (attributeValue != null && attributeValue
							.equals(attrValue))
						{
							return true;
						}
					}
				}
			}
		}
		catch (XMLStreamException e)
		{
			e.printStackTrace();
		}
		return false;
	}
}
