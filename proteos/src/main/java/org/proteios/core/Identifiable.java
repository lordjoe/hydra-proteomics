/*
	$Id: Identifiable.java 3207 2009-04-09 06:48:11Z gregory $

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
	An <code>Identifiable</code> item is an item which has an
	<code>id</code> a <code>type</code> and a <code>version</code>.
	The id value is always greater than 0 for an existing item, and 0 for
	a new item that has not yet been saved to the database.
	<p>
	The type is an value selected from the constants
	defined by the {@link Item} enumeration.

	@author Nicklas
	@version 2.0
	@see BasicItem
	@see Item
	@base.modified $Date: 2009-04-08 23:48:11 -0700 (Wed, 08 Apr 2009) $
*/
public interface Identifiable
	extends AccessControlled
{

	/**
		Get the id for the item. If it hasn't yet been saved to the
		database 0 is returned.
		@return The id of the item or 0
	*/
	public int getId();

	/**
		Get the type of item represented by the object. The returned
		value is one of the values defined in the {@link Item} enumeration.
		@return A value indicating the type of item
	*/
	public Item getType();
	
	/**
		Get the version number of the item. The version number is incremented each time
		the item is updated and is used to prevent that the same item is updated at
		the same time by two or more different threads or processes.
	*/
	public int getVersion();

}
