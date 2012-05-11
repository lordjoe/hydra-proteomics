/*
	$Id: NumberOutOfRangeException.java 3207 2009-04-09 06:48:11Z gregory $

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
	that only accepts numeric values within a certain range are 
	passed a value outside that range.

	@author Nicklas
	@version 2.0
*/
@SuppressWarnings("serial")
public class NumberOutOfRangeException
	extends InvalidDataException
{
	/**
		Create a new <code>NumberOutOfRangeException</code> object.
	*/
	public NumberOutOfRangeException()
	{
		super("Number out of range.");
	}

	/**
		Create a new <code>NumberOutOfRangeException</code> object when the 
		parameter is less than a minimum or greater than a maximum value.

		@param field The name of the field or parameter that is invalid
		@param theValue The value that is invalid
		@param minOrMaxValue The minimum or maximum allowed value
		@param tooBig TRUE if the value is greater than the maximum allowed, 
			FALSE if it is less than the minumum allowed
	*/
	public NumberOutOfRangeException(String field, long theValue, long minOrMaxValue, boolean tooBig)
	{
		super(tooBig ? 
			"Number out of range. The '"+field+"' must be lower than or equal to "+
				minOrMaxValue+". The supplied value, "+
				theValue+", is bigger."
			:
			"Number out of range. The '"+field+"' must be greater than or equal to "+
				minOrMaxValue+". The supplied value, "+
				theValue+", is lower."
		);
	}
	
	/**
		Create a new <code>NumberOutOfRangeException</code> object when the 
		parameter is outside both a minimum and a maximum allowed value.

		@param field The name of the field or parameter that is outside the range
		@param theValue The value that is outside the range
		@param minValue The minimum allowed value of the number
		@param maxValue The maximum allowed value of the number
	*/
	public NumberOutOfRangeException(String field, long theValue, long minValue, long maxValue)
	{
		super("Number out of range. The '"+field+"' must be in the range "+
			minValue+" to "+maxValue+". The supplied value, "+
			theValue+", is outside that range.");
	}

	/**
		Create a new <code>NumberOutOfRangeException</code> object when the 
		parameter is less than a minimum or greater than a maximum value.

		@param field The name of the field or parameter that is invalid
		@param theValue The value that is invalid
		@param minOrMaxValue The minimum or maximum allowed value
		@param tooBig TRUE if the value is greater than the maximum allowed, 
			FALSE if it is less than the minumum allowed
	*/
	public NumberOutOfRangeException(String field, double theValue, double minOrMaxValue, boolean tooBig)
	{
		super(tooBig ? 
			"Number out of range. The '"+field+"' must be lower than or equal to "+
				minOrMaxValue+". The supplied value, "+
				theValue+", is bigger."
			:
			"Number out of range. The '"+field+"' must be greater than or equal to "+
				minOrMaxValue+". The supplied value, "+
				theValue+", is lower."
		);
	}
	
	/**
		Create a new <code>NumberOutOfRangeException</code> object when the 
		parameter is outside both a minimum and a maximum allowed value.

		@param field The name of the field or parameter that is outside the range
		@param theValue The value that is outside the range
		@param minValue The minimum allowed value of the number
		@param maxValue The maximum allowed value of the number
	*/
	public NumberOutOfRangeException(String field, double theValue, double minValue, double maxValue)
	{
		super("Number out of range. The '"+field+"' must be in the range "+
			minValue+" to "+maxValue+". The supplied value, "+
			theValue+", is outside that range.");
	}
}


