/*
	$Id: StringUtil.java 3207 2009-04-09 06:48:11Z gregory $

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
	Utility methods that will make it easier to implement
	data validation for string attributes.
	
	@author Nicklas
	@version 2.0
*/
public class StringUtil
{

	/**
		Trim and check the length of a string. Null values are allowed.

		@param value The string to check
		@param name The name of the attribute to use if an exception is thrown
		@param maxLength The maximum length of the string
		@return The original string, trimmed from white-space at the 
			beginning and end
		@throws StringTooLongException If the string is too long
	*/
	public static String setNullableString(String value, String name, int maxLength)
		throws StringTooLongException
	{
		if (value == null) return null;
		value = value.trim();
		if (value.length() > maxLength) throw new StringTooLongException(name, value, maxLength);
		return value;
	}
	
	/**
		Trim and check the length of a string. Null values are not allowed.

		@param value The string to check
		@param name The name of the attribute to use if an exception is thrown
		@param maxLength The maximum length of the string
		@return The original string, trimmed from white-space at the 
			beginning and end
		@throws StringTooLongException If the string is too long
		@throws InvalidUseOfNullException If the string is null
	*/
	public static String setNotNullString(String value, String name, int maxLength)
		throws InvalidUseOfNullException, StringTooLongException
	{
		if (value == null) throw new InvalidUseOfNullException(name);
		value = value.trim();
		if (value.length() > maxLength) throw new StringTooLongException(name, value, maxLength);
		return value;
	}
	
	/**
		Check if two strings are equal or both are null.
		@param s1 The first string
		@param s2 The second string
		@return TRUE if the strings are equal or both are null, FALSE otherwise
	*/
	public static boolean isEqualOrNull(String s1, String s2)
	{
		return (s1 == s2) || (s1 != null &&  s1.equals(s2));
	}

}
