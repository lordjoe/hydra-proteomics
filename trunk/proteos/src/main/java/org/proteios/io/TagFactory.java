/*
 $Id: TagFactory.java 3207 2009-04-09 06:48:11Z gregory $
 
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

import java.lang.reflect.Method;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

/**
 * Extend this factory when you want to read an xml file and create a set of
 * objects out of each tag. Define newCLASS() methods for each tag you want to
 * match returning an object of type CLASS. For each </CLASS> tag that you want
 * to handel implement a done(CLASS c) method.
 * 
 * @author gregory
 */
public abstract class TagFactory
{
	protected XMLStreamReader reader;


	public TagFactory(XMLStreamReader reader)
	{
		if (reader == null)
			throw new RuntimeException(
				"XMLStreamReader in TagFactory cannot be null");
		this.reader = reader;
	}


	/**
	 * Reads value of the attribute name
	 * 
	 * @param key name of the attribute
	 * @return the read value
	 */
	protected String readString(String key)
	{
		return reader.getAttributeValue(null, key);
	}


	/**
	 * Read the content of a tag
	 * 
	 * @return the read value, or null if something goes wrong
	 */
	protected String readText()
	{
		try
		{
			return reader.getElementText();
		}
		catch (XMLStreamException e)
		{
			return null;
		}
	}


	/**
	 * Convenience method to read an attribute and convert it to a Long
	 * 
	 * @param key name of the attribute
	 * @return a Long value
	 * @throws NumberFormatException if the value is not a valid Long
	 */
	protected Long readLong(String key)
	{
		return Long.valueOf(readString(key));
	}


	/**
	 * Convenience method to read an attribute and convert it to an Integer
	 * 
	 * @param key name of the attribute
	 * @return an Integer
	 * @throws NumberFormatException if the value is not a valid Integer
	 */
	protected Integer readInt(String key)
	{
		return Integer.valueOf(readString(key));
	}


	/**
	 * The same as readInt(String)
	 * 
	 * @param key name of the attribute
	 * @return an Integer
	 */
	protected Integer readInteger(String key)
	{
		return readInt(key);
	}


	/**
	 * Convenience method to read an attribute and convert it to a Float.
	 * 
	 * @param key name of the attribute
	 * @return a Float
	 * @throws NumberFormatException if the value is not a Float
	 */
	protected Float readFloat(String key)
	{
		return Float.valueOf(readString(key));
	}


	/**
	 * Convenience method to read an attribute and convert it to a Float.
	 * 
	 * @param key name of the attribute
	 * @return a Float
	 * @throws NumberFormatException if the value is not a Float
	 */
	protected Double readDouble(String key)
	{
		return Double.valueOf(readString(key));
	}


	/**
	 * This method tries to find a method in this factory named "newCLASS" where
	 * the CLASS is the classname of the template. If no method is found an
	 * error is printed to System.err
	 * 
	 * @param <E> a XMLTag class
	 * @param template class to find
	 * @return new <E> object or null if the newCLASS method returns null or is
	 *         not found
	 */
	@SuppressWarnings("unchecked")
	public <E extends XMLTag> E create(E template)
	{
		if (template != null)
		{
			String methodName = "new" + template.getClass().getSimpleName();
			try
			{
				Method m = this.getClass().getDeclaredMethod(methodName);
				return (E) m.invoke(this);
			}
			catch (Exception e)
			{
				System.err.println(e.getLocalizedMessage());
			}
		}
		return null;
	}


	/**
	 * Tries to find the method done(E) and calls it with the supplied object.
	 * 
	 * @param <E>
	 * @param tag to end
	 */
	public <E extends XMLTag> void done(E tag)
	{
		if (tag != null)
		{
			String methodName = "done";
			try
			{
				Method m = this.getClass().getDeclaredMethod(methodName,
					tag.getClass());
				m.invoke(this, tag);
			}
			catch (Exception e)
			{
				System.err.println(e.getLocalizedMessage());
			}
		}
	}
}