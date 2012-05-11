/*
	$Id: ItemAlreadyExistsException.java 3207 2009-04-09 06:48:11Z gregory $

	Copyright (C) 2006 Gregory Vincic, Jari Hakkinen, Olle Mansson
	Copyright (C) 2007 Gregory Vincic

	This file is part of Proteios.
	Available at http://www.proteios.org/

	Proteios is free software; you can redistribute it and/or modify it
	under the terms of the GNU General Public License as published by
	the Free Software Foundati	BioArray Software Environment (Proteios) - http://base.thep.lu.se/
	Copyright (C) 2002-2004 Lao Saal, Carl Troein,
	Johan Vallon-Christersson, Jari H�kkinen, Nicklas Nordborg

	This file is part of Proteios.

	Proteios is free software; you can redistribute it and/or
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
on; either version 2 of the License, or
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
	This exception is thrown when trying to save an object 
	and another object with the same ID already exists.

	@author Nicklas
	@version 2.0
*/
@SuppressWarnings("serial")
public class ItemAlreadyExistsException
	extends InvalidDataException
{
	
	/**
		Creates a new <code>ItemAlreadyExistsException</code>. The error
		message produced will look like:
		<code>Item already exists: User[Login=peter]

		@param what A description of what already exists, for 
			example User[Login=peter]
	*/
	public ItemAlreadyExistsException(String what)
	{
		super("Item already exists: "+what);
	}
	

}



