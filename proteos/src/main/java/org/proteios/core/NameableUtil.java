/*
	$Id: NameableUtil.java 3207 2009-04-09 06:48:11Z gregory $

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

import org.proteios.core.data.NameableData;

/**
	Utility methods that will make it easier to implement
	the {@link Nameable} interface, including data validation.
	
	@author Nicklas
	@version 2.0
	@see Nameable
*/
public class NameableUtil
{

	/**
		Set the name of a {@link NameableData} object.
		@throws InvalidDataException If the name is null or is longer
			than {@link Nameable#MAX_NAME_LENGTH} constant
	*/
	public static void setName(NameableData nameableData, String name)
		throws InvalidDataException
	{
		nameableData.setName(
			StringUtil.setNotNullString(name, "name", Nameable.MAX_NAME_LENGTH)
		);
	}
	
	/**
		Set the description of a {@link NameableData} object. 
		@throws InvalidDataException If the description is longer
			than {@link Nameable#MAX_DESCRIPTION_LENGTH} constant
	*/
	public static void setDescription(NameableData nameableData, String description)
		throws InvalidDataException
	{
		nameableData.setDescription(
			StringUtil.setNullableString(description, "description", Nameable.MAX_DESCRIPTION_LENGTH)
		);
	}
}
