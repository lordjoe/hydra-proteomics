/*
	$Id: AccessControlled.java 3207 2009-04-09 06:48:11Z gregory $

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
	This interface should be implemented by a class representing items
	which requires the logged in user to have permission to access them.
	Since all items in Proteios should use access control, this interface should
	be implemented by all items.
	<p>
	The {@link BasicItem} which is the root superclass of all
	items provides an implementation that checks the role keys for
	access permission. Subclasses may override that implementation to
	also check for other things. For example, the {@link OwnedItem}
	class checks the item's owner against the logged in user.

	@author Nicklas
	@version 2.0
*/
public interface AccessControlled
{

	/**
		Check if the logged in user has the desired permission on
		the item.
		
		@return TRUE if the user has the permission, FALSE otherwise
	*/
	public boolean hasPermission(Permission permission);

	/**
		Check if the logged in user has the desired permission on
		the item, otherwise throw an exception.
		
		@throws PermissionDeniedException If the user doesn't have the 
			requested permission
	*/
	public void checkPermission(Permission permission)
		throws PermissionDeniedException;




}
