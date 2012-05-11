/*
	$Id: OwnableUtil.java 3207 2009-04-09 06:48:11Z gregory $

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

import org.proteios.core.data.OwnableData;

/**
	Utility methods that will make it easier to implement
	the {@link Ownable} interface, including data validation.
	
	@author Nicklas
	@version 2.0
	@see Ownable
	@base.modified $Date: 2009-04-08 23:48:11 -0700 (Wed, 08 Apr 2009) $
*/
public class OwnableUtil
{

	/**
		Set the owner of a {@link OwnableData} object.
		@throws InvalidDataException If the owner is null
	*/
	public static void setOwner(OwnableData ownableData, User owner)
		throws InvalidDataException
	{
		if (owner == null) throw new InvalidUseOfNullException("owner");
		ownableData.setOwner(owner.getData());
	}
	
}
