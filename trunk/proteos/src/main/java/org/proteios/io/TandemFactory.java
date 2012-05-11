/*
 $Id: TandemFactory.java 3207 2009-04-09 06:48:11Z gregory $
 
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

import javax.xml.stream.XMLStreamReader;

/**
 * Useful when reading tandem xml files
 * 
 * @author gregory
 */
public class TandemFactory
		extends TagFactory
{
	public TandemFactory(XMLStreamReader reader)
	{
		super(reader);
	}


	public ModelGroup newModelGroup()
	{
		ModelGroup tag = new ModelGroup();
		tag.id = readInt("id");
		tag.z = readInt("z");
		tag.expect = readDouble("expect");
		tag.mh = readDouble("mh");
		tag.label = readString("label");
		return tag;
	}


	public Protein newProtein()
	{
		Protein tag = new Protein();
		tag.id = readFloat("id");
		tag.uid = readInt("uid");
		tag.expect = readDouble("expect");
		tag.sumI = readFloat("sumI");
		return tag;
	}


	public SpectrumPathNote newSpectrumPathNote()
	{
		SpectrumPathNote tag = new SpectrumPathNote();
		tag.path = readText();
		return tag;
	}


	public BioML newBioML()
	{
		BioML tag = new BioML();
		return tag;
	}

	/***************************************************************************
	 * Tag classes
	 */
	//
	public class BioML
			extends Tag
	{}

	public class Protein
			extends Tag
	{
		public Float sumI = null;
		public Double expect = null;
		public Integer uid = null;
		public Float id = null;


		@Override
		public String attributes()
		{
			return build("id", id, "uid", uid, "sumI", sumI, "expect", expect);
		}
	}

	public class ModelGroup
			extends XMLTag
	{
		public Integer id = null;
		public Integer z = null;
		public Double expect = null;
		public Double mh = null;
		public String label = null;


		@Override
		public String attributes()
		{
			return build("id", id, "z", z, "expect", expect, "mh", mh);
		}


		@Override
		public boolean matchStart(XMLStreamReader parser, XMLTag parent)
		{
			return ((super.matchStartTag(parser, "GROUP")) && matchAttribute(
				parser, "type", "model"));
		}


		@Override
		public boolean matchEnd(XMLStreamReader parser, XMLTag parent)
		{
			return super.matchEndTag(parser, "GROUP");
		}
	}

	public class SpectrumPathNote
			extends XMLTag
	{
		@Override
		public boolean matchEnd(XMLStreamReader parser, XMLTag parent)
		{
			return super.matchEndTag(parser, "NOTE");
		}


		@Override
		public boolean matchStart(XMLStreamReader parser, XMLTag parent)
		{
			return (super.matchStartTag(parser, "NOTE") && matchAttribute(
				parser, "label", "spectrum, path"));
		}

		public String path = null;


		@Override
		public String attributes()
		{
			return build("path", path);
		}
	}
}
