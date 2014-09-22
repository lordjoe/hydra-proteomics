/*
 $Id: TandemFileReader.java 3207 2009-04-09 06:48:11Z gregory $
 
 Copyright (C) 2007 Gregory Vincic
 
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
package org.proteios.io;

import org.proteios.io.TandemFactory.SpectrumPathNote;
import java.io.InputStream;
import java.util.List;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

/**
 * Reads a tandem xml file
 * 
 * @author gregory
 */
public class TandemFileReader
		implements IdentificationFileInterface
{
	private XMLStreamReader parser;


	public TandemFileReader(InputStream stream) throws XMLStreamException
	{
		XMLInputFactory factory = XMLInputFactory.newInstance();
		parser = factory.createXMLStreamReader(stream);
	}


	public String getInputFileName()
	{
		XMLReaderUtil xml = new XMLReaderUtil();
		TandemFactory factory = new TandemFactory(parser);
		XMLTag template = factory.new SpectrumPathNote();
		xml.gotoTag(parser, template);
		SpectrumPathNote note;
		try
		{
			note = (SpectrumPathNote) xml
				.parse(parser, factory, template, null);
			if (note != null && note.path != null)
			{
				// Find filename in path
				String[] path = note.path.split("\\/");
				if (path.length > 0)
					return path[path.length - 1];
			}
		}
		catch (XMLStreamException e)
		{
			e.printStackTrace();
		}
		return null;
	}


	public List<HitInterface> listAllHits()
	{
		XMLReaderUtil xmlR = new XMLReaderUtil();
		TandemFactory factory = new TandemFactory(parser);
		XMLTag template = factory.new ModelGroup();
		// Extend the template here
		try
		{
			// For each <group type="model"> tag, see TandemFactory.Group class
			while (xmlR.gotoTag(parser, template))
			{
				XMLTag tag = xmlR.parse(parser, factory, template, null);
				if (tag instanceof TandemFactory.ModelGroup)
				{
					// Do something with each Group tag
					System.out.println(tag);
				}
			}
		}
		catch (XMLStreamException e)
		{
			e.printStackTrace();
		}
		return null;
	}
}
