/*
 $Id: XMLCrudeWriterImpl.java 3207 2009-04-09 06:48:11Z gregory $

 Copyright (C) 2006, 2007 Gregory Vincic, Olle Mansson

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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import javax.xml.stream.XMLStreamException;

/**
 * This class supports crude XML writing.
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
 * @proteios.modified $Date: 2009-04-08 23:48:11 -0700 (Wed, 08 Apr 2009) $
 */
public class XMLCrudeWriterImpl implements XMLCrudeWriter
{
	/*
	 * FileWriter output Stream
	 */
	private OutputStream outStream;


	/*
	 * Indentation flag
	 */
	private boolean indentationUsed = true;

	/*
	 * Indentation level
	 * Internal use only.
	 */
	private int level = -1;

	/*
	 * Flag to skip change to new line
	 * Internal use only.
	 */
	private boolean skipNewLine = false;

	/*
	 * String line separator
	 * Internal use only.
	 */
	private String lineSeparator;

	/*
	 * Default size of byte buffer for writing a String to an OutputStream.
	 */
	private final int BYTE_BUFFER_SIZE_DEFAULT = 8192;
	
	/*
	 * Size of byte buffer for writing a String to an OutputStream.
	 */
	private int byteBufferSize = BYTE_BUFFER_SIZE_DEFAULT;
	
	/*
	 * Initialize lineSeparator
	 */
	private void initializeLineSeparator()
	{
		lineSeparator = System.getProperty("line.separator");
	}

	/**
	 * Constructor that takes an output stream as parameter.
	 * 
	 * @param outStream The FileWriter output stream to use.
	 */
	public XMLCrudeWriterImpl(OutputStream outStream)
	{
		setOutStream(outStream);
		initializeLineSeparator();
	}

	/**
	 * Get output stream.
	 * 
	 * @return outStream output stream.
	 */
	public OutputStream getOutStream()
	{
		return this.outStream;
	}

	/**
	 * Set output stream.
	 * 
	 * @param outStream output stream to use.
	 */
	public void setOutStream(OutputStream outStream)
	{
		this.outStream = outStream;
	}

	/**
	 * Get indentation use flag.
	 * 
	 * @return indentationUsed boolean indentation use flag.
	 */
	public boolean isIndentationUsed()
	{
		return this.indentationUsed;
	}

	/**
	 * Set indentation use flag.
	 * 
	 * @param indentationUsed boolean indentation use flag.
	 */
	public void setIndentationUsed(boolean indentationUsed)
	{
		this.indentationUsed = indentationUsed;
	}

	/**
	 * Get size of byte buffer for writing a String to an OutputStream.
	 * 
	 * @return byteBufferSize int byte buffer size for writing a string to an OutputStream.
	 */
	public int getByteBufferSize()
	{
		return this.byteBufferSize;
	}

	/**
	 * Set size of byte buffer for writing a String to an OutputStream.
	 * 
	 * @param byteBufferSize int byte buffer size for writing a string to an OutputStream.
	 */
	public void setByteBufferSize(int byteBufferSize)
	{
		this.byteBufferSize = byteBufferSize;
	}

	/**
	 * Writes a string to the output stream.
	 * 
	 * @param string String string to write to output stream.
	 * @throws IOException If there is an error
	 */
	private void writeStr(String string)
		throws IOException
	{
		/*
		 * Write string to output stream.
		 */
		try {
			int numberOfBytesToWrite=0;
			byte [] myBuffer = new byte[getByteBufferSize()];
			ByteArrayInputStream inputStream = new ByteArrayInputStream(string.getBytes());

			while ((numberOfBytesToWrite=inputStream.read(myBuffer))!=-1)
			{
				outStream.write(myBuffer,0,numberOfBytesToWrite);
			}
		} catch (IOException e) {
			throw new IOException("Error writing string : " + e);
		}
	}

	/**
	 * Writes XML file header.
	 * 
	 * @param encoding String with XML encoding.
	 * @param version String with XML version.
	 * @throws XMLStreamException If there is an error
	 */
	public void writeStartDocument(String encoding, String version)
		throws XMLStreamException
	{
		/*
		 * Write XML header
		 */
		try {
			if (!encoding.equals(""))
			{
				writeStr("<?xml version=\"" + version + "\" encoding=\"" + encoding + "\"?>");
			}
			else
			{
				writeStr("<?xml version=\"" + version + "\"?>");
			}
		} catch (IOException e) {
			throw new XMLStreamException("Error writing XML header : " + e);
		}
	}

	/**
	 * Writes XML style sheet header.
	 * 
	 * @param type String XML style sheet type, e.g. "text/xsl", .
	 * @param href String Name of XSL file.
	 * @throws XMLStreamException If there is an error
	 */
	public void writeStyleSheetHeader(String type, String href)
		throws XMLStreamException
	{
		/*
		 * Write XML style sheet header
		 */
		try {
			writeStr(lineSeparator);
			writeStr("<?xml-stylesheet type=\"" + type + "\" href=\"" + href + "\"?>");
		} catch (IOException e) {
			throw new XMLStreamException("Error writing XML style sheet header : " + e);
		}
	}

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
		throws XMLStreamException
	{
		/*
		 * Write lineSeparator to end XML document
		 */
		try {
			writeStr(lineSeparator);
		} catch (IOException e) {
			throw new XMLStreamException("Error writing end of XML document : " + e);
		}
	}

	/**
	 * Writes XML comment.
	 * 
	 * @param comment String with comment.
	 * @throws XMLStreamException If there is an error
	 */
	public void writeComment(String comment)
		throws XMLStreamException
	{
		/*
		 * Write XML comment
		 */
		try {
			startNewLine(level);
			writeStr("<!--" + comment + "-->");
		} catch (IOException e) {
			throw new XMLStreamException("Error writing comment \"" + comment + "\": " + e);
		}
	}

	/**
	 * Writes XML start element.
	 * 
	 * @param element String with element name.
	 * @throws XMLStreamException If there is an error
	 */
	public void writeStartElement(String element)
		throws XMLStreamException
	{
		/*
		 * Write XML start element
		 */
		try {
			level++;
			startNewLine(level);
			writeStr("<" + element);
		} catch (IOException e) {
			throw new XMLStreamException("Error writing start element \"" + element + "\": " + e);
		}
	}

	/**
	 * Writes XML attribute.
	 * 
	 * @param name String with attribute name.
	 * @param value String with attribute value.
	 * @throws XMLStreamException If there is an error
	 */
	public void writeAttribute(String name, String value)
		throws XMLStreamException
	{
		/*
		 * Write XML attribute
		 */
		try {
			writeStr(" " + name + "=\"" + value + "\"");
		} catch (IOException e) {
			throw new XMLStreamException("Error writing attribute \"" + name + "\": " + e);
		}
	}

	/**
	 * Writes XML namespace.
	 * 
	 * @param name String with namespace name.
	 * @param value String with namespace value.
	 * @throws XMLStreamException If there is an error
	 */
	public void writeNamespace(String name, String value)
		throws XMLStreamException
	{
		/*
		 * Write XML namespace
		 */
		try {
			writeStr(" xmlns:" + name + "=\"" + value + "\"");
		} catch (IOException e) {
			throw new XMLStreamException("Error writing namespace \"" + name + "\": " + e);
		}
	}

	/**
	 * Writes XML single element end.
	 * 
	 * @throws XMLStreamException If there is an error
	 */
	public void writeSingleElementEnd()
		throws XMLStreamException
	{
		/*
		 * Write XML single element end
		 */
		try {
			writeStr("/>");
			level--;
		} catch (IOException e) {
			throw new XMLStreamException("Error writing single element end: " + e);
		}
	}

	/**
	 * Writes XML start element end.
	 * 
	 * @throws XMLStreamException If there is an error
	 */
	public void writeStartElementEnd()
		throws XMLStreamException
	{
		/*
		 * Write XML start element end
		 */
		try {
			writeStr(">");
		} catch (IOException e) {
			throw new XMLStreamException("Error writing start element end: " + e);
		}
	}

	/**
	 * Writes XML end element.
	 * 
	 * @param element String with element name.
	 * @throws XMLStreamException If there is an error
	 */
	public void writeEndElement(String element)
		throws XMLStreamException
	{
		/*
		 * Write XML end element
		 */
		try {
			/*
			 * Don't put end element on new line if coming
			 * after text following a start element.
			 */
			if (!skipNewLine) {
				startNewLine(level);
			} else {
				skipNewLine = false;
			}
			level--;
			writeStr("</" + element + ">");
		} catch (IOException e) {
			throw new XMLStreamException("Error writing end element \"" + element + "\": " + e);
		}
	}

	/**
	 * Writes text between XML start and end tags.
	 * 
	 * @param text String with element text.
	 * @throws XMLStreamException If there is an error
	 */
	public void writeCharacters(String text)
		throws XMLStreamException
	{
		/*
		 * Write XML element text
		 */
		try {
			writeStr(text);
			/*
			 * Set flag to not put end element on
			 * new line, when it follows a text block.
			 */
			skipNewLine = true;
		} catch (IOException e) {
			throw new XMLStreamException("Error writing element text \"" + text + "\": " + e);
		}
	}

	/**
	 * Writes indented text with optional extra indentation.
	 * The indentation level counter is not updated.
	 * 
	 * @param text String with text.
	 * @param extraIndentation int optional extra indentation.
	 * @throws XMLStreamException If there is an error
	 */
	public void writeIndentedText(String text, int extraIndentation)
		throws XMLStreamException
	{
		/*
		 * Write indented text
		 */
		try {
			// An extra level is added as a new line is written
			startNewLine(level + 1 + extraIndentation);
			writeStr(text);
		} catch (IOException e) {
			throw new XMLStreamException("Error writing indented text \"" + text + "\": " + e);
		}
	}

	/**
	 * Flushes output stream.
	 * 
	 * @throws XMLStreamException If there is an error
	 */
	public void flush()
		throws XMLStreamException
	{
		/*
		 * Flush output stream.
		 */
		try {
			outStream.flush();
		} catch (IOException e) {
			System.out.println("Error flushing output stream: " + e);
			throw new XMLStreamException("Error flushing output stream: " + e);
		}
	}

	/**
	 * Close (not) output stream.
	 * 
	 * @throws XMLStreamException If there is an error
	 */
	public void close()
		throws XMLStreamException
	{
		/*
		 * Handle requests to close XMLCrudeWriterImpl stream.
		 * Ignored, as the output stream normally is
		 * closed from the object that calls XMLCrudeWriterImpl();
		 */
		try
		{
			outStream.close();
		} catch (IOException e) {
			System.out.println("Error closing output stream: " + e);
			throw new XMLStreamException("Error closing output stream: " + e);
		}
	}

	//------------------------------------------------------//

	/**
	 * Start new line with optional indentation.
	 * 
	 * @throws IOException If there is an error
	 */
	private void startNewLine(int indentLevel)
		throws IOException
	{
		/*
		 * Start new line with optional indentation.
		 */
		try {
			writeStr(lineSeparator);
			if (indentationUsed) {
				indent(indentLevel);
			}
		} catch (IOException e) {
			throw new IOException("Error starting new line " + ": " + e);
		}
	}

	/**
	 * Indent XML element.
	 * 
	 * @param indentLevel int with level of indentation.
	 * @throws IOException If there is an error
	 */
	private void indent(int indentLevel)
		throws IOException
	{
		/*
		 * Write indentation of XML element
		 *
		 * No indentation will be written
		 * if indentLevel < 1, specifically
		 * indentLevel == -1, 0.
		 */
		try {
			for (int i = 0; i < indentLevel; i++) {
				/*
				 * Default indentation is a <tab>
				 * character for each level.
				 */
				writeStr("\t");
			}
		} catch (IOException e) {
			throw new IOException("Error indenting element to level " + indentLevel + ": " + e);
		}
	}

}
