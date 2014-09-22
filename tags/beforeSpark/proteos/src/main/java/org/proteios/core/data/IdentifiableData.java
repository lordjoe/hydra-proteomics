/*
	$Id: IdentifiableData.java 3207 2009-04-09 06:48:11Z gregory $

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
package org.proteios.core.data;

/**
	An identifiable item is an item which has an <code>id</code> and 
	a <code>version</code>. The id is always greater than 0 for an item
	which has been saved to the database, and 0 for a new item that has 
	not yet been saved to the database.
	<p>
	The version number is used by Hibernate to prevent concurrent modifications
	by different threads or processes. It does this by incrementing the version
	number each time the information is updated in the database. If the version 
	number in the database is different from the version on the object it is
	interpreted as another process has modified the item and an exception is
	thrown.
	<p>
	This interface is implemented by the {@link BasicData}
	class, which also provides the Hibernate mapping for the id property.
	<p>
	Hibernate also requires a <code>setId()</code> and a <code>setVersion()</code> method, 
	so this must also be implemented even though it is not required by this interface.

	@author Nicklas
	@version 2.0
	@see BasicData
	@see <a href="../../../../../../../development/overview/data/basic.html">Basic classes and interfaces</a>
*/
public interface IdentifiableData
{

	/**
		Get the id for the item. If it hasn't yet been saved to the
		database 0 is returned.
		@return The id of the item or 0
	*/
	public int getId();

	/**
		Get the version of this item. The version starts at 0, and is increased each time
		the information is updated. The version number is controlled by Hibernate.
	*/
	public int getVersion();
	
}
