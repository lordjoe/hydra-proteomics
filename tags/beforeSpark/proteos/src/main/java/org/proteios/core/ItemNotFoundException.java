/*
	$Id: ItemNotFoundException.java 3207 2009-04-09 06:48:11Z gregory $

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
	This exception is thrown when trying to load an item from
	the database that does not exist.

	@author Nicklas
	@version 2.0
*/
@SuppressWarnings("serial")
public class ItemNotFoundException
	extends InvalidDataException
{
	/**
		Creates a new <code>ItemNotFoundException</code>. The error
		message produced will look like:
		<code>Item not found: User[ID=1]</code>

		@param what A description of what was not found, for 
			example: User[ID=1]
	*/
	public ItemNotFoundException(String what)
	{
		super("Item not found: "+what);
	}

}



