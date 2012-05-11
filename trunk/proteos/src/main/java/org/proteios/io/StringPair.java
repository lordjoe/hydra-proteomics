/*
 $Id: StringPair.java 3207 2009-04-09 06:48:11Z gregory $

 Copyright (C) 2009 Gregory Vincic

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

/**
 * This class contains methods to access a name/value pair of strings.
 * 
 * @author olle
 */
public class StringPair implements StringPairInterface
{
	private String name;
	private String value;
	
	/**
	 * Set the name.
	 * 
	 * @param name String The name to set.
	 */
	public void setName(String name)
	{
		this.name = name;
	}


	/**
	 * Set the value.
	 * 
	 * @param value String The value to set.
	 */
	public void setValue(String value)
	{
		this.value= value;
	}

	
	// -------------------------------------------
	/*
	 * From the StringPairInterface interface
	 * -------------------------------------------
	 */
	/**
	 * Get the name.
	 * 
	 * @return String The name.
	 */
	public String getName()
	{
		return this.name;
	}


	/**
	 * Get the value.
	 * 
	 * @return String The value.
	 */
	public String getValue()
	{
		return this.value;
	}
}
