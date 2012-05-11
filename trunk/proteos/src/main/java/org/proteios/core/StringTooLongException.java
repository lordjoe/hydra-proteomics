/*
	$Id: StringTooLongException.java 3207 2009-04-09 06:48:11Z gregory $

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
package org.proteios.core;

/**
	This exception is thrown when an item's field or a parameter
	which must be shorter than a specified length exceeds that length.

	@author Nicklas
	@version 2.0
*/
@SuppressWarnings("serial")
public class StringTooLongException
	extends InvalidDataException
{
	/**
		Create a new <code>StringTooLongException</code> object.
	*/
	public StringTooLongException()
	{
		super("String too long.");
	}

	/**
		Create a new <code>StringTooLongException</code> object.

		@param field The name of the field or parameter that must 
			have a shorter value
		@param theString The string that is too long
		@param maxLength The maximum allowed length of the string
	*/
	public StringTooLongException(String field, String theString, int maxLength)
	{
		super("String too long. The '"+field+"' mustn't be longer than "+maxLength+
			" characters. The supplied value '"+theString+"' is "+theString.length()+
			" characters.");
	}

}


