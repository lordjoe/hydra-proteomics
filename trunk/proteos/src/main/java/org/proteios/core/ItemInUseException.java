/*
	$Id: ItemInUseException.java 3207 2009-04-09 06:48:11Z gregory $

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
	This exception is thrown when trying delete an item
	that is used (reference to) by some other item or items.

	@author Nicklas
	@version 2.0
	@base.modified $Date: 2009-04-08 23:48:11 -0700 (Wed, 08 Apr 2009) $
*/
@SuppressWarnings("serial")
public class ItemInUseException
	extends PermissionDeniedException
{

	/**
		Creates a new <code>ItemInUseException</code>. The error
		message produced will look like:
		<code>Permission denied. The item User[ID=325] is used by another item.</code>

		@param what A description of what already exists, for 
			example User[Id=325]
	*/
	public ItemInUseException(String what)
	{
		super("The item "+what+" is used by another item.");
	}
}
