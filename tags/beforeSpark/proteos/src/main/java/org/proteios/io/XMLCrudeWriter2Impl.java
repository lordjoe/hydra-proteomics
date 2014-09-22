/*
 $Id: XMLCrudeWriter2Impl.java 3207 2009-04-09 06:48:11Z gregory $

 Copyright (C) 2007 Gregory Vincic, Olle Mansson

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


import java.io.OutputStream;
import javax.xml.stream.XMLStreamException;

/**
 * This class supports crude XML writing, with some added convenience methods.
 * 
 * This class contains some methods that may help writing
 * XML files. It was created as support for e.g. writing
 * single tags with XMLStreamWriter could not be found.
 *
 * To get consistent indentation and change to a new line,
 * the following rules are used:
 * 1. A start element or comment starts a new line.
 * 2. An end element starts a new line, unless following data.
 * 3. Indentation level is increased by one before start element. 
 * 4. Indentation level is decreased by one after end element.
 * To avoid having the outermost element block indented,
 * the indentaiion level starts at -1 instead of 0.
 *
 * @author Olle
 * @version 2.0
 * @proteios.modified $Date: 2007-05-09 10:59:00 +0200 (Wed, 09 May 2007) $
 */
public class XMLCrudeWriter2Impl extends XMLCrudeWriterImpl implements XMLCrudeWriter2
{
	/**
	 * Constructor that takes an output stream as parameter.
	 * 
	 * @param outStream The FileWriter output stream to use.
	 */
	public XMLCrudeWriter2Impl(OutputStream outStream)
	{
		super(outStream);
	}


	/**
	 * Writes XML start tag for XML block.
	 * 
	 * @param element String with element name.
	 * @throws XMLStreamException If there is an XML Stream related error
	 */
	public void writeStartTag(String element)
		throws XMLStreamException
	{
		/*
		 * Write start tag for XML block.
		 */
		writeStartElement(element);
		writeStartElementEnd();
	}


	/**
	 * Writes XML start list tag for XML block.
	 * 
	 * @param element String with element name.
	 * @param count int the number of XML blocks in the list.
	 * @throws XMLStreamException If there is an XML Stream related error
	 */
	public void writeStartListTag(String element, int count)
		throws XMLStreamException
	{
		/*
		 * Write start tag for current XML block.
		 */
		writeStartElement(element);
		writeAttribute("count", Integer.toString(count));
		writeStartElementEnd();
	}


	/**
	 * Writes XML end tag for XML block.
	 * 
	 * @param element String with element name.
	 * @throws XMLStreamException If there is an XML Stream related error
	 */
	public void writeEndTag(String element)
		throws XMLStreamException
	{
		/*
		 * Write end tag for XML block.
		 */
		writeEndElement(element);
	}


	/**
	 * Convenience method for writing simple element pair.
	 * Writes XMl expression like: <elementName>data</elementName>
	 * 
	 * @param elementName String with element name.
	 * @param data String with element data.
	 * @throws XMLStreamException If there is an XML Stream related error
	 */
	public void writeSimpleElementPair(String elementName, String data)
			throws XMLStreamException
	{
		/*
		 * Write simple tag pair with data.
		 */
		writeStartElement(elementName);
		writeStartElementEnd();
		writeCharacters(data);
		writeEndElement(elementName);
	}
}
