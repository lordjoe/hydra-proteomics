/*
	$Id: IntegerUtil.java 3207 2009-04-09 06:48:11Z gregory $

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
	data validation for integer attributes.
	
	@author Nicklas, Enell
	@version 2.0
*/
public class IntegerUtil
{

	
	/**
		Get the int value of an <code>String</code>. If the <code>String</code>
		couldn't be parsed 0 is returned.
		@param value The <code>Integer</code>
		@return The value of the <code>Integer</code> or 0 if it is not parseable
	*/
	public static int getInt(String value)
	{
		return getInt(value, 0);
	}
	
	/**
		Get the int value of an <code>String</code>. If the <code>String</code>
		couldn't be parsed default value is returned.
		@param value The <code>Integer</code>
		@return The value of the <code>Integer</code> or defaultValue if it is not parseable
	*/
	public static int getInt(String value, int defaultValue)
	{
		try
		{
			return Integer.parseInt(value);
		}
		catch (NumberFormatException e)
		{
			return defaultValue;
		}
	}
	
	/**
		Get the value of an <code>Integer</code>.
		@param value The <code>Integer</code>
		@return The value of the <code>Integer</code> or 0 if it is null
	*/
	public static int getInt(Integer value)
	{
		return getInt(value, 0);
	}
	
	/**
		Get the value of an <code>Integer</code>.
		@param value The <code>Integer</code>
		@param defaultValue The default value to return if the value is null
		@return The value of the <code>Integer</code> or defaultValue if it is null
	*/
	public static int getInt(Integer value, int defaultValue)
	{
		return value == null ? defaultValue : value.intValue();
	}
	
	/**
		Check that an integer is equal or greater than a minimum value.
		@param value The value to check
		@param name The name of the attribute to use if an exception is thrown
		@param minValue The minimum allowed value
		@return The same value
		@throws NumberOutOfRangeException If the value lower than the minimum
	*/
	public static int checkMin(int value, String name, int minValue)
		throws NumberOutOfRangeException
	{
		if (value < minValue) throw new NumberOutOfRangeException(name, value, minValue, false);
		return value;
	}

	/**
		Check that an integer is equal or lower than a maximum value.
		@param value The value to check
		@param name The name of the attribute to use if an exception is thrown
		@param maxValue The maximum allowed value
		@return The same value
		@throws NumberOutOfRangeException If the value greater than the maximum
	*/
	public static int checkMax(int value, String name, int maxValue)
		throws NumberOutOfRangeException
	{
		if (value > maxValue) throw new NumberOutOfRangeException(name, value, maxValue, true);
		return value;
	}

	/**
		Check that an integer is equal or between a minimum and a maximum value.
		@param value The value to check
		@param name The name of the attribute to use if an exception is thrown
		@param minValue The minimum allowed value
		@param maxValue The maximum allowed value
		@return The same value
		@throws NumberOutOfRangeException If the value is outside the range
	*/
	public static int checkMinMax(int value, String name, int minValue, int maxValue)
		throws NumberOutOfRangeException
	{
		if (value < minValue || value > maxValue) throw new NumberOutOfRangeException(name, value, minValue, maxValue);
		return value;
	}

}
