/*
 $Id: XMLExportUtil.java 3207 2009-04-09 06:48:11Z gregory $

 Copyright (C) 2006 Olle Mansson
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

/**
 * This class supports writing of special XML blocks. Its methods take an
 * XMLCrudeWriter implementation as argument. This should ensure the correct
 * indentation to be used.
 * 
 * @author Olle
 * @version 2.0
 * @proteios.modified $Date: 2006-10-31 11:38:54 +0200 (Tue, 31 Oct 2006) $
 */
public class XMLExportUtil
{
	/*
	 * XMLCrudeWriter implementation
	 */
	private XMLCrudeWriter xmlCrudeWriter;


	/**
	 * Get XMLCrudeWriter implementation.
	 * 
	 * @return xmlCrudeWriter the XMLCrudeWrite implementation.
	 */
	public XMLCrudeWriter getXMLCrudeWriter()
	{
		return this.xmlCrudeWriter;
	}


	/**
	 * Set XMLCrudeWriter implementation.
	 * 
	 * @param xmlCrudeWriter the XMLCrudeWrite implementation to use.
	 */
	public void setXMLCrudeWriter(XMLCrudeWriter xmlCrudeWriter)
	{
		this.xmlCrudeWriter = xmlCrudeWriter;
	}


	/**
	 * Default constructor.
	 */
	public XMLExportUtil()
	{}


	/**
	 * Constructor that takes an XMLCrudeWriter implementation as parameter.
	 * 
	 * @param xmlCrudeWriter The XMLCrudeWriter implementation to use.
	 */
	public XMLExportUtil(XMLCrudeWriter xmlCrudeWriter)
	{
		setXMLCrudeWriter(xmlCrudeWriter);
	}


	/**
	 * Writes cvLookup XML block. A cvLookup block contains information about
	 * the ontology/CV source used.
	 * 
	 * @param xmlCrudeWriter The XMLCrudeWriter implementation to use.
	 * @param cvLabel String the cvLabel of the ontology/CV source.
	 * @param fullName String the full name of the ontology/CV source.
	 * @param version String the version of the ontology/CV source.
	 * @param address String the URI of the ontology/CV source.
	 */
	public static void writeCvLookupBlock(XMLCrudeWriter xmlCrudeWriter,
			String cvLabel, String fullName, String version, String address)
	{
		/*
		 * Write cvLookup XML block.
		 */
		try
		{
			xmlCrudeWriter.writeStartElement("cvLookup");
			xmlCrudeWriter.writeAttribute("cvLabel", cvLabel);
			xmlCrudeWriter.writeAttribute("fullName", fullName);
			xmlCrudeWriter.writeAttribute("version", version);
			xmlCrudeWriter.writeAttribute("address", address);
			// xmlCrudeWriter.writeEndElement();
			xmlCrudeWriter.writeSingleElementEnd();
		}
		catch (XMLStreamException e)
		{
			// throw new XMLStreamException("Error writing cvParam block: " +
			// e);
		}
	}


	/**
	 * Writes cvParam XML block.
	 * 
	 * @param xmlCrudeWriter The XMLCrudeWriter implementation to use.
	 * @param cvLabel String the cvLabel.
	 * @param accession String the accession number.
	 * @param name String the name of the parameter.
	 * @param value String the value of the parameter.
	 */
	public static void writeCvParamBlock(XMLCrudeWriter xmlCrudeWriter,
			String cvLabel, String accession, String name, String value)
	{
		/*
		 * Write cvParam XML block.
		 */
		try
		{
			xmlCrudeWriter.writeStartElement("cvParam");
			xmlCrudeWriter.writeAttribute("cvLabel", cvLabel);
			xmlCrudeWriter.writeAttribute("accession", accession);
			xmlCrudeWriter.writeAttribute("name", name);
			xmlCrudeWriter.writeAttribute("value", value);
			// xmlCrudeWriter.writeEndElement();
			xmlCrudeWriter.writeSingleElementEnd();
		}
		catch (XMLStreamException e)
		{
			// throw new XMLStreamException("Error writing cvParam block: " +
			// e);
		}
	}


	/**
	 * Writes userParam XML block.
	 * 
	 * @param xmlCrudeWriter The XMLCrudeWriter implementation to use.
	 * @param name String the name of the parameter.
	 * @param value String the value of the parameter.
	 */
	public static void writeUserParamBlock(XMLCrudeWriter xmlCrudeWriter,
			String name, String value)
	{
		/*
		 * Write userParam XML block.
		 */
		try
		{
			xmlCrudeWriter.writeStartElement("userParam");
			xmlCrudeWriter.writeAttribute("name", name);
			xmlCrudeWriter.writeAttribute("value", value);
			// xmlCrudeWriter.writeEndElement();
			xmlCrudeWriter.writeSingleElementEnd();
		}
		catch (XMLStreamException e)
		{
			// throw new XMLStreamException("Error writing userParam block: " +
			// e);
		}
	}
}
