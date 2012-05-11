/*
 $Id: XMLCrudeWriter.java 3207 2009-04-09 06:48:11Z gregory $

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

//import java.io.FileWriter;
import java.io.OutputStream;
import javax.xml.stream.XMLStreamException;

/**
 * This interface defines crude XML writing.
 * 
 * This interface contains some methods that may help writing
 * XML files. It was created as support for e.g. writing
 * single tags with XMLStreamWriter could not be found.
 *
 * It is up to the implementations to decide on indentation,
 * which may be ignored if desired, but if guidelines are
 * requested, recommended use is as follows:
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
 * @proteios.modified $Date: 2009-04-08 23:48:11 -0700 (Wed, 08 Apr 2009) $
 */
public interface XMLCrudeWriter
{
	/**
	 * Get FileWriter output stream.
	 * 
	 * @return outStream FileWriter output stream.
	 */
	//public FileWriter getOutStream();
	public OutputStream getOutStream();

	/**
	 * Set FileWriter output stream.
	 * 
	 * @param outStream FileWriter output stream.
	 */
	//public void setOutStream(FileWriter outStream);
	public void setOutStream(OutputStream outStream);

	/**
	 * Get indentation use flag.
	 * 
	 * @return indentationUsed boolean indentation use flag.
	 */
	public boolean isIndentationUsed();

	/**
	 * Set indentation use flag.
	 * 
	 * @param indentationUsed boolean indenation use flag.
	 */
	public void setIndentationUsed(boolean indentationUsed);

	/**
	 * Writes XML file header.
	 * 
	 * @param encoding String with XML encoding.
	 * @param version String with XML version.
	 * @throws XMLStreamException If there is an error
	 */
	public void writeStartDocument(String encoding, String version)
		throws XMLStreamException;

	/**
	 * Writes end of XML document.
	 *
	 * Note that unlike e.g. XMLStreamWriter
	 * writeEndDocument() method, this does not
	 * close any open tags, but simple writes
	 * a line separator to finish the last line.
	 * All tags must be closed by "hand".
	 *
	 * @throws XMLStreamException If there is an error
	 */
	public void writeEndDocument()
		throws XMLStreamException;

	/**
	 * Writes XML comment.
	 * 
	 * @param comment String with comment.
	 * @throws XMLStreamException If there is an error
	 */
	public void writeComment(String comment)
		throws XMLStreamException;

	/**
	 * Writes XML start element.
	 * 
	 * @param element String with element name.
	 * @throws XMLStreamException If there is an error
	 */
	public void writeStartElement(String element)
		throws XMLStreamException;

	/**
	 * Writes XML attribute.
	 * 
	 * @param name String with attribute name.
	 * @param value String with attribute value.
	 * @throws XMLStreamException If there is an error
	 */
	public void writeAttribute(String name, String value)
		throws XMLStreamException;

	/**
	 * Writes XML namespace.
	 * 
	 * @param name String with namespace name.
	 * @param value String with namespace value.
	 * @throws XMLStreamException If there is an error
	 */
	public void writeNamespace(String name, String value)
		throws XMLStreamException;

	/**
	 * Writes XML start element end.
	 * 
	 * @throws XMLStreamException If there is an error
	 */
	public void writeStartElementEnd()
		throws XMLStreamException;

	/**
	 * Writes XML single element end.
	 * 
	 * @throws XMLStreamException If there is an error
	 */
	public void writeSingleElementEnd()
		throws XMLStreamException;

	/**
	 * Writes XML end element.
	 * 
	 * @param element String with element name.
	 * @throws XMLStreamException If there is an error
	 */
	public void writeEndElement(String element)
		throws XMLStreamException;

	/**
	 * Writes text between XML start and end tags.
	 * 
	 * @param text String with element text.
	 * @throws XMLStreamException If there is an error
	 */
	public void writeCharacters(String text)
		throws XMLStreamException;

	/**
	 * Flushes output stream.
	 * 
	 * @throws XMLStreamException If there is an error
	 */
	public void flush()
		throws XMLStreamException;

	/**
	 * Close (not) output stream.
	 * 
	 * @throws XMLStreamException If there is an error
	 */
	public void close()
		throws XMLStreamException;

}
