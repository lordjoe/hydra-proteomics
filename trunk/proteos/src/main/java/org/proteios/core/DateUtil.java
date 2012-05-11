/*
	$Id: DateUtil.java 3207 2009-04-09 06:48:11Z gregory $

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

import java.text.SimpleDateFormat;
import java.util.Date;

/**
	Utility methods that will make it easier to handle date values.
	Especially, dates must be cloned whenver they are returned to
	or passed from client applications.

	@author Nicklas
	@version 2.0
*/
public class DateUtil
{

	/**
		Make a copy of a <code>Date</code> object. Null values are allowed.

		@param value The date to check
		@param name The name of the attribute to use if an exception is thrown
		@return A copy of the original date
	*/
	public static Date setNullableDate(Date value, String name)
	{
		return copy(value);
	}

	/**
		Make a copy of a <code>Date</code> object. Null values are not allowed.

		@param value The date to check
		@param name The name of the attribute to use if an exception is thrown
		@return A copy of the original date
	*/
	public static Date setNotNullDate(Date value, String name)
		throws InvalidUseOfNullException
	{
		if (value == null) throw new InvalidUseOfNullException(name);
		return copy(value);
	}


	/**
		Get a copy of the date. Null values are allowed.
	*/
	public static Date copy(Date value)
	{
		return value == null ? null : (Date)value.clone();
	}

	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
	public static Date parseString(String value)
		throws InvalidDataException
	{
		Date result = null;
		if (value != null)
		{
			try
			{
				result = DATE_FORMAT.parse(value);
			}
			catch (Exception ex)
			{
				try
				{
					result = new Date(new Long(value));
				}
				catch (Exception ex2)
				{
					throw new InvalidDataException("The value "+value+" is not a valid date.");
				}
			}
		}
		return result;
	}
}
